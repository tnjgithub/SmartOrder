package com.hanibey.smartorderbusiness;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.hanibey.smartorder.R;
import com.hanibey.smartorderhelper.Constant;
import com.hanibey.smartordermodel.SpinnerNavItem;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tanju on 26.12.2017.
 */

public class LanguageService {

    Context context;
    public LanguageService(Context con){
        this.context = con;
    }

    public ArrayList<SpinnerNavItem> getSpinnerNavItems()
    {
        ArrayList<SpinnerNavItem> navSpinner = new ArrayList<>();
        navSpinner.add(new SpinnerNavItem("Türkçe", R.drawable.language_tr));
        navSpinner.add(new SpinnerNavItem("English", R.drawable.language_en));
        navSpinner.add(new SpinnerNavItem("Deutsch", R.drawable.language_de));

        return navSpinner;
    }

    public String getLanguageCode(int pos){
        String lang = Constant.LanguageCodes[pos];
        return lang;
    }


    public int getLanguageIndex(String code){
        int index = 0;
        for (int i = 0; i< Constant.LanguageCodes.length; i++) {
            if (Constant.LanguageCodes[i].equals(code)) {
                index = i;
                break;
            }
        }

        return index;
    }
}
