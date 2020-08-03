package com.example.cit_app.tools;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.cit_app.Exercises;
import com.example.cit_app.Model;
import com.example.cit_app.R;
import com.example.cit_app.other_activities.MainActivity;
import com.example.cit_app.other_activities.ResultsPerDay;
import com.example.cit_app.other_activities.ResultsPerYear;
import com.example.cit_app.other_activities.Trainingset;

import java.util.List;

public class Adapter extends PagerAdapter {

    private List<Model> models;
    private Context context;
    private CardView cardView;

    public Adapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.main_item, container, false);

        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.image_item);
        title = view.findViewById(R.id.title_item);
        desc = view.findViewById(R.id.desc_item);
        cardView = view.findViewById(R.id.item_cardView);

        imageView.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch(title.getText().toString()) {
                    case "Daily Session":
                    case "Tägliche Einheit":
                        intent = new Intent(v.getContext(), Trainingset.class);
                        v.getContext().startActivity(intent);break;

                    case "Exercise List":
                    case "Übungen":
                        intent = new Intent(v.getContext(), Exercises.class);
                        v.getContext().startActivity(intent);break;

                    case "Daily Results":
                    case "Ergebnis des Tages":
                        intent = new Intent(v.getContext(), ResultsPerDay.class);
                        v.getContext().startActivity(intent);break;

                    case "Course of the year":
                    case "Verlauf des Jahres":
                        intent = new Intent(v.getContext(), ResultsPerYear.class);
                        v.getContext().startActivity(intent);break;

                    default:
                        intent = new Intent(v.getContext(), MainActivity.class);
                        v.getContext().startActivity(intent);break;
                }
            }
        });
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
