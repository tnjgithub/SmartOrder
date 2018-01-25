package com.hanibey.smartorderbusiness;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanibey.smartorder.MainActivity;
import com.hanibey.smartorder.R;
import com.hanibey.smartorderadapter.SelectedProductListAdapter;
import com.hanibey.smartorderhelper.Constant;
import com.hanibey.smartordermodel.Order;
import com.hanibey.smartordermodel.OrderItem;
import com.hanibey.smartorderrepository.FirebaseDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Tanju on 29.09.2017.
 */

public class OrderService {

    private DatabaseReference orderRef, clientRef;
    private SettingService settingService;
    private DateTimeService dateTimeService;
    GeneralService generalService;
    private MediaPlayer mediaPlayer = null;

    private ListView orderListView;
    private TextView txtTotalPrice;

    String message="";

    Context context;
    private Activity activity;

    public  OrderService(Context con){
        this.activity = (Activity) con;
        this.context = con;
        this.orderRef  = FirebaseDb.getDatabase().getReference(Constant.Nodes.Order);
        this.clientRef  = FirebaseDb.getDatabase().getReference(Constant.Nodes.Client);
        this.settingService = new SettingService(con);
        this.dateTimeService = new DateTimeService();
        this.generalService = new GeneralService();
    }

    public String sendOrder(String description){

        final String clientKey = settingService.getClientKey();

        if(MainActivity.getInstance().currentOrderKey.equals("")){

            String orderKey = orderRef.child(clientKey).push().getKey();

            String date = dateTimeService.getCurrentDate();
            double totalPrice = getTotalPrice();

            for(OrderItem item : MainActivity.getInstance().selectedOrderItems){
                item.Status = Constant.OrderItemStatus.New;
            }

            ArrayList<OrderItem> orderItems = MainActivity.getInstance().selectedOrderItems;
            Order order = new Order(orderKey, clientKey, description, totalPrice, date, date, Constant.OrderStatus.New, orderItems);
            orderRef.child(clientKey).child(orderKey).setValue(order);
            MainActivity.getInstance().currentOrderKey = orderKey;
            updateClientChangeStatus(clientKey);
            checkOrderStatus(clientKey, orderKey);

            message = "Siparişiniz başarıyla gönderilmiştir.";
        }
        else {

            final String note = description;
            orderRef.child(clientKey).child(MainActivity.getInstance().currentOrderKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Order order = dataSnapshot.getValue(Order.class);
                    String date = dateTimeService.getCurrentDate();
                    double totalPrice = getTotalPrice();

                    for(OrderItem item : MainActivity.getInstance().selectedOrderItems){
                        if(item.Status.equals(Constant.OrderItemStatus.Selected)){
                            item.Status = Constant.OrderItemStatus.New;
                        }
                    }

                    String orderNote;
                    if(note.equals("")){
                        orderNote = order.Description;
                    }
                    else {
                         orderNote = note + System.getProperty("line.separator") + order.Description;
                    }

                    Order updateOrder = new Order(order.Key, order.ClientKey, orderNote, totalPrice, order.CreateDate, date, Constant.OrderStatus.AdditionalOrder, MainActivity.getInstance().selectedOrderItems);
                    Map<String, Object> postValues = updateOrder.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(MainActivity.getInstance().currentOrderKey, postValues);
                    orderRef.child(clientKey).updateChildren(childUpdates);
                    updateClientChangeStatus(clientKey);
                    message = "Siparişiniz başarıyla güncellenmiştir.";
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    message = context.getString(R.string.unexpected_error);
                }

            });
        }


        return  message;
    }

    private void updateClientChangeStatus(String clientKey){

        clientRef.child(clientKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean changeStatus = dataSnapshot.child("ChangeStatus").getValue(Boolean.class);
                clientRef.child(dataSnapshot.getKey()).child("ChangeStatus").setValue(!changeStatus);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });


    }

    private void checkOrderStatus(String clientKey, String orderKey){

        orderRef.child(clientKey).child(orderKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Order order = dataSnapshot.getValue(Order.class);
                int sId = settingService.getSettingId();

                if(order.Status.equals(Constant.OrderStatus.New))
                    settingService.updateOrderKey(sId, dataSnapshot.getKey());

                if(order.Status.equals(Constant.OrderStatus.Paid)){
                    MainActivity.getInstance().currentOrderKey = "";
                    MainActivity.getInstance().selectedOrderItems = new ArrayList<>();
                    settingService.updateOrderKey(sId, "");
                }
                else {

                    ArrayList<OrderItem> items = getSelectedStatusItems();
                    MainActivity.getInstance().selectedOrderItems = new ArrayList<>();
                    if(items.size() > 0){
                        MainActivity.getInstance().selectedOrderItems.addAll(items);
                        MainActivity.getInstance().selectedOrderItems.addAll(order.Items);
                    }
                    else {
                        MainActivity.getInstance().selectedOrderItems.addAll(order.Items);
                    }

                    showNotification(getStatusText(order.Status));
                }

                updateSelectedProductList();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
    }

    private ArrayList<OrderItem> getSelectedStatusItems(){

        ArrayList<OrderItem>  orderItems = new ArrayList<>();
        for(OrderItem item :  MainActivity.getInstance().selectedOrderItems){
            if(item.Status.equals(Constant.OrderItemStatus.Selected)){
                orderItems.add(item);
            }
        }

        return orderItems;
    }

    public void updateSelectedProductList(){

        orderListView = (ListView) activity.findViewById(R.id.order_listView);
        txtTotalPrice = (TextView) activity.findViewById(R.id.txt_totalprice);
        Button btnSendOrder = (Button) activity.findViewById(R.id.btn_send_order);

        if(MainActivity.getInstance().selectedOrderItems.size() <= 0){
            btnSendOrder.setVisibility(View.GONE);
            txtTotalPrice.setText("Ürün seçilmedi");
        }
        else {

            if(isOrderListHasSelectedStatus()){
                btnSendOrder.setText("Siparişi Gönder");
            }
            else {
                btnSendOrder.setText("Siparişiniz Gönderildi");
            }

            btnSendOrder.setVisibility(View.VISIBLE);
            double totalPrice = getTotalPrice();
            txtTotalPrice.setText("Toplam: " + generalService.getCurrencyFormat(totalPrice)  + " " + Constant.CURRENCY);
        }

        //ArrayList<OrderItem> selectedOrderItems = groupByItems();
        SelectedProductListAdapter adapter = new SelectedProductListAdapter(context, MainActivity.getInstance().selectedOrderItems);
        orderListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void checkClientOrder(){

        final String ordKey = settingService.getOrderKey();

        if(!ordKey.equals("")){
            final String cKey = settingService.getClientKey();
            orderRef.child(cKey).child(ordKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        Order order = dataSnapshot.getValue(Order.class);
                        MainActivity.getInstance().currentOrderKey = ordKey;
                        MainActivity.getInstance().selectedOrderItems = order.Items;
                        updateSelectedProductList();
                        checkOrderStatus(cKey,ordKey );
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }

            });
        }

    }

    private double getTotalPrice(){

        double totalPrice = 0;

        for(OrderItem item : MainActivity.getInstance().selectedOrderItems)
        {
            totalPrice += item.Price * Integer.valueOf(item.Quantity);
        }

        return  totalPrice;
    }

    public void removeOrderListItem(String productKey){

        try{

            Iterator<OrderItem> itr = MainActivity.getInstance().selectedOrderItems.iterator();

            while (itr.hasNext())
            {
                OrderItem item = itr.next();
                if (item.ProductKey.equals(productKey) && item.Status.equals(Constant.OrderItemStatus.Selected))
                {
                    itr.remove();
                }
            }

            updateSelectedProductList();

        }catch (Exception ex){
            showLocationDialog(String.valueOf(ex));
        }
    }

    private void showLocationDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.dialog_title));
        builder.setMessage(message);

        String negativeText = context.getString(R.string.ok);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getStatusText(String status){

        String text ="";

        switch (status){
            case Constant.OrderStatus.New:
            case Constant.OrderStatus.AdditionalOrder:
                text = "Siparişiniz başarıyla iletildi";
                break;
            case Constant.OrderStatus.Preparing:
                text = "Siparişiniz hazırlanıyor...";
                break;
            case Constant.OrderStatus.OnTheTable:
                text = "Siparişiniz hazır";
                break;
            default:
                break;
        }

        return  text;
    }

    public boolean isOrderListHasSelectedStatus(){

        for (OrderItem item : MainActivity.getInstance().selectedOrderItems){
            if(item.Status.equals(Constant.OrderItemStatus.Selected)){
                return  true;
            }
        }
        return false;
    }

    private void showNotification(String message){

        managerOfSound();
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.coordinatorLayout);
        Snackbar mSnackBar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        View view = mSnackBar.getView();
        //view.setBackgroundColor(Color.GREEN);
        TextView mainTextView = (TextView) (view).findViewById(android.support.design.R.id.snackbar_text);
        mainTextView.setTextColor(Color.WHITE);
        mSnackBar.show();
    }

    private void managerOfSound() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.notification_sound);
        mediaPlayer.start();
    }

}
