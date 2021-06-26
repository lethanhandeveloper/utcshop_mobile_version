package com.vku.lethanhan.utcshop.api;

import com.google.gson.JsonElement;
import com.vku.lethanhan.utcshop.model.Account;
import com.vku.lethanhan.utcshop.model.ApiResponse;
import com.vku.lethanhan.utcshop.model.Cart;
import com.vku.lethanhan.utcshop.model.Category;
import com.vku.lethanhan.utcshop.model.Comment;
import com.vku.lethanhan.utcshop.model.Payment;
import com.vku.lethanhan.utcshop.model.Product;
import com.vku.lethanhan.utcshop.util.Server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder().baseUrl(Server.base_url)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build()
                                .create(ApiService.class);
    //slides
    @GET("slide")
    Call<ArrayList<String>> getSlide();

    @GET("product/newproduct")
    Call<List<Product>> getNewProduct();
    @GET("product/hotproduct")
    Call<List<Product>> gethotProduct();
    @GET("category")
    Call<List<Category>> getCategory();

    @GET("product/{id}")
    Call<ArrayList<Product>> getProductbyCategory(@Path("id") int category_id);
    @FormUrlEncoded
    @POST("login")
    Call<ApiResponse> login(@Field("username") String username, @Field("password") String password);
    @POST("register")
    Call<JsonElement> register(@Body Account account);
    @GET("account")
    Call<Account> getAccountData(@Header("Authorization") String token);
    @GET("logout")
    Call<ApiResponse> logout(@Header("Authorization") String token);
    //carts
    @GET("cart")
    Call<ArrayList<Cart>> getCart(@Header("Authorization") String token);
    @GET("cart/add/{id}/quantity/{quantity}")
    Call<ApiResponse> addCart(@Header("Authorization") String token, @Path("id") int product_id, @Path("quantity") int quantity);
    @GET("cart/delete/{id}")
    Call<ApiResponse> deleteCart(@Header("Authorization") String token, @Path("id") int id);
    @GET("cart/update/{id}/quantity/{quantity}")
    Call<ApiResponse> updateCart(@Header("Authorization") String token, @Path("id") int id, @Path("quantity") int quantity);
    //comments
    @GET("comment/product/{id}")
    Call<ArrayList<Comment>> getComment(@Path("id") int productId);
    @GET("comment/add/product/{id}/content/{content}")
    Call<ApiResponse> addComment(@Header("Authorization") String token, @Path("id") int productId, @Path("content") String content);
    @POST("bill/add")
    Call<ApiResponse> addBill(@Header("Authorization") String token, @Body Payment payment);
    //filter
    @FormUrlEncoded
    @POST("product/filter")
    Call<ArrayList<Product>> filterProduct(@Field("name") String productName,
                                           @Field("price") String productPrice, @Field("category_id") String categoryId);

    //payment
    @GET("bill/get/status/{status}")
    Call<ArrayList<Payment>> getBill(@Header("Authorization") String token, @Path("status") String status);
    @GET("bill/detail/{id}")
    Call<ArrayList<Cart>> getBillDetail(@Header("Authorization") String token, @Path("id") int id);

}
