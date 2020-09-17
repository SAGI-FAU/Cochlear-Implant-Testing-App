package com.example.cit_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MinimalPairsAdapter extends RecyclerView.Adapter<MinimalPairsAdapter.ViewHolder>{

    String correct_words[], chosen_words[];
    int images[];
    Context context;
    OnExerciseListener exListener;

    public MinimalPairsAdapter(Context con, String s1[], String s2[], int images1[], OnExerciseListener exList) {
        context = con;
        correct_words = s1;
        chosen_words = s2;
        images = images1;
        exListener = exList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.minimal_pairs_finished_element, parent, false);
        return new ViewHolder(view, exListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.correctWord.setText(correct_words[position]);
        holder.yourWord.setText(chosen_words[position]);
        holder.image.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return chosen_words.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView correctWord, yourWord;
        ImageView image;
        OnExerciseListener exListener;
        public ViewHolder(@NonNull View itemView, OnExerciseListener exerciseListener) {
            super(itemView);
            correctWord = itemView.findViewById(R.id.correctWord);
            yourWord = itemView.findViewById(R.id.yourWord);
            image = itemView.findViewById(R.id.correct);
            itemView.setOnClickListener(this);
            exListener = exerciseListener;
        }

        @Override
        public void onClick(View v) {
            exListener.onExerciseClick(getAdapterPosition());
        }
    }

    public interface OnExerciseListener {
        void onExerciseClick(int position);
    }
}
