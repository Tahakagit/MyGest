package com.example.franc.mygest.persistence;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;

/**
 * Created by franc on 23/04/2018.
 */

public class MoneyConverter {
    @TypeConverter
    public BigDecimal fromLong(Long value) {
        return value == null ? null : new BigDecimal(value).divide(new BigDecimal(100));
    }

    @TypeConverter
    public Long toLong(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        } else {
            return bigDecimal.multiply(new BigDecimal(100)).longValue();
        }
    }

}
