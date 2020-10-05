package com.kofi.currencyconverterapi.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Currency {
    private int id;
    private final String name;
    private final String symbol;
    private Map<String, Double> values;

    // @JsonCreator
    // public Currency(@JsonProperty("id") int id, @JsonProperty("name") String name,
    //         @JsonProperty("symbol") String symbol, @JsonProperty("values") Map<String, Double> values) {
    //     this.id = id;
    //     this.name = name;
    //     this.symbol = symbol;
    //     this.values = values;
    // }

    @JsonCreator
    public Currency(@JsonProperty("name") String name, @JsonProperty("symbol") String symbol,
            @JsonProperty("values") Map<String, Double> values) {
        this.name = name;
        this.symbol = symbol;
        this.values = values;
    }

    public Map<String, Double> getValues() {
        return values;
    }

    public void setValues(Map<String, Double> values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

}