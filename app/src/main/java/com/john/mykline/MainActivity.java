package com.john.mykline;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.john.mykline.bean.MyKlineData;
import com.john.mykline.databinding.ActivityMainBinding;
import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.drawarea.impl.DateDrawArea;
import com.johnzh.klinelib.element.DrawDate;
import com.johnzh.klinelib.indicators.BOLLIndicator;
import com.johnzh.klinelib.indicators.MAIndicator;
import com.johnzh.klinelib.indicators.VOLIndicator;
import com.johnzh.klinelib.indicators.WRIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());

        mBinding.klineView.setDetailView(mBinding.detailView);

        mBinding.pureK.setOnClickListener(v -> {
            mBinding.klineView.selectIndicator(1, 0);
        });
        mBinding.ma.setOnClickListener(v -> {
            mBinding.klineView.selectIndicator(MAIndicator.class);
        });
        mBinding.boll.setOnClickListener(v -> {
            mBinding.klineView.selectIndicator(BOLLIndicator.class);
        });

        mBinding.vol.setOnClickListener(v -> {
            mBinding.klineView.selectIndicator(VOLIndicator.class);
        });
        mBinding.wr.setOnClickListener(v -> {
            mBinding.klineView.selectIndicator(WRIndicator.class);
        });

        mBinding.areasAndAppendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
        mBinding.modifyStyles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangeStyleActivity.class));
            }
        });
        mBinding.changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DateDrawArea> drawAreaList = mBinding.klineView.getDrawAreaList(DateDrawArea.class);
                DrawDate drawDate = drawAreaList.get(0).getDrawDate();
                
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
