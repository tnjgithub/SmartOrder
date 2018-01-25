package com.hanibey.smartorderadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanibey.smartorder.R;
import com.hanibey.smartorderbusiness.ImageDownloader;
import com.hanibey.smartorderbusiness.InformationService;
import com.hanibey.smartordermodel.OrderItem;

import java.util.ArrayList;

/**
 * Created by Tanju on 8.10.2017.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    InformationService info;

    private ArrayList<OrderItem> listData;
    private LayoutInflater layoutInflater;
    Context context;

    public OrderListAdapter(Context aContext, ArrayList<OrderItem> listData) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(aContext);
        this.context = aContext;
        info = new InformationService(aContext);
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_list_item_layout, viewGroup, false);
        OrderListAdapter.ViewHolder viewHolder = new OrderListAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder viewHolder, int i) {

        OrderItem orderItem = listData.get(i);

        /*viewHolder.txtItemTitle.setText(orderItem.getProductName());
        double totalPrice = orderItem.getPrice() * orderItem.getQuantity();
        String priceText = String.valueOf(orderItem.getPrice()) +" X "+ String.valueOf(orderItem.getQuantity()) +" = "+ String.valueOf(totalPrice) +" TL";
        viewHolder.txtItemPrice.setText(priceText);
        new ImageDownloader(viewHolder.imgage).execute(orderItem.getImagePath());*/

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        public TextView txtItemTitle, txtItemPrice;
        public ImageView imgage;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            txtItemTitle = (TextView) itemView.findViewById(R.id.txt_order_item_title);
            txtItemPrice = (TextView) itemView.findViewById(R.id.txt_order_item_price);
            imgage = (ImageView) itemView.findViewById(R.id.order_item_image);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}
