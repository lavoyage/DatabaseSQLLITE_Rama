package com.example.databasesqllite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etLoginUsername, etLoginPassword;
    private Button btnLogin, btnGoToRegister;

    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);
        forgetPassword = findViewById(R.id.forgetPassword); // Tambahkan tombol di XML

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    showDialog("Error", "Fill all fields");
                } else {
                    boolean isValid = db.checkLogin(username, password);
                    if (isValid) {
                        showDialog("Success", "Login successful", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Lanjut ke DashboardActivity
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.putExtra("username", username); // Kirim username ke Dashboard
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        showDialog("Error", "Invalid credentials");
                    }
                }
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
                startActivity(intent);
            }
        });
    }

    // Fungsi untuk menampilkan dialog
    private void showDialog(String title, String message) {
        showDialog(title, message, null);
    }

    private void showDialog(String title, String message, DialogInterface.OnClickListener positiveAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", positiveAction);
        builder.setCancelable(false);
        builder.show();
    }

}
