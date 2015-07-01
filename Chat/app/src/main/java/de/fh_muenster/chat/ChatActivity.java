package de.fh_muenster.chat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ChatActivity extends ActionBarActivity {
    private ListView list;

    private static final String TAG = ChatActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        list = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetMessageTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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

    public void clickNewMessage(MenuItem item) {
        Intent i = new Intent(ChatActivity.this, NewMessageActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Wirklich ausloggen?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChatApplication myApp = (ChatApplication) getApplication();
                        myApp.setMessage(null);
                        myApp.setName(null);
                        myApp.setPrivkey_user(null);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Nein", null)
                .show();
    }

    public void clickLogout(MenuItem item) {
        new AlertDialog.Builder(this)
                .setMessage("Wirklich ausloggen?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChatApplication myApp = (ChatApplication) getApplication();
                        myApp.setMessage(null);
                        myApp.setName(null);
                        myApp.setPrivkey_user(null);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Nein", null)
                .show();
    }

    class GetMessageTask extends AsyncTask<String, String, ArrayList<String[]>> {
        private ProgressDialog Dialog = new ProgressDialog(ChatActivity.this);
        private ChatApplication myApp = (ChatApplication) getApplication();

        @Override
        protected void onPreExecute()
        {
            Dialog.setMessage("Nachrichten abrufen...");
            Dialog.show();
        }

        @Override
        protected ArrayList<String[]> doInBackground(String... params) {
            UseCases useCases = new UseCases(myApp);
            ArrayList<String[]> response = useCases.receiveMessage(myApp.getName());
            return response;
        }

        @Override
        protected void onPostExecute (final ArrayList<String[]> response) {
            Dialog.dismiss();
            if(response != null) {
                Toast.makeText(getApplicationContext(), "Nachrichten erfolgreich abgerufen", Toast.LENGTH_LONG).show();
                ArrayList<String> valueList = new ArrayList<>();
                for(int i=0; i<response.size(); i++) {
                    valueList.add(response.get(i)[0] + " | " + (response.get(i)[1]).substring(0,5) + "...");
                }
                //ListView Objekt mit Daten füllen
                ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, valueList);
                final ListView lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                        myApp.setMessage(response.get(myItemInt));
                        Log.d(TAG, myApp.getMessage()[0] + " " + myApp.getMessage()[1]);
                        Intent i = new Intent(ChatActivity.this, ShowMessageActivity.class);
                        startActivity(i);
                    }

                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Keine Nachrichten vorhanden", Toast.LENGTH_LONG).show();
            }
        }
    }


}
