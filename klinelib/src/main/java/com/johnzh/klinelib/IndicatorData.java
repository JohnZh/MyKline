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

import com.johnzh.klinelib.indicators.data.InData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2020/5/11
 * <p>
 * Description:
 */
public class IndicatorData {

    private List<InData> indicatorDataList;

    public IndicatorData() {
        this.indicatorDataList = new ArrayList<>();
    }

    public <T extends InData> T get(Class<T> clazz) {
        InData result = null;
        for (InData inData : indicatorDataList) {
            if (inData.getClass() == clazz) {
                result = inData;
                break;
            }
        }
        if (result == null) {
            if (InData.class.isAssignableFrom(clazz)) {
                try {
                    result = clazz.newInstance();
                    indicatorDataList.add(result);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException("The type of clazz should be subclass of InData");
            }
        }
        return (T) result;
    }
}