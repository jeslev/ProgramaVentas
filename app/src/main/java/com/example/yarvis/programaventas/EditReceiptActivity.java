package com.example.yarvis.programaventas;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditReceiptActivity extends ListActivity {

    private static StaticVariables staticVariables = new StaticVariables();

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;

    HashMap<String,Integer> productsBuying;


    // url to get all products list
    private static String url_all_products = "http://"+staticVariables.getIpServer()+"/lab3/get_all_products_receipt.php";

    // JSON Node names
    private static final String TAG_SUCCESS = staticVariables.getTagSuccess();
    private static final String TAG_PRODUCTS = staticVariables.getTagProducts();
    private static final String TAG_PID = staticVariables.getTagPid();
    private static final String TAG_NAME = staticVariables.getTagName();
    private static final String TAG_PRICE = staticVariables.getTagPrice();
    private static final String TAG_QUANT = staticVariables.getTagQuant();

    // products JSONArray
    JSONArray products = null;

    public Button btnFinish;

    int uid;
    String rid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receipt);
        btnFinish = (Button) findViewById(R.id.button);
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();


        Intent intent = getIntent();
        uid = intent.getIntExtra("UserID",0);
        rid = intent.getStringExtra(TAG_PID);
        productsBuying = new HashMap<String,Integer>();
        System.err.println("Datos: "+uid + "-" + rid);
        // Loading products in Background Thread
        new LoadAllProducts().execute();

        // Get listview
        ListView lv = getListView();


        final View mylayout=getLayoutInflater().inflate(R.layout.quantity, null);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(), BuyProductReceiptActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);
                in.putExtra("UserID", uid);
                in.putExtra("ReceiptID", rid);

                startActivity(in);

            }
        });


        btnFinish.setOnClickListener(
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
        getMenuInflater().inflate(R.menu.menu_edit_receipt, menu);
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

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditReceiptActivity.this);
            pDialog.setMessage("Cargando productos...");
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
            params.add(new BasicNameValuePair("idr", ""+rid));
            System.err.println("Valor: "+rid);
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Receipt All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String price = c.getString(TAG_PRICE);
                        String quant = c.getString(TAG_QUANT);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        //map.put(TAG_PID, "5");
                        map.put(TAG_NAME, name);
                        map.put(TAG_PRICE, price);
                        map.put(TAG_QUANT, quant);

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            EditReceiptActivity.this, productsList,
                            R.layout.list_item, new String[] { TAG_PID,
                            TAG_NAME, TAG_PRICE, TAG_QUANT},
                            new int[] { R.id.pid, R.id.name, R.id.price, R.id.cant });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}


