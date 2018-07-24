package fr.eql.autom.dictionnaire;

import fr.eql.autom.dictionnaire.exceptions.EntreeInexistanteException;
import fr.eql.autom.dictionnaire.exceptions.MotInexistantException;
import fr.eql.autom.modele.entrees.Entree;
import fr.eql.autom.modele.mots.Mot;
import fr.eql.autom.modele.proprietes.Categorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.eql.autom.dictionnaire.FournisseurEntrees.*;
import static fr.eql.autom.dictionnaire.FournisseurMot.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests sur les fonctionnalités de recherche avec un dictionnnaire rempli d'entrées et de mots")
public class DictionnaireTestAvecEntreesEtMots {

	private IDictionnaire iDictionnaire;

	@BeforeEach
	public void initDictionnaire() {
		Dictionnaire dictionnaire = new Dictionnaire();

		//init entrées
        List<Entree> entrees = asList(ENTREE_CODER, ENTREE_BEAU, ENTREE_PROPREMENT, ENTREE_CODON);
		Map<String, Entree> mapEntrees = entrees.stream()
                .collect(Collectors.toMap(Entree::getIdentite, Function.identity()));
		dictionnaire.entrees = mapEntrees;

		//init mots
        List<Mot> motsCodons = asList(MOT_CODONS_VERBE, MOT_CODONS_NOM);
        List<Mot> motsCodez = singletonList(MOT_CODEZ);
        List<Mot> motsCodent = singletonList(MOT_CODENT);


        Map<String, List<Mot>> mapDeMots = Stream.of(motsCodons, motsCodez, motsCodent)
                .collect(Collectors.toMap(mots -> mots.get(0).getForme(), Function.identity()));

        dictionnaire.mots = mapDeMots;

        //init mot par entrée
        Map<String, List<Mot>> motsPerEntree = new HashMap<>();
        motsPerEntree.put(CODER, asList(MOT_CODONS_VERBE, MOT_CODEZ, MOT_CODENT));

        dictionnaire.motsParEntree = motsPerEntree;

        //init entrée par mot
        Map<String, List<Entree>> entreeParMot = new HashMap<>();
        entreeParMot.put(CODONS, asList(ENTREE_CODER, ENTREE_CODON));

        dictionnaire.entreesParMot = entreeParMot;
        this.iDictionnaire = dictionnaire;
	}


    //EXO 17
    @Test
    @DisplayName("Trouver les mots associés à une entrée presente avec mots associés")
    void trouverMotAssocEntree() throws EntreeInexistanteException {
        Set<Mot> mots = iDictionnaire.trouverMotsAssociesAUneEntree(CODER);
        assertEquals(3, mots.size());
        assert (mots.contains(MOT_CODONS_VERBE));
        assert (mots.contains(MOT_CODEZ));
        assert (mots.contains(MOT_CODENT));
    }


    //EXO 18
    @Test
    @DisplayName("Trouver les mots associés à une entrée présente sans mots associés")
    void trouverMotSansAssocEntree() throws EntreeInexistanteException {
        Set<Mot> mots = iDictionnaire.trouverMotsAssociesAUneEntree(BEAU);
        assertEquals(0, mots.size());
    }

    //EXO 20
    @Test
    @DisplayName("Trouver les mots associés à une entrée presente avec mots associés")
    void trouverEntreesAssocMot() throws MotInexistantException {
        Set<Entree> entrees = iDictionnaire.trouverEntreesAssocieesAuMot(CODONS);
        assertEquals(2, entrees.size());
        assert (entrees.contains(ENTREE_CODON));
        assert (entrees.contains(ENTREE_CODER));
    }

    //EXO 21
    @Test
    @DisplayName("Chercher les entrées d'un mot inexistant doit générer une erreur métier")
    void trouverMotInexistant(){
        assertThrows(MotInexistantException.class, () -> iDictionnaire.trouverEntreesAssocieesAuMot("Perdu"));
    }

    //EXO 22
    @Test
    @DisplayName("Trouver les mots associés à une entrée presente avec mots associés, filtrés par categorie")
    void trouverEntreesAssocMotCategorie() throws MotInexistantException {
        Set<Entree> entrees = iDictionnaire.trouverEntreesAssocieesAuMotParCategorie(CODONS, Categorie.NOM);
        assertEquals(1, entrees.size());
        assert (entrees.contains(ENTREE_CODON));
    }

    //EXO 23
    @Test
    @DisplayName("Trouver les mots associés à une entrée presente sans mots associés pour la catégorie")
    void trouverEntreesNonAssocMotCategorie() throws MotInexistantException {
        Set<Entree> entrees = iDictionnaire.trouverEntreesAssocieesAuMotParCategorie(CODONS, Categorie.ADJ);
        assertEquals(0, entrees.size());
    }

    //EXO 24
    @Test
    @DisplayName("Chercher les entrées d'un mot inexistant filtrés par catégorie doit générer une erreur métier")
    void trouverMotInexistantCategorie(){
        assertThrows(MotInexistantException.class, () -> iDictionnaire.trouverEntreesAssocieesAuMotParCategorie("Perdu", Categorie.ADJ));
    }

}
