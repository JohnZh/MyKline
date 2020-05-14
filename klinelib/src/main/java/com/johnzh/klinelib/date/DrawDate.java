package com.johnzh.klinelib.date;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineView;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public interface DrawDate {
    void drawDate(KlineView klineView, int startIndex, int endIndex, Canvas canvas, Paint paint);
}
