package dominando.android.navegacao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_tabs.*
import kotlinx.android.synthetic.main.toolbar.*

class TabsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)

        setSupportActionBar(toolbar)

        val pagerAdapter = TabsPagerAdapter(this, supportFragmentManager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}
