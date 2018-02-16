package com.stasoption.countrypickerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;
import com.stasoption.countrypicker.View.CountryPickerView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mMainLayout;
    private TextView mPhoneNumber, mCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLayout = findViewById(R.id.mainLayout);
        mPhoneNumber = findViewById(R.id.tvPhoneNumber);
        mCountry = findViewById(R.id.tvCountry);

        CountryPickerView countryPickerView = findViewById(R.id.countryPickerView);
        countryPickerView.setOnGettingPhoneNumberListener(new CountryPickerView.OnPhoneNumberPickListener() {
            /*method called before the user going to select a country from the countries list...*/
            @Override
            public void onUserStartPickCountry(){
                mPhoneNumber.setText("");
                mCountry.setText("");
            }

            /**
             * the method calls when the user selected a country from the countries list
             * @param phone number without special symbols such as "+", "-" " " etc
             * @param countryCode code of the country in ISO format (RU, US...)
             */
            @Override
            public void onUserPickedCountry(String phone, String countryCode) {
                mMainLayout.requestFocus();
                mCountry.setText(getCountry(countryCode));
                mPhoneNumber.setText(phone);
            }
        });
    }

    private String getCountry(String countryCode){
        Locale l = new Locale("", countryCode);
        return l.getDisplayCountry();
    }
}
