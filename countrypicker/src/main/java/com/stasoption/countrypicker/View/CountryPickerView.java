package com.stasoption.countrypicker.View;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import com.stasoption.countrypicker.Adapter.CountryPickerAdapter;
import com.stasoption.countrypicker.Model.Country;
import com.stasoption.countrypicker.R;

import rx.functions.Action0;

/**
 *  @author Stas Averin
 */

public class CountryPickerView extends FrameLayout implements
        View.OnClickListener,
        CountryPickerAdapter.OnCountryPickedListener{

    private final static String TAG =  CountryPickerView.class.getSimpleName();
    private static final String NOT_DIGITS = "\\D+";

    private OnPhoneNumberPickListener mOnPhoneNumberPickListener;

    private Locale mLocale;
    private Country mCountry;
    private PhoneNumberFormatting mPhoneNumberFormattingTextWatcher;
    private Animation mAnimButtonRemove;
    private Animation mAnimButtonShow;

    private LinearLayout btnChoiceCountry;
    private TextView tvCountryCode;
    private ImageView ivCountryFlag;
    private PhoneInputView etCountryPhone;

    private ImageView btnCountryConfirmValid;
    private ImageView btnCountryConfirmInValid;

    private int mTextCounter;

    public CountryPickerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CountryPickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CountryPickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CountryPickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    public void setOnGettingPhoneNumberListener(OnPhoneNumberPickListener listener){
        this.mOnPhoneNumberPickListener = listener;
    }

    public Country getCountry(){
        return this.mCountry;
    }

    public Locale getLocale(){
        return this.mLocale;
    }

    public String getPhone(){
        String code = mCountry.getDialCode();
        String number = etCountryPhone.getText().toString();
        return removeNotDigits(code.concat(number));
    }

    private void init(@NonNull Context context){
        LayoutInflater.from(getContext()).inflate(R.layout.country_picker_view, this);
        try {
            mLocale = Locale.getDefault();
            mCountry = Country.getCountryByLocale(mLocale);

            btnChoiceCountry = findViewById(R.id.btnChoiseCountry);
            tvCountryCode = findViewById(R.id.tvCountryCode);
            etCountryPhone = findViewById(R.id.etCountryPhone);
            ivCountryFlag = findViewById(R.id.ivCountryFlag);
            btnCountryConfirmValid = findViewById(R.id.btnCountryConfirmValid);
            btnCountryConfirmInValid = findViewById(R.id.btnCountryConfirmInValid);

            mAnimButtonRemove = AnimationUtils.loadAnimation(getContext(), R.anim.button_remove);
            mAnimButtonShow = AnimationUtils.loadAnimation(getContext(), R.anim.button_show);

            etCountryPhone.setSendAction(new Action0() {
                @Override
                public void call() {
                    if(mOnPhoneNumberPickListener !=null)
                        mOnPhoneNumberPickListener.onUserStartPickCountry();
                }
            });
            etCountryPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_PHONE);

            btnChoiceCountry.setOnClickListener(this);
            btnCountryConfirmValid.setOnClickListener(this);

            checkButton();
            update();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        if(id ==  R.id.btnChoiseCountry){
            //calls when the user going to pick some country...
            pickUpCountry();

            if(mOnPhoneNumberPickListener !=null)
                mOnPhoneNumberPickListener.onUserStartPickCountry();

        }else if(id == R.id.btnCountryConfirmValid){
            //calls when the user selected the country..
            if(mOnPhoneNumberPickListener !=null)
                mOnPhoneNumberPickListener.onUserPickedCountry(getPhone(), getCountry().getCode());
        }
    }


    @Override
    public void onCountryPicked(Country country) {
        try {
            mCountry = country;
            mLocale = new Locale("", country.getCode());
            update();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }


    private void update()throws Exception{
        if(mCountry!=null){
            tvCountryCode.setText(mCountry.getDialCode());
            ivCountryFlag.setImageResource(mCountry.getFlag());
        }
        etCountryPhone.removeTextChangedListener(mPhoneNumberFormattingTextWatcher);
        mPhoneNumberFormattingTextWatcher = new PhoneNumberFormatting(mLocale.getCountry());
        etCountryPhone.addTextChangedListener(mPhoneNumberFormattingTextWatcher);
    }

    private void checkButton(){
        if(etCountryPhone.getText().length() == 1 && etCountryPhone.getText().length() > mTextCounter){
            btnCountryConfirmInValid.startAnimation(mAnimButtonRemove);
            btnCountryConfirmInValid.setVisibility(GONE);
            btnCountryConfirmValid.setVisibility(VISIBLE);
            btnCountryConfirmValid.startAnimation(mAnimButtonShow);

        }else if(etCountryPhone.getText().length() == 0){
            btnCountryConfirmValid.startAnimation(mAnimButtonRemove);
            btnCountryConfirmValid.setVisibility(GONE);
            btnCountryConfirmInValid.setVisibility(VISIBLE);
            btnCountryConfirmInValid.startAnimation(mAnimButtonShow);
        }
        mTextCounter = etCountryPhone.getText().length();
    }

    private void pickUpCountry(){
        ArrayList<Country>countries = new ArrayList<>(Country.getAllCountries());
        CountryPickerAdapter adapter = new CountryPickerAdapter(getContext(), countries);
        adapter.setOnCountryPickedListener(this);

        new AlertDialog.Builder(getContext())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        etCountryPhone.setText("");
                        etCountryPhone.requestFocus();
                    }
                })

                .setNegativeButton(getContext().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @NonNull
    public static String removeNotDigits(@Nullable String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll(NOT_DIGITS, "");
    }


    public interface OnPhoneNumberPickListener {

        void onUserStartPickCountry();

        void onUserPickedCountry(String phone, String country_code);

    }


    class PhoneNumberFormatting extends PhoneNumberFormattingTextWatcher {

        PhoneNumberFormatting(String countryCode) {
            super(countryCode);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);
            checkButton();
        }
    }
}
