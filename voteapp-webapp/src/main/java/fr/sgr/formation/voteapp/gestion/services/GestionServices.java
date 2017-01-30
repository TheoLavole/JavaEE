package fr.sgr.formation.voteapp.gestion.services;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.sgr.formation.voteapp.gestion.services.GestionInvalideException.ErreurGestion;
import fr.sgr.formation.voteapp.gestion.ws.DescriptionErreur;
import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class GestionServices {
	/** Services de validation d'un utilisateur. */
	@Autowired
	private ValidationGestionServices validationServices;
	/** Services de notification des événements. */
	@Autowired
	private NotificationsServices notificationsServices;
	@Autowired
	private EntityManager entityManager;

	/**
	 * Modifier un utilisateur sur le système avec les droits adminstrateur
	 * 
	 * @param utilisateur
	 *            Utilisateur à modifier.
	 * @return Utilisateur modifié.
	 * @throws UtilisateurInvalideException
	 *             Levée si l'utilisateur est invalide.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Utilisateur modifierAdministrateur(Utilisateur utilisateur) throws GestionInvalideException {
		log.info("=====> Modification administrateur de l'utilisateur : {}.", utilisateur);

		if (utilisateur == null) {
			log.info("=====> Utilisateur obligatoire");
			throw new GestionInvalideException(ErreurGestion.UTILISATEUR_OBLIGATOIRE);
		}

		// On conserve forcément le même login
		Utilisateur utilisateurExistant = rechercherParLogin(utilisateur.getLogin());
		/** Validation de l'existance de l'utilisateur. */
		if (utilisateurExistant == null) {
			log.info("=====> Erreur, login inconnu {}.", utilisateur.getLogin());
			throw new GestionInvalideException(ErreurGestion.UTILISATEUR_INCONNU);
		} else {
			if (utilisateur.getAdresse() != null) {
				utilisateurExistant.setAdresse(utilisateur.getAdresse());
			}
			if (utilisateur.getDateDeNaissance() != null) {
				utilisateurExistant.setDateDeNaissance(utilisateur.getDateDeNaissance());
			}
			if (utilisateur.getEmail() != null) {
				/** Validation de l'unicité du mail. */
				if (rechercherMail(utilisateur.getEmail())) {
					if (!rechercherParLogin(utilisateur.getLogin()).getEmail().equals(utilisateur.getEmail())) {
						log.info("=====> Mail déjà utilisé par un autre utilisateur");
						throw new GestionInvalideException(ErreurGestion.MAIL_EXISTANT);
					}
				} else {
					utilisateurExistant.setEmail(utilisateur.getEmail());
				}
			}
			if (utilisateur.getMotDePasse() != null) {
				utilisateurExistant.setMotDePasse(utilisateur.getMotDePasse());
			}
			if (utilisateur.getNom() != null) {
				utilisateurExistant.setNom(utilisateur.getNom());
			}
			if (utilisateur.getPhoto() != null) {
				utilisateurExistant.setPhoto(utilisateur.getPhoto());
			}
			if (utilisateur.getPrenom() != null) {
				utilisateurExistant.setPrenom(utilisateur.getPrenom());
			}
			if (utilisateur.getProfils() != null) {
				List<ProfilsUtilisateur> profils = utilisateur.getProfils();
				for (ProfilsUtilisateur profil : profils) {
					if (!utilisateurExistant.getProfils().contains(profil)) {
						List<ProfilsUtilisateur> profilsExistant = utilisateurExistant.getProfils();
						profilsExistant.add(profil);
						utilisateurExistant.setProfils(profilsExistant);
					}
				}
			}
		}

		/**
		 * Validation de l'utilisateur: lève une exception si l'utilisateur est
		 * invalide.
		 */
		validationServices.validerUtilisateur(utilisateurExistant);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Modification de l'utilisateur: " + utilisateurExistant.toString());

		// /** Persistance de l'utilisateur. */
		// entityManager.persist(utilisateur);

		return utilisateurExistant;
	}

	/**
	 * Crée un nouvel utilisateur sur le système.
	 * 
	 * @param utilisateur
	 *            Utilisateur à créer.
	 * @return Utilisateur utilisateur créé.
	 * @throws UtilisateurInvalideException
	 *             Levée si l'utilisateur est invalide.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Utilisateur modifierUtilisateur(Utilisateur utilisateur) throws GestionInvalideException {
		log.info("=====> Modification utilisateur de l'utilisateur : {}.", utilisateur);

		if (utilisateur == null) {
			log.info("=====> Utilisateur obligatoire");
			throw new GestionInvalideException(ErreurGestion.UTILISATEUR_OBLIGATOIRE);
		}

		// On conserve forcément le même login
		Utilisateur utilisateurExistant = rechercherParLogin(utilisateur.getLogin());
		/** Validation de l'existance de l'utilisateur. */
		if (utilisateurExistant == null) {
			log.info("=====> Erreur, login inconnu {}.", utilisateur.getLogin());
			throw new GestionInvalideException(ErreurGestion.UTILISATEUR_INCONNU);
		} else {
			if (utilisateur.getAdresse() != null) {
				utilisateurExistant.setAdresse(utilisateur.getAdresse());
			}
			if (utilisateur.getDateDeNaissance() != null) {
				utilisateurExistant.setDateDeNaissance(utilisateur.getDateDeNaissance());
			}
			if (utilisateur.getEmail() != null) {
				/** Validation de l'unicité du mail. */
				if (rechercherMail(utilisateur.getEmail())) {
					if (!rechercherParLogin(utilisateur.getLogin()).getEmail().equals(utilisateur.getEmail())) {
						log.info("=====> Mail déjà utilisé par un autre utilisateur");
						throw new GestionInvalideException(ErreurGestion.MAIL_EXISTANT);
					}
				} else {
					utilisateurExistant.setEmail(utilisateur.getEmail());
				}
			}
			if (utilisateur.getMotDePasse() != null) {
				utilisateurExistant.setMotDePasse(utilisateur.getMotDePasse());
			}
			if (utilisateur.getNom() != null) {
				utilisateurExistant.setNom(utilisateur.getNom());
			}
			if (utilisateur.getPrenom() != null) {
				utilisateurExistant.setPrenom(utilisateur.getPrenom());
			}
		}

		/**
		 * Validation de l'utilisateur: lève une exception si l'utilisateur est
		 * invalide.
		 */
		validationServices.validerUtilisateur(utilisateurExistant);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Modification de l'utilisateur: " + utilisateurExistant.toString());

		// /** Persistance de l'utilisateur. */
		// entityManager.persist(utilisateur);

		return utilisateurExistant;
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
	 * Rechercher si un utilisateur possède déjà ce mail
	 * 
	 * @param mail
	 *            Mail de l'utilisateur.
	 * @return Retourne true si le mail est déjà utilisé, false sinon.
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
	 * Méthode pour afficher tous les utilisateurs
	 * @param login Login de l'utilisateur à l'origine de l'action	
	 * @param nom Nom recherché
	 * @param prenom	Prénom recherché
	 * @param ville	Ville recherchée
	 * @param profil Profil recherché
	 * @return Un tableau de String contenant les lignes retournées par la requête mises en forme sous un format tableau html
	 */
	public String[] afficherUtilisateurs(String nom, String prenom, String ville, String profil) {
		log.info("=====> Affichage des utilisateurs");
		String order = "";
		boolean hasSomething = false;
		if (nom != "") {
			order += " AND LCASE(U.NOM) LIKE '%" + nom.toLowerCase().trim() + "%'";
			hasSomething = true;
		}
		if (prenom != "" && hasSomething) {
			order += " AND LCASE(U.PRENOM) LIKE '%" + prenom.toLowerCase().trim() + "%'";
		} else {
			if (prenom != "") {
				order += " AND LCASE(U.PRENOM) LIKE '%" + prenom.toLowerCase().trim() + "%'";
				hasSomething = true;
			}
		}
		if (ville != "" && hasSomething) {
			order += " AND LCASE(V.NOM) LIKE '%" + ville.toLowerCase().trim() + "%'";
		} else {
			if (ville != "") {
				order += " AND LCASE(V.NOM) LIKE '%" + ville.toLowerCase().trim() + "%'";
				hasSomething = true;
			}
		}
		if (profil != "" && hasSomething) {
			order += " AND LCASE(P.PROFILS) LIKE '%" + profil.toLowerCase().trim() + "%'";
		} else {
			if (profil != "") {
				order += " AND LCASE(P.PROFILS) LIKE '%" + profil.toLowerCase().trim() + "%'";
				hasSomething = true;
			}
		}

		String order2 = "SELECT DISTINCT * FROM UTILISATEUR U, VILLE V, PROFILS_UTILISATEURS P WHERE U.LOGIN = P.LOGIN_UTILISATEUR AND U.VILLE_ID = V.ID";
		order = order2 + order;

		Query query = entityManager.createNativeQuery(order, Utilisateur.class);
		int nbRetour = query.getResultList().size();
		String[] res = new String[nbRetour];
		for (int i = 0; i < nbRetour; i++) {
			Utilisateur myTest = (Utilisateur) query.getResultList().get(i);
			res[i] = "<tr><td>" + myTest.getLogin() + "</td>" + "<td>" + myTest.getNom() + "</td>" + "<td>"
					+ myTest.getPrenom() + "</td>" + "<td>" + myTest.getDateDeNaissance() + "</td>" + "<td>"
					+ myTest.getEmail() + "</td>" + "<td><table>";
			for (ProfilsUtilisateur profils : myTest.getProfils()) {
				res[i] += "<tr><td>" + profils + "</tr></td>";
			}
			res[i] += "</table></td><td>" + myTest.getAdresse().getRue() + " "
					+ myTest.getAdresse().getVille().getCodePostal() + " " + myTest.getAdresse().getVille().getNom()
					+ "</td></tr>";
		}
		return res;
	}

	/**
	 * Méthode de recherche et d'affichage des traces 
	 * @param login Login de l'utilisateur à l'origine de la recherche
	 * @param nom Nom de l'utilisateur recherché
	 * @param email Email à l'origine de l'action recherché
	 * @param action Action recherchée
	 * @param dateDebut Date de début recherchée, quand l'on souhaite faire une recherche du type "Depuis telle date" ou "Entre le tant et tant, combien..."
	 * @param dateFin Date de fin, voir dateDebut
	 * @return String[] Le résultat de la recherche mis en forme en tableau html et contenant une ligne par résultat
	 * @throws ParseException
	 */
	public String[] afficherTraces(String email, String nom, String action, String dateDebut, String dateFin)
			throws ParseException {
		log.info("=====> Affichage des traces");
		String order = "";
		boolean hasSomething = false;
		if (email != "") {
			order += " AND LCASE(U.EMAIL) LIKE '%" + email.toLowerCase().trim() + "%'";
			hasSomething = true;
		}
		if (nom != "" && hasSomething) {
			order += " AND LCASE(U.NOM) LIKE '%" + nom.toLowerCase().trim() + "%'";
		} else {
			if (nom != "") {
				order += " AND LCASE(U.NOM) LIKE '%" + nom.toLowerCase().trim() + "%'";
				hasSomething = true;
			}
		}
		if (action != "" && hasSomething) {
			order += " AND LCASE(T.ACTION) LIKE '%" + action.toLowerCase().trim() + "%'";
		} else {
			if (action != "") {
				order += " AND LCASE(T.ACTION) LIKE '%" + action.toLowerCase().trim() + "%'";
				hasSomething = true;
			}
		}
		if (dateDebut != "" && hasSomething) {
			order += " AND T.DATE >= '" + dateDebut + "'";
		} else {
			if (dateDebut != "") {
				order += " AND T.DATE >= '" + dateDebut + "'";
				hasSomething = true;
			}
		}
		if (dateFin != "" && hasSomething) {
			order += " AND T.DATE <= '" + dateFin + "'";
		} else {
			if (dateFin != "") {
				order += " AND T.DATE <= '" + dateFin + "'";
				hasSomething = true;
			}
		}

		String order2 = "SELECT DISTINCT * FROM UTILISATEUR U, TRACE T WHERE U.EMAIL = T.EMAIL";
		order = order2 + order + " ORDER BY T.DATE";
		log.info(order);
		Query query = entityManager.createNativeQuery(order, Trace.class);
		int nbRetour = query.getResultList().size();
		String[] res = new String[nbRetour];
		for (int i = 0; i < nbRetour; i++) {
			Trace myTest = (Trace) query.getResultList().get(i);
			res[i] = "<tr><td>" + myTest.getEmail() + "</td>" + "<td>" + myTest.getAction() + "</td>" + "<td>"
					+ myTest.getResultat() + "</td>" + "<td>" + myTest.getDescription() + "</td><td>" + myTest.getDate()
					+ "</td></tr>";
		}
		return res;
	}

	@ExceptionHandler({ UtilisateurInvalideException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur gestionErreur(UtilisateurInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(), exception.getErreur().getMessage());
	}
}
