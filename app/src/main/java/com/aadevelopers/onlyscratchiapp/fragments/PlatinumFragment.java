package com.aadevelopers.onlyscratchiapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.services.PointsService;
import com.aadevelopers.onlyscratchiapp.utils.Constant;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;

public class PlatinumFragment extends Fragment implements ScratchListener, MaxAdListener, MaxRewardedAdListener, MaxAdViewAdListener {


    private LinearLayout adLayout;
    private Activity activity;
    private Toolbar toolbar;
    private TextView scratch_count_textView, points_textView, points_text, _platinum;
    boolean first_time = true, scratch_first = true;
    private int scratch_count = 1;
    private final String TAG = "Silver Fragment";
    private String random_points;
    private int counter_dialog = 0;
    public int poiints = 0;
    public boolean rewardShow = true, interstitialShow = true, isFullRewardShow = false, isRewadedVideo = false;
    ScratchCardLayout scratchCardLayout;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    public StartAppAd startAppAd;
   // private UnityAdsListener unityAdsListener;
    private MaxInterstitialAd maxInterstitialAd;

    private MaxRewardedAd maxRewardedAd;
    private MaxAdView adView;
    public PlatinumFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public static PlatinumFragment newInstance() {
        PlatinumFragment fragment = new PlatinumFragment();
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
        View view = inflater.inflate(R.layout.fragment_platinum, container, false);
        if (getActivity() != null) {
            activity = getActivity();
        }

        adLayout = view.findViewById(R.id.banner_container_platinum);


        _platinum = view.findViewById(R.id._platinum);
        toolbar = view.findViewById(R.id.toolbar);
        points_text = view.findViewById(R.id.textView_points_show_platinum);
        scratch_count_textView = view.findViewById(R.id.limit_text_platinum);
        scratchCardLayout = view.findViewById(R.id.scratch_view_layout_platinum);
        scratchCardLayout.setScratchListener(this);
        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            TextView titleText = toolbar.findViewById(R.id.toolbarText);
            titleText.setText("Platinum Scratch");
            _platinum.setText("Platinum Scratch");
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


        //unityAdsListener = new UnityAdsListener();
        //UnityAds.addListener(unityAdsListener);
        if (Constant.isNetworkAvailable(activity)) {
            String adType = Constant.getString(activity, Constant.AD_TYPE);
            if (adType.equalsIgnoreCase("admob")) {
                loadAdmobBanner();
                loadAdmobRewarededVideo();
                LoadAdmobInterstitial();
            } else if (adType.equalsIgnoreCase("startapp")) {
                LoadStartAppInterstital();
                LoadStartAppBanner();
            } else if (adType.equalsIgnoreCase("unity")) {
                ///loadBanner();
                loadRewardedVideo();
                LoadInterstitial();
            }else if (adType.equalsIgnoreCase("applovin")) {
                loadApplovinBanner();
                loadApplovinRewarded();
                LoadApplovinInterstitial();
            }


        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
        onInit();
        return view;
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

    private void onInit() {

        String scratchCount = Constant.getString(activity, Constant.SCRATCH_COUNT_PLATINUM);
        if (scratchCount.equals("0")) {
            scratchCount = "";
            Log.e("TAG", "onInit: scratch card 0");
        }
        if (scratchCount.equals("")) {
            Log.e("TAG", "onInit: scratch card empty vala part");
            String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDate);
            String last_date = Constant.getString(activity, Constant.LAST_DATE_SCRATCH_PLATINUM);
            Log.e("TAG", "Lat date" + last_date);
            if (last_date.equals("")) {
                Log.e("TAG", "onInit: last date empty part");
                setScratchCount(Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                Constant.setString(activity, Constant.SCRATCH_COUNT_PLATINUM, Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                Constant.setString(activity, Constant.LAST_DATE_SCRATCH_PLATINUM, currentDate);
                //Constant.addDate(activity, "platinum", Constant.getString(activity, Constant.LAST_DATE_SCRATCH_PLATINUM));
            } else {
                Log.e("TAG", "onInit: last date not empty part");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date current_date = sdf.parse(currentDate);
                    Date lastDate = sdf.parse(last_date);
                    long diff = current_date.getTime() - lastDate.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Difference" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, Constant.LAST_DATE_SCRATCH_PLATINUM, currentDate);
                        Constant.setString(activity, Constant.SCRATCH_COUNT_PLATINUM, Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                        setScratchCount(Constant.getString(activity, Constant.SCRATCH_COUNT_PLATINUM));
                        //  Constant.addDate(activity, "platinum", Constant.getString(activity, Constant.LAST_DATE_SCRATCH_PLATINUM));
                        Log.e("TAG", "onClick: today date added to preference" + currentDate);
                    } else {
                        setScratchCount("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "onInit: scracth card in preference part");
            setScratchCount(scratchCount);
        }
    }

    private void setScratchCount(String string) {
        if (string != null || !string.equalsIgnoreCase(""))
            scratch_count_textView.setText("Your Today Scratch Count left = " + string);
    }


    private void showDialogPoints(final int addOrNoAddValue, final String points, final int counter, boolean isShowDialog) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);
        if (Constant.isNetworkAvailable(activity)) {
            if (addOrNoAddValue == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    imageView.setImageResource(R.drawable.sad);
                    title_text.setText(getResources().getString(R.string.better_luck));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.okk));
                } else if (points.equals("no")) {
                    Log.e("TAG", "showDialogPoints: rewared cancel");
                    imageView.setImageResource(R.drawable.sad);
                    title_text.setText(getResources().getString(R.string.not_watch_full_video));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.okk));
                } else {
                    Log.e("TAG", "showDialogPoints: points");
                    imageView.setImageResource(R.drawable.congo);
                    title_text.setText(getResources().getString(R.string.you_won));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.add_to_wallet));
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                imageView.setImageResource(R.drawable.donee);
                title_text.setText(getResources().getString(R.string.today_chance_over));
                points_text.setVisibility(View.GONE);
                add_btn.setText(getResources().getString(R.string.okk));
            }
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    first_time = true;
                    scratch_first = true;
                    scratchCardLayout.resetScratch();
                    poiints = 0;
                    if (addOrNoAddValue == 1) {
                        if (points.equals("0") || points.equalsIgnoreCase("no")) {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SCRATCH_COUNT_PLATINUM, current_counter);
                            setScratchCount(Constant.getString(activity, Constant.SCRATCH_COUNT_PLATINUM));
                            dialog.dismiss();
                        } else {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SCRATCH_COUNT_PLATINUM, current_counter);
                            setScratchCount(Constant.getString(activity, Constant.SCRATCH_COUNT_PLATINUM));
                            try {
                                int finalPoint;
                                if (points.equals("")) {
                                    finalPoint = 0;
                                } else {
                                    finalPoint = Integer.parseInt(points);
                                }
                                poiints = finalPoint;
                                Constant.addPoints(activity, finalPoint, 0);
                                setPointsText();
                            } catch (NumberFormatException ex) {
                                Log.e("TAG", "onScratchComplete: " + ex.getMessage());
                            }
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                    }
                    if (addOrNoAddValue == 1) {
                        if (scratch_count == Integer.parseInt(Constant.getString(activity, Constant.ADS_BEETWEEN))) {
                            if (rewardShow) {
                                Log.e(TAG, "onReachTarget: rewaded ads showing method");
                                if (Constant.getString(activity, Constant.AD_TYPE).equalsIgnoreCase("startapp")) {
                                    unityStartAppInterstitialShow();
                                } else {
                                    showDailog();
                                }
                                rewardShow = false;
                                interstitialShow = true;
                                scratch_count = 0;
                            } else if (interstitialShow) {
                                Log.e(TAG, "onReachTarget: interstital ads showing method");
                                unityStartAppInterstitialShow();
                                rewardShow = true;
                                interstitialShow = false;
                                scratch_count = 0;
                            }
                        } else {
                            scratch_count += 1;
                        }
                    }
                }
            });
        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
        if (isShowDialog) {
            dialog.show();
        } else {
            rewardShow = false;
            interstitialShow = true;
            scratch_count = 0;
            showDailog();
        }
    }

    public void showDailog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);
        AppCompatButton cancel_btn = dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setVisibility(View.GONE);
        imageView.setImageResource(R.drawable.watched);
        add_btn.setText("Watch Video");
        title_text.setText("Watch Full Video");
        points_text.setText("To Unlock this Reward Points");

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                unityStartAppRewardedShow();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (poiints != 0) {
                    String po = Constant.getString(activity, Constant.USER_POINTS);
                    if (po.equals("")) {
                        po = "0";
                    }
                    int current_Points = Integer.parseInt(po);
                    int finalPoints = current_Points - poiints;
                    Constant.setString(activity, Constant.USER_POINTS, String.valueOf(finalPoints));
                    Intent serviceIntent = new Intent(activity, PointsService.class);
                    serviceIntent.putExtra("points", Constant.getString(activity, Constant.USER_POINTS));
                    activity.startService(serviceIntent);
                    setPointsText();
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onScratchComplete() {
        if (first_time) {
            first_time = false;
            Log.e("onScratch", "Complete");
            Log.e("onScratch", "Complete" + random_points);
            String count = scratch_count_textView.getText().toString();
            String[] counteee = count.split("=", 2);
            String ran = counteee[1];
            Log.e(TAG, "onScratchComplete: " + ran);
            int counter = Integer.parseInt(ran.trim());
            counter_dialog = Integer.parseInt(ran.trim());
            if (counter <= 0) {
                showDialogPoints(0, "0", counter, true);
            } else {
                if (scratch_count == Integer.parseInt(Constant.getString(activity, Constant.ADS_BEETWEEN))) {
                    if (rewardShow) {
                        showDialogPoints(1, points_text.getText().toString(), counter, false);
                    } else {
                        showDialogPoints(1, points_text.getText().toString(), counter, true);
                    }
                } else {
                    showDialogPoints(1, points_text.getText().toString(), counter, true);
                }
            }
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (scratch_first) {
            scratch_first = false;
            Log.e(TAG, "onScratchStarted: " + random_points);
            if (Constant.isNetworkAvailable(activity)) {
                random_points = "";
                Random random = new Random();
                String str = Constant.getString(activity, Constant.SCRATCH_PRICE_COIN_PLATINUM);
                String[] parts = str.split("-", 2);
                int low = Integer.parseInt(parts[0]);
                int high = Integer.parseInt(parts[1]);
                int result = random.nextInt(high - low) + low;
                random_points = String.valueOf(result);
                if (random_points == null || random_points.equalsIgnoreCase("null")) {
                    random_points = String.valueOf(1);
                }
                points_text.setText(random_points);
                Log.e(TAG, "onScratchStarted: " + random_points);
            } else {
                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
            }
        }
    }

    @Override
    public void onScratchStarted() {
        if (Integer.parseInt(scratch_count_textView.getText().toString().trim()) <= 0) {
            showDialogPoints(0, "0", 0, true);
        }
    }


    ///////// admob ads ///////////
    private void loadAdmobBanner() {
        final AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Constant.getString(activity, Constant.ADMOB_BANNER_ID));
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adLayout.addView(adView);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.v("Admob Error", "client connection error");
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void loadAdmobRewarededVideo() {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(activity, Constant.getString(activity, Constant.ADMOB_REWARDED_ID),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                        isFullRewardShow = true;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        isFullRewardShow = false;
                        rewaredListner();
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }

    private void rewaredListner() {
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.");
                if (!isFullRewardShow) {
                    isFullRewardShow = true;
                    showDialogPoints(1, "no", counter_dialog, true);
                }
                mRewardedAd = null;
                loadAdmobRewarededVideo();
            }
        });
    }

    private void ShowAdmobRewaredVideo() {
        if (mRewardedAd != null) {
            mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    isFullRewardShow = true;
                    showDialogPoints(1, points_text.getText().toString().trim(), counter_dialog, true);
                }
            });
        } else {
            showDialogPoints(1, "no", counter_dialog, true);
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

    public void attemptListener() {
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.");
                LoadAdmobInterstitial();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                mInterstitialAd = null;
                Log.d("TAG", "The ad was shown.");
            }
        });
    }

    private void LoadAdmobInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, Constant.getString(activity, Constant.ADMOB_INTERSTITAL_ID), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        attemptListener();
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }

    private void ShowAdmobInterstital() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else {
            showDialogPoints(1, "no", counter_dialog, true);
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    /////////// admob end ////////////////


    ///////////// unity ads start ///////////////

    /*
        private void loadBanner() {
            BannerView bannerView = new BannerView(activity, Constant.getString(activity, Constant.UNITY_BANNER_ID), new UnityBannerSize(320, 50));
            bannerView.load();
            bannerView.setListener(new BannerView.IListener() {
                @Override
                public void onBannerLoaded(BannerView bannerView) {
                    adLayout.addView(bannerView);
                }

                @Override
                public void onBannerClick(BannerView bannerView) {

                }

                @Override
                public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {

                }

                @Override
                public void onBannerLeftApplication(BannerView bannerView) {

                }
            });
        }

    */
    public void loadRewardedVideo() {
        /*
        if (UnityAds.isInitialized()) {
            UnityAds.load(Constant.getString(activity, Constant.UNITY_REWARDED_ID));
        }

         */
    }

    private void ShowRewarded() {
        /*
        if (UnityAds.isReady(Constant.getString(activity, Constant.UNITY_REWARDED_ID))) {
            UnityAds.show(activity, Constant.getString(activity, Constant.UNITY_REWARDED_ID));
        } else {
            showDialogPoints(1, "no", counter_dialog, true);
            loadRewardedVideo();
        }

         */
    }


    private void LoadInterstitial() {
        /*
        if (UnityAds.isInitialized()) {
            UnityAds.load(Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID));
        }

         */
    }

    private void ShowInterstital() {
        /*
        if (UnityAds.isReady(Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID))) {
            UnityAds.show(activity, Constant.getString(activity, Constant.UNITY_INTERSTITAL_ID), unityListener);
        } else {
            showDialogPoints(1, "no", counter_dialog, true);
            LoadInterstitial();
        }

         */
    }
/*
    private final IUnityAdsShowListener unityListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {

        }

        @Override
        public void onUnityAdsShowStart(String s) {

        }

        @Override
        public void onUnityAdsShowClick(String s) {

        }

        @Override
        public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
            showDialogPoints(1, points_text.getText().toString().trim(), counter_dialog, true);
            LoadInterstitial();
        }
    };
*/
/*
    private class UnityAdsListener implements IUnityAdsListener {

        public void onUnityAdsReady(String adUnitId) {
            // Implement functionality for an ad being ready to show.
        }

        @Override
        public void onUnityAdsStart(String adUnitId) {
            // Implement functionality for a user starting to watch an ad.
        }

        @Override
        public void onUnityAdsFinish(String adUnitId, UnityAds.FinishState finishState) {
            if (activity == null) {
                return;
            }

            if (rewardShow) {
                return;
            }
            // Implement conditional logic for each ad completion status:
            if (finishState.equals(UnityAds.FinishState.COMPLETED)) {
                // Reward the user for watching the ad to completion.
                showDialogPoints(1, points_text.getText().toString().trim(), counter_dialog, true);
            } else if (finishState == UnityAds.FinishState.SKIPPED) {
                // Do not reward the user for skipping the ad.
                showDialogPoints(1, "no", counter_dialog, true);
            } else if (finishState == UnityAds.FinishState.ERROR) {
                // Log an error.
                showDialogPoints(1, "no", counter_dialog, true);
            }
            loadRewardedVideo();
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
            // Implement functionality for a Unity Ads service error occurring.
        }
    }

*/
    /////////////// start app ads /////////////
    private void LoadStartAppBanner() {
        Banner banner = new Banner(activity, new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                adLayout.addView(view);
            }

            @Override
            public void onFailedToReceiveAd(View view) {
                Log.e(TAG, "onFailedToReceiveAd: " + view.toString());
            }

            @Override
            public void onImpression(View view) {

            }

            @Override
            public void onClick(View view) {

            }
        });
        banner.loadAd();
    }

    private void LoadStartAppInterstital() {
        if (startAppAd == null) {
            startAppAd = new StartAppAd(activity);
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        } else {
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        }
    }

    private void ShowStartappInterstital() {
        if (startAppAd != null && startAppAd.isReady()) {
            startAppAd.showAd(new AdDisplayListener() {
                @Override
                public void adHidden(Ad ad) {
                    showDialogPoints(1, points_text.getText().toString().trim(), counter_dialog, true);
                }

                @Override
                public void adDisplayed(Ad ad) {

                }

                @Override
                public void adClicked(Ad ad) {

                }

                @Override
                public void adNotDisplayed(Ad ad) {
                    showDialogPoints(1, "no", counter_dialog, true);
                }
            });
        } else {
            LoadStartAppInterstital();
        }
    }


    ////////////// show ads ///////////////
    private void unityStartAppInterstitialShow() {
        isRewadedVideo = false;
        String adType = Constant.getString(activity, Constant.AD_TYPE);
        if (adType.equalsIgnoreCase("admob")) {
            ShowAdmobInterstital();
        } else if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital();
        } else if (adType.equalsIgnoreCase("unity")) {
            ShowInterstital();
        }else if (adType.equalsIgnoreCase("applovin")) {
            ShowApplovinInterstital();
        }


    }

    private void unityStartAppRewardedShow() {
        isRewadedVideo = true;
        isFullRewardShow = false;
        String adType = Constant.getString(activity, Constant.AD_TYPE);
        if (adType.equalsIgnoreCase("admob")) {
            ShowAdmobRewaredVideo();
        } else if (adType.equalsIgnoreCase("startapp")) {
            ShowStartappInterstital();
        } else if (adType.equalsIgnoreCase("unity")) {
            ShowRewarded();
        }else if (adType.equalsIgnoreCase("applovin")) {
            ShowApplovinRewarded();
        }
    }


    ////////////// applovin ads ////////////////
    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {
        if (!ad.getAdUnitId().equalsIgnoreCase(Constant.getString(activity, Constant.APPLOVIN_BANNER_ID))) {
            if (isRewadedVideo) {
                if (isFullRewardShow) {
                    showDialogPoints(1, points_text.getText().toString().trim(), counter_dialog, true);
                } else {
                    showDialogPoints(1, "no", counter_dialog, true);
                }
                loadApplovinRewarded();
            } else {
                LoadApplovinInterstitial();
            }
        }
    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        Log.e(TAG, "onAdLoadFailed: " + error.getMessage());
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }

    private void ShowApplovinInterstital() {
        if (maxInterstitialAd.isReady()) {
            maxInterstitialAd.showAd();
        } else {
            showDialogPoints(1, "no", counter_dialog, true);
            LoadApplovinInterstitial();
        }
    }

    private void LoadApplovinInterstitial() {
        if (maxInterstitialAd == null) {
            maxInterstitialAd = new MaxInterstitialAd(Constant.getString(activity, Constant.APPLOVIN_INTERSTITAL_ID), requireActivity());
            maxInterstitialAd.setListener(this);
        }
        // Load the first ad
        maxInterstitialAd.loadAd();
    }

    private void ShowApplovinRewarded() {
        if (maxRewardedAd.isReady()) {
            maxRewardedAd.showAd();
        } else {
            showDialogPoints(1, "no", counter_dialog, true);
            loadApplovinRewarded();
        }
    }


    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {
        isFullRewardShow = true;
    }


    private void loadApplovinRewarded() {
        if (maxRewardedAd == null) {
            maxRewardedAd = MaxRewardedAd.getInstance(Constant.getString(activity, Constant.APPLOVIN_REWARDED_ID), requireActivity());
            maxRewardedAd.setListener(this);
        }
        maxRewardedAd.loadAd();
    }

    @Override
    public void onAdExpanded(MaxAd ad) {

    }

    @Override
    public void onAdCollapsed(MaxAd ad) {

    }

    private void loadApplovinBanner() {
        adView = new MaxAdView(Constant.getString(activity, Constant.APPLOVIN_BANNER_ID), activity);
        adView.setListener(this);

        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);

        adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx, Gravity.BOTTOM));
        // Need to set the background or background color for banners to be fully functional.
        //adView.setBackgroundColor(Color.BLACK);
        final ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
        rootView.addView(adView);

        adView.loadAd();
        adView.setVisibility(View.VISIBLE);
        adView.startAutoRefresh();
    }

    @Override
    public void onDestroy() {
        try {
            if (adView != null) {
                adView.destroy();
            }
            //UnityAds.removeListener(unityAdsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

}