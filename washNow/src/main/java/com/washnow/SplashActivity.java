package com.washnow;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.washnow.adapter.ImagePagerAdapter;
import com.washnow.utils.Utils;
import com.washnow.vo.OrderVo;

public class SplashActivity extends Activity implements OnClickListener {

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		context=this;
		
 	 initSplashLayout();
		//initLayout();
	}
	
	public void initLayout(){
		
		ImageView imLogo = (ImageView ) findViewById(R.id.imLogo);
		Button btFinish = (Button ) findViewById(R.id.btFinish);
		imLogo.setVisibility(View.GONE);
		btFinish.setVisibility(View.GONE);
		btFinish.setOnClickListener(this);
		final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
		final ImageView imArrowR = (ImageView) findViewById(R.id.imArrowRight);
		final ImageView imArrowL = (ImageView) findViewById(R.id.imArrowLeft);
		pager.setVisibility(View.VISIBLE);
		imArrowR.setVisibility(View.VISIBLE);
		ImagePagerAdapter aDapter = new ImagePagerAdapter(context);
		pager.setAdapter(aDapter);
		pager.setOnPageChangeListener( new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int page) {
				// TODO Auto-generated method stub
				Button btFinish = (Button ) findViewById(R.id.btFinish);
				if(page==2||page==3){
					btFinish.setVisibility(View.VISIBLE);
				}else {
					btFinish.setVisibility(View.GONE);
				}
				if(page==0){
					imArrowL.setVisibility(View.GONE);
					imArrowR.setVisibility(View.VISIBLE);
					
				}else if(page==2){
					imArrowL.setVisibility(View.VISIBLE);
					imArrowR.setVisibility(View.GONE);
				
				}else{

					imArrowL.setVisibility(View.VISIBLE);
					imArrowR.setVisibility(View.VISIBLE);
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		imArrowL.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int count = pager.getCurrentItem()-1;
					pager.setCurrentItem(count);
				}
			});
		imArrowR.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int count = pager.getCurrentItem()+1;
				pager.setCurrentItem(count);
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btFinish:
			login();
			break;

		default:
			break;
		}
		
	}
	
		
	public void login(){
	 
		 Intent intent = new Intent(context, LoginActivity.class);
		
		startActivity(intent);
		finish();
	}
	
	public void initSplashLayout(){
		 new Thread(){
			 public void run(){
				 try {
					sleep(2500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 String name=Utils.getSharedPreference(context, "verified");
				 	if(name!=null&&name.equalsIgnoreCase("true")) {
					
					
				 OrderVo order = Utils.getOrder(context);
					Intent intent;
				/*	if(order==null){
						 intent = new Intent(context, RequestPickupActivity.class);
					}else {
						 intent = new Intent(context, OrderStatusActivity.class);
					}*/
						intent = new Intent(context, RequestPickupActivity.class);
					 startActivity(intent);
					 finish();
				 	}else {
				 		runOnUiThread( new  Runnable() {
							public void run() {
								initLayout();
							}
						});
							
					
				 	}
			 }
		 }.start();
	}
}
