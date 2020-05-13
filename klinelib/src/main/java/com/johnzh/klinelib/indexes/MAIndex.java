package com.johnzh.klinelib.indexes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;

import com.johnzh.klinelib.DrawArea;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.IndexData;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.PriceRange;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class MAIndex implements Index<KlineData>, PriceRange {

    private PureKIndex pureKIndex;
    private float lineWidth;
    private int[] ma;
    private int[] colors;

    private float maxPrice;
    private float minPrice;

    public MAIndex(@NonNull PureKIndex pureKIndex, @NonNull float lineWidth,
                   @NonNull int[] ma, @NonNull int[] maColors) {
        this.pureKIndex = pureKIndex;
        this.lineWidth = lineWidth;
        this.ma = ma;
        this.colors = maColors;
        if (ma.length != maColors.length) {
            throw new IllegalArgumentException("ma.length is not equal to maColors.length");
        }
    }

    @Override
    public float getMaxPrice() {
        return maxPrice;
    }

    @Override
    public float getMinPrice() {
        return minPrice;
    }

    public static class MA {
        SparseArray<Float> map;

        public MA() {
            this.map = new SparseArray<>();
        }

        public void put(int maKey, Float maValue) {
            map.put(maKey, maValue);
        }

        public Float get(int maKey) {
            return map.get(maKey);
        }
    }

    @Override
    public void calcIndexAsync(List<KlineData> klineDataList) {

    }

    @Override
    public void calcIndex(List<KlineData> klineDataList, int startIndex, int endIndex) {
        pureKIndex.calcIndex(klineDataList, startIndex, endIndex);

        resetMaxMinPrice();

        for (int i = startIndex; i < endIndex; i++) {
            IndexData indexData = klineDataList.get(i).getIndexData();
            if (indexData == null) {
                indexData = klineDataList.get(i).newIndexData();
            }

            for (int maKey : this.ma) {
                if (i - maKey < -1) continue; // data is not enough

                Float maValue = indexData.getMa().get(maKey);
                if (maValue != null) {
                    updateMaxMinPrice(maValue);
                    continue;
                }

                Float newMaValue = calcMaValue(klineDataList, i, maKey);
                updateMaxMinPrice(newMaValue);
                indexData.getMa().put(maKey, newMaValue);
            }
            klineDataList.get(i).setIndexData(indexData);
        }
    }

    private void resetMaxMinPrice() {
        maxPrice = Float.MIN_VALUE;
        minPrice = Float.MAX_VALUE;
    }

    private void updateMaxMinPrice(Float price) {
        maxPrice = Math.max(maxPrice, price);
        minPrice = Math.min(minPrice, price);
    }

    private Float calcMaValue(List<? extends KlineData> klineDataList, int curIndex, int maKey) {
        int start = curIndex - maKey + 1;
        float result = 0;
        for (int i = start; i <= curIndex; i++) {
            float closePrice = klineDataList.get(i).getClosePrice();
            result = FloatCalc.get().add(result, closePrice);
        }
        return FloatCalc.get().divide(result, maKey);
    }

    @Override
    public void onDraw(KlineView klineView, int startIndex, int endIndex, Canvas canvas, Paint paint) {
        pureKIndex.onDraw(klineView, startIndex, endIndex, canvas, paint);

        List<? extends KlineData> klineDataList = klineView.getKlineDataList();
        DrawArea drawArea = klineView.getDrawArea();

        for (int i = 0; i < ma.length; i++) {
            int maKey = ma[i];
            int maColor = colors[i];
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineWidth);
            paint.setColor(maColor);

            float startX = -1;
            float startY = -1;
            for (int j = startIndex; j < endIndex; j++) {
                Float maValue = klineDataList.get(j).getIndexData().getMa().get(maKey);
                if (maValue == null) continue;
                float dataX = drawArea.getDataX(drawArea.getVisibleIndex(j, startIndex));
                float dataY = drawArea.getDataY(maValue.floatValue());
                if (startX == -1 && startY == -1) { // first point
                    startX = dataX;
                    startY = dataY;
                } else {
                    canvas.drawLine(startX, startY, dataX, dataY, paint);
                    startX = dataX;
                    startY = dataY;
                }
            }
        }
    }
}
