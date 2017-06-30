package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by dev22 on 6/28/17.
 */
class MainPresenter @Inject constructor(val view: MainContract.View, val compositeDisposable: CompositeDisposable) : MainContract.Presenter {

    private var aa: Disposable? = null

    override fun clickImport() {
        view.requestContactPermission()
                .take(1)
                .filter({ requestResult -> requestResult.requestCode == MainActivity.REQUEST_PERMISSION_CONTACT })
                .doOnNext({ result ->
                    run {
                        if (result.status == BaseActivity.PermissionRequestResult.STATUS_PERMISSION_GRANTED) {

                        } else {
                            view.showWarning();
                        }
                    }
                })
                .subscribe()
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