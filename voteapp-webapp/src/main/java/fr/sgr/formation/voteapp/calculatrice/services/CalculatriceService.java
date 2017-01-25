package fr.sgr.formation.voteapp.calculatrice.services;

import org.springframework.stereotype.Service;

import fr.sgr.formation.voteapp.calculatrice.modele.ResultatOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Services prenant en charge l'exécution des calculs.
 */
@Service
@Slf4j
public class CalculatriceService {
	public ResultatOperation ajouter(double operande1, double operande2) {
		log.info("Appel du service d'addition: avec les opérandes {} et {}.", operande1, operande2);

		// @formatter:off
		return ResultatOperation.builder()
				.typeOperation(" + ")
				.operande1(operande1)
				.operande2(operande2)
				.resultat(operande1 + operande2)
				.build();
		// @formatter:on
	}
}
