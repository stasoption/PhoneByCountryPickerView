package ru.a3technology.countrypicker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ru.a3technology.countrypicker.Model.Country;
import ru.a3technology.countrypicker.R;

/**
 * Created by Stas on 19.04.2017.
 */

public class CountryPickerAdapter  extends ArrayAdapter<Country>  {

    private OnCountryPickedListener mPickedListener;

    public CountryPickerAdapter(Context context, ArrayList<Country> country) {
        super(context, 0, country);
    }

    public void setOnCountryPickedListener(OnCountryPickedListener listener){
        this.mPickedListener = listener;
    }

    public interface OnCountryPickedListener {
        void onCountryPicked(Country mCountry);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Country country = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_country_picker_adapter, parent, false);
        }
        // Lookup view for data population
        if(country != null){
            LinearLayout layoutCountry = (LinearLayout)convertView.findViewById(R.id.layoutCountry);
            layoutCountry.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        if(mPickedListener!=null)
                            mPickedListener.onCountryPicked(country);
                    }
                    return false;
                }
            });

            ImageView ivCountryFlag = (ImageView)convertView.findViewById(R.id.ivCountryFlag);
            ivCountryFlag.setImageResource(country.getFlag());

            TextView tvCountryName = (TextView) convertView.findViewById(R.id.tvCountryName);
            Locale l = new Locale("", country.getCode());
            tvCountryName.setText(l.getDisplayCountry());

            TextView tvCountryCode = (TextView) convertView.findViewById(R.id.tvCountryCode);
            tvCountryCode.setText(country.getDialCode());

        }
        return convertView;
    }



}