package io.ducommun.elo.scores

import io.ducommun.elo.shared.AutomaticCreator
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class ScoreCreator(
        repository: ScoreRepository,
        entityTranslator: ScoreEntityTranslator,
        domainTranslator: ScoreDomainTranslator,
        clock: Clock
) : AutomaticCreator<ScoreEntity, Score>(
        repository = repository,
        entityTranslator = entityTranslator,
        domainTranslator = domainTranslator,
        clock = clock
)