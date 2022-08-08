package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

private const val TAG = "MainActivity"

//先在onCrimeSelected(UUID)里打印调试日志，以此实现CrimeListFragment.callbacks接口。
class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //为了以代码的方式把fragment添加给activity，这里显式调用了activity的FragmentManager。我们使用
        //supportFragmentManager属性就能获取activity的fragment管理器。因为使用了Jetpack库版本的fragment和
        //AppCompatActivity类，所以这里用的是supportFragmentManager。前缀support表明它最初来自v4
        //支持库。现在，支持库已重新打包为androidx放在Jetpack库里。
//如果要向activity中添加多个fragment，通常需要分别为每个fragment创建具有不同ID的各种容器。
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        //这段代码创建并提交了一个fragment事务.可解读为：“创建一个新的fragment事务，执行一个fragment添加操作，然后提交该事务。”
            //fragment事务被用来添加、移除、附加、分离或替换fragment队列中的fragment。它们允许你按组执行多个操作，例如，同时添
            //加多个fragment到不同的视图容器里。这是使用fragment动态组装和重新组装用户界面的关键。FragmentManager维护着一个fragment事务回退栈，你可以查
            //看、历数它们。如果fragment事务包含多个操作，那么在事务从回退栈里移除时，其批量操作也会回退。基于这个原因，UI状态更好控制了。
        /*if (currentFragment == null) {
            val fragment = CrimeFragment()
            supportFragmentManager//显式调用了activity的FragmentManager
                .beginTransaction()//函数创建并返回FragmentTransaction实例
                .add(R.id.fragment_container, fragment)//add(...)函数是整个事务的核心，两个参数：容器视图资源ID和新创建的CrimeFragment。
                                                       //容器视图资源ID的作用有：告诉FragmentManager，fragment视图应该出现在activity视图的什么位置；唯一标识FragmentManager队列中的fragment。
                .commit()
        }*/
        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance()//硬编码让MainActivity只显示CrimeListFragment
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    //用户只要在CrimeListFragment界面点击某一条crime记录，就用CrimeFragment实例替换CrimeListFragment（crimeId暂时先忽略）
    override fun onCrimeSelected(crimeId: UUID) {
//在crime列表项界面，每点一条crime记录，就能看到Logcat窗口打印出一条日志。这表明，通过Callbacks.onCrimeSelected(UUID)，点击事件从CrimeListFragment传递到了MainActivity。
        //Log.d(TAG, "MainActivity.onCrimeSelected: $crimeId")


        val fragment = CrimeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)//把一个事务添加到回退栈后，在用户按回退键时，事务会回滚.回退栈状态取个名字，String类型
            .commit()
    }


}
