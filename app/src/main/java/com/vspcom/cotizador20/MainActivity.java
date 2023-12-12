package com.vspcom.cotizador20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ToggleButton;

import com.google.android.material.tabs.TabLayout;
import com.vspcom.cotizador20.Adapters.FragmentAdapter;
import com.vspcom.cotizador20.DB.DBManager;
import com.vspcom.cotizador20.DB.DBConexion;
import com.vspcom.cotizador20.SQLServer.DatabaseConnector;

import android.util.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ImageButton optionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        fragmentAdapter.setShowComingSoon(false);

        createDatabase();

        optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        openDatabaseConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() || isChangingConfigurations()) {
            closeDatabaseConnection();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void createDatabase() {
        openDatabaseConnection();
        closeDatabaseConnection();
    }

    private void sync() {
        new DatabaseTask().execute();
    }

    private class DatabaseTask extends AsyncTask<Void, Void, Connection> {

        @Override
        protected Connection doInBackground(Void... voids) {
            Connection connection = null;
            try {
                connection = DatabaseConnector.connect();
                // Realizar operaciones
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }

        @Override
        protected void onPostExecute(Connection connection) {
            if (connection != null) {
                // Conexión exitosa
                System.out.println("Conexion Exitosa");
            } else {
                // Error en la conexión
                System.out.println("Error durante la conexion");
            }
        }
    }

}
