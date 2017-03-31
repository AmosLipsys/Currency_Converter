package com.example.mossy.assingment1;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Array;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

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
    boolean use_short_list = false;
    int chosen_list;

    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create objects in fragment_main.xml
        spinner1 = (Spinner) findViewById(R.id.currency_spinner_1);
        spinner2 = (Spinner) findViewById(R.id.currency_spinner_2);
        edit_1 = (EditText) findViewById(R.id.currency_edit_box_1);
        edit_2 = (TextView) findViewById(R.id.currency_edit_box_2);

        int long_list = R.array.currency_names_long;
        int short_list = R.array.currency_names_short;

        // Select How Many Countries can be selected.
        if (use_short_list){
            chosen_list = short_list;
        }
        else {
            chosen_list = long_list;
        }

        // Create Spinners with simple dropdown menus
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                chosen_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        //Set Spinner to alternative selection
        spinner2.setSelection(2);

        //Create Spinner Listener 1
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update Rate, calculate amount 2 and update text boxes
                update_rates();
                caculate_amount_2();
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
                caculate_amount_2();
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


                try{
                    amount_1 = Double.parseDouble(string.toString());

                }
                catch (Exception e){
                    amount_1 = 0;
                }
                caculate_amount_2();
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
        caculate_amount_2();
        update_text_box_1();
        update_text_box_2();

        // Create Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        new doit().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class doit extends AsyncTask<Void, Void, Void> {
        String words;

        @Override
        protected Void doInBackground(Void... params) {

            String url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

            try {
                Document doc = Jsoup.connect(url).get();
                words = doc.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            status_view.setText(words);
        }
    }

    private void update_rates(){
        // Get Country Codes From Spinner
        country_code_1 = spinner1.getSelectedItem().toString();
        country_code_2 = spinner2.getSelectedItem().toString();

        // Get Rates
        rate_1 = convertedRates.currency_rates.get(country_code_1);
        rate_2 = convertedRates.currency_rates.get(country_code_2);
    }

    private void update_text_box_2(){
        // Format amounts
        String formatted_amount_2 = format_amount(amount_2);
        edit_2.setText(formatted_amount_2);
    }

    private void update_text_box_1(){
        // Format amounts
        String formatted_amount_1 = format_amount(amount_1);
        edit_1.setText(formatted_amount_1);
    }

    private String format_amount(double number){
        DecimalFormat number_format = new DecimalFormat("0.###");
        String formatted_number = number_format.format(number);
        return formatted_number;
    }

    private void caculate_amount_2(){
        amount_2 = (rate_2/rate_1) * amount_1;
    }
}


