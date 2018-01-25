package com.hanibey.smartorderadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hanibey.smartorder.R;
import com.hanibey.smartorderbusiness.GeneralService;
import com.hanibey.smartorderbusiness.OrderService;
import com.hanibey.smartorderhelper.Constant;
import com.hanibey.smartordermodel.OrderItem;
import com.hanibey.smartordermodel.SelectedOrderItem;

import java.util.ArrayList;


public class SelectedProductListAdapter extends BaseAdapter {

    GeneralService generalService;

    private ArrayList<OrderItem> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public SelectedProductListAdapter(Context aContext, ArrayList<OrderItem> data) {
        this.generalService = new GeneralService();
        this.listData = data;
        this.layoutInflater = LayoutInflater.from(aContext);
        this.context = aContext;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.order_list_row_layout, null);
            holder = new ViewHolder();
            holder.txtKey = (TextView)  convertView.findViewById(R.id.txt_key);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
            holder.btnRemoveItem = (ImageButton)  convertView.findViewById(R.id.btn_remove_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String key = listData.get(position).ProductKey;
        holder.txtKey.setText(String.valueOf(key));
        String title = listData.get(position).ProductName;
        holder.txtTitle.setText(title);

        final String status = listData.get(position).Status;

        if(status.equals(Constant.OrderItemStatus.Selected)){
            holder.txtTitle.setTextColor(Color.BLACK);
            holder.btnRemoveItem.setVisibility(View.VISIBLE);
        }
        else {
            holder.txtTitle.setTextColor(Color.GRAY);
            holder.btnRemoveItem.setVisibility(View.GONE);
        }

        double totalAmount = listData.get(position).Price * Integer.valueOf(listData.get(position).Quantity);
        String priceText = listData.get(position).Quantity +" X "
                +String.valueOf(listData.get(position).Price) +" = "
                +generalService.getCurrencyFormat(totalAmount)
                + " " + Constant.CURRENCY;

        holder.txtPrice.setText(priceText +" | "+ generalService.getItemStatusText(status));

        holder.btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderService orderService = new OrderService(context);
                orderService.removeOrderListItem(key);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView txtKey;
        TextView txtTitle;
        TextView txtPrice;
        ImageButton btnRemoveItem;
    }
}
