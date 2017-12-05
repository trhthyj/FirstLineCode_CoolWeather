package com.mi.www.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wm on 2017/12/5.
 */

public class Basic {
    //由于有些json字段不适合做java字段名，所以用@SerializedName让它们之间建立关联
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
