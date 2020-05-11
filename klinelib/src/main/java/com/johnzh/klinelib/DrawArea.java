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

    /**
     * Get index of visible data on screen.
     * For example, total 10 data, the real indexes of data are 0 - 9.
     * If maximum number of visible data is 5.
     * Initially, the visible data is data[5 - 9],
     * 5 - 9 are the real index of data, startIndex is 5, visible index is (real index - startIndex),
     * so the visible indexes of data are still 0 - 4
     *
     * @param visibleIndex the visible index of data on screen, must be 0 - (initialCandles - 1)
     * @param startIndex the index of data to be calculate or draw
     * @return
     */
    int getDataIndex(int visibleIndex, int startIndex);

    /**
     * Get index of visible data on screen.
     * For example, total 10 data, the real indexes of data are 0 - 9.
     * If maximum number of visible data is 5.
     * Initially, the visible data is data[5 - 9],
     * 5 - 9 are the real index of data, startIndex is 5, visible index is (real index - startIndex),
     * so the visible indexes of data are still 0 - 4
     *
     * @param dataIndex the real index of data on screen
     * @param startIndex the index of data to be calculate or draw
     * @return
     */
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
