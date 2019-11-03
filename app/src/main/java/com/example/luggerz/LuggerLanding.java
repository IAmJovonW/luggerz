package com.example.luggerz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luggerz.Model.Lugger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


public class LuggerLanding extends AppCompatActivity {

    Button btnLuggerRegister, btnLuggerSignIn;
    RelativeLayout rootLayout;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference Luggers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugger_landing);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Luggers = db.getReference("Lugger");

        //Init View
        btnLuggerRegister = (Button) findViewById(R.id.btnLuggerRegister);
        btnLuggerSignIn = (Button) findViewById(R.id.btnLuggerSignIn);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        //Event
        btnLuggerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });

        btnLuggerSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View lugger_login_layout = inflater.inflate(R.layout.layout_lugger_login, null);

        final MaterialEditText etLuggerEmail = lugger_login_layout.findViewById(R.id.etLuggerEmail);
        final MaterialEditText etLuggerPassword = lugger_login_layout.findViewById(R.id.etLuggerPassword);

        dialog.setView(lugger_login_layout);

        //Set button
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                //Check validation
                if (TextUtils.isEmpty(etLuggerEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etLuggerPassword.getText().toString())) {
                    Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (etLuggerPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootLayout, "Password is too short!!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //Login
                auth.signInWithEmailAndPassword(etLuggerEmail.getText().toString(), etLuggerPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(LuggerLanding.this, LuggerWelcome.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        });


        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        dialog.show();
    }

    private void showRegisterDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View lugger_register_layout = inflater.inflate(R.layout.layout_lugger_register, null);

        final MaterialEditText etLuggerEmail = lugger_register_layout.findViewById(R.id.etLuggerEmail);
        final MaterialEditText etLuggerPassword = lugger_register_layout.findViewById(R.id.etLuggerPassword);
        final MaterialEditText etLuggerName = lugger_register_layout.findViewById(R.id.etLuggerName);
        final MaterialEditText etLuggerPhone = lugger_register_layout.findViewById(R.id.etLuggerPhone);

        dialog.setView(lugger_register_layout);

        //Set button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                //Check Validation
                if(TextUtils.isEmpty(etLuggerEmail.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etLuggerPhone.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Please enter phone number", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etLuggerPassword.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etLuggerName.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Please enter name", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(etLuggerPassword.getText().toString().length() <6 )
                {
                    Snackbar.make(rootLayout, "Password too short", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //Register new Lugger
                auth.createUserWithEmailAndPassword(etLuggerEmail.getText().toString(), etLuggerPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Save lugger to db
                        Lugger lugger = new Lugger();

                        lugger.setEmail(etLuggerEmail.getText().toString());
                        lugger.setPassword(etLuggerPassword.getText().toString());
                        lugger.setName(etLuggerName.getText().toString());
                        lugger.setPhone(etLuggerPhone.getText().toString());


                        //db reference
                        Luggers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(lugger).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(rootLayout, "You are now registered!", Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout, "Failed"+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout, "Failed"+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });



            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
}