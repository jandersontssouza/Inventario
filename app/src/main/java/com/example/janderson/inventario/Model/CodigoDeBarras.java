package com.example.janderson.inventario.Model;

import android.app.Activity;
import com.google.zxing.integration.android.IntentIntegrator;

public class CodigoDeBarras {
    private Activity activity;

    public CodigoDeBarras(Activity activity) {
        this.activity = activity;
    }

    public Activity escanear(){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Encontre o código de barras do Tombo");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();

        return activity;
    }
}
