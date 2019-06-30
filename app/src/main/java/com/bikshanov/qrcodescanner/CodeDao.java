package com.bikshanov.qrcodescanner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CodeDao {

    @Insert
    void insert(Code code);

    @Update
    void update(Code code);

    @Delete
    void delete(Code code);

    @Query("DELETE FROM code_table")
    void deleteAllCodes();

    @Query("SELECT * FROM code_table ORDER BY date DESC")
    LiveData<List<Code>> getAllCodes();

    @Query("SELECT * FROM code_table WHERE id = :id")
    LiveData<Code> getCodeById(int id);
}
