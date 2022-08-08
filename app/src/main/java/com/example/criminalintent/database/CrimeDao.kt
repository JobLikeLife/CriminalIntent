package com.example.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.criminalintent.Crime
import java.util.*

//定义一个名为CrimeDao的空接口，并使用Room的@Dao注解它
//@Dao注解告诉Room，CrimeDao是一个数据访问对象。把CrimeDao和数据库类关联起来后，Room会自动给CrimeDao接口里的函数生成实现代码
@Dao
interface CrimeDao {

    //@Query注解表明，getCrimes()和getCrime(UUID)是从数据库读取数据，不是插入、更新或删除数据。DAO接口里查询函数的返回类型也就是数据库查询要返回数据的类型
    @Query("SELECT * FROM crime")//告诉Room取出crime数据库表里所有记录及其所有字段
    //fun getCrimes(): List<Crime>
    fun getCrimes(): LiveData<List<Crime>>//使用LiveData的跨线程沟通的功能，借助它执行数据库查询操作。首先，更新数据查询函数，改用LiveData对象作为其返回数据类型

    //从DAO类返回LiveData实例，就是告诉Room要在后台线程上执行数据库查询。查询到crime数据后，LiveData对象会把结果发到主线程并通知UI观察者

    @Query("SELECT * FROM crime WHERE id=(:id)")//取出匹配给定ID的某条记录的所有字段
    //fun getCrime(id: UUID): Crime?
    fun getCrime(id: UUID): LiveData<Crime?>

}
