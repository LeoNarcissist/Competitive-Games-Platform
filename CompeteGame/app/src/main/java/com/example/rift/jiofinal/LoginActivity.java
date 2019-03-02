package com.example.rift.jiofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Form;
import com.afollestad.bridge.Request;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    public static final String URL = "http://dobbster.ml/jiogame/signin.php";
    TextView _signupLink;
    static String uid = "";
    public ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        customFont();
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    //Custom Font
    public void customFont() {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Signatra.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    //Custom Font End

    public void login() {
        Log.d(TAG, "Login");

        validate();

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(LoginActivity.this, ActivitesActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public void validate() {

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!(isEmpty(_emailText) && isEmpty(_passwordText))) {
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_LONG).show();
            return;
        }

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("email", email);
        uriBuilder.appendQueryParameter("passwd", password);

        Check job = new Check(this);
        job.execute(uriBuilder.toString());
    }

    boolean isEmpty(EditText etText)
    {
        if(etText.getText().toString().trim().length() > 0)
            return true;
        return false;
    }
}

class Check extends AsyncTask<String, Void, String> {

        LoginActivity mActivity;

        public Check(LoginActivity activity) {
            mActivity = activity;
        }

    @Override
    protected String doInBackground(String[] params) {
       String uid = JsonParser.initiateConnection(params[0]);
       // String uid = JsonParser.initiateConnection("https://reqres.in/api/users%20?page=2");
        Log.e("EventExtractData", "Error response code : " + params[0]);
        return uid;
    }

    @Override
    protected void onPostExecute(String result) {

        LoginActivity.uid = result;

        if (mActivity.uid.equals("null") || mActivity.uid.equals("")) {
            Toast.makeText(mActivity, "User does not exists or password incorrect", Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            mActivity.onLoginFailed();
                            // onLoginFailed();
                            mActivity.progressDialog.dismiss();
                        }
                    }, 3000);
            return;
        } else {
            SaveSharedPreference.setLoggedIn(mActivity, true);
            SaveSharedPreference.setUserId(mActivity,LoginActivity.uid);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            mActivity.onLoginSuccess();
                            // onLoginFailed();
                            mActivity.progressDialog.dismiss();
                        }
                    }, 3000);
            return;
        }


    }
}

