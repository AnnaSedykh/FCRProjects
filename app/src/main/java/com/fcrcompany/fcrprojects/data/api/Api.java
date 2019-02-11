package com.fcrcompany.fcrprojects.data.api;

import com.fcrcompany.fcrprojects.data.api.model.DriveResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface Api {

    @GET("files")
    Observable<DriveResponse> files(@Header("Authorization") String token,
                                    @Query("orderBy") String orderBy,
                                    @Query("q") String query);
}
