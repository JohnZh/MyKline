package com.johnzh.klinelib;

import android.graphics.Canvas;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description: index for kline
 */
public interface Index<T> {
    List<T> calcIndex(List<KlineData> klineDataList);

    List<T> calcIndexForVisibleData(List<KlineData> klineDataList, int index, int len);

    void draw(Canvas canvas, List<? extends KlineData> klineDataList, List<T> indexDataList);
}
