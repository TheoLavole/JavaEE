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

	private void validerTitre(Election election) throws ElectionInvalideException {
		if (StringUtils.isBlank(election.getTitre())) {
			throw new ElectionInvalideException(ErreurElection.TITRE_OBLIGATOIRE);
		}
	}

	private void validerDescription(Election election) throws ElectionInvalideException {
		if (StringUtils.isBlank(election.getDescription())) {
			throw new ElectionInvalideException(ErreurElection.DESCRIPTION_OBLIGATOIRE);
		}
	}

	private void validerLogin(Election election) throws ElectionInvalideException {
		if (StringUtils.isBlank(election.getLoginElection())) {
			throw new ElectionInvalideException(ErreurElection.LOGIN_OBLIGATOIRE);
		}
	}

	private void validerGerant(String loginGerant) throws ElectionInvalideException {
		if (!utilisateursServices.rechercherParLogin(loginGerant).getProfils()
				.contains(ProfilsUtilisateur.GERANT)) {
			throw new ElectionInvalideException(ErreurElection.GERANT_OBLIGATOIRE);
		}
	}

	public void validerBonGerant(Election election, String loginTest) throws ElectionInvalideException {
		if (!(utilisateursServices.rechercherParLogin(loginTest).getLogin()).equals(loginTest)) {
			throw new ElectionInvalideException(ErreurElection.GERANT_OBLIGATOIRE);
		}
	}

	public void validerUtilisateur(String loginTest) throws UtilisateurInvalideException {
		if (utilisateursServices.rechercherParLogin(loginTest) == null) {
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_OBLIGATOIRE);
		}
	}
}
