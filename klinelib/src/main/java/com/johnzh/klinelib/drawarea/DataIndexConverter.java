/**
 * MIT License
 *
 * Copyright (c) 2020 JohnZh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
