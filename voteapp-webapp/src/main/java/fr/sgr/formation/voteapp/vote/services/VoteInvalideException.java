package fr.sgr.formation.voteapp.vote.services;

import lombok.Getter;

public class VoteInvalideException extends Exception {
	/** Identifie l'erreur. */
	@Getter
	private ErreurVote erreur;

	public VoteInvalideException(ErreurVote erreur, Throwable cause) {
		super(cause);

		this.erreur = erreur;
	}

	public VoteInvalideException(ErreurVote erreur) {
		this.erreur = erreur;
	}

	public enum ErreurVote {
		ELECTION_OBLIGATOIRE("L'election est obligatoire pour effectuer l'opération."),
		ELECTEUR_OBLIGATOIRE("L'electeur est obligatoire."),
		VOTE_OBLIGATOIRE("La réponse du vote est obligatoire."),
		VOTE_DEJA_FAIT("Vous avez déja voté"),
		ELECTION_INEXISTANT("Cette election n'existe pas."),
		ELECTEUR_INEXISTANT("Cet electeur n'existe pas."),
		ELECTION_CLOTURE("Election déja cloturé.");

		@Getter
		public String message;

		private ErreurVote(String message) {
			this.message = message;
		}
	}
}
