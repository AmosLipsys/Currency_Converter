// NOTE this currency converter is not 100% accurate
// USD --> EUR --> AUD is not the same as USD --> AUD but is very similar
// This is what is used for calculations.
// Rates were gathered by the European National Bank
// Rates are updated every 24hrs on weekdays
// The URL used to collect the rates: http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml

package com.example.mossy.assingment1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public CurrencyRateDAO dao;
    // Set Starting Variables
    TextView edit_2;
    Spinner spinner1, spinner2;
    EditText edit_1;
    String country_code_1, country_code_2;
    ConvertedAmount total_converted_amount;
    ConvertedRates converted_rates;
    boolean use_short_list;
    int chosen_list;
    Button update_butt;
    private TextView status_text_view;
    private SharedPreferences preferences;



    public MainActivityFragment() {
        super();
        dao = new CurrencyRateDAO(new CurrencyRateDAOListener() {
            @Override
            public void textAvailable(final String text) {
                final String[] webpage_text = text.split("Cube ");
                final Map<String, Double> currency_rates = new HashMap<String, Double>();

                // This is to extract the date.
                final String date = webpage_text[1].split("\'")[1];

                final String time = Calendar.getInstance().getTime().toString();

                int count = 0;
                String[] split_line;
                String country_code;
                double rate;

                currency_rates.put("EUR", 1.0);
                for (String line : webpage_text) {
                    if (count > 1) {
                        split_line = line.split("\'");
                        country_code = split_line[1];
                        rate = Double.parseDouble(split_line[3]);
                        currency_rates.put(country_code, rate);
                    }
                    count += 1;
                }


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        converted_rates.setCurrency_rates(currency_rates);

                        preferences.edit()
                                .putString("status_text", String.format("Status: \n\nRate Updated at: %s\nLast Request: %s", date, time))
                                .apply();
                        status_text_view.setText(preferences.getString("status_text", "Status: Unknown"));


                    }
                });
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_main, container, false);

        // Load Preferences
        preferences = this.getActivity().getSharedPreferences("values", MODE_PRIVATE);
        use_short_list = preferences.getBoolean("want_short_list", true);

        // Create objects in fragment_main.xml
        spinner1 = (Spinner) root_view.findViewById(R.id.currency_spinner_1);
        spinner2 = (Spinner) root_view.findViewById(R.id.currency_spinner_2);
        edit_1 = (EditText) root_view.findViewById(R.id.currency_edit_box_1);
        edit_2 = (TextView) root_view.findViewById(R.id.currency_edit_box_2);
        status_text_view = (TextView) root_view.findViewById(R.id.status_text);
        update_butt = (Button) root_view.findViewById(R.id.update_exchange_main_butt);

        // Set Converted Amount
        total_converted_amount = new ConvertedAmount();

        // Set Lists
        int long_list = R.array.currency_names_long;
        int short_list = R.array.currency_names_short;

        // Select How Many Countries can be selected.
        if (use_short_list) {
            chosen_list = short_list;
        } else {
            chosen_list = long_list;
        }

        // Create Spinners with simple dropdown menus
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                chosen_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        //Set Spinner to alternative selection
        spinner1.setSelection(1);
        spinner2.setSelection(2);

        // Create Button Listener
        update_butt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dao.get();
                // Perform action on click
            }
        });

        //Create Spinner Listener 1
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update Rate, calculate amount 2 and update text boxes
                update_rates();
                update_text_box_2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        //Create Spinner Listener 1
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update Rate, calculate amount 2 and update text boxes
                update_rates();
                update_text_box_2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        // Create Edit Text Listener
        edit_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence string, int start, int before, int count) {

                // Update amount 1
                // Catching the time it is blank


                try {
                    total_converted_amount.setUser_amount(Double.parseDouble(string.toString()));

                } catch (Exception e) {
                    total_converted_amount.setUser_amount(0);
                }
                update_text_box_2();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Create Table of Converted Rates. All comparing EUR
        // NOTE this is not 100% accurate
        // USD --> EUR --> AUD is not the same as USD --> AUD but is very similar
        converted_rates = new ConvertedRates();

        // Update rates by looking at the country codes selected
        update_rates();

        // Find and Set Amounts
        update_text_box_1();
        update_text_box_2();
        status_text_view.setText(preferences.getString("status_text", "Status: Unknown"));

        return root_view;
    }

    @Override
    public void onStart() {
        super.onStart();

        preferences = this.getActivity().getSharedPreferences("values", MODE_PRIVATE);
        use_short_list = preferences.getBoolean("want_short_list", true);

        int long_list = R.array.currency_names_long;
        int short_list = R.array.currency_names_short;

        if (use_short_list) {
            chosen_list = short_list;
        } else {
            chosen_list = long_list;
        }


        // Create Spinners with simple dropdown menus
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                chosen_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        if (preferences.getBoolean("force_update", true)) {
            dao.get();
            preferences.edit()
                    .putBoolean("force_update", false)
                    .apply();
        }
    }


    private void update_rates() {
        // Get Country Codes From Spinner
        country_code_1 = spinner1.getSelectedItem().toString();
        country_code_2 = spinner2.getSelectedItem().toString();

        // Get Rates from the country codes
        double rate_1 = converted_rates.currency_rates.get(country_code_1);
        double rate_2 = converted_rates.currency_rates.get(country_code_2);

        // Set rates
        total_converted_amount.update_rates(rate_1, rate_2);
    }


    private void update_text_box_2() {
        // Format amounts
        Currency currency = Currency.getInstance(country_code_2);
        String currency_symbol = currency.getSymbol();
        String formatted_amount_2 = String.format("%s %s", currency_symbol, format_amount(total_converted_amount.getTotal_amount()));
        edit_2.setText(formatted_amount_2);
    }

    private void update_text_box_1() {
        // Format amounts
        String formatted_amount_1 = format_amount(total_converted_amount.getUser_amount());
        edit_1.setText(formatted_amount_1);
    }

    private String format_amount(double number) {
        DecimalFormat number_format = new DecimalFormat("0.###");
        String formatted_number = number_format.format(number);
        return formatted_number;
    }


}
