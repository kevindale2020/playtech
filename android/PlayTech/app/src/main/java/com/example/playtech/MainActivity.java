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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout et_username;
    private TextInputLayout et_password;
    private Button btnLogin;
    private String username;
    private String password;
    private ProgressBar loading;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        // Parsing
        et_username = findViewById(R.id.etUsername);
        et_password = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        loading = findViewById(R.id.loading);

        // Focus
        et_username.getEditText().requestFocus();

        // login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getEditText().getText().toString().trim();
                password = et_password.getEditText().getText().toString().trim();

                if(!validateUsername() | !validatePassword()) {
                    return;
                }

                login();
            }
        });

    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login() {
        loading.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        //String url = "http://192.168.43.44:8080/IRO/Android/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Login: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (success.equals("1")) {

//                                loading.setVisibility(View.GONE);
//                                btnLogin.setVisibility(View.VISIBLE);

                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                JSONObject object = jsonArray.getJSONObject(0);
                                int id = object.getInt("id");
                                String image = object.getString("image");
                                String fname = object.getString("fname");
                                String lname = object.getString("lname");
                                String address = object.getString("address");
                                String email = object.getString("email");
                                String contact = object.getString("contact");
                                String username = object.getString("username");

                                sessionManager.createSession(String.valueOf(id), image, fname, lname, address, email, contact, username);

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                builder.setTitle("Failed to login");
                                builder.setMessage(message);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing but close the dialog
                                        et_username.getEditText().setText("");
                                        et_password.getEditText().setText("");
                                        et_username.getEditText().requestFocus();
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                                loading.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_login");
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public boolean validateUsername() {
        if (username.isEmpty()) {
            et_username.setError("This is required");
            return false;
        } else {
            et_username.setError(null);
            et_username.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatePassword() {
        if (password.isEmpty()) {
            et_password.setError("This is required");
            return false;
        } else {
            et_password.setError(null);
            et_password.setErrorEnabled(false);
            return true;
        }
    }
}
