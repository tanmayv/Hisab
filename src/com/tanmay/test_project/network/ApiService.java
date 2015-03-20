package com.tanmay.test_project.network;

import retrofit.RestAdapter;

public class ApiService {

	private static String url = "http://192.168.1.3:80";
	public static Api getService(){
		
		return new RestAdapter.Builder()
			.setEndpoint(url)
			.build()
			.create(Api.class);
	}
}

