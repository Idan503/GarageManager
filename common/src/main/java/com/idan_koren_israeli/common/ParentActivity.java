package com.idan_koren_israeli.common;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.idan_koren_israeli.common.retrofit_load.GarageDataController;
import com.idan_koren_israeli.common.retrofit_load.OnGarageDataLoadedListener;

import java.util.ArrayList;


public class ParentActivity extends AppCompatActivity {

    protected TextView car_list_LBL_title;
    protected TextView car_list_LBL_open;
    protected TextView car_list_LBL_address;
    protected ListView car_list_LV_list;

    private ArrayList<String> carsAdapter = new ArrayList<>();

    private static final String GARAGE_KEY = "WypPzJCt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        car_list_LBL_title = findViewById(R.id.car_list_LBL_title);
        car_list_LBL_open = findViewById(R.id.car_list_LBL_open);
        car_list_LBL_address = findViewById(R.id.car_list_LBL_address);
        car_list_LV_list = findViewById(R.id.car_list_LV_list);


        downloadGarageData();

    }


    private void downloadGarageData(){
        new GarageDataController().fetchGarageData(GARAGE_KEY, new OnGarageDataLoadedListener() {
            @Override
            public void onGarageDataLoaded(GarageData data) {
                updateData(data);
            }
        });
    }

    private void updateData(GarageData data){

        car_list_LBL_title.setText(data.getName());
        car_list_LBL_open.setText(data.isOpen() ? "Open" : "Closed");
        car_list_LBL_address.setText(data.getAddress());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                data.getCars()
        );

        car_list_LV_list.setAdapter(arrayAdapter);


    }
}
