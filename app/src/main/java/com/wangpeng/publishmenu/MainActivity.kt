package com.wangpeng.publishmenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.wangpeng.publishmenu.waveview.PublishWaveView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
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
        publishMenu = findViewById<PublishMenu>(R.id.publish_menu)
        publish_container = findViewById<FrameLayout>(R.id.publish_container);
        center_layout = findViewById<RelativeLayout>(R.id.center_layout)
        center_img = findViewById<ImageView>(R.id.center_img)
        bg_img = findViewById<ImageView>(R.id.bg_img)
        publish_wave = findViewById<PublishWaveView>(R.id.publish_wave)
        publishMenu.publishContiner = publish_container
        publishMenu.smallDrawables = intArrayOf(R.drawable.fadan_heyibei1, R.drawable.fadan_kandianying1,
                R.drawable.fadan_kge1, R.drawable.fadan_shipinliaotian1,
                R.drawable.fadan_meishi1, R.drawable.fadan_more1)
        publishMenu.normalDrawables = intArrayOf(R.drawable.fadan_heyibei, R.drawable.fadan_kandianying,
                R.drawable.fadan_kge, R.drawable.fadan_shipinliaotian,
                R.drawable.fadan_meishi, R.drawable.fadan_more)
        mAnimationHandler = PublishMenuAnimationHandler()
        mAnimationHandler.setPublishMenu(publishMenu)
        publishMenu.postDelayed({
            publishMenu.init()
        }, 100);
        publish_wave.start()
        center_layout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "点击发散按钮:" + PublishMenuHelper.smallOrBigState, Toast.LENGTH_SHORT).show()
                if (PublishMenuHelper.smallOrBigState) {
                    PublishMenuHelper.rorateCenter(center_img, false)
                    PublishMenuHelper.scaleCenter(center_layout, false, object : PublishMenuHelper.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            bg_img.setImageResource(R.drawable.fadan)
                            mAnimationHandler.normalInSmallOut()
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            publish_wave.clearAnimation()
                            publish_wave.start()
                        }
                    })
                } else {
                    PublishMenuHelper.rorateCenter(center_img, true)
                    PublishMenuHelper.scaleCenter(center_layout, true, object : PublishMenuHelper.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            bg_img.setImageResource(R.drawable.fadan_guanbi)
                            publish_wave.clearAnimation()
                            publish_wave.stop()
                            mAnimationHandler.smallInNormalOut()
                        }

                        override fun onAnimationEnd(animation: Animation?) {

                        }
                    })
                }
            }
        });
        PublishMenuHelper.setNormalStartEndAngle(publishMenu, -190.0f, 12.0f)
        PublishMenuHelper.setSmallStartEndAngle(publishMenu, -135.0f, 45.0f)
    }

    override fun onDestroy() {
        super.onDestroy()
        PublishMenuHelper.smallOrBigState = false
    }
}
