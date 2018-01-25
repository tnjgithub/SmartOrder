package com.hanibey.smartordermodel;

/**
 * Created by Tanju on 30.09.2017.
 */

public class Settings {

    public int Id;
    public String ClientKey;
    public String LanguageCode;
    public String OrderKey;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getClientKey() {
        return ClientKey;
    }

    public void setClientKey(String clientKey) {
        ClientKey = clientKey;
    }

    public String getLanguageCode() {
        return LanguageCode;
    }

    public void setLanguageCode(String languageCode) {
        LanguageCode = languageCode;
    }

    public String getOrderKey() {
        return OrderKey;
    }

    public void setOrderKey(String orderKey) {
        OrderKey = orderKey;
    }
}
