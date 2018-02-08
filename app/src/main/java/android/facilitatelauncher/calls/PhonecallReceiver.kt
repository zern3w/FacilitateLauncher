package android.facilitatelauncher.calls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

import java.util.Date

abstract class PhonecallReceiver : BroadcastReceiver() {

    //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
    private var lastState = TelephonyManager.CALL_STATE_IDLE
    private var callStartTime: Date? = null
    private var isIncoming: Boolean = false
    private var savedNumber: String? = null  //because the passed incoming is only valid in ringing


    override fun onReceive(context: Context, intent: Intent) {
        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
            savedNumber = intent.extras?.getString("android.intent.extra.PHONE_NUMBER")
        } else {
            val stateStr = intent.extras?.getString(TelephonyManager.EXTRA_STATE)
            val number = intent.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            var state = 0
            if (stateStr == TelephonyManager.EXTRA_STATE_IDLE) {
                state = TelephonyManager.CALL_STATE_IDLE
                Log.d("PhonecallReceiver", "CALL_STATE_IDLE")
//                CallIdentificationManager.getInstance().dimissDialogs()

            } else if (stateStr == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                state = TelephonyManager.CALL_STATE_OFFHOOK
                Log.d("PhonecallReceiver", "CALL_STATE_OFFHOOK")

            } else if (stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
                state = TelephonyManager.CALL_STATE_RINGING
                Log.d("PhonecallReceiver", "CALL_STATE_RINGING")
            }
            onCallStateChanged(context, state, number)
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected open fun onIncomingCallStarted(ctx: Context, number: String?, start: Date) {}
    protected open fun onOutgoingCallStarted(ctx: Context, number: String?, start: Date) {}
    protected open fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {}
    protected open fun onOutgoingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {}
    protected open fun onMissedCall(ctx: Context, number: String?, start: Date?) {}

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    fun onCallStateChanged(context: Context, state: Int, number: String?) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }
//        shouldAnswerCallViaNotification = state==TelephonyManager.CALL_STATE_RINGING
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {

                isIncoming = true
                callStartTime = Date()
                savedNumber = number
                onIncomingCallStarted(context, number, callStartTime!!)
//                CallIdentificationManager.getInstance().fetchIncomingInfo(context, number)
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                    onOutgoingCallStarted(context, savedNumber, callStartTime!!)
                }
//                CallIdentificationManager.getInstance().dimissDialogs()
            }
            TelephonyManager.CALL_STATE_IDLE ->
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
//                    onMissedCall(context, savedNumber, callStartTime)
//                    CallIdentificationManager.getInstance().showMissedCallDialog(context, savedNumber)
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, Date())
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                }
        }
        lastState = state
    }

}