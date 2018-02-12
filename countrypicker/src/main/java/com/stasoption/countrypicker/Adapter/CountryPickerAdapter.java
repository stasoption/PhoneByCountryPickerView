package com.stasoption.countrypicker.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
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

import com.stasoption.countrypicker.Model.Country;
import com.stasoption.countrypicker.R;

/**
 *  @author Stas Averin
 */

public class CountryPickerAdapter extends ArrayAdapter<Country>  {

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

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_country_picker_adapter, parent, false);
        }

        final Country country = getItem(position);
        if(country != null){
            LinearLayout layoutCountry = (LinearLayout)convertView.findViewById(R.id.layoutCountry);
            layoutCountry.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
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