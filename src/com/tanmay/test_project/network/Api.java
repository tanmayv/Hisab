package com.tanmay.test_project.network;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Api {
	@GET("/webservice/login.php")
	void login(@Query("username") String username, @Query("password") String password, Callback<LoginResponse> cb);
	
	@GET("/webservice/create_user.php")
	void signUp(@Query("username") String username, @Query("password") String password, Callback<SignupResponse> db);
	
	@GET("/webservice/get_transaction.php")
	void getTransaction(@Query("userid") int userid, Callback<TransactionResponse> cb);
	
	@GET("/webservice/get_transaction.php")
	void createTransaction(@Query("user1id") int senderid, @Query("user2id") int receiverid, @Query("amount") int amount, Callback<ServerResponse> sb);
}
