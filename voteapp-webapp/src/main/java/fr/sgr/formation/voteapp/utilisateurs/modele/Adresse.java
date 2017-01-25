package fr.sgr.formation.voteapp.utilisateurs.modele;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable
public class Adresse {
	private String rue;
	@ManyToOne
	private Ville ville;
}
