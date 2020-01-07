package com.example.hw1;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HighScoreFragment extends Fragment {
    View v;
    private ArrayList<GameUser> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private FragmentHighScoreListener listener;
    public interface FragmentHighScoreListener {
        void onGameUserInfoSent(GameUser user);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_highscore, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        GameRecyclerAdapter exampleAdapter = new GameRecyclerAdapter(getContext(), list);
        exampleAdapter.setOnItemClickListener(position->{
                GameUser gameuser = list.get(position);
                listener.onGameUserInfoSent(gameuser);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(exampleAdapter);
        return v;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            MySharedPreferences shared = new MySharedPreferences(getContext());
            list = shared.readDataFromStorage();
        }

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof FragmentHighScoreListener ){
            // if our activity implements this interface
            listener = (FragmentHighScoreListener ) context;

        }else{
            throw new RuntimeException(context.toString() + "must implements FragmentHighScoreListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        v = null; // now cleaning up!
    }
}