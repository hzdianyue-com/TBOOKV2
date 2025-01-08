package com.melon.tbook.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {

    private Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]*\\.?[0-9]{0," + digitsAfterZero + "}");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String newString = dest.toString().substring(0, dstart) + source.toString().substring(start, end) + dest.toString().substring(dend, dest.toString().length());
        Matcher matcher = mPattern.matcher(newString);
        if (!matcher.matches()) {
            return "";
        }
        return null;
    }
}