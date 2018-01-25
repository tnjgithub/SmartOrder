package com.hanibey.smartorder;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanibey.smartorderadapter.GridAdapter;
import com.hanibey.smartorderbusiness.LogService;
import com.hanibey.smartorderhelper.Constant;
import com.hanibey.smartordermodel.Product;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tanju on 16.09.2017.
 */

public class ProductFragment extends Fragment {

    FirebaseDatabase database ;
    DatabaseReference productRef;
    LogService logService;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private Context context;
    private String categoryKey;

    public ProductFragment(Context con, String cKey) {
        this.context = con;
        this.categoryKey = cKey;
        this.logService = new LogService();
        this.database = FirebaseDatabase.getInstance();
        this.productRef = database.getReference(Constant.Nodes.Product);
    }

    public ProductFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Calling the RecyclerView
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_meals);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), GetColomnCount());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setProductToGrid();

        return view;
    }


    private void setProductToGrid(){

        try{
            productRef.child(categoryKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Product> productList = new ArrayList<>();

                    for(DataSnapshot pos : dataSnapshot.getChildren()){

                        Product product = pos.getValue(Product.class);
                        product.Key = pos.getKey();
                        productList.add(product);
                    }

                    mAdapter = new GridAdapter(getActivity(), productList);
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("Error", "Failed to read value.", error.toException());
                }
            });
        }
        catch (Exception ex){
            Log.e("Hata", ex.toString());
        }
    }


    private  int GetColomnCount(){

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches >= 10){
            return 3;
        }
        else if (diagonalInches >= 6 && diagonalInches<10){
            return 2;
        }
        else { return  1;}
    }

}
