package com.hanibey.smartorderbusiness;

import android.content.Context;
import com.hanibey.smartordermodel.Settings;
import com.hanibey.smartorderrepository.DBHelper;

/**
 * Created by Tanju on 26.10.2017.
 */

public class SettingService {
    private DBHelper db;
    InformationService informationService;
    Context context;

    public  SettingService(Context con){
        db = new DBHelper(con);
        context = con;
        informationService = new InformationService(con);
    }

    public boolean saveSetting(String key, String code){

        try {
            Settings setting = new Settings();
            setting.setClientKey(key);
            setting.setLanguageCode(code);
            setting.setOrderKey("");

            db.insertSettings(setting);

            return  true;
        }
        catch(Exception ex) {
            return  false;
        }
    }

    public int getSettingId(){
        Settings setting = db.getSettings();
        int id=0;

        if(setting != null)
            id = setting.getId();

        return  id;
    }

    public boolean updateLanguageCode(int id, String code){

        try {
            db.updateLanguageCode(id, code);
            return  true;
        }
        catch(Exception ex) {
            return  false;
        }
    }

    public String getLanguageCode() {
        return db.getLanguageCode();
    }

    public String getClientKey() {
        Settings setting = db.getSettings();
        String clientKey = "";

        if(setting != null && setting.getClientKey() != null && !setting.getClientKey().equals(""))
            clientKey = setting.getClientKey();

        return clientKey;
    }

    public String getOrderKey() {
        Settings setting = db.getSettings();
        String orderKey = "";

        if(setting != null && setting.getOrderKey() != null && !setting.getOrderKey().equals(""))
            orderKey = setting.getClientKey();

        return orderKey;
    }

    public void updateOrderKey(int id, String key) {
        db.updateOrderKey(id, key);
    }

}
