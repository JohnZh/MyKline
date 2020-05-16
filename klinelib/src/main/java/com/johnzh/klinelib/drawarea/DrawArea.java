package com.johnzh.klinelib.drawarea;

import com.johnzh.klinelib.KlineView;

import java.util.List;

/**
 * Modified by john on 2020/5/16
 * <p>
 * Description:
 */
public interface DrawArea {

    void initOnDraw(KlineView view, List<DrawArea> allDrawAreas);

    int getLeft();

    int getWidth();

    int getTop();

    int getHeight();

    void hide(boolean hide);

    boolean isVisible();
}
