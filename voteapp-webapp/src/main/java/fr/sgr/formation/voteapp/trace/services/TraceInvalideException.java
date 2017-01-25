package fr.sgr.formation.voteapp.trace.services;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Builder;
import lombok.Getter;

/**
 * Exception levée pour indiquer qu'une gestion est invalide.
 */
public class TraceInvalideException extends Exception {
	/** Identifie l'erreur. */
	@Getter
	private ErreurTrace erreur;

	@Builder
	public TraceInvalideException(ErreurTrace erreur, Throwable cause) {
		super(cause);
		this.erreur = erreur;
	}

	public TraceInvalideException(ErreurTrace erreur) {
		this.erreur = erreur;
	}

	private String email;
	private String action;
	@Temporal(TemporalType.DATE)
	private Date date;
	private String resultat;
	
	public enum ErreurTrace {
		TRACE_OBLIGATOIRE("La trace est obligatoire pour effectuer l'opération."),
		EMAIL_OBLIGATOIRE("L'email de l'utilisateur est obligatoire."),
		ACTION_OBLIGATOIRE("L'action effectuée est obligatoire."),
		DATE_OBLIGATOIRE("La date est obligatoire."),
		RESULTAT_OBLIGATOIRE("Le résultat de l'action est obligatoire.");

		@Getter
		public String message;

		private ErreurTrace(String message) {
			this.message = message;
		}
	}
}
