package com.example.franc.mygest.persistence;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.util.Date;
import java.util.List;

/**
 * Created by franc on 23/04/2018.
 */

public class ContoViewModel extends AndroidViewModel {

    private ContoRepo mRepository;
    private MutableLiveData<Date> mDate = new MutableLiveData<>();
    private LiveData<List<EntityConto>> mActiveAccounts;
    private LiveData<List<EntityConto>> mAllAccounts;

    public ContoViewModel(Application application) {
        super(application);
        mRepository = new ContoRepo(application);
        mActiveAccounts = Transformations.switchMap(mDate, data -> mRepository.getAllAccountsDist(data));
        mAllAccounts = mRepository.getAllAccounts();
    }
    public void setDate(Date date){
        this.mDate.setValue(date); ;
    }

    public LiveData<List<EntityConto>> getAllAccounts() { return mAllAccounts; }

    public List<EntityConto> getAllAccountsList() {
        return mRepository.getAllAccountsNames();
    }

    public LiveData<List<EntityConto>> getAllAccoutsByName() { return mActiveAccounts; }

    public void insert(EntityConto conto) { mRepository.insert(conto); }

    public void update(EntityConto conto) { mRepository.update(conto); }

}
