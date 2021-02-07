package io.itsf.fr.models

import kotlinx.serialization.Serializable

@Serializable
data class GameData(val id: Int, val users: List<Int>)
