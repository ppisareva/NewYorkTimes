package polina.example.com.newyorktimes.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import polina.example.com.newyorktimes.R;
import polina.example.com.newyorktimes.databinding.ActivityWebViewerBinding;
import polina.example.com.newyorktimes.networks.MyBrowser;
import polina.example.com.newyorktimes.util.Utils;

import static polina.example.com.newyorktimes.R.id.miShare;

public class WebViewerActivity extends AppCompatActivity {

    private WebView myWebView;
    private ShareActionProvider mShareActionProvider;
    private Intent shareIntent;
    private String url;
    private ActivityWebViewerBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_viewer);
        url = getIntent().getStringExtra(Utils.URL);
        System.err.println(url);
        Toolbar toolbar = binding.toolbarWeb;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myWebView = binding.wvArticle;
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myWebView.setWebViewClient(new MyBrowser());
        myWebView.loadUrl(url);
        init(url);
    }

    private void init(String url){
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        MenuItem item =  menu.findItem(miShare);
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        init(url);
        mShareActionProvider.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



}}
