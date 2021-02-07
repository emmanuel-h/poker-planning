package io.itsf.fr.models
import kotlinx.serialization.Serializable

@Serializable
data class UserData(var id: Int? = null, val name: String, var vote: Int? = null, val gameId: Int)
