package com.johnzh.klinelib.assist;

import android.view.MotionEvent;

import com.johnzh.klinelib.KlineView;

/**
 * Modified by john on 2020/5/14
 * <p>
 * Description:
 */
public interface DetailView {

    /**
     * Call when {@link KlineView#setDetailView(DetailView)}
     *
     * @param klineView
     */
    void attach(KlineView klineView);

    /**
     * Touch event from klineView about detail feature, down, move, up, cancel
     *
     * @param event
     */
    void onKlineViewTouchEvent(MotionEvent event);

    /**
     * Call before {@link this#onKlineViewTouchEvent(MotionEvent)} with down event
     *
     * @param event
     */
    void onStart(MotionEvent event);

    /**
     * Call after {@link this#onKlineViewTouchEvent(MotionEvent)} with cancel/up event
     * @param event
     */
    void onCancel(MotionEvent event);
}
