package fr.sgr.formation.voteapp.utilisateurs.services;

import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fr.sgr.formation.voteapp.notifications.services.NotificationsServices;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;

@RunWith(MockitoJUnitRunner.class)
public class UtilisateursServicesTest {
	/** Classe des services à tester. */
	@InjectMocks
	private UtilisateursServices services = new UtilisateursServices();

	@Mock
	private ValidationUtilisateurServices validationServices;
	@Mock
	private NotificationsServices notificationsServices;
	@Mock
	private EntityManager entityManager;

	@Test
	public void creerUtilisateurNul() throws TraceInvalideException {
		try {
			/** When: Lorsqu'on appel le service de création */
			services.creer(null);

			fail("Une exception devrait être levée.");
		} catch (UtilisateurInvalideException e) {
			/** Then: Alors une exception est levée. */
			Assert.assertEquals(UtilisateurInvalideException.ErreurUtilisateur.UTILISATEUR_OBLIGATOIRE, e.getErreur());
		}
	}

	@Test
	public void creerVerificationExistanceUtilisateur() throws UtilisateurInvalideException {
		/** Etant donné un utilsateur à créer. */
		Utilisateur utilisateur = Utilisateur.builder().login(RandomStringUtils.random(10)).build();
		/* Et que l'utilisateur existe sur le système */
		UtilisateursServices spy = Mockito.spy(services);
		Mockito.doReturn(utilisateur).when(spy).rechercherParLogin(utilisateur.getLogin());

		try {
			/** Lorsqu'on appelle le service de création. */
			spy.creer(utilisateur);

			fail("Une exception devrait être levée.");
		} catch (UtilisateurInvalideException e) {
			/**
			 * Alors le service de vérification de l'existance de l'utilsiateur
			 * est appelé.
			 */
			Mockito.verify(spy).rechercherParLogin(utilisateur.getLogin());

			/* Et une exception est levée. */
			Assert.assertEquals(UtilisateurInvalideException.ErreurUtilisateur.UTILISATEUR_EXISTANT, e.getErreur());
		}
	}

	@Test
	public void creerAppelValidationUtilisateur() throws UtilisateurInvalideException {
		/** Etant donné un utilsateur à créer. */
		Utilisateur utilisateur = new Utilisateur();

		/** Lorsqu'on appelle le service de création. */
		services.creer(utilisateur);

		/** Alors le service de validation est appelé. */
		Mockito.verify(validationServices).validerUtilisateur(utilisateur);
	}

	@Test
	public void creerAppelValidationUtilisateurEnErreur() throws UtilisateurInvalideException {
		/** Etant donné un utilsateur à créer. */
		Utilisateur utilisateur = new Utilisateur();
		/* Et un utilisateur invalide */
		Mockito.when(validationServices.validerUtilisateur(utilisateur))
				.thenThrow(new UtilisateurInvalideException(ErreurUtilisateur.MDP_OBLIGATOIRE));

		try {
			/** Lorsqu'on appelle le service de création. */
			services.creer(utilisateur);

			fail("Une exception est attendue.");
		} catch (UtilisateurInvalideException e) {
			/**
			 * Alors le service de validation est appelé et l'exception est
			 * propagée.
			 */
			Mockito.verify(validationServices).validerUtilisateur(utilisateur);

			Assert.assertEquals(ErreurUtilisateur.MDP_OBLIGATOIRE, e.getErreur());
		}
	}

	@Test
	public void creerAppelNotification() throws UtilisateurInvalideException {
		/** Etant donné un utilsateur à créer. */
		Utilisateur utilisateur = Utilisateur.builder().login(RandomStringUtils.random(10)).build();
		/* Qui n'existe pas en base */
		Mockito.when(entityManager.find(Utilisateur.class, utilisateur.getLogin())).thenReturn(null);

		/** Lorsqu'on appelle le service de création. */
		services.creer(utilisateur);

		/** Alors le service de notification est appelé. */
		Mockito.verify(notificationsServices).notifier(Mockito.anyString());
	}

}
