package com.example.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class CrimeFragment : Fragment() {
    private lateinit var crime: Crime//Crime实例属性
    private lateinit var titleField: EditText//标题字段
    private lateinit var dateButton: Button//显示crime的发生日期
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    /*在onCreateView(...)函数中，fragment的视图是直接通过调
    用LayoutInflater.inflate(...)函数并传入布局的资源ID
    生成的。第二个参数是视图的父视图，我们通常需要父视图来正
    确配置部件。第三个参数告诉布局生成器是否立即将生成的视图
    添加给父视图。这里传入了false参数，因为fragment的视图将
    由activity的容器视图托管。稍后，activity会处理。*/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime,container, false)//布局的资源ID/传入了false参数，因为fragment的视图将由activity的容器视图托管。稍后，activity会处理。

        titleField = view.findViewById(R.id.crime_title) as EditText //p246页，手动调用实例化部件
        dateButton = view.findViewById(R.id.crime_date) as Button
        dateButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        return view
    }

    //p247页在onStart()生命周期回调里给EditText部件添加监听器
    override fun onStart() {
        super.onStart()
        //创建实现TextWatcher监听器接口的匿名内部类。TextWatcher有三个函数
        //本质上来说，这个接口只是用来监听输入的内容和长度的，并不太建议在这个方法中进行字符串的替换。
        //如果要在输入过程中进行字符替换修改。建议使用InputFilter进行监听
        //文章链接： https://zinyan.com/?p=204
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank这个空间有意留空
            }
            override fun onTextChanged(
                sequence: CharSequence?,//调用CharSequence（代表用户输入）的toString()函数。该函数最后返回用来设置Crime标题的字符串
                start: Int,
                before: Int,
                count: Int
            ) {
                crime.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?)
            {
                // This one too
            }
        }
        titleField.addTextChangedListener(titleWatcher)

        //虽然CheckBox部件的监听器不会因fragment的状态恢复而触发，但把它放在onStart()里，代码逻辑会更清楚，后续也更容易查找。
        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }

    }



}