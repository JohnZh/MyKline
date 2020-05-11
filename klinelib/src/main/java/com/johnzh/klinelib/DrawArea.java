package com.johnzh.klinelib;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.size.ViewSize;

/**
 * Modified by john on 2020/5/9
 * <p>
 * Description: Draw area is not contain padding. It is also a class for calculating.
 */
public interface DrawArea {

    void init(KlineView klineView, ViewSize viewSize, AuxiliaryLines auxiliaryLines);

    float getDistanceBetweenData(int visibleDataSize);

    int getDataIndex(int visibleIndex, int startIndex);

    int getVisibleIndex(int dataIndex, int startIndex);

    int getVisibleIndex(float touchX);

    float getDataX(int visibleIndex);

    float getDataY(float price);

    int getTop();

    int getLeft();

    int getWidth();

    int getHeight();

    int getDateTop();
}
