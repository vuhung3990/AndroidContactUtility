package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by dev22 on 6/28/17.
 */
class MainPresenter @Inject constructor(val view: MainContract.View, val compositeDisposable: CompositeDisposable) : MainContract.Presenter {
    private var flowImport: Disposable? = null

    override fun clickImport() {
        flowImport = requestContactPermission()
                // handle request contact permission
                ?.map {
                    result ->
                    handleRequestContactPermission(result, view::openImport)
                }
                // if granted
                ?.filter {
                    onGranted ->
                    onGranted == true
                }
                // then open file picker
                ?.flatMapSingle {
                    view.chooseFile()
                }
                // if file path not empty or null
                ?.filter {
                    it ->
                    !it.isNullOrEmpty()
                }
                // check valid cvs file on computation thread
                ?.observeOn(Schedulers.computation())
                ?.subscribe()
    }

    fun requestContactPermission(): Maybe<BaseActivity.PermissionRequestResult>? {
        return view.requestContactPermission()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    (requestCode) ->
                    requestCode == MainActivity.REQUEST_PERMISSION_CONTACT
                }
    }

    /**
     * handle request contact permission
     * @param onGranted function will call if all permission granted
     * @param result to check permission status [BaseActivity.PermissionRequestResult.STATUS_PERMISSION_GRANTED], [BaseActivity.PermissionRequestResult.STATUS_PERMISSION_DENIED]
     */
    fun handleRequestContactPermission(result: BaseActivity.PermissionRequestResult, onGranted: () -> Unit): Boolean {
        val isGranted = result.status == BaseActivity.PermissionRequestResult.STATUS_PERMISSION_GRANTED
        if (isGranted) {
            onGranted()
        } else {
            view.showWarning();
        }
        return isGranted
    }

    override fun clickBackup() {
        view.requestContactPermission()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    (requestCode) ->
                    requestCode == MainActivity.REQUEST_PERMISSION_CONTACT
                }
                .map {
                    result ->
                    handleRequestContactPermission(result, { view.openExport() })
                }
                .subscribe()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

    override fun terminate() {
        flowImport?.dispose()
    }
}