package com.stasoption.countrypicker.View;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import com.stasoption.countrypicker.Adapter.CountryPickerAdapter;
import com.stasoption.countrypicker.Model.Country;
import com.stasoption.countrypicker.R;

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
    private PhoneNumberFormattingTextWatcher mPhoneNumberFormattingTextWatcher;
    private Animation mAnimButtonRemove;
    private Animation mAnimButtonShow;

    private LinearLayout btnChoiceCountry;
    private TextView tvCountryCode;
    private ImageView ivCountryFlag;
    private EditText etCountryPhone;

    private FloatingActionButton btnCountryConfirmValid;
    private FloatingActionButton btnCountryConfirmInValid;

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

            etCountryPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_PHONE);

            mPhoneNumberFormattingTextWatcher = new PhoneNumberFormattingTextWatcher(mLocale.getCountry());

            btnChoiceCountry.setOnClickListener(this);
            btnCountryConfirmValid.setOnClickListener(this);

            checkButton();
            setTypeParam();
            setCountry();
            setPhoneMask();
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
                mOnPhoneNumberPickListener.onUserStartedChoosingCountry();

        }else if(id == R.id.btnCountryConfirmValid){
            //calls when the user selected the country..
            if(mOnPhoneNumberPickListener !=null)
                mOnPhoneNumberPickListener.onUserSelectedCountry(getPhone(), getCountry().getCode());
        }
    }


    @Override
    public void onCountryPicked(Country country) {
        try {
            mCountry = country;
            mLocale = new Locale("", country.getCode());
            setCountry();
            setPhoneMask();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    private void setCountry()throws Exception{
        /*check current user location*/
        if(mCountry!=null){
            tvCountryCode.setText(mCountry.getDialCode());
            ivCountryFlag.setImageResource(mCountry.getFlag());
        }
    }

    private void setTypeParam()throws Exception{
        etCountryPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etCountryPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    etCountryPhone.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.hideSoftInputFromWindow(etCountryPhone.getWindowToken(), 0);
                            }
                        }
                    },1);
                    if(mOnPhoneNumberPickListener !=null)
                        mOnPhoneNumberPickListener.onUserSelectedCountry(getPhone(), getCountry().getCode());
                    return true;
                }
                return false;
            }
        });
    }

    private void setPhoneMask()throws Exception{

        etCountryPhone.removeTextChangedListener(mPhoneNumberFormattingTextWatcher);
        mPhoneNumberFormattingTextWatcher = new PhoneNumberFormattingTextWatcher(mLocale.getCountry());
        etCountryPhone.addTextChangedListener(mPhoneNumberFormattingTextWatcher);
    }

    private void checkButton(){
        if(etCountryPhone.getText().length()== 1 && etCountryPhone.getText().length() > mTextCounter){
            btnCountryConfirmInValid.startAnimation(mAnimButtonRemove);
            btnCountryConfirmInValid.setVisibility(GONE);
            btnCountryConfirmValid.setVisibility(VISIBLE);
            btnCountryConfirmValid.startAnimation(mAnimButtonShow);

        }else if(etCountryPhone.getText().length()==0){
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

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        void onUserStartedChoosingCountry();

        void onUserSelectedCountry(String phone, String country_code);

    }
}
