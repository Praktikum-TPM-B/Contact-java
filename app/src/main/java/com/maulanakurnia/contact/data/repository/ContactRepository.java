package com.maulanakurnia.contact.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.maulanakurnia.contact.data.model.Contact;
import com.maulanakurnia.contact.utils.ContactDatabase;

import java.util.List;

/**
 * Created by Maulana Kurnia on 5/18/2021
 * Keep Coding & Stay Awesome!
 **/
public class ContactRepository {
    private ContactDAO contactDao;
    private LiveData<List<Contact>> listContact;

    public ContactRepository(Application application) {
        ContactDatabase db = ContactDatabase.getDatabase(application);

        // i use the DAO only inside the repository
        contactDao  = db.contactDao();
        listContact = contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllData() {
        return listContact;
    }

    public void insert(Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.insert(contact);
        });
    }

    public LiveData<Contact> get(int id) {
        return contactDao.get(id);
    }

    public void update(Contact contact) {
        ContactDatabase.databaseWriteExecutor.execute(()->
                contactDao.update(contact));
    }

    public void deleteById(long id) {
        ContactDatabase.databaseWriteExecutor.execute(()->
                contactDao.deleteById(id));
    }
}
