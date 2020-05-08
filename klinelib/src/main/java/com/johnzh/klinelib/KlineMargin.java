package com.johnzh.klinelib;

/**
 * Modified by john on 2020/5/8
 * <p>
 * Description:
 */
public class KlineMargin {
    private float marginLeft;
    private float marginTop;
    private float marginRight;
    private float marginBottom;

    public KlineMargin(float marginLeft, float marginTop, float marginRight, float marginBottom) {
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public float getMarginBottom() {
        return marginBottom;
    }
}
