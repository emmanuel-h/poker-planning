package io.itsf.fr.models
import kotlinx.serialization.Serializable

@Serializable
data class UserData(val id: Int, val name: String, var vote: Int, val gameId: Int)
