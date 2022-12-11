package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface AstroDao {

    // get all saved records
    @Query("select * from Asteroid order by date(closeApproachDate) ASC")
    fun getAll(): LiveData<List<Asteroid>>

    // get closeapproach recognized by date
    @Query("select * from Asteroid where closeApproachDate <=:date order by date(closeApproachDate) ASC ")
    fun getTodayAsteroid(date: String): LiveData<List<Asteroid>>

    //insert all recieved data to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<Asteroid>): List<Long>

    @Query("delete from Asteroid")
    fun deleteAll()
    //Refresh saved data by removing all records and save the new recieved ones
    @Transaction
    fun updateData(newAstros: List<Asteroid>): List<Long> {
        deleteAll()
        return insertAll(newAstros)
    }



}
@Dao
interface PictureOfTheDayDao {

    @Query("select * from pictureofday")
    fun get(): LiveData<PictureOfDay>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pic: PictureOfDay): Long

    @Query("delete from pictureofday")
    fun deleteAll()

    @Transaction
    fun updateData(pic: PictureOfDay): Long {
        deleteAll()
        return insert(pic)
    }


}
@Database(entities = [Asteroid::class,PictureOfDay::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val astroDao: AstroDao
    abstract val pictDao: PictureOfTheDayDao
}
private lateinit var INSTANCE: AsteroidDatabase
fun getInstance(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "astrodb"
                ).build()
        }
        return INSTANCE!!
    }

}
