package fr.sgr.formation.voteapp.elections.services;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException.ErreurElection;
import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class ElectionsServices {

	@Autowired
	private ValidationElectionServices validationServices;
	@Autowired
	private UtilisateursServices utilisateursServices;
	@Autowired
	private NotificationsServices notificationsServices;

	@Autowired
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRED)
	public Election creer(Election election, String loginGerant) throws ElectionInvalideException {
		log.info("=====> Création de l'election : {}.", election);

		if (election == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
		}

		if (rechercherParLogin(election.getLoginElection()) != null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_EXISTANT);
		}

		Utilisateur gerant = utilisateursServices.rechercherParLogin(loginGerant);
		election.setGerant(gerant);

		/**
		 * Validation de l'election: lève une exception si l'election est
		 * invalide.
		 */
		validationServices.validerElection(election, loginGerant);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Création de l'election: " + election.toString());

		/** Persistance de l'election. */
		entityManager.persist(election);

		return election;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Election cloturer(String loginElection, Date dateCloture, String loginGerant)
			throws ElectionInvalideException {
		log.info("=====> Cloture de l'election : {}.", loginElection);
		if (loginElection == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
		}
		if (rechercherParLogin(loginElection) == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_INEXISTANT);
		}
		if (dateCloture == null) {
			throw new ElectionInvalideException(ErreurElection.DATE_CLOTURE_OBLIGATOIRE);
		}
		if (rechercherParLogin(loginElection).getDateCloture() != null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_CLOTURE);
		}
		validationServices.validerBonGerant(rechercherParLogin(loginElection), loginGerant);
		rechercherParLogin(loginElection).setDateCloture(dateCloture);
		return rechercherParLogin(loginElection);
	}

	public String[] findAllElection(String loginUtilisateur, String recherche, String gerant, String cloture)
			throws UtilisateurInvalideException {
		log.info("=====> liste des élections");

		String order = "";
		boolean hasSomething = false;
		if (recherche != null) {
			order += " WHERE LCASE(TITRE) LIKE '%" + recherche.toLowerCase().trim() + "%'";
			hasSomething = true;
		}
		if (gerant != null && hasSomething) {
			order += " AND LCASE(GERANT) LIKE '%" + gerant.toLowerCase().trim() + "%'";
		} else {
			if (gerant != null) {
				order += " WHERE LCASE(GERANT) LIKE '%" + gerant.toLowerCase().trim() + "%'";
				hasSomething = true;
			}
		}
		if (cloture != null && hasSomething) {
			if (cloture.equals("oui")) {
				order += " AND DATE_CLOTURE IS NOT NULL";
			} else if (cloture.equals("non")) {
				order += " AND DATE_CLOTURE IS NULL";
			}
		} else {
			if (cloture != null) {
				if (cloture.equals("oui")) {
					order += " WHERE DATE_CLOTURE IS NOT NULL";
				} else if (cloture.equals("non")) {
					order += " WHERE DATE_CLOTURE IS NULL";
				}
			}
		}

		String order2 = "SELECT * FROM ELECTION";
		order = order2 + order;

		log.info("=====> {}", order);
		Query query = entityManager.createNativeQuery(order, Election.class);

		int nbRetour = query.getResultList().size();
		String[] res = new String[nbRetour];
		for (int i = 0; i < nbRetour; i++) {
			Election myTest = (Election) query.getResultList().get(i);
			res[i] = "<tr><td>" + myTest.getLoginElection() + "</td>" + "<td>" + myTest.getTitre() + "</td>" + "<td>"
					+ myTest.getDescription() + "</td>" + "<td>" + myTest.getGerant().getLogin() + "</td><td>"
					+ myTest.getDateCloture() + "</td></tr>";
		}
		return res;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Election modifier(String loginElection, String loginGerant, String titre, String description, String image)
			throws ElectionInvalideException {
		log.info("=====> modification de l'election : {}.", loginElection);
		if (loginElection == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
		}
		if (rechercherParLogin(loginElection) == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_INEXISTANT);
		}
		validationServices.validerBonGerant(rechercherParLogin(loginElection), loginGerant);
		if (!(titre == null)) {
			rechercherParLogin(loginElection).setTitre(titre);
		}
		if (!(image == null)) {
			rechercherParLogin(loginElection).setImage(image);
		}
		if (!(description == null)) {
			rechercherParLogin(loginElection).setDescription(description);
		}
		return rechercherParLogin(loginElection);
	}

	public Election rechercherParLogin(String loginElection) {
		log.info("=====> Recherche de l'election de login {}.", loginElection);

		if (StringUtils.isNotBlank(loginElection)) {
			return entityManager.find(Election.class, loginElection);
		}

		return null;
	}

	/**
	 * 
	 * @param loginElection
	 * @param modalite
	 * @return
	 * @throws ElectionInvalideException
	 */
	public int resultat(String loginElection, String modalite) throws ElectionInvalideException {
		if (loginElection.equals(null)) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
		}
		if (modalite.equals(null)) {
			return 0;
		}
		if (rechercherParLogin(loginElection) == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_INEXISTANT);
		}

		Query query = null;
		if (modalite.toLowerCase().equals(("oui").toLowerCase())) {
			query = entityManager.createNativeQuery(
					"SELECT count(*) FROM Vote where id_election= :1 and vote='oui'");
			query.setParameter("1", loginElection);
		} else if (modalite.toLowerCase().equals(("non").toLowerCase())) {
			query = entityManager.createNativeQuery(
					"SELECT count(*) FROM Vote where id_election= :1 and vote='non'");
			query.setParameter("1", loginElection);
		} else {
			query = entityManager.createNativeQuery(
					"SELECT count(*) FROM Vote where id_election= :1 ");
			query.setParameter("1", loginElection);
		}
		return Integer.parseInt(query.getSingleResult().toString());
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void supprimer(Election election) throws ElectionInvalideException {
		log.info("=====> Supression de l'election : {}.", election);
		if (election == null) {
			throw new ElectionInvalideException(ErreurElection.ELECTION_OBLIGATOIRE);
		} else {
			entityManager.remove(election);
		}
	}
}
