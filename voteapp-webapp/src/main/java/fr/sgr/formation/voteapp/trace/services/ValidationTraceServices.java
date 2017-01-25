package fr.sgr.formation.voteapp.trace.services;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import fr.sgr.formation.voteapp.gestion.services.GestionInvalideException.ErreurGestion;
import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException.ErreurTrace;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;

/**
 * Bean mettant à disposition les services permettant de valider les
 * informations d'un utilisateur.
 */
@Service
public class ValidationTraceServices {

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

	private void validerEmail(Trace trace) throws TraceInvalideException {
		if (StringUtils.isBlank(trace.getEmail())) {
			throw new TraceInvalideException(ErreurTrace.EMAIL_OBLIGATOIRE);
		}
	}

	private void validerAction(Trace trace) throws TraceInvalideException {
		if (StringUtils.isBlank(trace.getAction())) {
			throw new TraceInvalideException(ErreurTrace.ACTION_OBLIGATOIRE);
		}
	}

	private void validerDate(Trace trace) throws TraceInvalideException {
		if (StringUtils.isBlank(trace.getDate().toString())) {
			throw new TraceInvalideException(ErreurTrace.DATE_OBLIGATOIRE);
		}
	}

	private void validerResultat(Trace trace) throws TraceInvalideException {
		if (StringUtils.isBlank(trace.getResultat())) {
			throw new TraceInvalideException(ErreurTrace.RESULTAT_OBLIGATOIRE);
		}
	}
}
