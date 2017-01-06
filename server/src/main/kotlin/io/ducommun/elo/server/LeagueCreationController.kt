package io.ducommun.elo.server

import io.ducommun.elo.leagues.League
import io.ducommun.elo.leagues.LeagueCreator
import io.ducommun.elo.shared.Creator
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leagues")
class LeagueCreationController(
        val leagueCreator: Creator<League>
) {

    @PostMapping
    fun create(@RequestBody newLeague: League): ResponseEntity<League> = newLeague.saveAndReturn()

    private fun League.saveAndReturn() =
            leagueCreator
                    .create(this)
                    .toResponseEntity()

    private fun League.toResponseEntity() = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this)
}