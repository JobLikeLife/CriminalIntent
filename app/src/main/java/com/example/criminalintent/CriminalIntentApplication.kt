package com.example.criminalintent

import android.app.Application

//创建一个名为CriminalIntentApplication的类，让它继承Application类，然后覆盖Application.onCreate()函数进行CrimeRepository类的初始化
//系统要能使用应用程序类，还需要在manifest文件里先登记
//登记好CriminalIntentApplication之后，应用一启动，操作系统就会调用CriminalIntentApplication的onCreate()函数。
//然后，CrimeRepository就完成了初始化，欢迎其他对象随时来访。
class CriminalIntentApplication: Application() {

    //类似Activity.onCreate(...)，应用一加载到内存里，系统就会调用Application.onCreate()函数。对于这种一次性的初始化工作，Application.onCreate()函数很不错。
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)//在应用启动后就初始化CrimeRepository.创建一个Application子类。这样就能掌握应用的生命周期信息了
    }
}