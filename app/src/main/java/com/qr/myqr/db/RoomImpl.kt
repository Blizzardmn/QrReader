package com.qr.myqr.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qr.myqr.appIns
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Date：2023/4/3
 * Describe:
 */

@Database(entities = [CreateEntity::class,ScanEntity::class], version = 1)
abstract class RoomImpl: RoomDatabase() {
    companion object {
        private val instance by lazy {
            Room.databaseBuilder(appIns, RoomImpl::class.java, "qr_reader.db").apply {
                allowMainThreadQueries()
                enableMultiInstanceInvalidation()
                fallbackToDestructiveMigration()
                setQueryExecutor { GlobalScope.launch { it.run() } }
            }.build()
        }
        val scanDao get() = instance.scanDao()
        val createDao get() = instance.createDao()
    }
    abstract fun scanDao(): DaoScan
    abstract fun createDao(): DaoCreate
}