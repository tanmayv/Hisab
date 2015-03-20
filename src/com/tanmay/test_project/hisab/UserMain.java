package com.tanmay.test_project.hisab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tanmay.test_project.network.Api;
import com.tanmay.test_project.network.ApiService;
import com.tanmay.test_project.network.Transaction;
import com.tanmay.test_project.network.TransactionResponse;



public class UserMain extends Activity {


	List<Map<String,String>> collectListData;
	List<Map<String,String>> giveListData;
	
	ListView listCollect;
	ListView listGive;
	TextView lblCollect;
	TextView lvlGive;
	
	ListAdapter collectListAdapter;
	ListAdapter giveListAdapter;
	
	Map<Integer,Holder> database ;
	private int userid;
	private String username;
	
	
	
	
	Api api;
	public void onCreate(Bundle sis){
		super.onCreate(sis);
		setContentView(R.layout.user_main);
		listCollect = (ListView) findViewById(R.id.listCollect);
		lblCollect = (TextView) findViewById(R.id.lblCollect);
		
		
		listGive = (ListView) findViewById(R.id.listGive);
		giveListAdapter = new SimpleAdapter(getApplicationContext(),giveListData,R.layout.user_amount_list_item,
		new String[]{"name","amount"}, new int[]{R.id.user_amount_list_name,R.id.user_amount_list_amount});
		
		Intent i = getIntent();
		userid = i.getIntExtra("id", -1);
		Log.d("userid", " " + userid );
		
		updateDatabase();
	}
	
	Map<Integer, Holder> updateDatabase(){
		System.out.print("Helo");
		database = new HashMap<Integer,Holder>();
		getApi().getTransaction(userid,new Callback<TransactionResponse>() {
			
			@Override
			public void success(TransactionResponse response, Response arg1) {
				Log.d("success", response.success + " " );
				
				if(response.success == 1){
					
					//int totalAmountCollect = 0;
					Log.d("size",response.transaction.length + " "  );
					Transaction[] temp = response.transaction;
					int user = 0;
					
					for(Transaction t:temp){
						
						Holder h = new Holder();
						if(userid == t.user1id){
							
							user = t.user2id;
							h.id = user;
							h.name = t.receiver;
							h.amount = t.amount;
							
							if(database.containsKey(user)){
								
								int amount = database.get(user).amount + t.amount;
								h.amount = amount;
								database.remove(user);
								Log.d("insert collect","user "+h.name + " amount" + amount);
								database.put(user, h);
							}
							else{
								database.put(user, h);
								Log.d("insert collece new ","user "+ h.name + " amount" + h.amount);
							}
						}
						else{
							user = t.user1id;
							h.id = user;
							h.name = t.sender;
							h.amount = t.amount;
							if(database.containsKey(user)){
								int amount =database.get(user).amount - t.amount;
								h.amount = amount;
								database.remove(user);
								database.put(user, h);
								Log.d("insert give","user "+h.name + " amount" + h.amount);
							}else{
								database.put(user, h);
								Log.d("insert give new","user "+h.name + " amount" + t.amount);
							}
							
						}
						
						/*
							Log.d("usersid", " " + t.user2id);
							if(t.user1id == userid){
								Map<String, String> map = new HashMap<String, String>();
								map.put("name","nameid: "  + t.user2id);
								map.put("amount","amount: "+t.amount);
								totalAmountCollect += t.amount;
								
								collectListData.add(map);
							}*/
					}
					
					updateCollectList(database);
					
				}
				
				
			}
			
			
			
			@Override
			public void failure(RetrofitError arg0) {
				// TODO Auto-generated method stub
				Log.d("Failed",arg0.getMessage());
				
			}
		});
		Log.d("database size sd", " "+ database.size());
		return database;
	}
	
	
	
	public void updateCollectList(Map<Integer,Holder> database){
		
		/*Set<Map.Entry<Integer, Integer>> keySet = database.entrySet();
		Iterator<Map.Entry<Integer, Integer>> i = keySet.iterator();
		Log.d("database size", " "+ database.size());
		collectListData = new ArrayList<Map<String,String>>();
		while(i.hasNext()){
			Map.Entry<Integer,Integer> me = (Map.Entry<Integer,Integer>) i.next();
				if( Integer.parseInt(me.getKey().toString())> 0){
				Map<String,String> map = new HashMap<String, String>();
				map.put("amount",me.getValue().toString());
				map.put("name",me.getValue().toString());
				collectListData.add(map);
			}*/
		
			Collection<Holder> userSets = database.values();
			collectListData = new ArrayList<Map<String,String>>();
			
			for(Holder user:userSets){
				if(user.amount > 0){
					Map<String,String> temp = new HashMap<String, String>();
					Log.d("naem", user.name);
					temp.put("name", user.name);
					temp.put("amount", user.amount + "/-");
					
					collectListData.add(temp);
					
				}
			}
		
		
		collectListAdapter = new SimpleAdapter(getApplicationContext(),collectListData,R.layout.user_amount_list_item,
				new String[]{"name","amount"}, new int[]{R.id.user_amount_list_name,R.id.user_amount_list_amount});
				
				listCollect.setAdapter(collectListAdapter);
			
	}
	
	Api getApi(){
		if(api == null)
		api = ApiService.getService();
		return api;
	}
	
	
}


class Holder{
	public int id;
	public int amount;
	public String name;
}