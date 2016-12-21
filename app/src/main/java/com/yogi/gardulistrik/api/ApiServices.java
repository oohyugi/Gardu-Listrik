package com.yogi.gardulistrik.api;

import com.yogi.gardulistrik.api.mdl.BaseMdl;
import com.yogi.gardulistrik.api.mdl.TrafoMdl;

import java.util.List;

import retrofit2.http.GET;
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


}