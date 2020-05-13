package com.johnzh.klinelib;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.indexes.Index;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public interface Factory {
    Index createDefaultIndex(Class clazz);

    AuxiliaryLines createDefaultAuxiliaryLines(Class clazz);
}
