package fr.eql.autom.dictionnaire;

import fr.eql.autom.dictionnaire.exceptions.CategorieNonSupporteeException;
import fr.eql.autom.dictionnaire.exceptions.EntreeInexistanteException;
import fr.eql.autom.dictionnaire.exceptions.ProprieteDupliqueeException;
import fr.eql.autom.dictionnaire.exceptions.ProprieteObligatoireIndefinieException;
import fr.eql.autom.modele.entrees.Entree;
import fr.eql.autom.modele.proprietes.Categorie;
import fr.eql.autom.modele.proprietes.Genre;
import fr.eql.autom.modele.proprietes.IPropriete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests sur les fonctionnalités d'ajout d'entées dans le iDictionnaire plein")
public class DictionnaireTestAvecEntrees {

	public IDictionnaire iDictionnaire;

	@BeforeEach
	public void initTestSuite() {
        Dictionnaire dictionnaire = new Dictionnaire();
		Map<String, Entree> entrees = FournisseurEntrees.fournirEntreesNonNominale().collect(Collectors.toMap(Entree::getIdentite, Function.identity()));
		dictionnaire.entrees = entrees;
        this.iDictionnaire = dictionnaire;
	}

    // EXO - 3
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurEntrees#fournirArgumentEntrees")
    @DisplayName("Une entrée déjà présente dans le iDictionnaire n'est pas ajoutée")
    void verifierAjoutEntreeDejaPresente(String identite, Categorie categorie) throws CategorieNonSupporteeException {
        assertFalse(iDictionnaire.ajouterEntree(identite, categorie));
    }

    // EXO - 6
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurEntrees#fournirArgumentEntreesAvecGenre")
    @DisplayName("Une entrée déjà présente dans le iDictionnaire n'est pas ajoutée même avec un genre")
    void verifierAjoutEntreeDejaPresenteAvecGenre(String identite, Categorie categorie, Genre genre) {
        assertFalse(iDictionnaire.ajouterEntree(identite, categorie, genre));
    }

    // EXO - 8
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurEntrees#fournirEntreesArgumentsAvecPropriete")
    @DisplayName("Une entrée déjà présente dans le iDictionnaire n'est pas ajoutée même avec un genre")
    void verifierAjoutEntreeDejaPresenteAvecProprietes(String identite, Set<IPropriete> proprietes) throws ProprieteObligatoireIndefinieException, ProprieteDupliqueeException {
        assertFalse(iDictionnaire.ajouterEntree(identite, proprietes));
    }

    //EXO - 12
    @DisplayName("Un mot valide est accepté")
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurMot#fournirArgumentsMotsValides")
    void verifierAjoutMotValide(String identite,String forme, Set<IPropriete> proprietes) throws ProprieteObligatoireIndefinieException, ProprieteDupliqueeException, EntreeInexistanteException {
        assert (iDictionnaire.ajouterMot(identite,forme, proprietes));
    }

    //EXO - 14
    @DisplayName("Un mot sans ses propriété obligatoire est rejeté et génère une erreur")
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurMot#fournirArgumentsMotsPropManquante")
    void verifierAjoutMotProprieteManquante(String identite,String forme, Set<IPropriete> proprietes) {
        assertThrows (ProprieteObligatoireIndefinieException.class,() -> iDictionnaire.ajouterMot(identite,forme, proprietes));
    }

    //EXO -  15
    @DisplayName("Un mot avec propriété dupliquée est rejeté et génère une erreur")
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurMot#fournirArgumentsMotsPropDouble")
    void verifierAjoutMotProprieteDouble(String identite,String forme, Set<IPropriete> proprietes) {
        assertThrows (ProprieteDupliqueeException.class,() -> iDictionnaire.ajouterMot(identite,forme, proprietes));
    }

    //EXO - 16
    @DisplayName("Un mot valide est accepté avec des propriétés surnuméraires éventuellement en double")
    @ParameterizedTest
    @MethodSource("fr.eql.autom.dictionnaire.FournisseurMot#fournirArgumentsMotsValidesPropSurnum")
    void verifierAjoutMotValidePropSurnum(String identite,String forme, Set<IPropriete> proprietes) throws ProprieteObligatoireIndefinieException, ProprieteDupliqueeException, EntreeInexistanteException {
        assert (iDictionnaire.ajouterMot(identite,forme, proprietes));
    }


}
