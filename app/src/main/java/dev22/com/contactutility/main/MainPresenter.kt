package dev22.com.contactutility.main

import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by dev22 on 6/28/17.
 */
class MainPresenter @Inject constructor(val view: MainContract.View, val compositeDisposable: CompositeDisposable) : MainContract.Presenter {
    override fun clickImport() {

    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}