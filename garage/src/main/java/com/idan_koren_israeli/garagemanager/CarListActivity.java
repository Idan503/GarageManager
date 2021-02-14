package com.idan_koren_israeli.garagemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.idan_koren_israeli.common.ParentActivity;

public class CarListActivity extends ParentActivity {

    private static final String GARAGE_SIDE = "Garage Side";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        car_list_LBL_viewtype.setText(GARAGE_SIDE);
    }
}