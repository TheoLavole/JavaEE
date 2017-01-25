package fr.sgr.formation.voteapp.utilisateurs.services;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.utilisateurs.modele.Ville;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VilleService {
	@Autowired
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.REQUIRED)
	public void creer(Ville ville) {
		log.info("=====> Ville ajoutée dans la base : {}", ville);
		entityManager.persist(ville);
	}

	public long rechercherIdVille(Ville ville) {
		log.info("=====> Recherche de la ville {}.", ville);
		long res = Integer.parseInt(entityManager.createNativeQuery("SELECT ID FROM VILLE WHERE CODE_POSTAL='"
				+ ville.getCodePostal() + "' AND NOM='" + ville.getNom() + "'").getSingleResult().toString());
		return res;
	}
}
