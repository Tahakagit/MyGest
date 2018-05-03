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

    LiveData<List<String>> getAllMovimentoUpTo(java.util.Date upTo) {
        return mMovimentoDao.getTransactionUpTo(upTo);
    }

    LiveData<List<EntityMovimento>> getAllMovimentoUpToByAccount(java.util.Date upTo, String account) {
        return mMovimentoDao.getTransactionUpToByAccount(upTo, account);
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
