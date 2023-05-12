package isel.acrae.postchat.utils

import androidx.compose.foundation.gestures.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlin.math.abs

fun Modifier.pointerInput(
    key1: Any?,
    enabled: Boolean,
    block: suspend PointerInputScope.() -> Unit
): Modifier = if (enabled) Modifier.pointerInput(key1, block)
else this


/**
 * Detect Zoom, Pan or Drag operations in [PointerInputScope].
 *
 * Checks how many fingers are pressed to apply any operation:
 * * One finger pressed applies Drag operation.
 * * Two fingers pressed applies Zoom or Pan operations. Only when [touchSlop] has been passed can these operations occur.
 * @param touchSlop the threshold to pass to apply Zoom or Pan.
 */
suspend fun AwaitPointerEventScope.zoomPanOrDrag(
    touchSlop: Float = 2f,
    onZoomOrPan: (zoom : Float, pan : Offset) -> Unit,
    onDragStart: (PointerInputChange) -> Unit,
    onDrag: (PointerInputChange) -> Unit,
    onDragEnd: (PointerInputChange) -> Unit,
) {
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val down = awaitFirstDown(requireUnconsumed = true)
        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.any { it.isConsumed }

            //check how many pressed fingers
            when (event.changes.count { it.pressed }) {
                1 -> {
                    onDragStart(down)
                    var pointer = down
                    val inputChange = awaitDragOrCancellation(down.id)
                    if (inputChange != null) {
                        drag(inputChange.id) {
                            pointer = it
                            onDrag(pointer)
                        }
                        onDragEnd(pointer)
                        break
                    } else {
                        onDragEnd(pointer)
                        break
                    }
                }
                2 -> {
                    if (!canceled) {
                        val zoomChange = event.calculateZoom()
                        val panChange = event.calculatePan()
                        if (!pastTouchSlop) {
                            zoom *= zoomChange
                            pan += panChange

                            val centroidSize = event.calculateCentroidSize(useCurrent = false)
                            val zoomMotion = abs(1 - zoom) * centroidSize
                            val panMotion = pan.getDistance()

                            if (zoomMotion > touchSlop || panMotion > touchSlop) {
                                pastTouchSlop = true
                            }
                        }

                        if (pastTouchSlop) {
                            if (zoomChange != 1f || panChange != Offset.Zero)
                                onZoomOrPan(zoomChange, panChange)

                            event.changes.forEach {
                                if (it.positionChanged()) {
                                    it.consume()
                                }
                            }
                        }
                    }
                }
            }
        } while (!canceled && event.changes.any{ it.pressed })

        //consume all of first down changes if it has not
        down.consume()
}

/**
 * Await for a long press in [PointerInputScope].
 * @param initialDown the first input change o occur.
 * @return null if there is no long press (if the [initialDown] pointer has moved or is consumed) or [PointerInputChange] otherwise.
 */
suspend fun PointerInputScope.awaitLongPressOrDragCancel(
    initialDown: PointerInputChange
): PointerInputChange? {
    var longPress: PointerInputChange? = null
    var currentDown = initialDown
    val longPressTimeout = 300L
    var prevOffset: Offset? = null
    return try {
        withTimeout(longPressTimeout) {
            awaitPointerEventScope {
                var finished = false
                while (!finished) {
                    val event = awaitPointerEvent()
                    if (event.changes.all { it.changedToUpIgnoreConsumed() }) {
                        // All pointers are up
                        finished = true
                    }
                    if (event.changes.any {
                            it.consumed.downChange || it.isOutOfBounds(size, extendedTouchPadding)
                        }
                    ) {
                        finished = true // Canceled
                    }

                    // Check for cancel by position consumption. We can look on the Final pass of
                    // the existing pointer event because it comes after the Main pass we checked
                    // above.

                    if (prevOffset != null && prevOffset != event.changes.first().position) {
                        finished = true
                    }
                    if (event.changes.firstOrNull { it.id == currentDown.id }?.pressed == true    /*isPointerUp(currentDown.id)*/) {
                        longPress = event.changes.firstOrNull { it.id == currentDown.id }
                    } else {
                        val newPressed = event.changes.firstOrNull { it.pressed }
                        if (newPressed != null) {
                            currentDown = newPressed
                            longPress = currentDown
                        } else {
                            // should technically never happen as we checked it above
                            finished = true
                        }
                    }
                    prevOffset = event.changes.first().position
                }
            }
        }
        null
    } catch (_: TimeoutCancellationException) {
        longPress ?: initialDown
    }
}