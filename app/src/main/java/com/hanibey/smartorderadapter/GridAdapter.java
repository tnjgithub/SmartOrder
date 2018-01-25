package com.hanibey.smartorderadapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanibey.smartorder.MainActivity;
import com.hanibey.smartorder.R;
import com.hanibey.smartorderbusiness.GeneralService;
import com.hanibey.smartorderbusiness.OrderService;
import com.hanibey.smartorderhelper.Constant;
import com.hanibey.smartordermodel.OrderItem;
import com.hanibey.smartordermodel.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    OrderService orderService;
    GeneralService generalService;

    ArrayList<Product> productList;
    static Context context;

    public GridAdapter(Context context, ArrayList<Product> products) {
        super();
        this.context = context;
        this.productList = products;
        this.orderService = new OrderService(context);
        this.generalService = new GeneralService();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final  ViewHolder holder = viewHolder;
        Product product = productList.get(i);

        final String productKey = product.Key;
        final String title = product.Title;
        final double price = product.Price;
        final String description = product.Description;
        final String imageUrl = product.ImageUrl;

        viewHolder.txtKey.setText(productKey);
        viewHolder.txtTitle.setText(title);
        viewHolder.txtDescription.setText(description);
        viewHolder.txtPrice.setText(generalService.getCurrencyFormat(price)+ " " + Constant.CURRENCY);
        Picasso.with(context).load(imageUrl).into(viewHolder.imgThumbnail);

        viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.valueOf(holder.txtQuantity.getText().toString()) + 1;
                double newPrice = quantity * price;
                holder.txtQuantity.setText(String.valueOf(quantity));
                holder.txtPrice.setText(generalService.getCurrencyFormat(newPrice) + " " + Constant.CURRENCY);
            }
        });

        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.valueOf(holder.txtQuantity.getText().toString());
                if(quantity > 1){
                    quantity--;
                    double newPrice = quantity * price;
                    holder.txtQuantity.setText(String.valueOf(quantity));
                    holder.txtPrice.setText(generalService.getCurrencyFormat(newPrice) + " " + Constant.CURRENCY);
                }

            }
        });

        viewHolder.addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantity = holder.txtQuantity.getText().toString();

               int index = getIndex(productKey);
               if(index >= 0){
                   MainActivity.getInstance().selectedOrderItems.get(index).Quantity = quantity;
               }
               else {
                   OrderItem item = new OrderItem(productKey, title, price, quantity, Constant.OrderItemStatus.Selected);
                   MainActivity.getInstance().selectedOrderItems.add(item);
               }

                orderService.updateSelectedProductList();
                // Toast.makeText(context, "Ürün güncellendi!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) holder.imgThumbnail.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                zoomProductImage(bitmap);
            }
        });

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //zoomProductImage(imageUrl);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView txtTitle,txtDescription, txtKey, txtPrice, txtQuantity;
        public Button addOrderButton;
        public ImageButton btnPlus, btnMinus;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
            txtKey = (TextView) itemView.findViewById(R.id.txt_key);
            txtPrice = (TextView) itemView.findViewById(R.id.txt_price);
            txtQuantity = (TextView) itemView.findViewById(R.id.txt_quantity);

            addOrderButton = (Button) itemView.findViewById(R.id.btn_add_order);
            btnPlus = (ImageButton) itemView.findViewById(R.id.btn_plus);
            btnMinus = (ImageButton) itemView.findViewById(R.id.btn_minus);

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

    private boolean isSelected(String productKey){
        for(OrderItem item : MainActivity.getInstance().selectedOrderItems)
        {
            if(item.ProductKey.equals(productKey)){
                return true;
            }
        }

        return false;
    }

    private int getIndex(String productKey){

       int index = -1;
        for(OrderItem item : MainActivity.getInstance().selectedOrderItems)
        {
            if(item.ProductKey.equals(productKey) && item.Status.equals(Constant.OrderItemStatus.Selected)){
                return MainActivity.getInstance().selectedOrderItems.indexOf(item);
            }
        }

        return index;
    }

    private void zoomProductImage(Bitmap btm) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.zoom_image_dialog_layout);

        ImageView img = (ImageView) dialog.findViewById(R.id.img_product);
        img.setImageBitmap(btm);
        //Picasso.with(context).load(url).into(img);

        int deviceWidth = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int deviceHeight = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);

        int width, height;

        if(btm.getWidth() > deviceWidth)
            width = deviceWidth;
        else
            width = btm.getWidth() + 10;

        if(btm.getHeight() > deviceHeight)
            height = deviceWidth;
        else
            height = btm.getHeight() + 10;

        dialog.getWindow().setLayout(width, height);

        ImageButton btnClose = (ImageButton) dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}