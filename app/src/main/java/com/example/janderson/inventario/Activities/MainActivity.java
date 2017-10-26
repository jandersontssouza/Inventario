package com.example.janderson.inventario.Activities;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.janderson.inventario.Model.CodigoDeBarras;
import com.example.janderson.inventario.Model.Excel;
import com.example.janderson.inventario.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener  {

    EditText txtLocal;
    private GoogleApiClient mGoogleApiClient;
    private String[] MinhasPermissoes = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText txtTombo = (EditText) findViewById(R.id.txtTombo);
        final EditText txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        txtLocal = (EditText) findViewById(R.id.txtLocal);
        ImageView grava = (ImageView) findViewById(R.id.imgGrava);
        ImageView icone = (ImageView) findViewById(R.id.imgIcon);

        //Verificar concessão de permissão
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){

        } else {
        //Solicitar permissão para usuário
            ActivityCompat.requestPermissions(this, MinhasPermissoes, 1);
        }

       final Excel e = new Excel();
        if (e.verificaArquivo())
            Toast.makeText(getApplicationContext(), "Arquivo Inventario.xls criado agora", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Trabalhando em Inventario.xls", Toast.LENGTH_SHORT).show();

        grava.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if (txtTombo.getText().toString().equals("") || txtLocal.getText().toString().equals("") || txtDescricao.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        e.gravaLinha(txtTombo.getText().toString(), txtLocal.getText().toString(), txtDescricao.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplication(), "Dados gravados", Toast.LENGTH_SHORT).show();
                    txtTombo.setText("");
                    txtDescricao.setText("");
                    txtLocal.setText("");
                }
                }
            }
        );
        icone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CodigoDeBarras cdb = new CodigoDeBarras(activity);
                activity = cdb.escanear();
                callConnection();

            }
         }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "Escaneamento cancelado", Toast.LENGTH_SHORT).show();
            }
            else{
                final EditText txtTombo = (EditText) findViewById(R.id.txtTombo);
                EditText txtDescricao = (EditText) findViewById(R.id.txtDescricao);
                txtTombo.setText(result.getContents());
                callConnection();
                //txtDescricao.requestFocus();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private synchronized void callConnection(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();


    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("LOG", "onConnected("+bundle+")");
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(l != null){
            Log.i("LOG", "Latitude: " + l.getLatitude());
            Log.i("LOG", "Longitude: " + l.getLongitude());
            if(l.getLatitude()<= -3.829746 && l.getLatitude()>= -3.830637 && l.getLongitude()>= -49.664505 && l.getLongitude()<=-49.663151)
                txtLocal.setText("CAMTUC");
            if(l.getLatitude()<= -3.817808 && l.getLatitude()>= -3.818067 && l.getLongitude()>= -49.664900 && l.getLongitude()<= -49.664591)
                txtLocal.setText("IFPA - Bloco 01");
            if(l.getLatitude()<= -3.817623 && l.getLatitude()>= -3.817875 && l.getLongitude()>= -49.664645 && l.getLongitude()<= -49.664343)
                txtLocal.setText("IFPA - Bloco 02");
            if(l.getLatitude()<= -3.817623 && l.getLatitude()>= -3.817875 && l.getLongitude()>= -49.664645 && l.getLongitude()<= -49.664343)
                txtLocal.setText("IFPA - Bloco 02");
            else
                txtLocal.setText(l.getLatitude() + " | " + l.getLongitude());
            System.out.println(l.getLatitude() + " | " + l.getLongitude());

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Sem terminada", Toast.LENGTH_SHORT).show();
        Log.i("LOG", "onConnectionSuspended("+i+")");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Sem conexão", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onLocationChanged(Location location) {
        txtLocal.setText(Html.fromHtml(location.getLatitude() + " | " + location.getLongitude() + " | " + location.getBearing() + " | " +
        location        + " | " + "continuar..."));
    }
}
