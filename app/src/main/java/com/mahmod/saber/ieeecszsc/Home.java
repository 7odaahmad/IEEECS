package com.mahmod.saber.ieeecszsc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private DatabaseReference mDatabase;
    int feedCount;
    ArrayList<String> feeds = new ArrayList<String>();
    boolean stop = false;
    String taskText,taskHead,taskAddress;




    private RecyclerView list2;
    private RecyclerView.Adapter adapter;
    private List<TaskItem> Tasks;
    private ProgressDialog dialog;

    int backStatus;






    String email;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        mDatabase = FirebaseDatabase.getInstance().getReference();




        list2 = (RecyclerView) findViewById(R.id.feedlist2);
        list2.setLayoutManager(new LinearLayoutManager(this));
        Tasks = new ArrayList<>();




        email = getIntent().getExtras().getString("email");


        feed();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if (backStatus == 0){
                Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_LONG).show();
                backStatus = 1;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backStatus = 0;
                    }
                },2000);
            }else if (backStatus == 1){
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    private void feed() {



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (stop) {

                } else {
                    feedCount = Integer.parseInt(dataSnapshot.child("Tasks").child("Number").getValue().toString());
                    for (int i = feedCount; i > 0; i--) {
                        taskHead = dataSnapshot.child("Tasks").child(String.valueOf("Task"+i)).child("Head").getValue().toString();
                        taskText = dataSnapshot.child("Tasks").child(String.valueOf("Task"+i)).child("Desc").getValue().toString();
                        taskAddress = dataSnapshot.child("Tasks").child(String.valueOf("Task"+i)).child("Address").getValue().toString();



                        TaskItem Listitem = new TaskItem(
                                taskHead,
                                taskText,
                                taskAddress
                        );
                        Tasks.add(Listitem);

                    }

                    adapter = new TaskAdapeter(Tasks,getBaseContext());
                    list2.setAdapter(adapter);


                    stop = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


    }


    public void refresh (View view) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Tasks.clear();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stop = false;
                feed();
                dialog.dismiss();
            }
        }, 1000);
    }

    public void add (View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);



        LayoutInflater inflater = getLayoutInflater();
        View addTaskDialog = inflater.inflate(R.layout.add_task_dialog,null);
        final EditText newtaskHead = (EditText) addTaskDialog.findViewById(R.id.addtaskhead);
        final EditText newtaskDesc = (EditText) addTaskDialog.findViewById(R.id.addtaskdesc);

        builder.setTitle("New Task");
        builder.setView(addTaskDialog);
        builder.setPositiveButton("Write", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mDatabase.child("Tasks").child("Task"+(feedCount+1)).child("Head").setValue(newtaskHead.getText().toString());
                mDatabase.child("Tasks").child("Task"+(feedCount+1)).child("Desc").setValue(newtaskDesc.getText().toString());
                mDatabase.child("Tasks").child("Task"+(feedCount+1)).child("Address").setValue("Task"+(feedCount+1));
                mDatabase.child("Tasks Comments").child("Task"+(feedCount+1)).child("Number").setValue("0");


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mDatabase.child("Feed").child("Feed"+(feedCount+1)).setValue(task.getText().toString());
                        mDatabase.child("Tasks").child("Number").setValue(feedCount+1);
                        Tasks.clear();
                        stop = false;
                        feed();
                    }
                }, 2000);



            }
        });
        builder.show();

    }

}
