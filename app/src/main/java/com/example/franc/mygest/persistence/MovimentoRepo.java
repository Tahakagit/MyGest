package com.example.franc.mygest.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by franc on 23/04/2018.
 */

public class MovimentoRepo {

    private MovimentoDao mMovimentoDao;
    private LiveData<List<EntityMovimento>> mAllMovimenti;

    MovimentoRepo(Application application) {
        MovimentoRoomDatabase db = MovimentoRoomDatabase.getDatabase(application);
        mMovimentoDao = db.movimentoDao();
        mAllMovimenti = mMovimentoDao.getAllTransactions();
    }

    LiveData<List<EntityMovimento>> getAllMovimento() {
        return mAllMovimenti;
    }

    LiveData<List<EntityMovimento>> getAllMovimentoChecked(String checked) {
        return mMovimentoDao.getAllTransactionsChecked(checked);
    }


    List<EntityMovimento> getAllMovimentoUpToByAccount(java.util.Date upTo, int account) {
        return mMovimentoDao.getTransactionUpToByAccount(upTo, account);
    }

    int getTotMovimentoUpToByAccount(java.util.Date upTo, int account) {
        return mMovimentoDao.getTotalTransactionsUpToByAccount(upTo, account);
    }

    LiveData<List<EntityMovimento>> getDailyTransactionsByAccount(java.util.Date upTo, int account) {
        return mMovimentoDao.getDailyTransactionsByAccount(upTo, account);
    }

    List<String> getKnownBeneficiari(){
        return mMovimentoDao.getKnownBeneficiari();
    }
    LiveData<List<EntityMovimento>> getDailyTransactions(java.util.Date upTo) {
        return mMovimentoDao.getDailyTransactions(upTo);
    }
    LiveData<List<EntityMovimento>> getDailyTransactionsChecked(java.util.Date upTo, String checked, String beneficiario) {
        return mMovimentoDao.getDailyTransactionsCheckedFilter(upTo, checked, beneficiario);
    }

    LiveData<List<EntityMovimento>> getAllDatesByAccount(java.util.Date upTo, int account) {
        return mMovimentoDao.getAllDatesByAccount(upTo, account);
    }


    LiveData<List<EntityMovimento>> getAllDates(String account, String checked, String beneficiario) {
        if((beneficiario==null||beneficiario.equalsIgnoreCase(""))&&(account== null||account.equalsIgnoreCase(""))){
            return mMovimentoDao.getAllDatesCheckedNoBeneNoAccountGroup(checked);
        }else if (account== null||account.equalsIgnoreCase("")){
            return mMovimentoDao.getAllDatesCheckedBeneNoACcount(checked, beneficiario);
        }else if (beneficiario==null||beneficiario.equalsIgnoreCase("")){
            return mMovimentoDao.getAllDatesCheckedNoBeneAccount(checked,Integer.valueOf(account));
        }else {

            return mMovimentoDao.getAllDatesCheckedBeneAccount(Integer.valueOf(account),checked,beneficiario);

        }
    }

    LiveData<List<EntityMovimento>> getTransactionInDay(String upTo, String checked, String beneficiario) {
        SimpleDateFormat format = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        java.util.Date date = new java.util.Date();

        try {
            date = format.parse(upTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(beneficiario==null||beneficiario.equalsIgnoreCase("")){
            return mMovimentoDao.getDailyTransactionsChecked(date, checked);
        }else {
            return mMovimentoDao.getDailyTransactionsCheckedFilter(date, checked, beneficiario);
        }

    }

    public void checkTransaction(int id){
        mMovimentoDao.checkTransaction(id);
    }


    public void deleteTransactionById(int id){
        mMovimentoDao.deleteTransactionById(id);
    }
    public void insert (EntityMovimento movimento) {
        new insertAsyncTask(mMovimentoDao).execute(movimento);
    }

    private static class insertAsyncTask extends AsyncTask<EntityMovimento, Void, Void> {

        private MovimentoDao mAsyncTaskDao;

        insertAsyncTask(MovimentoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EntityMovimento... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


}
