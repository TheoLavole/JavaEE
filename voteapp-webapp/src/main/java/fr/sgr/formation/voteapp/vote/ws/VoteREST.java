package fr.sgr.formation.voteapp.vote.ws;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import fr.sgr.formation.voteapp.vote.services.VoteInvalideException;
import fr.sgr.formation.voteapp.vote.services.VoteServices;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("vote")
@Slf4j
public class VoteREST {

	@Autowired
	private VoteServices voteServices;
	@Autowired
	private UtilisateursServices utilisateursServices;
	@Autowired
	private ElectionsServices electionsServices;
	@Autowired
	private TraceServices traceServices;
	
	@RequestMapping(method = RequestMethod.PUT)
	public String cloture(@RequestParam(required = true) String loginElection,
			@RequestParam(required = true) String loginElecteur, @RequestParam(required = true) String vote)
					throws VoteInvalideException, TraceInvalideException {
		try {
			if (utilisateursServices.rechercherParLogin(loginElecteur) != null){
				if (electionsServices.rechercherParLogin(loginElection) != null){
					log.info("=====> Vote en cours {}: {}.", loginElection, loginElecteur);
					if (vote == null){ vote ="";}
					voteServices.creer(vote, loginElection, loginElecteur);
					Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(loginElecteur);
					Trace trace = new Trace();
					trace.setAction("Vote élection");
					trace.setDate(new Date());
					trace.setEmail(utilisateur2.getEmail());
					trace.setResultat("SUCCESS");
					trace.setDescription("");
					traceServices.creer(trace);
					return ("Le vote de l'utilisateur "+loginElecteur+" pour l'élection "+loginElection+" a été enregistré.");
				} else {
					throw new ElectionInvalideException(ErreurElection.ELECTION_INEXISTANT);
				}
			} else {
				throw new UtilisateurInvalideException(ErreurUtilisateur.UTILISATEUR_INCONNU);
			}
		} catch (ElectionInvalideException e){
			Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(loginElecteur);
			Trace trace = new Trace();
			trace.setAction("Vote élection");
			trace.setDate(new Date());
			trace.setEmail(utilisateur2.getEmail());
			trace.setResultat("FAIL");
			trace.setDescription("");
			traceServices.creer(trace);
			return e.getErreur().getMessage();
		} catch (UtilisateurInvalideException e){
			Trace trace = new Trace();
			trace.setAction("Vote élection");
			trace.setDate(new Date());
			trace.setEmail("MISSING");
			trace.setResultat("FAIL");
			trace.setDescription("");
			traceServices.creer(trace);
			return e.getErreur().getMessage();
		} catch (VoteInvalideException e){
			Utilisateur utilisateur2 = utilisateursServices.rechercherParLogin(loginElecteur);
			if (utilisateur2 == null){
				Trace trace = new Trace();
				trace.setAction("Vote élection");
				trace.setDate(new Date());
				trace.setEmail("MISSING");
				trace.setResultat("FAIL");
				trace.setDescription("");
				traceServices.creer(trace);
			} else {
				Trace trace = new Trace();
				trace.setAction("Vote élection");
				trace.setDate(new Date());
				trace.setEmail(utilisateur2.getEmail());
				trace.setResultat("FAIL");
				trace.setDescription("");
				traceServices.creer(trace);
			}
			return e.getErreur().getMessage();
		}
		
	}

}
