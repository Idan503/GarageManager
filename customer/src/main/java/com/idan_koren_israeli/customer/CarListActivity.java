package com.idan_koren_israeli.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.idan_koren_israeli.common.ParentActivity;

public class CarListActivity extends ParentActivity {

    private static final String CUSTOMER_SIDE = "Customer Side";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        car_list_LBL_viewtype.setText(CUSTOMER_SIDE);
    }
}