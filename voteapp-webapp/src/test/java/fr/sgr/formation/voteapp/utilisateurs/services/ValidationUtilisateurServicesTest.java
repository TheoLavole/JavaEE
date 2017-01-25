package fr.sgr.formation.voteapp.utilisateurs.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException.ErreurUtilisateur;

@RunWith(Parameterized.class)
public class ValidationUtilisateurServicesTest {
	private ValidationUtilisateurServices services = new ValidationUtilisateurServices();

	/** Ensemble des données permettant de paramétrer le test. */
	private ParamUtilisateurTest parametreTest;

	public ValidationUtilisateurServicesTest(ParamUtilisateurTest parametreTest) {
		this.parametreTest = parametreTest;
	}

	@Parameters
	public static Collection<ParamUtilisateurTest> parametres() {
		Collection<ParamUtilisateurTest> params = new ArrayList<>();

		// Utilisateur nul
		params.add(new ParamUtilisateurTest(null, false));

		// Validation du nom obligatoire: nul
		params.add(new ParamUtilisateurTest(utilisateurBuilderValide().nom(null).build(),
				ErreurUtilisateur.NOM_OBLIGATOIRE));
		// Validation du nom obligatoire: vide
		params.add(new ParamUtilisateurTest(utilisateurBuilderValide().nom("").build(),
				ErreurUtilisateur.NOM_OBLIGATOIRE));

		// Validation du prénom obligatoire: nul
		params.add(new ParamUtilisateurTest(utilisateurBuilderValide().prenom(null).build(),
				ErreurUtilisateur.PRENOM_OBLIGATOIRE));
		// Validation du prénom obligatoire: vide
		params.add(new ParamUtilisateurTest(utilisateurBuilderValide().prenom("").build(),
				ErreurUtilisateur.PRENOM_OBLIGATOIRE));

		// Validation du login obligatoire: nul
		params.add(new ParamUtilisateurTest(utilisateurBuilderValide().login(null).build(),
				ErreurUtilisateur.LOGIN_OBLIGATOIRE));
		// Validation du login obligatoire: vide
		params.add(new ParamUtilisateurTest(utilisateurBuilderValide().login("").build(),
				ErreurUtilisateur.LOGIN_OBLIGATOIRE));

		// Validation du mot de passe obligatoire: nul
		params.add(new ParamUtilisateurTest(utilisateurBuilderValide().motDePasse(null).build(),
				ErreurUtilisateur.MDP_OBLIGATOIRE));
		// Validation du mot de passe obligatoire: vide
		params.add(new ParamUtilisateurTest(utilisateurBuilderValide().motDePasse("").build(),
				ErreurUtilisateur.MDP_OBLIGATOIRE));

		return params;
	}

	@Test
	public void validerUtilisateurNul() {
		/** Etant donné un utilisateur. */
		Utilisateur utilisateur = parametreTest.utilisateur;

		try {
			/** Lorsqu'on appelle le service de validation */
			boolean res = services.validerUtilisateur(utilisateur);

			if (parametreTest.erreur == null) {
				assertEquals(parametreTest.retourService, res);
			} else {
				fail("Une exeption est attendue.");
			}
		} catch (UtilisateurInvalideException e) {
			/** Alors une exception est levée */
			assertEquals(parametreTest.erreur, e.getErreur());
		}
	}

	private static Utilisateur.UtilisateurBuilder utilisateurBuilderValide() {
		return Utilisateur.builder()
				.nom(RandomStringUtils.randomAlphabetic(10))
				.prenom(RandomStringUtils.randomAlphabetic(10))
				.login(RandomStringUtils.randomAlphabetic(10))
				.motDePasse(RandomStringUtils.randomAlphabetic(10))
				.email(RandomStringUtils.randomAlphabetic(10) + "@montest.fr");
	}

	/**
	 * Classe utilisé comme paramètre de chaque méthode de test.
	 */
	public static class ParamUtilisateurTest {
		Utilisateur utilisateur;
		UtilisateurInvalideException.ErreurUtilisateur erreur;
		boolean retourService;

		public ParamUtilisateurTest(Utilisateur utilisateur, ErreurUtilisateur erreur) {
			super();
			this.utilisateur = utilisateur;
			this.erreur = erreur;
		}

		public ParamUtilisateurTest(Utilisateur utilisateur, boolean retourService) {
			super();
			this.utilisateur = utilisateur;
			this.retourService = retourService;
		}
	}
}
