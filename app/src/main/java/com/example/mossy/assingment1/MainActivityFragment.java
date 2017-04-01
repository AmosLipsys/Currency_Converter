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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;



/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    // Set Starting Variables
    TextView status_view, edit_2;
    Spinner spinner1, spinner2;
    EditText edit_1;
    String xml_text;
    String country_code_1;
    String country_code_2;
    double rate_1;
    double rate_2;
    double amount_1 = 1.0;
    double amount_2;
    ConvertedRates convertedRates;
    boolean use_short_list;
    int chosen_list;


    private SharedPreferences preferences;
    public MainActivityFragment() {
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

        //Create Spinner Listener 1
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update Rate, calculate amount 2 and update text boxes
                update_rates();
                calculate_amount_2();
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
                calculate_amount_2();
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
                    amount_1 = Double.parseDouble(string.toString());

                } catch (Exception e) {
                    amount_1 = 0;
                }
                calculate_amount_2();
                update_text_box_2();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Create Table of Converted Rates. All From EUR
        convertedRates = new ConvertedRates();

        // Update rates by looking at the country codes selected
        update_rates();

        // Find and Set Amounts
        calculate_amount_2();
        update_text_box_1();
        update_text_box_2();

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
    }


    private void update_rates() {
        // Get Country Codes From Spinner
        country_code_1 = spinner1.getSelectedItem().toString();
        country_code_2 = spinner2.getSelectedItem().toString();

        // Get Rates
        rate_1 = convertedRates.currency_rates.get(country_code_1);
        rate_2 = convertedRates.currency_rates.get(country_code_2);
    }

    private void update_text_box_2() {
        // Format amounts
        String formatted_amount_2 = format_amount(amount_2);
        edit_2.setText(formatted_amount_2);
    }

    private void update_text_box_1() {
        // Format amounts
        String formatted_amount_1 = format_amount(amount_1);
        edit_1.setText(formatted_amount_1);
    }

    private String format_amount(double number) {
        DecimalFormat number_format = new DecimalFormat("0.###");
        String formatted_number = number_format.format(number);
        return formatted_number;
    }

    private void calculate_amount_2() {
        amount_2 = (rate_2 / rate_1) * amount_1;
    }
}
