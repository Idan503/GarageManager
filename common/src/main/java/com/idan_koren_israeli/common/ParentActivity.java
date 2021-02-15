package com.idan_koren_israeli.common;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.classy.timelogger.MyTimeLogger;
import com.classy.timelogger.TLog;
import com.idan_koren_israeli.common.retrofit.GarageDataController;
import com.idan_koren_israeli.common.retrofit.OnGarageDataLoadedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ParentActivity extends AppCompatActivity {

    protected TextView car_list_LBL_title;
    protected TextView car_list_LBL_open;
    protected TextView car_list_LBL_address;
    protected ListView car_list_LV_list;
    protected TextView car_list_LBL_viewtype;
    protected TextView car_list_LBL_usage_time;

    protected FrameLayout car_list_LAY_loading;

    private long startTimeStamp;

    private static final String GARAGE_KEY = "WypPzJCt";

    private static final String SQL_LOG_TAG = "activity_list_time";
    private static final String TOTAL_USAGE_TIME_TITLE = "Total Usage Time: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);


        findViews();

        downloadGarageData();
        showLoading();

        showTotalUsage();
    }


    private void downloadGarageData() {
        new GarageDataController().fetchGarageData(GARAGE_KEY, new OnGarageDataLoadedListener() {
            @Override
            public void onGarageDataLoaded(GarageData data) {
                updateData(data);
            }
        });
    }

    private void updateData(GarageData data) {
        car_list_LBL_title.setText(data.getName());
        car_list_LBL_open.setText(data.isOpen() ? "Open" : "Closed");
        car_list_LBL_address.setText(data.getAddress());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                data.getCars()
        );

        car_list_LV_list.setAdapter(arrayAdapter);

        hideLoading();

    }

    private void showLoading() {
        car_list_LAY_loading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        car_list_LAY_loading.setVisibility(View.INVISIBLE);
    }

    private void findViews() {
        car_list_LBL_title = findViewById(R.id.car_list_LBL_title);
        car_list_LBL_open = findViewById(R.id.car_list_LBL_open);
        car_list_LBL_address = findViewById(R.id.car_list_LBL_address);
        car_list_LV_list = findViewById(R.id.car_list_LV_list);
        car_list_LAY_loading = findViewById(R.id.car_list_LAY_loading);
        car_list_LBL_viewtype = findViewById(R.id.car_list_LBL_viewtype);
        car_list_LBL_usage_time = findViewById(R.id.car_list_LBL_usage_time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimeStamp = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long duration = System.currentTimeMillis() - startTimeStamp;
        MyTimeLogger.getInstance().addTlogTime(SQL_LOG_TAG, (int) duration);
    }


    private void showTotalUsage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyTimeLogger.getInstance().getTotalDuration(SQL_LOG_TAG, new MyTimeLogger.CallBack_Time() {
                    @Override
                    public void dataReady(long total) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplayTotalTime(total);
                                timerUpdateUsageTime(new long[]{total});
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void timerUpdateUsageTime(long[] startTotalTime) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startTotalTime[0] += 1000;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDisplayTotalTime(startTotalTime[0]);
                    }
                });
            }

        }, 1000, 1000);//Update text every second
    }

    @SuppressLint("SetTextI18n")
    private void updateDisplayTotalTime(long millis){

        car_list_LBL_usage_time.setText(TOTAL_USAGE_TIME_TITLE + "\n" + convertMillisToTime(millis));
    }

    private String convertMillisToTime(long millis) {
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
        if (minutes == 0)
            return seconds + " Seconds";
        if (hours == 0)
            return minutes + " Minutes, " + seconds + " Seconds";
        return hours + " Hours, " + minutes + " Minutes, " + seconds + " Seconds";

    }

}
