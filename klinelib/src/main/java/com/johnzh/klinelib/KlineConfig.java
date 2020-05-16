package com.johnzh.klinelib;

import com.johnzh.klinelib.date.DrawDate;
import com.johnzh.klinelib.indexes.Index;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class KlineConfig {

    private static final int DEFAULT_CANDLES = 70;

    private int initialCandles;

    private KlineConfig() {
    }

    public int getInitialCandles() {
        return initialCandles;
    }

    public void setInitialCandles(int initialCandles) {
        this.initialCandles = initialCandles;
    }


    public static final class Builder {

        private int initialCandles;
        private List<Index> indexes;
        private DrawDate drawDate;

        public Builder() {
            initialCandles = DEFAULT_CANDLES;
            indexes = new ArrayList<>();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder initialCandles(int initialCandles) {
            this.initialCandles = initialCandles;
            return this;
        }

        public Builder indexes(List<Index> indexes) {
            this.indexes = indexes;
            return this;
        }

        public Builder index(Index index) {
            this.indexes.add(index);
            return this;
        }

        public Builder drawDate(DrawDate drawDate) {
            this.drawDate = drawDate;
            return this;
        }

        public KlineConfig build() {
            KlineConfig klineConfig = new KlineConfig();
            klineConfig.initialCandles = initialCandles;
            return klineConfig;
        }
    }
}
