package de.fh_muenster.chat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NewMessageActivity extends ActionBarActivity {
    private EditText empfaenger;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        empfaenger = (EditText) findViewById(R.id.txtEmpfaenger);
        message = (EditText) findViewById(R.id.txtNachricht);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_message, menu);
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

    public void clickToSend (View view) {
        new SendMessageTask().execute(empfaenger.getText().toString(), message.getText().toString());
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

    class SendMessageTask extends AsyncTask<String, String, Integer> {
        private ProgressDialog Dialog = new ProgressDialog(NewMessageActivity.this);
        private ChatApplication myApp = (ChatApplication) getApplication();

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Nachricht senden...");
            Dialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            UseCases useCases = new UseCases(myApp);
            Integer response = useCases.sendMessage(myApp.getName(), params[0], params[1]);
            return response;
        }

        @Override
        protected void onPostExecute(Integer response) {
            Dialog.dismiss();
            if (response == 1) {
                Toast.makeText(getApplicationContext(), "Nachricht erfolgreich gesendet", Toast.LENGTH_LONG).show();
                empfaenger.setText("");
                message.setText("");
                NewMessageActivity.this.finish();
            } else {
                Toast.makeText(getApplicationContext(), "Fehler", Toast.LENGTH_LONG).show();
            }
        }
    }
}
