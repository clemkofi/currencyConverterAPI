package com.kofi.currencyconverterapi.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kofi.currencyconverterapi.model.Currency;

import org.springframework.stereotype.Repository;

@Repository("FakeDao") // annotation showing that class is a bean
public class FakeCurrencyDataAccessDao implements CurrencyDao {

    // temporary list to serve as DB
    private static List<Currency> DB = new ArrayList<>();

    @Override
    public int insertCurrency(Currency currency) {
        DB.add(currency);
        return 1;
    }

    @Override
    public List<Currency> selectAllCurrencies() {
        return DB;
    }

    @Override
    public Optional<Currency> selectOneCurrency(String currencyName) {
        return DB.stream().filter(currency -> currency.getName().equals(currencyName)).findFirst();
    }

    @Override
    public int updateCurrencyByName(Currency currency) {
        // check to find if the currency exists in DB
        return selectOneCurrency(currency.getName()).map(c -> {
            int indexOfCurrencyToUpdate = DB.indexOf(c);
            if(indexOfCurrencyToUpdate >= 0){
                // DB.set(indexOfCurrencyToUpdate, new Currency(c.getId(), currency.getName(), currency.getSymbol(), currency.getValues()));
                DB.set(indexOfCurrencyToUpdate, new Currency(currency.getName(), currency.getSymbol(), currency.getValues()));
                return 1;
            }
            return 0;
        }).orElse(0);
    }

    @Override
    public int deleteCurrencyByName(String currencyName) {
        Optional<Currency> selectCurrencyMaybe = selectOneCurrency(currencyName);
        if(selectCurrencyMaybe.isEmpty()){
            return 0;
        }
        DB.remove(selectCurrencyMaybe.get());
        return 1;
    }

    


    
}
