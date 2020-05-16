package com.johnzh.klinelib.drawarea;

import com.johnzh.klinelib.KlineView;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public interface DataIndexConverter {

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
     * @return
     */
    int getVisibleIndex(int dataIndex);

    /**
     * Get real data index of visible data on screen.
     * For example, total 10 data, the real indexes of data are [0 - 9].
     * If maximum number of visible data is 5.
     * Initially, the visible data is data[5 - 9],
     * [5 - 9] are the real index of data, startIndex is 5, visible index formula: dataIndex - startIndex,
     * so the visible indexes of data are [0 - 4]
     *
     * @param visibleIndex the visible index of data on screen, must be [0, {@link KlineView#getCandles()} - 1]
     * @return
     */
    int getDataIndex(int visibleIndex);
}
