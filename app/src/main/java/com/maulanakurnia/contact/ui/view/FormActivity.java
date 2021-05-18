package com.maulanakurnia.contact.ui.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.maulanakurnia.contact.R;
import com.maulanakurnia.contact.data.model.Contact;
import com.maulanakurnia.contact.ui.viewmodel.ContactViewModel;
import com.maulanakurnia.contact.utils.WindowSoftInputMode;

/**
 * Created by Maulana Kurnia on 5/18/2021
 * Keep Coding & Stay Awesome!
 **/
public class FormActivity extends AppCompatActivity {

    public static final String NAME_REPLY       = "name_reply";
    public static final String MOBILE_REPLY     = "mobile_reply";
    public static final String ADDRESS_REPLY    = "address_reply";
    public static final String GENDER_REPLY     = "gender_reply";

    private int contactID   = 0;
    private boolean isEdit  = false;

    private EditText inputName, inputMobile, inputAddress;
    private RadioGroup radioGroup;
    private RadioButton selectGender;
    private WindowSoftInputMode windowSoftInputMode;
    private TextView description;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        inputName           = findViewById(R.id.input_name);
        inputMobile         = findViewById(R.id.input_mobile);
        inputAddress        = findViewById(R.id.input_address);
        Button submit       = findViewById(R.id.submit_button);
        Button cancel       = findViewById(R.id.cancel_button);
        radioGroup          = findViewById(R.id.radioGender);
        TextView title      = findViewById(R.id.form_title);
        description         = findViewById(R.id.form_desc);
        windowSoftInputMode = new WindowSoftInputMode(this);

        // we need to use this viewModel provider,
        // otherwise we will get an error
        ContactViewModel contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                FormActivity.this.getApplication())
                .create(ContactViewModel.class);

        // Wenever we click one of the ViewHolder,
        // it will start FormActivity get the id
        // (since an observer is triggered) and set the data
        if(getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contactID = getIntent().getIntExtra(MainActivity.CONTACT_ID,0);

            contactViewModel.get(contactID).observe(this, contact -> {
                if(contact != null) {
                    inputName.setText(contact.getName());
                    inputMobile.setText(contact.getMobile());
                    inputAddress.setText(contact.getAddress());

                    boolean male = contact.getGender().trim().equals("Male");
                    if(male) {
                        radioGroup.check(R.id.radioMale);
                    }else {
                        radioGroup.check(R.id.radioFemale);
                    }
                    description.setText(contact.getName());
                }
            });
            isEdit = true;
            submit.setText("CHANGE");
            title.setText("Change Contact");

        }

        submit.setOnClickListener(v -> {
            Intent intent    = new Intent();
            int selectedId   = radioGroup.getCheckedRadioButtonId();
            selectGender     = findViewById(selectedId);

            int id           = contactID;
            String name      = inputName.getText().toString().trim();
            String mobile    = inputMobile.getText().toString().trim();
            String address   = inputAddress.getText().toString().trim();


            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(selectGender.getText().toString().trim())) {

                Toast.makeText(this, "Enter Information!", Toast.LENGTH_SHORT).show();

            } else {
                String gender    = selectGender.getText().toString().trim();
                if(!isEdit) {

                    intent.putExtra(NAME_REPLY, name);
                    intent.putExtra(MOBILE_REPLY, mobile);
                    intent.putExtra(ADDRESS_REPLY, address);
                    intent.putExtra(GENDER_REPLY, gender);
                    setResult(RESULT_OK, intent);

                    finish();

                }else {

                    Contact contact = new Contact(name, mobile, address, gender);
                    contact.setId(id);
                    ContactViewModel.update(contact);
                    isEdit = false;
                    finish();
                }
            }

        });

        cancel.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onResume() {
        windowSoftInputMode.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        windowSoftInputMode.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        windowSoftInputMode.onDestroy();
        super.onDestroy();
    }
}
