package fr.sgr.formation.voteapp.gestion.ws;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DescriptionErreur {
	private String codeErreur;
	private String description;
}
