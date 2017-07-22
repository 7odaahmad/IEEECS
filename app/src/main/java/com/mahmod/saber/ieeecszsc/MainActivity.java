package com.mahmod.saber.ieeecszsc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    ProgressDialog dialog,splash;


    RelativeLayout Feed, Login;


    SharedPreferences prefs;

    int exit = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String email = prefs.getString("email", " ");
        String pass = prefs.getString("pass", " ");


        mAuth = FirebaseAuth.getInstance();
        Login = (RelativeLayout) findViewById(R.id.Login);
        Feed = (RelativeLayout) findViewById(R.id.Feed);

        splash = new ProgressDialog(this);
        splash.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        splash.setMessage("Signing in...");
        splash.show();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        };
        if (exit == 1) {
            finish();

        } else {
            if (email.length() > 1) {


                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent home = new Intent(getApplicationContext(), Home.class);
                                    home.putExtra("email",email);
                                    startActivity(home);
//                            Login.setVisibility(View.GONE);
//                            Feed.setVisibility(View.VISIBLE);
//                            feed();
                                    splash.dismiss();
                                    exit = 1;

                                }
                                if (!task.isSuccessful()) {
                                    splash.dismiss();
                                }

                                // ...
                            }
                        });

            } else {
                splash.dismiss();
            }
        }
    }

    public void signin(View view) {
        final EditText email = (EditText) findViewById(R.id.Email);
        final EditText password = (EditText) findViewById(R.id.pass);


        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("email",email.getText().toString());
                            editor.putString("pass",password.getText().toString());
                            editor.apply();

                            exit = 1;

                            Intent home = new Intent(getApplicationContext(),Home.class);
                            home.putExtra("email",email.getText().toString());
                            startActivity(home);
//                            Login.setVisibility(View.GONE);
//                            Feed.setVisibility(View.VISIBLE);
//                            feed();

                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Wrong password or email", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }


    public void launch(){

    }


}
