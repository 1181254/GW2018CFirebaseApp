package com.auribises.myfirebaseapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auribises.myfirebaseapp.R;
import com.auribises.myfirebaseapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{


    // Documentation Link: https://firebase.google.com/docs/auth/android/start/?authuser=0

    EditText eTxtName, eTxtEmail, eTxtPassword;
    Button btnRegister;

    User user;

    // Create i.e. Register a User and Login a User
    FirebaseAuth auth;

    // To perform all the DB operations
    FirebaseFirestore db;

    FirebaseUser fbUser;

    ProgressDialog progressDialog;

    ArrayList<User> users;

    void initViews(){
        eTxtName = findViewById(R.id.editTextName);
        eTxtEmail = findViewById(R.id.editTextEmail);
        eTxtPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.registerButton);
        btnRegister.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().setTitle("Register");
        initViews();
    }

    void saveUserInFirestore(){

        db.collection("users").add(user)
                .addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            Toast.makeText(RegisterUserActivity.this,"User Added in Firestore",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(RegisterUserActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();



                        }
                    }
                });

        db.collection("users").add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String documentId = documentReference.getId();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    void fetchAllUsersFromFirestore(){
        users = new ArrayList<>();
        db.collection("users").get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        users.add(user);
                    }
                }
            }
        });

        // Show the Fetched users on Recyclerview / ListView
    }



    void registerUserWithFirebase(){

        progressDialog.show();
        auth.createUserWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()){
                            Toast.makeText(RegisterUserActivity.this,"User Created/Registered",Toast.LENGTH_LONG).show();

                            //fbUser = task.getResult().getUser();
                            //fbUser = auth.getCurrentUser();

                            saveUserInFirestore();

                        }else{
                            Toast.makeText(RegisterUserActivity.this,"Some Error, Please Try Again !!",Toast.LENGTH_LONG).show();
                        }


                    }
                });

    }

    void loginUserFromFirebase(){

        progressDialog.show();

        auth.signInWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()){



                            Toast.makeText(RegisterUserActivity.this,"User Logged In",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterUserActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterUserActivity.this,"Some Error, Please Try Again !!",Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        user = new User();
        user.name = eTxtName.getText().toString();
        user.email = eTxtEmail.getText().toString();
        user.password = eTxtPassword.getText().toString();
        registerUserWithFirebase();
    }
}
