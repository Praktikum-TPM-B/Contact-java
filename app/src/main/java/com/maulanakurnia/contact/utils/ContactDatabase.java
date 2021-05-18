package com.maulanakurnia.contact.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.maulanakurnia.contact.data.model.Contact;
import com.maulanakurnia.contact.data.repository.ContactDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Maulana Kurnia on 5/18/2021
 * Keep Coding & Stay Awesome!
 **/
@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {

    // Method to retrive DAO
    public abstract ContactDAO contactDao();
    public static final int NUMBER_OF_THREADS = 4;

    // Volatile means that any object created
    // with volatile wold disappear from the compiler
    private static volatile ContactDatabase INSTANCE;

    // as explained in the room model,
    // data must not be retrieved/write from the main thread,
    // so, i should manage threads to do using a executor
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Singleton pattern - thread safe
    public static ContactDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (ContactDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ContactDatabase.class, "db_contact")
                        .build();
            }
        }

        return INSTANCE;
    }
}
