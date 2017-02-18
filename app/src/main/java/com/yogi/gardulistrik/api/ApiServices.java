package com.yogi.gardulistrik.api;

import com.yogi.gardulistrik.api.mdl.AdminMdl;
import com.yogi.gardulistrik.api.mdl.BaseMdl;
import com.yogi.gardulistrik.api.mdl.LoginMdl;
import com.yogi.gardulistrik.api.mdl.TrafoMdl;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yogi on 19/12/16.
 */
public interface ApiServices {


    //GET LIST MAP
    @GET("get_data.php")
    Observable<BaseMdl<List<TrafoMdl>>> getData(@Query("status") int status);
    @GET("get_data.php")
    Observable<BaseMdl<List<TrafoMdl>>> getMapsTerdekat(@Query("status") int status, @Query("lat") double latitude, @Query("lng") double longitude, @Query("jarak") String jarak);
    //Cari Penyulang
    @GET("get_data.php")
    Observable<BaseMdl<List<TrafoMdl>>> getCariPenyulang(@Query("status") int status,
                                                @Query("nama_penyulang") String namaPenyulang);

    //Cari Trafo
    @GET("get_data.php")
    Observable<BaseMdl<List<TrafoMdl>>> getCariTrafo(@Query("status") int status,
                                                         @Query("nama_gardu") String namaGardu);

    @GET("crud.php")
    Observable<BaseMdl<LoginMdl>> getLogin(@Query("status") int status,
                                           @Query("username") String username,
                                           @Query("password") String password);
    @FormUrlEncoded
    @POST("crud.php")
    Observable<String> postData(@Query("status") int status,
                                  @Field("alamat") String alamat,
                                  @Field("daya") String daya,
                                  @Field("latitude") String latitude,
                                  @Field("longitude") String longitude,
                                  @Field("singk_gardu") String singk_gardu,
                                  @Field("jenis_gardu") String jenis_gardu,
                                  @Field("nama_gardu") String nama,
                                  @Field("id_penyulang") String id,
                                  @Field("feeder") String feeder

                                );

    @FormUrlEncoded
    @POST("crud.php")
    Observable<String> postUpdateData(@Query("status") int status,
                                @Field("alamat") String alamat,
                                @Field("daya") String daya,
                                @Field("latitude") String latitude,
                                @Field("longitude") String longitude,
                                @Field("singk_gardu") String singk_gardu,
                                @Field("jenis_gardu") String jenis_gardu,
                                @Field("nama_gardu") String nama,
                                @Field("id_penyulang") String id,
                                @Field("feeder") String feeder,
                                      @Field("id_gardu") String id_gardu

    );
    @FormUrlEncoded
    @POST("crud.php")
    Observable<String> getHapus(@Query("status") int status,
                                           @Field("id_gardu") String id_gardu);

}