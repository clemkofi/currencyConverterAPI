package com.kofi.currencyconverterapi.api;

import java.util.HashMap;
import java.util.Map;

import com.kofi.currencyconverterapi.service.CurrencyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class RootController {

    private final CurrencyService currencyService;

    // inject into the controller
    @Autowired
    public RootController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public Map<String, String> getRoot(){
        Map<String, String> responseText = new HashMap<String, String>();
            responseText.put("Status", "API Working");
            responseText.put("Message", "Welcome to Currency Converter API!");
            return responseText;
    }
    
}
