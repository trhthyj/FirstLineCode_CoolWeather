package com.mi.www.coolweather.ui;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.www.coolweather.R;
import com.mi.www.coolweather.db.City;
import com.mi.www.coolweather.db.Country;
import com.mi.www.coolweather.db.Province;
import com.mi.www.coolweather.util.HttpUtil;
import com.mi.www.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseAreaFragment extends Fragment{
    private static final int LEVEL_PROVINCE=0;
    private static final int LEVEL_CITY=1;
    private static final int LEVEL_COUNTRY=2;
    private static final String TYPE_PROVINCE = "province";
    private static final String TYPE_CITY = "city";
    private static final String TYPE_COUNTRY = "country";
    private ArrayList<String> mDataList = new ArrayList<>();
    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<Country> mCountryList;
    private ArrayAdapter<String> mAdapter;
    private TextView tvTitle;
    private Button btnBack;
    private ListView lvArea;
    private ProgressDialog mProgressDialog;
    private int mCurrentLevel;
    private Province mSelectedProvince;
    private City mSelectedCity;

    public ChooseAreaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        btnBack = view.findViewById(R.id.btn_back);
        lvArea = view.findViewById(R.id.lv_area);
        mAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,mDataList);
        lvArea.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mCurrentLevel == LEVEL_PROVINCE){
                    mSelectedProvince = mProvinceList.get(i);
                    queryCities();
                }else if(mCurrentLevel == LEVEL_CITY){
                    mSelectedCity = mCityList.get(i);
                    querCountries();
                }else if(mCurrentLevel == LEVEL_COUNTRY){
                    String weatherId = mCountryList.get(i).getWeatherId();
                    WeatherActivity.actionStart(getContext(),weatherId);
                    getActivity().finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentLevel == LEVEL_COUNTRY){
                    queryCities();
                }else if(mCurrentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });

        queryProvinces();
    }

    /**
     * 查询省数据（先从数据库取，没有则从服务器取）
     */
    private void queryProvinces() {
        tvTitle.setText(R.string.china);
        btnBack.setVisibility(View.GONE);
        mProvinceList = DataSupport.findAll(Province.class);
        if(mProvinceList.size() > 0){
            mDataList.clear();
            for(Province province : mProvinceList){
                mDataList.add(province.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            lvArea.setSelection(0);
            mCurrentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,TYPE_PROVINCE);
        }
    }

    /**
     * 查询市数据
     */
    private void queryCities() {
        tvTitle.setText(mSelectedProvince.getProvinceName());
        btnBack.setVisibility(View.VISIBLE);
        mCityList = DataSupport.where("provinceId = ?",
                String.valueOf(mSelectedProvince.getId())).find(City.class);
        if(mCityList.size() > 0){
            mDataList.clear();
            for(City city : mCityList){
                mDataList.add(city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            lvArea.setSelection(0);
            mCurrentLevel = LEVEL_CITY;
        }else{

            int provinceCode = mSelectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,TYPE_CITY);
        }
    }

    /**
     * 查询县数据
     */
    private void querCountries() {
        tvTitle.setText(mSelectedCity.getCityName());
        btnBack.setVisibility(View.VISIBLE);
        mCountryList = DataSupport.where("cityId = ?",String.valueOf(mSelectedCity.getId())).find(Country.class);
        if(mCountryList.size() >0){
            mDataList.clear();
            for(Country country : mCountryList){
                mDataList.add(country.getCountryName());
            }
            mAdapter.notifyDataSetChanged();
            lvArea.setSelection(0);
            mCurrentLevel = LEVEL_COUNTRY;
        }else{
            int provinceCode = mSelectedProvince.getProvinceCode();
            int cityCode = mSelectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,TYPE_COUNTRY);
        }
    }

    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if(type.equals(TYPE_PROVINCE)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if(type.equals(TYPE_CITY)){
                    result = Utility.handleCityResponse(responseText,mSelectedProvince.getId());
                }else if(type.equals(TYPE_COUNTRY)){
                    result = Utility.handleCountryResponse(responseText,mSelectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           closeProgressDialog();
                            if(type.equals(TYPE_PROVINCE)){
                                queryProvinces();
                            }else if(type.equals(TYPE_CITY)){
                                queryCities();
                            }else if(type.equals(TYPE_COUNTRY)){
                                querCountries();
                            }
                        }
                    });
                }

            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), R.string.download_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.downloading));
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgressDialog(){
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }



}
