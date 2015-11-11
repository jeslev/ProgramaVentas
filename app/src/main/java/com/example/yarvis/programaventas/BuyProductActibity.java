package com.example.yarvis.programaventas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyProductActibity extends AppCompatActivity {

    ProgressDialog pDialog;

    TextView txtItem;
    TextView txtPrice;
    TextView txtCant;
    EditText qnt;
    Button btn;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "productos";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "nombre";
    private static final String TAG_PRICE = "precio";
    private static final String TAG_QUANT = "cant";

    String pid;
    int uid, maxqnt,valqnt;
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product_actibity);

        txtItem = (TextView) findViewById(R.id.efname);
        txtPrice = (TextView) findViewById(R.id.efprice);
        txtCant = (TextView) findViewById(R.id.efcant);
        qnt = (EditText) findViewById(R.id.efqntTxt);

        btn = (Button) findViewById(R.id.efbtn);

        txtItem.setText("Producto: ");
        txtPrice.setText("S/. ");
        txtCant.setText("Cantidad: ");
        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_PID);
        uid = i.getIntExtra("UserID", 0);

        maxqnt = 0;

        new getDetails().execute();
        //consulta datos de producto

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String qntity = qnt.getText().toString();
                valqnt = new Integer(qntity).intValue();
                Log.d("String: ", pid+" "+qntity);

                //revisa si cantidad es menor que maximo
                if(valqnt>maxqnt || valqnt<1){
                    Toast toast = Toast.makeText(getApplicationContext(), "No existe suficientes productos", Toast.LENGTH_LONG);
                    toast.show();
                }

                //guarda en bd
                else{
                    Log.e("Add product", uid + " compro " + pid + " cant: " + valqnt);
                    new addToBuy().execute();
                }
                finish();
            }
        });
    }

    class addToBuy extends AsyncTask<String, String, String> {

        private String url_add_product= "http://192.168.0.3/lab3/create_product.php";
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BuyProductActibity.this);
            pDialog.setMessage("Agregando producto..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pid", pid));
            params.add(new BasicNameValuePair("uid", ""+uid));
            params.add(new BasicNameValuePair("qnty", ""+valqnt));
            Log.e("String: ", pid);
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jParser.makeHttpRequest(url_add_product,"POST", params);

            // check log cat fro response
            Log.e("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully get product


                } else {
                    // failed to get product

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


    class getDetails extends AsyncTask<String, String, String> {

        private String url_detail_product= "http://192.168.0.3/lab3/get_product_details.php";
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BuyProductActibity.this);
            pDialog.setMessage("Consultando datos..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pid", pid));
            Log.e("String: ", pid );
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jParser.makeHttpRequest(url_detail_product,"GET", params);

            // check log cat fro response
            Log.e("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully get product
                    JSONArray products = json.getJSONArray("product");
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String price = c.getString(TAG_PRICE);
                        String quant = c.getString(TAG_QUANT);

                        txtItem.setText("Producto: "+name);
                        txtPrice.setText("S/. "+price);
                        maxqnt = new Integer(quant).intValue();

                    }

                } else {
                    // failed to get product

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
