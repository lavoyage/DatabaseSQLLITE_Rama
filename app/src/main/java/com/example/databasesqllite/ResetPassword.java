package com.example.databasesqllite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etUsernameOrEmail, etNewPassword, etConfirmPassword;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        db = new DatabaseHelper(this);

        etUsernameOrEmail = findViewById(R.id.etUsernameOrEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameOrEmail = etUsernameOrEmail.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (usernameOrEmail.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    showDialog("Error", "All fields are required.");
                } else if (!newPassword.equals(confirmPassword)) {
                    showDialog("Error", "New password and confirm password do not match.");
                } else {
                    boolean userExists = db.checkUserExists(usernameOrEmail);
                    if (userExists) {
                        boolean isUpdated = db.updatePassword(usernameOrEmail, newPassword);
                        if (isUpdated) {
                            showDialog("Success", "Password has been reset successfully.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish(); // Kembali ke halaman sebelumnya
                                }
                            });
                        } else {
                            showDialog("Error", "Failed to reset password. Please try again.");
                        }
                    } else {
                        showDialog("Error", "User does not exist.");
                    }
                }
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
