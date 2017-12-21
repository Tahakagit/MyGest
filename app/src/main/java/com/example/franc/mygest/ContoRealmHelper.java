package com.example.franc.mygest;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by franc on 20/12/2017.
 */

public class ContoRealmHelper {
    Realm realm;
    public ContoRealmHelper(Realm realm) {
        this.realm = realm;
    }
    //WRITE
    public void save(final Conto conto)
    {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Conto s=realm.copyToRealm(conto);
            }
        });
    }
    //READ
    public ArrayList<String> retrieve()
    {
        ArrayList<String> nomiConto=new ArrayList<>();
        RealmResults<Conto> conti= realm.where(Conto.class).findAll();
        for(Conto s:conti)
        {
            nomiConto.add(s.getNomeConto());
        }
        return nomiConto;
    }

}
