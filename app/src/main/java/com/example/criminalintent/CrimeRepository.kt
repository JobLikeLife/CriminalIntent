package com.example.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.database.CrimeDatabase
import java.util.*

private const val DATABASE_NAME = "crime-database"

//CrimeRepository是个单例（singleton），也就是说，在应用进程里，只会有一个实例。见书本356页，11.9深入学习：单例
class CrimeRepository private constructor(context: Context){

    //在CrimeRepository里添加两个属性，用来保存数据库和DAO对象，由于没有外部访问需要，因此这里定义使用了私有字符串常量。
    //Room.databaseBuilder()使用三个参数具体实现了CrimeDatabase抽象类。
    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,//Context对象，因为数据库要访问文件系统。这里传入的是应用上下文。之前说过，它要比任何activity类都“活得久”
        CrimeDatabase::class.java,//Room用来创建数据库的类
        DATABASE_NAME//Room将要创建的数据库文件的名字
    ).build()
    private val crimeDao = database.crimeDao()//DAO对象

    //让其他类能通过它访问到数据库. 添加两个仓库函数，访问到DAO对象的相应数据库操作函数。
    //既然Room提供DAO里的查询方法实现，我们就通过仓库调用它们。这样处理，仓库代码不仅简洁，还易于理解。
    //fun getCrimes(): List<Crime> = crimeDao.getCrimes()
    //fun getCrime(id: UUID): Crime? = crimeDao.getCrime(id)
//如代码清单11-16所示，让CrimeRepository里的查询函数也返回LiveData对象。
    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    companion object {
        private var INSTANCE: CrimeRepository? = null//private关键字，以此保证不让其他类捣乱生成新的类实例。
        //初始化生成仓库新实例
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        //读取仓库数据
        //注意，如果在CrimeRepository的initialize()函数执行之前有人调用数据读取函数，它就会抛出IllegalStateException异常。因此，你需要在应用启动后就初始化CrimeRepository。
        //为了在应用一启动就完成这件事，可以创建一个Application子类。这样就能掌握应用的生命周期信息了
        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}