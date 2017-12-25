package com.example.franc.mygest;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by franc on 24/12/2017.
 */

public class DailyTransaction extends RealmObject {

    private long timestamp;

    String dayOfYear = null;
/*
    List<Movimento> transactions = null;
*/

    public String getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(String dayOfYear) {
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
