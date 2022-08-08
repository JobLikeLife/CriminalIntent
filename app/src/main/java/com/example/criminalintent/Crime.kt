package com.example.criminalintent

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*


//数据类：添加字段代表crime的ID、crime标题、发生日期和处理状态，使用一个构造函数给ID和日期赋初值
//使用@Entity注解一个类，然后交给Room处理，一张数据库表就诞生了。
@Entity
data class Crime (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),//变量值为当前日期，作为crime的默认发生时间
    var isSolved: Boolean = false){

    @Ignore
    constructor() : this(UUID.randomUUID())

}
