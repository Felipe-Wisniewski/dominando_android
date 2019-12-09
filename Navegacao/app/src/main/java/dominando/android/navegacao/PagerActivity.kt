package dominando.android.navegacao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pager.*
import kotlinx.android.synthetic.main.toolbar.*

class PagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tabsPagerAdapter = TabsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = tabsPagerAdapter
    }
}
