package com.tanmay.test_project.hisab;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tanmay.test_project.network.Api;
import com.tanmay.test_project.network.ApiService;
import com.tanmay.test_project.network.LoginResponse;
import com.tanmay.test_project.network.SignupResponse;

public class Login extends Activity {

	Api api;
	
	public Login() {
		
	}
	
	TextView txtUsername;
	TextView txtPassword;
	Button btnLogin;
	Button btnSignup;
	
	public void onCreate(Bundle sis){
		super.onCreate(sis);
		setContentView(R.layout.login);
		
		txtUsername = (TextView) findViewById(R.id.txtUsername);
		txtPassword = (TextView) findViewById(R.id.txtPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignup = (Button) findViewById(R.id.btnSignup);
		
	
	}
	
	public void onLoginClicked(View v){
		Log.d("Button", "Login Clicked");
		checkLogin();
	}
	
	public void onSignupClicked(View v){
		signup();
	}
	
	Api getApi(){
		if(api == null)
			api = ApiService.getService();
		
		return api;
	}
	
	

	void checkLogin(){
		
		String username = txtUsername.getText().toString();
		String password = txtPassword.getText().toString();
		getApi().login(username, password, new Callback<LoginResponse>() {
			
			@Override
			public void success(LoginResponse arg0, Response arg1) {
				Toast.makeText(getApplicationContext(),arg0.message + "  " + arg0.id, Toast.LENGTH_LONG).show();
				Log.d("success", arg0.message);
				Intent i = new Intent(getApplicationContext(), UserMain.class);
				i.putExtra("id", Integer.parseInt(arg0.id));
				startActivity(i);
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				Log.d("failure login", arg0.toString());
			}
		});
	}
	
	void signup(){
		String username = txtUsername.getText().toString();
		String password = txtPassword.getText().toString();
		
		txtUsername.setEnabled(false);
		txtPassword.setEnabled(false);
		
		getApi().signUp(username, password, new Callback<SignupResponse>() {

			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void success(SignupResponse response, Response arg1) {
				if(response.success == 1){
					checkLogin();
					
				}else
					Toast.makeText(getApplicationContext(),response.success + "  " , Toast.LENGTH_LONG).show();
			}
		});
	}
}
