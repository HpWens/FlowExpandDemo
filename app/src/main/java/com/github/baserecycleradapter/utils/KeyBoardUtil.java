package com.github.baserecycleradapter.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.ref.WeakReference;

/**
 * 操作输入法的工具类。可以方便的关闭和显示输入法.
 */
public class KeyBoardUtil {

    private static KeyBoardUtil instance;
    private InputMethodManager mInputMethodManager;
    private static WeakReference<Activity> mActivity;

    private KeyBoardUtil() {
        mInputMethodManager = (InputMethodManager) mActivity.get().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static KeyBoardUtil getInstance(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
        if (instance == null) {
            instance = new KeyBoardUtil();
        }
        return instance;
    }

    /**
     * 强制显示输入法
     */
    public void show() {
        Activity activity = mActivity.get();
        if (activity != null) {
            show(activity.getWindow().getCurrentFocus());
        }
    }

    public void show(View view) {
        mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 强制关闭输入法
     */
    public void hide() {
        Activity activity = mActivity.get();
        if (activity != null) {
            hide(activity.getWindow().getCurrentFocus());
        }
    }

    public void hide(View view) {
        mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 如果输入法已经显示，那么就隐藏它；如果输入法现在没显示，那么就显示它
     */
    public void showOrHide() {
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 调用制定的输入键盘上的删除按钮
     *
     * @param editText
     */
    public void callDeleteKey(EditText editText) {
        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    /**
     * 关闭dialog中打开的键盘
     *
     * @param dialog
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public static void closeKeyboard(Dialog dialog) {
        View view = dialog.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
