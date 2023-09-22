package com.aadevelopers.onlyscratchiapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aadevelopers.onlyscratchiapp.App;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.activity.LoginActivity;
import com.aadevelopers.onlyscratchiapp.adapter.PaymentHistoryAdapter;
import com.aadevelopers.onlyscratchiapp.models.PaymentHistoryModel;
import com.aadevelopers.onlyscratchiapp.utils.BaseUrl;
import com.aadevelopers.onlyscratchiapp.utils.Constant;
import com.aadevelopers.onlyscratchiapp.utils.CustomVolleyJsonRequest;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PaymentHistoryFragment extends Fragment {


    private TextView no_history_found_text;
    private RecyclerView paymentHistoryRecyclerView;
    private PaymentHistoryAdapter paymentHistoryAdapter;
    private ArrayList<PaymentHistoryModel> paymentHistoryModelArrayList = new ArrayList<>();
    private Context context;
    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private TextView points_textView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public PaymentHistoryFragment() {
        // Required empty public constructor
    }

    public static PaymentHistoryFragment newInstance() {
        PaymentHistoryFragment fragment = new PaymentHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);

        paymentHistoryRecyclerView = view.findViewById(R.id.paymentHistoryRecyclerView);
        paymentHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        paymentHistoryAdapter = new PaymentHistoryAdapter(paymentHistoryModelArrayList);
        paymentHistoryRecyclerView.setAdapter(paymentHistoryAdapter);
        no_history_found_text = view.findViewById(R.id.no_payment_history_text);
        toolbar = view.findViewById(R.id.toolbar);
        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            TextView titleText = toolbar.findViewById(R.id.toolbarText);
            titleText.setText("Payment History");
            points_textView = toolbar.findViewById(R.id.points_text_in_toolbar);
            setPointsText();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshLayout = view.findViewById(R.id.refreshLyt);
        refreshLayout.setRefreshing(true);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        loadData();

        return view;
    }

    private void loadData() {
        String is_login = Constant.getString(context, Constant.IS_LOGIN);
        boolean LOGIN = false;
        if (is_login.equals("true")) {
            LOGIN = true;
        }
        if (Constant.isNetworkAvailable(context)) {
            if (LOGIN) {
                try {
                    String tag_json_obj = "json_login_req";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", Constant.getString(context, Constant.USER_ID));
                    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                            BaseUrl.PAYMENT_HISTORY, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());

                            try {
                                boolean status = response.getBoolean("status");
                                if (status) {
                                    JSONArray object = response.getJSONArray("data");
                                    paymentHistoryModelArrayList.clear();
                                    for (int i = 0; i < object.length(); i++) {
                                        JSONObject jsonObject = object.getJSONObject(i);
                                        PaymentHistoryModel paymentInfo = new PaymentHistoryModel();
                                        paymentInfo.setId(jsonObject.getString("id"));
                                        paymentInfo.setRedeem_point(jsonObject.getString("redeem_point"));
                                        paymentInfo.setPayment_mode(jsonObject.getString("payment_mode"));
                                        paymentInfo.setPayment_info(jsonObject.getString("payment_info"));
                                        paymentInfo.setPayment_time(jsonObject.getString("payment_time"));
                                        paymentHistoryModelArrayList.add(paymentInfo);
                                    }
                                    if (!paymentHistoryModelArrayList.isEmpty()) {
                                        no_history_found_text.setVisibility(View.GONE);
                                        paymentHistoryRecyclerView.setVisibility(View.VISIBLE);
                                        paymentHistoryAdapter.notifyDataSetChanged();
                                    } else {
                                        no_history_found_text.setVisibility(View.VISIBLE);
                                        paymentHistoryRecyclerView.setVisibility(View.GONE);
                                        Constant.showToastMessage(context, "Nothing Found...");
                                    }
                                    if (refreshLayout.isRefreshing()) {
                                        refreshLayout.setRefreshing(false);
                                    }
                                } else {
                                    if (refreshLayout.isRefreshing()) {
                                        refreshLayout.setRefreshing(false);
                                    }
                                    Constant.showToastMessage(context, "Nothing Found...");
                                }
                            } catch (JSONException e) {
                                if (refreshLayout.isRefreshing()) {
                                    refreshLayout.setRefreshing(false);
                                }
                                Constant.showToastMessage(context, "Something Went Wrong...");
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            VolleyLog.d("TAG", "Error: " + error.getMessage());
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.setRefreshing(false);
                            }
                            Constant.showToastMessage(context, "Something Went Wrong...");
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Constant.showToastMessage(context, getResources().getString(R.string.slow_internet_connection));
                            }
                        }
                    });
                    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                            1000 * 60,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (Constant.getString(context, Constant.USER_BLOCKED).equals("true")) {
                    Constant.showBlockedDialog(context, getResources().getString(R.string.you_are_blocked));
                    return;
                }
                Log.e("TAG", "onInit: else part of no login");
                Constant.GotoNextActivity(context, LoginActivity.class, "");
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

        } else {
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            Constant.showInternetErrorDialog(context, getResources().getString(R.string.no_internet_connection));
        }
    }

    private void setPointsText() {
        if (points_textView != null) {
            String userPoints = Constant.getString(context, Constant.USER_POINTS);
            if (userPoints.equalsIgnoreCase("")) {
                userPoints = "0";
            }
            points_textView.setText(userPoints);
        }
    }

}