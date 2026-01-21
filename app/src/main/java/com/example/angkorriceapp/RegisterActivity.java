package com.example.angkorriceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText nameInput, emailInput, passInput;
    Button btnRegister;
    TextView txtLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        passInput = findViewById(R.id.input_password);
        btnRegister = findViewById(R.id.btn_register);
        txtLogin = findViewById(R.id.txt_login);

        btnRegister.setOnClickListener(v -> registerUser());

        txtLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String pass = passInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Firebase user
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser == null) {
                         Toast.makeText(RegisterActivity.this, "Registration failed: user is null.", Toast.LENGTH_SHORT).show();
                         return;
                    }
                    // Save user data to Firestore
                    String userId = firebaseUser.getUid();
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("createdAt", System.currentTimeMillis());

                    db.collection("users").document(userId).set(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("RegisterActivity", "Error saving user data to Firestore.", e);

                            // If saving to Firestore fails, delete the user from Auth to allow re-registration
                            firebaseUser.delete().addOnCompleteListener(deleteTask -> {
                                if (deleteTask.isSuccessful()) {
                                    Log.d("RegisterActivity", "Successfully deleted user from Auth after Firestore failure.");
                                } else {
                                    Log.w("RegisterActivity", "Failed to delete user from Auth after Firestore failure.", deleteTask.getException());
                                }
                            });

                            String errorMessage = "Registration failed. Please try again.";
                            if (e instanceof FirebaseFirestoreException && ((FirebaseFirestoreException) e).getCode() == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                                errorMessage = "Registration failed: Database access denied. Please contact support.";
                            }
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        });
                } else {
                    // Handle user creation failure
                    String errorMessage = "Registration failed. Please try again.";
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        errorMessage = "This email address is already registered.";
                    } else if (task.getException() != null) {
                        errorMessage = task.getException().getMessage();
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("RegisterActivity", "User creation failed.", task.getException());
                }
            });
    }
}
