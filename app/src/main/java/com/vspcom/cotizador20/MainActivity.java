package com.vspcom.cotizador20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ToggleButton;

import com.google.android.material.tabs.TabLayout;
import com.vspcom.cotizador20.DB.DBManager;
import com.vspcom.cotizador20.DB.DBConexion;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ToggleButton checkboxCCTV;
    private ToggleButton checkboxB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        checkboxCCTV = findViewById(R.id.btnSectionCCTV);
        checkboxB = findViewById(R.id.btnSectionB);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void onResume() {
        super.onResume();
        openDatabaseConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDatabaseConnection();
    }

    private void openDatabaseConnection() {
        try {
            dbManager.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeDatabaseConnection() {
        dbManager.close();
    }
}
