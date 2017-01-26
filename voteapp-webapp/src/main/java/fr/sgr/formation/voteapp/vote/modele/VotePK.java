package fr.sgr.formation.voteapp.vote.modele;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VotePK implements Serializable {

	@Column(name = "id_election")
	private String electionID;

	@Column(name = "id_electeur")
	private String electeurID;

	public VotePK() {
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VotePK) {
			VotePK votePk = (VotePK) obj;

			if (!votePk.getElecteurID().equals(electeurID)) {
				return false;
			}

			if (!votePk.getElectionID().equals(electionID)) {
				return false;
			}

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return electeurID.hashCode() + electeurID.hashCode();
	}

	public String getElectionID() {
		return electionID;
	}

	public String getElecteurID() {
		return electeurID;
	}

	public void setElectionID(String electionID) {
		this.electionID = electionID;
	}

	public void setElecteurID(String electeurID) {
		this.electeurID = electeurID;
	}

}
