package com.example.drdypadmin.faculty;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.drdypadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.w3c.dom.CDATASection;
import java.util.ArrayList;
import java.util.List;
public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csDepartment,aidsDepartment,otherDepartment;
    private LinearLayout csNoData, aidsNoData,otherNoData;
    private teacherAdapter adapter;
    private List<TeacherData>list1,list2,list3;
    private DatabaseReference reference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        csNoData = findViewById(R.id.csNoData);
        aidsNoData = findViewById(R.id.aidsNoData);
        csDepartment = findViewById(R.id.csDepartment);
        aidsDepartment = findViewById(R.id.aidsDepartment);
        otherNoData =findViewById(R.id.otherNoData);
        otherDepartment = findViewById(R.id.otherDepartment);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");

        csDepartment();
        aidsDepartment();
        otherDepartment();
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateFaculty.this , addTeacher.class));
            }
        });
    }

    private void csDepartment() {
        dbRef = reference.child("Computer Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else{
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot DataSnapshot: dataSnapshot.getChildren()){
                        TeacherData data = DataSnapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new teacherAdapter(list1,UpdateFaculty.this,"Computer Science");
                    csDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aidsDepartment() {
        dbRef = reference.child("AIDS");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    aidsNoData.setVisibility(View.VISIBLE);
                    aidsDepartment.setVisibility(View.GONE);
                }else{
                    aidsNoData.setVisibility(View.GONE);
                    aidsDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot DataSnapshot:dataSnapshot.getChildren()){
                        TeacherData data =DataSnapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    aidsDepartment.setHasFixedSize(true);
                    aidsDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new teacherAdapter(list2,UpdateFaculty.this,"AIDS");
                    aidsDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void otherDepartment() {
        dbRef = reference.child("Other");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    otherNoData.setVisibility(View.VISIBLE);
                    otherDepartment.setVisibility(View.GONE);
                }else{
                    otherNoData.setVisibility(View.GONE);
                    otherDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot DataSnapshot:dataSnapshot.getChildren()){
                        TeacherData data =DataSnapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    otherDepartment.setHasFixedSize(true);
                    otherDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new teacherAdapter(list3,UpdateFaculty.this,"Other");
                    otherDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(UpdateFaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}