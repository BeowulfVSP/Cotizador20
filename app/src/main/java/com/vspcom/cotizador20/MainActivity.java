package com.vspcom.cotizador20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.google.android.material.tabs.TabLayout;
import com.vspcom.cotizador20.Adapters.FragmentAdapter;
import com.vspcom.cotizador20.DB.DBManager;
import com.vspcom.cotizador20.NewClasses.CSpinner;
import com.vspcom.cotizador20.SQLServer.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton optionsButton;

    List<CSpinner> prdsArticulos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Desactivar StrictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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

    private class DatabaseTask extends AsyncTask<Void, Void, ResultSet> {

        @Override
        protected ResultSet doInBackground(Void... voids) {
            Connection connection = null;
            ResultSet resultSet = null;
            try {
                connection = DatabaseConnector.connect();
                if (connection != null) {
                    Statement stmt = connection.createStatement();
                    String query = "SELECT ARTICULO, DESCRIP, LINEA, MARCA, PRECIO1, IMPUESTO, UBICACION, fabricante FROM prods WHERE fabricante = 'COT'";
                    resultSet = stmt.executeQuery(query);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultSet;
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            if (resultSet != null) {
                try {
                    while (resultSet.next()) {
                        String prds_Articulo = resultSet.getString("ARTICULO");
                        String prds_Descripcion = resultSet.getString("DESCRIP");
                        System.out.println("Articulo: " + prds_Articulo);

                        prdsArticulos.add(new CSpinner(prds_Articulo, prds_Descripcion));
                    }
                    resultSet.close();

                    SimpleAdapter adapter = new SimpleAdapter(
                            this,
                            prdsArticulos,
                            android.R.layout.simple_spinner_item,
                            new String[]{"descripcion"},
                            new int[]{android.R.id.text1}
                    );

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    Spinner spinner = findViewById(R.id.spinnerGra);
                    spinner.setAdapter(adapter);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error durante la conexión");
            }
        }
    }
}
