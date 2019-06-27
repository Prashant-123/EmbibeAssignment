package com.embibeassignment.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.embibeassignment.models.MovieModel;
import com.embibeassignment.R;
import com.embibeassignment.adapter.MovieAdapter;
import com.embibeassignment.utils.BackgroundService;
import com.embibeassignment.utils.DBHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class Movies extends Fragment {

    public Movies() {}
    
    public static final String TAG =  "TAG";
    private static final String CHANNEL_ID = "1";
    private DBHelper db;
    private MaterialToolbar toolbar;
    private RecyclerView rv;
    private MovieAdapter adapter;
    public static final ArrayList<MovieModel> movie_list = new ArrayList<>();
    public static final String BROADCAST_ACTION = "broadcast_action";
    public static final String BROADCAST_DATA = "broadcast_data";
    private IntentFilter intentFilter;
    private Intent serviceIntent;
    private Context context;

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
        setupNotificationChannel();

        context = view.getContext();
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        serviceIntent = new Intent(getContext(), BackgroundService.class);

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(null);

        movie_list.clear();
        view.getContext().startService(serviceIntent);

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
        getContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        getContext().unregisterReceiver(receiver);
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
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MovieModel movie = db.getMovie(intent.getStringExtra(BROADCAST_DATA));
            movie_list.add(movie);

            Notify(movie.imageUrl, movie.title, movie.overview);

            adapter.notifyDataSetChanged();
        }
    };

    private void Notify(final String url, final String title, final String overview) {


        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loaded bitmap is here (bitmap)
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_icon)
                        .setContentTitle("New Movie Available: " + title)
                        .setLargeIcon(bitmap)
                        .setContentText(overview)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(overview));
                int id = 0;
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(id++, builder.build());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
    }

    private void setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel 1");

            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
