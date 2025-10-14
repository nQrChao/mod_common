package com.box.common.ui.activity.ademo

import com.box.base.base.viewmodel.BaseViewModel
import com.box.base.callback.databind.StringObservableField

class ActivityDemoModel : BaseViewModel(title = "demo") {
    var searchKey = StringObservableField()
}