package com.hanibey.smartorderbusiness;

import com.hanibey.smartorder.MainActivity;
import com.hanibey.smartorderhelper.Constant;
import com.hanibey.smartordermodel.Product;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tanju on 30.09.2017.
 */

public class GeneralService {

    public String getCurrencyFormat(double price){
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        String formated = df.format(price);

        return formated;
    }

    public String getItemStatusText(String status){
        String sText = "-";

        switch (status){
            case Constant.OrderItemStatus.Selected:
                sText = "bekliyor...";
                break;
            case Constant.OrderItemStatus.New:
                sText = "gönderildi...";
                break;
            case Constant.OrderItemStatus.Preparing:
                sText = "hazırlanıyor...";
                break;
            case Constant.OrderItemStatus.OnTheTable:
                sText = "servis edildi...";
                break;
        }

        return sText;
    }

    public static ArrayList<Product> GetProductByCategory(int categoryId){

        ArrayList<Product> productList = new ArrayList<>();
        for(Product product : MainActivity.getInstance().allProduct)
        {
            /*if(product.CategoryKey == categoryId)
                productList.add(product);*/
        }

        return productList;
    }

    public static String[] getDefaultQuantities(){
        String[] quantityList = new String[10];

        for (int i=0; i < 10; i++){
            quantityList[i] = String.valueOf(i+1);
        }

        return  quantityList;
    }

}
