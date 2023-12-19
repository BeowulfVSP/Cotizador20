package com.vspcom.cotizador20.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vspcom.cotizador20.MainActivity;
import com.vspcom.cotizador20.R;

import java.util.concurrent.atomic.AtomicInteger;

public class CCTV extends Fragment {

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
        LinearLayout nuevosElementosContainer = view.findViewById(R.id.contenedorAcc);

        AtomicInteger contador = new AtomicInteger(1);

        btnAddAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nuevoTextView = new TextView(getContext());
                nuevoTextView.setText("Información del spinner aquí");
                nuevosElementosContainer.addView(nuevoTextView);

                EditText nuevoCampo1 = new EditText(getContext());
                EditText nuevoCampo2 = new EditText(getContext());

                nuevoCampo1.setHint("Campo 1");
                nuevoCampo2.setHint("Campo 2");

                nuevosElementosContainer.addView(nuevoCampo1);
                nuevosElementosContainer.addView(nuevoCampo2);

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
}