package com.john.mykline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.john.mykline.bean.MyKlineData;
import com.john.mykline.databinding.ActivityMainBinding;
import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineConfig;
import com.johnzh.klinelib.detail.DetailView;
import com.johnzh.klinelib.gesture.DragInfo;
import com.johnzh.klinelib.indicators.BOLLIndicator;
import com.johnzh.klinelib.indicators.MAIndicator;
import com.johnzh.klinelib.indicators.VOLIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "KlineView";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.klineView.setConfig(
                new KlineConfig.Builder()
                        .activeDetailAction(DetailView.TRIGGERED_BY_DOUBLE_TAP)
                        .build());
        binding.klineView.setDetailView(binding.detailView);
        binding.klineView.setOnDataDragListener(new DragInfo.Listener() {
            @Override
            public void onDrag(int remainingData, int draggedData, int visibleData) {
                if (binding.klineView.getDragInfo().isLeftMost()) {
                    getTestDataForHistory();
                }
            }
        });

        binding.pureK.setOnClickListener(v -> {
            binding.klineView.selectIndicator(1, 0);
        });
        binding.ma.setOnClickListener(v -> {
            binding.klineView.selectIndicator(MAIndicator.class);
        });
        binding.boll.setOnClickListener(v -> {
            binding.klineView.selectIndicator(BOLLIndicator.class);
        });

        binding.vol.setOnClickListener(v -> {
            binding.klineView.selectIndicator(VOLIndicator.class);
        });

        binding.combination.setOnClickListener(v -> {
            getTestData(true);
            //startCombinationActivity();
        });

        getTestData(false);

    }

    private void getTestData(boolean buffered) {
        HttpAgent.getApi().getDailyKline("M2009")
                .enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        List<DATA> data = new ArrayList<>();
                        convertData(response.body(), data);
                        if (buffered) {
                            DATA more = data.get(data.size() - 1);
                            data.add(more);
                            binding.klineView.setBufferedList(data);
                        } else {
                            Log.d(TAG, "onResponse: " + data.size());
                            binding.klineView.setDataList(data);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {

                    }
                });
    }

    private void getTestDataForHistory() {
        HttpAgent.getApi().getDailyKline("M2009")
                .enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        List<DATA> data = new ArrayList<>();
                        convertData(response.body(), data);
                        binding.klineView.addHistoricalData(data);
                    }

                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {}
                });
    }

    private void startCombinationActivity() {
        startActivity(new Intent(this, CombinationActivity.class));
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
