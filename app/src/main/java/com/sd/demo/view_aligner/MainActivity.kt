package com.sd.demo.view_aligner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.view_aligner.databinding.ActivityMainBinding
import com.sd.lib.aligner.Aligner
import com.sd.lib.valigner.FViewAligner
import com.sd.lib.valigner.ViewAligner

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    private val _aligner by lazy {
        FViewAligner().apply {
            // 设置目标View
            setTarget(_binding.viewTarget)

            // 设置源View
            setSource(_binding.viewSource)

            // 设置回调对象
            setCallback(object : ViewAligner.Callback {
                override fun onResult(result: Aligner.Result?) {
                    handleResult(result)
                }
            })
        }
    }

    private val _onPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        _aligner.update()
        true
    }

    private fun handleResult(result: Aligner.Result?) {
        if (result == null) return

        val x = result.x
        val y = result.y

        logMsg { "(${x}, ${y})" }
        logMsg { "sourceOverflow: ${result.sourceOverflow}" }
        logMsg { "targetOverflow: ${result.targetOverflow}" }

        val view = _binding.viewSource
        view.layout(x, y, x + view.measuredWidth, y + view.measuredHeight)
    }

    override fun onClick(v: View) {
        when (v) {
            _binding.btnStart -> {
                // 开始
                with(window.decorView.viewTreeObserver) {
                    removeOnPreDrawListener(_onPreDrawListener)
                    addOnPreDrawListener(_onPreDrawListener)
                }
            }
            _binding.btnStop -> {
                // 停止
                with(window.decorView.viewTreeObserver) {
                    removeOnPreDrawListener(_onPreDrawListener)
                }
            }

            _binding.btnTopStart -> _aligner.setPosition(Aligner.Position.TopStart)
            _binding.btnTopCenter -> _aligner.setPosition(Aligner.Position.TopCenter)
            _binding.btnTopEnd -> _aligner.setPosition(Aligner.Position.TopEnd)

            _binding.btnBottomStart -> _aligner.setPosition(Aligner.Position.BottomStart)
            _binding.btnBottomCenter -> _aligner.setPosition(Aligner.Position.BottomCenter)
            _binding.btnBottomEnd -> _aligner.setPosition(Aligner.Position.BottomEnd)

            _binding.btnStartTop -> _aligner.setPosition(Aligner.Position.StartTop)
            _binding.btnStartCenter -> _aligner.setPosition(Aligner.Position.StartCenter)
            _binding.btnStartBottom -> _aligner.setPosition(Aligner.Position.StartBottom)

            _binding.btnEndTop -> _aligner.setPosition(Aligner.Position.EndTop)
            _binding.btnEndCenter -> _aligner.setPosition(Aligner.Position.EndCenter)
            _binding.btnEndBottom -> _aligner.setPosition(Aligner.Position.EndBottom)

            _binding.btnCenter -> _aligner.setPosition(Aligner.Position.Center)
        }
    }
}

inline fun logMsg(block: () -> String) {
    Log.i("view-aligner-demo", block())
}