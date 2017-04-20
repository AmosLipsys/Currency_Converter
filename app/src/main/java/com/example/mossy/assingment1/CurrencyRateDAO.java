package com.example.mossy.assingment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class CurrencyRateDAO {

    private CurrencyRateDAOListener listener;

    CurrencyRateDAO(CurrencyRateDAOListener listener) {
        this.listener = listener;
    }

    void get() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String currency_rate_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

                    URL url = new URL(currency_rate_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    final StringBuilder builder = new StringBuilder();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        while (true) {
                            String line = reader.readLine();
                            if (line == null) break;
                            builder.append(line);
                        }
                        listener.textAvailable(builder.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
