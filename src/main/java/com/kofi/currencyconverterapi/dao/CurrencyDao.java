package com.kofi.currencyconverterapi.dao;

import java.util.List;
import java.util.Optional;

import com.kofi.currencyconverterapi.model.Currency;

public interface CurrencyDao {

    // methods to be abstracted

    // method to insert a new currency
    int insertCurrency(Currency currency);

    // method to get all currencies
    List<Currency> selectAllCurrencies();

    // method to get one currency
    Optional<Currency> selectOneCurrency(String currencyName);

    // method to update a currency
    int updateCurrencyByName(Currency currency);

    // method to delete a currency
    int deleteCurrencyByName(String currencyName);
    
    
}
