package ru.skillbranch.skillarticles.viewmodels.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelDelegate<T: ViewModel>(private val clazz: Class<T>, private val arg: Any?) :
    ReadOnlyProperty<FragmentActivity, T> {

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
//        val factory = ViewModelFactory (arg!!)
val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(thisRef.getApplication());
        return ViewModelProviders.of(thisRef, factory).get(clazz)

//        fun <T : ViewModel?> getViewModel(activity: FragmentActivity, modelClass: Class<T>): T {
//            return ViewModelProviders.of(this, ViewModelFactory(activity)).get(modelClass)

        }
}