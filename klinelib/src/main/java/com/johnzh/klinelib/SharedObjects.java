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

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Modified by john on 2020/5/10
 * <p>
 * Description:
 */
public class SharedObjects {

    private Path mPath;
    private StringBuilder mStringBuilder;
    private RectF mRectF;

    public SharedObjects() {
        mPath = new Path();
        mStringBuilder = new StringBuilder();
        mRectF = new RectF();
    }

    public <T> T getObject(Class<T> clazz) {
        if (clazz.isInstance(mPath)) {
            mPath.reset();
            return (T) mPath;
        }

        if (clazz.isInstance(mStringBuilder)) {
            mStringBuilder.setLength(0);
            return (T) mStringBuilder;
        }

        if (clazz.isInstance(mRectF)) {
            mRectF.setEmpty();
            return (T) mRectF;
        }
        return null;
    }
}
