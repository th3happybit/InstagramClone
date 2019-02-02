package com.thehappybit.instagramclone.Unsplash;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotosEndpointInterface {

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("page") Integer page, @Query("per_page") Integer perPage,
                                @Query("order_by") String orderBy);

    @GET("photos/random")
    Call<List<Photo>> getRandomPhotos(@Query("collections") String collections,
                                      @Query("featured") boolean featured, @Query("username") String username,
                                      @Query("query") String query, @Query("w") Integer width,
                                      @Query("h") Integer height, @Query("orientation") String orientation,
                                      @Query("count") Integer count);
}
