package dev22.com.contactutility

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.*

/**
 * base activity for automatic subscribe and unsubscribe automatically
 *
 * @param <T> presenter class implement base presenter
 */
abstract class BaseActivity<T : BasePresenter> : AppCompatActivity() {
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
        initView();
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

    private var requestListener: RequestPermissionsResultCallback? = null

    private var permissions: Array<out String>? = null

    private var permissionRequestCode: Int? = null

    /**
     * request permission(s), **DON'T FORGET INCLUDE PERMISSION IN MANIFEST**

     * @param permission            permission to request (**Manifest.permission.***)
     * @param permissionRequestCode request code
     * @param requestListener       callback for request permission
     *
     *[RequestPermissionsResultCallback.onGranted] granted all permission
     *[RequestPermissionsResultCallback.onDenied] one or more denied by user
     */
    protected fun requestPermissionHelper(vararg permission: String, permissionRequestCode: Int, requestListener: RequestPermissionsResultCallback) {
        this.requestListener = requestListener

        // if one or more of permission not granted
        // don't request every time because it's async and block user input
        if (!isGrantedAll(permission)) {
            this.permissions = permission
            this.permissionRequestCode = permissionRequestCode

            ActivityCompat.requestPermissions(this,
                    permission,
                    permissionRequestCode)
        } else {
            this.requestListener?.onGranted(permissionRequestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == this.permissionRequestCode) {
            if (Arrays.equals(permissions, this.permissions) && isGrantedAll(grantResults))
                requestListener?.onGranted(requestCode)
            else
                requestListener?.onDenied(requestCode)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * @param permissions array of permission to check (**Manifest.permission.***)
     * @return true: if all granted else otherwise
     */
    protected fun isGrantedAll(permissions: Array<out String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    /**
     * @param grantResults array grant result to check
     * @return true: if all granted
     */
    private fun isGrantedAll(grantResults: IntArray): Boolean {
        for (gr in grantResults) {
            if (gr != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }

    /**
     * callback for request permission
     */
    interface RequestPermissionsResultCallback {
        /**
         * when user accepted all permissions
         *
         * @param permissionRequestCode to know which request
         */
        fun onGranted(permissionRequestCode: Int)

        /**
         * when one or all permissions request denied
         *
         * @param permissionRequestCode to know which request
         */
        fun onDenied(permissionRequestCode: Int)
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
}