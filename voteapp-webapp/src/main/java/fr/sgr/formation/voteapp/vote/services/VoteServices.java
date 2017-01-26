package fr.sgr.formation.voteapp.vote.services;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.vote.modele.Vote;
import fr.sgr.formation.voteapp.vote.modele.VotePK;
import fr.sgr.formation.voteapp.vote.services.VoteInvalideException.ErreurVote;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class VoteServices {
	@Autowired
	private ValidationVoteServices validationServices;

	@Autowired
	private NotificationsServices notificationsServices;

	@Autowired
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRED)
	public Vote creer(String res, String loginElection, String loginElecteur) throws VoteInvalideException {
		log.info("=====> Création duvote : {}.", res);

		if (res.equals(null)) {
			throw new VoteInvalideException(ErreurVote.VOTE_OBLIGATOIRE);
		}
		if (loginElection.equals(null)) {
			throw new VoteInvalideException(ErreurVote.ELECTION_OBLIGATOIRE);
		}
		if (loginElecteur.equals(null)) {
			throw new VoteInvalideException(ErreurVote.ELECTEUR_OBLIGATOIRE);
		}
		if (rechercherParLogin(loginElection, loginElecteur) != null) {
			throw new VoteInvalideException(ErreurVote.VOTE_DEJA_FAIT);
		}

		/**
		 * Validation de l'election: lève une exception si l'election est
		 * invalide.
		 */
		validationServices.validerVote(res, loginElection, loginElecteur);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Création de l'election: " + res.toString());
		Vote vote = new Vote();
		VotePK votepk = new VotePK();
		votepk.setElecteurID(loginElecteur);
		votepk.setElectionID(loginElection);
		vote.setVoteID(votepk);
		vote.setVote(res);
		/** Persistance de l'election. */
		entityManager.persist(vote);
		return vote;
	}

	public Vote rechercherParLogin(String loginElection, String loginElecteur) {
		log.info("=====> Recherche de l'election de login {} {}.", loginElection, loginElecteur);

		if (StringUtils.isNotBlank(loginElection) && StringUtils.isNotBlank(loginElecteur)) {
			VotePK votePK = new VotePK();
			votePK.setElecteurID(loginElecteur);
			votePK.setElectionID(loginElection);
			return entityManager.find(Vote.class, votePK);
		}

		return null;
	}

}
