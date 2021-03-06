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
package com.johnzh.klinelib.indicators.data;

/**
 * Created by JohnZh on 2020/5/20
 *
 * <p>BOLL indicator data structure</p>
 */
public class BOLL implements InData {
    private Float MID;
    private Float UPPER;
    private Float LOWER;

    public Float getMID() {
        return MID;
    }

    public void setMID(Float MID) {
        this.MID = MID;
    }

    public Float getUPPER() {
        return UPPER;
    }

    public void setUPPER(Float UPPER) {
        this.UPPER = UPPER;
    }

    public Float getLOWER() {
        return LOWER;
    }

    public void setLOWER(Float LOWER) {
        this.LOWER = LOWER;
    }
}
