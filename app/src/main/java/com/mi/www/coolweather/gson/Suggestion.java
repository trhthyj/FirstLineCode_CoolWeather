package com.mi.www.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wm on 2017/12/5.
 */

public class Suggestion {
    /*
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
    */
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
