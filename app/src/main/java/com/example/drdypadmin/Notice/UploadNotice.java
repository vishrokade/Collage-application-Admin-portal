package com.example.drdypadmin.Notice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.drdypadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadNotice extends AppCompatActivity {

    private CardView addnotice;
    private ImageView noticeimageview;
    private EditText noticetitle;
    private Button uploadnoticebutton;

    private final int REQ = 1;
    private Bitmap bitmap;
    private DatabaseReference reference,dbref;
    private StorageReference storageReference;
    String downloadUrl;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);

        pd = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("Announcement");
        storageReference = FirebaseStorage.getInstance().getReference().child("Announcement");
        addnotice = findViewById(R.id.addnotice);
        noticeimageview = findViewById(R.id.noticeimageview);
        noticetitle = findViewById(R.id.noticetitle);
        uploadnoticebutton = findViewById(R.id.uploadnoticebutton);
        uploadnoticebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noticetitle.getText().toString().isEmpty()){
                    noticetitle.setError("Empty");
                    noticetitle.requestFocus();
                }
                else if(bitmap == null){
                    uploadData();
                }else{
                    uploadImage();
                }
                
            }
        });

        addnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }

        });
    }

    private void uploadImage() {
        pd.setTitle("Please wait.");
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("Announcement").child(finalimg+"jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UploadNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();

                                }
                            });
                        }
                    });
                }else{
                    pd.dismiss();
                    Toast.makeText(UploadNotice.this, "Image not Uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        dbref = reference.child("Announcement");
        final String uniqueKey = dbref.push().getKey();

        String title = noticetitle.getText().toString();

        Calendar CalForDate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MM-yyyy");
        String date = currentdate.format(CalForDate.getTime());

        Calendar CalForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh-mm a");
        String time = currentTime.format(CalForTime.getTime());

        NoticeData noticedata = new NoticeData(title,downloadUrl,date,time,uniqueKey);

        dbref.child(uniqueKey).setValue(noticedata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(UploadNotice.this, "Announcement Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadNotice.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent PickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(PickImage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode ==RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            noticeimageview.setImageBitmap(bitmap);
        }
    }
}