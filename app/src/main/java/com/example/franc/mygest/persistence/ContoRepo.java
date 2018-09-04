package com.example.franc.mygest.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.TypeConverters;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

/**
 * Created by franc on 23/04/2018.
 */

public class ContoRepo {

    private ContoDao mContoDao;
    private MovimentoDao mMovimentoDao;
    private LiveData<List<EntityConto>> mAllAccounts;
    private List<String> mAllAccountName;
    private LiveData<List<EntityConto>> mAllAccountsDist;
    private List<EntityConto> mAllAccountsDistSync;

    ContoRepo(Application application) {
        MovimentoRoomDatabase db = MovimentoRoomDatabase.getDatabase(application);
        mContoDao = db.contoDao();
        mAllAccounts = mContoDao.getAllAccounts();
/*
        mAllAccountName = mContoDao.getAllAccountsName();
*/
    }

    LiveData<List<EntityConto>> getAllAccounts() {
        return mAllAccounts;
    }


    EntityConto getAccountIdByName(String accountName) {
        return mContoDao.getAccountIdByName(accountName);
    }

    List<EntityConto> getAllAccountsNames() {
        return mContoDao.getAllAccountsName();
    }

    @TypeConverters(DateConverter.class)
    LiveData<List<EntityConto>> getAllAccountsDist(Date upTo) {
/*
        distAccounts = mMovimentoDao.getTransactionUpTo(upTo);
*/
        mAllAccountsDist = mContoDao.getAllAccountsDist(upTo);

        return mAllAccountsDist;
    }
    @TypeConverters(DateConverter.class)
    List<EntityConto> getAllAccountsSync(Date upTo) {
/*
        distAccounts = mMovimentoDao.getTransactionUpTo(upTo);
*/
        mAllAccountsDistSync = mContoDao.getAllAccountsDistAsync(upTo);

        return mAllAccountsDistSync;
    }

    public void insert (EntityConto conto) {
        new insertAsyncTask(mContoDao).execute(conto);
    }

    public void update (EntityConto conto) {
        new updateAsyncTask(mContoDao).execute(conto);
    }

    public void delete (EntityConto conto) {
        new deleteAsyncTask(mContoDao).execute(conto);
    }


    private static class insertAsyncTask extends AsyncTask<EntityConto, Void, Void> {

        private ContoDao mAsyncTaskDao;

        insertAsyncTask(ContoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EntityConto... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<EntityConto, Void, Void> {

        private ContoDao mAsyncTaskDao;

        updateAsyncTask(ContoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EntityConto... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<EntityConto, Void, Void> {

        private ContoDao mAsyncTaskDao;

        deleteAsyncTask(ContoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EntityConto... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
