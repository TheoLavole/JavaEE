package fr.sgr.formation.voteapp.utilisateurs.services;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.modele.Adresse;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Ville;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class UtilisateursServices {
	/** Services de validation d'un utilisateur. */
	@Autowired
	private ValidationUtilisateurServices validationServices;
	/** Services de notification des événements. */
	@Autowired
	private NotificationsServices notificationsServices;
	@Autowired
	private VilleService villeService;
	@Autowired
	private EntityManager entityManager;
	
	/**
	 * Crée un nouvel utilisateur sur le système.
	 * 
	 * @param utilisateur
	 *            Utilisateur à créer.
	 * @return Utilisateur créé.
	 * @throws UtilisateurInvalideException
	 *             Levée si l'utilisateur est invalide.
	 * @throws TraceInvalideException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Utilisateur creer(Utilisateur utilisateur) throws UtilisateurInvalideException {
		log.info("=====> Création de l'utilisateur : {}.", utilisateur);

		if (utilisateur == null) {
			log.info("=====> Utilisateur obligatoire");
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_OBLIGATOIRE);
		}

		if (utilisateur.getLogin() == null) {
			log.info("=====> Login obligatoire");
			throw new UtilisateurInvalideException(ErreurUtilisateur.LOGIN_OBLIGATOIRE);
		}

		if (utilisateur.getMotDePasse() == null) {
			log.info("=====> Mot de passe obligatoire");
			throw new UtilisateurInvalideException(ErreurUtilisateur.MDP_OBLIGATOIRE);
		}

		if (utilisateur.getPrenom() == null) {
			log.info("=====> Prénom manquant");
			throw new UtilisateurInvalideException(ErreurUtilisateur.PRENOM_OBLIGATOIRE);
		}

		if (utilisateur.getNom() == null) {
			log.info("=====> Nom manquant");
			throw new UtilisateurInvalideException(ErreurUtilisateur.NOM_OBLIGATOIRE);
		}

		if (utilisateur.getProfils() == null || utilisateur.getProfils().isEmpty()) {
			log.info("=====> Profil manquant");
			throw new UtilisateurInvalideException(ErreurUtilisateur.PROFIL_OBLIGATOIRE);
		}

		/** Validation de l'existance de l'utilisateur. */
		if (rechercherParLogin(utilisateur.getLogin()) != null) {
			log.info("=====> Login déjà utilisé");
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_EXISTANT);
		}

		/** Validation de l'unicité du mail. */
		if (rechercherMail(utilisateur.getEmail())) {
			log.info("=====> Mail déjà utilisé");
			throw new UtilisateurInvalideException(ErreurUtilisateur.MAIL_EXISTANT);
		}

		if (utilisateur.getAdresse() != null) {
			if (!rechercherVille(utilisateur.getAdresse().getVille())) {
				Ville ville = new Ville();
				ville.setCodePostal(utilisateur.getAdresse().getVille().getCodePostal());
				ville.setNom(utilisateur.getAdresse().getVille().getNom());
				villeService.creer(ville);
				Adresse adresse = new Adresse();
				adresse.setRue(utilisateur.getAdresse().getRue());
				adresse.setVille(ville);
				utilisateur.setAdresse(adresse);
			} else {
				Ville ville = new Ville();
				ville.setCodePostal(utilisateur.getAdresse().getVille().getCodePostal());
				ville.setNom(utilisateur.getAdresse().getVille().getNom());
				ville.setId(rechercherIdVille(utilisateur.getAdresse().getVille()));
				Adresse adresse = new Adresse();
				adresse.setRue(utilisateur.getAdresse().getRue());
				adresse.setVille(ville);
				utilisateur.setAdresse(adresse);
			}
		}

		/**
		 * Validation de l'utilisateur: lève une exception si l'utilisateur est
		 * invalide.
		 */
		validationServices.validerUtilisateur(utilisateur);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Création de l'utilisateur: " + utilisateur.toString());

		/** Persistance de l'utilisateur. */
		entityManager.persist(utilisateur);
		
		return utilisateur;
	}

	/**
	 * Retourne l'utilisateur identifié par le login.
	 * 
	 * @param login
	 *            Login identifiant l'utilisateur.
	 * @return Retourne l'utilisateur identifié par le login.
	 */
	public Utilisateur rechercherParLogin(String login) {
		log.info("=====> Recherche de l'utilisateur de login {}.", login);
		if (StringUtils.isNotBlank(login)) {
			return entityManager.find(Utilisateur.class, login);
		}
		return null;
	}

	/**
	 * Retourne 1 si le mail est déjà utilisé, 0 sinon.
	 * 
	 * @param mail
	 *            mail de l'utilisateur.
	 * @return Retourne 1 si le mail est déjà utilisé, 0 sinon.
	 */
	public boolean rechercherMail(String mail) {
		log.info("=====> Recherche du mail {}.", mail);
		if (StringUtils.isNotBlank(mail)) {
			int res = Integer.parseInt(
					entityManager.createNativeQuery("SELECT COUNT(*) FROM UTILISATEUR WHERE EMAIL='" + mail + "'")
							.getSingleResult().toString());
			log.info(res + "");
			if (res < 1) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retourne true si la ville est connue, false sinon.
	 * 
	 * @param ville
	 *            ville de l'utilisateur.
	 * @return Retourne true si la ville est connue, false sinon.
	 */
	public boolean rechercherVille(Ville ville) {
		log.info("=====> Recherche de la ville {}.", ville);
		if (ville != null) {
			int res = Integer
					.parseInt(
							entityManager
									.createNativeQuery("SELECT COUNT(*) FROM VILLE WHERE CODE_POSTAL='"
											+ ville.getCodePostal() + "' AND NOM='" + ville.getNom() + "'")
							.getSingleResult().toString());
			if (res < 1) {
				log.info("=====> La ville est inconnue.");
				return false;
			} else {
				log.info("=====> La ville est connue.");
				return true;
			}
		}
		return false;
	}

	/**
	 * Retourne l'id de la ville connue.
	 * 
	 * @param ville
	 *            ville de l'utilisateur.
	 * @return Retourne l'id de la ville connue.
	 */
	public long rechercherIdVille(Ville ville) {
		log.info("=====> Recherche de la ville {}.", ville);
		long res = Integer.parseInt(entityManager.createNativeQuery("SELECT ID FROM VILLE WHERE CODE_POSTAL='"
				+ ville.getCodePostal() + "' AND NOM='" + ville.getNom() + "'").getSingleResult().toString());
		return res;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String supprimer(String login) throws UtilisateurInvalideException {
		log.info("=====> Suppression de l'utilisateur de login : {}.", login);
		if (login == null) {
			log.info("=====> Utilisateur obligatoire");
			throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_OBLIGATOIRE);
		} else {
			Utilisateur utilisateur = rechercherParLogin(login);
			if (utilisateur != null) {
				log.info("=====> Utilisateur supprimé :{}",utilisateur);
				entityManager.remove(utilisateur);
				return "<p>Utilisateur "+login+" supprimé</p>";
			} else {
				log.info("=====> Aucun utilisateur {} a supprimer.", login);
				throw new UtilisateurInvalideException(ErreurUtilisateur.LOGIN_OBLIGATOIRE);
			}
		}
	}
}
