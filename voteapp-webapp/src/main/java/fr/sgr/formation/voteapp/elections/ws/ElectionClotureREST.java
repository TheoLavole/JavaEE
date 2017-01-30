package fr.sgr.formation.voteapp.elections.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
@RequestMapping("utilisateurs/{login}/elections/{loginElection}/cloture")
@Slf4j
public class ElectionClotureREST {

	@Autowired
	private ElectionsServices electionsServices;

	/**
	 * Méthode pour cloturer une élection
	 * @param login Login de l'utilisateur à l'origine de l'action
	 * @param loginElection	Login de l'élection
	 * @param date	Date de cloture de l'élection
	 * @throws ElectionInvalideException
	 * @throws ParseException
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void cloture(@PathVariable("login") String login, @PathVariable("loginElection") String loginElection,
			@RequestParam(required = true) String date)
					throws ElectionInvalideException, ParseException {
		log.info("=====> cloture de l'election de login {}: {}.", loginElection);
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		Date d = sdf.parse(date);
		electionsServices.cloturer(loginElection, d, login);
	}

}
