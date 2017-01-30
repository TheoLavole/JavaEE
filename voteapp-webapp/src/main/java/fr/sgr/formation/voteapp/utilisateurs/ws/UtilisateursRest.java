package fr.sgr.formation.voteapp.utilisateurs.ws;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException;
import fr.sgr.formation.voteapp.trace.services.TraceServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/utilisateurs/{login}")
@Slf4j
public class UtilisateursRest {
	@Autowired
	private UtilisateursServices utilisateursServices;
	@Autowired
	private TraceServices traceServices;

	/**
	 * Méthode de création d'un utilisateur
	 * 
	 * @param login
	 *            Login de l'utilisateur à l'origine de l'action
	 * @param utilisateur
	 *            Utilisateur que l'on souhaite créer
	 * @return String Une chaine de caractères précisant le résultat de cette
	 *         méthode
	 * @throws UtilisateurInvalideException
	 * @throws TraceInvalideException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public String creer(@PathVariable String login, @RequestBody Utilisateur utilisateur)
			throws UtilisateurInvalideException, TraceInvalideException {
		try {
			if (utilisateursServices.rechercherParLogin(login) != null) {
				if (utilisateursServices.rechercherParLogin(login).getProfils()
						.contains(ProfilsUtilisateur.ADMINISTRATEUR)) {
					Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);

					utilisateursServices.creer(utilisateur);
					Trace trace = new Trace();
					trace.setAction("Création utilisateur");
					trace.setDate(new Date());
					trace.setEmail(utilisateur2.getEmail());
					trace.setResultat("SUCCESS");
					trace.setDescription("");
					traceServices.creer(trace);

					return utilisateur.getLogin() + " créé";
				} else {
					log.info("=====> Erreur, seul un administrateur peut créer un utilisateur.");
					throw new UtilisateurInvalideException(ErreurUtilisateur.ADMIN_ONLY);
				}
			} else {
				log.info("=====> Login invalide");
				throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_INCONNU);
			}
		} catch (UtilisateurInvalideException e) {
			Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
			if (utilisateur2 != null) {
				Trace trace = new Trace();
				trace.setAction("Création utilisateur");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Création utilisateur");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			log.info("=====> {}", e.getErreur().getMessage());
			return e.getErreur().getMessage();
		}
	}

	/**
	 * Méthode de suppression d'un utilisateur
	 * @param login Login de l'utilisateur à l'origine de l'action
	 * @param loginADel Login de l'utilisateur à supprimer
	 * @return	String Résultat de l'action
	 * @throws UtilisateurInvalideException
	 * @throws TraceInvalideException
	 */
	@RequestMapping(value = "/{recherche}", method = RequestMethod.DELETE)
	public String supprimer(@PathVariable("login") String login, @PathVariable("recherche") String loginADel)
			throws UtilisateurInvalideException, TraceInvalideException {
		try {
			if (utilisateursServices.rechercherParLogin(login) != null) {
				if (utilisateursServices.rechercherParLogin(login).getProfils()
						.contains(ProfilsUtilisateur.ADMINISTRATEUR)) {
					log.info("=====> Suppression de l'utilisateur de login {}.", loginADel);
					Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
					String res = utilisateursServices.supprimer(loginADel);
					Trace trace = new Trace();
					trace.setAction("Création utilisateur");
					trace.setDate(new Date());
					trace.setEmail(utilisateur2.getEmail());
					trace.setResultat("SUCCESS");
					trace.setDescription("");
					traceServices.creer(trace);
					return res;
				} else {
					log.info("=====> Erreur, seul un administrateur peut supprimer un utilisateur.");
					throw new UtilisateurInvalideException(ErreurUtilisateur.ADMIN_ONLY);
				}
			} else {
				log.info("=====> Login invalide");
				throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_INCONNU);
			}
		} catch (UtilisateurInvalideException e) {
			Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
			if (utilisateur2 != null) {
				Trace trace = new Trace();
				trace.setAction("Suppression utilisateur");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Suppression utilisateur");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			log.info("=====> {}", e.getErreur().getMessage());
			return e.getErreur().getMessage();
		}
	}

	/**
	 * Méthode de visualisation d'un individu
	 * @param login Login de l'utilisateur qui souhaite visualiser son profil
	 * @return utilisateur Les informations du profil de l'utilisateur
	 * @throws UtilisateurInvalideException
	 * @throws TraceInvalideException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Utilisateur lire(@PathVariable("login") String login)
			throws UtilisateurInvalideException, TraceInvalideException {
		try {
			if (utilisateursServices.rechercherParLogin(login) != null) {
				log.info("=====> Récupération de l'utilisateur de login {}.", login);
				Utilisateur utilisateur = utilisateursServices.rechercherParLogin(login);
				Trace trace = new Trace();
				trace.setAction("Consultation utilisateur");
				trace.setDate(new Date());
				trace.setEmail(utilisateur.getEmail());
				trace.setResultat("SUCCESS");
				trace.setDescription("");
				traceServices.creer(trace);
				return utilisateur;
			} else {
				log.info("=====> Login invalide");
				throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_INCONNU);
			}
		} catch (UtilisateurInvalideException e) {
			Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
			if (utilisateur2 != null) {
				Trace trace = new Trace();
				trace.setAction("Consultation utilisateur");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Consultation utilisateur");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			return null;
		}
	}

	@ExceptionHandler({ UtilisateurInvalideException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur gestionErreur(UtilisateurInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(), exception.getErreur().getMessage());
	}
}
