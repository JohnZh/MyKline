/**
 * MIT License
 *
 * Copyright (c) 2020 JohnZh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
    private int formatScale;

    private static final FloatCalc sCalc = new FloatCalc();

    private FloatCalc() {
        decimalFormat = (DecimalFormat) NumberFormat.getInstance();
        formatScale = -1;
    }

    public void setFormatScale(int formatScale) {
        this.formatScale = formatScale;
    }

    public static FloatCalc get() {
        return sCalc;
    }

    public float multiply(float multiplicand, float multiplier) {
        return Float.parseFloat(format(multiplicand * multiplier, getMaxScale(multiplicand, multiplier)));
    }

    public float subtraction(float minuend, float subtrahend) {
        return Float.parseFloat(format(minuend - subtrahend, getMaxScale(minuend, subtrahend)));
    }

    public float divide(float dividend, float divisor) {
        return Float.parseFloat(format(dividend / divisor, getMaxScale(dividend, divisor)));
    }

    public float add(float summand, float addend) {
        return Float.parseFloat(format(summand + addend, getMaxScale(summand, addend)));
    }

    private int getMaxScale(float v1, float v2) {
        return Math.max(getScale(v1), getScale(v2));
    }

    public String format(float value, int scale) {
        decimalFormat.setMaximumFractionDigits(scale);
        decimalFormat.setMinimumFractionDigits(scale);
        decimalFormat.setMinimumIntegerDigits(1);
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        return decimalFormat.format(value);
    }

    public int getScale(float value) {
        String s = Float.toString(value);
        s = s.replaceAll("0+?$", "");
        return s.length() - 1 - s.indexOf('.');
    }

    public String stripTrailingZeros(float value) {
        String s = Float.toString(value);
        return s.replaceAll("0+?$", "")
                .replaceAll("[.]$", "");
    }

    public String format(float value) {
        return formatScale >= 0 ? format(value, formatScale) : stripTrailingZeros(value);
    }
}
