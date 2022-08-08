package com.example.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    //托管活动所需的接口.对于这样的fragment，谁托管它，谁就得实现它定义的接口
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }
    private var callbacks: Callbacks? = null//callbacks属性用来保存实现Callbacks接口的对象

    private lateinit var crimeRecyclerView: RecyclerView
    //private var adapter: CrimeAdapter? = null//代码清单11-19关联RecyclerView

    //既然CrimeListFragment改用数据库里的crime数据填充目标crimeRecyclerView，那就先用一个空crime集合初始化循环视图
    //的adapter。然后在LiveData有了新数据后，更新给crimeRecyclerView的adapter。
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())//代码清单11-19关联RecyclerView

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this)[CrimeListViewModel::class.java]
    }

    //覆盖附加onAttach(Context)和分离onDetach()函数以设置和取消callbacks属性
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    //移除旧版本ViewModel引用override fun onCreate(savedInstanceState: Bundle?) {
    //移除旧版本ViewModel引用super.onCreate(savedInstanceState)
    //移除旧版本ViewModel引用Log.d(TAG, "Total crimes:${crimeListViewModel.crimes.size}")
    //移除旧版本ViewModel引用}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)//布局的资源ID
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView//手动调用实例化部件
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)//委托给LayoutManager处理,以竖直列表的形式摆放列表项/LayoutManager不仅要安排列表项出现的位置，还负责定义如何滚屏。

        //为RecyclerView配置adapter
        //搞定了Adapter，最后要做的就是将它和RecyclerView关联起来。实现一个设置CrimeListFragment的UI的updateUI函数，该函数会创建CrimeAdapter，然后配置给RecyclerView
    //移除旧版本ViewModel引用updateUI()//函数换个地方调用
        crimeRecyclerView.adapter = adapter//代码清单11-19关联RecyclerView
        return view
    }

    //代码清单11-19关联RecyclerView,此段代码应用解释详见书本350页
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    //LiveData.observe(LifecycleOwner,Observer)函数用来给LiveData实例登记观察者，让观察者和类似 activity或fragment这样的其他组件同呼吸共命运。
//observe(...)函数的第二个参数是一个Observer实现。这个对象负责响应LiveData的新数据通知。这里，观察者代码块在 crimeListLiveData的crime集合数据有更新时执行。
//收到LiveData数据更新消息后，只要crime属性有值，观察者对象就会打印出日志记录。
    //除非退定，或者让你的Observer实现不再监听目标LiveData的变化，否则，即便fragment的视图处于失效状态（比如被销毁了），你的Observer实现也会尝试更新它。试图更新失效状态视图会让应用崩溃。
//这时，就该LiveData.observe(...)的第一个参数LifecycleOwner登场了。你指定的Observer实现的生命周期会和 LifecycleOwner所代表的Android组件的生命周期保持一致。
//在上述代码中，就是指你的Observer实现和CrimeListFragment视图的生命周期保持一致了。
    //只要Observer实现同步的生命周期拥有者（lifecycle owner）处于有效生命周期状态，LiveData对象一有数据更新就会通知它的观察者。当与Observer实现有着相同生命周期的关联对象不存在了，
    //LiveData对象会自动和Observer实现解除订阅关系。因为LiveData能响应生命周期变化，所以它还有个名字叫生命周期感知组件（lifecycle-aware component）。
    //有关生命周期感知组件的更多内容，详见第25章。
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner, Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)//从数据库读取到crime数据后，定义的Observer实现就会打印日志信息，发送收到的数据给updateUI()准备adapter。
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    //搞定了Adapter，最后要做的就是将它和RecyclerView关联起来。
    //实现一个设置CrimeListFragment的UI的updateUI函数，该函数
    //会创建CrimeAdapter，然后配置给RecyclerView，
    //移除旧版本ViewModel引用private fun updateUI() {
    private fun updateUI(crimes: List<Crime>) {
    //移除旧版本ViewModel引用val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }


        //RecyclerView的任务仅限于回收和摆放屏幕上的View。列表项View能够显示数据还离不开另外两个类的支持：ViewHolder子类和
    //Adapter子类（详见下一节）。ViewHolder会引用列表项视图（有时也会引用列表项视图里的某个具体部件）。
    //CrimeHolder的构造函数首先接收并保存view，然后将其作为值参传递给RecyclerView.ViewHolder的构造函数。这样，这个ViewHolder基类的一个名为itemView的属性就能引用列表项视图了
    //RecyclerView并不会创建View，它只会创建ViewHolder。从图9-7可以看出，是ViewHolder带着其引用着的itemView展现一行行列表项的。
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

//首先添加一个存储Crime的属性，再顺手把TextView属性变为私有，然后向CrimeHolder中添加一个bind(Crime)函数，处理绑定工作。在新添加函数里，把绑定的crime对象赋值给属性变量，
//并设置titleTextView和dateTextView视图的显示文字 .现在，只要取到一个要绑定的Crime，CrimeHolder就会更新显示TextView标题视图和TextView日期视图。
//最后，修改CrimeAdapter类，调用bind(Crime)函数。每次RecyclerView要求CrimeHolder绑定对应的Crime时，都会调用bind(Crime)函数，
            private lateinit var crime: Crime//在创建holder后绑定模型层数据

        //如代码清单9-10所示，更新CrimeHolder，在当前实例的itemView视图层级结构里找到显示题头和日期文字的视图，将它们保存到各自的属性里。
        //升级后的CrimeHolder会引用列表项题头和日期文字，之后想修改它们的值就很容易了，如图9-8所示
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)//添加一个ImageView实例变量

            init {//检测用户点击事件,对于itemView来说，CrimeHolder承担了接收用户点击事件的任务。
                itemView.setOnClickListener(){
                   // Toast.makeText(context, "${crime.title} pressed!",Toast.LENGTH_SHORT).show()
                    callbacks?.onCrimeSelected(crime.id)//在CrimeHolder.onClick(View)函数里，调用Callbacks接口的onCrimeSelected(Crime)函数，响应用户点击crime列表项事件
                }
            }

            //首先添加一个存储Crime的属性，再顺手把TextView属性变为私有，然后向CrimeHolder中添加一个bind(Crime)函数，处理绑定工作。在新添加函数里，把绑定的crime对象赋值给属性变量，并设置titleTextView和dateTextView视图的显示文字。
            //现在，只要取到一个要绑定的Crime，CrimeHolder就会更新显示TextView标题视图和TextView日期视图。
            fun bind(crime: Crime) {
                this.crime = crime
                titleTextView.text = this.crime.title
                dateTextView.text = this.crime.date.toString()
                //根据crime记录的解决状态控制图片的显示
                solvedImageView.visibility = if (crime.isSolved) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }



//Adapter负责：
//创建必要的ViewHolder；
//绑定ViewHolder至模型层数据。
//    RecyclerView负责：
//    请Adapter创建ViewHolder；
//    请Adapter绑定ViewHolder至具体的模型层数据。
//内部类。使用一个主构造函数接收crime集合，存入一个变量中。
// 如图9-9所示，Crime对象是什么样的或者数据集里有多少Crime对象，RecyclerView完全不关心，什么也不知道。
// CrimeAdapter则对这些信息了如指掌，它不仅知道Crime对象的具体内容，还知道数据集里有多少条要显示的crime列表项。
    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

    //Adapter.onCreateViewHolder(...)负责创建要显示的视图，将其封装到一个ViewHolder里并返回结果。这里，我们从list_item_view.xml布局实例化视图，将其传递给CrimeHolder。
    //（onCreateViewHolder(...)函数的参数现在可以忽略。要在同一RecyclerView里显示不同视图时，我们才需要关心该如何设置参数值。详细信息可参看9.10节。）
//图9-10 生动有趣的RecyclerView-Adapter会话.首先，RecyclerView会调用Adapter的onCreateViewHolder(ViewGroup, Int)函数创建ViewHolder
//及其要显示的视图。此时，Adapter创建并返给RecyclerView的ViewHolder（和它的itemView）还没有数据。
//然后，RecyclerView会调用onBindViewHolder(ViewHolder,Int)函数，传入ViewHolder和Crime对象的位置。Adapter会找 到目标位置的数据并将其绑定到ViewHolder的视图上。
//所谓绑定，就是使用模型对象数据填充视图。整个过程执行完毕，RecyclerView就能在屏幕上显示crime列表项了。
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

    //RecyclerView想知道数据集里到底有多少数据时，会让Adapter调用Adapter.getItemCount()函数。这里，响应RecyclerView，getItemCount()会返回crime数据集里有多少个列表项要显示。
        override fun getItemCount() = crimes.size

    //Adapter.onBindViewHolder(holder: CrimeHolder,position: Int)负责将数据集里指定位置的crime数据发送给指定
    //ViewHolder。这里，我们首先从crime集合里取出指定位置的crime数据，然后使用其中的题头和日期信息设置相应的TextView视图。
//试试看，即便一通猛滑，列表项应该还是滚动得非常流畅。这要归功于onBindViewHolder(...)函数。
// 任何时候，都要尽量确保这个函数轻巧、高效！！！
    //9.7清理绑定，详见286页,见：58行注释
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
        //当前，在Adapter.onBindViewHolder(...)函数里，Adapter是把crime数据直接和CrimeHolder的TextView视图绑定的。这么做虽然可行，但最好是把ViewHolder和Adapter各自该做的工作分
        //清。Adapter应尽量不插手ViewHolder的内部工作和细节。因此，我们推荐把数据和视图的绑定工作都放在CrimeHolder里处理。
            //holder.apply {
            //    titleTextView.text = crime.title
             //   dateTextView.text = crime.date.toString()
            //}
            holder.bind(crime)//每次RecyclerView要求CrimeHolder绑定对应的Crime时，都会调用bind(Crime)函数
        }
    }


        //为了让activity调用获取fragment实例，我们添加了一个newInstance(...)函数。这是个不错的做法，和在GeoQuiz里使用newIntent()很相似
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}
