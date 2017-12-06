package com.mi.www.coolweather.gson;

/**
 * Created by wm on 2017/12/5.
 */

public class AQI {
    /*

    "aqi": {
        "city": {
            "aqi": "27",
            "pm25": "17",
        }
    }

    */



    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
