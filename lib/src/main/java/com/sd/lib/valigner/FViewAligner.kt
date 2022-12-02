package com.sd.lib.valigner

import android.view.View
import com.sd.lib.aligner.Aligner
import com.sd.lib.aligner.Aligner.Position
import com.sd.lib.aligner.Aligner.Result
import com.sd.lib.aligner.FAligner
import com.sd.lib.valigner.ViewAligner.Callback
import java.lang.ref.WeakReference

open class FViewAligner : FAligner(), ViewAligner {
    private val _coordinateTarget = IntArray(2)
    private val _coordinateSource = IntArray(2)
    private val _coordinateContainer = IntArray(2)

    private var _position = Position.Center

    private var _targetRef: WeakReference<View>? = null
    private var _sourceRef: WeakReference<View>? = null
    private var _callback: Callback? = null

    override val position: Position get() = _position
    override val target: View? get() = _targetRef?.get()
    override val source: View? get() = _sourceRef?.get()

    override fun setCallback(callback: Callback?) {
        _callback = callback
    }

    override fun setPosition(position: Position) {
        _position = position
        if (_callback != null) update()
    }

    override fun setTarget(view: View?) {
        val old = target
        if (old != view) {
            _targetRef = if (view == null) null else WeakReference(view)
            onTargetChanged(old, view)
        }
    }

    override fun setSource(view: View?) {
        val old = source
        if (old != view) {
            _sourceRef = if (view == null) null else WeakReference(view)
            onSourceChanged(old, view)
        }
    }

    override fun update(): Result? {
        return updateInternal().also {
            _callback?.onResult(it)
        }
    }

    private fun updateInternal(): Result? {
        val target = target ?: return null
        val source = source ?: return null
        val container = source.parentView() ?: return null

        if (_callback?.canUpdate(target, source, container) == false) {
            return null
        }

        target.getLocationOnScreen(_coordinateTarget)
        source.getLocationOnScreen(_coordinateSource)
        container.getLocationOnScreen(_coordinateContainer)

        val input = Aligner.Input(
            position = _position,

            targetX = _coordinateTarget[0],
            targetY = _coordinateTarget[1],

            sourceX = _coordinateSource[0],
            sourceY = _coordinateSource[1],

            containerX = _coordinateContainer[0],
            containerY = _coordinateContainer[1],

            targetWidth = target.width,
            targetHeight = target.height,

            sourceWidth = source.width,
            sourceHeight = source.height,

            containerWidth = container.width,
            containerHeight = container.height,
        )

        return align(input)
    }

    open fun onTargetChanged(oldView: View?, newView: View?) {}
    open fun onSourceChanged(oldView: View?, newView: View?) {}
}


private fun View?.parentView(): View? {
    if (this == null) return null
    val parent = parent
    return if (parent is View) parent else null
}