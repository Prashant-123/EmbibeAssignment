package com.embibeassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movies extends Fragment {

    public Movies() {}
    
    public static final String TAG =  "TAG";

    private DBHelper db;
    private MaterialToolbar toolbar;
    private RecyclerView rv;
    private MovieAdapter adapter;
    public static ArrayList<MovieModel> movie_list = new ArrayList<>();
    private String API_KEY = BuildConfig.API_KEY;
    public static JSONArray results;

    public static final String BROADCAST_ACTION = "broadcast_action";
    public static final String BROADCAST_DATA = "broadcast_data";
    private IntentFilter intentFilter;
    private Intent serviceIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies, container, false);
        db = new DBHelper(view.getContext());

        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        serviceIntent = new Intent(getContext(), BackgroundService.class);

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(null);

        FetchMovies(view);

        adapter = new MovieAdapter(view.getContext(), movie_list);

        rv = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(reciever, intentFilter);
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(reciever);
        getContext().stopService(serviceIntent);
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }
    private BroadcastReceiver reciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MovieModel movie = db.getMovie(intent.getStringExtra(BROADCAST_DATA));
            movie_list.add(movie);
            adapter.notifyDataSetChanged();
            Log.i(TAG, "onReceive: " + movie.title);
        }
    };

//    API call to get top_rated movie from server
    public void FetchMovies(final View view) {
        AndroidNetworking.get("https://api.themoviedb.org/3/movie/{type}?api_key={api_key}&page={page_no}")
                .addPathParameter("api_key", API_KEY)
                .addPathParameter("type", "top_rated")
                .addPathParameter("page_no", "1")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            results = response.getJSONArray("results");

                            view.getContext().startService(serviceIntent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i(TAG, "onError: " + anError);
                    }
                });
    }
}
