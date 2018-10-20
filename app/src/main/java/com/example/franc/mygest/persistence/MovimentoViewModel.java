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
import java.util.Date;
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
    private MutableLiveData<String> direction = new MutableLiveData<>();

    //todo modificare direction
    CustomLiveData2 trigger2 = new CustomLiveData2(account, checked, beneficiario);


    private LiveData<List<EntityMovimento>> mActiveDates;

    public MovimentoViewModel(Application application) {
        super(application);
        mRepository = new MovimentoRepo(application);
        mActiveDates = Transformations.switchMap(trigger2, values -> mRepository.getAllDates(values.get(0), values.get(1), values.get(2)));

    }

    class CustomLiveData2 extends MediatorLiveData<List<String>> {

        //todo modificare costruttore  direction
        CustomLiveData2(MutableLiveData<String> account, MutableLiveData<String> checked, MutableLiveData<String> beneficiario) {

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
    public void filterDirezione(String direzione){
        this.direction.setValue(direzione);
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

    /**
     *
     * @param accountId account
     * @param upTo up to this date
     * @param direction IN, OUT
     * @return total amount at selected time for account
     */
    public String getTransactionsAmount(int accountId, Date upTo, String direction){
        List<EntityMovimento> allMovs = mRepository.getTransactionsUpToByAccount(upTo, accountId, direction);
        BigDecimal amount = new BigDecimal(0);

        for (EntityMovimento res: allMovs) {
            amount = amount.add(new BigDecimal(String.valueOf(res.getImporto())));
        }
        return amount.toString();
    }


    public int getTotalTransaction(int accountId, java.util.Date upTo){
        return mRepository.getTotMovimentoUpToByAccount(upTo, accountId);
    }


    public LiveData<List<EntityMovimento>> getDailyTransactionsByAccount(java.util.Date upTo, int account) { return mRepository.getDailyTransactionsByAccount(upTo, account); }

    public List<String> getKnownBeneficiari(){
        return mRepository.getKnownBeneficiari();
    }

    public LiveData<List<EntityMovimento>> getAllDatesByAccount(java.util.Date upTo, int account) { return mRepository.getAllDatesByAccount(upTo, account); }

    /**
     *
     * @return all days with transactions in AllTransactionActivity
     */
    public LiveData<List<EntityMovimento>> getAllDates() { return mActiveDates; }

    public LiveData<List<EntityMovimento>> getAllTransactionsDates(String upTo, int account, String checked, String beneficiario) {
        return mRepository.getTransactionInDay(String.valueOf(account), checked, beneficiario, upTo);}


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
