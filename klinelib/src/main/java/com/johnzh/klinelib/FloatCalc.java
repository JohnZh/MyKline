package com.johnzh.klinelib;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Modified by john on 2020/5/9
 * <p>
 * Description: Just use rounding_mode: half_even, not use BigDecimal
 */
public class FloatCalc {

    private DecimalFormat decimalFormat;

    private static final FloatCalc sCalc = new FloatCalc();

    private FloatCalc() {
        decimalFormat = (DecimalFormat) NumberFormat.getInstance();
    }

     public static FloatCalc get() {
        return sCalc;
    }

    public float subtraction(float minuend, float subtrahend) {
        return format(minuend - subtrahend, getMaxScale(minuend, subtrahend));
    }

    public float divide(float dividend, float divisor) {
        return format(dividend / divisor, getMaxScale(dividend, divisor));
    }

    private int getMaxScale(float v1, float v2) {
        return Math.max(getScale(v1), getScale(v2));
    }

    private float format(float value, int scale) {
        decimalFormat.setMaximumFractionDigits(scale);
        decimalFormat.setMinimumFractionDigits(scale);
        decimalFormat.setMinimumIntegerDigits(1);
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        String v = decimalFormat.format(value);
        return Float.parseFloat(v);
    }

    public int getScale(float value) {
        String s = Float.toString(value);
        return s.length() - 1 - s.indexOf('.');
    }
}
