package com.qr.myqr.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Date：2023/4/3
 * Describe:
 */
@Dao
interface DaoScan {
    @Query("SELECT * FROM scanentity order by time desc")
    fun getAll(): List<ScanEntity>

//    @Query("SELECT * FROM scanentity WHERE type IN (:type) LIMIT 1")
//    fun getScanEntityByType(type: Int): ScanEntity

//    @Update
//    fun updateScan(scanEntity: ScanEntity)

    @Insert
    fun addScan(scanEntity: ScanEntity)

    @Delete
    fun delScan(scanEntity: ScanEntity)

    @Query("DELETE FROM scanentity WHERE id like :scanEntityId")
    fun delById(scanEntityId: Int)
}