package fr.sgr.formation.voteapp.elections.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException.ErreurElection;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;

@Service
public class ValidationElectionServices {

	@Autowired
	private UtilisateursServices utilisateursServices;

	/**
	 * Méthode pour valider une élection
	 * @param election
	 * @param loginGerant
	 * @return boolean true si l'élection et valide, false sinon
	 * @throws ElectionInvalideException
	 */
	public boolean validerElection(Election election, String loginGerant) throws ElectionInvalideException {
		if (election == null) {
			return false;
		}

		validerLogin(election);
		validerTitre(election);
		validerGerant(loginGerant);
		validerDescription(election);

		/** Validation des champs. */
		return true;
	}

	/**
	 * Méthode pour savoir si le titre d'une élection est valide
	 * @param election
	 * @throws ElectionInvalideException
	 */
	private void validerTitre(Election election) throws ElectionInvalideException {
		if (StringUtils.isBlank(election.getTitre())) {
			throw new ElectionInvalideException(ErreurElection.TITRE_OBLIGATOIRE);
		}
	}

	/** 
	 * Méthode pour savoir si une description est valide
	 * @param election
	 * @throws ElectionInvalideException
	 */
	private void validerDescription(Election election) throws ElectionInvalideException {
		if (StringUtils.isBlank(election.getDescription())) {
			throw new ElectionInvalideException(ErreurElection.DESCRIPTION_OBLIGATOIRE);
		}
	}

	/**
	 * Méthode pour savoir si un login est valide
	 * @param election
	 * @throws ElectionInvalideException
	 */
	private void validerLogin(Election election) throws ElectionInvalideException {
		if (StringUtils.isBlank(election.getLoginElection())) {
			throw new ElectionInvalideException(ErreurElection.LOGIN_OBLIGATOIRE);
		}
	}

	/**
	 * Méthode pour savoir si un gérant est valide
	 * @param loginGerant
	 * @throws ElectionInvalideException
	 */
	private void validerGerant(String loginGerant) throws ElectionInvalideException {
		if (!utilisateursServices.rechercherParLogin(loginGerant).getProfils()
				.contains(ProfilsUtilisateur.GERANT)) {
			throw new ElectionInvalideException(ErreurElection.GERANT_OBLIGATOIRE);
		}
	}

	/**
	 * Méthode pour savoir si le gérant saisi est celui qui a créé l'élection
	 * @param election
	 * @param loginTest
	 * @throws ElectionInvalideException
	 */
	public void validerBonGerant(Election election, String loginTest) throws ElectionInvalideException {
		if (!(utilisateursServices.rechercherParLogin(loginTest).getLogin()).equals(loginTest)) {
			throw new ElectionInvalideException(ErreurElection.GERANT_OBLIGATOIRE);
		}
	}

	/**
	 * Méthode pour valider un utilisateur
	 * @param loginTest
	 * @throws UtilisateurInvalideException
	 */
	public void validerUtilisateur(String loginTest) throws UtilisateurInvalideException {
		if (utilisateursServices.rechercherParLogin(loginTest) == null) {
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_OBLIGATOIRE);
		}
	}
}
