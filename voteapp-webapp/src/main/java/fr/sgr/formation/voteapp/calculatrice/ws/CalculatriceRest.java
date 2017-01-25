package fr.sgr.formation.voteapp.calculatrice.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.sgr.formation.voteapp.calculatrice.modele.ParametresOperation;
import fr.sgr.formation.voteapp.calculatrice.modele.ResultatOperation;
import fr.sgr.formation.voteapp.calculatrice.services.CalculatriceService;

@RestController
@RequestMapping("/calculatrice")
public class CalculatriceRest {
	@Autowired
	private CalculatriceService calculatrice;

	@RequestMapping("/add")
	// @RequestMapping(value = "/add", produces = {
	// MediaType.APPLICATION_JSON_VALUE})
	public ResultatOperation additionner(@RequestParam double operande1, @RequestParam double operande2) {
		return calculatrice.ajouter(operande1, operande2);
	}

	/**
	 * Exécution de l'addition et production d'une réponse HTML.
	 * 
	 * @param operande1
	 * @param operande2
	 * @return réponse HTML
	 */
	@RequestMapping(value = "/add", produces = { MediaType.TEXT_HTML_VALUE })
	public String additionnerVersHtml(@RequestParam double operande1, @RequestParam double operande2) {
		return calculatrice.ajouter(operande1, operande2).toHtml();
	}

	@RequestMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResultatOperation addition(@RequestBody ParametresOperation paramOperation) {
		if ((paramOperation == null) || (paramOperation.getOperande1() == null)
				|| (paramOperation.getOperande2() == null)) {
			throw new IllegalArgumentException("Les deux opérandes sont obligatoires.");
		}

		return calculatrice.ajouter(paramOperation.getOperande1(), paramOperation.getOperande2());
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Paramètres invalides.")
	public String traiterErreurParametres(IllegalArgumentException e) {
		return e.getMessage();
	}
}
