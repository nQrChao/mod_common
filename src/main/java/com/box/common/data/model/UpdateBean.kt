package com.box.common.data.model

class UpdateBean {
    var version :String = ""
    var force = false
    var fileName = ""
    var fileUrl = ""
    var versionContent : MutableList<String> = mutableListOf()
}

