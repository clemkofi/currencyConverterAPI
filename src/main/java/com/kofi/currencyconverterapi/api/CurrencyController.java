package com.kofi.currencyconverterapi.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.kofi.currencyconverterapi.model.Currency;
import com.kofi.currencyconverterapi.service.CurrencyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/currency")
@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    // inject into the controller
    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping // this means the method that follows handles post requests
    public Map<String, String> addCurrency(@Valid @NonNull @RequestBody Currency currency){
        if (currencyService.addCurrency(currency) == 1){
            Map<String, String> responseText = new HashMap<String, String>();
            responseText.put("Status", ResponseMessage.SUCCESS_MESSAGE.getId());
            responseText.put("Message", ResponseMessage.SUCCESS_MESSAGE.getMessage());
            return responseText;
        } else {
            Map<String, String> responseText = new HashMap<String, String>();
            responseText.put("Status", ResponseMessage.FAILURE_MESSAGE.getId());
            responseText.put("Message", ResponseMessage.FAILURE_MESSAGE.getMessage());
            return responseText;
        }
    }

    @GetMapping
    public List<Currency> getAllCurrencies(){
        return currencyService.getAllCurrencies();
    }

    @GetMapping(path = "/convert")
    public Currency getOneCurrency(@Valid @NonNull @RequestParam("currName") String currencyName){
        return currencyService.getOneCurrency(currencyName).orElse(null);
    }

    @PutMapping
    public Map<String, String> updateCurrency(@Valid @NonNull @RequestBody Currency currency){
        if (currencyService.updateCurrency(currency) == 1){
            Map<String, String> responseText = new HashMap<String, String>();
            responseText.put("Status", ResponseMessage.SUCCESS_MESSAGE.getId());
            responseText.put("Message", ResponseMessage.SUCCESS_MESSAGE.getMessage());
            return responseText;
        } else {
            Map<String, String> responseText = new HashMap<String, String>();
            responseText.put("Status", ResponseMessage.FAILURE_MESSAGE.getId());
            responseText.put("Message", ResponseMessage.FAILURE_MESSAGE.getMessage());
            return responseText;
        }
    }

    @DeleteMapping(path = "{currencyName}")
    public Map<String, String> deleteCurrency(@PathVariable("currencyName") String currencyName){
        if (currencyService.deleteCurrency(currencyName) == 1){
            Map<String, String> responseText = new HashMap<String, String>();
            responseText.put("Status", ResponseMessage.SUCCESS_MESSAGE.getId());
            responseText.put("Message", ResponseMessage.SUCCESS_MESSAGE.getMessage());
            return responseText;
        } else {
            Map<String, String> responseText = new HashMap<String, String>();
            responseText.put("Status", ResponseMessage.FAILURE_MESSAGE.getId());
            responseText.put("Message", ResponseMessage.FAILURE_MESSAGE.getMessage());
            return responseText;
        }
    }



    
}
