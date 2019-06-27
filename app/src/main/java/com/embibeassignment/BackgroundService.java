package com.embibeassignment;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.embibeassignment.Movies.TAG;
import static com.embibeassignment.MainActivity.results;

public class BackgroundService extends Service {

    private String API_KEY = BuildConfig.API_KEY;
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
        timer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        task = new TimerTask() {
            int index = 0;
            @Override
            public void run() {

                Log.i(TAG, "run: " + "Thread executed");

                if (index < results.length()) {
                    try {
                        JSONObject response = (JSONObject) results.get(index);

                        GetImdbId(response.getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    index++;
                } else {
                    Log.i(TAG, "run: " + "ALL MOVIEs ADDED");
                    timer.cancel();
                }
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 5000);

        return START_STICKY;
    }


    public void GetImdbId(String tmdb_id) {
        AndroidNetworking.get("https://api.themoviedb.org/3/movie/{id}?api_key={api_key}")
                .addPathParameter("id", tmdb_id)
                .addPathParameter("api_key", API_KEY)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String title = response.getString("title");
                    String imageUrl = "https://image.tmdb.org/t/p/w400".concat(response.getString("poster_path"));
                    String year = response.getString("release_date");
                    String id = response.getString("imdb_id");
                    float rating = Float.parseFloat(String.valueOf(response.getDouble("vote_average")))/2;

                    db.insertMovie(id, title, imageUrl, year, String.valueOf(rating));

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
