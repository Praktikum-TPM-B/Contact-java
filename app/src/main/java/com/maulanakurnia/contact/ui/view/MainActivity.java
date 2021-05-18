package com.maulanakurnia.contact.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maulanakurnia.contact.R;
import com.maulanakurnia.contact.data.model.Contact;
import com.maulanakurnia.contact.ui.adapter.ContactAdapter;
import com.maulanakurnia.contact.ui.viewmodel.ContactViewModel;

/**
 * Created by Maulana Kurnia on 5/18/2021
 * Keep Coding & Stay Awesome!
 **/
public class MainActivity extends AppCompatActivity {

    private static final int FORM_ACTIVITY_REQUEST_CODE = 1;
    public static final String TAG = "Clicked";
    public static final String CONTACT_ID = "contact_id";

    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.main_rv_contacts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // i need to use this viewModel provider,
        // otherwise i will get an error
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this
                        .getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContact().observe(this, contacts -> {
            // Set up adapter
            // when user pass the context to the contactAdapter,
            // it will know that implements the click event
            contactAdapter = new ContactAdapter(contacts, MainActivity.this);
            recyclerView.setAdapter(contactAdapter);
        });

        Button addContact = findViewById(R.id.main_btn_add_contact);
        addContact.setOnClickListener(v -> {
            // This takes us back to FormActivity
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivityForResult(intent, FORM_ACTIVITY_REQUEST_CODE);
        });
    }

    // Invoked whenever we return from activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FORM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Contact contact = new Contact(
                    data.getStringExtra(FormActivity.NAME_REPLY),
                    data.getStringExtra(FormActivity.MOBILE_REPLY),
                    data.getStringExtra(FormActivity.ADDRESS_REPLY),
                    data.getStringExtra(FormActivity.GENDER_REPLY)
            );
            ContactViewModel.insert(contact);
        }
    }
}
