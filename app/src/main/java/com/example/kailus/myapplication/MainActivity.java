package com.example.kailus.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    private TextView userRegistartion;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView myImageView = (ImageView) findViewById(R.id.my_image_view);
        myImageView.setImageResource(R.drawable.hoop1);

        Name = (EditText) findViewById(R.id.etName);
        Password = (EditText) findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.btnLogin);
        userRegistartion = (TextView)findViewById(R.id.tvRegister);
        forgotPassword = (TextView)findViewById(R.id.tvForgotPassword);



        Info.setText("No of attempts remaining: 5");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null) {
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());


            }
        });

            userRegistartion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                }
            });

            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, PasswordActivity.class));
                }
            });
            Name.addTextChangedListener(loginTextWathcer);
            Password.addTextChangedListener(loginTextWathcer);
    }
    private TextWatcher loginTextWathcer = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String usernameInput = Name.getText().toString().trim();
            String passwordInput = Password.getText().toString().trim();

            Login.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void validate(String userEmail, String userPassword) {

            progressDialog.setMessage("Almost Done");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        // Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG);
                        //startActivity(new Intent(MainActivity.this, SecondActivity.class));
                        checkEmailVerification();
                    } else {
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG);
                        counter--;
                        Info.setText("No of attempts remaining: " + counter);
                        progressDialog.dismiss();
                        if (counter == 0) {
                            Login.setEnabled(false);
                        }
                    }
                }
            });

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();



        if(emailflag){
           finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }else{
           Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
           firebaseAuth.signOut();
       }
    }

}
