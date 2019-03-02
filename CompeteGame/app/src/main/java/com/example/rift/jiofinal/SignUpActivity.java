package com.example.rift.jiofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class SignUpActivity extends AppCompatActivity {
    public static final String URL = "http://dobbster.ml/jiogame/signup.php";
    private static final String TAG = "SignUpActivity";
    EditText _emailText;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;
    EditText _fnameText;
    EditText _lnameText;
    static String uid = "";
    public ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        customFont();
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
        _fnameText = (EditText) findViewById(R.id.input_fname);
        _lnameText = (EditText) findViewById(R.id.input_lname);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the com.example.rift.jiofinal.Login activity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
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

    public void signup() {
        Log.d(TAG, "SignUp");

        String fname = _fnameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!(isEmpty(_emailText) && isEmpty(_passwordText) && isEmpty(_fnameText) && isEmpty(_lnameText)) ) {
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isEmailValid(email)) {
            Toast.makeText(getBaseContext(), "Invalid Emaild", Toast.LENGTH_LONG).show();
            return;
        }

         /*
        Getting Connectivity service.
         */
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            validate();
        }
        else {
            Toast.makeText(this, "Internet connection not available!", Toast.LENGTH_SHORT).show();
            return;
        }


        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "SignUp failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public void validate() {

        String fname = _fnameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("fname", fname);
        uriBuilder.appendQueryParameter("lname", lname);
        uriBuilder.appendQueryParameter("email", email);
        uriBuilder.appendQueryParameter("passwd", password);

        CheckSignup job = new CheckSignup(this);
        job.execute(uriBuilder.toString());

        return;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean isEmpty(EditText etText)
    {
        if(etText.getText().toString().trim().length() > 0)
            return true;
        return false;
    }
}

class CheckSignup extends AsyncTask<String, Void, String> {

    SignUpActivity mActivity;

    public CheckSignup(SignUpActivity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(String[] params) {
        String uid = JsonParser.initiateConnection(params[0]);
        Log.e("EventExtractData", "Error response code : " + params[0]);
        return uid;
    }

    @Override
    protected void onPostExecute(String result) {
        SignUpActivity.uid = result;
        if (SignUpActivity.uid.equals("null") || SignUpActivity.uid.equals("")) {
            Toast.makeText(mActivity, "Sign up Failed", Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            mActivity.onSignupFailed();
                            // onSignupFailed();
                            mActivity.progressDialog.dismiss();
                        }
                    }, 3000);
            return;
        } else
        {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            mActivity.onSignupSuccess();
                            // onSignupFailed();
                            mActivity.progressDialog.dismiss();
                        }
                    }, 3000);
        }
            return;
    }
}

