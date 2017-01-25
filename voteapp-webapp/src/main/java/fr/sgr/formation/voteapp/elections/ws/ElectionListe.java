package fr.sgr.formation.voteapp.elections.ws;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.elections.modele.Election;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("utilisateurs/{login}/elections/liste")
@Slf4j
public class ElectionListe {

	@Autowired
	private ElectionsServices electionsServices;

	@RequestMapping(method = RequestMethod.GET)
	public Collection<Election> lire(@PathVariable("login") String login,
			@RequestParam(required = false) String recherche, @RequestParam(required = false) String gerant,
			@RequestParam(required = false) String cloture) throws UtilisateurInvalideException {
		log.info("=====> Récupération des election");
		return electionsServices.findAllElection(login, recherche, gerant, cloture);
	}
}
