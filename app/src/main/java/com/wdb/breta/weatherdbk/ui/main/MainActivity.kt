package com.wdb.breta.weatherdbk.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.wdb.breta.weatherdbk.R
import com.wdb.breta.weatherdbk.ui.close_cities.CloseCityFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  override fun supportFragmentInjector(): AndroidInjector<Fragment> {
    return dispatchingAndroidInjector
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    //init toolbar
    setSupportActionBar(toolbar)

    if (savedInstanceState == null) {
      val closeCityFragment = CloseCityFragment.newInstance()
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.container, closeCityFragment)
        .commit()
    }
  }
}
