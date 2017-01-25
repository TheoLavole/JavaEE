package fr.sgr.formation.voteapp.gestion.services;

import lombok.Builder;
import lombok.Getter;

/**
 * Exception levée pour indiquer qu'une gestion est invalide.
 */
public class GestionInvalideException extends Exception {
	/** Identifie l'erreur. */
	@Getter
	private ErreurGestion erreur;

	@Builder
	public GestionInvalideException(ErreurGestion erreur, Throwable cause) {
		super(cause);
		this.erreur = erreur;
	}

	public GestionInvalideException(ErreurGestion erreur) {
		this.erreur = erreur;
	}

	public enum ErreurGestion {
		UTILISATEUR_OBLIGATOIRE("L'utilisateur est obligatoire pour effectuer l'opération."),
		NOM_OBLIGATOIRE("Le nom de l'utilisateur est obligatoire."),
		PRENOM_OBLIGATOIRE("Le prénom de l'utilisateur est obligatoire."),
		LOGIN_OBLIGATOIRE("Le login est obligatoire."),
		MDP_OBLIGATOIRE("Le mot de passe est obligatoire."),
		UTILISATEUR_EXISTANT("Un utilisateur de même login existe déjà sur le système."),
		PROFIL_OBLIGATOIRE("L'utilisateur n'a aucun profil."),
		MAIL_EXISTANT("Un autre utilisateur utilise déjà ce mail."),
		UTILISATEUR_INCONNU("Aucun utilisateur n'est connu avec ce login."),
		ADMINISTRATEUR_ONLY("Seul un administrateur peut effectuer cette action.");

		@Getter
		public String message;

		private ErreurGestion(String message) {
			this.message = message;
		}
	}
}
