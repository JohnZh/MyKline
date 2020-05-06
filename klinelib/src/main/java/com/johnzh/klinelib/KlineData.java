package com.johnzh.klinelib;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public interface KlineData {

    float getOpenPrice();

    float getHighestPrice();

    float getLowestPrice();

    float getClosePrice();

    float getVolume();

    long getDate();
}
