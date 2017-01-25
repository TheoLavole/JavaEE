package fr.sgr.formation.voteapp.elections.ws;

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

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException;
import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException.ErreurElection;
import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException;
import fr.sgr.formation.voteapp.trace.services.TraceServices;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import fr.sgr.formation.voteapp.utilisateurs.ws.DescriptionErreur;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("utilisateurs/{login}/elections")
@Slf4j

public class ElectionRest {

	@Autowired
	private ElectionsServices electionsServices;
	@Autowired
	private UtilisateursServices utilisateursServices;
	@Autowired
	private TraceServices traceServices;

	@RequestMapping(value = "/{loginElection}", method = RequestMethod.PUT)
	public String creer(@PathVariable("login") String login, @PathVariable("loginElection") String loginElection,
			@RequestBody Election election) throws ElectionInvalideException, TraceInvalideException {
		try {
			if (utilisateursServices.rechercherParLogin(login) != null) {
				if (utilisateursServices.rechercherParLogin(login).getProfils().contains(ProfilsUtilisateur.GERANT)) {
					log.info("=====> Création ou modification de l'election de login {}: {}.", loginElection, election);
					election.setLoginElection(loginElection);
					electionsServices.creer(election, login);

					Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
					Trace trace = new Trace();
					trace.setAction("Création élection");
					trace.setDate(new Date());
					trace.setEmail(utilisateur2.getEmail());
					trace.setResultat("SUCCESS");
					trace.setDescription("");
					traceServices.creer(trace);

					return ("Election " + election.getLoginElection() + " créée.");
				} else {
					log.info("=====> Seul un gérant peut créer une élection");
					throw new ElectionInvalideException(ErreurElection.GERANT_ONLY);
				}
			} else {
				log.info("=====> Utilisateur inconnu");
				throw new ElectionInvalideException(ErreurElection.LOGIN_INCONNU);
			}
		} catch (ElectionInvalideException e) {
			Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
			if (utilisateur2 != null) {
				Trace trace = new Trace();
				trace.setAction("Création élection");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Création élection");
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

	@RequestMapping(value = "/{loginElection}", method = RequestMethod.DELETE)
	public String supprimer(@PathVariable("login") String login, @PathVariable("loginElection") String loginElection)
			throws ElectionInvalideException, TraceInvalideException {
		try {
			Election election = electionsServices.rechercherParLogin(loginElection);
			Utilisateur utilisateur = utilisateursServices.rechercherParLogin(login);
			if (utilisateur != null) {
				if (utilisateur.getProfils().contains(ProfilsUtilisateur.GERANT)) {
					if (election == null) {
						log.info("=====> Election inconnue");
						throw new ElectionInvalideException(ErreurElection.ELECTION_INEXISTANT);
					} else {
						log.info("=====> Suppression de l'election de login {}.", loginElection);
						electionsServices.supprimer(election);
						Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
						Trace trace = new Trace();
						trace.setAction("Suppression élection");
						trace.setDate(new Date());
						trace.setEmail(utilisateur2.getEmail());
						trace.setResultat("SUCCESS");
						trace.setDescription("");
						traceServices.creer(trace);

						return ("Election " + loginElection + " supprimée");
					}
				} else {
					log.info("=====> Utilisateur non gérant");
					throw new ElectionInvalideException(ErreurElection.GERANT_ONLY);
				}
			} else {
				log.info("=====> Utilisateur inconnu");
				throw new ElectionInvalideException(ErreurElection.LOGIN_INCONNU);
			}

		} catch (ElectionInvalideException e) {
			Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
			if (utilisateur2 != null) {
				Trace trace = new Trace();
				trace.setAction("Suppression élection");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Suppression élection");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			return e.getErreur().getMessage();
		}
	}

	@RequestMapping(value = "/{loginElection}", method = RequestMethod.GET)
	public Election lire(@PathVariable("login") String login, @PathVariable("loginElection") String loginElection) throws TraceInvalideException {
		try {
			Election election = electionsServices.rechercherParLogin(loginElection);
			Utilisateur utilisateur = utilisateursServices.rechercherParLogin(login);
			if (utilisateur != null) {
				if (utilisateur.getProfils().contains(ProfilsUtilisateur.GERANT)) {
					if (election != null) {
						log.info("=====> Récupération de l'election de login {}.", loginElection);
						return electionsServices.rechercherParLogin(loginElection);
					} else {
						log.info("=====> Election inconnue");
						throw new ElectionInvalideException(ErreurElection.ELECTION_INEXISTANT);
					}
				} else {
					log.info("=====> Utilisateur non gérant");
					throw new ElectionInvalideException(ErreurElection.GERANT_ONLY);
				}
			} else {
				log.info("=====> Utilisateur inconnu");
				throw new ElectionInvalideException(ErreurElection.LOGIN_INCONNU);
			}
		} catch (ElectionInvalideException e) {
			Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
			if (utilisateur2 != null) {
				Trace trace = new Trace();
				trace.setAction("Rechercher élection");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Rechercher élection");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			return null;
		}
	}

	@ExceptionHandler({ ElectionInvalideException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur gestionErreur(ElectionInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(), exception.getErreur().getMessage());
	}

}
