package com.yogi.gardulistrik.api.mdl;

import java.io.Serializable;

/**
 * Created by oohyugi on 12/18/17.
 * github: https://github.com/oohyugi
 */

public class CoordinateMdl implements Serializable {
    public double latitude;
    public  double longitude;
    public String address;

    public CoordinateMdl() {

    }

    public CoordinateMdl(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
