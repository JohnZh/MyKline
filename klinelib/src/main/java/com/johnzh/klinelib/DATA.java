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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JohnZh on 2020/5/18
 *
 * <p>Kline data wrapper</p>
 */
public class DATA implements KlineData  {

    public static List<DATA> makeList(List<? extends KlineData> list) {
        List<DATA> dataList = new ArrayList<>();
        for (KlineData data : list) {
            dataList.add(new DATA(data));
        }
        return dataList;
    }

    private KlineData klineData;

    public DATA(KlineData klineData) {
        this.klineData = klineData;
    }

    @Override
    public float getOpenPrice() {
        return klineData.getOpenPrice();
    }

    @Override
    public float getHighestPrice() {
        return klineData.getHighestPrice();
    }

    @Override
    public float getLowestPrice() {
        return klineData.getLowestPrice();
    }

    @Override
    public float getClosePrice() {
        return klineData.getClosePrice();
    }

    @Override
    public float getVolume() {
        return klineData.getVolume();
    }

    @Override
    public Date getDate() {
        return klineData.getDate();
    }

    @Override
    public IndicatorData getIndexData() {
        return klineData.getIndexData();
    }

    @Override
    public IndicatorData newIndexData() {
        return klineData.newIndexData();
    }

    @Override
    public void setIndexData(IndicatorData indexData) {
        klineData.setIndexData(indexData);
    }
}
