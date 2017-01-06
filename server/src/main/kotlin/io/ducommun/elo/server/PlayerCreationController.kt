package io.ducommun.elo.server

import io.ducommun.elo.players.Player
import io.ducommun.elo.players.PlayerCreator
import io.ducommun.elo.players.PlayerFinder
import io.ducommun.elo.shared.Creator
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/players")
class PlayerCreationController(
        val playerCreator: Creator<Player>
) {

    @PostMapping
    fun create(@RequestBody player: Player) = player.saveAndReturn()

    private fun Player.saveAndReturn() =
            playerCreator
                    .create(this)
                    .toResponseEntity()

    private fun Player.toResponseEntity() =
            ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(APPLICATION_JSON)
                    .body(this)
}