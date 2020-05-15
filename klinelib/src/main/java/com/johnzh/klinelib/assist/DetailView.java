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

    void onActionDown(MotionEvent event);

    void onActionMove(MotionEvent event);

    void onActionUp(MotionEvent event);

    void onActionCancel();
}
