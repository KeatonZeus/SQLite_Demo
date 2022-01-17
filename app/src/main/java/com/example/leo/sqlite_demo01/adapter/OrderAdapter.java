package com.example.leo.sqlite_demo01.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leo.sqlite_demo01.R;
import com.example.leo.sqlite_demo01.model.Order;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    Activity activity;
    List<Order> listOrder;
    LayoutInflater layoutInflater;

    EditText edt_Id, edt_Name, edt_Quantity, edt_Price;

    public OrderAdapter(Activity activity, List<Order> listOrder, EditText edt_Id, EditText edt_Name, EditText edt_Quantity, EditText edt_Price) {
        this.activity = activity;
        this.listOrder = listOrder;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.edt_Id = edt_Id;
        this.edt_Name = edt_Name;
        this.edt_Quantity = edt_Quantity;
        this.edt_Price = edt_Price;
    }

    @Override//多少個
    public int getCount() {
        return listOrder.size();
    }

    @Override//單項內容
    public Object getItem(int position) {
        return listOrder.get(position);
    }

    @Override//單項ID
    public long getItemId(int position) {
        return listOrder.get(position).getProductId();
    }

    @Override//顯示
    public View getView(int position, View convertView, ViewGroup parent) {
        View row_View;
        row_View = layoutInflater.inflate(R.layout.list_row, null);
        final TextView txt_Id, txt_Name, txt_Quantity, txt_Price;
        //各個單項上的txt
        //ViewHolder
        txt_Id = (TextView) row_View.findViewById(R.id.txt_Id);
        txt_Name = (TextView) row_View.findViewById(R.id.txt_Name);
        txt_Quantity = (TextView) row_View.findViewById(R.id.txt_Quantity);
        txt_Price = (TextView) row_View.findViewById(R.id.txt_Price);

        //BindView
        txt_Id.setText("" + listOrder.get(position).getProductId());
        txt_Name.setText("" + listOrder.get(position).getProductName());
        txt_Quantity.setText("" + listOrder.get(position).getQuantity());
        txt_Price.setText("" + listOrder.get(position).getPrice());

        row_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_Id.setText("" + txt_Id.getText());
                edt_Name.setText("" + txt_Name.getText());
                edt_Quantity.setText("" + txt_Quantity.getText());
                edt_Price.setText("" + txt_Price.getText());
            }
        });

        return row_View;
    }
}
