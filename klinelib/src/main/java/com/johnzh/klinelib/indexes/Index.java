package com.johnzh.klinelib.indexes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.IndexDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description: index for kline
 */
public interface Index<T extends KlineData> {

    void calcIndexAsync(List<T> klineDataList);

    void calcIndex(List<T> klineDataList, int startIndex, int endIndex);

    void calcAuxiliaryLines(List<T> klineDataList, int startIndex, int endIndex);

    void drawAuxiliaryLines(KlineView klineView, IndexDrawArea drawArea, Canvas canvas, Paint paint);

    void drawIndex(KlineView klineView, IndexDrawArea drawArea, int startIndex, int endIndex,
                   Canvas canvas, Paint paint);

    AuxiliaryLines<T> getAuxiliaryLines();
}
