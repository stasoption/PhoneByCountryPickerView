package com.stasoption.countrypicker.View;

import android.content.Context;
import android.content.DialogInterface;
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
 * Created by Stas on 18.04.2017.
 */

public class CountryPickerView extends FrameLayout implements
        View.OnClickListener,
        CountryPickerAdapter.OnCountryPickedListener{

    private final static String TAG =  CountryPickerView.class.getSimpleName();
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


    public CountryPickerView(Context context) {
        super(context);
    }

    public CountryPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountryPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        return code.concat(number).replace("+", "").replace(" ", "").replace("-", "").trim();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.country_picker_view, this);
        try {
            init();
            initViews();
            setViewsParameters();
            setCurrentCountry();
            setCurrentPhoneMask();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    private void init()throws Exception{
        mLocale = Locale.getDefault();
        mCountry = Country.getCountryByLocale(mLocale);
    }

    private void initViews()throws Exception{
        btnChoiceCountry = (LinearLayout) findViewById(R.id.btnChoiseCountry);
        tvCountryCode = (TextView) findViewById(R.id.tvCountryCode);
        etCountryPhone = (EditText) findViewById(R.id.etCountryPhone);
        ivCountryFlag = (ImageView) findViewById(R.id.ivCountryFlag);
        btnCountryConfirmValid = (FloatingActionButton) findViewById(R.id.btnCountryConfirmValid);
        btnCountryConfirmInValid = (FloatingActionButton) findViewById(R.id.btnCountryConfirmInValid);
    }

    private void setViewsParameters()throws Exception{
        mAnimButtonRemove = AnimationUtils.loadAnimation(getContext(), R.anim.button_remove);
        mAnimButtonShow = AnimationUtils.loadAnimation(getContext(), R.anim.button_show);
        btnChoiceCountry.setOnClickListener(this);
        mPhoneNumberFormattingTextWatcher = new PhoneNumberFormattingTextWatcher(mLocale.getCountry());
        btnCountryConfirmValid.setOnClickListener(this);
        setTypePhoneFieldParam();
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
            if(mOnPhoneNumberPickListener !=null) mOnPhoneNumberPickListener.onUserSelectedCountry(getPhone(), getCountry().getCode());
        }
    }


    @Override
    public void onCountryPicked(Country country) {
        try {
            mCountry = country;
            mLocale = new Locale("", country.getCode());
            setCurrentCountry();
            setCurrentPhoneMask();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
    }

    private void setCurrentCountry()throws Exception{
        /*check current user location*/
        if(mCountry!=null){
            tvCountryCode.setText(mCountry.getDialCode());
            ivCountryFlag.setImageResource(mCountry.getFlag());
        }
    }

    private void setTypePhoneFieldParam()throws Exception{
        setValidButtonStatus();
        etCountryPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setValidButtonStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCountryPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    etCountryPhone.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etCountryPhone.getWindowToken(), 0);
                        }
                    },1);
                    if(mOnPhoneNumberPickListener !=null) mOnPhoneNumberPickListener.onUserSelectedCountry(getPhone(), getCountry().getCode());
                    return true;
                }
                return false;
            }
        });
    }

    private void setCurrentPhoneMask()throws Exception{
        etCountryPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_PHONE);
        etCountryPhone.removeTextChangedListener(mPhoneNumberFormattingTextWatcher);
        mPhoneNumberFormattingTextWatcher = new PhoneNumberFormattingTextWatcher(mLocale.getCountry());
        etCountryPhone.addTextChangedListener(mPhoneNumberFormattingTextWatcher);
    }

    private void setValidButtonStatus(){
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

    public interface OnPhoneNumberPickListener {
        void onUserStartedChoosingCountry();
        void onUserSelectedCountry(String phone, String country_code);
    }



}
