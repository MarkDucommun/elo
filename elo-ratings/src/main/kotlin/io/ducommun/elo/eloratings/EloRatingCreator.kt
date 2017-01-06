package io.ducommun.elo.eloratings

import io.ducommun.elo.shared.AutomaticCreator
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class EloRatingCreator(
        repository: EloRatingRepository,
        domainTranslator: EloRatingDomainTranslator,
        entityTranslator: EloRatingEntityTranslator,
        clock: Clock
) : AutomaticCreator<EloRatingEntity, EloRating>(
        repository = repository,
        domainTranslator = domainTranslator,
        entityTranslator = entityTranslator,
        clock = clock
)