package fr.sgr.formation.voteapp.elections.services;

import lombok.Builder;
import lombok.Getter;

public class ElectionInvalideException extends Exception {
	/** Identifie l'erreur. */
	@Getter
	private ErreurElection erreur;

	@Builder
	public ElectionInvalideException(ErreurElection erreur, Throwable cause) {
		super(cause);

		this.erreur = erreur;
	}

	public ElectionInvalideException(ErreurElection erreur) {
		this.erreur = erreur;
	}

	public enum ErreurElection {
		ELECTION_OBLIGATOIRE("L'election est obligatoire pour effectuer l'opération."),
		GERANT_OBLIGATOIRE("Le gerant de l'election est obligatoire."),
		TITRE_OBLIGATOIRE("Le titre de l'elelction est obligatoire."),
		LOGIN_OBLIGATOIRE("Le login est obligatoire."),
		DESCRIPTION_OBLIGATOIRE("La description est obligatoire."),
		ELECTION_EXISTANT("Une election de même login existe déjà sur le système."),
		ELECTION_INEXISTANT("Cette election n'existe pas."),
		ELECTION_CLOTURE("Election déja cloturé."),
		DATE_CLOTURE_OBLIGATOIRE("Date de cloture obligatoire");
		@Getter
		public String message;

		private ErreurElection(String message) {
			this.message = message;
		}
	}
}
