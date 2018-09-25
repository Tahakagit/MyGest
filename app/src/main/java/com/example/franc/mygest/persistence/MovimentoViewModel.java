package com.example.franc.mygest.persistence;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by franc on 23/04/2018.
 */

public class MovimentoViewModel extends AndroidViewModel {

    private MovimentoRepo mRepository;
    private MutableLiveData<String> data = new MutableLiveData<>();
    private MutableLiveData<String> checked = new MutableLiveData<>();
    private MutableLiveData<String> beneficiario = new MutableLiveData<>();
    private MutableLiveData<String> account = new MutableLiveData<>();

    /*
    String checked;
    String beneficiario;
*/
    CustomLiveData2 trigger2 = new CustomLiveData2(account, checked, beneficiario);
    CustomLiveData3 trigger3 = new CustomLiveData3(account, checked, beneficiario, data);


    private LiveData<List<EntityMovimento>> mAllMovimento;
    private LiveData<List<EntityMovimento>> mActiveDates;
    private LiveData<List<EntityMovimento>> mActiveTransactions;

    public MovimentoViewModel(Application application) {
        super(application);
        mRepository = new MovimentoRepo(application);
        mAllMovimento = mRepository.getAllMovimento();
        mActiveDates = Transformations.switchMap(trigger2, values -> mRepository.getAllDates(values.get(0), values.get(1), values.get(2)));
        mActiveTransactions = Transformations.switchMap(trigger3, values -> mRepository.getTransactionInDay(values.get(0), values.get(1), values.get(2), values.get(3)));

    }

    class CustomLiveData2 extends MediatorLiveData<List<String>> {


        public CustomLiveData2(MutableLiveData<String> account, MutableLiveData<String> checked, MutableLiveData<String> beneficiario) {

            addSource(checked, new Observer<String>() {
                public void onChanged(@Nullable String a) {
                    List<String> list = new ArrayList<>();

                    list.add(account.getValue());
                    list.add(a);
                    list.add(beneficiario.getValue());
                    setValue(list);
                }
            });
            addSource(beneficiario, new Observer<String>() {
                public void onChanged(@Nullable String b) {
                    List<String> list = new ArrayList<>();

                    list.add(account.getValue());
                    list.add(checked.getValue());
                    list.add(b);
                    setValue(list);
                }
            });
            addSource(account, new Observer<String>() {
                public void onChanged(@Nullable String c) {
                    List<String> list = new ArrayList<>();

                    list.add(c);
                    list.add(checked.getValue());
                    list.add(beneficiario.getValue());
                    setValue(list);
                }
            });


        }
    }

    class CustomLiveData3 extends MediatorLiveData<List<String>> {


        public CustomLiveData3(MutableLiveData<String> account, MutableLiveData<String> checked, MutableLiveData<String> beneficiario, MutableLiveData<String> data) {

            addSource(account, new Observer<String>() {
                public void onChanged(@Nullable String c) {
                    List<String> list = new ArrayList<>();

                    list.add(c);
                    list.add(checked.getValue());
                    list.add(beneficiario.getValue());
                    list.add(data.getValue());
                    setValue(list);
                }
            });
            addSource(checked, new Observer<String>() {
                public void onChanged(@Nullable String a) {
                    List<String> list = new ArrayList<>();

                    list.add(account.getValue());
                    list.add(a);
                    list.add(beneficiario.getValue());
                    list.add(data.getValue());

                    setValue(list);
                }
            });
            addSource(beneficiario, new Observer<String>() {
                public void onChanged(@Nullable String b) {
                    List<String> list = new ArrayList<>();

                    list.add(account.getValue());
                    list.add(checked.getValue());
                    list.add(b);
                    list.add(data.getValue());

                    setValue(list);
                }
            });
            addSource(data, new Observer<String>() {
                public void onChanged(@Nullable String d) {
                    List<String> list = new ArrayList<>();

                    list.add(account.getValue());
                    list.add(checked.getValue());
                    list.add(beneficiario.getValue());
                    list.add(d);
                    setValue(list);
                }
            });


        }
    }


    public void setChecked(String checked){
        this.checked.setValue(checked);
    }

    public void viewChecked(){
        this.checked.setValue("checked");
    }

    public void viewUnchecked(){
        this.checked.setValue("unchecked");
    }

    public void filterBeneficiario(String beneficiario){
        this.beneficiario.setValue(beneficiario);
    }

    public void filterConto(String conto){
        this.account.setValue(conto);
    }

    public void filterDate(String data){
        this.data.setValue(data);
    }

    public void deleteTransactionById(int id){
        mRepository.deleteTransactionById(id);
    }

    public void checkTransaction(int id){
        mRepository.checkTransaction(id);
    }

    public LiveData<List<EntityMovimento>> getAllWords() { return mAllMovimento; }

    public String getAllTransactionAmount(int accountId, java.util.Date upTo){
        List<EntityMovimento> allMovs = mRepository.getAllMovimentoUpToByAccount(upTo, accountId);
        BigDecimal amount = new BigDecimal(0);

        for (EntityMovimento res: allMovs) {
            amount = amount.add(new BigDecimal(String.valueOf(res.getImporto())));
        }
        return amount.toString();
    }

    public int getTotalTransaction(int accountId, java.util.Date upTo){
        return mRepository.getTotMovimentoUpToByAccount(upTo, accountId);
    }


    public List<EntityMovimento> getAllMovimentoDistByAccount(java.util.Date upTo, int account) { return mRepository.getAllMovimentoUpToByAccount(upTo, account); }

    public LiveData<List<EntityMovimento>> getDailyTransactionsByAccount(java.util.Date upTo, int account) { return mRepository.getDailyTransactionsByAccount(upTo, account); }

    public LiveData<List<EntityMovimento>> getDailyTransactions(java.util.Date upTo) { return mRepository.getDailyTransactions(upTo); }
    public List<String> getKnownBeneficiari(){
        return mRepository.getKnownBeneficiari();
    }
/*
    public LiveData<List<EntityMovimento>> getDailyTransactionsChecked() { return mActiveTransactions; }
*/
    public LiveData<List<EntityMovimento>> getDailyTransactionsChecked() {

        return mActiveTransactions;
/*
        return mActiveTransactions;
*/
    }

    public LiveData<List<EntityMovimento>> getAllInDayFiltered(java.util.Date upTo, int account, String checked, String beneficiario) {
        return mRepository.getAllInDayFiltered(upTo, account, checked, beneficiario);
    }

    public LiveData<List<EntityMovimento>> getAllDatesByAccount(java.util.Date upTo, int account) { return mRepository.getAllDatesByAccount(upTo, account); }

    public LiveData<List<EntityMovimento>> getAllDates() { return mActiveDates; }

    public LiveData<List<EntityMovimento>> getAllTransactionsDates(java.util.Date upTo, int account, String checked, String beneficiario) {
        return mRepository.getAllInDayFiltered(upTo, account, checked, beneficiario); }


    public void insert(String beneficiario, String importo, java.util.Date scadenza, java.util.Date saldato, String nomeConto, int idConto, @Nullable final java.util.Date endDate, String recurrence, String tipo) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(scadenza);

        if(recurrence.equalsIgnoreCase("NESSUNA")){
            EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo);

            mRepository.insert(mov);
        }else if(recurrence.equalsIgnoreCase("DAILY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo);
                mRepository.insert(mov);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }else if(recurrence.equalsIgnoreCase("WEEKLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo);
                mRepository.insert(mov);
                cal.add(Calendar.WEEK_OF_YEAR, 1);
            }
        }else if(recurrence.equalsIgnoreCase("MONTHLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo);
                mRepository.insert(mov);
                cal.add(Calendar.MONTH, 1);
            }
        }else if(recurrence.equalsIgnoreCase("YEARLY")){
            while (cal.getTime().before(endDate)){
                EntityMovimento mov = new EntityMovimento(beneficiario, importo, cal.getTime(), saldato, idConto, nomeConto, endDate, tipo);
                mRepository.insert(mov);
                cal.add(Calendar.YEAR, 1);
            }
        }



/*
        mRepository.insert(movimento);
*/
    }
}
