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
package com.johnzh.klinelib.detail;

import android.view.MotionEvent;

import com.johnzh.klinelib.KlineView;

/**
 * Modified by john on 2020/5/14
 * <p>
 * Description:
 */
public interface DetailView {

    int TRIGGERED_BY_LONG_PRESS = 666;
    int TRIGGERED_BY_DOUBLE_TAP = 888;

    /**
     * Call when {@link KlineView#setDetailView(DetailView)}
     *
     * @param klineView
     */
    void attach(KlineView klineView);

    /**
     * Call before {@link this#onMove(MotionEvent)} with first MOVE event
     *
     * @param event first MOVE event
     */
    void onStart(MotionEvent event);

    /**
     * Call after {@link this#onStart(MotionEvent)} or {@link this#onDownAgain(MotionEvent)}
     *
     * @param event
     */
    void onMove(MotionEvent event);

    /**
     * Call before {@link this#onMove(MotionEvent)} with DOWN event
     *
     * @param event
     */
    void onDownAgain(MotionEvent event);


    /**
     * Call after {@link this#onMove(MotionEvent)} with CANCEL/UP event, when finger up
     *
     * @param event
     */
    void onUp(MotionEvent event);

    /**
     * Call after {@link this#onMove(MotionEvent)} with CANCEL/UP event, when cancel detail feature
     * @param event
     */
    void onCancel(MotionEvent event);

    /**
     * Call before its attached KlineView draw elements
     *
     * @param view
     */
    void onPreKlineViewDraw(KlineView view);

    /**
     * Call after its attached KlineView draw elements
     *
     * @param view
     */
    void onPostKlineViewDraw(KlineView view);
}
