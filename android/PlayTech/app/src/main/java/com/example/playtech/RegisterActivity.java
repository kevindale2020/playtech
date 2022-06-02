package com.example.playtech;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout et_firstname;
    private TextInputLayout et_lastname;
    private TextInputLayout et_address;
    private TextInputLayout et_email;
    private TextInputLayout et_contact;
    private TextInputLayout et_username;
    private TextInputLayout et_password;
    private ProgressBar loading;
    private Button btnRegister;
    private String fname;
    private String lname;
    private String address;
    private String email;
    private String contact;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Parsing
        et_firstname = findViewById(R.id.etFirstName);
        et_lastname = findViewById(R.id.etLastName);
        et_address = findViewById(R.id.etAddress);
        et_email = findViewById(R.id.etEmail);
        et_contact = findViewById(R.id.etContact);
        et_username = findViewById(R.id.etUsername);
        et_password = findViewById(R.id.etPassword);
        loading = findViewById(R.id.loading);
        btnRegister = findViewById(R.id.btnRegister);

        // Focus
        et_firstname.getEditText().requestFocus();

        // register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = et_firstname.getEditText().getText().toString().trim();
                lname = et_lastname.getEditText().getText().toString().trim();
                address = et_address.getEditText().getText().toString().trim();
                email = et_email.getEditText().getText().toString().trim();
                contact = et_contact.getEditText().getText().toString().trim();
                username = et_username.getEditText().getText().toString().trim();
                password = et_password.getEditText().getText().toString().trim();

                if(!validateFirstName() | !validateLastName() | !validateAddress() |  !validateEmail() | !validateContact() | !validateUsername() | !validatePassword() ) {
                    return;
                }

                register();
            }
        });

    }

    public void register() {
        loading.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.GONE);
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        // url = "http://192.168.43.44:8080/IRO/Android/register.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Register: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if(success.equals("1")) {

                                loading.setVisibility(View.GONE);
                                btnRegister.setVisibility(View.VISIBLE);

                                clear();

                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                                builder.setTitle("Registration");
                                builder.setMessage(message);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing but close the dialog
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        //dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                            else if(success.equals("2")) {
                                et_email.setError(message);
                                loading.setVisibility(View.GONE);
                                btnRegister.setVisibility(View.VISIBLE);
                            }
                            else {
                                et_username.setError(message);
                                loading.setVisibility(View.GONE);
                                btnRegister.setVisibility(View.VISIBLE);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Failed" +e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btnRegister.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_register");
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("address", address);
                params.put("email", email);
                params.put("contact", contact);
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public void login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean validateFirstName() {
        if(fname.isEmpty()) {
            et_firstname.setError("This is required");
            return false;
        } else {
            et_firstname.setError(null);
            et_firstname.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateLastName() {
        if(lname.isEmpty()) {
            et_lastname.setError("This is required");
            return false;
        } else {
            et_lastname.setError(null);
            et_lastname.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateAddress() {
        if(address.isEmpty()) {
            et_address.setError("This is required");
            return false;
        } else {
            et_address.setError(null);
            et_address.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateEmail() {
        if(email.isEmpty()) {
            et_email.setError("This is required");
            return false;
        } else {
            et_email.setError(null);
            et_email.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateContact() {
        if(contact.isEmpty()) {
            et_contact.setError("This is required");
            return false;
        } else {
            et_contact.setError(null);
            et_email.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateUsername() {
        if(username.isEmpty()) {
            et_username.setError("This is required");
            return false;
        } else {
            et_username.setError(null);
            et_email.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatePassword() {
        if(password.isEmpty()) {
            et_password.setError("This is required");
            return false;
        } else {
            et_password.setError(null);
            et_password.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                //action
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clear() {
        et_firstname.getEditText().setText("");
        et_lastname.getEditText().setText("");
        et_address.getEditText().setText("");
        et_email.getEditText().setText("");
        et_contact.getEditText().setText("");
        et_username.getEditText().setText("");
        et_password.getEditText().setText("");
    }
}
