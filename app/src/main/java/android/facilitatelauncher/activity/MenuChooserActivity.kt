package android.facilitatelauncher.activity

import android.app.PendingIntent.getActivity
import android.content.Context
import android.facilitatelauncher.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import kotlinx.android.synthetic.main.activity_menu_chooser.*

class MenuChooserActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_menu_chooser)

        initListener()
    }

    private fun initListener() {
        cvElder.setOnClickListener { Log.i("test", "Elder") }
        cvHandicap.setOnClickListener {  Log.i("test", "cvHandicap")}
        cvExitApp.setOnClickListener { this.finishAffinity()
            System.exit(0) }
    }


}
