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

import com.johnzh.klinelib.detail.DetailView;
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
            activeDetailAction = DetailView.TRIGGERED_BY_LONG_PRESS;
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
