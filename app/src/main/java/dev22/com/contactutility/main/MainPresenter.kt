package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by dev22 on 6/28/17.
 */
class MainPresenter @Inject constructor(val view: MainContract.View, val compositeDisposable: CompositeDisposable) : MainContract.Presenter {
    override fun clickImport() {
        view.requestContactPermission()
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter({ (requestCode) -> requestCode == MainActivity.REQUEST_PERMISSION_CONTACT })
                .doOnNext({ result -> handleRequestContactPermission(result, { view.openImport() }) })
                .subscribe()
    }

    /**
     * handle request contact permission
     * @param onGranted function will call if all permission granted
     * @param result to check permission status [BaseActivity.PermissionRequestResult.STATUS_PERMISSION_GRANTED], [BaseActivity.PermissionRequestResult.STATUS_PERMISSION_DENIED]
     */
    fun handleRequestContactPermission(result: BaseActivity.PermissionRequestResult, onGranted: () -> Unit) {
        if (result.status == BaseActivity.PermissionRequestResult.STATUS_PERMISSION_GRANTED) {
            onGranted()
        } else {
            view.showWarning();
        }
    }

    override fun clickBackup() {
        view.requestContactPermission()
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter({ (requestCode) -> requestCode == MainActivity.REQUEST_PERMISSION_CONTACT })
                .doOnNext({ result -> handleRequestContactPermission(result, { view.openExport() }) })
                .subscribe()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}