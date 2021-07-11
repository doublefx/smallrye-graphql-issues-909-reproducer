package me.escoffier.heroes.boundary.graphql;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import lombok.extern.slf4j.Slf4j;
import me.escoffier.heroes.domain.dto.HeroResponse;
import me.escoffier.heroes.domain.entity.Hero;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import javax.enterprise.event.Observes;

import static java.time.Duration.ofSeconds;

@GraphQLApi
@Slf4j
public class HeroAPI {
	BroadcastProcessor<HeroResponse> heroProcessor = BroadcastProcessor.create();

	void onStart(@Observes StartupEvent ev) {
		emitRandomHero();
	}

	@Query("randomHero")
	@Description("Get a random Hero")
	public Uni<HeroResponse> randomHero() {
		return Hero.getRandomHero()
		           .onItem().transform(HeroResponse::from);
	}

	@Subscription("randomHeroEmitted")
	@Description("Get a random emitted Hero")
	public Multi<HeroResponse> randomHeroEmitted() {
		return heroProcessor;
	}

	private void emitRandomHero() {
		Multi.createFrom().ticks().every(ofSeconds(2))
		     .subscribe().with(subscription -> {
			     Hero.getRandomHero()
			         .subscribe().with(hero -> {
				         final var heroResponse = HeroResponse.from(hero);
				         log.info(heroResponse.toString());
				         heroProcessor.onNext(heroResponse);
			         });
		     });
	}
}
