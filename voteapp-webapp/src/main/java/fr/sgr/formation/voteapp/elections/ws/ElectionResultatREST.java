package fr.sgr.formation.voteapp.elections.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException;
import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException.ErreurElection;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
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
@RequestMapping("utilisateurs/{login}/elections/resultat")
@Slf4j
public class ElectionResultatREST {

	@Autowired
	private ElectionsServices electionsServices;
	@Autowired
	private UtilisateursServices utilisateursServices;
	@Autowired
	private TraceServices traceServices;

	@RequestMapping(method = RequestMethod.GET)
	public String lire(@PathVariable("login") String login, @RequestParam(required = false) String election)
			throws ElectionInvalideException, TraceInvalideException, UtilisateurInvalideException {
		try {
			Utilisateur utilisateur = utilisateursServices.rechercherParLogin(login);
			if (utilisateur != null) {
				if (utilisateur.getProfils().contains(ProfilsUtilisateur.GERANT)) {
					if (electionsServices.rechercherParLogin(election) != null) {
						if (electionsServices.rechercherParLogin(election).getGerant().getLogin() == login) {
							log.info("=====> Récupération des election");
							HashMap<String, Integer> resultat = new HashMap<String, Integer>();
							resultat.put("oui", electionsServices.resultat(election, "oui"));
							resultat.put("non", electionsServices.resultat(election, "non"));
							resultat.put("blanc",
									(electionsServices.resultat(election, "o")
											- (electionsServices.resultat(election, "non")
													+ electionsServices.resultat(election, "oui"))));
							resultat.put("total", (electionsServices.resultat(election, "o")));

							Trace trace = new Trace();
							trace.setAction("Résultat élection");
							trace.setDate(new Date());
							trace.setEmail(utilisateur.getEmail());
							trace.setResultat("SUCCESS");
							traceServices.creer(trace);

							String res = "<table><th>Modalité du vote</th><th>Nombre de voix</th>";
							Set<String> modalites = resultat.keySet();
							for (String modalite : modalites) {
								res += "<tr><td>" + modalite + "</td><td>" + resultat.get(modalite) + "</td></tr>";
							}
							res += "</table>";
							return res;
						} else {
							throw new ElectionInvalideException(ErreurElection.GERANT_RESULTAT);
						}
					} else {
						throw new ElectionInvalideException(ErreurElection.ELECTION_INEXISTANT);
					}
				} else {
					throw new ElectionInvalideException(ErreurElection.GERANT_ONLY);
				}
			} else {
				throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_INCONNU);
			}
		} catch (ElectionInvalideException e) {
			Utilisateur utilisateur = utilisateursServices.rechercherParLogin(login);
			if (utilisateur != null) {
				Trace trace = new Trace();
				trace.setAction("Résultat élection");
				trace.setDate(new Date());
				trace.setEmail(utilisateur.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Résultat élection");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription(e.getErreur().getMessage());
				traceServices.creer(trace);
			}
			return e.getErreur().getMessage();
		}
	}
}
