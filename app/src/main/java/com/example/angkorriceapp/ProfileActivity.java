package com.example.angkorriceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private EditText inputName, inputEmail;
    private androidx.cardview.widget.CardView btnLogout, btnEditProfile;
    private Button btnBack, btnSaveProfile, btnCancelEdit;
    private androidx.cardview.widget.CardView orderHistorySection;
    private LinearLayout editButtonContainer, saveButtonContainer;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize views
        inputName = findViewById(R.id.input_profile_name);
        inputEmail = findViewById(R.id.input_profile_email);
        btnLogout = findViewById(R.id.btn_logout);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnBack = findViewById(R.id.btn_back);
        orderHistorySection = findViewById(R.id.order_history_section);
        btnSaveProfile = findViewById(R.id.btn_save_profile);
        btnCancelEdit = findViewById(R.id.btn_cancel_edit);
        editButtonContainer = findViewById(R.id.edit_button_container);
        saveButtonContainer = findViewById(R.id.save_button_container);

        // Load user data
        loadUserData();

        // Set button listeners
        btnBack.setOnClickListener(v -> finish());
        
        btnLogout.setOnClickListener(v -> logout());
        
        btnEditProfile.setOnClickListener(v -> enterEditMode());
        
        btnSaveProfile.setOnClickListener(v -> saveProfileChanges());
        
        btnCancelEdit.setOnClickListener(v -> exitEditMode());

        // Order history listener
        orderHistorySection.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail();
            
            // Display email from Firebase Auth (read-only)
            inputEmail.setText(userEmail != null ? userEmail : "");
            inputEmail.setEnabled(false);
            
            Log.d("ProfileActivity", "Loading data for user: " + userId);
            
            // Fetch additional data from Firestore
            db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        
                        Log.d("ProfileActivity", "Data found - Name: " + name);
                        
                        if (name != null) {
                            inputName.setText(name);
                        }
                    } else {
                        Log.d("ProfileActivity", "No user document found in Firestore");
                        // If no Firestore document, extract name from email
                        if (userEmail != null) {
                            String nameFromEmail = userEmail.split("@")[0];
                            inputName.setText(nameFromEmail);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ProfileActivity", "Error loading user data from Firestore: " + e.getMessage(), e);
                    // Fallback: show email-based name
                    if (userEmail != null) {
                        String nameFromEmail = userEmail.split("@")[0];
                        inputName.setText(nameFromEmail);
                    }
                });
        } else {
            Log.e("ProfileActivity", "No current user found");
            Toast.makeText(this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void enterEditMode() {
        isEditMode = true;
        inputName.setEnabled(true);
        inputName.requestFocus();
        editButtonContainer.setVisibility(LinearLayout.GONE);
        saveButtonContainer.setVisibility(LinearLayout.VISIBLE);
        orderHistorySection.setVisibility(LinearLayout.GONE);
        Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
    }

    private void exitEditMode() {
        isEditMode = false;
        inputName.setEnabled(false);
        editButtonContainer.setVisibility(LinearLayout.VISIBLE);
        saveButtonContainer.setVisibility(LinearLayout.GONE);
        orderHistorySection.setVisibility(LinearLayout.VISIBLE);
        // Reload original data
        loadUserData();
        Toast.makeText(this, "Changes cancelled", Toast.LENGTH_SHORT).show();
    }

    private void saveProfileChanges() {
        String newName = inputName.getText().toString().trim();
        
        if (newName.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (currentUser != null) {
            String userId = currentUser.getUid();
            
            // Update Firestore
            db.collection("users").document(userId)
                .update("name", newName)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ProfileActivity", "Name updated successfully");
                    isEditMode = false;
                    inputName.setEnabled(false);
                    editButtonContainer.setVisibility(LinearLayout.VISIBLE);
                    saveButtonContainer.setVisibility(LinearLayout.GONE);
                    orderHistorySection.setVisibility(LinearLayout.VISIBLE);
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("ProfileActivity", "Error updating name: " + e.getMessage(), e);
                    Toast.makeText(ProfileActivity.this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        }
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(ProfileActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileActivity.this, WelcomeScreenActivity.class));
        finish();
    }
}

