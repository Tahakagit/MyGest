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
        this.mDate.setValue(date);
    }

    /**
     *
     * @return all account in AccountsManageActivity
     */
    public LiveData<List<EntityConto>> getAllAccounts() { return mAllAccounts; }

    /**
     *
     * @return account with expected transactions in MainActivity
     */
    public LiveData<List<EntityConto>> getActiveAccounts() { return mActiveAccounts; }


    public EntityConto getAccountIdByName(String accountName) { return mRepository.getAccountIdByName(accountName); }

    public List<EntityConto> getAllAccountsList() {
        return mRepository.getAllAccountsNames();
    }

    public List<EntityConto> getActiveAccountsSync(Date date) { return mRepository.getAllAccountsSync(date); }

    public void insert(EntityConto conto) { mRepository.insert(conto); }

    public void update(EntityConto conto) { mRepository.update(conto); }
    public void delete(EntityConto conto) { mRepository.delete(conto); }


}
