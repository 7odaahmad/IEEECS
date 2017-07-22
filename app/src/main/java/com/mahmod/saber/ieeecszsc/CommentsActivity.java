package com.mahmod.saber.ieeecszsc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CommentsActivity extends AppCompatActivity {

    String address;
    String commentMember;
    String commentContent;
    boolean stop = false;
    int commentsNumber;
    private List<commentModel> comments;
    private RecyclerView.Adapter adapter;
    private RecyclerView commentsList;

    private DatabaseReference mDatabase;
    EditText comment;



    String email;
    String userName;

    boolean addComment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        address = getIntent().getExtras().getString("taskAddress");

        comments = new ArrayList<>();

        commentsList = (RecyclerView) findViewById(R.id.commentsListView);
        commentsList.setLayoutManager(new LinearLayoutManager(this));

        getTaskAndComments();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),dataSnapshot.child("Toast").getValue().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    public void getTaskAndComments(){

        final TextView taskAddress = (TextView) findViewById(R.id.commentsTaskAddress);
        final TextView taskContent = (TextView) findViewById(R.id.commentsTaskContent);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (stop) {

                } else {
                    commentsNumber = Integer.parseInt(dataSnapshot.child("Tasks Comments").child(address).child("Number").getValue().toString());
                    taskAddress.setText(dataSnapshot.child("Tasks").child(address).child("Head").getValue().toString());
                    taskContent.setText(dataSnapshot.child("Tasks").child(address).child("Desc").getValue().toString());
                    for (int i = 0; i < commentsNumber; i++) {
                        commentMember = dataSnapshot.child("Tasks Comments").child(address).child(String.valueOf("Comment"+(i+1))).child("Member").getValue().toString();
                        commentContent = dataSnapshot.child("Tasks Comments").child(address).child(String.valueOf("Comment"+(i+1))).child("Comment").getValue().toString();



                        commentModel Listitem = new commentModel(
                                commentMember,
                                commentContent
                        );
                        comments.add(Listitem);

                    }

                    adapter = new commentAdapter(comments,getBaseContext());
                    commentsList.setAdapter(adapter);


                    stop = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


    }

    public void newComment (View view){
        addComment = true;
        comment = (EditText) findViewById(R.id.comment);


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (addComment) {
                        commentsNumber = Integer.parseInt(dataSnapshot.child("Tasks Comments").child(address).child("Number").getValue().toString());
                        userName = dataSnapshot.child("Emails").child(email.split("\\.")[0]).child("User").getValue().toString();
                        mDatabase.child("Tasks Comments").child(address).child(String.valueOf("Comment" + (commentsNumber + 1))).child("Comment").setValue(comment.getText().toString());
                        mDatabase.child("Tasks Comments").child(address).child(String.valueOf("Comment" + (commentsNumber + 1))).child("Member").setValue(userName);
                        mDatabase.child("Tasks Comments").child(address).child("Number").setValue(String.valueOf(commentsNumber + 1));

                        addComment = false;
                        final android.os.Handler handler = new android.os.Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stop = false;
                                comments.clear();
                                getTaskAndComments();
                            }
                        }, 2000);
                    }else {
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }
}
