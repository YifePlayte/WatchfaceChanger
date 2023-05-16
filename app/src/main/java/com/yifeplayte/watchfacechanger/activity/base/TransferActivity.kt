package com.yifeplayte.watchfacechanger.activity.base

import android.content.Context
import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import com.github.kyuubiran.ezxhelper.EzXHelper
import com.yifeplayte.watchfacechanger.hook.MainHook

open class TransferActivity : MIUIActivity() {
    class FixedClassLoader(private val mModuleClassLoader: ClassLoader, private val mHostClassLoader: ClassLoader): ClassLoader(mBootstrap) {

        companion object {
            private val mBootstrap: ClassLoader = Context::class.java.classLoader!!
        }

        override fun loadClass(name: String, resolve: Boolean): Class<*> {
            runCatching {
                return mBootstrap.loadClass(name)
            }

            runCatching {
                if ("androidx.lifecycle.ReportFragment" == name) {
                    return mHostClassLoader.loadClass(name)
                }
            }

            return try {
                mModuleClassLoader.loadClass(name)
            } catch (e: Exception) {
                mHostClassLoader.loadClass(name)
            }
        }

    }

    override fun getClassLoader(): ClassLoader {
        return FixedClassLoader(MainHook::class.java.classLoader!!, EzXHelper.appContext.classLoader)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val windowState = savedInstanceState.getBundle("android:viewHierarchyState")
        windowState?.let {
            it.classLoader = TransferActivity::class.java.classLoader
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}