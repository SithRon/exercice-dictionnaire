package fr.eql.autom.dictionnaire;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import fr.eql.autom.dictionnaire.exceptions.EntreeInexistanteException;
import fr.eql.autom.dictionnaire.exceptions.ProprieteDupliqueeException;
import fr.eql.autom.dictionnaire.exceptions.ProprieteObligatoireIndefinieException;
import fr.eql.autom.modele.entrees.Entree;
import fr.eql.autom.modele.entrees.EntreeNominale;
import fr.eql.autom.modele.proprietes.Genre;
import fr.eql.autom.modele.proprietes.IPropriete;
import fr.eql.autom.modele.proprietes.Nombre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.eql.autom.dictionnaire.exceptions.CategorieNonSupporteeException;
import fr.eql.autom.modele.proprietes.Categorie;

@DisplayName("Tests sur les fonctionnalités du iDictionnaire vide")
public class DictionnaireTest {

	public IDictionnaire iDictionnaire;

	@BeforeEach
	public void initTestSuite() {
		iDictionnaire = new Dictionnaire();
	}

	// EXO - 1
	@DisplayName("Une entrée valide d'une catégorie non nominale est acceptée")
	@ParameterizedTest
	@MethodSource("fr.eql.autom.dictionnaire.FournisseurEntrees#fournirArgumentEntrees")
	void verifierAjoutEntreeNonNominale(String identite, Categorie categorie) throws CategorieNonSupporteeException {
		assert (iDictionnaire.ajouterEntree(identite, categorie));
        Entree entree = ((Dictionnaire)iDictionnaire).entrees.get(identite);
        assertNotNull(entree);
        assertEquals(categorie, entree.getCategorie());
	}
	
	
	// EXO - 1 en poussant un peu plus avec des valeurs invalides ou inattendues
	@DisplayName("Une entrée invalide est refusée.")
	@ParameterizedTest
	@MethodSource("fournirEntreesInvalides")
	void verifierAjoutEntreeNonMotNulle(String identite, Categorie categorie) throws CategorieNonSupporteeException {
		//Le code devrait renvoyer une exception métier définie et pas une RuntimeException
		assertThrows(RuntimeException.class, ()-> iDictionnaire.ajouterEntree(identite, categorie));
	}
	
	private static Stream<Arguments> fournirEntreesInvalides() {
		return Stream.of(
				Arguments.of(null, Categorie.VERBE),
				//sur la categorie NOM quelle exception prend le pas ? 
				//categorie invalide ou une exception pour identite nulle -> Choisir et implémenter en conséquence
				Arguments.of(null, Categorie.NOM),
				Arguments.of("bug !!!", null),
				Arguments.of("123456", Categorie.VERBE),
				Arguments.of("", Categorie.ADJ));
	}
	
	
	// EXO - 2
	@Test
	@DisplayName("Une entrée d'une catégorie nominale sans genre est refusée.")
	void verifierAjoutEntreeNominaleSansGenre() throws CategorieNonSupporteeException {
		assertThrows(CategorieNonSupporteeException.class, ()-> iDictionnaire.ajouterEntree("saturne", Categorie.NOM));
	}

	// EXO - 4
	@DisplayName("Une entrée valide d'une catégorie non nominale est acceptée même avec un genre")
	@ParameterizedTest
	@MethodSource("fr.eql.autom.dictionnaire.FournisseurEntrees#fournirArgumentEntreesAvecGenre")
	void verifierAjoutEntreeNonMotAvecGenre(String identite, Categorie categorie) throws CategorieNonSupporteeException {
		assert (iDictionnaire.ajouterEntree(identite, categorie));
        Entree entree = ((Dictionnaire)iDictionnaire).entrees.get(identite);
        assertNotNull(entree);
        assertEquals(categorie,entree.getCategorie());
	}

	// EXO - 5
	@DisplayName("Une entrée valide d'une catégorie  nominale est acceptée avec un genre")
	@ParameterizedTest
	@MethodSource("fr.eql.autom.dictionnaire.FournisseurEntrees#fournirArgumentEntreesNominales")
	void verifierAjoutEntreeMotAvecGenre(String identite, Categorie categorie, Genre genre) {
		assert (iDictionnaire.ajouterEntree(identite, categorie, genre));
        Entree entree = ((Dictionnaire)iDictionnaire).entrees.get(identite);
        assertNotNull(entree);
        assertEquals(Categorie.NOM, entree.getCategorie());
        assertEquals(genre,((EntreeNominale)entree).getGenre());
	}

    // EXO - 7
    @DisplayName("Une entrée valide avec propriétés est acceptée")
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurEntrees#fournirEntreesArgumentsAvecPropriete")
    void verifierAjoutEntreePropriete(String identite, Set<IPropriete> proprietes) throws ProprieteObligatoireIndefinieException, ProprieteDupliqueeException {
        assert (iDictionnaire.ajouterEntree(identite, proprietes));
        Entree entree = ((Dictionnaire)iDictionnaire).entrees.get(identite);
        assertNotNull(entree);
        IPropriete categorie = proprietes.stream().filter(iPropriete -> iPropriete instanceof Categorie).findFirst().get();
        assertEquals(categorie,entree.getCategorie());
    }

    // EXO - 9
    @Test
    @DisplayName("Une entrée sans propriété obligatoire est refusée et génère une erreur.")
    void verifierAjoutEntreeSansProp() {
        assertThrows(ProprieteObligatoireIndefinieException.class, ()-> iDictionnaire.ajouterEntree("saturne", new HashSet<>()));
    }

    // EXO - 10
    @Test
    @DisplayName("Une entrée avec propriété obligatoire dupliquée est refusée et génère une erreur.")
    void verifierAjoutEntreePropDupliquee() {
        assertThrows(ProprieteDupliqueeException.class, ()-> iDictionnaire.ajouterEntree("Coder", FournisseurEntrees.proprietesEnSet(Categorie.VERBE, Categorie.ADJ)));
    }

    // EXO - 11
    @DisplayName("Une entrée valide avec propriétés surnumeraires est acceptée")
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurEntrees#fournirEntreesArgumentsAvecProprietesSurnumeraires")
    void verifierAjoutEntreeProprieteSurnumeraire(String identite, Set<IPropriete> proprietes) throws ProprieteObligatoireIndefinieException, ProprieteDupliqueeException {
        assert (iDictionnaire.ajouterEntree(identite, proprietes));
        Entree entree = ((Dictionnaire)iDictionnaire).entrees.get(identite);
        assertNotNull(entree);

        IPropriete categorie = proprietes.stream()
                .filter(iPropriete -> iPropriete instanceof Categorie)
                .findFirst()
                .get();

        assertEquals(categorie,entree.getCategorie());
    }

    //Exo 13
    @Test
    @DisplayName("Ajouter un mot sans entrée génère une erreur")
    void verifierAjoutMotSansEntree() {
        assertThrows(EntreeInexistanteException.class, ()-> iDictionnaire.ajouterMot("Fantôme","Fantomatique", FournisseurEntrees.proprietesEnSet(Categorie.ADJ, Genre.TOUS, Nombre.SG)));
    }

    //EXO - 19
    @Test
    @DisplayName("Chercher les mots d'une entrée inexistante doit générer une erreur métier")
    void trouverEntreeInexistante(){
        assertThrows(EntreeInexistanteException.class, () -> iDictionnaire.trouverMotsAssociesAUneEntree("Perdu"));
    }



}
