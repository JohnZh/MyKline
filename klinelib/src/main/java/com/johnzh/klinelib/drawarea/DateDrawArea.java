package com.johnzh.klinelib.drawarea;

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
    public void initOnDraw(KlineView view, List<DrawArea> allDrawAreas) {
        super.initOnDraw(view, allDrawAreas);
        mStartIndex = view.getStartIndex();
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
