package com.yifeplayte.watchfacechanger.hook.hooks.common

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import com.yifeplayte.watchfacechanger.hook.hooks.BaseHook


object LocalAccount : BaseHook() {
    override fun init() {
        loadClass("com.xiaomi.fitness.account.manager.AccountManagerImpl").methodFinder()
            .filterByName("isLocal").first().createHook {
                before {
                    it.result = true
                }
            }
    }
}
