package fr.sgr.formation.voteapp.vote.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import fr.sgr.formation.voteapp.vote.services.VoteInvalideException.ErreurVote;

@Service
public class ValidationVoteServices {
	@Autowired
	private UtilisateursServices utilisateursServices;

	@Autowired
	private ElectionsServices electionsServices;

	public boolean validerVote(String res, String loginElection, String loginElecteur)
			throws VoteInvalideException {
		if (res.equals(null)) {
			return false;
		}
		if (loginElection.equals(null)) {
			return false;
		}
		if (loginElecteur.equals(null)) {
			return false;
		}

		validerElection(loginElection);
		validerElecteur(loginElecteur);
		validerRESVote(res);

		/** Validation des champs. */
		return true;
	}

	private void validerElection(String loginElection) throws VoteInvalideException {
		if (electionsServices.rechercherParLogin(loginElection) == null) {
			throw new VoteInvalideException(ErreurVote.ELECTION_INEXISTANT);
		}
		if (electionsServices.rechercherParLogin(loginElection).getDateCloture() != null) {
			throw new VoteInvalideException(ErreurVote.ELECTION_CLOTURE);
		}
	}

	private void validerElecteur(String loginElecteur) throws VoteInvalideException {
		if (utilisateursServices.rechercherParLogin(loginElecteur) == null) {
			throw new VoteInvalideException(ErreurVote.ELECTEUR_INEXISTANT);
		}
	}

	private void validerRESVote(String vote) throws VoteInvalideException {
		if (StringUtils.isBlank(vote)) {
			throw new VoteInvalideException(ErreurVote.VOTE_OBLIGATOIRE);
		}
	}
}
