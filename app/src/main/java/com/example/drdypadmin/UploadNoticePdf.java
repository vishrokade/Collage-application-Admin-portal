package com.example.drdypadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class UploadNoticePdf extends AppCompatActivity {
    private CardView addpdf;
    private EditText pdftitle;
    private Button uploadpdfbutton;
    private TextView pdfTextView;
    private String pdfName,title;

    private final int REQ = 1;
    private Uri pdfData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        pd = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("NoticePdf");
        storageReference = FirebaseStorage.getInstance().getReference().child("NoticePdf");
        addpdf = findViewById(R.id.addpdf);
        pdftitle = findViewById(R.id.pdftitle);
        pdfTextView = findViewById(R.id.pdfTextView);
        uploadpdfbutton = findViewById(R.id.uploadpdfbutton);

        uploadpdfbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = pdftitle.getText().toString();
                if(title.isEmpty()){
                    pdftitle.setError("Empty");
                    pdftitle.requestFocus();
                }else if(pdfData==null){
                    Toast.makeText(UploadNoticePdf.this, "Please select pdf", Toast.LENGTH_SHORT).show();
                }else{
                    uploadPdf();
                }
            }
        });

        addpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }

        });
    }

    private void uploadPdf() {
        pd.setTitle("Please Wait...");
        pd.setMessage("Pdf Uploading...");
        pd.show();
        StorageReference reference = storageReference.child("pdf"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        uploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(UploadNoticePdf.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String downloadUrl) {
        String uniquekey = databaseReference.child("pdf").push().getKey();

        HashMap data =  new HashMap();
        data.put("pdfTitle",title);
        data.put("downloadUrl",downloadUrl);

        databaseReference.child("pdf").child(uniquekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadNoticePdf.this, "pdf uploaded successfully", Toast.LENGTH_SHORT).show();
                pdftitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadNoticePdf.this, "pdf upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select pdf category"),REQ);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode ==RESULT_OK){
            pdfData = data.getData();

            if(pdfData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = UploadNoticePdf.this.getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor != null && cursor.moveToFirst()){
                        pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();

            }
            pdfTextView.setText(pdfName);
        }
    }
}