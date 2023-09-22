package com.aadevelopers.onlyscratchiapp.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.aadevelopers.onlyscratchiapp.App;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.activity.LoginActivity;
import com.aadevelopers.onlyscratchiapp.adapter.LeaderBoardAdapter;
import com.aadevelopers.onlyscratchiapp.models.LeaderBoard;
import com.aadevelopers.onlyscratchiapp.utils.BaseUrl;
import com.aadevelopers.onlyscratchiapp.utils.Constant;
import com.aadevelopers.onlyscratchiapp.utils.CustomVolleyJsonRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderBoardFragment extends Fragment {


    private RecyclerView leaderBoardRecyclerView;
    private TextView points_textView;
    private Toolbar toolbar;
    private Context activity;
    boolean LOGIN = false;
    private LeaderBoardAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    List<LeaderBoard> leaderBoards = new ArrayList<>();

    public LeaderBoardFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context;
    }

    public static LeaderBoardFragment newInstance() {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
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
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        leaderBoardRecyclerView = view.findViewById(R.id.leaderBoardRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        leaderBoardRecyclerView.setLayoutManager(manager);
        refreshLayout = view.findViewById(R.id.refreshLyt);
        refreshLayout.setRefreshing(true);
        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            TextView titleText = toolbar.findViewById(R.id.toolbarText);
            titleText.setText("LeaderBoards");
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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        adapter = null;
                    }
                    onInit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        onInit();
        return view;
    }

    private void onInit() {
        String is_login = Constant.getString(activity, Constant.IS_LOGIN);
        if (is_login.equals("true")) {
            LOGIN = true;
        }
        if (Constant.isNetworkAvailable(activity)) {
            if (LOGIN) {
                try {
                    String tag_json_obj = "json_login_req";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("top_users", "anything");
                    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                            BaseUrl.TOP_10_USER, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());

                            try {
                                boolean status = response.getBoolean("status");
                                if (status) {
                                    JSONArray object = response.getJSONArray("0");
                                    leaderBoards.clear();
                                    int rank = 1;
                                    for (int i = 0; i < object.length(); i++) {
                                        JSONObject jsonObject = object.getJSONObject(i);
                                        if (!jsonObject.getString("points").equalsIgnoreCase("")){
                                            LeaderBoard leaderBoard = new LeaderBoard();
                                            leaderBoard.setId(String.valueOf(rank));
                                            leaderBoard.setName(jsonObject.getString("name"));
                                            leaderBoard.setImage(jsonObject.getString("image"));
                                            leaderBoard.setPoints(jsonObject.getString("points"));
                                            leaderBoards.add(leaderBoard);
                                            rank++;
                                        }
                                    }
                                    if (!leaderBoards.isEmpty()) {
                                        adapter = new LeaderBoardAdapter(leaderBoards, activity);
                                        leaderBoardRecyclerView.setAdapter(adapter);
                                    } else {
                                        Constant.showToastMessage(activity, "Nothing Found...");
                                    }
                                    if (refreshLayout.isRefreshing()) {
                                        refreshLayout.setRefreshing(false);
                                    }
                                } else {
                                    if (refreshLayout.isRefreshing()) {
                                        refreshLayout.setRefreshing(false);
                                    }
                                    Constant.showToastMessage(activity, "Nothing Found...");
                                }
                            } catch (JSONException e) {
                                if (refreshLayout.isRefreshing()) {
                                    refreshLayout.setRefreshing(false);
                                }
                                Constant.showToastMessage(activity, "Something Went Wrong...");
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
                            Constant.showToastMessage(activity, "Something Went Wrong...");
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                            }
                        }
                    });
                    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                            1000 * 20,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (Constant.getString(activity, Constant.USER_BLOCKED).equals("true")) {
                    Constant.showBlockedDialog(activity, getResources().getString(R.string.you_are_blocked));
                    return;
                }
                Log.e("TAG", "onInit: else part of no login");
                Constant.GotoNextActivity(activity, LoginActivity.class, "");
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

        } else {
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
    }

    private void setPointsText() {
        if (points_textView != null) {
            String userPoints = Constant.getString(activity, Constant.USER_POINTS);
            if (userPoints.equalsIgnoreCase("")) {
                userPoints = "0";
            }
            points_textView.setText(userPoints);
        }
    }

}