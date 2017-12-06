package com.mi.www.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wm on 2017/12/5.
 */

public class Forecast {

   /*
   "daily_forecast": [
    {
        "cond": {
                "code_d": "101",
                "code_n": "101",
                "txt_d": "多云",
                "txt_n": "多云"
                },
        "date": "2017-12-06",
        "tmp": {
                "max": "-15",
                "min": "-26"
                }
    },
    {
        "cond": {
                "code_d": "101",
                "code_n": "100",
                "txt_d": "多云",
                "txt_n": "晴"
                },
        "date": "2017-12-07",
        "tmp": {
                "max": "-15",
                "min": "-26"
                },
    }]
            */
    public String date;

    @SerializedName("cond")
    public More more;

    @SerializedName("tmp")
    public Temperature temperature;

    public class More{
        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature {
        public String max;
        public String min;
    }
}
