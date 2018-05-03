package com.example.franc.mygest.persistence;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by franc on 23/04/2018.
 */

public class DateConverter {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
