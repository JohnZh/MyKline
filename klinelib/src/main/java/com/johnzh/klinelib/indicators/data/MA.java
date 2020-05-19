package com.johnzh.klinelib.indicators.data;

import android.util.SparseArray;

/**
 * Created by JohnZh on 2020/5/19
 *
 * <p>Data structure for MA indicator</p>
 */
public class MA implements InData {
    SparseArray<Float> map;

    public MA() {
        this.map = new SparseArray<>();
    }

    public void put(int maKey, Float maValue) {
        map.put(maKey, maValue);
    }

    public Float get(int maKey) {
        return map.get(maKey);
    }
}
