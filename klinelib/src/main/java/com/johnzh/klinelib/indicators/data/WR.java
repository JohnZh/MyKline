package com.johnzh.klinelib.indicators.data;

import android.util.SparseArray;

/**
 * Created by JohnZh on 2020/5/21
 *
 * <p>WR indicator data structure</p>
 */
public class WR implements InData {

    SparseArray<Float> map;

    public WR() {
        this.map = new SparseArray<>();
    }
    public void put(int wrKey, Float wrValue) {
        map.put(wrKey, wrValue);
    }

    public Float get(int wrKey) {
        return map.get(wrKey);
    }
}
