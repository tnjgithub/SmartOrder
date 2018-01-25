package com.hanibey.smartorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanibey.smartorderbusiness.InformationService;
import com.hanibey.smartorderbusiness.LogService;
import com.hanibey.smartorderbusiness.SettingService;
import com.hanibey.smartorderhelper.Constant;
import com.hanibey.smartordermodel.Client;

public class SettingActivity extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference clientRef;

    LogService logService;
    SettingService settingService;
    InformationService informationService;

    EditText edtClientName;

    String clientKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /*database = FirebaseDatabase.getInstance();
        clientRef = database.getReference(Constant.Nodes.Client);

        logService = new LogService();
        settingService = new SettingService(this);
        informationService = new InformationService(this);

        edtClientName = (EditText) findViewById(R.id.edt_client_name);
        Button btnsSave = (Button) findViewById(R.id.btn_save);

        clientKey = settingService.getClientKey();
        if(!clientKey.equals("")){
            setNameToEditText();
        }

        btnsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edtClientName.getText())){
                    informationService.alertDialog("Cihaz adı girilmedi!");
                }
                else {
                    clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            boolean isSameName = false;

                            for(DataSnapshot pos : dataSnapshot.getChildren()){
                                String name = pos.child("Name").getValue(String.class);
                                if(name.equals(edtClientName.getText().toString())){
                                    isSameName = true;
                                }
                            }

                            if (isSameName){
                                informationService.alertDialog("same name");
                            }
                            else {
                                String key = clientRef.push().getKey();

                                if(clientKey.equals("")){
                                    Client client = new Client(key, edtClientName.getText().toString(), Constant.ClientStatus.Online, true);
                                    clientRef.child(key).setValue(client);
                                    settingService.saveSetting(key, Constant.LanguageCodes[Constant.LanguageCodeIndex.Tr]);
                                }
                                else {
                                    Client client = new Client(clientKey, edtClientName.getText().toString(), Constant.ClientStatus.Online, true);
                                    clientRef.child(clientKey).setValue(client);
                                }

                                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                                startActivity(intent);
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
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_login){
            //settingService.showLoginDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNameToEditText(){
        /*clientRef.child(clientKey).addListenerForSingleValueEvent(new ValueEventListener() {
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
        });*/
    }


}



