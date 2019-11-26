package com.example.diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.common.util.Hex;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;

import static com.example.diary.AssistantUtil.getDateNow;
import static com.example.diary.AssistantUtil.getTimeNow;

public class AddDiary extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{
    EditText edtTitle, edtContent;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    ColorPicker colorPicker;
    Diary diary;
    Button btnSubmit;
    Intent mainIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_add);

        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
        btnSubmit = findViewById(R.id.btn_submit);

        diary = new Diary();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_date, R.id.navigation_time, R.id.navigation_color, R.id.navigation_delete)
                .build();
        BottomNavigationItemView navDate = (BottomNavigationItemView) findViewById(R.id.navigation_date);
        BottomNavigationItemView navTime = (BottomNavigationItemView) findViewById(R.id.navigation_time);
        BottomNavigationItemView navColor = (BottomNavigationItemView) findViewById(R.id.navigation_color);
        BottomNavigationItemView navDelete = (BottomNavigationItemView) findViewById(R.id.navigation_delete);

        // Set up date
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePickerDialog(this, AddDiary.this, yy, mm, dd);
        // Set up time
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timePicker = new TimePickerDialog(this, AddDiary.this, hour, minute,
                DateFormat.is24HourFormat(this));

       mainIntent = new Intent();



        // Set up onclick
        navDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        navTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show();
            }
        });

        navColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayColorDialog();
            }
        });

        navDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diary.setmTitle(edtTitle.getText().toString());
                diary.setmContent(edtContent.getText().toString());
                if (TextUtils.isEmpty(edtTitle.getText().toString()) || TextUtils.isEmpty(edtTitle.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Nhập đầy đủ vào", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(diary.getmDate())) {
                    diary.setmDate(getDateNow());
                }
                if (TextUtils.isEmpty(diary.getmTime())) {
                    diary.setmTime(getTimeNow());
                }
                if (TextUtils.isEmpty(diary.getmColor()))
                    diary.setmColor("#FFAA00");

                // Set up publisher
                String pub = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                diary.setmPublisher(pub);
                mainIntent.putExtra("diary", diary);
                setResult(Activity.RESULT_OK, mainIntent);
                finish();
            }
        });
    }


    private void displayColorDialog() {
        new ColorPicker(this)
                .setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        System.out.println(Integer.toHexString(color));
                        diary.setmColor("#"+Integer.toHexString(color));
                    }
                    @Override
                    public void onCancel() {
                    }
                })
                .setTitle("Chọn màu")
                .show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        System.out.println(i2+" "+i1+" "+i);
        diary.setmDate(i2+"/"+(i1+1)+"/"+(2009+i1));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        System.out.println(i+" "+i1);

        diary.setmTime(AssistantUtil.timeFormat(i, i1));
    }

}
