package com.embibeassignment.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.embibeassignment.R;
import com.google.android.material.appbar.MaterialToolbar;

import static com.embibeassignment.ui.Movies.movie_list;

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

        _SetupToolbar(view);

        index = this.getArguments().getInt("index");
        String URL = "https://www.imdb.com/title/".concat(this.getArguments().getString("id"));
        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                try {
                    toolbar.setTitle(getString(R.string.loading));
                } catch (Exception e){}
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    toolbar.setTitle(getResources().getString(R.string.imdb));
                } catch (Exception e){}
            }
        });
        webView.loadUrl(URL);

        return view;
    }

    private void _SetupToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar_webview);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        toolbar.setTitle(getResources().getString(R.string.loading));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
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
                if (index-1 < 0) {
                    Toast.makeText(getContext(), R.string.back_error, Toast.LENGTH_SHORT).show();
                } else {
                 webView.loadUrl("https://www.imdb.com/title/"+movie_list.get(index - 1).id);
                 index-=1;
                }

                return true;
            case R.id.action_forward:
                if (index + 1 >= movie_list.size()) {
                    Toast.makeText(getContext(), R.string.fwd_error, Toast.LENGTH_SHORT).show();
                } else {
                    webView.loadUrl("https://www.imdb.com/title/"+movie_list.get(index + 1).id);
                    index+=1;
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
