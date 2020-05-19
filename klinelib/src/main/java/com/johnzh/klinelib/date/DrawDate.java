package com.johnzh.klinelib.date;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.drawarea.impl.DateDrawArea;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public interface DrawDate {
    void drawDate(KlineView klineView, DateDrawArea drawArea,
                  int startIndex, int endIndex, Canvas canvas, Paint paint);
}
