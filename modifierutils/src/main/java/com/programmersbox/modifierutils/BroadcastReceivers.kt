package com.programmersbox.modifierutils

import android.content.IntentFilter
import androidx.compose.runtime.*

/**
 * Registers a broadcast receiver and unregisters at the end of the composable lifecycle
 *
 * @param defaultValue the default value that this starts as
 * @param intentFilter the filter for intents
 * @see IntentFilter
 * @param tick the callback from the broadcast receiver
 */
/*
@Composable
fun <T : Any> broadcastReceiver(
    defaultValue: T,
    intentFilter: IntentFilter,
    tick: (context: Context, intent: Intent) -> T
): State<T> {
    val item: MutableState<T> = remember { mutableStateOf(defaultValue) }
    val context = LocalContext.current

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                item.value = tick(context, intent)
            }
        }
        context.registerReceiver(receiver, intentFilter)
        onDispose { context.unregisterReceiver(receiver) }
    }
    return item
}

*/
/**
 * Registers a broadcast receiver and unregisters at the end of the composable lifecycle
 *
 * @param defaultValue the default value that this starts as
 * @param intentFilter the filter for intents.
 * @see IntentFilter
 * @param tick the callback from the broadcast receiver
 *//*

@Composable
fun <T : Any> broadcastReceiverNullable(
    defaultValue: T?,
    intentFilter: IntentFilter,
    tick: (context: Context, intent: Intent) -> T?
): State<T?> {
    val item: MutableState<T?> = remember { mutableStateOf(defaultValue) }
    val context = LocalContext.current

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                item.value = tick(context, intent)
            }
        }
        context.registerReceiver(receiver, intentFilter)
        onDispose { context.unregisterReceiver(receiver) }
    }
    return item
}
*/
