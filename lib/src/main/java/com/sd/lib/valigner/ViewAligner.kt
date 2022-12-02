package com.sd.lib.valigner

import android.view.View
import com.sd.lib.aligner.Aligner
import com.sd.lib.aligner.Aligner.Result

interface ViewAligner : Aligner {
    /**
     * 对齐的位置
     */
    val position: Aligner.Position

    /**
     * 目标View
     */
    val target: View?

    /**
     * 源View
     */
    val source: View?

    /**
     * 设置回调
     */
    fun setCallback(callback: Callback?)

    /**
     * 设置要对齐的位置
     */
    fun setPosition(position: Aligner.Position)

    /**
     * 设置目标View
     */
    fun setTarget(view: View?)

    /**
     * 设置源View
     */
    fun setSource(view: View?)

    /**
     * 更新
     */
    fun update(): Result?

    interface Callback {
        /**
         * 在[ViewAligner.update]之前触发，可以做一些初始化操作。
         *
         * @return true-继续更新，false-跳过本次更新
         */
        fun canUpdate(target: View, source: View, sourceContainer: View): Boolean {
            if (!target.isAttachedToWindow) return false
            if (!source.isAttachedToWindow) return false
            if (!sourceContainer.isAttachedToWindow) return false

            if (target.width <= 0 || target.height <= 0) return false
            if (source.width <= 0 || source.height <= 0) return false
            if (sourceContainer.width <= 0 || sourceContainer.height <= 0) return false

            return true
        }

        /**
         * 结果回调
         */
        fun onResult(result: Result?)
    }
}