package fr.sgr.formation.voteapp.elections.modele;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = { "loginElection" })
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Election {
	@Id
	private String loginElection;

	@ManyToOne
	@JoinColumn(name = "gerant")
	private Utilisateur gerant;

	private String titre;
	private String description;
	private String image;
	@Temporal(TemporalType.DATE)
	private Date dateCloture;
}
