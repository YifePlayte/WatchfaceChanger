package com.yifeplayte.watchfacechanger.hook.hooks

abstract class BaseHook {
    var isInit: Boolean = false
    abstract fun init()
}