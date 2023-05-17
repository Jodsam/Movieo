package com.tonyk.android.movieo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tonyk.android.movieo.model.Movie

@Database(entities = [Movie::class], version = 2, exportSchema = false)
abstract class MovieDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao

}

val migration_1_2 = object :Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Movie ADD COLUMN INTEGER NOT NULL DEFAULT 0"
        )
    }
}