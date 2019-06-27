package com.embibeassignment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.embibeassignment.Movies.CHANNEL_ID;
import static com.embibeassignment.Movies.TAG;

public class MainActivity extends AppCompatActivity {

    private String API_KEY = BuildConfig.API_KEY;
    public static JSONArray results;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        dbHelper = new DBHelper(this);

        new FetchMovies().execute();

        _ShowSplashScreen();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.clearDB();
    }

    public void _ShowSplashScreen() {
        Fragment splash_fragment = new SplashScreen();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame, splash_fragment);
        ft.commit();
    }

    public class FetchMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.i(TAG, "onError: " + anError);
                        }
                    });
            return null;
        }
    }

    private void setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
