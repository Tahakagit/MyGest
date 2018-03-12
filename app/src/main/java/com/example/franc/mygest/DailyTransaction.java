package com.example.franc.mygest;

import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by franc on 24/12/2017.
 */

public class DailyTransaction extends RealmObject {
    @PrimaryKey

    private long timestamp;
    private Date dayOfYear = null;
/*
    List<Movimento> transactions = null;
*/

    public Date getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(Date dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

/*
    public List<Movimento> getBanche() {
        return transactions;
    }

    public void setBanche(List<Movimento> banche) {
        this.transactions = banche;
    }
*/

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
