package com.yifeplayte.watchfacechanger.hook.hooks.common

import android.app.Activity
import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import com.yifeplayte.watchfacechanger.hook.hooks.BaseHook

object Feedback : BaseHook() {
    override fun init() {
        loadClass("com.xiaomi.fitness.feedback.bugreport.FeedBackDeviceActivity").methodFinder().first {
            name == "onCreate"
        }.createHook {
            after { param ->
                if (!EzXHelper.isAppContextInited) {
                    EzXHelper.initAppContext(param.thisObject as Activity)
                }
            }
        }
    }
}