package com.mahmod.saber.ieeecszsc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by 7odaahmad on 7/21/2017.
 */

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {

    private List<commentModel> listItems;
    private Context context;

    private DatabaseReference mDatabase;

    public commentAdapter(List<commentModel> listItem, Context context) {
        this.listItems = listItem;
        this.context = context;
    }

    @Override
    public commentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_card_layout,parent,false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final commentAdapter.ViewHolder holder, final int position) {

        final commentModel listItem = listItems.get(position);


        holder.commentMember.setText(listItem.getMember());
        holder.commentContent.setText(listItem.getContent());


    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView commentMember;
        public TextView commentContent;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            commentMember = (TextView) itemView.findViewById(R.id.commentMember);
            commentContent = (TextView) itemView.findViewById(R.id.commentContent);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.commentCard);
        }
    }
}
