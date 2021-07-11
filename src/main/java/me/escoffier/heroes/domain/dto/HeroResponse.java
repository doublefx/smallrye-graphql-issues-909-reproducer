package me.escoffier.heroes.domain.dto;

import lombok.Builder;
import lombok.ToString;
import me.escoffier.heroes.domain.entity.Hero;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Name;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Name("HeroResponse")
@Description("Definition of a HeroResponse")
@ToString
public class HeroResponse {
	@Id
	public Long    id;
	public String  name;
	public String  otherName;
	public Integer level;
	public String  picture;
	public String  powers;

	public static HeroResponse from(Hero hero) {
		return HeroResponse.builder()
		                   .id(hero.id)
		                   .name(hero.name)
		                   .otherName(hero.otherName)
		                   .level(hero.level)
		                   .picture(hero.picture)
		                   .powers(hero.powers)
		                   .build();
	}

	public static List<HeroResponse> from(List<Hero> actor) {
		return actor.stream().map(HeroResponse::from)
		            .collect(Collectors.toList());

	}
}
