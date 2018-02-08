package android.facilitatelauncher.calls;

import android.content.Context
import android.facilitatelauncher.calls.PhonecallReceiver
import android.util.Log
import android.view.View
import android.view.WindowManager
import java.util.*


public class CallReceiver : PhonecallReceiver() {

    internal var mWindowsParams: WindowManager.LayoutParams? = null
    internal var mWindowManager: WindowManager? = null
    internal var mDialogView: View? = null


    override fun onIncomingCallStarted(ctx: Context, number: String?, start: Date) {
        super.onIncomingCallStarted(ctx, number, start)
        //        Intent intent = new Intent(ctx,CallIdentificationActivity_.class);
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        ctx.startActivity(intent);
    }


    override fun onIncomingCallEnded(ctx: Context, number: String?, start: Date?, end: Date) {
        super.onIncomingCallEnded(ctx, number, start, end)
        Log.d("CallReceiver", "onIncomingCallEnded")

    }

    override fun onMissedCall(ctx: Context, number: String?, start: Date?) {
        super.onMissedCall(ctx, number, start)
        Log.d("CallReceiver", "onMissedCall")
    }


}