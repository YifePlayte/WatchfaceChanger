package com.yifeplayte.watchfacechanger.hook.hooks.common

import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.yifeplayte.watchfacechanger.hook.hooks.BaseHook
import com.yifeplayte.watchfacechanger.hook.utils.DexKit.dexKitBridge

object HttpRequest : BaseHook() {
    override fun init() {
        dexKitBridge.findMethodUsingString {
            usingString = "Only one HTTP method is allowed. Found: %s and %s."
        }.map {
            it.getMethodInstance(EzXHelper.safeClassLoader)
        }.firstOrNull {
            it.parameterCount == 3
                    && it.parameterTypes[0] == String::class.java
                    && it.parameterTypes[1] == String::class.java
                    && it.parameterTypes[2] == Boolean::class.java
        }?.createHook {
            before { param ->
                var path = param.args[1] as String
                path = when (path) {
                    "index/get_operation_info" -> "get_operation_info"
                    "index/get_watchface_list" -> "get_watchface_list"
                    "index/get_hot_watchface_list" -> "get_hot_watchface_list"
                    "prize/by_tab" -> "by_tab"
                    "prize/function_icon_config" -> "function_icon_config"
                    "prize/detail" -> "detail"
                    "get_icons/v2" -> "get_icons"
                    "prize/tabs" -> "tabs"
                    "watch/appstore/multi_get_watch_app_info" -> "get_watch_app_info"
                    "watch/appstore/get_watch_app_list" -> "get_watch_app_list"
                    else -> path
                }
                param.args[1] = path
            }
        }
    }
}