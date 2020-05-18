package com.johnzh.klinelib.indicators;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;

import com.johnzh.klinelib.DATA;
import com.johnzh.klinelib.FloatCalc;
import com.johnzh.klinelib.IndicatorData;
import com.johnzh.klinelib.KlineData;
import com.johnzh.klinelib.KlineView;
import com.johnzh.klinelib.ValueRange;
import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.IndexDrawArea;

import java.util.List;

/**
 * Modified by john on 2020/5/6
 * <p>
 * Description:
 */
public class MAIndex extends AbsIndex implements ValueRange {

    private PureKIndex pureKIndex;
    private float lineWidth;
    private int[] ma;
    private int[] colors;

    private float maxPrice;
    private float minPrice;

    public MAIndex(AuxiliaryLines auxiliaryLines,
                   PureKIndex pureKIndex,
                   float lineWidth,
                   int[] ma, int[] colors) {
        super(auxiliaryLines);
        this.pureKIndex = pureKIndex;
        this.lineWidth = lineWidth;
        this.ma = ma;
        this.colors = colors;
        if (ma.length != colors.length) {
            throw new IllegalArgumentException("ma.length is not equal to maColors.length");
        }
    }

    @Override
    public float getMaximum() {
        return maxPrice;
    }

    @Override
    public float getMinimum() {
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
    public void calcIndexAsync(List<DATA> dataList) {

    }

    @Override
    public void calcIndex(List<DATA> dataList, int startIndex, int endIndex) {
        pureKIndex.calcIndex(dataList, startIndex, endIndex);

        resetMaxMinPrice();

        for (int i = startIndex; i < endIndex; i++) {
            IndicatorData indicatorData = dataList.get(i).getIndexData();
            if (indicatorData == null) {
                indicatorData = dataList.get(i).newIndexData();
            }

            for (int maKey : this.ma) {
                if (i - maKey < -1) continue; // data is not enough

                Float maValue = indicatorData.getMa().get(maKey);
                if (maValue != null) {
                    updateMaxMinPrice(maValue);
                    continue;
                }

                Float newMaValue = calcMaValue(dataList, i, maKey);
                updateMaxMinPrice(newMaValue);
                indicatorData.getMa().put(maKey, newMaValue);
            }
            dataList.get(i).setIndexData(indicatorData);
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
    public void drawIndex(KlineView klineView, IndexDrawArea drawArea, int startIndex, int endIndex, Canvas canvas, Paint paint) {
        pureKIndex.drawIndex(klineView, drawArea, startIndex, endIndex, canvas, paint);

        List<? extends KlineData> klineDataList = klineView.getDataList();

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
                float dataX = drawArea.getDrawX(drawArea.getVisibleIndex(j));
                float dataY = drawArea.getDrawY(maValue.floatValue());
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
