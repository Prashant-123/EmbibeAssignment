package com.embibeassignment.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.embibeassignment.BuildConfig;
import com.embibeassignment.R;
import com.embibeassignment.utils.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.embibeassignment.ui.Movies.TAG;

public class MainActivity extends AppCompatActivity {

    public static JSONArray results;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        this.deleteDatabase(DBHelper.DATABASE_NAME);

        new FetchMovies().execute();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void _ShowSplashScreen() {
        Fragment splash_fragment = new SplashScreen();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame, splash_fragment);
        ft.commit();
    }

    class FetchMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            _ShowSplashScreen();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String API_KEY = BuildConfig.API_KEY;
            AndroidNetworking.get("https://api.themoviedb.org/3/movie/{type}?api_key={api_key}")
                    .addPathParameter("api_key", API_KEY)
                    .addPathParameter("type", "top_rated")
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
}
