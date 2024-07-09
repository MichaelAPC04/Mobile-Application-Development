package com.example.a3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.assignment_1_michael_cetrola.provider.EMAViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.Key;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListEvent extends Fragment {

    ArrayList<Event> listEvent = new ArrayList<>();
    MyRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Gson gson = new Gson();
    private EMAViewModel emaViewModel;
    MyRecyclerAdapter eventAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListEvent newInstance(String param1, String param2) {
        FragmentListEvent fragment = new FragmentListEvent();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_event, container, false);

        recyclerView = view.findViewById(R.id.rv_Event);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        eventAdapter = new MyRecyclerAdapter();

        // Load SP gson
//        SharedPreferences sP = getActivity().getSharedPreferences(KeyStore.SP4_FILENAME, Context.MODE_PRIVATE);
//        String json = sP.getString(KeyStore.SP5_FILENAME, null);
//
//        Type type = new TypeToken<ArrayList<Event>>(){}.getType();
//        listEvent = gson.fromJson(json, type);

        emaViewModel = new ViewModelProvider(this).get(EMAViewModel.class);

        emaViewModel.getAllEvents().observe(getViewLifecycleOwner(), newData -> {
            eventAdapter.setEventData(new ArrayList<Event>(newData));
            eventAdapter.notifyDataSetChanged();
        });

        //recyclerAdapter.setEventData(listEvent);
        recyclerView.setAdapter(eventAdapter);
        //recyclerAdapter.notifyDataSetChanged();
        return view;

    }

}