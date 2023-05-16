package com.yifeplayte.watchfacechanger.activity.pages

import android.annotation.SuppressLint
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity.Companion.safeSP
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.yifeplayte.watchfacechanger.R
import com.yifeplayte.watchfacechanger.hook.PACKAGE_NAME_HOOKED
import com.yifeplayte.watchfacechanger.utils.Terminal

@SuppressLint("NonConstantResourceId")
@BMMainPage(titleId = R.string.app_name)
class MainPage : BasePage() {
    override fun onCreate() {
        TitleText(textId = R.string.health)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.debug_mode
            ),
            SwitchV("debug_mode", true)
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.change_api
            ),
            SwitchV("change_api", true)
        )

        TitleText("API")
        TextSummaryWithArrow(
            TextSummaryV(
                "FaceApiService",
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle("FaceApiService")
                        setEditText(safeSP.getString("face_api_service_host", "https://miwatch.conversmod.ru/"), "host")
                        setEditText(safeSP.getString("face_api_service_path", "api/"), "path")
                        setLButton("Cancel") {
                            dismiss()
                        }
                        setRButton("OK") {
                            dismiss()
                        }
                    }.show()
                }
            )
        )

        TitleText(textId = R.string.reboot)
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.restart_all_scope
            ) {
                MIUIDialog(activity) {
                    setTitle(R.string.warning)
                    setMessage(R.string.restart_all_scope_tips)
                    setLButton(R.string.cancel) {
                        dismiss()
                    }
                    setRButton(R.string.done) {
                        PACKAGE_NAME_HOOKED.forEach {
                            if (it != "android") Terminal.exec("killall $it")
                        }
                        Toast.makeText(
                            activity,
                            getString(R.string.finished),
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                }.show()
            }
        )
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.reboot_system
            ) {
                MIUIDialog(activity) {
                    setTitle(R.string.warning)
                    setMessage(R.string.reboot_tips)
                    setLButton(R.string.cancel) {
                        dismiss()
                    }
                    setRButton(R.string.done) {
                        Terminal.exec("/system/bin/sync;/system/bin/svc power reboot || reboot")
                    }
                }.show()
            }
        )
    }
}