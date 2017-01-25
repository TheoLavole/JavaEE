package fr.sgr.formation.voteapp.utilisateurs.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(of = { "id" })
public class Ville {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String codePostal;
	private String nom;
}
