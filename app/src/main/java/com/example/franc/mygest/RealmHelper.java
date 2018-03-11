package com.example.franc.mygest;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by franc on 11/03/2018.
 */

public class RealmHelper {

    Realm mRealm;

    public RealmResults<Movimento> getTransactionByDay(String day){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> transactionInDay = mRealm.where(Movimento.class).equalTo("scadenza", day).findAll();
        mRealm.close();
        return transactionInDay;
    }

    public void saveMovimento(final Movimento mov){

        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(mov);
                DailyTransaction dailyTransaction = new DailyTransaction();
                dailyTransaction.setDayOfYear(mov.getScadenza());
                dailyTransaction.setTimestamp(System.currentTimeMillis());
                realm.copyToRealmOrUpdate(dailyTransaction);

            }
        });
    }
}
