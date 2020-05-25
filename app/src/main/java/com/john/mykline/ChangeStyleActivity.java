package com.john.mykline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.john.mykline.bean.MyKlineData;
import com.john.mykline.databinding.ActivityChangeStyleBinding;
import com.johnzh.klinelib.DATA;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeStyleActivity extends AppCompatActivity {

    ActivityChangeStyleBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityChangeStyleBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());

        MyFactory2 factory2 = new MyFactory2(this);
        mBinding.klineView.setDrawAreaList(factory2);
        mBinding.changeCandleStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getTestData();
    }

    private void getTestData() {
        HttpAgent.getApi().getDailyKline("M2009")
                .enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        List<DATA> data = new ArrayList<>();
                        convertData(response.body(), data);
                        mBinding.klineView.setDataList(data);
                    }

                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {
                    }
                });
    }


    private void convertData(List<List<String>> body, List<DATA> kDataList) {
        for (List<String> stringList : body) {
            kDataList.add(getData(stringList));
        }
    }

    private DATA getData(List<String> stringList) {
        MyKlineData kData = new MyKlineData();
        if (stringList.size() >= 6) {
            int i = 0;
            kData.setDate(stringList.get(i++));
            kData.setOpenPrice(stringList.get(i++));
            kData.setHighestPrice(stringList.get(i++));
            kData.setLowestPrice(stringList.get(i++));
            kData.setClosePrice(stringList.get(i++));
            kData.setVolume(stringList.get(i++));
        }
        return new DATA(kData);
    }
}
