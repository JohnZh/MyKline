package com.johnzh.klinelib.gesture;

/**
 * Modified by john on 2020/5/14
 * <p>
 * Description:
 */
public final class Scale {
    private static final float MAX_SCALE = 7;
    private static final float MIN_SCALE = 0.5f;
    private static final float DEFAULT_SCALE = 1;

    public interface Listener {
        void onScaleChanged(float scale);
    }

    private float maxScale;
    private float minScale;
    private float scale;

    public Scale() {
        this(MAX_SCALE, MIN_SCALE);
    }

    public Scale(float maxScale, float minScale) {
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.scale = DEFAULT_SCALE;
    }

    public float getMaxScale() {
        return maxScale;
    }

    public float getMinScale() {
        return minScale;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
