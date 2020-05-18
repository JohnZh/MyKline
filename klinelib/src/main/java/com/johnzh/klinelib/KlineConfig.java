package com.johnzh.klinelib;

import com.johnzh.klinelib.gesture.Scale;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class KlineConfig {

    private static final int DEFAULT_CANDLES = 70;

    private int initialCandles;
    private int activeDetailAction;
    private Scale scale;

    private KlineConfig() {
    }

    public int getInitialCandles() {
        return initialCandles;
    }

    public void setInitialCandles(int initialCandles) {
        this.initialCandles = initialCandles;
    }

    public int getActiveDetailAction() {
        return activeDetailAction;
    }

    public void setActiveDetailAction(int activeDetailAction) {
        this.activeDetailAction = activeDetailAction;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public static final class Builder {

        private int initialCandles;
        private int activeDetailAction;
        private Scale scale;

        public Builder() {
            initialCandles = DEFAULT_CANDLES;
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder initialCandles(int initialCandles) {
            this.initialCandles = initialCandles;
            return this;
        }

        public Builder activeDetailAction(int activeDetailAction) {
            this.activeDetailAction = activeDetailAction;
            return this;
        }

        public Builder scale(Scale scale) {
            this.scale = scale;
            return this;
        }

        public KlineConfig build() {
            KlineConfig klineConfig = new KlineConfig();
            klineConfig.initialCandles = initialCandles;
            klineConfig.activeDetailAction = activeDetailAction;
            klineConfig.scale = scale;
            return klineConfig;
        }
    }
}
