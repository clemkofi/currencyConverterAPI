package com.kofi.currencyconverterapi.service;

import java.util.List;
import java.util.Optional;

import com.kofi.currencyconverterapi.dao.CurrencyDao;
import com.kofi.currencyconverterapi.model.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private final CurrencyDao currencyDao;

    // use dependency injection to inject Dao interface into Service
    @Autowired
    public CurrencyService(@Qualifier("postgres") CurrencyDao currencyDao){
        this.currencyDao = currencyDao;
    }

    public int addCurrency(Currency currency){
        return currencyDao.insertCurrency(currency);
    }

    public List<Currency> getAllCurrencies(){
        return currencyDao.selectAllCurrencies();
    }

    public Optional<Currency> getOneCurrency(String currencyName){
        return currencyDao.selectOneCurrency(currencyName);
    }

    public int updateCurrency(Currency currency){
        return currencyDao.updateCurrencyByName(currency);
    }

    public int deleteCurrency(String currencyName){
        return currencyDao.deleteCurrencyByName(currencyName);
    }
}
