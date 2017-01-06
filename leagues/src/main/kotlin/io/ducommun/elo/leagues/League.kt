package io.ducommun.elo.leagues

data class League(
        val id: Long = 0,
        val name: String,
        val description: String,
        val kFactor: Int,
        val defaultELO: Int
) {

    companion object {
        fun create(
                id: Long = 0,
                name: String = "",
                description: String = "",
                kFactor: Int = 20,
                defaultELO: Int = 1500
        ) = League(
                id,
                name,
                description,
                kFactor,
                defaultELO
        )
    }
}