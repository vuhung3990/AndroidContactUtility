package dev22.com.contactutility.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import dev22.com.contactutility.BaseActivity
import dev22.com.contactutility.R
import dev22.com.contactutility.data.RepositoryModule
import dev22.com.contactutility.di.CompositeDisableModule
import dev22.com.contactutility.main.di.DaggerMainComponent
import dev22.com.contactutility.main.di.MainPresenterModule
import io.reactivex.Single
import io.reactivex.SingleEmitter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {
    override fun showImportError() {
        TODO("not implemented")
    }

    companion object {
        const val REQUEST_PERMISSION_CONTACT: Int = 11
        const val REQUEST_FILE_PICKER: Int = 13
    }

    private var filePickerEmitter: SingleEmitter<String>? = null

    @Inject lateinit var presenter: MainPresenter

    override fun getLayoutContent(): Int = R.layout.activity_main

    override fun getPresenterForAutoDisposeRx(): MainPresenter? = presenter

    override fun initView() {
        btn_import.setOnClickListener { presenter.clickImport() }
        btn_backup.setOnClickListener { presenter.clickBackup() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_FILE_PICKER) {
            val filePath = if (data != null) data.data.path else ""
            filePickerEmitter?.onSuccess(filePath)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun openExport() {
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

    override fun chooseFile(): Single<String> {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/*"
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 13)
        return Single.create { emitter ->
            this.filePickerEmitter = emitter
        }
    }

    override fun requestContactAndReadExternalStoragePermission(): Single<PermissionRequestResult> = requestPermissionHelper(
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            permissionRequestCode = REQUEST_PERMISSION_CONTACT)

    override fun injectDI() {
        DaggerMainComponent
                .builder()
                .mainPresenterModule(MainPresenterModule(this))
                .compositeDisableModule(CompositeDisableModule())
                .repositoryModule(RepositoryModule())
                .build()
                .inject(this)
    }

    override fun onDestroy() {
        presenter.terminate()
        super.onDestroy()
    }
}
