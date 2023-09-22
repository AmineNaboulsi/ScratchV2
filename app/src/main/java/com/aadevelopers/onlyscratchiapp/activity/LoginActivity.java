package com.aadevelopers.onlyscratchiapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aadevelopers.onlyscratchiapp.R;
import com.aadevelopers.onlyscratchiapp.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_Login, LoginFragment.newInstance()).commit();

    }
}