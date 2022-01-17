package com.example.leo.sqlite_demo01;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.leo.sqlite_demo01.adapter.OrderAdapter;
import com.example.leo.sqlite_demo01.database.DataBaseHelper;
import com.example.leo.sqlite_demo01.model.Order;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edt_Id, edt_Name, edt_Quantity, edt_Price;
    private Button btn_Add, btn_Update, btn_Delete;
    private ListView list;

    private List<Order> data = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseHelper = DataBaseHelper.getInstance(this);

        initView();

        //Load Data
        refreshData();

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order(Integer.parseInt(edt_Id.getText().toString()), edt_Name.getText().toString(), edt_Quantity.getText().toString(), edt_Price.getText().toString());
                dataBaseHelper.addOrUpdateOrder(order);
                refreshData();

                //hide soft-keyboard
                hideKeyboard(v);
            }
        });

        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order(Integer.parseInt(edt_Id.getText().toString()), edt_Name.getText().toString(), edt_Quantity.getText().toString(), edt_Price.getText().toString());
                dataBaseHelper.updateOrder(order);
                refreshData();

                //hide soft-keyboard
                hideKeyboard(v);
            }
        });

        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order(Integer.parseInt(edt_Id.getText().toString()), edt_Name.getText().toString(), edt_Quantity.getText().toString(), edt_Price.getText().toString());
                dataBaseHelper.deleteOrder(order);
                refreshData();

                //hide soft-keyboard
                hideKeyboard(v);
            }
        });
    }

    private void refreshData() {
        data = dataBaseHelper.getAllOrder();
        OrderAdapter orderAdapter = new OrderAdapter(MainActivity.this, data, edt_Id, edt_Name, edt_Quantity, edt_Price);
        list.setAdapter(orderAdapter);
    }

    private void hideKeyboard(View v) {
        //hide soft-keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void initView() {
        btn_Add = (Button) findViewById(R.id.btn_Add);
        btn_Update = (Button) findViewById(R.id.btn_Update);
        btn_Delete = (Button) findViewById(R.id.btn_Delete);
        list = (ListView) findViewById(R.id.list);

        edt_Id = (EditText) findViewById(R.id.edt_Id);
        edt_Name = (EditText) findViewById(R.id.edt_Name);
        edt_Quantity = (EditText) findViewById(R.id.edt_Quantity);
        edt_Price = (EditText) findViewById(R.id.edt_Price);
    }
}
