package com.johnzh.klinelib;

import com.johnzh.klinelib.indexes.MAIndex;

/**
 * Modified by john on 2020/5/11
 * <p>
 * Description:
 */
public class IndexData {

    private MAIndex.MA ma;

    public MAIndex.MA getMa() {
        if (ma == null) {
            ma = new MAIndex.MA();
        }
        return ma;
    }
}
