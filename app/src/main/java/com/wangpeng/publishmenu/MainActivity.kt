package com.wangpeng.publishmenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.wangpeng.publishmenu.waveview.PublishWaveView

class MainActivity : AppCompatActivity(), PublishMenu.OnIconClickListener {
    private lateinit var publish_wave: PublishWaveView
    private lateinit var center_img: ImageView
    private lateinit var bg_img: ImageView
    private lateinit var center_layout: RelativeLayout
    private lateinit var publishMenu: PublishMenu
    private lateinit var publish_container: FrameLayout
    private lateinit var mAnimationHandler: PublishMenuAnimationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        publish_container = findViewById<FrameLayout>(R.id.publish_container);
        center_layout = findViewById<RelativeLayout>(R.id.center_layout)
        center_img = findViewById<ImageView>(R.id.center_img)
        bg_img = findViewById<ImageView>(R.id.bg_img)
        publish_wave = findViewById<PublishWaveView>(R.id.publish_wave)
        publishMenu = findViewById<PublishMenu>(R.id.publish_menu)
        publishMenu.publishContiner = publish_container
        publishMenu.openSmallOrNormal = false
        publishMenu.smallDrawables = intArrayOf(R.drawable.fadan_heyibei1, R.drawable.fadan_kandianying1,
                R.drawable.fadan_kge1, R.drawable.fadan_shipinliaotian1,
                R.drawable.fadan_meishi1, R.drawable.fadan_more1)
        publishMenu.normalDrawables = intArrayOf(R.drawable.fadan_heyibei, R.drawable.fadan_kandianying,
                R.drawable.fadan_kge, R.drawable.fadan_shipinliaotian,
                R.drawable.fadan_meishi, R.drawable.fadan_more)
        publishMenu.mOnIconClickListener = this
        mAnimationHandler = PublishMenuAnimationHandler()
        mAnimationHandler.setPublishMenu(publishMenu)
        publishMenu.postDelayed({
            publishMenu.init()
        }, 100);
        publish_wave.start()
        center_layout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "点击发散按钮", Toast.LENGTH_SHORT).show()
                if (publishMenu.openSmallOrNormal) {
                    PublishMenuHelper.rorateCenter(center_img, true)
                    PublishMenuHelper.scaleCenter(center_layout, true, object : PublishMenuHelper.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            bg_img.setImageResource(R.drawable.fadan_guanbi)
                            publish_wave.clearAnimation()
                            publish_wave.stop()
                            mAnimationHandler.smallInNormalOut()
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            publishMenu.openSmallOrNormal = false
                        }
                    })
                } else {
                    PublishMenuHelper.rorateCenter(center_img, false)
                    PublishMenuHelper.scaleCenter(center_layout, false, object : PublishMenuHelper.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            bg_img.setImageResource(R.drawable.fadan)
                            mAnimationHandler.normalInSmallOut()
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            publish_wave.clearAnimation()
                            publish_wave.start()
                            publishMenu.openSmallOrNormal = true
                        }
                    })

                }
            }
        });
        PublishMenuHelper.setNormalStartEndAngle(publishMenu, -190.0f, 12.0f)
        PublishMenuHelper.setSmallStartEndAngle(publishMenu, -135.0f, 45.0f)
    }

    override fun onClick(view: View) {
        when (view.id) {
            0 -> {
                Toast.makeText(this@MainActivity, "喝一杯", Toast.LENGTH_SHORT).show()
            }
            1 -> {
                Toast.makeText(this@MainActivity, "看电影", Toast.LENGTH_SHORT).show()
            }
            2 -> {
                Toast.makeText(this@MainActivity, "K歌", Toast.LENGTH_SHORT).show()
            }
            3 -> {
                Toast.makeText(this@MainActivity, "视频聊天", Toast.LENGTH_SHORT).show()
            }
            4 -> {
                Toast.makeText(this@MainActivity, "吃美食", Toast.LENGTH_SHORT).show()
            }
            5 -> {
                Toast.makeText(this@MainActivity, "更多", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
