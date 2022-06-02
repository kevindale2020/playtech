package com.example.playtech.ui.cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.playtech.AppController;
import com.example.playtech.Cart;
import com.example.playtech.CartAdapter;
import com.example.playtech.CheckoutActivity;
import com.example.playtech.HomeActivity;
import com.example.playtech.R;
import com.example.playtech.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CartFragment extends Fragment {
    private List<Cart> cartList;
    private ListView listView;
    private CartAdapter adapter;
    SessionManager sessionManager;
    private String user_id;
    private TextView text_total;
    private TextView empty_msg;
    private double total = 0.0;
    private Menu menuList;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        //for the session values
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        user_id = user.get(sessionManager.USER_ID);

        text_total = root.findViewById(R.id.value_total);
        view = root.findViewById(R.id.divider);
        empty_msg = root.findViewById(R.id.empty_msg);

        cartList = new ArrayList<>();
        listView = root.findViewById(R.id.list);
        adapter = new CartAdapter(cartList, getContext(), CartFragment.this);

        getCart();
        return root;
    }

    public void getCart() {
        final ProgressDialog progressDialog;
        progressDialog = createProgressDialog(getContext());
        progressDialog.show();
        String url = "http://192.168.137.1:8081/PlayTech/MobileServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Cart: ", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                showMenu();
                                progressDialog.dismiss();
                                text_total.setVisibility(View.VISIBLE);
                                view.setVisibility(View.VISIBLE);
                                empty_msg.setVisibility(View.GONE);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    Cart cart = new Cart();
                                    cart.setId(object.getInt("id"));
                                    cart.setImage(object.getString("image"));
                                    cart.setName(object.getString("name"));
                                    cart.setPrice(object.getDouble("price"));
                                    cart.setQty(object.getInt("qty"));

                                    total += (cart.getPrice() * cart.getQty());

                                    cartList.add(cart);
                                }
                                text_total.setText("Total: ₱" + String.format("%.2f", total));
                                adapter = new CartAdapter(cartList, getContext(), CartFragment.this);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                hideMenu();
                                progressDialog.dismiss();
                                text_total.setVisibility(View.GONE);
                                view.setVisibility(View.GONE);
                                empty_msg.setVisibility(View.VISIBLE);
                                empty_msg.setText("No item has been added");
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
                params.put("action", "mobile_get_cart");
                params.put("uid", user_id);

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menuList = menu;
        inflater.inflate(R.menu.cart_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.checkout:

                // Do nothing but close the dialog
//                Toast.makeText(getContext(), "Hello World", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                startActivity(intent);
                break;
        }
        return super.

                onOptionsItemSelected(item);

    }

    private void hideMenu()
    {

        MenuItem item = menuList.findItem(R.id.checkout);
        item.setVisible(false);
    }


    private void showMenu()
    {

        MenuItem item = menuList.findItem(R.id.checkout);
        item.setVisible(true);
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

    public void reload(){
        FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new CartFragment());
        ft.commit();
    }
}
