package de.fh_muenster.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    private static final String TAG = LoginActivity.class.getName();
    private EditText name;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText)findViewById(R.id.txtName);
        password = (EditText)findViewById(R.id.txtPassword);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void clickLogin(View view) {
        //Server Kommunikation
        new LoginTask().execute(name.getText().toString(), password.getText().toString());
    }

    public void clickToRegister(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }


    class LoginTask extends AsyncTask<String, String, Integer> {
        private ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
        private ChatApplication myApp = (ChatApplication) getApplication();

        @Override
        protected void onPreExecute()
        {
            Dialog.setMessage("Login...");
            Dialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            UseCases useCases = new UseCases(myApp);
            Integer response = useCases.login(params[0],params[1]);
            return response;
        }

        @Override
        protected void onPostExecute (Integer response) {
            Dialog.dismiss();
            Log.d(TAG, "Rückgabe: " + response.toString());
            if(response == 1) {
                Intent i = new Intent(LoginActivity.this, ChatActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Erfolgreich eingeloggt", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Fehler", Toast.LENGTH_LONG).show();
            }
        }
    }

}
