package com.example.franc.mygest.persistence;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by franc on 23/04/2018.
 */

public class DateViewModel extends AndroidViewModel {

    private MovimentoRepo mRepository;
    private MutableLiveData<String> checked = new MutableLiveData<>();
    private MutableLiveData<String> beneficiario = new MutableLiveData<>();
/*
    private MutableLiveData<String> data2 = new MutableLiveData<>();
    private MutableLiveData<String> checked2 = new MutableLiveData<>();
    private MutableLiveData<String> beneficiario2 = new MutableLiveData<>();
*/

    /*
    String checked;
    String beneficiario;
*/
    CustomLiveData trigger = new CustomLiveData(checked, beneficiario);
/*
    CustomLiveData2 trigger2 = new CustomLiveData2(data2, checked2, beneficiario2);
*/


    private LiveData<List<EntityMovimento>> mAllMovimento;
    private LiveData<List<EntityMovimento>> mActiveDates;

    public DateViewModel(Application application) {
        super(application);
        mRepository = new MovimentoRepo(application);
        mAllMovimento = mRepository.getAllMovimento();
/*
        mActiveDates = Transformations.switchMap(trigger, value -> mRepository.getAllDates(value.value.second, value.first));
*/
    }

    class CustomLiveData extends MediatorLiveData<Pair<String, String>> {
        public CustomLiveData(MutableLiveData<String> checked, MutableLiveData<String> beneficiario) {
            addSource(checked, new Observer<String>() {
                public void onChanged(@Nullable String first) {
                    setValue(Pair.create(beneficiario.getValue(), first));
                }
            });
            addSource(beneficiario, new Observer<String>() {
                public void onChanged(@Nullable String second) {
                    setValue(Pair.create(second, checked.getValue()));
                }
            });

        }
    }
//todo convertire data to string e vice versa
    public void viewChecked(){
        this.checked.setValue("checked");
    }

    public void viewUnchecked(){
        this.checked.setValue("unchecked");
    }

    public void filterBeneficiario(String beneficiario){
        this.beneficiario.setValue(beneficiario);
    }

    public void filterDate(String data){
    }

    public void deleteTransactionById(int id){
        mRepository.deleteTransactionById(id);
    }

    public void checkTransaction(int id){
        mRepository.checkTransaction(id);
    }

    public LiveData<List<EntityMovimento>> getAllWords() { return mAllMovimento; }

/*
    public String getAllTransactionAmount(int accountId, java.util.Date upTo){
        List<EntityMovimento> allMovs = mRepository.getTransactionsUpToByAccount(upTo, accountId, );
        BigDecimal amount = new BigDecimal(0);

        for (EntityMovimento res: allMovs) {
            amount = amount.add(new BigDecimal(String.valueOf(res.getImporto())));
        }
        return amount.toString();
    }
*/

/*
    public List<EntityMovimento> getAllMovimentoDistByAccount(java.util.Date upTo, int account) { return mRepository.getTransactionsUpToByAccount(upTo, account, ); }
*/

    public LiveData<List<EntityMovimento>> getDailyTransactionsByAccount(java.util.Date upTo, int account) { return mRepository.getDailyTransactionsByAccount(upTo, account); }

    public LiveData<List<EntityMovimento>> getDailyTransactions(java.util.Date upTo) { return mRepository.getDailyTransactions(upTo); }

    /*
        public LiveData<List<EntityMovimento>> getDailyTransactionsChecked() { return mActiveTransactions; }
    */
    public LiveData<List<EntityMovimento>> getDailyTransactionsChecked(java.util.Date date, String checked, String beneficiario) {

        return mRepository.getDailyTransactionsChecked(date, checked, beneficiario);
    }


    public LiveData<List<EntityMovimento>> getAllDatesByAccount(java.util.Date upTo, int account) { return mRepository.getAllDatesByAccount(upTo, account); }

    public LiveData<List<EntityMovimento>> getAllDates() { return mActiveDates; }

    public void insert(String beneficiario, String importo, Date scadenza, Date saldato, String nomeConto, int idConto, @Nullable final Date endDate, String recurrence, String tipo, String direction) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(scadenza);

        if(recurrence.equalsIgnoreCase("NESSUNA")){
            EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo, direction);

            mRepository.insert(mov);
        }else if(recurrence.equalsIgnoreCase("DAILY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo, direction);
                mRepository.insert(mov);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }else if(recurrence.equalsIgnoreCase("WEEKLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo, direction);
                mRepository.insert(mov);
                cal.add(Calendar.WEEK_OF_YEAR, 1);
            }
        }else if(recurrence.equalsIgnoreCase("MONTHLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo, direction);
                mRepository.insert(mov);
                cal.add(Calendar.MONTH, 1);
            }
        }else if(recurrence.equalsIgnoreCase("YEARLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo, direction);
                mRepository.insert(mov);
                cal.add(Calendar.YEAR, 1);
            }
        }



/*
        mRepository.insert(movimento);
*/
    }
}
