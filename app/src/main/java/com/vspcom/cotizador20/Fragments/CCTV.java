package com.vspcom.cotizador20.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vspcom.cotizador20.DB.DBManager;
import com.vspcom.cotizador20.MainActivity;
import com.vspcom.cotizador20.NewClasses.CSpinner;
import com.vspcom.cotizador20.NewClasses.Producto;
import com.vspcom.cotizador20.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CCTV extends Fragment {

    private DBManager dbManager;

    private LinearLayout nuevosAccesorios;
    private LinearLayout nuevasCamaras;
    private LinearLayout nuevoAlmacenamiento;
    private LinearLayout nuevaFuente;
    private LinearLayout nuevosCables;
    private LinearLayout nuevoGrabador;

    public double totalCostoProducto = 0.0;

    private Map<Button, LinearLayout> buttonLayoutMap = new HashMap<>();
    private Map<Button, Double> buttonCostMap = new HashMap<>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CCTV() {

    }

    public static CCTV newInstance(String param1, String param2) {
        CCTV fragment = new CCTV();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cctv, container, false);

        Button btnAddAcc = view.findViewById(R.id.btnAddAcc);
        Button btnAddCam = view.findViewById(R.id.btnAddCam);
        Button btnAddAlm = view.findViewById(R.id.btnAddAlm);
        Button btnAddFue = view.findViewById(R.id.btnAddFue);
        Button btnAddCab = view.findViewById(R.id.btnAddCab);
        Button btnAddGra = view.findViewById(R.id.btnAddGra);

        AtomicInteger contador = new AtomicInteger(1);

        nuevosAccesorios = view.findViewById(R.id.contenedorAcc);
        nuevasCamaras = view.findViewById(R.id.contenedorCam);
        nuevoAlmacenamiento = view.findViewById(R.id.contenedorAlm);
        nuevaFuente = view.findViewById(R.id.contenedorFue);
        nuevosCables = view.findViewById(R.id.contenedorCab);
        nuevoGrabador = view.findViewById(R.id.contenedorGra);

        dbManager = new DBManager(getContext());

        DecimalFormat df = new DecimalFormat("######.##");

        btnAddAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerAcc = view.findViewById(R.id.spinnerAcc);
                HashMap<String, String> selectedHashMap = (HashMap<String, String>) spinnerAcc.getSelectedItem();
                String selectedId = selectedHashMap.get("id");

                System.out.println("Articulo seleccionado: " + selectedId);

                Handler handler = new Handler();

                dbManager.open();
                Producto producto = dbManager.getProductById(selectedId);

                LinearLayout linearLayoutDesc = new LinearLayout(getContext());
                linearLayoutDesc.setOrientation(LinearLayout.VERTICAL);

                TextView nuevoTextView = new TextView(getContext());
                nuevoTextView.setText(producto.getArticulo() + " - " + producto.getDescripcion());

                EditText cantidadEditText = new EditText(getContext());
                EditText costoEditText = new EditText(getContext());
                EditText totalEditText = new EditText(getContext());
                Button nuevoBoton = new Button(getContext());

                cantidadEditText.setHint("Cantidad");
                costoEditText.setHint("Precio");
                totalEditText.setHint("Total");

                cantidadEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                costoEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                totalEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                cantidadEditText.setGravity(Gravity.CENTER);
                costoEditText.setGravity(Gravity.CENTER);
                totalEditText.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                layoutParams.weight = 1;

                cantidadEditText.setLayoutParams(layoutParams);
                costoEditText.setLayoutParams(layoutParams);
                totalEditText.setLayoutParams(layoutParams);

                linearLayoutDesc.addView(nuevoTextView);

                LinearLayout linearLayoutCantidadTotal = new LinearLayout(getContext());
                linearLayoutCantidadTotal.setOrientation(LinearLayout.HORIZONTAL);

                linearLayoutCantidadTotal.addView(cantidadEditText);
                linearLayoutCantidadTotal.addView(costoEditText);
                linearLayoutCantidadTotal.addView(totalEditText);
                linearLayoutCantidadTotal.addView(nuevoBoton);

                nuevoBoton.setText("ELIMINAR");
                nuevoBoton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                nuevoBoton.setTextColor(Color.WHITE);

                double[] costoAnterior = {0.0};
                nuevoBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarLayoutAsociado((Button) v);
                    }
                });

                buttonLayoutMap.put(nuevoBoton, linearLayoutDesc);

                linearLayoutDesc.addView(linearLayoutCantidadTotal);

                nuevosAccesorios.addView(linearLayoutDesc);

                double costoProducto = Double.parseDouble(producto.getPrecio().replace(",", ""));
                if (producto.getImpuesto().equals("IVA")) {
                    costoProducto = costoProducto * 1.16;
                }
                costoProducto = Double.parseDouble(df.format(costoProducto));
                buttonCostMap.put(nuevoBoton, costoProducto);
                actualizarTotalCosto(costoProducto);

                cantidadEditText.setText("1");
                costoEditText.setText("" + costoProducto);
                costoEditText.addTextChangedListener(new TextWatcher() {
                    private final long DELAY = 1000; // Espera 500 milisegundos después del último cambio
                    private final Handler handler = new Handler();
                    private Runnable runnable;

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handler.removeCallbacks(runnable);
                        runnable = () -> {
                            String costoText = costoEditText.getText().toString().trim();
                            String cantidadText = cantidadEditText.getText().toString().trim();

                            if (!costoText.isEmpty() && !cantidadText.isEmpty()) {
                                try {
                                    double nuevoCosto = Double.parseDouble(costoText);
                                    double cantidad = Double.parseDouble(cantidadText);
                                    double total = nuevoCosto * cantidad;

                                    // Actualiza los valores
                                    double costoAnterior = buttonCostMap.get(nuevoBoton);
                                    restarCostoEliminado(costoAnterior);
                                    buttonCostMap.put(nuevoBoton, nuevoCosto);
                                    actualizarTotalCosto(nuevoCosto);

                                    // Actualiza el total en totalEditText
                                    totalEditText.setText(String.valueOf(total));

                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        handler.postDelayed(runnable, DELAY);
                    }
                });

                cantidadEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handler.removeCallbacks(runnable);
                        runnable = () -> {
                            String costoText = costoEditText.getText().toString().trim();
                            String cantidadText = cantidadEditText.getText().toString().trim();

                            if (!costoText.isEmpty() && !cantidadText.isEmpty()) {
                                try {
                                    double nuevoCosto = Double.parseDouble(costoText);
                                    double cantidad = Double.parseDouble(cantidadText);
                                    double total = nuevoCosto * cantidad;

                                    // Actualiza los valores
                                    double costoAnterior = buttonCostMap.get(nuevoBoton);
                                    restarCostoEliminado(costoAnterior);
                                    buttonCostMap.put(nuevoBoton, nuevoCosto);
                                    actualizarTotalCosto(nuevoCosto);

                                    // Actualiza el total en totalEditText
                                    totalEditText.setText(String.valueOf(total));

                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        handler.postDelayed(runnable, DELAY);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        // No se usa en este caso
                    }
                });

                dbManager.close();
                contador.getAndIncrement();
            }
        });

        btnAddCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerCab = view.findViewById(R.id.spinnerCab); // Asegúrate de obtener la referencia correcta del Spinner
                HashMap<String, String> selectedHashMap = (HashMap<String, String>) spinnerCab.getSelectedItem();
                String selectedId = selectedHashMap.get("id");

                dbManager.open();
                Producto producto = dbManager.getProductById(selectedId);

                LinearLayout linearLayoutDesc = new LinearLayout(getContext());
                linearLayoutDesc.setOrientation(LinearLayout.VERTICAL);

                TextView nuevoTextView = new TextView(getContext());
                nuevoTextView.setText(producto.getArticulo() + " - " + producto.getDescripcion());

                EditText cantidadEditText = new EditText(getContext());
                EditText costoEditText = new EditText(getContext());
                Button nuevoBoton = new Button(getContext());

                cantidadEditText.setHint("Cantidad");
                costoEditText.setHint("Precio");

                cantidadEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                costoEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                cantidadEditText.setGravity(Gravity.CENTER);
                costoEditText.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                layoutParams.weight = 1;

                cantidadEditText.setLayoutParams(layoutParams);
                costoEditText.setLayoutParams(layoutParams);

                linearLayoutDesc.addView(nuevoTextView);

                LinearLayout linearLayoutCantidadTotal = new LinearLayout(getContext());
                linearLayoutCantidadTotal.setOrientation(LinearLayout.HORIZONTAL);

                linearLayoutCantidadTotal.addView(cantidadEditText);
                linearLayoutCantidadTotal.addView(costoEditText);
                linearLayoutCantidadTotal.addView(nuevoBoton);

                nuevoBoton.setText("ELIMINAR");
                nuevoBoton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                nuevoBoton.setTextColor(Color.WHITE);

                double[] costoAnterior = {0.0};
                nuevoBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarLayoutAsociado((Button) v);
                    }
                });

                buttonLayoutMap.put(nuevoBoton, linearLayoutDesc);

                linearLayoutDesc.addView(linearLayoutCantidadTotal);

                nuevosCables.addView(linearLayoutDesc); // Agrega al contenedor de cables

                double costoProducto = Double.parseDouble(producto.getPrecio().replace(",", ""));
                if (producto.getImpuesto().equals("IVA")) {
                    costoProducto = costoProducto * 1.16;
                }
                costoProducto = Double.parseDouble(df.format(costoProducto));
                buttonCostMap.put(nuevoBoton, costoProducto);
                actualizarTotalCosto(costoProducto);

                cantidadEditText.setText("1");
                costoEditText.setText("" + costoProducto);

                costoEditText.addTextChangedListener(new TextWatcher() {
                    private final long DELAY = 1000;
                    private final Handler handler = new Handler();
                    private Runnable runnable;

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handler.removeCallbacks(runnable);
                        runnable = () -> {
                            String costoText = costoEditText.getText().toString().trim();
                            if (!costoText.isEmpty()) {
                                try {
                                    double nuevoCosto = Double.parseDouble(costoText);
                                    double costoAnterior = buttonCostMap.get(nuevoBoton);
                                    restarCostoEliminado(costoAnterior);
                                    buttonCostMap.put(nuevoBoton, nuevoCosto);
                                    actualizarTotalCosto(nuevoCosto);
                                } catch (NumberFormatException e) {

                                }
                            }
                        };
                        handler.postDelayed(runnable, DELAY);
                    }
                });

                dbManager.close();
                contador.getAndIncrement();
            }
        });

        btnAddFue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerFue = view.findViewById(R.id.spinnerFue); // Obtén la referencia correcta del Spinner
                HashMap<String, String> selectedHashMap = (HashMap<String, String>) spinnerFue.getSelectedItem();
                String selectedId = selectedHashMap.get("id");

                dbManager.open();
                Producto producto = dbManager.getProductById(selectedId);

                LinearLayout linearLayoutDesc = new LinearLayout(getContext());
                linearLayoutDesc.setOrientation(LinearLayout.VERTICAL);

                TextView nuevoTextView = new TextView(getContext());
                nuevoTextView.setText(producto.getArticulo() + " - " + producto.getDescripcion());

                EditText cantidadEditText = new EditText(getContext());
                EditText costoEditText = new EditText(getContext());
                Button nuevoBoton = new Button(getContext());

                cantidadEditText.setHint("Cantidad");
                costoEditText.setHint("Precio");

                cantidadEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                costoEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                cantidadEditText.setGravity(Gravity.CENTER);
                costoEditText.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                layoutParams.weight = 1;

                cantidadEditText.setLayoutParams(layoutParams);
                costoEditText.setLayoutParams(layoutParams);

                linearLayoutDesc.addView(nuevoTextView);

                LinearLayout linearLayoutCantidadTotal = new LinearLayout(getContext());
                linearLayoutCantidadTotal.setOrientation(LinearLayout.HORIZONTAL);

                linearLayoutCantidadTotal.addView(cantidadEditText);
                linearLayoutCantidadTotal.addView(costoEditText);
                linearLayoutCantidadTotal.addView(nuevoBoton);

                nuevoBoton.setText("ELIMINAR");
                nuevoBoton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                nuevoBoton.setTextColor(Color.WHITE);

                double[] costoAnterior = {0.0};
                nuevoBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarLayoutAsociado((Button) v);
                    }
                });

                buttonLayoutMap.put(nuevoBoton, linearLayoutDesc);

                linearLayoutDesc.addView(linearLayoutCantidadTotal);

                nuevaFuente.addView(linearLayoutDesc); // Agrega al contenedor de fuentes

                double costoProducto = Double.parseDouble(producto.getPrecio().replace(",", ""));
                if (producto.getImpuesto().equals("IVA")) {
                    costoProducto = costoProducto * 1.16;
                }
                costoProducto = Double.parseDouble(df.format(costoProducto));
                buttonCostMap.put(nuevoBoton, costoProducto);
                actualizarTotalCosto(costoProducto);

                cantidadEditText.setText("1");
                costoEditText.setText("" + costoProducto);

                costoEditText.addTextChangedListener(new TextWatcher() {
                    private final long DELAY = 1000;
                    private final Handler handler = new Handler();
                    private Runnable runnable;

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handler.removeCallbacks(runnable);
                        runnable = () -> {
                            String costoText = costoEditText.getText().toString().trim();
                            if (!costoText.isEmpty()) {
                                try {
                                    double nuevoCosto = Double.parseDouble(costoText);
                                    double costoAnterior = buttonCostMap.get(nuevoBoton);
                                    restarCostoEliminado(costoAnterior);
                                    buttonCostMap.put(nuevoBoton, nuevoCosto);
                                    actualizarTotalCosto(nuevoCosto);
                                } catch (NumberFormatException e) {

                                }
                            }
                        };
                        handler.postDelayed(runnable, DELAY);
                    }
                });

                dbManager.close();
                contador.getAndIncrement();
            }
        });

        btnAddAlm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerAlm = view.findViewById(R.id.spinnerAlm); // Asegúrate de obtener la referencia correcta del Spinner
                HashMap<String, String> selectedHashMap = (HashMap<String, String>) spinnerAlm.getSelectedItem();
                String selectedId = selectedHashMap.get("id");

                dbManager.open();
                Producto producto = dbManager.getProductById(selectedId);

                LinearLayout linearLayoutDesc = new LinearLayout(getContext());
                linearLayoutDesc.setOrientation(LinearLayout.VERTICAL);

                TextView nuevoTextView = new TextView(getContext());
                nuevoTextView.setText(producto.getArticulo() + " - " + producto.getDescripcion());

                EditText cantidadEditText = new EditText(getContext());
                EditText costoEditText = new EditText(getContext());
                Button nuevoBoton = new Button(getContext());

                cantidadEditText.setHint("Cantidad");
                costoEditText.setHint("Precio");

                cantidadEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                costoEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                cantidadEditText.setGravity(Gravity.CENTER);
                costoEditText.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                layoutParams.weight = 1;

                cantidadEditText.setLayoutParams(layoutParams);
                costoEditText.setLayoutParams(layoutParams);

                linearLayoutDesc.addView(nuevoTextView);

                LinearLayout linearLayoutCantidadTotal = new LinearLayout(getContext());
                linearLayoutCantidadTotal.setOrientation(LinearLayout.HORIZONTAL);

                linearLayoutCantidadTotal.addView(cantidadEditText);
                linearLayoutCantidadTotal.addView(costoEditText);
                linearLayoutCantidadTotal.addView(nuevoBoton);

                nuevoBoton.setText("ELIMINAR");
                nuevoBoton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                nuevoBoton.setTextColor(Color.WHITE);

                double[] costoAnterior = {0.0};
                nuevoBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarLayoutAsociado((Button) v);
                    }
                });

                buttonLayoutMap.put(nuevoBoton, linearLayoutDesc);

                linearLayoutDesc.addView(linearLayoutCantidadTotal);

                nuevoAlmacenamiento.addView(linearLayoutDesc); // Agrega al contenedor de almacenamiento

                double costoProducto = Double.parseDouble(producto.getPrecio().replace(",", ""));
                if (producto.getImpuesto().equals("IVA")) {
                    costoProducto = costoProducto * 1.16;
                }
                costoProducto = Double.parseDouble(df.format(costoProducto));
                buttonCostMap.put(nuevoBoton, costoProducto);
                actualizarTotalCosto(costoProducto);

                cantidadEditText.setText("1");
                costoEditText.setText("" + costoProducto);

                costoEditText.addTextChangedListener(new TextWatcher() {
                    private final long DELAY = 1000;
                    private final Handler handler = new Handler();
                    private Runnable runnable;

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handler.removeCallbacks(runnable);
                        runnable = () -> {
                            String costoText = costoEditText.getText().toString().trim();
                            if (!costoText.isEmpty()) {
                                try {
                                    double nuevoCosto = Double.parseDouble(costoText);
                                    double costoAnterior = buttonCostMap.get(nuevoBoton);
                                    restarCostoEliminado(costoAnterior);
                                    buttonCostMap.put(nuevoBoton, nuevoCosto);
                                    actualizarTotalCosto(nuevoCosto);
                                } catch (NumberFormatException e) {
                                    // Manejar la excepción si el texto ingresado no es un número
                                }
                            }
                        };
                        handler.postDelayed(runnable, DELAY);
                    }
                });

                dbManager.close();
                contador.getAndIncrement();
            }
        });

        btnAddCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerCam = view.findViewById(R.id.spinnerCam); // Asegúrate de obtener la referencia correcta del Spinner
                HashMap<String, String> selectedHashMap = (HashMap<String, String>) spinnerCam.getSelectedItem();
                String selectedId = selectedHashMap.get("id");

                dbManager.open();
                Producto producto = dbManager.getProductById(selectedId);

                LinearLayout linearLayoutDesc = new LinearLayout(getContext());
                linearLayoutDesc.setOrientation(LinearLayout.VERTICAL);

                TextView nuevoTextView = new TextView(getContext());
                nuevoTextView.setText(producto.getArticulo() + " - " + producto.getDescripcion());

                EditText cantidadEditText = new EditText(getContext());
                EditText costoEditText = new EditText(getContext());
                Button nuevoBoton = new Button(getContext());

                cantidadEditText.setHint("Cantidad");
                costoEditText.setHint("Precio");

                cantidadEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                costoEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                cantidadEditText.setGravity(Gravity.CENTER);
                costoEditText.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                layoutParams.weight = 1;

                cantidadEditText.setLayoutParams(layoutParams);
                costoEditText.setLayoutParams(layoutParams);

                linearLayoutDesc.addView(nuevoTextView);

                LinearLayout linearLayoutCantidadTotal = new LinearLayout(getContext());
                linearLayoutCantidadTotal.setOrientation(LinearLayout.HORIZONTAL);

                linearLayoutCantidadTotal.addView(cantidadEditText);
                linearLayoutCantidadTotal.addView(costoEditText);
                linearLayoutCantidadTotal.addView(nuevoBoton);

                nuevoBoton.setText("ELIMINAR");
                nuevoBoton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                nuevoBoton.setTextColor(Color.WHITE);

                double[] costoAnterior = {0.0};
                nuevoBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarLayoutAsociado((Button) v);
                    }
                });

                buttonLayoutMap.put(nuevoBoton, linearLayoutDesc);

                linearLayoutDesc.addView(linearLayoutCantidadTotal);

                nuevasCamaras.addView(linearLayoutDesc); // Agrega al contenedor de cámaras

                double costoProducto = Double.parseDouble(producto.getPrecio());
                if (producto.getImpuesto().equals("IVA")) {
                    costoProducto = costoProducto * 1.16;
                }
                costoProducto = Double.parseDouble(df.format(costoProducto));
                buttonCostMap.put(nuevoBoton, costoProducto);
                actualizarTotalCosto(costoProducto);

                cantidadEditText.setText("1");
                costoEditText.setText("" + costoProducto);

                costoEditText.addTextChangedListener(new TextWatcher() {
                    private final long DELAY = 1000;
                    private final Handler handler = new Handler();
                    private Runnable runnable;

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No se usa en este caso
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        handler.removeCallbacks(runnable);
                        runnable = () -> {
                            String costoText = costoEditText.getText().toString().trim();
                            if (!costoText.isEmpty()) {
                                try {
                                    double nuevoCosto = Double.parseDouble(costoText);
                                    double costoAnterior = buttonCostMap.get(nuevoBoton);
                                    restarCostoEliminado(costoAnterior);
                                    buttonCostMap.put(nuevoBoton, nuevoCosto);
                                    actualizarTotalCosto(nuevoCosto);
                                } catch (NumberFormatException e) {

                                }
                            }
                        };
                        handler.postDelayed(runnable, DELAY);
                    }
                });

                dbManager.close();
                contador.getAndIncrement();
            }
        });

        btnAddGra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinnerGra = view.findViewById(R.id.spinnerGra);
                HashMap<String, String> selectedHashMap = (HashMap<String, String>) spinnerGra.getSelectedItem();
                String selectedId = selectedHashMap.get("id");

                dbManager.open();
                Producto producto = dbManager.getProductById(selectedId);

                LinearLayout linearLayoutDesc = new LinearLayout(getContext());
                linearLayoutDesc.setOrientation(LinearLayout.VERTICAL);

                TextView nuevoTextView = new TextView(getContext());
                nuevoTextView.setText(producto.getArticulo() + " - " + producto.getDescripcion());

                EditText cantidadEditText = new EditText(getContext());
                EditText costoEditText = new EditText(getContext());
                TextView totalTextView = new TextView(getContext()); // Cambiado a TextView para mostrar el total
                Button nuevoBoton = new Button(getContext());

                cantidadEditText.setHint("Cantidad");
                costoEditText.setHint("Precio");

                cantidadEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                costoEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                cantidadEditText.setGravity(Gravity.CENTER);
                costoEditText.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

                cantidadEditText.setLayoutParams(layoutParams);
                costoEditText.setLayoutParams(layoutParams);
                totalTextView.setLayoutParams(layoutParams);

                linearLayoutDesc.addView(nuevoTextView);

                LinearLayout linearLayoutCantidadTotal = new LinearLayout(getContext());
                linearLayoutCantidadTotal.setOrientation(LinearLayout.HORIZONTAL);

                linearLayoutCantidadTotal.addView(cantidadEditText);
                linearLayoutCantidadTotal.addView(costoEditText);
                linearLayoutCantidadTotal.addView(totalTextView); // Agrega el TextView para mostrar el total
                linearLayoutCantidadTotal.addView(nuevoBoton);

                nuevoBoton.setText("ELIMINAR");
                nuevoBoton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                nuevoBoton.setTextColor(Color.WHITE);

                double[] costoAnterior = {0.0};
                nuevoBoton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eliminarLayoutAsociado((Button) v);
                    }
                });

                buttonLayoutMap.put(nuevoBoton, linearLayoutDesc);

                linearLayoutDesc.addView(linearLayoutCantidadTotal);

                nuevoGrabador.addView(linearLayoutDesc); // Agrega al contenedor de gráficos

                double costoProducto = Double.parseDouble(producto.getPrecio());
                if (producto.getImpuesto().equals("IVA")) {
                    costoProducto = costoProducto * 1.16;
                }
                costoProducto = Double.parseDouble(df.format(costoProducto));
                buttonCostMap.put(nuevoBoton, costoProducto);
                actualizarTotalCosto(costoProducto);

                cantidadEditText.setText("1");
                costoEditText.setText("" + costoProducto);

                // Calcula y muestra el total al inicio
                double cantidadInicial = Double.parseDouble(cantidadEditText.getText().toString());
                double totalInicial = cantidadInicial * costoProducto;
                totalTextView.setText(String.valueOf(totalInicial));

                // Lógica para calcular el total en tiempo real cuando cambia la cantidad o el costo
                TextWatcher watcher = new TextWatcher() {
                    private final Handler handler = new Handler();
                    private Runnable runnable;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        handler.removeCallbacks(runnable);
                        runnable = () -> {
                            double cantidad = 0, costo = 0;
                            if (!cantidadEditText.getText().toString().isEmpty()) {
                                cantidad = Double.parseDouble(cantidadEditText.getText().toString());
                            }
                            if (!costoEditText.getText().toString().isEmpty()) {
                                costo = Double.parseDouble(costoEditText.getText().toString());
                            }
                            double total = cantidad * costo;
                            totalTextView.setText(String.valueOf(total));
                        };
                        handler.postDelayed(runnable, 500); // Ajusta el tiempo de retraso según sea necesario
                    }
                };

                cantidadEditText.addTextChangedListener(watcher);
                costoEditText.addTextChangedListener(watcher);

                dbManager.close();
                contador.getAndIncrement();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null) {
            mainActivity.loadData();
        }
    }

    public void borrarElementos() {
        //LinearLayout nuevosElementosContainer = getView().findViewById(R.id.contenedorAcc);
        //nuevosElementosContainer.removeAllViews();

        if (nuevosAccesorios != null) {
            nuevosAccesorios.removeAllViews();
        } else {
            Log.e("CCTVFragment", "El contenedor de elementos es nulo.");
        }

        if (nuevosCables != null) {
            nuevosCables.removeAllViews();
        } else {
            Log.e("CCTVFragment", "El contenedor de elementos es nulo.");
        }

        if (nuevaFuente != null) {
            nuevaFuente.removeAllViews();
        } else {
            Log.e("CCTVFragment", "El contenedor de elementos es nulo.");
        }

        if (nuevoAlmacenamiento != null) {
            nuevoAlmacenamiento.removeAllViews();
        } else {
            Log.e("CCTVFragment", "El contenedor de elementos es nulo.");
        }

        if (nuevasCamaras != null) {
            nuevasCamaras.removeAllViews();
        } else {
            Log.e("CCTVFragment", "El contenedor de elementos es nulo.");
        }

        if (nuevoGrabador != null) {
            nuevoGrabador.removeAllViews();
        } else {
            Log.e("CCTVFragment", "El contenedor de elementos es nulo.");
        }

        totalCostoProducto = 0.0;
        mostrarTotalCostoEnTextView();
    }

    private void eliminarLayoutAsociado(Button button) {
        if (buttonLayoutMap.containsKey(button)) {
            LinearLayout layoutToDelete = buttonLayoutMap.get(button);
            double costoEliminado = buttonCostMap.get(button);

            if (nuevosAccesorios.indexOfChild(layoutToDelete) != -1) {
                nuevosAccesorios.removeView(layoutToDelete);
            } else if (nuevosCables.indexOfChild(layoutToDelete) != -1) {
                nuevosCables.removeView(layoutToDelete);
            } else if (nuevaFuente.indexOfChild(layoutToDelete) != -1) {
                nuevaFuente.removeView(layoutToDelete);
            } else if (nuevoAlmacenamiento.indexOfChild(layoutToDelete) != -1) {
                nuevoAlmacenamiento.removeView(layoutToDelete);
            } else if (nuevasCamaras.indexOfChild(layoutToDelete) != -1) {
                nuevasCamaras.removeView(layoutToDelete);
            } else if (nuevoGrabador.indexOfChild(layoutToDelete) != -1) {
                nuevoGrabador.removeView(layoutToDelete);
            }

            buttonLayoutMap.remove(button);
            buttonCostMap.remove(button);

            restarCostoEliminado(costoEliminado);
        }
    }

    private void actualizarTotalCosto(double costo) {
        totalCostoProducto += costo;
        mostrarTotalCostoEnTextView();
    }

    private void restarCostoEliminado(double costo) {
        totalCostoProducto -= costo;
        mostrarTotalCostoEnTextView();
    }

    public void mostrarTotalCostoEnTextView() {
        ((MainActivity) getActivity()).actualizarTotalCostoEnActivity(totalCostoProducto);
    }

}