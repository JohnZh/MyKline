package com.johnzh.klinelib.size;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: data part height and date part height are fixed
 */
public class DefaultViewSize extends ViewSize {

    public static final int DATA_HEIGHT_IN_DP = 200;
    public static final int DATE_HEIGHT_IN_DP = 30;

    public DefaultViewSize(int dataHeight, int dateHeight) {
        super(dataHeight, dateHeight);
    }
}
