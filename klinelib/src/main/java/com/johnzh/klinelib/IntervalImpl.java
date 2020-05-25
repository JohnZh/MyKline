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

/**
 * Created by JohnZh on 2020/5/24
 *
 * <p>Class to save interval</p>
 */
public class IntervalImpl implements Interval {
    private float max;
    private float min;

    public IntervalImpl() {
        this.max = Float.MIN_VALUE;
        this.min = Float.MAX_VALUE;
    }

    public void reset() {
        this.max = Float.MIN_VALUE;
        this.min = Float.MAX_VALUE;
    }

    public void updateMaxMin(float max, float min) {
        this.max = Math.max(this.max, max);
        this.min = Math.min(this.min, min);
    }

    public void updateMaxMin(float value) {
        this.max = Math.max(this.max, value);
        this.min = Math.min(this.min, value);
    }

    @Override
    public float getMax() {
        return max;
    }

    @Override
    public float getMin() {
        return min;
    }
}
