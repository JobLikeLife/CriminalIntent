package com.example.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {

    //删除数据生成代码，改用CrimeRepository的getCrimes()函数从数据库读取crime数据
    private val crimeRepository = CrimeRepository.get()
    //val crimes = crimeRepository.getCrimes()
    val crimeListLiveData = crimeRepository.getCrimes()//给crimes属性换个更醒目的名字
    /*
    val crimes = mutableListOf<Crime>()//新建List将包含用户自建的Crime，用户可自由存取它们
    init {
        //批量存入100个乏味的Crime对象
        for (i in 0 until 100) {
            val crime = Crime()
            crime.title = "Crime #$i"
            crime.isSolved = i % 2 == 0
            crimes += crime
        }
    }*/
}