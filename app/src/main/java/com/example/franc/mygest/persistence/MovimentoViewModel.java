package com.example.franc.mygest.persistence;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;
import android.widget.Switch;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;


/**
 * Created by franc on 23/04/2018.
 */

public class MovimentoViewModel extends AndroidViewModel {

    private MovimentoRepo mRepository;
    private MutableLiveData<String> checked = new MutableLiveData<>();

    private LiveData<List<EntityMovimento>> mAllMovimento;
    private LiveData<List<EntityMovimento>> mActiveDates;

    public MovimentoViewModel(Application application) {
        super(application);
        mRepository = new MovimentoRepo(application);
        mAllMovimento = mRepository.getAllMovimento();
        mActiveDates = Transformations.switchMap(checked, check -> mRepository.getAllDates(check));
    }

/*
    public void switchChecked(){
        if (this.checked.equals("checked")){
            this.checked.setValue("unchecked");
        }else {
            this.checked.setValue("checked");
        }
    }
*/

    public void viewChecked(){
        this.checked.setValue("checked");
    }

    public void viewUnchecked(){
        this.checked.setValue("unchecked");
    }
    public void deleteTransactionById(int id){
        mRepository.deleteTransactionById(id);
    }

    public void checkTransaction(int id){
        mRepository.checkTransaction(id);
    }


    public LiveData<List<EntityMovimento>> getAllWords() { return mAllMovimento; }



/*
    public LiveData<List<EntityMovimento>> getAllMovimentoChecked() {
        return mActiveTransaction; }
*/

    public String getAllTransactionAmount(int accountId, java.util.Date upTo){
        List<EntityMovimento> allMovs = mRepository.getAllMovimentoUpToByAccount(upTo, accountId);
        BigDecimal amount = new BigDecimal(0);

        for (EntityMovimento res: allMovs) {
            amount = amount.add(new BigDecimal(String.valueOf(res.getImporto())));
        }
        return amount.toString();
    }

    public List<EntityMovimento> getAllMovimentoDistByAccount(java.util.Date upTo, int account) { return mRepository.getAllMovimentoUpToByAccount(upTo, account); }


    public LiveData<List<EntityMovimento>> getDailyTransactionsByAccount(java.util.Date upTo, int account) { return mRepository.getDailyTransactionsByAccount(upTo, account); }


    public LiveData<List<EntityMovimento>> getDailyTransactions(java.util.Date upTo) { return mRepository.getDailyTransactions(upTo); }

    public LiveData<List<EntityMovimento>> getDailyTransactionsChecked(java.util.Date upTo, String checked) { return mRepository.getDailyTransactionsChecked(upTo, checked); }


    public LiveData<List<EntityMovimento>> getAllDatesByAccount(java.util.Date upTo, int account) { return mRepository.getAllDatesByAccount(upTo, account); }

    public LiveData<List<EntityMovimento>> getAllDates() { return mActiveDates; }

    public void insert(String beneficiario, String importo, java.util.Date scadenza, String nomeConto, int idConto, @Nullable final java.util.Date endDate, String recurrence, String tipo) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(scadenza);

        if(recurrence.equalsIgnoreCase("NESSUNA")){
            EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), idConto, nomeConto, endDate, tipo);

            mRepository.insert(mov);
        }else if(recurrence.equalsIgnoreCase("DAILY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), idConto, nomeConto, endDate, tipo);
                mRepository.insert(mov);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }else if(recurrence.equalsIgnoreCase("WEEKLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), idConto, nomeConto, endDate, tipo);
                mRepository.insert(mov);
                cal.add(Calendar.WEEK_OF_YEAR, 1);
            }
        }else if(recurrence.equalsIgnoreCase("MONTHLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), idConto, nomeConto, endDate, tipo);
                mRepository.insert(mov);
                cal.add(Calendar.MONTH, 1);
            }
        }else if(recurrence.equalsIgnoreCase("YEARLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), idConto, nomeConto, endDate, tipo);
                mRepository.insert(mov);
                cal.add(Calendar.YEAR, 1);
            }
        }



/*
        mRepository.insert(movimento);
*/
    }
}