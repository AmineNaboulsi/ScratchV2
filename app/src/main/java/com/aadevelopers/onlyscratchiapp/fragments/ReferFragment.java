package com.aadevelopers.onlyscratchiapp.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.utils.Constant;
import static android.content.Context.CLIPBOARD_SERVICE;


public class ReferFragment extends Fragment {

    TextView txtrefercoin, txtcode, txtcopy, txtinvite;
    Toolbar toolbar;
    private TextView points_textView;
    private Context mContext;

    public ReferFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static ReferFragment newInstance() {
        ReferFragment fragment = new ReferFragment();
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
        View view = inflater.inflate(R.layout.fragment_refer, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        txtcode = view.findViewById(R.id.txtcode);
        txtcopy = view.findViewById(R.id.txtcopy);
        txtinvite = view.findViewById(R.id.txtinvite);
        txtrefercoin = view.findViewById(R.id.txtrefercoin);
        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Invite Friends");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            TextView titleText = toolbar.findViewById(R.id.toolbarText);
            titleText.setText("Invite Friends");
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
        txtrefercoin.setText(Constant.getString(mContext, Constant.REFER_TEXT));

        txtinvite.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(mContext, R.drawable.ic_share), null, null, null);
        txtcode.setText(Constant.getString(mContext, Constant.USER_REFFER_CODE));
        txtcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", txtcode.getText());
                clipboard.setPrimaryClip(clip);
                Constant.showToastMessage(mContext, "Refer Code Copied");
            }
        });
        txtinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcode.equals("")) {
                    Constant.showToastMessage(mContext, "Can't Find Refer Code Login First...");
                } else {
                    Constant.referApp(mContext, Constant.getString(mContext, Constant.USER_REFFER_CODE));
                }
            }
        });
        return view;
    }

    private void setPointsText() {
        if (points_textView != null) {
            String userPoints = Constant.getString(mContext, Constant.USER_POINTS);
            if (userPoints.equalsIgnoreCase("")) {
                userPoints = "0";
            }
            points_textView.setText(userPoints);
        }
    }

}