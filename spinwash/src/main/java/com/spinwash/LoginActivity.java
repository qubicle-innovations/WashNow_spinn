package com.spinwash;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.spinwash.async.LoginTask;
import com.spinwash.parser.Parser;
import com.spinwash.utils.Utils;
import com.spinwash.utils.ZTypeface;
import com.spinwash.vo.OrderVo;
import com.spinwash.vo.SignUpPojo;
import com.spinwash.vo.TResponse;
import com.spinwash.vo.UserVO;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class LoginActivity extends Activity implements OnClickListener {

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		context=this;
		String name=Utils.getSharedPreference(context, "name");
		String verified=Utils.getSharedPreference(context, "verified");
		if(verified!=null&&verified.equalsIgnoreCase("true")&&name!=null&&name.length()>0) {
			String email=Utils.getSharedPreference(context, "email");

			try {
				double id = Double.parseDouble(Utils.getSharedPreference(context, "userid"));
				if(id>0){
					initSplashLayout();
				}else {
					login(name, email);
				}

			} catch (Exception e) {
				// TODO: handle exception
				login(name, email);
			}
			 
		}else{
			initLayout();
		}


	}
	
	public void initLayout(){

		TextView tv = (TextView) findViewById(R.id.tvTerms);
		EditText etName = (EditText) findViewById(R.id.etName);
		EditText etEmail = (EditText) findViewById(R.id.etEmail);
		Button btGo = (Button) findViewById(R.id.btGo);
		tv.setText(Html.fromHtml("<u>"+(Utils.getColoredSpanned("By Clicking Start You Agree To WASHNOW<br> ","#ffffff")+Utils.getColoredSpanned("Terms of Use","#3498db")+Utils.getColoredSpanned(" And ","#ffffff")+Utils.getColoredSpanned("Privacy Policy","#3498db")+"</u>")));
		ImageView imLogo = (ImageView ) findViewById(R.id.imLogo);
		imLogo.setVisibility(View.GONE);
		etName.setVisibility(View.VISIBLE);
		etEmail.setVisibility(View.VISIBLE);
		tv.setVisibility(View.VISIBLE);
		btGo.setVisibility(View.VISIBLE);
		etName.setTypeface(ZTypeface.UbuntuR(this));
		etEmail.setTypeface(ZTypeface.UbuntuR(this));
		tv.setOnClickListener(this);
		btGo.setTypeface(ZTypeface.UbuntuR(this));
		btGo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btGo:
			EditText etName = (EditText) findViewById(R.id.etName);
			EditText etEmail = (EditText) findViewById(R.id.etEmail);
			
			String name =Utils.getText(etName);
			String email =Utils.getText(etEmail);
			
			login(name, email);
			break;
			case R.id.tvTerms:
				String url = "https://www.washnow.me/privacy";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				break;
		default:
			break;
		}
		
	}
	
		
	public void login(String name, String email){
		if(name==null||name.length()<3){
			Toast.makeText(context, "Please enter a valid name", Toast.LENGTH_SHORT).show();
			return;
		}
		if(email==null||email.length()<2){
			Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show();
			return;
		}
		//Utils.setSharedPreference(context, "name", name);
		//Utils.setSharedPreference(context, "email", email);
	/*	 Intent intent = new Intent(context, RequestPickupActivity.class);
		
		startActivity(intent);
		finish();*/
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add( new BasicNameValuePair("process","signup") ); 
		params.add( new BasicNameValuePair("name",name) );
		params.add( new BasicNameValuePair("email",email) );
		params.add( new BasicNameValuePair("verified","true") );

		new LoginTask(context, params).execute("");
		
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
				 OrderVo order = Utils.getOrder(context);
					Intent intent;
					if(order==null){
						 intent = new Intent(context, RequestPickupActivity.class);
					}else {
						 intent = new Intent(context, OrderStatusActivity.class);
					}
					 startActivity(intent);
					 finish();
			
			 }
		 }.start();
	}

	public void Login(TResponse<String> result) {
		// TODO Auto-generated method stub
		
		if(result==null){
			Toast.makeText(context, "unable to signup \n please check network connection", Toast.LENGTH_SHORT).show();
		}else if(result.isHasError()){
			Toast.makeText(context, "unable to signup \n please try later", Toast.LENGTH_SHORT).show();
			
		}else  if(result.getResponseContent()!=null) {
			
			try {
				SignUpPojo pojo = Parser.getUser(result.getResponseContent());
				if(pojo==null){
					Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
				}else if(pojo.getUser()==null){
					Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
				}else {
					UserVO vo = pojo.getUser();
					if(pojo.getOrder()!=null){
						OrderVo order = pojo.getOrder();
						Utils.setOrder(context, order);
					}

					/*UserVO vo = pojo.getUser();
					Utils.setSharedPreference(context, "name", vo.getName());
					Utils.setSharedPreference(context, "email", vo.getEmail());
					Utils.setSharedPreference(context, "userid", vo.getUser_id());
					if(pojo.getOrder()!=null){
						OrderVo order = pojo.getOrder();
						  Utils.setOrder(context, order);
						  Intent intent = new Intent(context, OrderStatusActivity.class);
						  intent.putExtra("order", order);
						  startActivity(intent);
						  finish();
					}else {
						 Intent intent = new Intent(context, RequestPickupActivity.class);

							startActivity(intent);
							finish();
					}
					Toast.makeText(context, "Succesfully Registered", Toast.LENGTH_SHORT).show();*/

					Intent intent = new Intent(context, PhoneAuthActivity.class);
					intent.putExtra("pojo", pojo);
					startActivity(intent);
					initLayout();

					FirebaseAnalytics mFirebaseAnalytics=	((WashNowApplication)getApplication()).getDefaultTracker();
					Bundle bundle = new Bundle();
					bundle.putString(FirebaseAnalytics.Param.ITEM_ID, pojo.getUser().getUser_id());
					bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, pojo.getUser().getName());
					mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
