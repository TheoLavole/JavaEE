package fr.sgr.formation.voteapp.trace.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException.ErreurTrace;

@Service
public class ValidationTraceServices {

	/**
	 * Méthode pour valider une trace
	 * @param trace Trace que l'on souhaite valider
	 * @return Boolean Un booléen égal à true si la trace est valide, false sinon
	 * @throws TraceInvalideException
	 */
	public boolean validerTrace(Trace trace) throws TraceInvalideException {
		if (trace == null) {
			return false;
		}
		
		validerEmail(trace);
		validerAction(trace);
		validerDate(trace);
		validerResultat(trace);

		/** Validation des champs. */
		return true;
	}

	/**
	 * Méthode de vérification de l'email associé à la trace
	 * @param trace Trace que l'on souhaite valider
	 * @throws TraceInvalideException
	 */
	private void validerEmail(Trace trace) throws TraceInvalideException {
		if (StringUtils.isBlank(trace.getEmail())) {
			throw new TraceInvalideException(ErreurTrace.EMAIL_OBLIGATOIRE);
		}
	}

	/**
	 * Méthode de validation de l'action
	 * @param trace Trace que l'on souhaite valider
	 * @throws TraceInvalideException
	 */
	private void validerAction(Trace trace) throws TraceInvalideException {
		if (StringUtils.isBlank(trace.getAction())) {
			throw new TraceInvalideException(ErreurTrace.ACTION_OBLIGATOIRE);
		}
	}

	/**
	 * Méthode de validation de la date
	 * @param trace Trace que l'on souhaite valider
	 * @throws TraceInvalideException
	 */
	private void validerDate(Trace trace) throws TraceInvalideException {
		if (StringUtils.isBlank(trace.getDate().toString())) {
			throw new TraceInvalideException(ErreurTrace.DATE_OBLIGATOIRE);
		}
	}

	/**
	 * Méthode de validation du résultat de la trace
	 * @param trace Trace que l'on souhaite valider
	 * @throws TraceInvalideException
	 */
	private void validerResultat(Trace trace) throws TraceInvalideException {
		if (StringUtils.isBlank(trace.getResultat())) {
			throw new TraceInvalideException(ErreurTrace.RESULTAT_OBLIGATOIRE);
		}
	}
}
