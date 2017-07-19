package dev22.com.contactutility

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.util.*


/**
 * base activity for automatic subscribe and unsubscribe automatically
 *
 * @param <T> presenter class implement base presenter
 */
abstract class BaseActivity<out T : BasePresenter> : AppCompatActivity() {
    private var presenter: T? = null

    override fun onPause() {
        super.onPause()
        presenter?.unsubscribe()
    }

    override fun onResume() {
        super.onResume()
        presenter?.subscribe()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDI()
        setContentView(getLayoutContent())
        presenter = getPresenterForAutoDisposeRx()
        initView()
    }

    /**
     * @return resource id of content layout
     */
    abstract fun getLayoutContent(): Int

    /**
     * @return presenter for auto subscribe and unsubscribe
     */
    abstract fun getPresenterForAutoDisposeRx(): T?

    /**
     * this func call in onCreate, after bindView
     */
    abstract fun initView()

    /**
     * inject dependency
     */
    abstract fun injectDI()

    private var permissions: Array<out String>? = null

    private var permissionRequestCode: Int? = null

    private var requestListener: SingleEmitter<PermissionRequestResult>? = null

    /**
     * request permission(s), **DON'T FORGET INCLUDE PERMISSION IN MANIFEST**

     * @param permission            permission to request (**Manifest.permission.***)
     * @param permissionRequestCode request code
     *
     *[RequestPermissionsResultCallback.onGranted] granted all permission
     *[RequestPermissionsResultCallback.onDenied] one or more denied by user
     */
    protected fun requestPermissionHelper(vararg permission: String, permissionRequestCode: Int): Single<PermissionRequestResult> {
        // if one or more of permission not granted
        // don't request every time because it's async and block user input
        return Single.create(
                { requestListener ->
                    run {
                        this.requestListener = requestListener;
                        if (!isGrantedAll(permission)) {
                            this.permissions = permission
                            this.permissionRequestCode = permissionRequestCode
                            ActivityCompat.requestPermissions(this,
                                    permission,
                                    permissionRequestCode)
                        } else {
                            requestListener.onSuccess(PermissionRequestResult(permissionRequestCode, PermissionRequestResult.STATUS_PERMISSION_GRANTED))
                        }
                    }
                }
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == this.permissionRequestCode) {
            if (Arrays.equals(permissions, this.permissions) && isGrantedAll(grantResults))
                requestListener?.onSuccess(PermissionRequestResult(requestCode, PermissionRequestResult.STATUS_PERMISSION_GRANTED))
            else
                requestListener?.onSuccess(PermissionRequestResult(requestCode, PermissionRequestResult.STATUS_PERMISSION_DENIED))
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * @param permissions array of permission to check (**Manifest.permission.***)
     * @return true: if all granted else otherwise
     */
    protected fun isGrantedAll(permissions: Array<out String>): Boolean {
        return permissions.none { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
    }

    /**
     * @param grantResults array grant result to check
     * @return true: if all granted
     */
    private fun isGrantedAll(grantResults: IntArray): Boolean {
        return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
    }

    /**
     * for deliver result of request permission
     * @param requestCode to identify which request
     * @param status [PermissionRequestResult.STATUS_PERMISSION_GRANTED], [PermissionRequestResult.STATUS_PERMISSION_DENIED]
     */
    data class PermissionRequestResult(val requestCode: Int, val status: Int) {
        companion object {
            /**
             * when user accepted all permissions
             */
            const val STATUS_PERMISSION_GRANTED = 0
            /**
             * when one or all permissions request denied
             */
            const val STATUS_PERMISSION_DENIED = -1
        }
    }

    /**
     * print error
     */
    fun Context.printError(msg: String) {
        Log.e(this.packageName, msg)
    }

    /**
     * print log for debug
     */
    fun Context.printLog(msg: String) {
        Log.d(this.packageName, msg)
    }

    /**
     * permission denied by user
     */
    fun showWarningPermissionDenied(view: View, msg: String) {
        Snackbar.make(view, msg, 10000)
                .setAction(R.string.grant_permission_manual, { openAppSetting() })
                .show()
    }

    /**
     * open app setting
     */
    private fun openAppSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}