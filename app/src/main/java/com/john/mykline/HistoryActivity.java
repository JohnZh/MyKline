package com.john.mykline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.john.mykline.bean.MyKlineData;
import com.john.mykline.databinding.ActivityHistoryBinding;
import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.Factory;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    ActivityHistoryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHistoryBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());

        Factory factory = new MyFactory(this);
        mBinding.klineView.setDrawAreaList(factory);
        mBinding.klineView.setDetailView(mBinding.detailView);
        mBinding.klineView.setOnDataDragListener((remainingData, draggedData, visibleData) -> {
            if (remainingData == 0) {
                Toast.makeText(this, "获取假的历史数据", Toast.LENGTH_SHORT).show();
                getFakeHistoricalData();
            }
        });

        getTestData();
    }

    private void getFakeHistoricalData() {
        HttpAgent.getApi().getDailyKline("M2009")
                .enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        List<DATA> data = convertData(response.body());
                        mBinding.klineView.addHistoricalData(data);
                    }

                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {
                    }
                });
    }

    private void getTestData() {
        HttpAgent.getApi().getDailyKline("M2009")
                .enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        List<DATA> data = convertData(response.body());
                        mBinding.klineView.setDataList(data);
                    }

                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {
                    }
                });
    }

    private List<DATA> convertData(List<List<String>> body) {
        List<MyKlineData> list = new ArrayList<>();
        for (List<String> stringList : body) {
            list.add(getData(stringList));
        }
        return DATA.makeList(list);
    }

    private MyKlineData getData(List<String> stringList) {
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
        return kData;
    }
}
