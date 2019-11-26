package com.example.diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.common.util.Hex;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;

import petrov.kristiyan.colorpicker.ColorPicker;

import static com.example.diary.MainActivity.diaryRef;

public class EditDiary extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{
    EditText edtTitle, edtContent;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    ColorPicker colorPicker;
    Diary oldDiary, newDiary;
    Button btnSubmit;
    Intent mainIntent;
    ListView lvHistory;
    MyAdapterHistory adapter;
    ArrayList<History> listHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_edit);

        lvHistory = findViewById(R.id.lv_history);
        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
        btnSubmit = findViewById(R.id.btn_submit);

        mainIntent = getIntent();
        if (mainIntent != null) {
            newDiary = (Diary) mainIntent.getSerializableExtra("diary");
            edtTitle = findViewById(R.id.edt_title);
            edtContent = findViewById(R.id.edt_content);

            edtTitle.setText(newDiary.getmTitle());
            edtContent.setText(newDiary.getmContent());
        }
        oldDiary = new Diary();
        oldDiary.getValue(newDiary);

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
        datePicker = new DatePickerDialog(this, EditDiary.this, yy, mm, dd);
        // Set up time
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timePicker = new TimePickerDialog(this, EditDiary.this, hour, minute,
                DateFormat.is24HourFormat(this));



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
                int pos = getIntent().getIntExtra("pos", -1);
                if (pos != -1) {
                    // Delete on Firebase with ID key
                    deleteDiaryFirebase(diaryRef, newDiary.getId());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });


        mainIntent = new Intent(this, MainActivity.class);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newDiary.setmTitle(edtTitle.getText().toString());
                newDiary.setmContent(edtContent.getText().toString());
                System.out.println(oldDiary);
                System.out.println(newDiary);
                mainIntent.putExtra("edit", editContent(oldDiary, newDiary));
               mainIntent.putExtra("diaryEdit", newDiary);
                startActivity(mainIntent);
            }
        });



        /// Show history list
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listHistory = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot
                        .child(newDiary.getId()).child("history").getChildren()) {
                    String id = ds.getKey();
                    String content = ds.child("content").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    String publisher = ds.child("publisher").getValue(String.class);

                    listHistory.add(new History(id, date, time, content, publisher));
                }
                adapter = new MyAdapterHistory(getApplicationContext(), listHistory);
                lvHistory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        diaryRef.addListenerForSingleValueEvent(eventListener);
    }

    private boolean deleteDiaryFirebase(DatabaseReference diaryRef, String id) {

        //removing artist
        diaryRef.child(id).removeValue();
        Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
        return true;
    }

    private void displayColorDialog() {
        new ColorPicker(this)
                .setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        System.out.println(Integer.toHexString(color));
                        newDiary.setmColor("#"+Integer.toHexString(color));
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
        newDiary.setmDate(i2+"/"+(i1+1)+"/"+(2009+i1));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        System.out.println(i+" "+i1);
        newDiary.setmTime(AssistantUtil.timeFormat(i, i1));
    }


    public String editContent(Diary oldDiary, Diary newDiary) {
        String content = "";
        System.out.println(oldDiary);
        System.out.println(newDiary);
        if (!oldDiary.getmTitle().equals(newDiary.getmTitle())) {
            content+= "Change title to '"+newDiary.getmTitle()+"'\n";
        }
        if (!oldDiary.getmContent().equals(newDiary.getmContent())) {
            content+= "Change content to '" +newDiary.getmContent()+"'\n";
        }

        if (!oldDiary.getmDate().equals(newDiary.getmDate())) {
            content+= "Change date to " +newDiary.getmDate()+"\n";
        }
        if (!oldDiary.getmTime().equals(newDiary.getmTime())) {
            content+= "Change time to " +newDiary.getmTime()+"\n";
        }

        return content;
    }
}
