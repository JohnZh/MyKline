package com.johnzh.klinelib.size;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description: Fixed view size, hide date part, view size still same
 */
public class FixedViewSize extends ViewSize {

    public FixedViewSize(int fixedHeight, float dateRatio) {
        dateHeight = (int) (fixedHeight * dateRatio);
        dataHeight = fixedHeight - dateHeight;
    }
}
