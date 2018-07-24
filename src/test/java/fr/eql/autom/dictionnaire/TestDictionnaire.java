package fr.eql.autom.dictionnaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.eql.autom.dictionnaire.exceptions.CategorieNonSupporteeException;
import fr.eql.autom.modele.entrees.Entree;
import fr.eql.autom.modele.proprietes.Categorie;

public class TestDictionnaire {

	@Test
	@DisplayName("La méthode AjouterEntrée doit permettre d'ajouter une entrée si la catégorie n'est pas nominale")
	void exo1() throws CategorieNonSupporteeException {

		IDictionnaire iDictionnaire = new Dictionnaire();
		Boolean result = iDictionnaire.ajouterEntree("Aller", Categorie.VERBE);
		assertTrue(result);
	}

	@ParameterizedTest
	@DisplayName("La méthode AjouterEntrée doit permettre d'ajouter une entrée si la catégorie n'est pas nominale")
	@MethodSource("fournirEntreesNonNominales")
	void exo1Bis(String identite, Categorie categorie) throws CategorieNonSupporteeException {

		IDictionnaire iDictionnaire = new Dictionnaire();
		Boolean result = iDictionnaire.ajouterEntree(identite, categorie);
		assertTrue(result);
		Dictionnaire dictionnaire = (Dictionnaire) iDictionnaire;
		Map<String, Entree> mapEntree = dictionnaire.entrees;
		assertEquals(1, mapEntree.size());
		Entree entree = mapEntree.get(identite);
		assertNotNull(entree);
		assertEquals(identite, entree.getIdentite());
		assertEquals(categorie, entree.getCategorie());
	}

	static Stream<Arguments> fournirEntreesNonNominales() {
		return Stream.of(Arguments.of("Aller", Categorie.VERBE), Arguments.of("Devant", Categorie.ADV),
				Arguments.of("Rapide", Categorie.ADJ));
	}

	@Test
	@DisplayName("test2")
	void exo2() throws CategorieNonSupporteeException {

		IDictionnaire iDictionnaire = new Dictionnaire();

		Executable executable = new Executable() {

			@Override
			public void execute() throws Throwable {
				iDictionnaire.ajouterEntree("poulet", Categorie.NOM);
			}
		};
		{
			assertThrows(CategorieNonSupporteeException.class, executable);

		}
		;
	}

	@Test
	@DisplayName("Test3")
	void exo3() throws CategorieNonSupporteeException 
	{
		IDictionnaire iDictionnaire = new Dictionnaire();
		Dictionnaire dictionnaire = (Dictionnaire) iDictionnaire;
		//initialisation 
		dictionnaire.entrees.put("Aller", new Entree("Aller", Categorie.VERBE));
		//test
		Boolean result = iDictionnaire.ajouterEntree("Aller", Categorie.VERBE);
		assertFalse(result);
		Map<String, Entree> mapEntree = dictionnaire.entrees;
		assertEquals(1, mapEntree.size());
	}

}