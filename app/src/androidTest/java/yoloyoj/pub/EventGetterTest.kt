package yoloyoj.pub

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import yoloyoj.pub.web.handlers.EventGetter

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class EventGetterTest {
    @Test
    fun eventGetter() {
        EventGetter { events ->
            Log.i(
                "list of events",
                events.joinToString(",\n", "[\n", "\n]") { event ->
                    event.toString()
                }
            )
        }.start()
    }
}
