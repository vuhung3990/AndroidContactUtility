package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import dev22.com.contactutility.BasePresenter
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleObserver

/**
 * Created by dev22 on 6/28/17.
 */

class MainContract {
    interface View {
        /**
         * request contact permission
         */
        fun requestContactPermission(): Single<BaseActivity.PermissionRequestResult>

        /**
         * show warning permission not grant
         * @see requestContactPermission
         */
        fun showWarning()

        /**
         * open contact activity
         */
        fun openImport()

        fun chooseFile(): Single<String>

        fun openExport()
    }

    internal interface Presenter : BasePresenter {
        fun clickImport()
        fun clickBackup()
        fun terminate()
    }
}
