package com.qr.myqr.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Date：2023/4/3
 * Describe:
 */
@Entity
class ScanEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var content: String = ""
    var type: Int = 0
    var time: Long = System.currentTimeMillis()
}