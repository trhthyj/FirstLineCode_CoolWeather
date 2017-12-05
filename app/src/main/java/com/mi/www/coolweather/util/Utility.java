package com.mi.www.coolweather.util;

import android.text.TextUtils;

import com.mi.www.coolweather.db.City;
import com.mi.www.coolweather.db.Country;
import com.mi.www.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wm on 2017/12/4.
 */

public class Utility {
    /**
     * 处理省信息
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray provinceArray = new JSONArray(response);
                for(int i=0;i<provinceArray.length();i++){
                    JSONObject object = provinceArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(object.getString("name"));
                    province.setProvinceCode(object.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 处理市信息
     * @param response
     * @return
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray cityArray = new JSONArray(response);
                for(int i=0;i<cityArray.length();i++){
                    JSONObject object = cityArray.getJSONObject(i);
                    City city = new City();
                    city.setCityName(object.getString("name"));
                    city.setCityCode(object.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 处理县信息
     * @param response
     * @return
     */
    public static boolean handleCountryResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray countryArray = new JSONArray(response);
                for(int i=0;i<countryArray.length();i++){
                    JSONObject object = countryArray.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryName(object.getString("name"));
                    country.setCityId(cityId);
                    country.setWeatherId(object.getString("weather_id"));
                    country.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
