package com.aadevelopers.onlyscratchiapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.aadevelopers.onlyscratchiapp.App;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.models.User;
import com.aadevelopers.onlyscratchiapp.utils.BaseUrl;
import com.aadevelopers.onlyscratchiapp.utils.Constant;
import com.aadevelopers.onlyscratchiapp.utils.CustomVolleyJsonRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private TextInputEditText name_editText, email_editText, number_editText;
    private AppCompatButton update_profile_btn;
    private CircleImageView user_image;
    private Context activity;
    private ACProgressFlower alertDialog;

    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private String imageUrl = "";

    private ImageView back;
    private Bitmap bitmap;
    private String image;

    public ProfileFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context;
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name_editText = view.findViewById(R.id.name_pf_edit_text);
        email_editText = view.findViewById(R.id.email_pf_edit_text);
        number_editText = view.findViewById(R.id.number_pf_edit_text);
        user_image = view.findViewById(R.id.user_profile_update);
        back = view.findViewById(R.id.back_img_pf);
        update_profile_btn = view.findViewById(R.id.update_btn);
        onClick();
        return view;
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

    private void onClick() {
        name_editText.setText(Constant.getString(activity, Constant.USER_NAME));
        email_editText.setText(Constant.getString(activity, Constant.USER_EMAIL));
        number_editText.setText(Constant.getString(activity, Constant.USER_NUMBER));
        String user_email = Constant.getString(activity, Constant.USER_EMAIL);
        imageUrl = Constant.getString(activity, Constant.USER_IMAGE);
        Glide.with(activity)
                .load(BaseUrl.LOAD_USER_IMAGE + imageUrl)
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(user_image);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    SelectImage();
                }
            }
        });

        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(activity)) {
                    attemptUpdate();
                } else {
                    Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().onBackPressed();
            }
        });

    }

    public boolean isStoragePermissionGranted() {
        if (getActivity() == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void attemptUpdate() {
        if (Constant.isNetworkAvailable(activity)) {
            if (name_editText.getText().toString().length() == 0) {
                name_editText.setError(getResources().getString(R.string.enter_name));
                name_editText.requestFocus();
            } else if (number_editText.getText().toString().length() == 0) {
                number_editText.setError(getResources().getString(R.string.enter_number));
                number_editText.requestFocus();
            } else if (number_editText.getText().toString().length() != 10) {
                number_editText.setError("Enter Correct Number");
                number_editText.requestFocus();
            } else {
                alertDialog = new ACProgressFlower.Builder(activity)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .text("Please Wait...")
                        .fadeColor(Color.DKGRAY).build();
                Constant.hideKeyboard(getActivity());
                showProgressDialog();
                updateProfile(name_editText.getText().toString(), email_editText.getText().toString(), number_editText.getText().toString());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            Uri selectedImage = data.getData();
            Log.e("TAG", "onActivityResult: filePath" + filePath);
            Log.e("TAG", "onActivityResult: selectedImage" + selectedImage);
            InputStream imageStream = null;
            try {
                imageStream = activity.getContentResolver().openInputStream(
                        selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            bitmap = BitmapFactory.decodeStream(imageStream);
            Log.e("TAG", "onActivityResult: bmp" + bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                Glide.with(activity)
                        .load(bitmap)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(user_image);
                image = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfile(String name, String email, String number) {

        String userId = Constant.getString(activity, Constant.USER_ID);

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("update_profile", "any");
        params.put("email", email);
        params.put("name", name);
        params.put("password", "");
        if (image == null) {
            params.put("img", "");
        } else {
            params.put("img", image);
        }
        params.put("user_id", userId);
        params.put("number", number);
        Log.e("TAG", "updateProfile: " + params);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseUrl.UPDATE_PROFILE, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        JSONObject jsonObject = response.getJSONObject("0");
                        Constant.setString(activity, Constant.USER_ID, jsonObject.getString("id"));
                        final User user = new User(jsonObject.getString("name"), jsonObject.getString("number"), jsonObject.getString("email"), jsonObject.getString("points"), jsonObject.getString("referraled_with"), jsonObject.getString("image"), jsonObject.getString("status"), jsonObject.getString("referral_code"));
                        hideProgressDialog();

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
                            Constant.setString(activity, Constant.USER_POINTS, user.getPoints());
                            Log.e("TAG", "onDataChange: " + user.getPoints());
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
                            Log.e("TAG", "onDataChange: " + user.getImage());
                        }
                        hideProgressDialog();
                        Constant.setString(activity, Constant.IS_LOGIN, "true");
                        Constant.showToastMessage(activity, getResources().getString(R.string.update_successfully));
                    } else {
                        Constant.showToastMessage(activity, "Not Updated Try Again...");
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
                    Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}