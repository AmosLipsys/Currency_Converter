package com.example.mossy.assingment1;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mossy on 30/03/2017.
 */

public class ConvertedRates {
    Map<String, Double> currency_rates = new HashMap<String, Double>();
    public ConvertedRates(){
        // Set Default. 7:54pm 30th March 2017
        // This is just in case there is no internet for updated exchange rates.
        // Will provide estimate instead.
        currency_rates.put("EUR",  1.0);
        currency_rates.put("USD",  1.0748);
        currency_rates.put("JPY", 119.17);
        currency_rates.put("BGN", 1.9558);
        currency_rates.put("CZK", 27.021);
        currency_rates.put("DKK", 7.4412);
        currency_rates.put("GBP", 0.86385);
        currency_rates.put("HUF", 309.62);
        currency_rates.put("PLN", 4.2389);
        currency_rates.put("RON", 4.5563);
        currency_rates.put("SEK", 9.5505);
        currency_rates.put("CHF", 1.0712);
        currency_rates.put("NOK", 9.1908);
        currency_rates.put("HRK", 7.4360);
        currency_rates.put("RUB", 61.2237);
        currency_rates.put("TRY", 3.9246);
        currency_rates.put("AUD", 1.4060);
        currency_rates.put("BRL", 3.3666);
        currency_rates.put("CAD", 1.4380);
        currency_rates.put("CNY", 7.4063);
        currency_rates.put("HKD", 8.3503);
        currency_rates.put("IDR", 14305.20);
        currency_rates.put("ILS", 3.8972);
        currency_rates.put("INR", 69.7590);
        currency_rates.put("KRW", 1195.89);
        currency_rates.put("MXN", 20.3522);
        currency_rates.put("MYR", 4.7495);
        currency_rates.put("NZD", 1.5321);
        currency_rates.put("PHP", 54.033);
        currency_rates.put("SGD", 1.5006);
        currency_rates.put("THB", 37.011);
        currency_rates.put("ZAR", 14.1215);
    }
}
