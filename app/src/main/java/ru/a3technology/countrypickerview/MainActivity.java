package ru.a3technology.countrypickerview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.widget.TextView;

import java.util.Locale;
import ru.a3technology.countrypicker.View.CountryPickerView;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final Context mContext = MainActivity.this;

    private CountryPickerView mCountryPickerView;
    private TextView tvPhoneNumber, tvCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPhoneNumber = (TextView)findViewById(R.id.tvPhoneNumber);
        tvCountry = (TextView)findViewById(R.id.tvCountry);

        mCountryPickerView = (CountryPickerView)findViewById(R.id.countryPickerView);
        mCountryPickerView.setOnGettingPhoneNumberListener(new CountryPickerView.OnGettingPhoneNumberListener() {
            /**
             * the method called before the user going to select a country from the countries list...
             */
            @Override
            public void onUserStartedChoosingCountry(){
                tvPhoneNumber.setText("");
                tvCountry.setText("");
            }

            /**
             * the method calls when the user selected a country from the countries list
             * @param phone number without speshal symbols such as "+", "-" " " etc, for sending to a server
             * @param countryCode code of the country in ISO format (RU, US...)
             */
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onUserSelectedCountry(String phone, String countryCode) {
                tvCountry.setText(getCountry(countryCode));
                tvPhoneNumber.setText("+" + PhoneNumberUtils.formatNumber(phone, countryCode));
            }

        });
    }

    private String getCountry(String countryCode){
        Locale l = new Locale("", countryCode);
       return l.getDisplayCountry();
    }
}
