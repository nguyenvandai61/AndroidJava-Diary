package com.example.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Button btnLogout;
    private TextView tvUsername;
    static private ListView lvDiary;
    static private ArrayList<Diary> list;
    static MyAdapter adapter;
    static FirebaseListAdapter<String> firebaseAdapter;
    static DatabaseReference dbref;
    static DatabaseReference diaryRef;
    static ValueEventListener eventListener;
    Diary diary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvUsername = findViewById(R.id.tv_username);
        lvDiary = findViewById(R.id.lvDiary);

        tvUsername.setText("Chào "+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"!");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbref = FirebaseDatabase.getInstance()
                .getReference();
        diaryRef = dbref.child("diary");


        diary = (Diary) getIntent().getSerializableExtra("diaryEdit");
        if (diary != null) {
            adapter = new MyAdapter(this, list);
            // Firebase update
           updateDiaryFirebase(diaryRef, diary);
           diary = null;
        }
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });



        if (list == null) {
            list = new ArrayList<>();
            adapter = new MyAdapter(this, list);
            lvDiary.setAdapter(adapter);
        }



              // Retrieve data from Firebase
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getKey();
                    String color = ds.child("color").getValue(String.class);
                    String content = ds.child("content").getValue(String.class);
                    String title = ds.child("title").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    String publisher = ds.child("publisher").getValue(String.class);

                    list.add(new Diary(id, date, time, title, content, color, publisher));
                }
                adapter = new MyAdapter(getApplicationContext(), list);
                lvDiary.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        diaryRef.addListenerForSingleValueEvent(eventListener);





        // Tool page
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddDiary.class);
                startActivityForResult(intent, 1);
            }
        });


    }

    private void updateDiaryFirebase(DatabaseReference diaryRef, Diary diary) {
        DatabaseReference mRef =  diaryRef.child(diary.getId());
        System.out.println("Tien hanh update");
        // Retrieve data from Firebase

        String content = getIntent().getStringExtra("edit");


        mRef.child("date").setValue(diary.getmDate());
        mRef.child("time").setValue(diary.getmTime());
        mRef.child("color").setValue(diary.getmColor());
        mRef.child("title").setValue(diary.getmTitle());
        mRef.child("content").setValue(diary.getmContent());

        System.out.println(content);
        if (!content.equals("")) {
            System.out.println("Vào edit History");
            // Make history item add
            String userUpdate = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String hId = mRef.child("history").push().getKey();
            DatabaseReference hItemRef = mRef.child("history").child(hId);
            hItemRef.child("id").setValue(hId);
            hItemRef.child("publisher").setValue(userUpdate);
            hItemRef.child("date").setValue(AssistantUtil.getDateNow());
            hItemRef.child("time").setValue(AssistantUtil.getTimeNow());
            hItemRef.child("content").setValue(content);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Diary result= (Diary) data.getSerializableExtra("diary");
//                list.add(result);
                addDiaryFirebase(diaryRef, result);
                System.out.println(result);
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }

    private void addDiaryFirebase(DatabaseReference diaryRef, Diary diary) {
        String id = diaryRef.push().getKey();
        System.out.println(id);
        DatabaseReference mRef =  diaryRef.child(id);
        mRef.child("id").setValue(id);
        mRef.child("date").setValue(diary.getmDate());
        mRef.child("time").setValue(diary.getmTime());
        mRef.child("color").setValue(diary.getmColor());
        mRef.child("title").setValue(diary.getmTitle());
        mRef.child("content").setValue(diary.getmContent());
        mRef.child("publisher").setValue(diary.getmPublisher());

        // Make history item add
        String userAdd = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String hId = mRef.child("history").push().getKey();
        DatabaseReference hItemRef = mRef.child("history").child(hId);
        hItemRef.child("id").setValue(hId);
        hItemRef.child("publisher").setValue(userAdd);
        hItemRef.child("date").setValue(AssistantUtil.getDateNow());
        hItemRef.child("time").setValue(AssistantUtil.getTimeNow());
        hItemRef.child("content").setValue("Created '"+diary.getmTitle()+"'");

        lvDiary.setAdapter(adapter);
        diaryRef.addListenerForSingleValueEvent(eventListener);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


}
