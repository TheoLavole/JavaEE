package fr.sgr.formation.voteapp.utilisateurs.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;
import lombok.extern.slf4j.Slf4j;

/**
 * Bean mettant à disposition les services permettant de valider les
 * informations d'un utilisateur.
 */
@Service
@Slf4j
public class ValidationUtilisateurServices {
	/**
	 * Vérifie qu'un utilisateur est valide.
	 * 
	 * @param utilisateur
	 *            Utilisateur à valider.
	 * @return true si l'utilisateur est valide, false si aucun utilisateur
	 *         n'est passé en paramètre.
	 * @throws UtilisateurInvalideException
	 *             Levée si l'utilisateur est invalide.
	 */
	public boolean validerUtilisateur(Utilisateur utilisateur) throws UtilisateurInvalideException {
		if (utilisateur == null) {
			return false;
		}

		validerLogin(utilisateur);
		validerNom(utilisateur);
		validerPrenom(utilisateur);
		validerMotDePasse(utilisateur);
		/** Validation des champs. */
		return true;
	}

	private void validerNom(Utilisateur utilisateur) throws UtilisateurInvalideException {
		if (StringUtils.isBlank(utilisateur.getNom())) {
			throw new UtilisateurInvalideException(ErreurUtilisateur.NOM_OBLIGATOIRE);
		}
	}

	private void validerPrenom(Utilisateur utilisateur) throws UtilisateurInvalideException {
		if (StringUtils.isBlank(utilisateur.getPrenom())) {
			throw new UtilisateurInvalideException(ErreurUtilisateur.PRENOM_OBLIGATOIRE);
		}
	}

	private void validerLogin(Utilisateur utilisateur) throws UtilisateurInvalideException {
		if (StringUtils.isBlank(utilisateur.getLogin())) {
			throw new UtilisateurInvalideException(ErreurUtilisateur.LOGIN_OBLIGATOIRE);
		}
	}

	private void validerMotDePasse(Utilisateur utilisateur) throws UtilisateurInvalideException {
		if (StringUtils.isBlank(utilisateur.getMotDePasse())) {
			throw new UtilisateurInvalideException(ErreurUtilisateur.MDP_OBLIGATOIRE);
		}
	}
}
