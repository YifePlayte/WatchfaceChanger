package com.yifeplayte.watchfacechanger.hook.hooks.common

import com.github.kyuubiran.ezxhelper.ClassUtils.loadClass
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.ObjectHelper.Companion.objectHelper
import com.github.kyuubiran.ezxhelper.ObjectUtils
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import com.yifeplayte.watchfacechanger.hook.hooks.BaseHook
import java.util.concurrent.ConcurrentHashMap


object ApiHolder : BaseHook() {
    override fun init() {
        @Suppress("UNCHECKED_CAST")
        val clazzSecret = loadClass("com.xiaomi.fitness.net.url.Secret") as Class<Annotation>
        val clazzSecretData = loadClass("com.xiaomi.fitness.net.url.SecretData")
        val clazzDefaultUrlData = loadClass("com.xiaomi.fitness.net.url.DefaultUrlData")


        loadClass("com.xiaomi.fitness.net.url.ApiHolder").methodFinder().first {
            name == "getAnnotationUrl"
        }.createHook {
            before { param ->
                val clazz = param.args[0] as Class<*>
                val host: String
                val path: String
                when (clazz.name) {
                    "com.xiaomi.fitness.watch.face.request.FaceApiService" -> {
                        host = "https://miwatch.conversmod.ru/"
                        path = "api/"
                    }

                    "com.xiaomi.fitness.watch.face.request.FaceMarketApiService" -> {
                        host = "https://miwatch.conversmod.ru/"
                        path = "api/"
                    }

                    "com.xiaomi.xms.wearable.request.ThirdAppService" -> { //encrypt false
                        host = "https://miwatch.conversmod.ru/"
                        path = "micolor/api/"
                    }

                    else -> return@before
                }
                var baseUrlData =
                    ObjectUtils.getObjectOrNullAs<ConcurrentHashMap<Class<*>, Any>>(param.thisObject, "mUrlDatas")
                        ?.get(clazz) // // val classLoader: ClassLoader = lpparam.classLoader
                // val DefaultUrlDataClass = classLoader.loadClass("com.xiaomi.fitness.net.url.DefaultUrlData")

                if (baseUrlData == null) {
                    baseUrlData = clazzDefaultUrlData.constructors.first {
                        it.parameterCount == 2
                    }.newInstance(host, path)
                    ObjectUtils.getObjectOrNullAs<ConcurrentHashMap<Class<*>, Any>>(param.thisObject, "mUrlDatas")
                        ?.put(clazz, baseUrlData)
                }
                param.result = baseUrlData
            }
        }

        loadClass("com.xiaomi.fitness.net.url.ApiHolder").methodFinder().first {
            name == "getPath"
        }.createHook {
            after { param ->
                var result = param.result as String
                result = when (result) {
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
                    else -> result
                }
                param.result = result
            }
        }

        loadClass("com.xiaomi.fitness.net.url.ApiHolder").methodFinder().first {
            name == "updateSecret"
        }.createHook {
            before { param ->
                val clazz = param.args[0] as Class<*>
                if (clazz.name != "com.xiaomi.xms.wearable.request.ThirdAppService") return@before
                var url: String? = null
                ObjectUtils.getObjectOrNullAs<ConcurrentHashMap<Class<*>, Any>>(param.thisObject, "mUrlDatas")
                    ?.get(clazz)?.objectHelper {
                        url = invokeMethodBestMatch("getHost") as String + invokeMethodBestMatch("getPath") as String
                    }
                url?.let {
                    val secret = clazz.getAnnotation(clazzSecret)!!
                    val secretData = clazzSecretData.constructors.first {
                        it.parameterCount == 5
                    }.newInstance(
                        false,
                        secret.objectHelper().invokeMethodBestMatch("pathPrefix"),
                        secret.objectHelper().invokeMethodBestMatch("filterSignatureKeys"),
                        secret.objectHelper().invokeMethodBestMatch("sid"),
                        secret.objectHelper().invokeMethodBestMatch("loginPolicy")
                    )
                    ObjectUtils.getObjectOrNullAs<ConcurrentHashMap<String, Any>>(param.thisObject, "mSecrets")
                        ?.put(it, secretData)
                }
            }
        }
    }
}