package fr.sgr.formation.voteapp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

//@WebServlet("/additionner")
public class AdditionnerServlet extends HttpServlet {
	private final static String PARAM_OPERANDE_1 = "op1";
	private final static String PARAM_OPERANDE_2 = "op2";
	private final static String PARAM_FORMAT = "format";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/** Récupération des opérandes passées en paramètre de la requête... */
		String operande1 = req.getParameter(PARAM_OPERANDE_1);
		String operande2 = req.getParameter(PARAM_OPERANDE_2);

		/** Parsing des opérandes... */
		Double op1 = lireOperande(operande1, resp);
		Double op2 = lireOperande(operande2, resp);
		if ((op1 == null) || (op2 == null)) {
			/** Une erreur de formattage est apparue */

			// Initialisation du code HTTP de retour
			resp.setStatus(400);
			resp.getWriter().close();
			return;
		}

		/** Calcul et retour du résultat... */
		Double resultat = op1 + op2;

		/** Sélection du format de sortie... */
		String format = req.getParameter(PARAM_FORMAT);
		if ("json".equalsIgnoreCase(format)) {
			ecrireReponseJSon(resp, op1, op2, resultat);
		} else if ("html".equalsIgnoreCase(format)) {
			ecrireReponseHtml(resp, op1, op2, resultat);
		} else {
			ecrireReponseTexte(resp, op1, op2, resultat);
		}

		resp.getWriter().close();
	}

	/**
	 * Parse une opérande et retourne sa valeur.<br/>
	 * Si l'opérande est mal formatée, alors initialise la réponse avec
	 * l'erreur, et retourne null.
	 * 
	 * @param operande
	 *            Opérande à parser.
	 * @param resp
	 *            Réponse HTTP initialisée en cas d'erreur.
	 * @return Valeur de l'opérande.
	 */
	private Double lireOperande(String operande, HttpServletResponse resp) throws IOException {
		Double op1 = Double.valueOf(0);
		try {
			if (StringUtils.isNotBlank(operande)) {
				op1 = Double.valueOf(operande);
			}
		} catch (NumberFormatException e) {
			/** Erreur de formatage de l'opérande */
			// Initialisation d'un message d'erreur
			resp.getWriter().format("L'opérande \"%1s\" passée en paramètre est mal formée.", operande);
			return null;
		}

		return op1;
	}

	/**
	 * Ecrit la réponse au format texte sur le flux de sortie.
	 * 
	 * @param resp
	 *            Réponse HTTP sur laquelle le résultat est écrit.
	 * @param op1
	 *            Opérande 1.
	 * @param op2
	 *            Opérande 2.
	 * @param resultat
	 *            Résultat de l'opération.
	 * @return Chaine résultat.
	 */
	private void ecrireReponseTexte(HttpServletResponse resp, Double op1, Double op2, Double resultat)
			throws IOException {
		/** Construction de la réponse au format demandé... */
		resp.getWriter().format("Résultat de %1f + %2f: %3f.", op1, op2, resultat);

		/**
		 * Précision du type de format retourné dans l'entête HTTP de la
		 * réponse...
		 */
		resp.setContentType("text/plain");
	}

	/**
	 * Ecrit la réponse au format json sur le flux de sortie.
	 * 
	 * @param resp
	 *            Réponse HTTP sur laquelle le résultat est écrit.
	 * @param op1
	 *            Opérande 1.
	 * @param op2
	 *            Opérande 2.
	 * @param resultat
	 *            Résultat de l'opération.
	 * @return Chaine résultat.
	 * @throws IOException
	 */
	private void ecrireReponseJSon(HttpServletResponse resp, Double op1, Double op2, Double resultat)
			throws IOException {
		/** Construction de la réponse au format demandé... */
		resp.getWriter().println("{");
		resp.getWriter().format("\"operande1\": \"%1f\",\n", op1);
		resp.getWriter().format("\"operande2\": \"%1f\",\n", op2);
		resp.getWriter().format("\"resultat\": \"%1f\"\n", resultat);
		resp.getWriter().println("}");

		/**
		 * Précision du type de format retourné dans l'entête HTTP de la
		 * réponse...
		 */
		resp.setContentType("application/json");
	}

	/**
	 * Ecrit la réponse au format html sur le flux de sortie.
	 * 
	 * @param resp
	 *            Réponse HTTP sur laquelle le résultat est écrit.
	 * @param op1
	 *            Opérande 1.
	 * @param op2
	 *            Opérande 2.
	 * @param resultat
	 *            Résultat de l'opération.
	 * @return Chaine résultat.
	 */
	private void ecrireReponseHtml(HttpServletResponse resp, Double op1, Double op2, Double resultat)
			throws IOException {
		/** Construction de la réponse au format demandé... */
		resp.getWriter().println("<html>");
		resp.getWriter().println("<body>");
		resp.getWriter().format("<h1>Résultat de l'addition</H1>");
		resp.getWriter().format("%1f + %2f = %3f", op1, op2, resultat);
		resp.getWriter().println("</body>");
		resp.getWriter().println("</html>");

		/**
		 * Précision du type de format retourné dans l'entête HTTP de la
		 * réponse...
		 */
		resp.setContentType("text/html");
	}
}
