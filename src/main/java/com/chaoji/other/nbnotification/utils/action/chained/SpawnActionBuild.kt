package com.chaoji.other.nbnotification.utils.action.chained

class SpawnActionBuild : AbstractActionBuild<SpawnActionBuild>() {
    override val type: ActionBuildTypeEnum
        get() = ActionBuildTypeEnum.SPAWN
}