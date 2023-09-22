package com.aadevelopers.onlyscratchiapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.fragments.ContactFragment;
import com.aadevelopers.onlyscratchiapp.fragments.ForgotFragment;
import com.aadevelopers.onlyscratchiapp.fragments.GoldFragment;
import com.aadevelopers.onlyscratchiapp.fragments.LeaderBoardFragment;
import com.aadevelopers.onlyscratchiapp.fragments.PaymentHistoryFragment;
import com.aadevelopers.onlyscratchiapp.fragments.PlatinumFragment;
import com.aadevelopers.onlyscratchiapp.fragments.ProfileFragment;
import com.aadevelopers.onlyscratchiapp.fragments.ReferFragment;
import com.aadevelopers.onlyscratchiapp.fragments.SilverFragment;
import com.aadevelopers.onlyscratchiapp.fragments.WalletFragment;
import com.aadevelopers.onlyscratchiapp.utils.Constant;

public class ReferActivity extends AppCompatActivity {

    private String type;
    private ReferActivity activity;
    private Fragment fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        activity = this;
        type = getIntent().getStringExtra("type");

        if (type != null) {
            switch (type) {
                case "changePassword":
                    fm = ForgotFragment.newInstance();
                    break;

                case "wallet":
                    fm = WalletFragment.newInstance();
                    break;
                case "contact":
                    fm = ContactFragment.newInstance();
                    break;
                case "Profile":
                    fm = ProfileFragment.newInstance();
                    break;
                case "refer":
                    fm = ReferFragment.newInstance();
                    break;
                case "Silver Scratch":
                    fm = SilverFragment.newInstance();
                    break;
                case "Platinum Scratch":
                    fm = PlatinumFragment.newInstance();
                    break;
                case "Gold Scratch":
                    fm = GoldFragment.newInstance();
                    break;
                case "LeaderBoard":
                    fm = LeaderBoardFragment.newInstance();
                    break;
                case "PaymentHistory":
                    fm = PaymentHistoryFragment.newInstance();
                    break;

            }
            if (fm != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout_refer, fm).commit();
            }
        } else {
            Constant.showToastMessage(activity, "Something Went Wrong...");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}