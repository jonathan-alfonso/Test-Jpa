package org.diginamic.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.diginamic.jpa.entities.Livre;

public class TestJpa {

	public static void main(String[] args) {
		/**
		 * Création instance EntityManagerFactory
		 * Création instance EntityManager
		 */
		EntityManagerFactory eFactory = null;
		try {
			eFactory = Persistence.createEntityManagerFactory("bibliothequeDB");
			EntityManager eManager = eFactory.createEntityManager();
			
			/** INSERT */
			eManager.getTransaction().begin();
			Livre newLivre = new Livre();
			newLivre.setTitre("Les fleurs du mal");
			newLivre.setAuteur("Charles Baudelaire");
			eManager.persist(newLivre);
			eManager.getTransaction().commit();
			
			System.out.println("Ajout : TITRE : " + newLivre.getTitre() + " | AUTEUR : " + newLivre.getAuteur());
			System.out.println("------------------------");
			
			/** UPDATE */
			eManager.getTransaction().begin();
			Livre updateLivre = new Livre();
			updateLivre = eManager.find(Livre.class, 5);
			if (updateLivre != null) {
				updateLivre.setTitre("Du plaisir dans la cuisine");
				eManager.merge(updateLivre);
				eManager.getTransaction().commit();
				
				System.out.println("Update : TITRE : " + updateLivre.getTitre() + " | AUTEUR : " + updateLivre.getAuteur());
			} else {
				eManager.getTransaction().rollback();
			}
			
			/** DELETE */
			eManager.getTransaction().begin();
			Livre deleteLivre = new Livre();
			deleteLivre = eManager.find(Livre.class, 2);
			if (deleteLivre != null) {
				eManager.remove(deleteLivre);
				eManager.getTransaction().commit();
			} else {
				eManager.getTransaction().rollback();
			}
			
			/** JPQL */
			TypedQuery<Livre> lQuery = eManager.createQuery("SELECT li FROM Livre li", Livre.class);
			TypedQuery<Livre> lTitleQuery = eManager.createQuery("SELECT li FROM Livre li WHERE titre='Vingt mille lieues sous les mers'", Livre.class);
			TypedQuery<Livre> lAuteurQuery = eManager.createQuery("SELECT li FROM Livre li WHERE auteur='Léon Tolstoï'", Livre.class);
			
			List<Livre> lTitleList = lTitleQuery.getResultList();
			lTitleList.stream().forEach(liv -> System.out.println("ID : " + liv.getId() + " | titre : " + liv.getTitre() + " | AUTEUR : " + liv.getAuteur()));
			
			System.out.println("------------------------");
			
			List<Livre> lAuteurList = lAuteurQuery.getResultList();
			lAuteurList.stream().forEach(liv -> System.out.println("ID : " + liv.getId() + " | TITRE : " + liv.getTitre() + " | auteur : " + liv.getAuteur()));
			
			System.out.println("------------------------");
			
			List<Livre> lList = lQuery.getResultList();
			lList.stream().forEach(liv -> System.out.println("TITRE : " + liv.getTitre() + " | AUTEUR : " + liv.getAuteur()));
			
			/** CLOSE */
			eManager.close();
			eFactory.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
}
