package com.johnzh.klinelib.indexes;

/**
 * Modified by john on 2020/5/13
 * <p>
 * Description:
 */
public interface IndexFactory {
    Index createDefaultIndex(Class clazz);
}
