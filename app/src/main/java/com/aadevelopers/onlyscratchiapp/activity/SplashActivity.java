package com.aadevelopers.onlyscratchiapp.activity;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aadevelopers.onlyscratchiapp.App;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.models.User;
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
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    boolean LOGIN = false;
    private AppUpdateManager appUpdateManager;
    public static final int RC_APP_UPDATE = 101;
    SplashActivity activity;
    String user_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;


        String is_login = Constant.getString(activity, Constant.IS_LOGIN);
        if (is_login.equals("true")) {
            LOGIN = true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(activity, "LOLLIPOP", Toast.LENGTH_SHORT).show();

            Log.e("TAG", "onCreate:if part activarte ");
            appUpdateManager = AppUpdateManagerFactory.create(this);
            UpdateApp();
        } else {
            Toast.makeText(activity, "no LOLLIPOP", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onCreate:else part activarte ");
            onInit();
        }
    }

    private void onInit() {
        if (Constant.isNetworkAvailable(activity)) {
            if (LOGIN) {
                try {
                    String tag_json_obj = "json_login_req";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("get_login", "any");
                    params.put("email", Constant.getString(activity, Constant.USER_EMAIL));
                    params.put("password", Constant.getString(activity, Constant.USER_PASSWORD));

                    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                            BaseUrl.LOGIN_API, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());

                            try {
                                boolean status = response.getBoolean("status");
                                if (status) {
                                    JSONObject jsonObject = response.getJSONObject("0");
                                    Constant.setString(activity, Constant.USER_ID, jsonObject.getString("id"));
                                    final User user = new User(jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("email"), jsonObject.getString("points"), jsonObject.getString("referraled_with"), jsonObject.getString("image"), jsonObject.getString("status"), jsonObject.getString("referral_code"));

                                    if (response.has("date")) {
                                        Constant.setString(activity, Constant.TODAY_DATE, response.getString("date"));
                                    } else {
                                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                        Constant.setString(activity, Constant.TODAY_DATE, currentDate);
                                    }
                                    if (user.getName() != null) {
                                        Constant.setString(activity, Constant.USER_NAME, user.getName());
                                        Log.e("TAG", "onDataChange: " + user.getName());
                                    }
                                    if (user.getNumber() != null) {
                                        Constant.setString(activity, Constant.USER_NUMBER, user.getNumber());
                                        Log.e("TAG", "onDataChange: " + user.getNumber());
                                    }
                                    if (user.getEmail() != null) {
                                        Constant.setString(activity, Constant.USER_EMAIL, user.getEmail());
                                        Log.e("TAG", "onDataChange: " + user.getEmail());
                                    }
                                    if (user.getPoints() != null) {
                                        if (!Constant.getString(activity, Constant.LAST_TIME_ADD_TO_SERVER).equals("")) {
                                            Log.e("TAG", "onDataChange: Last time not empty");
                                            if (!Constant.getString(activity, Constant.USER_POINTS).equals("")) {
                                                Log.e("TAG", "onDataChange: user points not empty");
                                                if (Constant.getString(activity, Constant.IS_UPDATE).equalsIgnoreCase("")) {
                                                    Constant.setString(activity, Constant.USER_POINTS, Constant.getString(activity, Constant.USER_POINTS));
                                                    Log.e("TAG", "onDataChange: " + user.getPoints());
                                                    Constant.setString(activity, Constant.IS_UPDATE, "true");
                                                } else {
                                                    Constant.setString(activity, Constant.IS_UPDATE, "true");
                                                    Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
                                                    Log.e("TAG", "onDataChange: " + user.getPoints());
                                                }
                                            }
                                        }
                                    }
                                    if (user.getReferCode() != null) {
                                        Constant.setString(activity, Constant.REFER_CODE, user.getReferCode());
                                        Log.e("TAG", "onDataChange: " + user.getReferCode());
                                    }
                                    if (user.getIsBLocked() != null) {
                                        Constant.setString(activity, Constant.USER_BLOCKED, user.getIsBLocked());
                                        Log.e("TAG", "onDataChange: " + user.getIsBLocked());
                                    }
                                    if (user.getUserReferCode() != null) {
                                        Constant.setString(activity, Constant.USER_REFFER_CODE, user.getUserReferCode());
                                        Log.e("TAG", "onDataChange: " + user.getUserReferCode());
                                    }
                                    if (user.getImage() != null) {
                                        Constant.setString(activity, Constant.USER_IMAGE, user.getImage());
                                        Log.e("TAG", "onDataChange: " + user.getUserReferCode());
                                    }


                                    if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                                        Constant.showBlockedDialog(activity, getResources().getString(R.string.you_are_blocked));
                                    } else {
//                                        Log.e("TAG", "onInit: login pART");
//                                        Constant.GotoNextActivity(activity, MainActivity.class, "");
//                                        overridePendingTransition(R.anim.enter, R.anim.exit);
//                                        finish();
                                        getSettingsFromAdminPannel();
                                    }
                                } else {
//                                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//                                    Constant.setString(activity, Constant.TODAY_DATE, currentDate);
//                                    Log.e("TAG", "onInit: user_information from database");
//                                    Constant.setString(activity, Constant.IS_LOGIN, "");
//                                    Constant.GotoNextActivity(activity, LoginActivity.class, "");
//                                    overridePendingTransition(R.anim.enter, R.anim.exit);
//                                    finish();
                                    getSettingsFromAdminPannel();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            VolleyLog.d("TAG", "Error: " + error.getMessage());
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
                    Log.e("TAG", "onInit: excption " + e.getMessage());
                }
            } else {
                if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                    Constant.showBlockedDialog(activity, getResources().getString(R.string.you_are_blocked));
                    return;
                }
//                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//                Constant.setString(activity, Constant.TODAY_DATE, currentDate);
//                Log.e("TAG", "onInit: else part of no login");
//                Constant.GotoNextActivity(activity, LoginActivity.class, "");
//                overridePendingTransition(R.anim.enter, R.anim.exit);
//                finish();
                getSettingsFromAdminPannel();
            }
        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
    }

    public void UpdateApp() {
        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                            Log.e("TAG", "onCreate:startUpdateFlowForResult part activarte ");
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", "onCreate:startUpdateFlowForResult else part activarte ");
                        Toast.makeText(activity, "activity.onInit()", Toast.LENGTH_SHORT).show();

                        activity.onInit();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onCreate:addOnFailureListener else part activarte ");
                    activity.onInit();
                    Toast.makeText(activity, "onFailure activity.onInit()", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                onInit();
            } else {
                onInit();
            }
        }
    }

    private void getSettingsFromAdminPannel() {

        if (Constant.isNetworkAvailable(activity)) {
            try {

                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.ADMIN_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("TAG", "onResponse: " + response);
                            boolean status = response.getBoolean("status");
                            if (status) {
                                JSONObject jb = response.getJSONObject("0");
                                Constant.setString(activity, Constant.ADS_BEETWEEN, jb.getString("ads_between"));
                                Constant.setString(activity, Constant.DAILY_SCRATCH_COUNT, jb.getString("daily_scratch_limit"));
                                Constant.setString(activity, Constant.DAILY_SPIN_COUNT, jb.getString("daily_spin_limit"));
                                Constant.setString(activity, Constant.DAILY_CHECK_IN_POINTS, jb.getString("daily_check_in_points"));
                                Constant.setString(activity, Constant.COIN_TO_RUPEE, jb.getString("coin_to_rupee_text"));
                                Constant.setString(activity, Constant.DAILY_CAPTCHA_COUNT, jb.getString("daily_captcha_limit"));
                                Constant.setString(activity, Constant.MINIMUM_REDEEM_POINTS, jb.getString("minimum_redeem_points"));
                                Constant.setString(activity, Constant.AD_TYPE, jb.getString("ad_type"));

                                Constant.setString(activity, Constant.ADMOB_BANNER_ID, jb.getString("admob_banner_id"));
                                Constant.setString(activity, Constant.ADMOB_INTERSTITAL_ID, jb.getString("admob_interstital_id"));
                                Constant.setString(activity, Constant.ADMOB_REWARDED_ID, jb.getString("admob_rewarded_id"));

                                Constant.setString(activity, Constant.UNITY_BANNER_ID, jb.getString("unity_banner_id"));
                                Constant.setString(activity, Constant.UNITY_INTERSTITAL_ID, jb.getString("unity_interstital_id"));
                                Constant.setString(activity, Constant.UNITY_REWARDED_ID, jb.getString("unity_rewarded_id"));

                                Constant.setString(activity, Constant.STARTAPP_BANNER_ID, jb.getString("startapp_banner_id"));
                                Constant.setString(activity, Constant.STARTAPP_INTERSTITAL_ID, jb.getString("startapp_interstital_id"));
                                Constant.setString(activity, Constant.REFER_TEXT, jb.getString("refer_text"));


                                Constant.setString(activity, Constant.SPIN_PRICE_COIN, jb.getString("spin_price_coins"));
                                Constant.setString(activity, Constant.SCRATCH_PRICE_COIN, jb.getString("scratch_price_coins"));
                                Constant.setString(activity, Constant.CAPTCHA_PRICE_COIN, jb.getString("captcha_price_coins"));
                                Constant.setString(activity, Constant.SIGNUP_BOUNUS, jb.getString("signup_points"));
                                Constant.setString(activity, Constant.UNITY_GAME_ID, jb.getString("unity_game_id"));
                                Constant.setString(activity, Constant.STARTAPP_APP_ID, jb.getString("startapp_app_id"));
                                Constant.setString(activity, Constant.ADMOB_APP_ID, jb.getString("admob_app_id"));
                                Constant.setString(activity, Constant.SCRATCH_PRICE_COIN_PLATINUM, jb.getString("scratch_coin_platinum"));
                                Constant.setString(activity, Constant.SCRATCH_PRICE_COIN_GOLD, jb.getString("scratch_coin_gold"));
                                Constant.setString(activity, Constant.APPLOVIN_BANNER_ID, jb.getString("applovin_banner_id"));
                                Constant.setString(activity, Constant.APPLOVIN_INTERSTITAL_ID, jb.getString("applovin_interstital_id"));
                                Constant.setString(activity, Constant.APPLOVIN_REWARDED_ID, jb.getString("applovin_rewarded_id"));


                                gotoLoginActivity();
                            } else {
                                Constant.showToastMessage(activity, "No Settings Found In Admin Pannel");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Constant.showToastMessage(activity, "Something Went Wrong Try Again");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                        }
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000 * 20,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                App.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Admin Settings: excption " + e.getMessage().toString());
            }
        } else {
            Constant.showToastMessage(activity, "No internet Connection");
        }
    }

    private void gotoLoginActivity() {
        App.initAds();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LOGIN) {
                    Constant.GotoNextActivity(activity, MainActivity.class, "");
                } else {
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    Constant.setString(activity, Constant.TODAY_DATE, currentDate);
                    Log.e("TAG", "onInit: else part of no login");
                    Constant.GotoNextActivity(activity, LoginActivity.class, "");
                }
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        }, 1000);
    }


}