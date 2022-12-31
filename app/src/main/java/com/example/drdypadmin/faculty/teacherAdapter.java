package com.example.drdypadmin.faculty;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drdypadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class teacherAdapter extends RecyclerView.Adapter<teacherAdapter.teacherViewAdopter> {

    private List<TeacherData> list;
    private Context context;
    private String category;

    public teacherAdapter(List<TeacherData> list, Context context,String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public teacherViewAdopter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faculty_item_layout,parent,false);
        return new teacherViewAdopter(view);

    }

    @Override
    public void onBindViewHolder(@NonNull teacherViewAdopter holder, int position) {

        TeacherData item = list.get(position);
        try {
            Picasso.get().load(item.getImage()).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.post.setText(item.getPost());
        holder.subject.setText(item.getSubject());


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UpdateTeacherActivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("post",item.getPost());
                intent.putExtra("subject",item.getSubject());
                intent.putExtra("image",item.getImage());
                intent.putExtra("key",item.getKey());
                intent.putExtra("category",category);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class teacherViewAdopter extends RecyclerView.ViewHolder {
        private TextView name,email,post,subject;
        private ImageView imageView;
        private Button update;

        public teacherViewAdopter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teacherName);
            email =itemView.findViewById(R.id.teacherEmail);
            post = itemView.findViewById(R.id.teacherPost);
            subject=itemView.findViewById(R.id.teacherSub);
            imageView=itemView.findViewById(R.id.teacherImage);
            update =itemView.findViewById(R.id.teacherUpdate);
        }
    }
}
