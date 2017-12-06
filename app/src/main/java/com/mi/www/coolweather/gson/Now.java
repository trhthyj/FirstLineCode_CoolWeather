package com.mi.www.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wm on 2017/12/5.
 */

public class Now {

    /*
    "now": {
        "cond": {
            "txt": "多云"
        }
        "tmp": "-18"
    }
    */
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
