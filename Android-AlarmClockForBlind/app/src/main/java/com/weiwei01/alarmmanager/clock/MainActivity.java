package com.weiwei01.alarmmanager.clock;

import android.content.DialogInterface;
import android.media.Ringtone;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.weiwei01.alarmmanager.clock.view.SelectRemindCyclePopup;
import com.weiwei01.alarmmanager.clock.view.SelectRemindWayPopup;
import com.weiwei01.lib.alarmmanager.clock.AlarmManagerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.weiwei01.alarmmanager.clock.R.string.ringsound;
import static com.weiwei01.alarmmanager.clock.R.string.ringeveryday;
import static com.weiwei01.alarmmanager.clock.R.string.ringonce;
import static com.weiwei01.alarmmanager.clock.R.string.vibration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView hour_choose_btn, minute_choose_btn;
    private TimePickerView pvTime;
    private RelativeLayout repeat_rl, ring_rl;
    private TextView tv_repeat_value, tv_ring_value;
    private LinearLayout allLayout;
    private Button set_btn;
    private String time;
    private int cycle;
    private int ring;
    private List<String> minutesList;
    private List<String> hoursList;
    private String hours;
    private String minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(this);
        hour_choose_btn = (TextView) findViewById(R.id.hour_choose);
        minute_choose_btn = (TextView) findViewById(R.id.minute_choose);
        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);
        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
//        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//
//            @Override
//            public void onTimeSelect(Date date) {
//                time = getTime(date);
//                hour_choose_btn.setText(time);
//            }
//        });

//        hour_choose_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                pvTime.show();
//                final String[] hours = {"1時","2時","3時","4時","5時","6時","7時","8時","9時","10時","11時","12時"};
//
//
//
//                AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
//                dialog_list.setTitle("請選擇小時");
//                dialog_list.setItems(hours, new DialogInterface.OnClickListener(){
//
//                    //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(MainActivity.this, "你選的是" + hours[which], Toast.LENGTH_SHORT).show();
//                    }
//                });
//                dialog_list.show();
//            }
//        });
        hour_choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listHours();
            }
        });

        minute_choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listMinutes();
            }
        });
    }


    private void initData() {
        minutesList = new ArrayList<>();
        for(int i = 0; i < 60; i++){
            String stringValue = Integer.toString(i);
            minutesList.add(stringValue);
        }
        hoursList = new ArrayList<>();
        for(int i = 0; i < 24; i++){
            String stringValue = Integer.toString(i);
            hoursList.add(stringValue);
        }
//        minutesList.add(getString(R.string.lunch2));
    }
    private void listMinutes(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.pleasechooseminutes))
                .setItems(minutesList.toArray(new String[minutesList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = minutesList.get(which);
                        Toast.makeText(getApplicationContext(), getString(R.string.youchose) +" "+ name + " " + getString(R.string.minutes), Toast.LENGTH_SHORT).show();
                        minutes = name;
                    }
                })
                .show();
    }
    private void listHours(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.pleasechoosehours))
                .setItems(hoursList.toArray(new String[hoursList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = hoursList.get(which);
                        Toast.makeText(getApplicationContext(), getString(R.string.youchose)  +" "+ name  +" "+ getString(R.string.hours), Toast.LENGTH_SHORT).show();
                        hours = name;
                    }
                })
                .show();
    }
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.ring_rl:
                selectRingWay();
                break;
            case R.id.set_btn:
                setClock();
                break;
            default:
                break;
        }
    }

    private void setClock() {
        String alarmring = getString(R.string.alarmring);
        time = hours + ":" + minutes;
        if (time != null && time.length() > 0) {
            String[] times = time.split(":");
            if (cycle == 0) {//是每天的闹钟
                AlarmManagerUtil.setAlarm(this, 0, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, alarmring, ring);
            } if(cycle == -1){//是只响一次的闹钟
                AlarmManagerUtil.setAlarm(this, 1, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, alarmring, ring);
            }else {//多选，周几的闹钟
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    AlarmManagerUtil.setAlarm(this, 2, Integer.parseInt(times[0]), Integer
                            .parseInt(times[1]), i, Integer.parseInt(weeks[i]), alarmring, ring);
                }
            }
            Toast.makeText(this, hours + getString(R.string.hours) + " " + minutes + getString(R.string.minutes) +" "+ getString(R.string.alarmsettingsuccessful), Toast.LENGTH_LONG).show();

        }

    }


    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText(ringeveryday);
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText(ringonce);
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        tv_ring_value.setText(vibration);
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        tv_ring_value.setText(R.string.ringsound);
                        ring = 1;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "周二";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "周三";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "周四";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "周五";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "周六";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "周日";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }

}
