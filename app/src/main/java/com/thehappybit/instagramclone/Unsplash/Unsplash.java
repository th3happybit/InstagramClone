package com.thehappybit.instagramclone.Unsplash;

import android.util.Log;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Unsplash {

    private String clientId;
    private PhotosEndpointInterface photosEndpointInterface;
    public Unsplash(String clientId){
        this.clientId = clientId;
    }

    public void getPhotos(){

        HeaderInterceptor interceptor = new HeaderInterceptor(clientId);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.unsplash.com/").client(client).addConverterFactory(GsonConverterFactory.create()).build();

        photosEndpointInterface = retrofit.create(PhotosEndpointInterface.class);

        Call<List<Photo>> call = photosEndpointInterface.getPhotos(1,10,"popular");

        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                Log.d("unsplash", response.toString());
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.d("unsplash", "failed");
            }
        });
    }

}
