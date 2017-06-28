package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import dev22.com.contactutility.R
import dev22.com.contactutility.di.CompositeDisableModule
import dev22.com.contactutility.main.di.DaggerMainComponent
import dev22.com.contactutility.main.di.MainPresenterModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {
    @Inject lateinit var presenter: MainPresenter

    override fun getLayoutContent(): Int = R.layout.activity_main

    override fun getPresenterForAutoDisposeRx(): MainPresenter? = presenter

    override fun initView() {
        btn_import.setOnClickListener { presenter.clickImport() }
    }

    override fun injectDI() {
        DaggerMainComponent
                .builder()
                .mainPresenterModule(MainPresenterModule(this))
                .compositeDisableModule(CompositeDisableModule())
                .build()
                .inject(this)
    }
}
