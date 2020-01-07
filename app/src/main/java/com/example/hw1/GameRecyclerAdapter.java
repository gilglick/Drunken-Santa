package com.example.hw1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GameRecyclerAdapter extends RecyclerView.Adapter<GameRecyclerAdapter.ScoreViewHolder> {
    private ArrayList<GameUser> mExampleList;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public GameRecyclerAdapter(Context context, ArrayList<GameUser> exampleList) {
        this.context = context;
        if (exampleList != null) {
            mExampleList = exampleList;
        } else {
            mExampleList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ScoreViewHolder(v, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        GameUser currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getPlayerCharacter());
        holder.mTextView1.setText(currentItem.getUserName());
        holder.mScore.setText("Score: " + currentItem.getScore());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView1;
        private TextView mScore;

        public ScoreViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mScore = itemView.findViewById(R.id.textScore);
            itemView.setOnClickListener(e -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }


}
