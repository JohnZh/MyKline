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
