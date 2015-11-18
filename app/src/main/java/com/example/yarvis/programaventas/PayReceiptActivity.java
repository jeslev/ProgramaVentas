package com.example.yarvis.programaventas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PayReceiptActivity extends AppCompatActivity {

    private static StaticVariables staticVariables = new StaticVariables();

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    HashMap<String,Integer> productsBuying;


    // url to get all products list
    private static String url_all_products = "http://"+staticVariables.getIpServer()+"/lab3/pay_receipt.php";

    // JSON Node names
    private static final String TAG_SUCCESS = staticVariables.getTagSuccess();
    private static final String TAG_PRODUCTS = staticVariables.getTagProducts();
    private static final String TAG_PID = staticVariables.getTagPid();
    private static final String TAG_NAME = staticVariables.getTagName();
    private static final String TAG_PRICE = staticVariables.getTagPrice();
    private static final String TAG_QUANT = staticVariables.getTagQuant();

    // products JSONArray
    JSONArray products = null;

    public Button btnPay;
    public Button btnCancel;
    EditText codBanc;

    int uid;
    String rid, valCodBanc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_receipt);

        codBanc = (EditText) findViewById(R.id.editCodBanc);
        btnPay = (Button) findViewById(R.id.buttonPay);
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();


        Intent intent = getIntent();
        uid = intent.getIntExtra("UserID",0);
        rid = intent.getStringExtra(TAG_PID);

        //final View mylayout=getLayoutInflater().inflate(R.layout.quantity, null);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valCodBanc = codBanc.getText().toString();
                Log.d("String: ", rid+" "+valCodBanc);


                // setear estado a 0 del recibo
                new pagaRecibo().execute();
                //abrir activity para pagar
                finish();
            }
        });

        btnCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pay_receipt, menu);
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

    class pagaRecibo extends AsyncTask<String, String, String> {

        private  String url_terminar_recibo = "http://"+staticVariables.getIpServer()+"/lab3/update_receipt.php";

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PayReceiptActivity.this);
            pDialog.setMessage("Pagando recibo...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("rid", ""+rid));
            params.add(new BasicNameValuePair("codBanc", ""+valCodBanc));

            System.err.println("RID: " + rid);
            System.err.println("CODBANC: " + valCodBanc);

            // getting JSON     string from URL
            JSONObject json = jParser.makeHttpRequest(url_terminar_recibo, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    //se agrego recibo, inicia intent a
                    // activity de pagar recibo
                } else {
                    // no products found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }

}
