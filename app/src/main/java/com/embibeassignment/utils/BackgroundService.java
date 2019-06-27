package com.embibeassignment.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.embibeassignment.BuildConfig;
import com.embibeassignment.R;
import com.embibeassignment.ui.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.embibeassignment.ui.Movies.TAG;
import static com.embibeassignment.ui.MainActivity.results;

public class BackgroundService extends Service {

    private Timer timer;
    private DBHelper db;
    private TimerTask task;

    @Override
    public IBinder onBind(Intent intent) {
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = new DBHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        task.cancel();
        timer.purge();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        task = new TimerTask() {
            int index = 0;
            @Override
            public void run() {
                if (index < results.length()) {
                    try {
                        JSONObject response = (JSONObject) results.get(index);

                        GetImdbId(response.getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    index++;
                } else {
                    timer.cancel();
                }
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 5000);

        return START_STICKY;
    }


    private void GetImdbId(String tmdb_id) {
        String API_KEY = BuildConfig.API_KEY;
        AndroidNetworking.get("https://api.themoviedb.org/3/movie/{id}?api_key={api_key}")
                .addPathParameter("id", tmdb_id)
                .addPathParameter("api_key", API_KEY)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String title = response.getString("title");
                    String imageUrl = "https://image.tmdb.org/t/p/w400".concat(response.getString("poster_path"));
                    String year = response.getString("release_date").substring(0, 4);
                    String id = response.getString("imdb_id");
                    String overview = response.getString("overview");
                    float rating = Float.parseFloat(String.valueOf(response.getDouble("vote_average")))/2;

                    StringBuilder genre = new StringBuilder();

                    JSONArray genres = response.getJSONArray("genres");
                    for (int i=0; i<genres.length(); i++) {
                        JSONObject g = genres.getJSONObject(i);
                        genre.append(g.getString("name").concat(" | "));

                        try {
                            if (i == genres.length()-1)
                                db.insertMovie(id, title, imageUrl, year, String.valueOf(rating), overview, genre.substring(0, genre.length()-2));
                        } catch (Exception e) {
                            db.insertMovie(id, title, imageUrl, year, String.valueOf(rating), overview, getString(R.string.no_genre));
                        }
                    }

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Movies.BROADCAST_ACTION);
                    broadcastIntent.putExtra(Movies.BROADCAST_DATA, id);
                    sendBroadcast(broadcastIntent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.i(TAG, "onError: " + anError.getResponse());
            }
        });
    }

}
