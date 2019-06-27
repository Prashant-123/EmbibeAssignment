package com.embibeassignment.utils;

import android.widget.Filter;

import com.embibeassignment.models.MovieModel;
import com.embibeassignment.adapter.MovieAdapter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    MovieAdapter adapter ;
    ArrayList<MovieModel> filterList ;
    public CustomFilter(ArrayList<MovieModel> filterList, MovieAdapter adapter)
    {

        this.adapter = adapter;
        this.filterList = filterList ;
    }
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        if(charSequence != null && charSequence.length() > 0 )
        {
            charSequence= charSequence.toString().toUpperCase() ;
            ArrayList<MovieModel> filteredTeacher = new ArrayList<>() ;

            for(int i = 0 ; i < filterList.size() ; i++)
            {
                if(filterList.get(i).getTitle().toUpperCase().contains(charSequence))
                {
                    filteredTeacher.add(filterList.get(i)) ;
                }
            }
            results.count = filteredTeacher.size() ;
            results.values = filteredTeacher ;
        }
        else
        {
            results.count = filterList.size() ;
            results.values = filterList ;
        }

        return results ;
    }
    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.movies = (ArrayList<MovieModel>) filterResults.values;
        adapter.notifyDataSetChanged();
    }
}
