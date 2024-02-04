package com.wegrzyn.marcin.mychart

import android.graphics.PointF
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MyViewModel: ViewModel() {

    val list = MutableLiveData(List(30) { PointF(it.toFloat(), Random.nextInt(0, 100).toFloat())}.toMutableStateList())
}