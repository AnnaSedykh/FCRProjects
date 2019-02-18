package com.fcrcompany.fcrprojects.data.api;

import com.fcrcompany.fcrprojects.data.api.model.DriveResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface Api {

    @GET("files")
    Observable<DriveResponse> files(@Header("Authorization") String token,
                                    @Query("orderBy") String orderBy,
                                    @Query("fields") String fields,
                                    @Query("q") String query);

    @Streaming
    @GET("files/{fileId}")
    Observable<ResponseBody> downloadFile(@Header("Authorization") String token,
                                             @Path("fileId") String fileId,
                                             @Query("alt") String param);
}
