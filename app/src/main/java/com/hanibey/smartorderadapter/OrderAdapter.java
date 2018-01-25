package com.hanibey.smartorderadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hanibey.smartorder.R;
import com.hanibey.smartordermodel.Order;

import java.util.ArrayList;

/**
 * Created by Tanju on 8.10.2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    ArrayList<Order> orderList;
    static Context context;


    public OrderAdapter(Context context, ArrayList<Order> orders) {
        super();
        this.context = context;
        this.orderList = orders;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_item_layout, viewGroup, false);
        OrderAdapter.ViewHolder viewHolder = new OrderAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder viewHolder, int i) {

       /* Order order = orderList.get(i);

        final int id = order.getId();
        final int  clientNo = order.getClientNo();
        final String description = order.getDescription();

        viewHolder.txtOrderId.setText(String.valueOf(id));
        viewHolder.txtClientNo.setText(String.valueOf(clientNo));
        viewHolder.txtDescription.setText(description);
        viewHolder.txtTotalPrice.setText("Toplam: "+String.valueOf(order.getTotalPrice())+" TL");

        viewHolder.mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 3);
        viewHolder.mRecyclerView.setLayoutManager(mLayoutManager);
        viewHolder.mRecyclerView.setAdapter(new OrderListAdapter(context, order.getItems()));

        //viewHolder.orderListView.setAdapter(new OrderListAdapter(context, order.getItems()));

        viewHolder.completeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(context, "#" + position + " - " + String.valueOf(id) + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(context, "#" + position + " - " + title, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        RecyclerView mRecyclerView;
        //public ListView orderListView;
        public TextView txtOrderId, txtClientNo,txtDescription, txtTotalPrice;
        public Button completeOrderButton;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            //orderListView = (ListView) itemView.findViewById(R.id.order_listview);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_order_list);
            txtOrderId = (TextView) itemView.findViewById(R.id.txt_order_id);
            txtClientNo = (TextView) itemView.findViewById(R.id.txt_clientno);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_order_description);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.txt_total_order_price);

            completeOrderButton = (Button) itemView.findViewById(R.id.btn_complete_order);

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