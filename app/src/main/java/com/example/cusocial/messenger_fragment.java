package com.example.cusocial;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link messenger_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class messenger_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public messenger_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment messenger_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static messenger_fragment newInstance(String param1, String param2) {
        messenger_fragment fragment = new messenger_fragment();
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
    Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context =getActivity();


//        tabLayout.getTabAt(0).setIcon(R.drawable.chats_lines_svgrepo_com);
//        tabLayout.getTabAt(1).setIcon(R.drawable.groups_svgrepo_com);
//        tabLayout.getTabAt(2).setIcon(R.drawable.phone_in_talk_svgrepo_com);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messenger_fragment, container, false);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        ViewPager viewPager = context.findViewById(R.id.view_pager);
        TabLayout tabLayout = context.findViewById(R.id.tab_layout);

        ViewPager_Messenger_Adapter viewPagerMessengerAdapter = new ViewPager_Messenger_Adapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerMessengerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}