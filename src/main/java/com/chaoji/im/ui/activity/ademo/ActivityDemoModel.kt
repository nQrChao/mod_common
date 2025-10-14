package com.chaoji.im.ui.activity.ademo

import com.chaoji.base.base.viewmodel.BaseViewModel
import com.chaoji.base.callback.databind.StringObservableField

class ActivityDemoModel : BaseViewModel(title = "demo") {
    var searchKey = StringObservableField()
}