package com.aadevelopers.onlyscratchiapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.aadevelopers.onlyscratchiapp.App;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.utils.BaseUrl;
import com.aadevelopers.onlyscratchiapp.utils.Constant;
import com.aadevelopers.onlyscratchiapp.utils.CustomVolleyJsonRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static com.aadevelopers.onlyscratchiapp.utils.Constant.hideKeyboard;


public class ForgotFragment extends Fragment {

    private TextInputEditText email_EditText, change_password_edit_text;
    private AppCompatButton reset_btn, change_password_btn;
    private Context mContext;
    private ACProgressFlower alertDialog;
    private TextView header_text, resend_text;
    //private OtpTextView otp_edit_text;
    private String type;
    private ImageView back;
    private RelativeLayout baseLyt;
    private LinearLayout password_change_lyt;
    private String OTP;
    private TextInputLayout emailLyt;

    public ForgotFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static ForgotFragment newInstance() {
        ForgotFragment fragment = new ForgotFragment();
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
        View view = inflater.inflate(R.layout.fragment_forgot, container, false);

        email_EditText = view.findViewById(R.id.reset_email_edit_text);
        reset_btn = view.findViewById(R.id.reset_password_btn);
        header_text = view.findViewById(R.id.login_txt_forgot);
        //otp_edit_text = view.findViewById(R.id.otpEditText);
        resend_text = view.findViewById(R.id.resend_otp);
        baseLyt = view.findViewById(R.id.otp_base_lyt);
        emailLyt = view.findViewById(R.id.email_lyt);

        password_change_lyt = view.findViewById(R.id.reset_password_lyt);
        change_password_edit_text = view.findViewById(R.id.new_password);
        baseLyt = view.findViewById(R.id.otp_base_lyt);
        change_password_btn = view.findViewById(R.id.change_password_btn);

        alertDialog = new ACProgressFlower.Builder(mContext)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Please Wait...")
                .fadeColor(Color.DKGRAY).build();
        back = view.findViewById(R.id.back_img_forgot);
        if (getActivity().getIntent() != null) {
            type = getActivity().getIntent().getStringExtra("type");
        }
        if (type == null) {
            header_text.setText(getResources().getString(R.string.forgot_password));
        } else {
            header_text.setText(getResources().getString(R.string.change_password));
        }
        onClick();

        return view;
    }

    private void onClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().onBackPressed();
            }
        });
        resend_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                showProgressDialog();
                Random rnd = new Random();
                int number = rnd.nextInt(999999);
                resetPasswordEmail(email_EditText.getText().toString(), String.format("%06d", number));
            }
        });

        /*
        otp_edit_text.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {


            }

            @Override
            public void onOTPComplete(String otp) {
                if (otp.equalsIgnoreCase(OTP)) {
                    baseLyt.setVisibility(View.GONE);
                    password_change_lyt.setVisibility(View.VISIBLE);
                } else {
                    Constant.showToastMessage(mContext, "Enter Correct Otp...");
                }
            }
        });

         */

        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change_password_edit_text.getText().toString().length() == 0) {
                    change_password_edit_text.setError(getResources().getString(R.string.enter_password));
                    change_password_edit_text.requestFocus();
                } else if (change_password_edit_text.getText().toString().length() < 6) {
                    change_password_edit_text.setError(getResources().getString(R.string.enter_valid_number));
                    change_password_edit_text.requestFocus();
                } else {
                    changePasswordMethod(change_password_edit_text.getText().toString());
                }
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(mContext)) {
                    String Email = email_EditText.getText().toString();
                    if (Email.length() == 0) {
                        email_EditText.setError(getResources().getString(R.string.enter_email));
                        email_EditText.requestFocus();
                    } else if (!Constant.isValidEmailAddress(Email)) {
                        email_EditText.setError(getResources().getString(R.string.enter_valid_email));
                        email_EditText.requestFocus();
                    } else {
                        reset_btn.setEnabled(false);
                        hideKeyboard(getActivity());
                        showProgressDialog();
                        Random rnd = new Random();
                        int number = rnd.nextInt(999999);
                        resetPasswordEmail(Email, String.format("%06d", number));
                    }
                } else {
                    Constant.showInternetErrorDialog(mContext, getResources().getString(R.string.no_internet_connection));
                }
            }
        });

    }

    private void changePasswordMethod(String password) {
        showProgressDialog();

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("rest_pass", "any");
        params.put("email", email_EditText.getText().toString());
        params.put("pass", password);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.RESET_PASSWORD, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        hideProgressDialog();
                        Constant.showToastMessage(mContext, "Password Reset");
                        if (getActivity() == null) {
                            return;
                        }
                        getActivity().onBackPressed();
                    } else {
                        Constant.showToastMessage(mContext, "Password Not Updated Please Check your email");
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(mContext, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void resetPasswordEmail(String email, final String otp) {
        showProgressDialog();

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("recover", "anything");
        params.put("email", email);
        params.put("otp", otp);
        Log.e("TAG", "resetPasswordEmail: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.FORGOT_PASSWORD, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        hideProgressDialog();
                        Constant.showToastMessage(mContext, "Otp Sent to your email check your email");
                        emailLyt.setVisibility(View.GONE);
                        resend_text.setVisibility(View.VISIBLE);
                        reset_btn.setEnabled(false);
                        OTP = otp;
                    } else {
                        Constant.showToastMessage(mContext, "This Email is Not Registered");
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Constant.showToastMessage(mContext, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void showProgressDialog() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}