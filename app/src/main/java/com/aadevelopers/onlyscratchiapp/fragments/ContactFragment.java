package com.aadevelopers.onlyscratchiapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.aadevelopers.onlyscratchiapp.R;

public class ContactFragment extends Fragment {

    private TextInputEditText subject, message;
    private AppCompatButton btn;
    // TODO: Rename and change types of parameters
    private ImageView back;
    public ContactFragment() {

    }

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
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
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        subject = view.findViewById(R.id.subject);
        message = view.findViewById(R.id.message);
        btn = view.findViewById(R.id.send_btn);
        back = view.findViewById(R.id.back_img_contact);
        intView();
        return view;
    }

    private void intView() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subject.getText().toString().length() == 0) {
                    subject.setError("Enter Subject");
                    subject.requestFocus();
                } else if (message.getText().toString().length() == 0) {
                    message.setError("Please Type Message");
                    message.requestFocus();
                } else {
                    if (getActivity() == null) {
                        return;
                    }
                    String[] Email = {getResources().getString(R.string.gmail_id)};
                    String Subject = subject.getText().toString();
                    String Message = message.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL, Email);
                    intent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                    intent.putExtra(Intent.EXTRA_TEXT, Message);
                    getActivity().startActivity(Intent.createChooser(intent, "Send Via"));
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
}