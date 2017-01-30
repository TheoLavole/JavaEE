package fr.sgr.formation.voteapp.trace.services;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.sgr.formation.voteapp.gestion.ws.DescriptionErreur;
import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException.ErreurTrace;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS)
public class TraceServices {
	/** Services de validation d'un utilisateur. */
	@Autowired
	private ValidationTraceServices validationServices;
	/** Services de notification des événements. */
	@Autowired
	private NotificationsServices notificationsServices;

	@Autowired
	private EntityManager entityManager;

	/**
	 * Méthode de création de la trace et persistance
	 * @param trace Trace que l'on souhaite ajouter dans la base de données
	 * @return Trace Trace ajoutée
	 * @throws TraceInvalideException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Trace creer(Trace trace) throws TraceInvalideException {
		log.info("=====> Création de la trace : {}.", trace);

		if (trace == null) {
			log.info("=====> Trace obligatoire");
			throw new TraceInvalideException(ErreurTrace.TRACE_OBLIGATOIRE);
		}

		validationServices.validerTrace(trace);

		/** Notification de l'événement de création */
		notificationsServices.notifier("Création de la trace : " + trace.toString());

		/** Persistance de l'utilisateur. */
		entityManager.persist(trace);

		return trace;
	}

	@ExceptionHandler({ TraceInvalideException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur traceErreur(TraceInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(), exception.getErreur().getMessage());
	}
}
