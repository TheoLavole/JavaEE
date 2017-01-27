package fr.sgr.formation.voteapp.gestion.ws;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.gestion.services.GestionInvalideException;
import fr.sgr.formation.voteapp.gestion.services.GestionInvalideException.ErreurGestion;
import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException;
import fr.sgr.formation.voteapp.trace.services.TraceServices;
import fr.sgr.formation.voteapp.gestion.services.GestionServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/utilisateurs/{login}/gestion")
@Slf4j
public class GestionRest {
	@Autowired
	private GestionServices gestionServices;
	@Autowired
	private TraceServices traceServices;

	/**
	 * Méthode pour récupérer un mot de passe perdu
	 * @param login
	 * @return Qtring Une chaine de caractères
	 * @throws GestionInvalideException
	 * @throws TraceInvalideException
	 */
	@RequestMapping(value = "/mdp", method = RequestMethod.GET)
	public String nouveauMdp(@PathVariable String login) throws GestionInvalideException, TraceInvalideException {
		try {
			Utilisateur utilisateur = gestionServices.rechercherParLogin(login);
			if (utilisateur != null) {
				log.info("=====> Récupération de mdp pour l'utilisateur {}. Mail envoyé à l'adresse suivante {}.",
						login, utilisateur.getEmail());
				Trace trace = new Trace();
				trace.setAction("Renouvellement mot de passe");
				trace.setDate(new Date());
				trace.setEmail(utilisateur.getEmail());
				trace.setResultat("SUCCESS");
				traceServices.creer(trace);
				return ("Un nouveau mot de passe a été envoyé à l'adresse mail : "+utilisateur.getEmail());
			} else {
				log.info("=====> Récupération de mdp pour un utilisateur inconnu : {}.", login);
				throw new GestionInvalideException(ErreurGestion.UTILISATEUR_INCONNU);
			}
		} catch (GestionInvalideException e) {
			log.info("=====> {}",e.getErreur().getMessage());
			Trace trace = new Trace();
			trace.setAction("Renouvellement mot de passe");
			trace.setDate(new Date());
			trace.setEmail("MISSING");
			trace.setResultat("FAIL");
			trace.setDescription(e.getErreur().getMessage());
			traceServices.creer(trace);
			return (e.getErreur().getMessage());
		}
	}

	/**
	 * Modifier un utilisateur
	 * 
	 * @param login
	 * @param utilisateur
	 * @return
	 * @throws GestionInvalideException
	 * @throws TraceInvalideException
	 */
	@RequestMapping(value = "/modifier", method = RequestMethod.PUT)
	public String modifier(@PathVariable String login, @RequestBody Utilisateur utilisateur)
			throws GestionInvalideException, TraceInvalideException {
		try {
			if (gestionServices.rechercherParLogin(login) != null) {
				if (gestionServices.rechercherParLogin(login).getProfils()
						.contains(ProfilsUtilisateur.ADMINISTRATEUR)) {
					gestionServices.modifierAdministrateur(utilisateur);
					Utilisateur utilisateur2 = gestionServices.rechercherParLogin(login);
					Trace trace = new Trace();
					trace.setAction("Modification utilisateur");
					trace.setDate(new Date());
					trace.setEmail(utilisateur2.getEmail());
					trace.setResultat("SUCCESS");
					traceServices.creer(trace);
				} else {
					if (login.equals(utilisateur.getLogin())) {
						gestionServices.modifierUtilisateur(utilisateur);
						Trace trace = new Trace();
						trace.setAction("Modification utilisateur");
						trace.setDate(new Date());
						trace.setEmail(utilisateur.getEmail());
						trace.setResultat("SUCCESS");
						traceServices.creer(trace);
					} else {
						log.info("=====> Administrateur only");
						throw new GestionInvalideException(ErreurGestion.ADMINISTRATEUR_ONLY);
					}
				}
				return "<p>Utilisateur " + utilisateur.getLogin() + " modifié(e)</p>";
			} else {
				log.info("=====> Erreur : Utilisateur inconnu");
				throw new GestionInvalideException(ErreurGestion.UTILISATEUR_INCONNU);
			}
		} catch (GestionInvalideException e) {
			if (e.getErreur().getMessage().equals("Aucun utilisateur n'est connu avec ce login.")) {
				Trace trace = new Trace();
				trace.setAction("Modification utilisateur");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Utilisateur utilisateur2 = gestionServices.rechercherParLogin(login);
				Trace trace = new Trace();
				trace.setAction("Modification utilisateur");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			return e.getErreur().getMessage();
		}
	}

	/**
	 * Affichage des utilisateurs
	 * 
	 * @param login
	 * @param nom
	 * @param prenom
	 * @param ville
	 * @param profil
	 * @param nb
	 * @param page
	 * @return
	 * @throws GestionInvalideException
	 * @throws TraceInvalideException
	 */
	@RequestMapping(value = "/recherche", method = RequestMethod.GET)
	public String afficherListeUtilisateurs(@PathVariable("login") String login,
			@RequestParam(required = false) String nom, @RequestParam(required = false) String prenom,
			@RequestParam(required = false) String ville, @RequestParam(required = false) String profil,
			@RequestParam(required = false) String nb, @RequestParam(required = false) String page)
					throws GestionInvalideException, TraceInvalideException {
		try {
			if (gestionServices.rechercherParLogin(login) != null) {
				if (gestionServices.rechercherParLogin(login).getProfils()
						.contains(ProfilsUtilisateur.ADMINISTRATEUR)) {
					int nbAffi;
					int numPage;
					if (nb == null) {
						nbAffi = 15;
					} else {
						nbAffi = Integer.parseInt(nb);
					}
					if (page == null) {
						numPage = 1;
					} else {
						numPage = Integer.parseInt(page);
					}
					if (prenom == null) {
						prenom = "";
					}
					if (nom == null) {
						nom = "";
					}
					if (ville == null) {
						ville = "";
					}
					if (profil == null) {
						profil = "";
					}
					String res = "<table><tr><th>Login</th><th>Nom</th><th>Prénom</th><th>Date de naissance</th><th>Email</th><th>Profils</th><th>Adresse</th></tr>";
					String[] res2 = gestionServices.afficherUtilisateurs(nom, prenom, ville, profil);
					Double nbIter1 = (double) (res2.length / (nbAffi + 0.0));
					int nbIter = nbIter1.intValue();
					if (nbIter1 > nbIter) {
						nbIter++;
					}
					if (numPage <= nbIter && numPage > 0) {
						int stop = (nbAffi) * numPage;
						if (res2.length < stop) {
							stop = res2.length;
						}
						for (int i = (nbAffi) * (numPage - 1); i < stop; i++) {
							res += res2[i];
						}
						res += "</table>";
						res += "<br/>Page " + numPage + " sur " + nbIter + ".";
						res += "<br/>Affichage de " + (stop - (nbAffi) * (numPage - 1)) + "/" + res2.length
								+ " élément(s).<br/>";
						Utilisateur utilisateur2 = gestionServices.rechercherParLogin(login);
						Trace trace = new Trace();
						trace.setAction("Liste utilisateurs");
						trace.setDate(new Date());
						trace.setEmail(utilisateur2.getEmail());
						trace.setResultat("SUCCESS");
						traceServices.creer(trace);
					} else {
						if (numPage == 0) {
							res = "Erreur, il n'y a pas de page 0.";
						} else {
							if (nbIter == 0) {
								res = "Aucun résultat à votre recherche.";
							} else {
								res = "Erreur, vous cherchez à visualiser la page " + numPage
										+ " alors qu'il n'y a que " + nbIter + " page(s).";
							}
						}
					}
					return res;
				} else {
					log.info("=====> Administrateur only");
					throw new GestionInvalideException(ErreurGestion.ADMINISTRATEUR_ONLY);
				}
			} else {
				log.info("=====> Erreur : Utilisateur inconnu");
				throw new GestionInvalideException(ErreurGestion.UTILISATEUR_INCONNU);
			}
		} catch (GestionInvalideException e) {
			if (e.getErreur().getMessage().equals("Aucun utilisateur n'est connu avec ce login.")) {
				Trace trace = new Trace();
				trace.setAction("Liste utilisateurs");
				trace.setDate(new Date());
				trace.setEmail("NA");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Utilisateur utilisateur2 = gestionServices.rechercherParLogin(login);
				Trace trace = new Trace();
				trace.setAction("Liste utilisateurs");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			return e.getErreur().getMessage();
		}
	}

	/**
	 * Méthode pour afficher les traces
	 * @param login
	 * @param email
	 * @param nom
	 * @param action
	 * @param dateDebut
	 * @param dateFin
	 * @param nb
	 * @param page
	 * @return String Un tableau html de traces
	 * @throws GestionInvalideException
	 * @throws TraceInvalideException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/trace", method = RequestMethod.GET)
	public String afficherTraces(@PathVariable("login") String login, @RequestParam(required = false) String email,
			@RequestParam(required = false) String nom, @RequestParam(required = false) String action,
			@RequestParam(required = false) String dateDebut, @RequestParam(required = false) String dateFin,
			@RequestParam(required = false) String nb, @RequestParam(required = false) String page)
					throws GestionInvalideException, TraceInvalideException, ParseException {
		try {
			if (gestionServices.rechercherParLogin(login) != null) {
				if (gestionServices.rechercherParLogin(login).getProfils()
						.contains(ProfilsUtilisateur.ADMINISTRATEUR)) {
					int nbAffi;
					int numPage;
					if (nb == null) {
						nbAffi = 15;
					} else {
						nbAffi = Integer.parseInt(nb);
					}
					if (page == null) {
						numPage = 1;
					} else {
						numPage = Integer.parseInt(page);
					}
					if (email == null) {
						email = "";
					}
					if (nom == null) {
						nom = "";
					}
					if (action == null) {
						action = "";
					}
					if (dateDebut == null) {
						dateDebut = "";
					}
					if (dateFin == null) {
						dateFin = "";
					}
					String res = "<table><tr><th>Email</th><th>Action</th><th>Résultat</th><th>Description</th><th>Date</th></tr>";
					String[] res2 = gestionServices.afficherTraces(email, nom, action, dateDebut, dateFin);
					Double nbIter1 = (double) (res2.length / (nbAffi + 0.0));
					int nbIter = nbIter1.intValue();
					if (nbIter1 > nbIter) {
						nbIter++;
					}
					if (numPage <= nbIter && numPage > 0) {
						int stop = (nbAffi) * numPage;
						if (res2.length < stop) {
							stop = res2.length;
						}
						for (int i = (nbAffi) * (numPage - 1); i < stop; i++) {
							res += res2[i];
						}
						res += "</table>";
						res += "<br/>Page " + numPage + " sur " + nbIter + ".";
						res += "<br/>Affichage de " + (stop - (nbAffi) * (numPage - 1)) + "/" + res2.length
								+ " élément(s).<br/>";
						Utilisateur utilisateur2 = gestionServices.rechercherParLogin(login);
						Trace trace = new Trace();
						trace.setAction("Liste utilisateurs");
						trace.setDate(new Date());
						trace.setEmail(utilisateur2.getEmail());
						trace.setResultat("SUCCESS");
						traceServices.creer(trace);
					} else {
						if (numPage == 0) {
							res = "Erreur, il n'y a pas de page 0.";
						} else {
							if (nbIter == 0) {
								res = "Aucun résultat à votre recherche.";
							} else {
								res = "Erreur, vous cherchez à visualiser la page " + numPage
										+ " alors qu'il n'y a que " + nbIter + " page(s).";
							}
						}
					}
					return res;
				} else {
					log.info("=====> Administrateur only");
					throw new GestionInvalideException(ErreurGestion.ADMINISTRATEUR_ONLY);
				}
			} else {
				log.info("=====> Erreur : Utilisateur inconnu");
				throw new GestionInvalideException(ErreurGestion.UTILISATEUR_INCONNU);
			}
		} catch (GestionInvalideException e) {
			if (e.getErreur().getMessage().equals("Aucun utilisateur n'est connu avec ce login.")) {
				log.info("YOLO");
				log.info(e.getErreur().getMessage());
				Trace trace = new Trace();
				trace.setAction("Liste traces");
				trace.setDate(new Date());
				trace.setEmail("NA");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Utilisateur utilisateur2 = gestionServices.rechercherParLogin(login);
				Trace trace = new Trace();
				trace.setAction("Liste traces");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			return e.getErreur().getMessage();
		}
	}

	@ExceptionHandler({ GestionInvalideException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur gestionErreur(GestionInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(), exception.getErreur().getMessage());
	}
}
