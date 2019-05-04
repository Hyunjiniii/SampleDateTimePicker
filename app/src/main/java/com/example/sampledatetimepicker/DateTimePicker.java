package com.example.sampledatetimepicker;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;

public class DateTimePicker extends LinearLayout {

    // DatePicker와 TimePicker의 이벤트를 하나의 이벤트로 처리하기위한 리스너
    public static interface OnDateTimeChangedListener {
        void onDateTimeChanged(DateTimePicker view, int year, int monthOfYear, int dayOfYear, int hourOfDay, int minute);
    }

    private OnDateTimeChangedListener listener;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox enableTimeCheckBox;

    public DateTimePicker (Context context) {
        super (context);
        init(context);

    }
    public DateTimePicker (Context context, AttributeSet attrs) {
        super (context, attrs);
        init(context);
    }

    private void init (Context context) {
        // XML 레이아웃 inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.picker, this, true);

        // 시간 정보 참조
        Calendar calendar = (Calendar.getInstance());
        final int curYear = calendar.get(Calendar.YEAR);
        final int curMonth = calendar.get(Calendar.MONTH);
        final int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int curMinute = calendar.get(Calendar.MINUTE);

        // 날짜 선택 위젯 초기화
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.init(curYear, curMonth, curDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                // 새로 정의한 리스너로 전달
                // getHour(), getMinute() 메소드는 API 23부터 지원
                if (listener != null) {
                    listener.onDateTimeChanged(DateTimePicker.this, year, monthOfYear, dayOfMonth,
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                }
            }
        });

        // 체크박스 이벤트 처리
        enableTimeCheckBox = (CheckBox) findViewById(R.id.enableTimeCheckBox);
        enableTimeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                timePicker.setEnabled(isChecked);
                timePicker.setVisibility((enableTimeCheckBox).isChecked()? View.VISIBLE : View.INVISIBLE);
            }
        });

        //시간 선택 위젯 이벤트 처리
        // 날짜변경 이벤트 발생 시 새로운 리스너의 메소드 호출
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                if (listener != null) {
                    listener.onDateTimeChanged(DateTimePicker.this, datePicker.getYear(), datePicker.getMonth(),
                            datePicker.getDayOfMonth(), hourOfDay, minute);
                }
            }
        });

        timePicker.setCurrentHour(curHour);
        timePicker.setCurrentMinute(curMinute);
        timePicker.setEnabled(enableTimeCheckBox.isChecked());
        timePicker.setVisibility((enableTimeCheckBox).isChecked()? View.VISIBLE : View.INVISIBLE);
    }

    // 새로운 리스터 객체 설정 메소드
    public void setOnDateTimeChangedListener (OnDateTimeChangedListener dateTimeListener) {
        this.listener = dateTimeListener;
    }

    public void updateDateTime (int year, int monthOfYear, int dayOfMonth, int currentHour, int currentMinute) {
        datePicker.updateDate(year, monthOfYear, dayOfMonth);
        timePicker.setCurrentHour(currentHour);
        timePicker.setCurrentMinute(currentMinute);
    }

}
