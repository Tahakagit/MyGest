package com.example.franc.mygest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by franc on 11/03/2018.
 */

public class RealmHelper {

    Realm mRealm;

    public RealmResults<Movimento> getTransactionByDay(String day){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> transactionInDay = mRealm.where(Movimento.class).equalTo("accountName", day).findAll();
        mRealm.close();
        return transactionInDay;
    }

    public RealmResults<Movimento> getTransactionsUntil(Date dateTo){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> transactionInDay = mRealm.where(Movimento.class).lessThan("scadenza", dateTo).findAll();
        mRealm.close();
        return transactionInDay;

    }


    public Conto getAccountByName(String accountName){
        mRealm = Realm.getDefaultInstance();

        // cerco i conti con transazioni in accountName nel range todo fix this

        Conto conto = mRealm.where(Conto.class).equalTo("nomeConto", accountName).findFirst();
        mRealm.close();
        return conto;


    }

    /**
     * @param dateTo Transactions max due date
     * @return an array of affected by transactions account
     */
    public ArrayList<ContoObj> getTransactionsUntilGroupedByAccount(Date dateTo){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> allTransaction = getTransactionsUntil(dateTo);

        // cerco i conti con transazioni in accountName nel range todo fix this
        RealmResults<Movimento> conti = allTransaction.where().distinct("conto");
        ArrayList<ContoObj> listConti = new ArrayList<>();

        for (Movimento mov:conti) {
            ContoObj contoO = new ContoObj(getAccountByName(mov.getConto()).getNomeConto(), getAccountByName(mov.getConto()).getSaldoConto().toString(), getAccountByName(mov.getConto()).getColoreConto() );
/*
            listConti.add(getAccountByName(mov.getConto()));
*/
            listConti.add(contoO);
        }

        mRealm.close();
        return listConti;

    }

    /**
     * Get all transaction within due date by account
     * @param dateTo Transactions max due date
     * @param account account to filter by
     * @return Realm result of Movimento
     */
    public RealmResults<Movimento> getTransactionsUntilGroupedBySingleAccount(Date dateTo, String account){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> allTransaction = getTransactionsUntil(dateTo);

        // cerco i conti con transazioni in accountName nel range todo fix this
        RealmResults<Movimento> accountTransaction = allTransaction.where().equalTo("conto", account).findAll();


        mRealm.close();
        return accountTransaction;

    }

    public RealmResults<Movimento> getTransactionsAll(){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> transactionInDay = mRealm.where(Movimento.class).findAll();
        mRealm.close();
        return transactionInDay;

    }

    public BigDecimal getAccountBalance(String account){
        mRealm = Realm.getDefaultInstance();
        Conto conto = mRealm.where(Conto.class).equalTo("nomeConto", account).findFirst();
        mRealm.close();
        return conto.getSaldoConto();


    }
    public void updateBalance(String accountName, final BigDecimal newBalance){
        mRealm = Realm.getDefaultInstance();
        final Conto conto = mRealm.where(Conto.class).equalTo("nomeConto", accountName).findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                conto.setSaldoConto(newBalance);
            }
        });
    }


    public void saveMovimento(final String beneficiario, final BigDecimal importo, final Date scadenza, final String conto){


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

    // REMOVE WEAPON ON SWIPED
    void removeMovimento(@Nonnull final int index){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Movimento> resultWeapon = mRealm.where(Movimento.class).findAll();
                final Movimento mov = resultWeapon.get(index);
                mov.deleteFromRealm();
            }
        });
    }


    public void saveConto(final String nomeConto, final BigDecimal saldoConto){


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
