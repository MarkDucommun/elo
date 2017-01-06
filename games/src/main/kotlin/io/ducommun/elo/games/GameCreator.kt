package io.ducommun.elo.games

import io.ducommun.elo.shared.AutomaticCreator
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class GameCreator(
        gameRepository: GameRepository,
        gameEntityTranslator: GameEntityTranslator,
        gameDomainTranslator: GameDomainTranslator,
        clock: Clock
) : AutomaticCreator<GameEntity, Game>(
        repository = gameRepository,
        entityTranslator = gameEntityTranslator,
        domainTranslator = gameDomainTranslator,
        clock = clock
)

//@Service
//open class GameCreator(
//        val leagueFinder: LeagueFinder,
//        val playerFinder: PlayerFinder,
//        val matchCreator: MatchCreator,
//        val eloRatingFinder: EloRatingFinder,
//        val eloRatingCreator: EloRatingCreator
//) {
//
//    open fun create(game: Game): SavedGame {
//        val league = leagueFinder.findByName(game.leagueName)
//        val playerScores = game.scores.map { it.toPlayerScore() }
//
//
//        val oldEloRatings = playerScores.associateBy({ it.player.id }, {
//            eloRatingFinder.findCurrentEloRatingForPlayerInLeague(
//                    playerId = it.player.id,
//                    leagueId = league.id
//            )
//        })
//
//        val matchList = playerScores.mapIndexed { i, playerScore ->
//
//            val playerCombos = playerScores.subList(i + 1, playerScores.size)
//
//            playerCombos.map { opponentScore: PlayerScore ->
//
//                val (winnerScore, loserScore) = if (playerScore.score > opponentScore.score) {
//                    Pair(playerScore, opponentScore)
//                } else {
//                    Pair(opponentScore, playerScore)
//                }
//
//                matchCreator.create(Match(
//                        leagueId = league.id,
//                        winningPlayerId = winnerScore.player.id,
//                        losingPlayerId = loserScore.player.id,
//                        winningScore = winnerScore.score,
//                        losingScore = loserScore.score
//                ))
//            }
//        }.flatten()
//
//        val eloList = matchList.flatMap {
//            val winnerOldEloRating = oldEloRatings[it.winningPlayerId]!!
//            val loserOldEloRating = oldEloRatings[it.losingPlayerId]!!
//
//            val winnerNewEloValue = FiveThirtyEightNflCalculator(
//                    currentRating = winnerOldEloRating.value,
//                    opponentRating = loserOldEloRating.value,
//                    playerScore = it.winningScore,
//                    opponentScore = it.losingScore,
//                    kFactor = league.kFactor
//            ).newRating()
//
//            val winnerNewEloRating = EloRating(
//                    leagueId = league.id,
//                    playerId = it.winningPlayerId,
//                    matchId = it.id,
//                    version = winnerOldEloRating.version + 1,
//                    value = winnerNewEloValue
//            )
//
//            val loserNewEloValue = FiveThirtyEightNflCalculator(
//                    currentRating = loserOldEloRating.value,
//                    opponentRating = winnerOldEloRating.value,
//                    playerScore = it.losingScore,
//                    opponentScore = it.winningScore,
//                    kFactor = league.kFactor
//            ).newRating()
//
//            val loserNewEloRating = EloRating(
//                    leagueId = league.id,
//                    playerId = it.losingPlayerId,
//                    matchId = it.id,
//                    version = loserOldEloRating.version + 1,
//                    value = loserNewEloValue
//            )
//
//            listOf(winnerNewEloRating, loserNewEloRating)
//        }
//
//        val groupedEloRatings = eloList.groupBy { it.playerId }
//
//        val finalEloRatings = groupedEloRatings.map {
//            val finalEloValue = it.value.map(EloRating::value).average().toInt()
//            it.value[0].copy(value = finalEloValue, matchId = 0)
//        }
//
//        val playerRatings = playerScores.map {
//            val newEloRating = eloRatingFinder.findCurrentEloRatingForPlayerInLeague(
//                    playerId = it.player.id,
//                    leagueId = league.id)
//
//            val oldEloRating = oldEloRatings[it.player.id]!!
//
//            PlayerRating(
//                    name = it.player.name,
//                    score = it.score,
//                    newRating = newEloRating.value,
//                    ratingChange = newEloRating.value - oldEloRating.value
//            )
//        }
//
//        println()
//
//        return SavedGame(leagueName = league.name, ratings = playerRatings)
//    }
//
//    open fun NamedPlayerScore.toPlayerScore(): PlayerScore {
//        val player = playerFinder.findByName(playerName)
//        return PlayerScore(player = player, score = score)
//    }
//}