package com.maulanakurnia.contact.data.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maulanakurnia.contact.data.model.Contact;

import java.util.List;

/**
 * Created by Maulana Kurnia on 5/18/2021
 * Keep Coding & Stay Awesome!
 **/
@Dao
public interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contacts);

    @Query("SELECT * FROM contact ORDER BY name ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contact WHERE contact.id == :id")
    LiveData<Contact> get(int id);

    @Update
    void update(Contact contacts);

    @Query("DELETE FROM contact WHERE id = :id")
    void deleteById(long id);
}
