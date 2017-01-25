package fr.sgr.formation.voteapp.trace.services;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.sgr.formation.voteapp.gestion.services.GestionInvalideException.ErreurGestion;
import fr.sgr.formation.voteapp.gestion.ws.DescriptionErreur;
import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException.ErreurTrace;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import lombok.extern.slf4j.Slf4j;

/**
 * Sur la création et modification d'un utilisateur : - Vérification des champs
 * obligatoires - Vérification de la validité (longueur) des champs - Appeler un
 * service de notification inscrivant dans la log création ou modification de
 * l'utilisateur Sur la récupération d'un utilisateur Vérification de
 * l'existance de l'utilisateur Retourner l'utilisateur Sur la suppression d'un
 * utilisateur Vérification de l'existance de l'utilisateur Retourner
 * l'utilisateur Appeler un service de notification inscrivant dans la log la
 * suppression de l'utilisateur
 */
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
	 * Modifier un utilisateur sur le système avec les droits adminstrateur
	 * 
	 * @param utilisateur
	 *            Utilisateur à modifier.
	 * @return Utilisateur modifié.
	 * @throws UtilisateurInvalideException
	 *             Levée si l'utilisateur est invalide.
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

	@ExceptionHandler({TraceInvalideException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public DescriptionErreur traceErreur(TraceInvalideException exception) {
		return new DescriptionErreur(exception.getErreur().name(), exception.getErreur().getMessage());
	}
}
