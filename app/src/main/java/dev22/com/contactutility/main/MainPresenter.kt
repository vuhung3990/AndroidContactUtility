package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by dev22 on 6/28/17.
 */
class MainPresenter @Inject constructor(val view: MainContract.View, val compositeDisposable: CompositeDisposable) : MainContract.Presenter {

    private var aa: Disposable? = null

    override fun clickImport() {
        view.requestContactPermission()
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter({ requestResult -> requestResult.requestCode == MainActivity.REQUEST_PERMISSION_CONTACT })
                .doOnNext({ result -> handleRequestContactPermission(result) })
                .subscribe()
    }

    /**
     * handle request contact permission
     */
    fun handleRequestContactPermission(result: BaseActivity.PermissionRequestResult) {
        if (result.status == BaseActivity.PermissionRequestResult.STATUS_PERMISSION_GRANTED) {
            view.openImport();
        } else {
            view.showWarning();
        }
    }

    override fun clickBackup() {
        print("$aa dafug")
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}