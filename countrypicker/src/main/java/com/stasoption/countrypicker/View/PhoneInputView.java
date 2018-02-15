package com.stasoption.countrypicker.View;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.stasoption.countrypicker.util.KeyboardUtil;

import rx.functions.Action0;

/**
 * @author Stas Averin
 */

public class PhoneInputView extends android.support.v7.widget.AppCompatEditText implements View.OnFocusChangeListener{

    private AppCompatEditText mAppCompatEditText;
    @Nullable
    private Action0 mSendAction;

    public PhoneInputView(Context context) {
        super(context);
        init();
    }

    public PhoneInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setSendAction(@Nullable Action0 sendAction) {
        mSendAction = sendAction;
    }

    private void init(){
        mAppCompatEditText = this;
        setOnFocusChangeListener(this);

        setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            KeyboardUtil.hideKeyboard((Activity)getContext());
                            if(mSendAction !=null)
                                mSendAction.call();
                        }
                    },100);

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus){
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    KeyboardUtil.showKeyboard((Activity)getContext(), mAppCompatEditText);
                }
            } ,100);
        }else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    KeyboardUtil.hideKeyboard((Activity)getContext());
                }
            }, 100);
        }
    }
}
