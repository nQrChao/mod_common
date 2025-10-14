package com.chaoji.im.data

import java.io.Serializable

class PictureElem  : Serializable {
     var sourcePath=""
     var sourcePicture:PictureInfo?=null
     var bigPicture:PictureInfo?=null
     var snapshotPicture:PictureInfo?=null
}
