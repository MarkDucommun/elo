package io.ducommun.elo.server

import io.ducommun.elo.new_game_orchestrator.GameOrchestrator
import io.ducommun.elo.new_game_orchestrator.domain.SavedGame
import io.ducommun.elo.new_game_orchestrator.domain.UnsavedGame
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameCreationController(
        val gameOrchestrator: GameOrchestrator
) {

    @PostMapping
    fun create(@RequestBody game: UnsavedGame): ResponseEntity<SavedGame> = game.saveAndReturn()

    fun UnsavedGame.saveAndReturn() =
            gameOrchestrator
                    .create(this)
                    .toResponseEntity()

    fun SavedGame.toResponseEntity() =
            ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this)
}