package com.stasoption.countrypickerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.widget.TextView;

import java.util.Locale;
import com.stasoption.countrypicker.View.CountryPickerView;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private CountryPickerView mCountryPickerView;
    private TextView tvPhoneNumber, tvCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPhoneNumber = (TextView)findViewById(R.id.tvPhoneNumber);
        tvCountry = (TextView)findViewById(R.id.tvCountry);

        mCountryPickerView = (CountryPickerView)findViewById(R.id.countryPickerView);
        mCountryPickerView.setOnGettingPhoneNumberListener(new CountryPickerView.OnPhoneNumberPickListener() {
            /*method called before the user going to select a country from the countries list...*/
            @Override
            public void onUserStartPickCountry(){
                tvPhoneNumber.setText("");
                tvCountry.setText("");
            }

            /**
             * the method calls when the user selected a country from the countries list
             * @param phone number without special symbols such as "+", "-" " " etc
             * @param countryCode code of the country in ISO format (RU, US...)
             */
            @Override
            public void onUserPickedCountry(String phone, String countryCode) {
                tvCountry.setText(getCountry(countryCode));
                tvPhoneNumber.setText(phone);
            }
        });
    }

    private String getCountry(String countryCode){
        Locale l = new Locale("", countryCode);
        return l.getDisplayCountry();
    }
}
