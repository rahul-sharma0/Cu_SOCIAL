package com.example.cusocial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Group_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Group_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Group_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Group_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Group_fragment newInstance(String param1, String param2) {
        Group_fragment fragment = new Group_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ListView groupListView;
    private DatabaseReference groupsRef;
    private ArrayList<Group_Adapter> groupList;
//    private ArrayAdapter<Group_Adapter> groupAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    ArrayList<Group_Adapter> group_ArrayList;
    ArrayList<Users_Adapter> users_ArrayList;
    Group_recycler_Adapter groupAdapter;
    SearchView searchView;
    Users_Adapter users_Adapter;
    Activity context;
    Context this_context;

FloatingActionButton create_group;
    String role=null;
    @SuppressLint("SuspiciousIndentation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_fragment, container, false);
/*
        groupListView = view.findViewById(R.id.group_recycler);
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups");

        groupList = new ArrayList<>();
        groupAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, groupList);
        groupListView.setAdapter(groupAdapter);

        // Fetch groups from Firebase
        fetchGroups();

        // Handle item click to open the group chat
       groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Group_Adapter selectedGroup = groupList.get(position);
               Intent intent = new Intent(view.getContext(), Group_Chat_Windo.class);
//               intent.putExtra("groupId", selectedGroup.()); // Pass the group ID
               startActivity(intent);
           }
       });
*/





        firebaseAuth  =FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        create_group = view.findViewById(R.id.create_group);
        create_group.setVisibility(View.GONE);

        DatabaseReference databaseReference = firebaseDatabase.getReference("Groups");
//        DatabaseReference reference = firebaseDatabase.getReference("Groups").child("Group").child("Group Members");
        group_ArrayList = new ArrayList<>();

        DatabaseReference reference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                     role = snapshot.child("role").getValue().toString();
                    if (role.equals("Faculty"))
                        create_group.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        reference.addValueEventListener(new ValueEventListener() {
//         @Override
//         public void onDataChange(@NonNull DataSnapshot snapshot) {
//         for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//         Users_Adapter users_Adapter = dataSnapshot.getValue(Users_Adapter.class);
////                    DatabaseReference reference = firebaseDatabase.getReference("Groups").child(Group_Adapter.class.getName()).child("Group Members");
//         databaseReference.addValueEventListener(new ValueEventListener() {
//         @Override
//         public void onDataChange(@NonNull DataSnapshot snapshot) {
//         for (DataSnapshot Snapshot : snapshot.getChildren()) {
//            Group_Adapter group_adapter = Snapshot.getValue(Group_Adapter.class);
//         if (users_Adapter.getUser_id().equals(firebaseAuth.getUid())) {
//            group_ArrayList.add(group_adapter);
//         }
//         }
//         groupAdapter.notifyDataSetChanged();
//         }
//         @Override
//         public void onCancelled(@NonNull DatabaseError error) {
//
//                                                        }
//                                                    });
////                    Users_Adapter users_Adapter = dataSnapshot.getValue(Users_Adapter.class);
//
//                    if (users_Adapter.getUser_id().equals(firebaseAuth.getUid())) {
//
//                        group_ArrayList.add(group_adapter);
//                    }
//                                                }
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });



            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    group_ArrayList.clear();
                    for (DataSnapshot Snapshot : snapshot.getChildren())
                    {
                        Group_Adapter group_adapter = Snapshot.getValue(Group_Adapter.class);

                        DatabaseReference reference = firebaseDatabase.getReference("Groups").child(group_adapter.getGroup_name()).child("Group Members");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                 Users_Adapter users_adapter = dataSnapshot.getValue(Users_Adapter.class);
                                    if (users_adapter.getUser_id().equals(firebaseAuth.getUid()))
                                       {
                                            group_ArrayList.add(group_adapter);
                                        }
                                }
                                groupAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
//                    groupAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        recyclerView = view.findViewById(R.id.group_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupAdapter = new Group_recycler_Adapter(Group_fragment.this, group_ArrayList);
        recyclerView.setAdapter(groupAdapter);

        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Group_fragment.this.getContext(), create_group.class));
            }
        });

        searchView = view.findViewById(R.id.group_searchView);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });



        return view;

    }
    private void search(String s) {
        ArrayList<Group_Adapter> filteredList = new ArrayList<>();
        for (Group_Adapter item : group_ArrayList) {
            if (item.getGroup_name().toLowerCase().contains(s.toLowerCase()))
                filteredList.add(item);

        }
        groupAdapter.filterList(filteredList);
    }

    private void fetchGroups() {
        groupsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Group_Adapter group = snapshot.getValue(Group_Adapter.class);
                groupList.add(group);
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}