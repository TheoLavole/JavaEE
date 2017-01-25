package fr.sgr.formation.voteapp.elections.ws;

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
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import fr.sgr.formation.voteapp.utilisateurs.ws.DescriptionErreur;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("utilisateurs/{login}/elections/{loginElection}")
@Slf4j

public class ElectionRest {

	@Autowired
	private ElectionsServices electionsServices;

	@RequestMapping(method = RequestMethod.PUT)
	public void creer(@PathVariable("login") String login, @PathVariable("loginElection") String loginElection,
			@RequestBody Election election)
					throws ElectionInvalideException {
		log.info("=====> Création ou modification de l'election de login {}: {}.", loginElection, election);
		election.setLoginElection(loginElection);
		electionsServices.creer(election, login);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public void supprimer(@PathVariable("loginElection") String loginElection) {
		log.info("=====> Suppression de l'election de login {}.", loginElection);

	}

	@RequestMapping(method = RequestMethod.GET)
	public Election lire(@PathVariable("loginElection") String loginElection) {
		log.info("=====> Récupération de l'election de login {}.", loginElection);

		return electionsServices.rechercherParLogin(loginElection);
	}

	@ExceptionHandler({ ElectionInvalideException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur gestionErreur(ElectionInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(), exception.getErreur().getMessage());
	}

}
