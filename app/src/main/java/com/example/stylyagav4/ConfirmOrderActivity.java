package com.example.stylyagav4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stylyagav4.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class ConfirmOrderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText fullNameEditText, userPhoneEditText, cityEditText, addressEditText;
    private TextView closeConfirmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        fullNameEditText = findViewById(R.id.confirm_name);
        userPhoneEditText = findViewById(R.id.confirm_phone);
        cityEditText = findViewById(R.id.confirm_city);
        addressEditText = findViewById(R.id.confirm_address);

        closeConfirmOrder = findViewById(R.id.close_confirm_button);

        Toolbar toolbar = findViewById(R.id.toolbar);
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

        userInfoDisplay(fullNameEditText, userPhoneEditText, cityEditText, addressEditText);

        closeConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void userInfoDisplay(final EditText fullNameEditText, final EditText userPhoneEditText, final EditText cityEditText, final EditText addressEditText) {
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

                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        cityEditText.setText(city);
                        addressEditText.setText(address);;
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
        getMenuInflater().inflate(R.menu.confirm_order, menu);
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
            Intent intent = new Intent(ConfirmOrderActivity.this, HomeActivity.class);
            startActivity(intent);
//        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(ConfirmOrderActivity.this, Cart2Activity.class);
            startActivity(intent);
//        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ConfirmOrderActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();

            Intent intent = new Intent(ConfirmOrderActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
