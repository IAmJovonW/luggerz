package com.example.luggerz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.luggerz.Model.Patron;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;

public class PatronMainActivity extends AppCompatActivity {

    Button btnPatronrRegister, btnPatronSignIn;
    RelativeLayout rootPatronLayout;
    public AlertDialog waitingDialog; // let this be public

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference Patrons;

    private final static int PERMISSION = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patron_landing);



        //Init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Patrons = db.getReference("Patron");

        //Init View
        btnPatronrRegister = (Button) findViewById(R.id.btnPatronRegister);
        btnPatronSignIn = (Button) findViewById(R.id.btnPatronSignIn);
        rootPatronLayout = (RelativeLayout) findViewById(R.id.rootPatronLayout);

        //Event
        btnPatronrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });

        btnPatronSignIn.setOnClickListener(new View.OnClickListener() {
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
        View patron_login_layout = inflater.inflate(R.layout.layout_patron_login, null);

        final MaterialEditText etPatronEmail = patron_login_layout.findViewById(R.id.etPatronEmail);
        final MaterialEditText etPatronPassword = patron_login_layout.findViewById(R.id.etPatronPassword);

        dialog.setView(patron_login_layout);

        //Set button
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


                //Set disable button Sign In if processing
                btnPatronSignIn.setEnabled(false);



                //Check validation
                if (TextUtils.isEmpty(etPatronEmail.getText().toString())) {
                    Snackbar.make(rootPatronLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etPatronPassword.getText().toString())) {
                    Snackbar.make(rootPatronLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (etPatronPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootPatronLayout, "Password is too short!!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(PatronMainActivity.this).build();
                waitingDialog.show();


                //Login
                auth.signInWithEmailAndPassword(etPatronEmail.getText().toString(), etPatronPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();
                                //TODO: Need Patron Welcome
                                startActivity(new Intent(PatronMainActivity.this, PatronHome.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootPatronLayout, "Failed" + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                        //Active button
                        btnPatronSignIn.setEnabled(true);
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
        View patron_register_layout = inflater.inflate(R.layout.layout_patron_register, null);

        final MaterialEditText etPatronEmail = patron_register_layout.findViewById(R.id.etPatronEmail);
        final MaterialEditText etPatronPassword = patron_register_layout.findViewById(R.id.etPatronPassword);
        final MaterialEditText etPatronName = patron_register_layout.findViewById(R.id.etPatronName);
        final MaterialEditText etPatronPhone = patron_register_layout.findViewById(R.id.etPatronPhone);

        dialog.setView(patron_register_layout);

        //Set button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                //Check Validation
                if(TextUtils.isEmpty(etPatronEmail.getText().toString()))
                {
                    Snackbar.make(rootPatronLayout, "Please enter email address", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etPatronPhone.getText().toString()))
                {
                    Snackbar.make(rootPatronLayout, "Please enter phone number", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etPatronPassword.getText().toString()))
                {
                    Snackbar.make(rootPatronLayout, "Please enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etPatronName.getText().toString()))
                {
                    Snackbar.make(rootPatronLayout, "Please enter name", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(etPatronPassword.getText().toString().length() <6 )
                {
                    Snackbar.make(rootPatronLayout, "Password too short", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //Register new Patron
                auth.createUserWithEmailAndPassword(etPatronEmail.getText().toString(), etPatronPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //TODO: Need class for patron db objects
                        //Save patron to db
                        Patron patron = new Patron();

                        patron.setEmail(etPatronEmail.getText().toString());
                        patron.setPassword(etPatronPassword.getText().toString());
                        patron.setName(etPatronName.getText().toString());
                        patron.setPhone(etPatronPhone.getText().toString());


                        //db reference
                        Patrons.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(patron).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(rootPatronLayout, "You are now registered!", Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootPatronLayout, "Failed"+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootPatronLayout, "Failed"+e.getMessage(), Snackbar.LENGTH_SHORT).show();
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
