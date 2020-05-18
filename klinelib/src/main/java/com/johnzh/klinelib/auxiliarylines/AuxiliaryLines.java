package com.johnzh.klinelib.auxiliarylines;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.ValueRange;
import com.johnzh.klinelib.drawarea.IndexDrawArea;
import com.johnzh.klinelib.indicators.Index;

import java.util.List;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: generally, horizontal lines are about price, index. vertical lines are about date
 */
public interface AuxiliaryLines extends ValueRange {

    void calcHorizontalLines(List<DATA> dataList, Index curIndex, int startIndex, int endIndex);

    void calcVerticalLines(List<DATA> dataList, int startIndex, int endIndex);

    void drawHorizontalLines(KlineView klineView, IndexDrawArea drawArea, Canvas canvas, Paint paint);

    void drawVerticalLines(KlineView klineView, IndexDrawArea drawArea, Canvas canvas, Paint paint);
}