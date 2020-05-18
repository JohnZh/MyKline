package com.johnzh.klinelib.drawarea;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.KlineView;

import java.util.List;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public interface DrawArea extends XAxisConverter, DataIndexConverter {

    void prepareOnDraw(KlineView view, List<DrawArea> allDrawAreas);

    int getLeft();

    int getWidth();

    int getTop();

    int getHeight();

    boolean contains(float x, float y);

    void hide(boolean hide);

    boolean isVisible();

    void calculate(List<DATA> list, int startIndex, int endIndex);

    void draw(KlineView klineView, Canvas canvas, Paint paint);
}
