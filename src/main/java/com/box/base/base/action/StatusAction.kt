package com.box.base.base.action

import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.box.common.R
import com.box.common.ui.layout.StatusLayout

interface StatusAction {

    fun getStatusLayout(): StatusLayout?

    fun showLoading(@RawRes id: Int = R.raw.loading) {
        getStatusLayout()?.let {
            it.show()
            it.setAnimResource(id)
            it.setHint("")
            it.setOnRetryListener(null)
        }
    }
    fun showLoading_pro(@RawRes id: Int = R.raw.loading_pro) {
        getStatusLayout()?.let {
            it.show()
            it.setAnimResource(id)
            it.setHint("")
            it.setOnRetryListener(null)
        }
    }

    fun showLoading_2(@RawRes id: Int = R.raw.loading_around2) {
        getStatusLayout()?.let {
            it.show()
            it.setAnimResource(id)
            it.setHint("")
            it.setOnRetryListener(null)
        }
    }
    fun showLoading_3(@RawRes id: Int = R.raw.loading_around) {
        getStatusLayout()?.let {
            it.show()
            it.setAnimResource(id)
            it.setHint("")
            it.setOnRetryListener(null)
        }
    }

    fun showLoading_4(@RawRes id: Int = R.raw.loading1) {
        getStatusLayout()?.let {
            it.show()
            it.setAnimResource(id)
            it.setHint("")
            it.setOnRetryListener(null)
        }
    }

    fun showComplete() {
        getStatusLayout()?.let {
            if (!it.isShow()) {
                return
            }
            it.hide()
        }
    }

    fun showEmpty() {
        showLayout(R.drawable.status_empty_ic, R.string.status_layout_no_data, null)
    }

    fun showError(listener: StatusLayout.OnRetryListener?) {
        getStatusLayout()?.let {
            val manager: ConnectivityManager? = ContextCompat.getSystemService(it.context, ConnectivityManager::class.java)
            if (manager != null) {
                val info: NetworkInfo? = manager.activeNetworkInfo
                if (info == null || !info.isConnected) {
                    showLayout(R.drawable.status_network_ic, R.string.status_layout_error_network, listener)
                    return
                }
            }
            showLayout(R.drawable.status_error_ic, R.string.status_layout_error_request, listener)
        }
    }

    fun showLayout(@DrawableRes drawableId: Int, @StringRes stringId: Int, listener: StatusLayout.OnRetryListener?) {
        getStatusLayout()?.let {
            showLayout(ContextCompat.getDrawable(it.context, drawableId), it.context.getString(stringId), listener)
        }
    }

    fun showLayout(drawable: Drawable?, hint: CharSequence?, listener: StatusLayout.OnRetryListener?) {
        getStatusLayout()?.let {
            it.show()
            it.setIcon(drawable)
            it.setHint(hint)
            it.setOnRetryListener(listener)
        }
    }
}