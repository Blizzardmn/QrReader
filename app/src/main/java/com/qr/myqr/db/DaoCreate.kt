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
interface DaoCreate {
    @Query("SELECT * FROM CreateEntity")
    fun getAll(): List<CreateEntity>

    @Query("SELECT * FROM CreateEntity WHERE type IN (:type) LIMIT 1")
    fun getScanEntityByType(type: Int): CreateEntity?

    @Update
    fun update(createEntity: CreateEntity)

    @Insert
    fun add(createEntity: CreateEntity)

    @Delete
    fun delete(createEntity: CreateEntity)

    @Query("DELETE FROM CreateEntity WHERE id like :id")
    fun delById(id: Int)
}