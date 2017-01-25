package fr.sgr.formation.voteapp.calculatrice.modele;

import lombok.Builder;
import lombok.Data;

@Data
public class ResultatOperation extends ParametresOperation {
	private double resultat;
	private String typeOperation;

	@Builder
	public ResultatOperation(double operande1, double operande2, String typeOperation, double resultat) {
		super(operande1, operande2);

		this.typeOperation = typeOperation;
		this.resultat = resultat;
	}

	public String toHtml() {
		StringBuilder stringBuilder = new StringBuilder();

		/** Construction de la réponse au format demandé... */
		stringBuilder.append("<html>");
		stringBuilder.append("<body>");
		stringBuilder.append("<h1>Résultat de l'addition</H1>");
		stringBuilder.append(getOperande1() + getTypeOperation() + getOperande2() + " = " + resultat);
		stringBuilder.append("</body>");
		stringBuilder.append("</html>");

		return stringBuilder.toString();
	}

}
