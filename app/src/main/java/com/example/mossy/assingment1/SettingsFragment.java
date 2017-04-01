package com.example.mossy.assingment1;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private SharedPreferences preferences;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_settings, container, false);

        preferences = this.getActivity().getSharedPreferences("values", MODE_PRIVATE);

        // Set Checkbox Listener
        final CheckBox more_countries_checkbox = (CheckBox) root_view.findViewById(R.id.more_countries_checkbox);
        more_countries_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit()
                        .putBoolean("want_short_list", !more_countries_checkbox.isChecked())
                        .apply();
            }
        });

        more_countries_checkbox.setChecked(!preferences.getBoolean("want_short_list", true));


        return root_view;
    }
}
