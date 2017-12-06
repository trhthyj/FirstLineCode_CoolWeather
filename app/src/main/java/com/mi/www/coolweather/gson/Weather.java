package com.mi.www.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wm on 2017/12/5.
 */
public class Weather {
/*
http://guolin.tech/api/weather?cityid=CN101050401&key=c62affbd245f4d528681195b38cf58dd
    {
        "aqi": {
        "city": {
            "aqi": "27",
            "pm25": "17",
            }
        },
        "basic": {
        "city": "佳木斯",
                "id": "CN101050401",
                "update": {
                    "loc": "2017-12-06 15:50",
                    "utc": "2017-12-06 07:50"
                }
        },
        "daily_forecast": [
        {
            "cond": {
                    "txt_d": "多云",
            },
            "date": "2017-12-06",
            "tmp": {
                    "max": "-15",
                    "min": "-26"
            }
        },
        {
            "cond": {
                    "txt_d": "多云",
            },
            "date": "2017-12-07",
            "tmp": {
                    "max": "-15",
                    "min": "-26"
            },
        }],
        "now": {
            "cond": {
                  "code": "101",
                  "txt": "多云"
            }
             "tmp": "-18"
    },
    "status": "ok",
    "suggestion": {
        "comf": {
            "txt": "白天天气虽然晴好，但气温低，您会感觉十分寒冷，极不舒适，请注意保暖，并避免出门，以防冻伤。"
        },
        "cw": {
            "txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
        },

        "sport": {
            "txt": "天气较好，但考虑天气寒冷，推荐您进行室内运动，户外运动时请注意保暖并做好准备活动。"
        }
    }
    }
    */
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
