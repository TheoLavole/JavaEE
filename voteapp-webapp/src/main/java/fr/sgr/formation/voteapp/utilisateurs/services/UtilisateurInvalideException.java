package fr.sgr.formation.voteapp.utilisateurs.services;

import lombok.Builder;
import lombok.Getter;

/**
 * Exception levée pour indiquer qu'un utilisateur est invalide.
 */
public class UtilisateurInvalideException extends Exception {
	/** Identifie l'erreur. */
	@Getter
	private ErreurUtilisateur erreur;

	@Builder
	public UtilisateurInvalideException(ErreurUtilisateur erreur, Throwable cause) {
		super(cause);
		this.erreur = erreur;
	}

	public UtilisateurInvalideException(ErreurUtilisateur erreur) {
		this.erreur = erreur;
	}

	public enum ErreurUtilisateur {
		UTILISATEUR_OBLIGATOIRE("L'utilisateur est obligatoire pour effectuer l'opération."),
		NOM_OBLIGATOIRE("Le nom de l'utilisateur est obligatoire."),
		PRENOM_OBLIGATOIRE("Le prénom de l'utilisateur est obligatoire."),
		LOGIN_OBLIGATOIRE("Le login est obligatoire."),
		MDP_OBLIGATOIRE("Le mot de passe est obligatoire."),
		UTILISATEUR_EXISTANT("Un utilisateur de même login existe déjà sur le système."),
		PROFIL_OBLIGATOIRE("L'utilisateur n'a aucun profil."),
		MAIL_EXISTANT("Un autre utilisateur utilise déjà ce mail."),
		ADRESSE_OBLIGATOIRE("Une adresse est nécessaire"),
		ADMIN_ONLY("Seul un administrateur peut faire cette action");

		@Getter
		public String message;

		private ErreurUtilisateur(String message) {
			this.message = message;
		}
	}
}
