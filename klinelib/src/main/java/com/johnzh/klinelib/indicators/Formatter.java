package com.johnzh.klinelib.indicators;

import com.johnzh.klinelib.DATA;

/**
 * Created by JohnZh on 2020/5/19
 *
 * <p>Formatter to format indicator data </p>
 */
public interface Formatter {
    String format(DATA data);
}
