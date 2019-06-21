package com.example.stylyagav4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylyagav4.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText fullNameEditText, userPhoneEditText, cityEditText, addressEditText, passwordEditText;
    private TextView closeTextButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Налаштування");
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);

        userNameTextView.setText(Prevalent.currentOnlineUser.getName());

        fullNameEditText = findViewById(R.id.settings_name);
        userPhoneEditText = findViewById(R.id.settings_phone_number);
        cityEditText = findViewById(R.id.settings_city);
        addressEditText = findViewById(R.id.settings_address);
        passwordEditText = findViewById(R.id.settings_password);

//        closeTextButton = findViewById(R.id.close_settings_button);
        saveButton = findViewById(R.id.update_settings_button);

        userInfoDisplay(fullNameEditText, userPhoneEditText, cityEditText, addressEditText, passwordEditText);

//        closeTextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserInfo();
            }
        });
    }

    private void checkUserInfo() {
        String name = fullNameEditText.getText().toString();
        String phone = userPhoneEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(SettingsActivity.this, "Будь ласка, введіть своє ім'я...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(SettingsActivity.this, "Будь ласка, введіть свій телефон...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(SettingsActivity.this, "Будь ласка, введіть своє місто...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(SettingsActivity.this, "Будь ласка, введіть свою адресу...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(SettingsActivity.this, "Будь ласка, введіть свій пароль!", Toast.LENGTH_SHORT).show();
        } else {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", fullNameEditText.getText().toString());
        userMap.put("phone", userPhoneEditText.getText().toString());
        userMap.put("city", cityEditText.getText().toString());
        userMap.put("address", addressEditText.getText().toString());
        userMap.put("password", passwordEditText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Інформація успішно оновлена.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoDisplay(final EditText fullNameEditText, final EditText userPhoneEditText, final EditText cityEditText, final EditText addressEditText, final EditText passwordEditText) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("phone").exists()) {
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String city = dataSnapshot.child("city").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();

                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        cityEditText.setText(city);
                        addressEditText.setText(address);
                        passwordEditText.setText(password);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(intent);
//        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(SettingsActivity.this, Cart2Activity.class);
            startActivity(intent);
//        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();

            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}