package com.yifeplayte.watchfacechanger.hook.hooks.common

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHooks
import com.github.kyuubiran.ezxhelper.ObjectUtils
import com.yifeplayte.watchfacechanger.hook.hooks.BaseHook

object DebugMode : BaseHook() {
    override fun init() {
        loadClass("com.xiaomi.fitness.common.log.Logger").constructors.createHooks {
            after { param ->
                ObjectUtils.setObject(param.thisObject, "isDebug", true)
                ObjectUtils.setObject(param.thisObject, "sIsDebug", true)
            }
        }
    }
}