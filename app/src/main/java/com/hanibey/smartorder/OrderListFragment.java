package com.hanibey.smartorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.hanibey.smartorderadapter.SelectedProductListAdapter;

/**
 * Created by Tanju on 26.09.2017.
 */

public class OrderListFragment extends Fragment {

    public OrderListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getContext(),   " oncreate ", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getContext(),   " onCreateView ", Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.fragment_orderlist, container, false);
        ListView orderListView = (ListView) view.findViewById(R.id.order_listView);

        orderListView.setAdapter(new SelectedProductListAdapter(getContext(), MainActivity.getInstance().selectedOrderItems));

        return view;
    }
}