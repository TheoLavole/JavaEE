package fr.sgr.formation.voteapp.vote.modele;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "loginElection" })
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Vote {
	@EmbeddedId
	private VotePK voteID;

	private String vote;

}
