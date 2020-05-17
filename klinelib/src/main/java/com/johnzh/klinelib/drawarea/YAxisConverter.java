package com.johnzh.klinelib.drawarea;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public interface YAxisConverter {

    /**
     * get the number from the drawY axis when user touch
     *
     * @param drawY
     * @return
     */
    float getNumber(float drawY);

    /**
     * get the y axis from number of data on screen
     *
     * @param number
     * @return
     */
    float getDrawY(float number);
}
