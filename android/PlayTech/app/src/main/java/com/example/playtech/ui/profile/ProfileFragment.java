package com.example.playtech.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.playtech.AppController;
import com.example.playtech.MainActivity;
import com.example.playtech.R;
import com.example.playtech.RegisterActivity;
import com.example.playtech.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;
    private String user_id;
    private ImageView imageView;
    private ImageView iconEdit;
    private TextInputLayout etFirstName;
    private TextInputLayout etLastName;
    private TextInputLayout etAddress;
    private TextInputLayout etEmail;
    private TextInputLayout etContact;
    private TextInputLayout etUsername;
    private TextInputLayout etPassword;
    private ProgressBar loading;
    private Button btnSave;
    private Bitmap bitmap;
    private String displayName;
    private String fname;
    private String lname;
    private String address;
    private String email;
    private String contact;
    private String username;
    private String password;
    private String image;

    private static final int PICKFILE_RESULT_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //for the session values
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(sessionManager.USER_ID);

        // widgets
        imageView = root.findViewById(R.id.imageView);
        iconEdit = root.findViewById(R.id.iconEdit);
        etFirstName = root.findViewById(R.id.etFirstName);
        etLastName = root.findViewById(R.id.etLastName);
        etAddress = root.findViewById(R.id.etAddress);
        etEmail = root.findViewById(R.id.etEmail);
        etContact = root.findViewById(R.id.etContact);
        etUsername = root.findViewById(R.id.etUsername);
        etPassword = root.findViewById(R.id.etPassword);
        loading = root.findViewById(R.id.loading);
        btnSave = root.findViewById(R.id.btnSave);

        iconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = etFirstName.getEditText().getText().toString().trim();
                lname = etLastName.getEditText().getText().toString().trim();
                address = etAddress.getEditText().getText().toString().trim();
                email = etEmail.getEditText().getText().toString().trim();
                contact = etContact.getEditText().getText().toString().trim();
                username = etUsername.getEditText().getText().toString().trim();
                password = etPassword.getEditText().getText().toString().trim();

                if (!validateFirstName() | !validateLastName() | !validateAddress() | !validateEmail() | !validateContact() | !validateUsername()) {
                    return;
                }

                if(password.isEmpty()) {
                    password = "empty";
                }

                // save profile
                if (bitmap != null) {
                    image = getStringImage(bitmap);
                    saveProfile();
                } else {
                    image = "empty";
                    saveProfile();
                }
            }
        });

        getProfile();

        return root;
    }

    public void getProfile() {
        final ProgressDialog progressDialog;
        progressDialog = createProgressDialog(getContext());
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Profile: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                progressDialog.dismiss();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                JSONObject object = jsonArray.getJSONObject(0);

                                StringBuilder sb = new StringBuilder("http://192.168.137.1:8081/PlayTech/images/users/");
                                sb.append(object.getString("image"));

                                String imageURL = sb.toString();

                                // place data to UI
                                Picasso.get().load(imageURL).into(imageView);
                                etFirstName.getEditText().setText(object.getString("fname"));
                                etLastName.getEditText().setText(object.getString("lname"));
                                etAddress.getEditText().setText(object.getString("address"));
                                etEmail.getEditText().setText(object.getString("email"));
                                etContact.getEditText().setText(object.getString("contact"));
                                etUsername.getEditText().setText(object.getString("username"));

                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_get_profile");
                params.put("id", user_id);

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public void saveProfile() {
//        loading.setVisibility(View.VISIBLE);
//        btnSave.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Save Profile: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                getProfile();
//                                loading.setVisibility(View.GONE);
//                                btnSave.setVisibility(View.VISIBLE);
                                etPassword.getEditText().setText("");

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Update Profile");
                                builder.setMessage(message);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing but close the dialog
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Failed" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "mobile_save_profile");
                params.put("id", user_id);
                params.put("image", image);
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

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK
                    && null != data) {

                if (data.getData() != null) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                            cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                //imagesEncodedList.add(displayName);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                }
            } else {
                Toast.makeText(getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong " + e, Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.NO_WRAP);

        return encodedImage;
    }

    public boolean validateFirstName() {
        if (fname.isEmpty()) {
            etFirstName.setError("This is required");
            return false;
        } else {
            etFirstName.setError(null);
            etFirstName.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateLastName() {
        if (lname.isEmpty()) {
            etLastName.setError("This is required");
            return false;
        } else {
            etLastName.setError(null);
            etLastName.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateAddress() {
        if (address.isEmpty()) {
            etAddress.setError("This is required");
            return false;
        } else {
            etAddress.setError(null);
            etAddress.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateEmail() {
        if (email.isEmpty()) {
            etEmail.setError("This is required");
            return false;
        } else {
            etEmail.setError(null);
            etEmail.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateContact() {
        if (contact.isEmpty()) {
            etContact.setError("This is required");
            return false;
        } else {
            etContact.setError(null);
            etEmail.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateUsername() {
        if (username.isEmpty()) {
            etUsername.setError("This is required");
            return false;
        } else {
            etUsername.setError(null);
            etEmail.setErrorEnabled(false);
            return true;
        }
    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }
}
