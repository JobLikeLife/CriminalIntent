package com.example.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.criminalintent.Crime

//@Database注解告诉Room，CrimeDatabase类就是应用里的数据库。
//第一个参数是实体类集合，告诉Room在创建和管理数据库表时该用哪个实体类。这里只传入了Crime类，因为整个应用就这么一个实体。
//第二个参数是数据库版本。
//第三个参数是注释处理器,禁用导出功能。安卓room构建错误(Schema警告)：模式导出目录未提供给注释处理器，因此我们无法导出模式。 您可以提供 `room.schemaLocation` 注释处理器参数或将 exportSchema 设置为 false。
@Database(entities = [ Crime::class ], version=1, exportSchema = false)

//需要把类型转换类添加到数据库类里.通过添加@TypeConverters注解，并传入CrimeTypeConverters类，你告诉数据库，需要转换数据类型时，请使用CrimeTypeConverters类里的函数。
//至此，数据库和数据库表的定义完成了
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {

    //为关联DAO，打开CrimeDatabase.kt，添加一个返回类型是CrimeDao的抽象函数
    //现在，数据库创建后，Room会生成DAO的具体实现代码。然后，你可以引用到它，调用里面定义的各个函数与数据库交互。
    abstract fun crimeDao(): CrimeDao

}
