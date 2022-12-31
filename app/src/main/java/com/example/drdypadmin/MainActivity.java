package com.example.drdypadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.drdypadmin.Notice.DeleteNoticeActivity;
import com.example.drdypadmin.faculty.UpdateFaculty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView UploadNotice,galleryimage,addebook,addfaculty,deletenotice,noticePdf;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UploadNotice = findViewById(R.id.addnotice);
        galleryimage = findViewById(R.id.galleryimage);
        addebook = findViewById(R.id.addebook);
        addfaculty = findViewById(R.id.addfaculty);
        deletenotice = findViewById(R.id.deletenotice);
        noticePdf = findViewById(R.id.noticePdf);
        noticePdf.setOnClickListener(this);
        UploadNotice.setOnClickListener(this);
        galleryimage.setOnClickListener(this);
        addebook.setOnClickListener(this);
        addfaculty.setOnClickListener(this);
        deletenotice.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
    switch(view.getId()){
        case R.id.addnotice:
            Intent intent = new Intent(MainActivity.this, com.example.drdypadmin.Notice.UploadNotice.class);
            startActivity(intent);
            break;
        case R.id.galleryimage:
            intent = new Intent(MainActivity.this, UploadGalleryImage.class);
            startActivity(intent);
            break;
        case R.id.addebook:
            intent = new Intent(MainActivity.this, UploadPdf.class);
            startActivity(intent);
            break;
        case R.id.addfaculty:
            intent = new Intent(MainActivity.this, UpdateFaculty.class);
            startActivity(intent);
            break;
        case R.id.deletenotice:
            intent = new Intent(MainActivity.this, DeleteNoticeActivity.class);
            startActivity(intent);
            break;
        case R.id.noticePdf:
            intent = new Intent(MainActivity.this,UploadNoticePdf.class);
            startActivity(intent);
            break;
    }
    }
}