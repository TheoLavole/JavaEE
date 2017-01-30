package fr.sgr.formation.voteapp.elections.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.elections.services.ElectionInvalideException;
import fr.sgr.formation.voteapp.elections.services.ElectionsServices;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("utilisateurs/{login}/elections/modifier")
@Slf4j

public class ElectionGestionREST {

	@Autowired
	private ElectionsServices electionsServices;

	/** 
	 * Méthode pour modifier une élection
	 * @param login Login de l'utilisateur à l'origine de l'action
	 * @param loginElection	Login de l'élection
	 * @param titre	Titre de l'élection que l'on souhaite modifier
	 * @param description Description que l'on souhaite modifier
	 * @param image URL de l'image que l'on souhaite modifier
	 * @throws ElectionInvalideException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void modifier(@PathVariable("login") String login, @RequestParam(required = true) String loginElection,
			@RequestParam(required = false) String titre, @RequestParam(required = false) String description,
			@RequestParam(required = false) String image)
					throws ElectionInvalideException {
		log.info("=====> Création ou modification de l'election de login {}: {}.", loginElection);
		electionsServices.modifier(loginElection, login, titre, description, image);

	}

}
