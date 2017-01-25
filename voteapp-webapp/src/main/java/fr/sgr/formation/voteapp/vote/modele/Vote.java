package fr.sgr.formation.voteapp.vote.modele;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Vote {
	@Id
//	@ManyToOne
//	@JoinColumn(name = "gerant")
//	private Election election;
//
//	@Id
//	@ManyToOne
//	@JoinColumn(name = "gerant")
//	private Utilisateur electeur;

	private String nomVote;
}
