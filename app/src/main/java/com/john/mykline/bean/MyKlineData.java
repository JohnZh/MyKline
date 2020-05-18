package com.john.mykline.bean;

import com.johnzh.klinelib.KlineData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Modified by john on 2020/5/7
 * <p>
 * Description:
 */
public class MyKlineData implements KlineData<MyIndicatorData> {

    private static final String DAILY_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT
            = new SimpleDateFormat(DAILY_FORMAT);

    private String date;
    private String openPrice;
    private String highestPrice;
    private String lowestPrice;
    private String closePrice;
    private String volume;

    @Override
    public String toString() {
        return "KData{" +
                "date='" + date + '\'' +
                ", openPrice='" + openPrice + '\'' +
                ", highestPrice='" + highestPrice + '\'' +
                ", lowestPrice='" + lowestPrice + '\'' +
                ", closePrice='" + closePrice + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public void setHighestPrice(String highestPrice) {
        this.highestPrice = highestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public float getOpenPrice() {
        return Float.parseFloat(openPrice);
    }

    @Override
    public float getHighestPrice() {
        return Float.parseFloat(highestPrice);
    }

    @Override
    public float getLowestPrice() {
        return Float.parseFloat(lowestPrice);
    }

    @Override
    public float getClosePrice() {
        return Float.parseFloat(closePrice);
    }

    @Override
    public float getVolume() {
        return Float.parseFloat(volume);
    }

    @Override
    public Date getDate() {
        try {
            return SIMPLE_DATE_FORMAT.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private MyIndicatorData indexData;

    @Override
    public MyIndicatorData getIndexData() {
        return indexData;
    }

    @Override
    public MyIndicatorData newIndexData() {
        return new MyIndicatorData();
    }


    @Override
    public void setIndexData(MyIndicatorData indexData) {
        this.indexData = indexData;
    }
}
