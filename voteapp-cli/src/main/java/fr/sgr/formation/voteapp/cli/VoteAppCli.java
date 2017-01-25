package fr.sgr.formation.voteapp.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class VoteAppCli implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(VoteAppCli.class);

	private static final String URL_SERVICE_ADDITION = "http://localhost:8080/voteapp-webapp/additionner";

	public static void main(String args[]) {
		SpringApplication.run(VoteAppCli.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		log.info("===========> Application démarrée");
		log.info("===========> Paramètres: {}", Arrays.stream(strings).collect(Collectors.joining(", ")));

		/** Récupération des paramètres. */
		double operande1 = 0;
		double operande2 = 0;
		if (strings != null) {
			if (strings.length >= 1) {
				operande1 = Double.valueOf(strings[0]);
			}
			if (strings.length >= 2) {
				operande2 = Double.valueOf(strings[1]);
			}
		}

		/** Appel du service. */
		appelerServiceViaSpring(operande1, operande2);
	}

	/**
	 * Appel du service d'addition via les classes java.net.
	 * 
	 * @param operande1
	 * @param operande2
	 */
	private void appelerServiceAddition(double operande1, double operande2) {
		try {
			URL url = new URL(URL_SERVICE_ADDITION + "?format=json&op1=" + operande1 + "&op2=" + operande2);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			// Méthode HTTP à utiliser
			con.setRequestMethod("GET");

			// Appel de l'URL
			int responseCode = con.getResponseCode();
			log.info("Soumission de la requête en GET sur l'URL : {}", url);
			log.info("Statut HTTP de la réponse : {}", responseCode);

			// Lecture de la réponse
			StringBuilder response = new StringBuilder();
			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
			}

			// print result
			log.info("Réponse retournée suite à l'appel du service: {}", response.toString());
		} catch (MalformedURLException e) {
			log.error("URL du service cible invalide.", e);
		} catch (IOException e) {
			log.error("Erreur de lecture de la réponse.", e);
		}
	}

	/**
	 * Appel du service d'addition via un template Spring.
	 * 
	 * @param operande1
	 * @param operande2
	 */
	private void appelerServiceViaSpring(double operande1, double operande2) {
		RestTemplate restTemplate = new RestTemplate();
		ReponseAddition res = restTemplate.getForObject(
				URL_SERVICE_ADDITION + "?format=json&op1=" + operande1 + "&op2=" + operande2, ReponseAddition.class);

		log.info("Réponse retournée suite à l'appel du service: {}", res.toString());
	}
}
