package com.example.yarvis.programaventas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class UserOptionActivity extends AppCompatActivity {

    Button btnCrear;
    Button btnEditar;
    Button btnPagar;
    Button btnSalir;

    int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);

        btnCrear = (Button) findViewById(R.id.btnRecibo);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnPagar = (Button) findViewById(R.id.btnPagar);
        btnSalir = (Button) findViewById(R.id.btnSalir);

        Intent intent = getIntent();
        uid = intent.getIntExtra("UserID",0);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BuyActivity.class);
                i.putExtra("UserID", uid);
                startActivity(i);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditActivity.class);
                i.putExtra("UserID",uid);
                startActivity(i);
            }
        });

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PayListReceiptActivity.class);
                i.putExtra("UserID",uid);
                startActivity(i);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
