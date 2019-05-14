package com.bikshanov.qrcodescanner;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CodeRepository {

    private CodeDao codeDao;
    private LiveData<List<Code>> allCodes;

    public CodeRepository(Application application) {
        CodeDatabase database = CodeDatabase.getInstance(application);
        codeDao = database.codeDao();
        allCodes = codeDao.getAllCodes();
    }

    public void insert(Code code) {
        new InsertCodeAsyncTask(codeDao).execute(code);
    }

    public void update(Code code) {
        new UpdateCodeAsyncTask(codeDao).execute(code);
    }

    public void delete(Code code) {
        new DeleteCodeAsyncTask(codeDao).execute(code);
    }

    public void deleteAllCodes() {
        new DeleteAllCodesAsyncTask(codeDao).execute();
    }

    public LiveData<List<Code>> getAllCodes() {
        return allCodes;
    }

    private static class InsertCodeAsyncTask extends AsyncTask<Code, Void, Void> {

        private CodeDao codeDao;

        private InsertCodeAsyncTask(CodeDao codeDao) {
            this.codeDao = codeDao;
        }

        @Override
        protected Void doInBackground(Code... codes) {
            codeDao.insert(codes[0]);
            return null;
        }
    }

    private static class UpdateCodeAsyncTask extends AsyncTask<Code, Void, Void> {

        private CodeDao codeDao;

        private UpdateCodeAsyncTask(CodeDao codeDao) {
            this.codeDao = codeDao;
        }

        @Override
        protected Void doInBackground(Code... codes) {
            codeDao.update(codes[0]);
            return null;
        }
    }

    private static class DeleteCodeAsyncTask extends AsyncTask<Code, Void, Void> {

        private CodeDao codeDao;

        private DeleteCodeAsyncTask(CodeDao codeDao) {
            this.codeDao = codeDao;
        }

        @Override
        protected Void doInBackground(Code... codes) {
            codeDao.delete(codes[0]);
            return null;
        }
    }

    private static class DeleteAllCodesAsyncTask extends AsyncTask<Code, Void, Void> {

        private CodeDao codeDao;

        private DeleteAllCodesAsyncTask(CodeDao codeDao) {
            this.codeDao = codeDao;
        }

        @Override
        protected Void doInBackground(Code... codes) {
            codeDao.deleteAllCodes();
            return null;
        }
    }
}
