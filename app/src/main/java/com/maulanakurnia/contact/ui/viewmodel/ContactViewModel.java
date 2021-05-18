package com.maulanakurnia.contact.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.maulanakurnia.contact.data.model.Contact;
import com.maulanakurnia.contact.data.repository.ContactRepository;
import com.maulanakurnia.contact.data.model.Contact;
import com.maulanakurnia.contact.data.repository.ContactRepository;

/**
 * Created by Maulana Kurnia on 5/18/2021
 * Keep Coding & Stay Awesome!
 **/

import java.util.List;

// The viewModel is responsible of holding UI data
// so, it survives to configuration changes
// (i.e, rotation of the device) and use Livedata to
// notify/publish changes in the backend of the application

public class ContactViewModel extends AndroidViewModel {

    public static ContactRepository repository;
    public final LiveData<List<Contact>> listContact;

    public ContactViewModel(@NonNull Application application) {
        super(application);

        repository  = new ContactRepository(application);
        listContact = repository.getAllData();
    }

    // You can use the methods as static,
    // since once the class is being instantiated once
    // it will hold the same repository for the class,
    public LiveData<List<Contact>> getAllContact() {
        return listContact;
    }

    public static void insert(Contact contact) {
        repository.insert(contact);
    }

    public LiveData<Contact> get(int id) {
        return repository.get(id);
    }

    public static void update(Contact contact) {
        repository.update(contact);
    }

    public static void deleteById(long id) {
        repository.deleteById(id);
    }

}
