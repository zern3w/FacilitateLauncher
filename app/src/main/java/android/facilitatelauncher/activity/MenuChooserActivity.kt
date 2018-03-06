package android.facilitatelauncher.activity

import android.content.Context
import android.content.Intent
import android.facilitatelauncher.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import kotlinx.android.synthetic.main.activity_menu_chooser.*

public class MenuChooserActivity : AppCompatActivity() {

    companion object {
        @JvmField val ELDER_TYPE = 0
        @JvmField val HANDICAP_TYPE = 1
    }

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
        cvElder.setOnClickListener { showMainMenu(ELDER_TYPE) }
        cvHandicap.setOnClickListener {  showMainMenu(HANDICAP_TYPE) }
        cvExitApp.setOnClickListener { this.finishAffinity()
            System.exit(0) }
    }

    private fun showMainMenu(type: Int){
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("USER_TYPE", type)
        startActivity(intent)
    }
}
