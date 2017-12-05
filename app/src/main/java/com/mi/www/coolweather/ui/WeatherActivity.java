package com.mi.www.coolweather.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mi.www.coolweather.R;
import com.mi.www.coolweather.gson.Forecast;
import com.mi.www.coolweather.gson.Weather;
import com.mi.www.coolweather.util.HttpUtil;
import com.mi.www.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private static final String EXTRA_WEATHER_ID ="weather_id";
    private static final String WEATHER_KEY ="c62affbd245f4d528681195b38cf58dd";
    private ScrollView scrollViewWeather;
    private TextView tvTitleCity;
    private TextView tvUpdateTime;
    private TextView tvDegree;
    private TextView tvWeatherInfo;
    private LinearLayout llForecast;
    private TextView tvAqiText;
    private TextView tvPM25;
    private TextView tvComfort;
    private TextView tvCarWash;
    private TextView tvSport;
    private ImageView ivWeatherBg;

    public static void actionStart(Context context, String weatherId){
        Intent intent =new Intent(context,WeatherActivity.class);
        intent.putExtra(EXTRA_WEATHER_ID,weatherId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让背景图和状态栏融合到一起
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            //让活动的布局在状态栏下面
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initView();
        initData();
    }

    private void initView() {
        scrollViewWeather = findViewById(R.id.scroll_weather);
        tvTitleCity = findViewById(R.id.tv_title_city);
        tvUpdateTime = findViewById(R.id.tv_update_time);
        tvDegree = findViewById(R.id.tv_degree);
        tvWeatherInfo = findViewById(R.id.tv_weather_info);
        llForecast = findViewById(R.id.ll_forecast);
        tvAqiText = findViewById(R.id.tv_aqi);
        tvPM25 = findViewById(R.id.tv_pm25);
        tvComfort = findViewById(R.id.tv_comfort);
        tvCarWash = findViewById(R.id.tv_car_wash);
        tvSport = findViewById(R.id.tv_sport);
        ivWeatherBg = findViewById(R.id.iv_weather_bg);
    }

    private void initData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherSting = prefs.getString("weather",null);
        String weatherBg = prefs.getString("bing_pic",null);
        if(weatherSting != null){
            Weather weather = Utility.handleWeatherResponse(weatherSting);
            showWeatherInfo(weather);
        }else{
            String weatherId = getIntent().getStringExtra(EXTRA_WEATHER_ID);
            scrollViewWeather.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        if(weatherBg != null){
            Glide.with(this).load(weatherBg).into(ivWeatherBg);
        }else{
            loadBingPic();
        }
    }

    /**
     * 从服务器获取天气信息
     * @param weatherId
     */
    private void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+
                "&key="+WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();//todo 和commit区别
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,
                                    "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //请求背景图
        loadBingPic();
    }

    /**
     * 显示天气信息
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        tvTitleCity.setText(cityName);
        tvUpdateTime.setText(updateTime);
        tvDegree.setText(degree);
        tvWeatherInfo.setText(weatherInfo);
        llForecast.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.item_weather_forecast,
                    llForecast,false);
            TextView tvDate = view.findViewById(R.id.tv_date);
            TextView tvInfo = view.findViewById(R.id.tv_info);
            TextView tvMax = view.findViewById(R.id.tv_max);
            TextView tvMin = view.findViewById(R.id.tv_min);
            tvDate.setText(forecast.date);
            tvInfo.setText(forecast.more.info);
            tvMax.setText(forecast.temperature.max);
            tvMin.setText(forecast.temperature.min);
            llForecast.addView(view);
        }
        if(weather.aqi != null){
            tvAqiText.setText(weather.aqi.city.aqi);
            tvPM25.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        tvComfort.setText(comfort);
        tvCarWash.setText(carWash);
        tvSport.setText(sport);
        scrollViewWeather.setVisibility(View.VISIBLE);
    }

    /**
     * 加载bing背景图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(ivWeatherBg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
}
