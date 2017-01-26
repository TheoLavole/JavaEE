package fr.sgr.formation.voteapp.elections.ws;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException;
import fr.sgr.formation.voteapp.trace.services.TraceServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("utilisateurs/{login}/elections/liste")
@Slf4j
public class ElectionListe {

	@Autowired
	private ElectionsServices electionsServices;
	@Autowired
	private UtilisateursServices utilisateursServices;
	@Autowired
	private TraceServices traceServices;

	@RequestMapping(method = RequestMethod.GET)
	public String lire(@PathVariable("login") String login,
			@RequestParam(required = false) String recherche, @RequestParam(required = false) String gerant,
			@RequestParam(required = false) String cloture, @RequestParam(required=false) String nb, @RequestParam(required=false) String page) throws UtilisateurInvalideException, TraceInvalideException {
		try {
			if (utilisateursServices.rechercherParLogin(login) != null) {
				
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
				String res = "<table><tr><th>Login</th><th>Titre</th><th>Description</th><th>Login gérant</th><th>Date cloture</th></tr>";
				String[] res2 = electionsServices.findAllElection(login, recherche, gerant, cloture);
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
					Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(login);
					Trace trace = new Trace();
					trace.setAction("Liste élections");
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
				throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_INCONNU);
			}
		} catch (UtilisateurInvalideException e) {
			Utilisateur utilisateur = utilisateursServices.rechercherParLogin(login);
			if (utilisateur != null){
				Trace trace = new Trace();
				trace.setAction("Liste élections");
				trace.setDate(new Date());
				trace.setEmail(utilisateur.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription("");
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Liste élections");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription("");
				traceServices.creer(trace);
			}
			return null;
		}
	}
}
