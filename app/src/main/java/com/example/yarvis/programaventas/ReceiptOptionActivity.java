package com.example.yarvis.programaventas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ReceiptOptionActivity extends AppCompatActivity {

    ProgressDialog pDialog;

    Button btnEdit;
    Button btnAdd;
    Button btnDelete;
    Button btnCancel;

    private static StaticVariables staticVariables = new StaticVariables();

    // JSON Node names
    private static final String TAG_SUCCESS = staticVariables.getTagSuccess();
    private static final String TAG_PRODUCTS = staticVariables.getTagProducts();
    private static final String TAG_PID = staticVariables.getTagPid();
    private static final String TAG_NAME = staticVariables.getTagName();
    private static final String TAG_PRICE = staticVariables.getTagPrice();
    private static final String TAG_QUANT = staticVariables.getTagQuant();

    String rid;
    int uid;
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_option);

        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnEdit = (Button) findViewById(R.id.buttonEdit);
        btnDelete = (Button) findViewById(R.id.buttonDelete);
        btnCancel = (Button) findViewById(R.id.buttonCancel);

        Intent intent = getIntent();
        uid = intent.getIntExtra("UserID",0);
        rid = intent.getStringExtra(TAG_PID);

        System.err.println("Recibo ID: "+rid);
        System.err.println("Usuario ID: "+uid);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddReceiptActivity.class);
                i.putExtra("UserID", uid);
                i.putExtra(TAG_PID, rid);
                startActivity(i);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditReceiptActivity.class);
                i.putExtra("UserID", uid);
                i.putExtra(TAG_PID, rid);
                startActivity(i);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeleteReceiptActivity.class);
                i.putExtra("UserID", uid);
                i.putExtra(TAG_PID, rid);
                startActivity(i);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receipt_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
