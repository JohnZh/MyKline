package com.johnzh.klinelib;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class KlineConfig {

    public static final int DEFAULT_CANDLES = 40;

    private int initialCandles;
    private List<Index> indexes;

    public KlineConfig() {
        this.initialCandles = DEFAULT_CANDLES;
        this.indexes = new ArrayList<>();
    }

    public KlineConfig(int initialCandles) {
        this.initialCandles = initialCandles;
        this.indexes = new ArrayList<>();
    }

    public KlineConfig(int initialCandles, @NonNull List<Index> indexes) {
        this.initialCandles = initialCandles;
        this.indexes = indexes;
    }

    public void setIndexes(@NonNull List<Index> indexes) {
        this.indexes = indexes;
    }

    public KlineConfig addIndex(Index index) {
        this.indexes.add(index);
        return this;
    }

    public int getInitialCandles() {
        return initialCandles;
    }

    public List<Index> getIndexes() {
        return indexes;
    }
}
