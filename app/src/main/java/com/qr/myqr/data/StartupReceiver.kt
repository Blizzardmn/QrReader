package com.qr.myqr.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.qr.myqr.appIns
import com.qr.myqr.pop.PopManager

object StartupReceiver {


    fun doRegister(context: Context) {
        registerScreen(context)
        registerOthers(context)
    }

    private fun registerScreen(context: Context) {
        // 监听屏幕锁定、解除锁定广播、解锁
        context.apply {
            val broadcast = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    when (val action = intent?.action) {
                        Intent.ACTION_SCREEN_OFF -> {
                            appIns.isScreenOn = false
                            //FirebaseEvent.ins.logEvent("onepaper_trigger")
                            //KeepPixelManager.ins.startOnePixelByNotify(context)
                        }

                        Intent.ACTION_SCREEN_ON -> {
                            appIns.isScreenOn = true
                            //KeepPixelManager.ins.finishOnePixel()
                        }

                        //解锁
                        Intent.ACTION_USER_PRESENT -> {
                            PopManager.pop()
                        }

                        //充电
                        Intent.ACTION_POWER_CONNECTED -> {
                            PopManager.pop()
                        }
                    }
                }
            }
            registerReceiver(broadcast, IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON) //开屏
                addAction(Intent.ACTION_SCREEN_OFF) //锁屏
                addAction(Intent.ACTION_USER_PRESENT) //解锁
                addAction(Intent.ACTION_POWER_CONNECTED) //充电
            })
        }
    }

    private const val SYSTEM_DIALOG_REASON_KEY = "reason"
    private const val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
    private fun registerOthers(context: Context) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)

        context.apply {
            val broadcast = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val action = intent?.action ?: return
                    when (action) {
                        /*Intent.ACTION_CLOSE_SYSTEM_DIALOGS -> {
                            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                            if (reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
                                //随机等待 0-2秒 触发弹出
                                GlobalScope.launch {
                                    var isRandom1 = true
                                    if (Random().nextInt(10) % 2 == 0) {
                                        delay(2000L)
                                        isRandom1 = false
                                    }
                                    Log.i(tag, "主页按键弹窗触发")
                                    NotifyRealFinder.ins.doHomePressedRandom4ForRealAlert(NotifySource.TriggerHomeKey, isRandom1)
                                }
                            }
                        }*/

                        Intent.ACTION_BATTERY_CHANGED -> {
                            val currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                            val maxLevel = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
                            val percent = (currentLevel / maxLevel.toFloat() * 100).toInt()
                            PopManager.pop()
                        }
                    }
                }
            }
            registerReceiver(broadcast, intentFilter)
        }
    }

}