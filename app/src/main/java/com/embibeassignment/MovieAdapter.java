package com.embibeassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements Filterable {

    public Context context;
    public LayoutInflater inflater;
    public ArrayList<MovieModel> movies;
    public ArrayList<MovieModel> filterList;
    public CustomFilter filter;

    public MovieAdapter(Context context, ArrayList<MovieModel> movies) {
        this.movies = movies;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.filterList = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_model, parent, false);
        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        final int position = holder.getAdapterPosition();

        holder.title.setText(movies.get(position).title);
        holder.year.setText(movies.get(position).year);
        holder.ratingBar.setRating(movies.get(position).rating);

        Glide.with(holder.itemView).load(movies.get(position).imageUrl)
                .into(holder.poster);

        holder.model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("id", movies.get(position).id);
                bundle.putInt("index", position);
                Fragment webView = new WebViewFragment();
                webView.setArguments(bundle);

                ft.replace(R.id.main_frame, webView);
                ft.commit();
                ft.addToBackStack(null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, year;
        RatingBar ratingBar;
        ImageView poster;
        CircularRevealCardView model;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            year = itemView.findViewById(R.id.year);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            poster = itemView.findViewById(R.id.poster);
            model = itemView.findViewById(R.id.model);
        }
    }
}
