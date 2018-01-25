package com.hanibey.smartorder;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hanibey.smartorderadapter.TitleNavigationAdapter;
import com.hanibey.smartorderadapter.ViewPagerAdapter;
import com.hanibey.smartorderbusiness.InformationService;
import com.hanibey.smartorderbusiness.LanguageService;
import com.hanibey.smartorderbusiness.LogService;
import com.hanibey.smartorderbusiness.OrderService;
import com.hanibey.smartorderbusiness.SettingService;
import com.hanibey.smartorderhelper.Constant;
import com.hanibey.smartordermodel.Client;
import com.hanibey.smartordermodel.OrderItem;
import com.hanibey.smartordermodel.Product;
import com.hanibey.smartordermodel.SpinnerNavItem;
import com.hanibey.smartorderrepository.FirebaseDb;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "LIFECYCLE_TEST";
    DatabaseReference clientRef;
    LogService logService;
    InformationService informationService;
    LanguageService languageService;
    SettingService settingService;
    OrderService orderService;

    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    private String clientKey = "";
    public String currentOrderKey="";
    public ArrayList<Product> allProduct = new ArrayList<>();
    public ArrayList<OrderItem> selectedOrderItems = new ArrayList<>();

    private ArrayList<SpinnerNavItem> navSpinner;
    private TitleNavigationAdapter titleNavigationAdapter;
    EditText edtClientName;

    public static final MainActivity instance = new MainActivity();
    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_main);

        if(isNetworkAvailable(this)){

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);

            logService = new LogService();
            settingService = new SettingService(this);
            informationService = new InformationService(this);
            languageService = new LanguageService(this);
            orderService = new OrderService(this);

            orderService.checkClientOrder();
            clientKey = settingService.getClientKey();

            if(clientKey.equals("")){
                settingDialog("");
            }
            else {
                loadData();
                orderService.updateSelectedProductList();
                Button btnSendOrder = (Button) findViewById(R.id.btn_send_order);
                btnSendOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(orderService.isOrderListHasSelectedStatus()){
                            showNoteDialog();
                        }
                    }
                });
            }
        }
        else {
            connectingDialog();
        }


    }

    @Override
    public void onStart() {
        super.onStart();  // Always call the superclass method first
        changeClientStatus(Constant.ClientStatus.Online);
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        changeClientStatus(Constant.ClientStatus.Background);
        Log.d(TAG, "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        changeClientStatus(Constant.ClientStatus.Offline);
        Log.d(TAG, "onDestroy");
    }


    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.i("TAG", "Press Home");
            System.exit(0);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }*/

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(isNetworkAvailable(this)){
            final MenuItem item = menu.findItem(R.id.language_spinner);
            Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

            navSpinner = languageService.getSpinnerNavItems();
            titleNavigationAdapter = new TitleNavigationAdapter(getApplicationContext(), navSpinner);
            spinner.setAdapter(titleNavigationAdapter);

            int index = languageService.getLanguageIndex(Constant.SELECTED_LANGUAGE_CODE);
            spinner.setSelection(index);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                    String code = languageService.getLanguageCode(position);
                    if(!Constant.SELECTED_LANGUAGE_CODE.equals(code)){
                        Constant.SELECTED_LANGUAGE_CODE = code;
                        recreate();
                    }
                    //Toast.makeText(getApplicationContext(), "Selected Language : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final Menu fMenu = menu;
        if(isNetworkAvailable(this)){
            DatabaseReference customerRef = FirebaseDb.getDatabase().getReference(Constant.Nodes.Customer);
            customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        String title = dataSnapshot.child("Title").getValue(String.class);
                        String currency = dataSnapshot.child("Currency").getValue(String.class);

                        fMenu.findItem(R.id.action_company).setTitle(title);
                        Constant.CURRENCY = currency;
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Error", "Failed to read value.", error.toException());
                }
            });
        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){
            String cKey = settingService.getClientKey();
            settingDialog(cKey);

            /*Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);*/
        }
        else if(id == R.id.action_help){
            Toast.makeText(MainActivity.this, "Yardım", Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.action_about){
            Toast.makeText(MainActivity.this, "Hakkında", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    private void setLanguage(){
        Resources res = this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(Constant.SELECTED_LANGUAGE_CODE)); // API 17+ only.
        res.updateConfiguration(conf, dm);
    }

    private void settingDialog(String cKey){

        final String clientKey = cKey;

        clientRef = FirebaseDb.getDatabase().getReference(Constant.Nodes.Client);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.client_setting_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.75);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.60);
        dialog.getWindow().setLayout(width, height);

        edtClientName = (EditText) dialog.findViewById(R.id.edt_client_name);
        Button btnSave = (Button) dialog.findViewById(R.id.btn_save);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);


        if(!clientKey.equals("")){
            setNameToEditText();
            btnCancel.setVisibility(View.VISIBLE);
        }
        else {
           btnCancel.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edtClientName.getText())){
                    informationService.alertDialog("Cihaz adı girilmedi!");
                }
                else {

                    final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Lütfen Bekleyin",
                            "İşlem gerçekleşiyor...", false, false);

                    clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            boolean isSameName = checkSameName(dataSnapshot, edtClientName.getText().toString());

                            if (isSameName){
                                informationService.alertDialog("Bu isim başka bir cihaz için tanımlı");
                                progressDialog.dismiss();
                            }
                            else {
                                if(clientKey.equals("")){
                                    String key = clientRef.push().getKey();
                                    Client client = new Client(key, edtClientName.getText().toString(), Constant.ClientStatus.Online, true);
                                    clientRef.child(key).setValue(client);
                                    settingService.saveSetting(key, Constant.LanguageCodes[Constant.LanguageCodeIndex.Tr]);
                                }
                                else {
                                    Client client = new Client(clientKey, edtClientName.getText().toString(), Constant.ClientStatus.Online, true);
                                    clientRef.child(clientKey).setValue(client);
                                }

                                dialog.dismiss();
                                reStartActivity();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            informationService.alertDialog(getString(R.string.unexpected_error));
                            Log.w("Error", "Failed to read value.", error.toException());
                        }
                    });
                }

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean checkSameName(DataSnapshot dataSnapshot, String edtName){
        for(DataSnapshot pos : dataSnapshot.getChildren()){
            String name = pos.child("Name").getValue(String.class);
            if(name.equals(edtName)){
                return true;
            }
        }

        return false;
    }

    private void setNameToEditText(){
        DatabaseReference clientRef = FirebaseDb.getDatabase().getReference(Constant.Nodes.Client);
        clientRef.child(clientKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue(String.class);
                edtClientName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                informationService.alertDialog(getString(R.string.unexpected_error));
                Log.w("Error", "Failed to read value.", error.toException());
            }
        });
    }

    private void loadData() {


        final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.uploading), getString(R.string.uploading_data_message), false, false);

        try{

            DatabaseReference categoryRef = FirebaseDb.getDatabase().getReference(Constant.Nodes.Category);
            categoryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    for(DataSnapshot pos : dataSnapshot.getChildren()){
                        String name = pos.child("Name").getValue(String.class);
                        viewPagerAdapter.addFragment(new ProductFragment(MainActivity.this, pos.getKey()), name);
                    }
                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Error", "Failed to read value.", error.toException());
                    progressDialog.dismiss();
                }
            });
        }
        catch (Exception ex){
            progressDialog.dismiss();
        }


    }

    private void showNoteDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.order_note_dialog_layout);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.60);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.60);
        dialog.getWindow().setLayout(width, height);

        final EditText edtNote = (EditText) dialog.findViewById(R.id.edt_order_note);
        final Button btnSaveOrder = (Button) dialog.findViewById(R.id.btn_save_order);

        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = TextUtils.isEmpty(edtNote.getText()) ? "" : edtNote.getText().toString();
                orderService.sendOrder(note);
                //informationService.alertDialog(message);
                dialog.dismiss();
            }
        });

        //cancel button
        final Button btnCancelOrder = (Button) dialog.findViewById(R.id.btn_cancel_order);
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void changeClientStatus(String status){
        if(!clientKey.equals("")){
            DatabaseReference clientRef = FirebaseDb.getDatabase().getReference(Constant.Nodes.Client);
            clientRef.child(clientKey).child("Status").setValue(status);
        }
    }

    private void reStartActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private boolean isNetworkAvailable(Context con){

        try{
            ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return false;
    }

    private void connectingDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.connecting_layout);
        dialog.setCanceledOnTouchOutside(false);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.80);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.60);
        dialog.getWindow().setLayout(width, height);

        Button btnReconnect = (Button) dialog.findViewById(R.id.btn_reconnect);

        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                recreate();
            }
        });

        dialog.show();
    }

}
