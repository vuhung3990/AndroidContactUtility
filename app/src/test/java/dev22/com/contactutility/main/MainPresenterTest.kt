package dev22.com.contactutility.main

import dev22.com.contactutility.BaseActivity
import dev22.com.contactutility.data.Repository
import dev22.com.contactutility.main.MainContract.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
    lateinit var repo: Repository

    @Before
    fun setUp() {
        view = mock(View::class.java)
        repo = Repository()
        presenter = MainPresenter(view, CompositeDisposable(), repo)
    }

    @Test
    fun handleRequestPermission() {
        // permission denied
        val denied = BaseActivity.PermissionRequestResult(11, BaseActivity.PermissionRequestResult.STATUS_PERMISSION_DENIED)
        presenter.handleRequestContactPermission(denied, {})
        verify(view).showWarning()

        // when permission granted
        val granted = BaseActivity.PermissionRequestResult(11, BaseActivity.PermissionRequestResult.STATUS_PERMISSION_GRANTED)
        presenter.handleRequestContactPermission(granted, { view.openImport() })
        verify(view).openImport()

        presenter.handleRequestContactPermission(granted, { view.openExport() })
        verify(view).openExport()
    }

    @Test
    fun flowImport() {
        var flowImport = presenter.requestContactPermission()
                // handle request contact permission
                ?.map {
                    result ->
                    presenter.handleRequestContactPermission(result, view::openImport)
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
                    filePath ->
                    !filePath.isNullOrEmpty()
                }
                // check valid cvs file on computation thread
                ?.observeOn(Schedulers.computation())
                ?.map {
                    repo.cleanAndImportContact(it)
                }
                ?.test()
    }
}