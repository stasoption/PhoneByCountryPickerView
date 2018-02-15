package com.stasoption.countrypicker.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 *  @author Stas Averin
 */

public final class KeyboardUtil {

    public static void hideKeyboard(@Nullable Activity activity) {
        com.mikepenz.materialize.util.KeyboardUtil.hideKeyboard(activity);
    }

    public static void showKeyboard(@NonNull Activity activity, @NonNull View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
