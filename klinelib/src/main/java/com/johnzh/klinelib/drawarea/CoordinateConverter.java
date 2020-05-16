package com.johnzh.klinelib.drawarea;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public interface CoordinateConverter {

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
}
