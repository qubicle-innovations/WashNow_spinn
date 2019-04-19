package com.washnow;

import com.washnow.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
 
public class WebViewActivity extends BaseBackActivity {
 
	private WebView webView;
 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
 
		setTitle("PRICING");
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://www.washnow.me/#!pricing/clzz");
 
		findViewById(R.id.im_bck).setVisibility(View.VISIBLE);
		 findViewById(R.id.ll_logo).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                 onBackPressed();
	            }
	        });
	}


}