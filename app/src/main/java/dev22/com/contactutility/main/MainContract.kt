package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import dev22.com.contactutility.BasePresenter
import io.reactivex.Single

/**
 * Created by dev22 on 6/28/17.
 */

class MainContract {
    interface View {
        /**
         * request contact and read external storage permission
         */
        fun requestContactAndReadExternalStoragePermission(): Single<BaseActivity.PermissionRequestResult>

        /**
         * show warning permission not grant
         * @see requestContactAndReadExternalStoragePermission
         */
        fun showWarning()

        /**
         * open contact activity
         */
        fun openImport()

        fun chooseFile(): Single<String>

        fun openExport()

        /**
         * show error when parse csv or import to db error
         */
        fun showImportError()
    }

    internal interface Presenter : BasePresenter {
        fun clickImport()
        fun clickBackup()
        fun terminate()
    }
}
