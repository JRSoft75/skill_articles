package ru.skillbranch.skillarticles.viewmodels.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/*
class ViewModelDelegate<T: ViewModel>(private val clazz: Class<T>, private val arg: Any?) :
    ReadOnlyProperty<FragmentActivity, T> {

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        val factory = ViewModelFactory (arg!!)
        //val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(thisRef.getApplication());

                if (clazz.isAssignableFrom(ViewModel::class.java)) {
                    return (T) model
                }
                throw new IllegalArgumentException("unexpected model class " + modelClass);


    }
        return ViewModelProviders.of(thisRef, factory).get(clazz)



}*/
