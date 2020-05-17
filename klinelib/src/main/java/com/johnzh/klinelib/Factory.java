package com.johnzh.klinelib;

import com.johnzh.klinelib.auxiliarylines.AuxiliaryLines;
import com.johnzh.klinelib.drawarea.DrawArea;
import com.johnzh.klinelib.indicators.Index;

import java.util.List;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public interface Factory {
    List<DrawArea> createDrawAreas();

    Index createDefaultIndex(Class clazz);

    AuxiliaryLines createDefaultAuxiliaryLines(Class clazz);
}
