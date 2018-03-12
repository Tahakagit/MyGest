package com.example.franc.mygest;

import java.util.Date;

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

    public RealmResults<Movimento> getTransactionsUntil(Date dateTo){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> transactionInDay = mRealm.where(Movimento.class).lessThan("scadenza", dateTo).findAll();
        mRealm.close();
        return transactionInDay;

    }



    public void saveMovimento(final String beneficiario, final String importo, final Date scadenza, final String conto){


        final Movimento movimento = new Movimento();
        movimento.setBeneficiario(beneficiario);
        movimento.setImporto(importo);
        movimento.setScadenza(scadenza);
        movimento.setConto(conto);
        movimento.setTimestamp(System.currentTimeMillis());

        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(movimento);
                if(realm.where(DailyTransaction.class).equalTo("dayOfYear", movimento.getScadenza()).findAll() == null ){
                    DailyTransaction dailyTransaction = new DailyTransaction();
                    dailyTransaction.setDayOfYear(movimento.getScadenza());
                    dailyTransaction.setTimestamp(System.currentTimeMillis());
                    realm.copyToRealmOrUpdate(dailyTransaction);

                }

            }
        });
    }

    public void saveConto(final String nomeConto, final String saldoConto){


        final Conto s=new Conto();
        s.setNomeConto(nomeConto);
        s.setSaldoConto(saldoConto);



        mRealm = Realm.getDefaultInstance();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(s);

            }
        });
    }

}
