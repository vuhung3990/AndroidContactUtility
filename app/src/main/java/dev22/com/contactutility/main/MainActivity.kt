package dev22.com.contactutility.main

import android.Manifest
import android.content.Intent
import android.widget.Toast
import dev22.com.contactutility.BaseActivity
import dev22.com.contactutility.R
import dev22.com.contactutility.contact.ContactActivity
import dev22.com.contactutility.di.CompositeDisableModule
import dev22.com.contactutility.main.di.DaggerMainComponent
import dev22.com.contactutility.main.di.MainPresenterModule
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {
    companion object {
        const val REQUEST_PERMISSION_CONTACT: Int = 11
    }

    @Inject lateinit var presenter: MainPresenter

    override fun getLayoutContent(): Int = R.layout.activity_main

    override fun getPresenterForAutoDisposeRx(): MainPresenter? = presenter

    override fun initView() {
        btn_import.setOnClickListener { presenter.clickImport() }
        btn_backup.setOnClickListener { presenter.clickBackup() }
    }

    override fun openExport() {
        TODO("not implemented")
    }

    override fun showWarning() {
        showWarningPermissionDenied(container, "permission is need for this action please try again.")
    }

    override fun openImport() {
        // -> open default file explorer
        // -> select .csv file
        // -> check valid ?
        // -> clear all && import to sqlite
        // -> open contact & param
        Toast.makeText(this, "import contact", Toast.LENGTH_LONG).show()
//        startActivity(Intent(this, ContactActivity::class.java))
    }

    override fun requestContactPermission(): Flowable<PermissionRequestResult> = requestPermissionHelper(permission = Manifest.permission.WRITE_CONTACTS, permissionRequestCode = REQUEST_PERMISSION_CONTACT)

    override fun injectDI() {
        DaggerMainComponent
                .builder()
                .mainPresenterModule(MainPresenterModule(this))
                .compositeDisableModule(CompositeDisableModule())
                .build()
                .inject(this)
    }
}
