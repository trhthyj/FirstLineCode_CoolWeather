package com.mi.www.coolweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mi.www.coolweather.gson.Weather;
import com.mi.www.coolweather.ui.WeatherActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*先判断本地是否有天气信息，有说明用户已经选择过城市，直接跳WeatherActivity*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("weather",null) != null){
            WeatherActivity.actionStart(this,"");
            finish();
        }
    }
}
