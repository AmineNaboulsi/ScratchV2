package com.aadevelopers.onlyscratchiapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.services.PointsService;
import com.aadevelopers.onlyscratchiapp.sharedPref.PrefManager;

import java.util.regex.Pattern;

public class Constant {
    public static final String IS_LOGIN = "IsLogin";
    public static final String USER_BLOCKED = "user_blocked";
    public static final String USER_NAME = "user_name";
    public static final String USER_NUMBER = "user_number";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_POINTS = "user_points";
    public static final String USER_REFFER_CODE = "user_reffer_code";
    public static final String LAST_DATE = "Last_Date";
    public static final String LAST_TIME_ADD_TO_SERVER = "last_time_added";
    public static final String REFER_CODE = "refer_code";
    public static final String LAST_DATE_SCRATCH_SILVER = "last_date_scratch_silver";
    public static final String LAST_DATE_SCRATCH_PLATINUM = "last_date_scratch_platinum";
    public static final String LAST_DATE_SCRATCH_GOLD = "last_date_scratch_gold";
    public static final String USER_IMAGE = "user_image";
    public static final String SCRATCH_COUNT_SILVER = "silver_scratch";
    public static final String SCRATCH_COUNT_PLATINUM = "platinum_scratch";
    public static final String SCRATCH_COUNT_GOLD = "gold_scratch";
    public static final String USER_ID = "user_id";
    public static final String IS_UPDATE = "user_update";
    public static final String USER_PASSWORD = "password";
    private static PrefManager prefManager;
    public static InterstitialAd interstitial_Ad;
    public static RewardedAd rewarded_ad;
    public static boolean isShowInterstitial = true;
    public static boolean isShowRewarded = true;
    public static boolean isShowfacebookRewarded = true;
    public static boolean isAttachedInterstitial = true;
    public static boolean isAttachedfacebookInterstitial = true;
    public static boolean isShowFacebookInterstitial = true;
    public static RewardedAdLoadCallback adLoadCallback;
    public static final String TAG = "Constant";
    public static InterstitialAdListener interstitialAdListener;
    public static RewardedVideoAd rewardedVideoAd;
    public static ProgressDialog alertDialog;
    public  static String TODAY_DATE = "today_date";

    // these are get from admin pannel
    public static final String AD_TYPE = "ads_type";
    public static final String DAILY_SCRATCH_COUNT = "daily_scratch_count";
    public static final String DAILY_SPIN_COUNT = "daily_spin_count";
    public static final String ADS_BEETWEEN = "ads_beetween";
    public static final String DAILY_CHECK_IN_POINTS = "daily_check_in_points";
    public static final String MINIMUM_REDEEM_POINTS = "minimum_redeem_amount";
    public static final String COIN_TO_RUPEE = "COIN_TO_RUPEE";
    public static final String ADMOB_BANNER_ID = "admob_banner_id";
    public static final String ADMOB_INTERSTITAL_ID = "admob_interstital_id";
    public static final String ADMOB_REWARDED_ID = "admob_rewarded_id";
    public static final String UNITY_REWARDED_ID = "unity_rewarded_id";
    public static final String UNITY_INTERSTITAL_ID = "unity_interstital_id";
    public static final String UNITY_BANNER_ID = "unity_banner_id";

    public static final String STARTAPP_BANNER_ID = "startapp_banner_id";
    public static final String STARTAPP_INTERSTITAL_ID = "startapp_interstital_id";

    public static final String REFER_TEXT = "refer_text_from_admin";
    public static final String SPIN_PRICE_COIN = "spin_price_coins";
    public static final String SCRATCH_PRICE_COIN = "scratch_price_coins";
    public static final String CAPTCHA_PRICE_COIN = "captcha_price_coins";
    public static final String SIGNUP_BOUNUS = "singup_bounus_points";
    public static final String DAILY_CAPTCHA_COUNT = "daily_captcha_count";
    public static final String STARTAPP_APP_ID = "start_app_app_id";
    public static final String UNITY_GAME_ID = "unity_game_id";
    public static final String ADMOB_APP_ID = "admob_app_id";
    public static final String SCRATCH_PRICE_COIN_PLATINUM = "spin_price_coins_platinum";
    public static final String SCRATCH_PRICE_COIN_GOLD = "spin_price_coins_gold";
    public static final String LAST_DATE_CAPTCHA = "last_date_captcha";
    public static final String CAPTCHA_COUNT = "captcha_count";

    // add these new constant to your file

    public static final String APPLOVIN_INTERSTITAL_ID = "applovin_interstital_ad_unit";
    public static final String APPLOVIN_REWARDED_ID = "applovin_rewarded_ad_unit";
    public static final String APPLOVIN_BANNER_ID = "applovin_banner_ad_unit";

    public static void hideProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public static void GotoNextActivity(Context context, Class nextActivity, String msg) {
        if (context != null && nextActivity != null) {
            if (msg == null) {
                msg = "";
            }
            Intent intent = new Intent(context, nextActivity);
            intent.putExtra("Intent", msg);
            context.startActivity(intent);
        }
    }

    public static boolean isValidEmailAddress(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        boolean isMatches = pattern.matcher(email).matches();
        Log.e("Boolean Value", "" + isMatches);
        return isMatches;
    }

    public static void showToastMessage(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void setString(Context context, String preKey, String setString) {
        if (prefManager == null) {
            prefManager = new PrefManager(context);
        }
        prefManager.setString(preKey, setString);
    }

    public static String getString(Context context, String prefKey) {
        if (prefManager == null) {
            prefManager = new PrefManager(context);
        }
        return prefManager.getString(prefKey);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void addPoints(Context context, int points, int type) {
        if (context != null) {
            String po = Constant.getString(context, Constant.USER_POINTS);
            if (po.equals("")) {
                po = "0";
            }
            if (type == 1) {
                Constant.setString(context, Constant.USER_POINTS, po);
                Intent serviceIntent = new Intent(context, PointsService.class);
                serviceIntent.putExtra("points", Constant.getString(context, Constant.USER_POINTS));
                context.startService(serviceIntent);
            } else {
                int current_Points = Integer.parseInt(po);
                String total_points = String.valueOf(current_Points + points);
                Constant.setString(context, Constant.USER_POINTS, total_points);
                Intent serviceIntent = new Intent(context, PointsService.class);
                serviceIntent.putExtra("points", Constant.getString(context, Constant.USER_POINTS));
                context.startService(serviceIntent);
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showInternetErrorDialog(Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);

        imageView.setImageResource(R.drawable.ic_internet);
        title_text.setText(msg);
        points_text.setVisibility(View.GONE);
        add_btn.setText(context.getResources().getString(R.string.okk));

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showBlockedDialog(final Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);

        imageView.setImageResource(R.drawable.ic_close);
        title_text.setText(msg);
        points_text.setVisibility(View.GONE);
        add_btn.setText(context.getResources().getString(R.string.okk));

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void referApp(Context context, String refer_code) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_text) + "' " + refer_code + " '" + " Download Link = " + " https://play.google.com/store/apps/details?id=" + context.getPackageName());
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        } catch (Exception e) {
            Log.e("TAG", "referApp: " + e.getMessage());
        }
    }
}
