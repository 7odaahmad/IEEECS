package com.mahmod.saber.ieeecszsc;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by 7odaahmad on 7/19/2017.
 */

public class TaskAdapeter extends RecyclerView.Adapter<TaskAdapeter.ViewHolder> {


    private List<TaskItem> listItems;
    private Context context;

    private DatabaseReference mDatabase;

    public TaskAdapeter(List<TaskItem> listItem, Context context) {
        this.listItems = listItem;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items,parent,false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final TaskItem listItem = listItems.get(position);
        final String[] taskAddress = new String[1];



        holder.taskHead.setText(listItem.getHead());
        holder.taskDesc.setText(listItem.getDesc());
        holder.taskAddress.setText(listItem.getAddress());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                taskAddress[0] = listItem.getAddress();

                Intent i = new Intent(context,CommentsActivity.class);
                i.putExtra("taskAddress",taskAddress[0]);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView taskHead;
        public TextView taskDesc;
        public TextView taskAddress;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            taskHead = (TextView) itemView.findViewById(R.id.taskHead);
            taskDesc = (TextView) itemView.findViewById(R.id.taskDesc);
            taskAddress = (TextView) itemView.findViewById(R.id.taskAddress);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.taskCard);
        }
    }
}
