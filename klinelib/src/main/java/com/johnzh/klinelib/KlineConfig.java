package com.johnzh.klinelib;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.indexes.Index;
import com.johnzh.klinelib.size.ViewSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class KlineConfig {

    private static final int DEFAULT_CANDLES = 40;

    private int initialCandles;
    private List<Index> indexes;
    private ViewSize viewSize;
    private AuxiliaryLines auxiliaryLines;

    private KlineConfig() {
    }

    public int getInitialCandles() {
        return initialCandles;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public ViewSize getViewSize() {
        return viewSize;
    }

    public AuxiliaryLines getAuxiliaryLines() {
        return auxiliaryLines;
    }

    public static final class Builder {

        private int initialCandles;
        private List<Index> indexes;
        private ViewSize viewSize;
        private AuxiliaryLines auxiliaryLines;

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

        public Builder viewSize(ViewSize viewSize) {
            this.viewSize = viewSize;
            return this;
        }

        public Builder auxiliaryLines(AuxiliaryLines auxiliaryLines) {
            this.auxiliaryLines = auxiliaryLines;
            return this;
        }

        public KlineConfig build() {
            KlineConfig klineConfig = new KlineConfig();
            klineConfig.initialCandles = initialCandles;
            klineConfig.indexes = indexes;
            klineConfig.viewSize = this.viewSize;
            klineConfig.auxiliaryLines = this.auxiliaryLines;
            return klineConfig;
        }
    }
}
