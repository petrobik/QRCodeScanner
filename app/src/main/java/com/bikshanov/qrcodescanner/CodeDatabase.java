package com.bikshanov.qrcodescanner;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Code.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class CodeDatabase extends RoomDatabase {

    private static CodeDatabase instance;

    public abstract CodeDao codeDao();

    public static synchronized CodeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CodeDatabase.class, "code_database")
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
            }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private CodeDao codeDao;

        private PopulateDbAsyncTask(CodeDatabase db) {
            codeDao = db.codeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            codeDao.insert(new Code("Code 1", "QR", "text"));
            codeDao.insert(new Code("Code 2", "Barcode", "EAN_13"));
            codeDao.insert(new Code("Code 3", "URL", ""));
            codeDao.insert(new Code("Code 4", "Text", ""));
            return null;
        }
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE code_table " +
                    " ADD COLUMN type TEXT DEFAULT 'Text'");
        }
    };
}
