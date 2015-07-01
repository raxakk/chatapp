package de.fh_muenster.chat;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends ActionBarActivity {

    private static final String TAG = RegisterActivity.class.getName();
    private EditText name;
    private EditText password;
    private EditText passwordConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText)findViewById(R.id.txtName);
        password = (EditText)findViewById(R.id.txtPassword);
        passwordConf = (EditText)findViewById(R.id.txtPasswordConfirmation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void clickRegister(View view) {
        if(password.getText().toString().equals(passwordConf.getText().toString())) {
            //Server Kommunikation
            new RegisterTask().execute(name.getText().toString(),password.getText().toString());
        }
        else {
            Toast.makeText(getApplicationContext(), "Passwörter stimmen nicht überein", Toast.LENGTH_LONG).show();
        }
    }

    class RegisterTask extends AsyncTask<String, String, Integer> {
        private ProgressDialog Dialog = new ProgressDialog(RegisterActivity.this);
        private         ChatApplication myApp = (ChatApplication) getApplication();

        @Override
        protected void onPreExecute()
        {
            Dialog.setMessage("Registrieren...");
            Dialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            UseCases useCases = new UseCases(myApp);
            Integer response = useCases.register(params[0], params[1]);
            return response;
        }

        @Override
        protected void onPostExecute (Integer response) {
            Dialog.dismiss();
            Log.d(TAG, "Rückgabe: " + response.toString());
            if(response == 1) {
                Toast.makeText(getApplicationContext(), "Erfolgreich registriert", Toast.LENGTH_LONG).show();
                RegisterActivity.this.finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Fehler", Toast.LENGTH_LONG).show();
            }
        }
    }
}
