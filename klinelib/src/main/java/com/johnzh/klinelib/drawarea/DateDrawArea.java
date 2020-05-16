package com.johnzh.klinelib.drawarea;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.date.DrawDate;

import java.util.List;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public class DateDrawArea extends SizedDrawArea implements DataIndexConverter {

    private DrawDate mDrawDate;
    private int mStartIndex;

    public DateDrawArea(int height, DrawDate drawDate) {
        super(height);
        mDrawDate = drawDate;
    }

    @Override
    public void prepareOnDraw(KlineView view, List<DrawArea> allDrawAreas) {
        super.prepareOnDraw(view, allDrawAreas);
        mStartIndex = view.getStartIndex();
    }

    @Override
    public void calculate(List<? extends KlineData> list, int startIndex, int endIndex) {
    }

    @Override
    public void draw(KlineView klineView, Canvas canvas, Paint paint) {
        mDrawDate.drawDate(klineView, this,
                klineView.getStartIndex(), klineView.getEndIndex(),
                canvas, paint);
    }

    public DrawDate getDrawDate() {
        return mDrawDate;
    }

    @Override
    public int getDataIndex(int visibleIndex) {
        return visibleIndex + mStartIndex;
    }

    @Override
    public int getVisibleIndex(int dataIndex) {
        return dataIndex - mStartIndex;
    }
}
