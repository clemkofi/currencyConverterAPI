package com.kofi.currencyconverterapi.model;

public class CurrencyResult {
    private final String convertCurrency;
    private final String convertSymbol;
    private final Double value;
    private final int mainCurrencyId;
    private final String mainCurrency;
    private final String mainCurrencySymbol;

    public CurrencyResult(String convertCurrency, String convertSymbol, Double value, int mainCurrencyId,
            String mainCurrency, String mainCurrencySymbol) {
        this.convertCurrency = convertCurrency;
        this.convertSymbol = convertSymbol;
        this.value = value;
        this.mainCurrencyId = mainCurrencyId;
        this.mainCurrency = mainCurrency;
        this.mainCurrencySymbol = mainCurrencySymbol;
    }

    public String getMainCurrencySymbol() {
        return mainCurrencySymbol;
    }

    public String getMainCurrency() {
        return mainCurrency;
    }

    public int getMainCurrencyId() {
        return mainCurrencyId;
    }

    public Double getValue() {
        return value;
    }

    public String getconvertSymbol() {
        return convertSymbol;
    }

    public String getConvertCurrency() {
        return convertCurrency;
    }

}
