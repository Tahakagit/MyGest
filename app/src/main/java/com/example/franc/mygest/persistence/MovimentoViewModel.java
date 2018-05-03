package com.example.franc.mygest.persistence;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.sql.Date;
import java.util.List;

/**
 * Created by franc on 23/04/2018.
 */

public class MovimentoViewModel extends AndroidViewModel {

    private MovimentoRepo mRepository;

    private LiveData<List<EntityMovimento>> mAllMovimento;

    public MovimentoViewModel(Application application) {
        super(application);
        mRepository = new MovimentoRepo(application);
        mAllMovimento = mRepository.getAllMovimento();
    }

    public void deleteTransactionById(int id){
        mRepository.deleteTransactionById(id);
    }
    public LiveData<List<EntityMovimento>> getAllWords() { return mAllMovimento; }
/*
    public LiveData<List<String>> getAllMovimentoDist(java.util.Date upTo) { return mRepository.getAllMovimentoUpTo(upTo); }
*/

    public LiveData<List<EntityMovimento>> getAllMovimentoDistByAccount(java.util.Date upTo, String account) { return mRepository.getAllMovimentoUpToByAccount(upTo, account); }


    public void insert(EntityMovimento movimento) { mRepository.insert(movimento); }
}
