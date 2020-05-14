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

    float getDataWidth(int visibleDataSize);

    /**
     * Get real data index of visible data on screen.
     * For example, total 10 data, the real indexes of data are [0 - 9].
     * If maximum number of visible data is 5.
     * Initially, the visible data is data[5 - 9],
     * [5 - 9] are the real index of data, startIndex is 5, visible index formula: dataIndex - startIndex,
     * so the visible indexes of data are [0 - 4]
     *
     * @param visibleIndex the visible index of data on screen, must be [0, {@link KlineView#getCandles()} - 1]
     * @param startIndex the index of the first data to be calculate or draw
     * @return
     */
    int getDataIndex(int visibleIndex, int startIndex);

    /**
     * Get index of visible data on screen.
     * The visible index is always [0, {@link KlineView#getCandles()} - 1].
     * For example, total 10 data, the real indexes of data are [0 - 9].
     * If maximum number of visible data is 5.
     * Initially, the visible data is data[5 - 9],
     * [5 - 9] are the real index of data, startIndex is 5, visible index formula: dataIndex - startIndex,
     * so the visible indexes of data are [0 - 4]
     *
     * @param dataIndex the real index of data on screen
     * @param startIndex the index of the first data to be calculate or draw
     * @return
     */
    int getVisibleIndex(int dataIndex, int startIndex);

    /**
     * get the visible index of data from x axis when user touch
     *
     * @param drawX
     * @return
     */
    int getVisibleIndex(float drawX);

    /**
     * get the number from the y axis when user touch
     *
     * @param drawY
     * @return
     */
    float getNumber(float drawY);

    /**
     * get the x axis from visible index of data on screen
     *
     * @param visibleIndex
     * @return
     */
    float getDrawX(int visibleIndex);

    /**
     * get the y axis from number of data on screen
     *
     * @param number
     * @return
     */
    float getDrawY(float number);

    int getTop();

    int getLeft();

    int getWidth();

    int getHeight();

    int getDateTop();
}
