package com.yifeplayte.watchfacechanger.hook

import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.LogExtensions.logexIfThrow
import com.yifeplayte.watchfacechanger.hook.hooks.BaseHook
import com.yifeplayte.watchfacechanger.hook.hooks.common.ApiHolder
import com.yifeplayte.watchfacechanger.hook.hooks.common.DebugMode
import com.yifeplayte.watchfacechanger.hook.hooks.common.HttpRequest
import com.yifeplayte.watchfacechanger.hook.hooks.common.LocalAccount
import com.yifeplayte.watchfacechanger.hook.utils.DexKit
import com.yifeplayte.watchfacechanger.hook.utils.XSharedPreferences.getBoolean
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

private const val TAG = "WatchFaceChanger"
val PACKAGE_NAME_HOOKED = listOf(
    "com.mi.health",
    "com.xiaomi.wearable",
)

@Suppress("unused")
class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "android") DexKit.initDexKit(lpparam)
        EzXHelper.initHandleLoadPackage(lpparam)
        EzXHelper.setLogTag(TAG)
        EzXHelper.setToastTag(TAG)
        initHook(ApiHolder, "change_api", true)
        initHook(DebugMode, "debug_mode", true)
        initHook(HttpRequest, "change_api", true)
        initHook(LocalAccount)
        DexKit.closeDexKit()
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        EzXHelper.initZygote(startupParam)
    }

    private fun initHook(hook: BaseHook, key: String, defValue: Boolean = false) =
        initHook(hook, getBoolean(key, defValue))

    private fun initHook(hook: BaseHook, enable: Boolean = true) {
        if (enable) runCatching {
            if (hook.isInit) return
            hook.init()
            hook.isInit = true
            Log.ix("Inited hook: ${hook.javaClass.simpleName}")
        }.logexIfThrow("Failed init hook: ${hook.javaClass.simpleName}")
    }
}
