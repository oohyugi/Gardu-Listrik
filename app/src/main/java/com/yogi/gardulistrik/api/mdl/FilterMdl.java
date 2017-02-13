package com.yogi.gardulistrik.api.mdl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogi on 13/02/17.
 */
public class FilterMdl {
    public String nama;

    public FilterMdl(String nama) {
        this.nama = nama;
    }

    public static List<FilterMdl>  getFilterItem(){
        List<FilterMdl> mdl = new ArrayList<>();
        mdl.add(new FilterMdl("Feeder"));
        mdl.add(new FilterMdl("Jarak"));

        return mdl;
    }
}