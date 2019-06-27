package com.embibeassignment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ListIterator;

import static com.embibeassignment.Movies.TAG;
import static com.embibeassignment.Movies.movie_list;

public class WebViewFragment extends Fragment {

    public WebViewFragment() {}

    private WebView webView;
    private MaterialToolbar toolbar;
    private int index;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview, container, false);

        toolbar = view.findViewById(R.id.toolbar_webview);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("IMDB");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        index = this.getArguments().getInt("index");
        String URL = "https://www.imdb.com/title/".concat(this.getArguments().getString("id"));
        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(URL);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.browser, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.action_back:
                if (index-1 < 0){
                    Toast.makeText(getContext(), "This is the First Movie", Toast.LENGTH_SHORT).show();
                } else {
                 webView.loadUrl(movie_list.get(index - 1).imageUrl);
                }

                return true;
            case R.id.action_forward:
                if (index + 1 >= movie_list.size()) {
                    Toast.makeText(getContext(), "This is the Last One", Toast.LENGTH_SHORT).show();
                } else {
                    webView.loadUrl(movie_list.get(index + 1).imageUrl);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
