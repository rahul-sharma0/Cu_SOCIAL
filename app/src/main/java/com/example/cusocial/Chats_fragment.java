package com.example.cusocial;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chats_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chats_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Chats_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chats_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Chats_fragment newInstance(String param1, String param2) {
        Chats_fragment fragment = new Chats_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    RecyclerView recyclerView;
    ArrayList<Users_Adapter> users_ArrayList;
    User_recycler_adapter adapter;
    Activity context;
    Context this_context;
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chats_fragment, container, false);

        this_context = Chats_fragment.this.getContext();
        context=getActivity();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Users");

        users_ArrayList=new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users_ArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Users_Adapter users_Adapter = dataSnapshot.getValue(Users_Adapter.class);
                    if (users_Adapter.getUser_id().equals(firebaseAuth.getUid())) {
                        continue;
                    } else {
                        users_ArrayList.add(users_Adapter);
                    }
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = view.findViewById(R.id.chats_recycler);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this_context));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new User_recycler_adapter(Chats_fragment.this, users_ArrayList);
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.chats_searchView);
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
        ArrayList<Users_Adapter> filteredList = new ArrayList<>();
        for (Users_Adapter item : users_ArrayList) {
            if (item.getUID().toLowerCase().contains(s.toLowerCase()))
                filteredList.add(item);

        }
        adapter.filterList(filteredList);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}