package com.johnzh.klinelib.drawarea;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;

import java.util.List;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public interface DrawArea {

    void prepareOnDraw(KlineView view, List<DrawArea> allDrawAreas);

    int getLeft();

    int getWidth();

    int getTop();

    int getHeight();

    void hide(boolean hide);

    boolean isVisible();

    void calculate(List<? extends KlineData> list, int startIndex, int endIndex);

    void draw(KlineView klineView, Canvas canvas, Paint paint);
}
