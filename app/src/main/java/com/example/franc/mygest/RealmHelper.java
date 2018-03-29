package com.example.franc.mygest;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by franc on 11/03/2018.
 */

public class RealmHelper {

    Realm mRealm;

    public RealmResults<Movimento> getTransactionByDay(String day){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> transactionInDay = mRealm.where(Movimento.class).equalTo("textImporto", day).findAll();
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

        // cerco i conti con transazioni in textImporto nel range todo fix this

        Conto conto = mRealm.where(Conto.class).equalTo("nomeConto", accountName).findFirst();
        mRealm.close();
        return conto;


    }
    public ContoObj getAccountObjectByName(String accountName){
        mRealm = Realm.getDefaultInstance();

        // cerco i conti con transazioni in textImporto nel range todo fix this

        Conto conto = mRealm.where(Conto.class).equalTo("nomeConto", accountName).findFirst();
        ContoObj contoBj = new ContoObj(conto.getNomeConto(), conto.getSaldoConto().toString(), conto.getColoreConto());
        mRealm.close();
        return contoBj;


    }

    /**
     * @param dateTo Transactions max due date
     * @return an array of affected by transactions account
     */
    public ArrayList<ContoObj> getTransactionsUntilGroupedByAccount(Date dateTo){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> allTransaction = getTransactionsUntil(dateTo);

        // cerco i conti con transazioni in textImporto nel range todo fix this
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

        // cerco i conti con transazioni in textImporto nel range todo fix this
        RealmResults<Movimento> accountTransaction = allTransaction.where().equalTo("conto", account).findAllSorted("scadenza", Sort.ASCENDING);


        mRealm.close();
        return accountTransaction;

    }

    public RealmResults<Movimento> getTransactionsAll(){

        mRealm = Realm.getDefaultInstance();
        RealmResults<Movimento> transactionInDay = mRealm.where(Movimento.class).findAllSorted("scadenza", Sort.ASCENDING);
        mRealm.close();
        return transactionInDay;

    }

    public BigDecimal getAccountBalance(String account){
        mRealm = Realm.getDefaultInstance();
        Conto conto = mRealm.where(Conto.class).equalTo("nomeConto", account).findFirst();
        mRealm.close();
        return conto.getSaldoConto();


    }
    void updateBalance(String accountName, final BigDecimal newBalance){
        mRealm = Realm.getDefaultInstance();
        final Conto conto = mRealm.where(Conto.class).equalTo("nomeConto", accountName).findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                conto.setSaldoConto(newBalance);
            }
        });
    }


    void saveMovimento(final String beneficiario, final BigDecimal importo, final Date scadenza, final String conto, @Nullable final Date endDate, @Nullable final String recurrence){

        Calendar cal = Calendar.getInstance();
        cal.setTime(scadenza);

        if(recurrence.equalsIgnoreCase("NESSUNA")){
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
/*
                    if(realm.where(DailyTransaction.class).equalTo("dayOfYear", movimento.getScadenza()).findAll() == null ){
                        DailyTransaction dailyTransaction = new DailyTransaction();
                        dailyTransaction.setDayOfYear(movimento.getScadenza());
                        dailyTransaction.setTimestamp(System.currentTimeMillis());
                        realm.copyToRealmOrUpdate(dailyTransaction);

                    }
*/

                }
            });

        }
        else if(recurrence.equalsIgnoreCase("DAILY")){
            while (cal.getTime().before(endDate)){
                final Movimento movimento = new Movimento();
                movimento.setBeneficiario(beneficiario);
                movimento.setImporto(importo);
                movimento.setScadenza(cal.getTime());
                movimento.setConto(conto);
                movimento.setTimestamp(System.currentTimeMillis());
                mRealm = Realm.getDefaultInstance();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(movimento);
/*
                        if(realm.where(DailyTransaction.class).equalTo("dayOfYear", movimento.getScadenza()).findAll() == null ){
                            DailyTransaction dailyTransaction = new DailyTransaction();
                            dailyTransaction.setDayOfYear(movimento.getScadenza());
                            dailyTransaction.setTimestamp(System.currentTimeMillis());
                            realm.copyToRealmOrUpdate(dailyTransaction);
                        }
*/
                    }
                });
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else if(recurrence.equalsIgnoreCase("WEEKLY")){
            while (cal.getTime().before(endDate)){
                final Movimento movimento = new Movimento();
                movimento.setBeneficiario(beneficiario);
                movimento.setImporto(importo);
                movimento.setScadenza(cal.getTime());
                movimento.setConto(conto);
                movimento.setTimestamp(System.currentTimeMillis());
                mRealm = Realm.getDefaultInstance();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(movimento);
/*
                        if(realm.where(DailyTransaction.class).equalTo("dayOfYear", movimento.getScadenza()).findAll() == null ){
                            DailyTransaction dailyTransaction = new DailyTransaction();
                            dailyTransaction.setDayOfYear(movimento.getScadenza());
                            dailyTransaction.setTimestamp(System.currentTimeMillis());
                            realm.copyToRealmOrUpdate(dailyTransaction);
                        }
*/
                    }
                });
                cal.add(Calendar.WEEK_OF_YEAR, 1);
            }
        }
        else if(recurrence.equalsIgnoreCase("MONTHLY")){
            while (cal.getTime().before(endDate)){
                final Movimento movimento = new Movimento();
                movimento.setBeneficiario(beneficiario);
                movimento.setImporto(importo);
                movimento.setScadenza(cal.getTime());
                movimento.setConto(conto);
                movimento.setTimestamp(System.currentTimeMillis());
                mRealm = Realm.getDefaultInstance();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(movimento);
/*
                        if(realm.where(DailyTransaction.class).equalTo("dayOfYear", movimento.getScadenza()).findAll() == null ){
                            DailyTransaction dailyTransaction = new DailyTransaction();
                            dailyTransaction.setDayOfYear(movimento.getScadenza());
                            dailyTransaction.setTimestamp(System.currentTimeMillis());
                            realm.copyToRealmOrUpdate(dailyTransaction);
                        }
*/
                    }
                });
                cal.add(Calendar.MONTH, 1);
            }
        }
        else if(recurrence.equalsIgnoreCase("YEARLY")){
            while (cal.getTime().before(endDate)){
                final Movimento movimento = new Movimento();
                movimento.setBeneficiario(beneficiario);
                movimento.setImporto(importo);
                movimento.setScadenza(cal.getTime());
                movimento.setConto(conto);
                movimento.setTimestamp(System.currentTimeMillis());
                mRealm = Realm.getDefaultInstance();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(movimento);
/*
                        if(realm.where(DailyTransaction.class).equalTo("dayOfYear", movimento.getScadenza()).findAll() == null ){
                            DailyTransaction dailyTransaction = new DailyTransaction();
                            dailyTransaction.setDayOfYear(movimento.getScadenza());
                            dailyTransaction.setTimestamp(System.currentTimeMillis());
                            realm.copyToRealmOrUpdate(dailyTransaction);
                        }
*/
                    }
                });
                cal.add(Calendar.YEAR, 1);
            }
        }


    }

    // REMOVE TRNSACTION ON SWIPED
    void removeMovimento(@Nonnull final long timestamp){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Movimento mov = realm.where(Movimento.class).equalTo("timestamp", timestamp).findFirst();
                mov.deleteFromRealm();
            }
        });
    }


    void saveConto(final String nomeConto, final BigDecimal saldoConto){


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
