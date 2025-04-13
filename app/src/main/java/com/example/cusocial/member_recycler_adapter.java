package com.example.cusocial;

import static androidx.core.content.ContextCompat.startActivity;
import static java.lang.Integer.parseInt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class member_recycler_adapter extends RecyclerView.Adapter<member_recycler_adapter.ViewHolder> {
    create_group createGroup;
    int count =0;String Group_name ;
    ArrayList<Users_Adapter> usersArrayList, active_User_ArrayList;
    TextView member_count;
    Button group_btn;
    ArrayList<Users_Adapter> groupMemberArrayList = new ArrayList<>();
    EditText Grp_name;

    public member_recycler_adapter(create_group createGroup, ArrayList<Users_Adapter> usersArrayList, TextView member_count, Button group_btn, EditText Group_Name, ArrayList<Users_Adapter> active_User_Adapter) {
        this.createGroup = createGroup;
        this.usersArrayList = usersArrayList;
        this.member_count = member_count;
        this.group_btn = group_btn;
        this.Grp_name = Group_Name;
        this.active_User_ArrayList = active_User_Adapter;
    }

    @NonNull
    @Override
    public member_recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(createGroup.getApplicationContext()).inflate(R.layout.user_group_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull member_recycler_adapter.ViewHolder holder, int position) {
//        int count = 0;

        Users_Adapter Active_user_Adapter = active_User_ArrayList.get(0);
        Users_Adapter usersAdapter = usersArrayList.get(position);
//        Users_Adapter users_Adapter1  = new Users_Adapter(Users_Adapter.class);
        holder.User_name.setText(usersAdapter.getUID());
        holder.User_status.setText(usersAdapter.getFULLNAME());
        if (holder.circleImageView != null) {
//            holder.circleImageView.setImageResource(R.drawable.users); // Replace with your image resource
            Picasso.get().load(usersAdapter.IMAGE).into(holder.circleImageView);

        } else {
            // Handle the case where profileImageView is null (e.g., log an error, use a default view, etc.)
            Log.e("User_recycler_adapter", "profileImageView is null!");
//            holder.circleImageView.setImageResource(R.drawable.users);

//            Picasso.get().load(usersAdapter.IMAGE).into(holder.circleImageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.member_check.isChecked()) {
                    count -= 1;
                    if (count <1) {
                        member_count.setVisibility(View.INVISIBLE);
                    }
                    holder.member_check.setChecked(false);

                    member_count.setText(String.valueOf(count));

                    groupMemberArrayList.remove(usersAdapter);

                    Log.e("member count", "" + groupMemberArrayList);
                } else {
                    count += 1;
                    if (count >=1) {
                        member_count.setVisibility(View.VISIBLE);
                    }
               //                    holder.member_check.setChecked(true);

                    member_count.setText(String.valueOf(count));
                    Log.e("member count", "" + count);

                    groupMemberArrayList.add(usersAdapter);
                    Log.e("member count", "" + groupMemberArrayList);
                holder.member_check.setChecked(true);

//        Log.e("msg",Group_name);
                }
            }

        });



        group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group_name= Grp_name.getText().toString();
                if (Group_name.isEmpty()) {
                    Grp_name.setError("Enter Group Name");
                    Toast.makeText(createGroup, "Group name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }ProgressDialog progressDialog = new ProgressDialog(createGroup);
                progressDialog.setMessage("Creating group...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                groupMemberArrayList.add(Active_user_Adapter);

                int i =0;

                Group_Adapter group_Adapter = new Group_Adapter(Group_name);

               DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(Group_name);
                databaseReference.setValue(group_Adapter).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(createGroup, "Group name created successfully", Toast.LENGTH_SHORT).show();
                    }
                });
//                reference.child(Active_user_Adapter.getUser_id()).setValue(Active_user_Adapter);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups").child(Group_name).child("Group Members");


                for ( ; i<groupMemberArrayList.size(); i++) {
                reference.child(groupMemberArrayList.get(i).getUser_id()).setValue(groupMemberArrayList.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//

                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();

                        }
                    }
                });
                }
                Toast.makeText(createGroup, "Group created successfully", Toast.LENGTH_LONG).show();
                createGroup.startActivity(new Intent(createGroup.getApplicationContext(),MainActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }
    public void filterList(ArrayList<Users_Adapter> filteredList) {
        usersArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView User_name, User_status;
        CheckBox member_check;
//        int count =0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            member_check = itemView.findViewById(R.id.member_check);
            circleImageView = itemView.findViewById(R.id.group_img_circular);
            User_name = itemView.findViewById(R.id.group_name);
            User_status = itemView.findViewById(R.id.group_last_message);

//            count = Integer.parseInt(member_count.getText().toString());
        }
    }

}
