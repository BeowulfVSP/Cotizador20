package com.vspcom.cotizador20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.google.android.material.tabs.TabLayout;
import com.vspcom.cotizador20.Adapters.FragmentAdapter;
import com.vspcom.cotizador20.DB.DBManager;
import com.vspcom.cotizador20.NewClasses.CSpinner;
import com.vspcom.cotizador20.NewClasses.Producto;
import com.vspcom.cotizador20.SQLServer.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton optionsButton;

    List<CSpinner> grabadorList = new ArrayList<>();
    List<CSpinner> camarasList = new ArrayList<>();
    List<CSpinner> almacenamientoList = new ArrayList<>();
    List<CSpinner> fuenteList = new ArrayList<>();
    List<CSpinner> cableList = new ArrayList<>();
    List<CSpinner> accesoriosList = new ArrayList<>();

    List<Producto> listaProducto = new ArrayList<>();

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

        //createDatabase();

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

                listaProducto.clear();

                try {
                    dbManager.open();
                    while (resultSet.next()) {
                        String prds_Articulo = resultSet.getString("ARTICULO");
                        String prds_Descripcion = resultSet.getString("DESCRIP");
                        String prds_Linea = resultSet.getString("LINEA");
                        String prds_Marca = resultSet.getString("MARCA");
                        String prds_Precio1 = resultSet.getString("PRECIO1");
                        String prds_Impuesto = resultSet.getString("IMPUESTO");
                        String prds_Ubicacion = resultSet.getString("UBICACION");
                        String prds_Fabricante = resultSet.getString("fabricante");
                        System.out.println("UBICACION: " + prds_Ubicacion);

                        Producto producto = new Producto(prds_Articulo, prds_Descripcion, prds_Linea, prds_Marca, prds_Precio1, prds_Impuesto, prds_Ubicacion, prds_Fabricante);

                        listaProducto.add(producto);
                        dbManager.insertPorducts(producto);
                    }
                    resultSet.close();
                    dbManager.close();

                    loadData();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error durante la conexión");
            }
        }
    }

    public void loadData() {
        dbManager.open();
        List<Producto> productosSQLIte = dbManager.getAllProducts();
        dbManager.close();

        grabadorList.clear();
        camarasList.clear();
        almacenamientoList.clear();
        fuenteList.clear();
        cableList.clear();
        accesoriosList.clear();

        for( Producto producto : productosSQLIte ){
            Log.d("MainActivity", "Código: " + producto.getArticulo() +
                    ", Descripción: " + producto.getDescripcion() +
                    ", Línea: " + producto.getLinea() +
                    ", Marca: " + producto.getMarca() +
                    ", Precio: " + producto.getPrecio() +
                    ", Impuesto: " + producto.getImpuesto() +
                    ", Ubicación: " + producto.getUbicacion() +
                    ", Fabricante: " + producto.getFabricante());


            if( producto.getUbicacion().equals("GRA") ) {
                grabadorList.add(new CSpinner(producto.getArticulo(), producto.getDescripcion(), producto.getPrecio()));
            }

            if( producto.getUbicacion().equals("CAM") ) {
                camarasList.add(new CSpinner(producto.getArticulo(), producto.getDescripcion(), producto.getPrecio()));
            }

            if( producto.getUbicacion().equals("ALM") ) {
                almacenamientoList.add(new CSpinner(producto.getArticulo(), producto.getDescripcion(), producto.getPrecio()));
            }

            if( producto.getUbicacion().equals("FUE") ) {
                fuenteList.add(new CSpinner(producto.getArticulo(), producto.getDescripcion(), producto.getPrecio()));
            }

            if( producto.getUbicacion().equals("CAB") ) {
                cableList.add(new CSpinner(producto.getArticulo(), producto.getDescripcion(), producto.getPrecio()));
            }

            if( producto.getUbicacion().equals("ACC") ) {
                accesoriosList.add(new CSpinner(producto.getArticulo(), producto.getDescripcion(), producto.getPrecio()));
            }

        }

        /* GRABADOR */
        List<Map<String, String>> grabadorSpinner = new ArrayList<>();
        for (CSpinner item : grabadorList) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("descripcion", item.getDescripcion());
            itemMap.put("precio", item.getPrecio());
            grabadorSpinner.add(itemMap);
        }

        SimpleAdapter grabadorAdapter = new SimpleAdapter(
            MainActivity.this,
            grabadorSpinner,
            android.R.layout.simple_spinner_item,
            new String[]{"descripcion"},
            new int[]{android.R.id.text1}
        );

        grabadorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerGrabador = findViewById(R.id.spinnerGra);
        spinnerGrabador.setAdapter(grabadorAdapter);
        /* GRABADOR */

        /* CAMARAS */
        List<Map<String, String>> camarasSpinner = new ArrayList<>();
        for (CSpinner item : camarasList) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("descripcion", item.getDescripcion());
            itemMap.put("precio", item.getPrecio());
            camarasSpinner.add(itemMap);
        }

        SimpleAdapter camarasAdapter = new SimpleAdapter(
            MainActivity.this,
            camarasSpinner,
            android.R.layout.simple_spinner_item,
            new String[]{"descripcion"},
            new int[]{android.R.id.text1}
        );

        camarasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerCamaras = findViewById(R.id.spinnerCam);
        spinnerCamaras.setAdapter(camarasAdapter);
        /* CAMARAS */

        /* ALMACENAMIENTO */
        List<Map<String, String>> almacenamientoSpinner = new ArrayList<>();
        for (CSpinner item : almacenamientoList) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("descripcion", item.getDescripcion());
            itemMap.put("precio", item.getPrecio());
            almacenamientoSpinner.add(itemMap);
        }

        SimpleAdapter almacenamientoAdapter = new SimpleAdapter(
            MainActivity.this,
            almacenamientoSpinner,
            android.R.layout.simple_spinner_item,
            new String[]{"descripcion"},
            new int[]{android.R.id.text1}
        );

        almacenamientoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerAlmacenamiento = findViewById(R.id.spinnerAlm);
        spinnerAlmacenamiento.setAdapter(almacenamientoAdapter);
        /* ALMACENAMIENTO */

        /* FUENTE */
        List<Map<String, String>> fuenteSpinner = new ArrayList<>();
        for (CSpinner item : fuenteList) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("descripcion", item.getDescripcion());
            itemMap.put("precio", item.getPrecio());
            fuenteSpinner.add(itemMap);
        }

        SimpleAdapter fuenteAdapter = new SimpleAdapter(
            MainActivity.this,
            fuenteSpinner,
            android.R.layout.simple_spinner_item,
            new String[]{"descripcion"},
            new int[]{android.R.id.text1}
        );

        fuenteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerFuente = findViewById(R.id.spinnerFue);
        spinnerFuente.setAdapter(fuenteAdapter);
        /* FUENTE */

        /* CABLE */
        List<Map<String, String>> cableSpinner = new ArrayList<>();
        for (CSpinner item : cableList) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("descripcion", item.getDescripcion());
            itemMap.put("precio", item.getPrecio());
            cableSpinner.add(itemMap);
        }

        SimpleAdapter cableAdapter = new SimpleAdapter(
            MainActivity.this,
            cableSpinner,
            android.R.layout.simple_spinner_item,
            new String[]{"descripcion"},
            new int[]{android.R.id.text1}
        );

        cableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerCable = findViewById(R.id.spinnerCab);
        spinnerCable.setAdapter(cableAdapter);
        /* CABLE */

        /* ACCESORIOS */
        List<Map<String, String>> accesoriosSpinner = new ArrayList<>();
        for (CSpinner item : accesoriosList) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("id", item.getId());
            itemMap.put("descripcion", item.getDescripcion());
            itemMap.put("precio", item.getPrecio());
            accesoriosSpinner.add(itemMap);
        }

        SimpleAdapter accesoriosAdapter = new SimpleAdapter(
            MainActivity.this,
            accesoriosSpinner,
            android.R.layout.simple_spinner_item,
            new String[]{"descripcion"},
            new int[]{android.R.id.text1}
        );

        accesoriosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerAccesorios = findViewById(R.id.spinnerAcc);
        spinnerAccesorios.setAdapter(accesoriosAdapter);
        /* ACCESORIOS */
    }
}
