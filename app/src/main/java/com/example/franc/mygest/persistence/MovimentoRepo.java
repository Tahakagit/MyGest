package com.example.franc.mygest.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.sql.Date;
import java.util.List;

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

/*
    LiveData<List<String>> getAllMovimentoUpTo(java.util.Date upTo) {
        return mMovimentoDao.getTransactionUpTo(upTo);
    }
*/

    List<EntityMovimento> getAllMovimentoUpToByAccount(java.util.Date upTo, int account) {
        return mMovimentoDao.getTransactionUpToByAccount(upTo, account);
    }

    LiveData<List<EntityMovimento>> getDailyTransactionsByAccount(java.util.Date upTo, int account) {
        return mMovimentoDao.getDailyTransactionsByAccount(upTo, account);
    }


    LiveData<List<EntityMovimento>> getAllDates(java.util.Date upTo, int account) {
        return mMovimentoDao.getAllDates(upTo, account);
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
