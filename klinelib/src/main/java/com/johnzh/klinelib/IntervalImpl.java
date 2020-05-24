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
