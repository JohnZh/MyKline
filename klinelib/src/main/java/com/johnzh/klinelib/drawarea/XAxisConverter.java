package com.johnzh.klinelib.drawarea;

/**
 * Modified by john on 2020/5/17
 * <p>
 * Description:
 */
public interface XAxisConverter {
    /**
     * get the visible index of data from drawX axis when user touch
     *
     * @param drawX
     * @return
     */
    int getVisibleIndex(float drawX);


    /**
     * get the x axis from visible index of data on screen
     *
     * @param visibleIndex
     * @return
     */
    float getDrawX(int visibleIndex);
}
