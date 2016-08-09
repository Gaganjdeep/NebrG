package gagan.com.communities.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import gagan.com.communities.R;

public class ShowTextActivity extends AppCompatActivity
{

     WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        settingActionBar(getIntent().getStringExtra("title"));

        String url=getIntent().getStringExtra("text");

        webView=  (WebView) findViewById(R.id.webview);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }


    private void settingActionBar(String title)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_img);


        TextView tvTitle = (TextView) toolbar.findViewById(R.id.txtvTitle);
        tvTitle.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return super.onOptionsItemSelected(item);
    }


}
