package dev22.com.contactutility.main

import dev22.com.contactutility.BasePresenter

/**
 * Created by dev22 on 6/28/17.
 */

class MainContract {
    interface View

    internal interface Presenter : BasePresenter {
        fun clickImport()
    }
}
