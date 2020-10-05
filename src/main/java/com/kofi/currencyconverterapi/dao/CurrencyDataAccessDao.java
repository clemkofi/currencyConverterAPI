package com.kofi.currencyconverterapi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kofi.currencyconverterapi.model.Currency;
import com.kofi.currencyconverterapi.model.CurrencyResult;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("postgres")
public class CurrencyDataAccessDao implements CurrencyDao {

    // instance of the jdbc driver for postgres
    private final JdbcTemplate jdbcTemplate;

    // inner class to handle do the row mapper work in jdbctemplate
    private static final class CurrencyValueMapper implements RowMapper<CurrencyResult> {

        public CurrencyResult mapRow(ResultSet rs, int rowNum) throws SQLException {
            String convertCurrency = rs.getString("convert_currency");
            String convertCurrencySymbol = rs.getString("convert_currency_symbol");
            Double value = rs.getDouble("value");
            int mainCurrencyId = rs.getInt("primary_currency");
            String mainCurrency = rs.getString("main_currency");
            String mainCurrencySymbol = rs.getString("main_currency_symbol");

            return new CurrencyResult(convertCurrency, convertCurrencySymbol, value, mainCurrencyId, mainCurrency,
                    mainCurrencySymbol);
        }

    }

    // inner class for all currencies in db
    private static final class CurrencyMapper implements RowMapper<Currency> {

        public Currency mapRow(ResultSet rs, int rowNum) throws SQLException {

            int currencyId = rs.getInt("id");
            String mainCurrency = rs.getString("name");
            String mainCurrencySymbol = rs.getString("symbol");
            Map<String, Double> values = new HashMap<>();

            // return new Currency(currencyId, mainCurrency, mainCurrencySymbol, values);
            Currency returningCurrency =  new Currency(mainCurrency, mainCurrencySymbol, values);
            returningCurrency.setId(currencyId);
            return returningCurrency;
        }

    }

    @Autowired
    public CurrencyDataAccessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertCurrency(Currency currency) {
        // sql to insert new currency into the db
        final String insertCurrencySql = "INSERT INTO currency(name, symbol) VALUES (?, ?)";

        jdbcTemplate.update(insertCurrencySql, currency.getName(), currency.getSymbol());

        // get all currencies from the db
        List<Currency> dbCurrencyResults = getCurrencyDetails();

        // Map of all the currencies with their ids
        Map<String, Integer> mapCurrencies = dbCurrencyResults.stream().collect(Collectors.toMap(Currency::getName, c -> c.getId()));

        // iterate and update each of the values for the currency
        for (String value : mapCurrencies.keySet()) {
            // sql to insert into the currency values tables
            if(!currency.getName().equals(value)){
                final String insertCurrencyValuesSql = "INSERT INTO currency_values(primary_currency, secondary_currency, value) "  
                                                        + "VALUES (?, ?, ?)";
                
                // jdbc used to execute update query
                jdbcTemplate.update(insertCurrencyValuesSql, mapCurrencies.get(currency.getName()), mapCurrencies.get(value), currency.getValues().get(value));
            }
            
        }

        return 1;
    }

    // inner methods 
    private List<Currency> getCurrencyDetails() {
        // get all currencies from the db
        final String currencySql = "SELECT * FROM currency";

        // jdbc is used to execute the query
        return jdbcTemplate.query(currencySql, new CurrencyMapper());
        
    }

    @Override
    public List<Currency> selectAllCurrencies() {
        
        // get all currencies from the db
        List<Currency> dbCurrencyResults = getCurrencyDetails();

        // getting all values for all the currencies in the db
        final String sql = "SELECT DISTINCT " + "id, " + "name as convert_currency, "
                + "symbol as convert_currency_symbol, " + "primary_currency, "
                + "(SELECT name FROM currency WHERE id = primary_currency) as main_currency, "
                + "(SELECT symbol FROM currency WHERE id = primary_currency) as main_currency_symbol, " + "value "
                + "FROM currency " + "JOIN currency_values " + "ON currency.id = currency_values.secondary_currency "
                + "ORDER BY primary_currency";

        // jdbc is used to execute the query
        List<CurrencyResult> dbValuesResults = jdbcTemplate.query(sql, new CurrencyValueMapper());

        // map tp group all the currency values by the currency id
        Map<String, Map<String, Double>> mapCurrencyValues = dbValuesResults.stream()
                .collect(Collectors.groupingBy(CurrencyResult::getMainCurrency,
                        Collectors.toMap(CurrencyResult::getConvertCurrency, c -> c.getValue())));

        // for loop to update the list of currencies obtained from the db with their
        // values from map
        for (Currency currency : dbCurrencyResults) {
            currency.setValues(mapCurrencyValues.get(currency.getName()));
        }

        // return all the and currencies
        return dbCurrencyResults;
    }

    @Override
    public Optional<Currency> selectOneCurrency(String currencyName) {
        // get all currencies from the db
        final String currencySql = "SELECT * FROM currency WHERE name = ?";

        // jdbc is used to execute the query
        Currency dbCurrencyResult = jdbcTemplate.queryForObject(currencySql, new CurrencyMapper(),
                new Object[] { currencyName });

        // check if result was returned from query or not
        if(dbCurrencyResult == null){
            return Optional.ofNullable(dbCurrencyResult); 
        }

        // getting all values for all the currencies in the db
        final String sql = "SELECT * FROM ("
                + "SELECT DISTINCT " + "id, " + "name as convert_currency, "
                + "symbol as convert_currency_symbol, " + "primary_currency, "
                + "(SELECT name FROM currency WHERE id = primary_currency) as main_currency, "
                + "(SELECT symbol FROM currency WHERE id = primary_currency) as main_currency_symbol, " + "value "
                + "FROM currency " + "JOIN currency_values " + "ON currency.id = currency_values.secondary_currency "
                + "ORDER BY primary_currency"
                + ") as currency_values "
                + "WHERE main_currency = ?";

        // jdbc is used to execute the query
        List<CurrencyResult> dbValuesResults = jdbcTemplate.query(sql, new Object[] { currencyName }, new CurrencyValueMapper());

        // map tp group all the currency values by the currency id
        Map<Integer, Map<String, Double>> mapCurrencyValues = dbValuesResults.stream()
                .collect(Collectors.groupingBy(CurrencyResult::getMainCurrencyId,
                        Collectors.toMap(CurrencyResult::getConvertCurrency, c -> c.getValue())));

        // set values for the selected currency
        dbCurrencyResult.setValues(mapCurrencyValues.get(dbCurrencyResult.getId()));

        return Optional.ofNullable(dbCurrencyResult); 

    }

    @Override
    public int updateCurrencyByName(Currency currency) {
        // check if the person exist in the DB
        Optional<Currency> selectCurrencyMaybe = selectOneCurrency(currency.getName());

        if (selectCurrencyMaybe.isEmpty()) {
            return 0;
        }

        // get all currencies from the db
        List<Currency> dbCurrencyResults = getCurrencyDetails();

        // Map of all the currencies with their ids
        Map<String, Integer> mapCurrencies = dbCurrencyResults.stream().collect(Collectors.toMap(Currency::getName, c -> c.getId()));

        // iterate and update each of the values for the currency
        for (String value : currency.getValues().keySet()) {
            // get all currencies from the db
            final String currencyUpdateSql = "UPDATE currency_values SET value = ? " 
                                            + "WHERE primary_currency = ? AND secondary_currency =?";

            // jdbc used to execute update query
            jdbcTemplate.update(currencyUpdateSql, currency.getValues().get(value), mapCurrencies.get(currency.getName()), mapCurrencies.get(value));
        }

        return 1;
    }

    @Override
    public int deleteCurrencyByName(String currencyName) {
        // check if the person exist in the DB
        Optional<Currency> selectCurrencyMaybe = selectOneCurrency(currencyName);

        if (selectCurrencyMaybe.isEmpty()) {
            return 0;
        }

        // sql to delete currency from currency table
        final String deleteCurrencySql = "DELETE FROM currency where id = ?";

        // jdbc used to execute delete query
        jdbcTemplate.update(deleteCurrencySql, selectCurrencyMaybe.get().getId());

        // sql to delete currency from currency values table
        final String deleteCurrencyValuesSql = "DELETE FROM currency_values where primary_currency = ? OR secondary_currency = ?";

        // jdbc used to execute delete query
        jdbcTemplate.update(deleteCurrencyValuesSql, selectCurrencyMaybe.get().getId(), selectCurrencyMaybe.get().getId());

        return 1;

    }

}
