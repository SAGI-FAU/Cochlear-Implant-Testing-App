package com.example.cit_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder>{

    String elements[];
    int images[];
    Context context;
    OnExerciseListener exListener;

    public ExerciseAdapter(Context con, String s1[], int images1[], OnExerciseListener exList) {
        context = con;
        elements = s1;
        images = images1;
        exListener = exList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercise_element, parent, false);
        return new ViewHolder(view, exListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(elements[position]);
        holder.image.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return elements.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView image;
        OnExerciseListener exListener;
        public ViewHolder(@NonNull View itemView, OnExerciseListener exerciseListener) {
            super(itemView);
            title = itemView.findViewById(R.id.exerciseName);
            image = itemView.findViewById(R.id.imageView);
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
