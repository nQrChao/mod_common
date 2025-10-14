package com.chaoji.other.nbnotification.utils.action.chained


class SequenceActionBuild : AbstractActionBuild<SequenceActionBuild>() {
    override val type: ActionBuildTypeEnum
        get() = ActionBuildTypeEnum.SEQUENCE
}