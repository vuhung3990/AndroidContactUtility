package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import dev22.com.contactutility.main.MainContract.View
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Created by dev22 on 7/4/17.
 */
class MainPresenterTest {
    lateinit var presenter: MainPresenter
    lateinit var view: View

    @Before
    fun setUp() {
        view = mock(View::class.java)
        presenter = MainPresenter(view, CompositeDisposable())
    }

    @Test
    fun handleRequestPermission() {
        // permission denied
        val denied = BaseActivity.PermissionRequestResult(11, BaseActivity.PermissionRequestResult.STATUS_PERMISSION_DENIED)
        presenter.handleRequestContactPermission(denied)
        verify(view).showWarning()

        // when permission granted
        val granted = BaseActivity.PermissionRequestResult(11, BaseActivity.PermissionRequestResult.STATUS_PERMISSION_GRANTED)
        presenter.handleRequestContactPermission(granted)
        verify(view).openImport()
    }
}