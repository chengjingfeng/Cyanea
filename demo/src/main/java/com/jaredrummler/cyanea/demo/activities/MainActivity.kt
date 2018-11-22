package com.jaredrummler.cyanea.demo.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity
import com.jaredrummler.cyanea.demo.R
import com.jaredrummler.cyanea.demo.R.array
import com.jaredrummler.cyanea.demo.R.dimen
import com.jaredrummler.cyanea.demo.R.id
import com.jaredrummler.cyanea.demo.R.layout
import com.jaredrummler.cyanea.demo.R.menu
import com.jaredrummler.cyanea.demo.R.string
import com.jaredrummler.cyanea.demo.fragments.AboutFragment
import com.jaredrummler.cyanea.demo.fragments.DialogsFragment
import com.jaredrummler.cyanea.demo.fragments.OtherFragment
import com.jaredrummler.cyanea.demo.fragments.WidgetsFragment
import com.jaredrummler.cyanea.prefs.CyaneaSettingsActivity
import com.jaredrummler.cyanea.prefs.CyaneaThemePickerActivity
import kotlinx.android.synthetic.main.activity_main.bar
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.tabLayout
import kotlinx.android.synthetic.main.activity_main.viewPager


class MainActivity : CyaneaAppCompatActivity(), OnMenuItemClickListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)

    viewPager.adapter = DemoPagerAdapter(this)
    tabLayout.setupWithViewPager(viewPager)
    addTabLeftMargin()

    bar.replaceMenu(menu.bottom_bar_menu)
    bar.setOnMenuItemClickListener(this)

    fab.setOnClickListener {
      startActivity(Intent(this, CyaneaThemePickerActivity::class.java))
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
    id.action_settings -> {
      startActivity(Intent(this, CyaneaSettingsActivity::class.java))
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  override fun onMenuItemClick(item: MenuItem) = when (item.itemId) {
    id.action_share -> {
      launchShareIntent()
      true
    }
    id.action_github -> {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(string.github_project_url))))
      true
    }
    else -> false
  }

  private fun addTabLeftMargin() {
    if (tabLayout.tabCount > 0) {
      val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(0)
      val p = tab.layoutParams as ViewGroup.MarginLayoutParams
      p.setMargins(resources.getDimension(dimen.margin_default).toInt(), 0, 0, 0)
      tab.requestLayout()
    }
  }

  private fun launchShareIntent() {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_TEXT, getString(string.share_cyanea_message))
      type = "text/plain"
    }
    startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share)))
  }

  override fun getThemeResId(): Int = cyanea.themes.actionBarTheme

  class DemoPagerAdapter(
      private val activity: FragmentActivity
  ) : FragmentStatePagerAdapter(activity.supportFragmentManager) {

    private val items = activity.resources.getStringArray(array.tabs)

    override fun getItem(position: Int): Fragment {
      return when (items[position]) {
        activity.getString(R.string.tab_about) -> AboutFragment()
        activity.getString(R.string.tab_widgets) -> WidgetsFragment()
        activity.getString(R.string.tab_dialogs) -> DialogsFragment()
        activity.getString(R.string.tab_other) -> OtherFragment()
        else -> throw IllegalArgumentException("No fragment associated with tab '${items[position]}'")
      }
    }

    override fun getPageTitle(position: Int): CharSequence? = items[position]

    override fun getCount(): Int = items.size

  }

}
