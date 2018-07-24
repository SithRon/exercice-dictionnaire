package fr.eql.autom.dictionnaire;

import fr.eql.autom.modele.entrees.Entree;
import fr.eql.autom.modele.mots.Nom;
import fr.eql.autom.modele.mots.VerbeFini;
import fr.eql.autom.modele.proprietes.*;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static fr.eql.autom.dictionnaire.FournisseurEntrees.*;

public class FournisseurMot {

    static final String CODONS = "Codons";

    public static final VerbeFini MOT_CODONS_VERBE = getVerbeFini(ENTREE_CODER, CODONS, Mode.IND, Personne.PL1);
    public static final Nom MOT_CODONS_NOM = new Nom(ENTREE_CODON, CODONS, Nombre.PL);
    public static final VerbeFini MOT_CODEZ = getVerbeFini(ENTREE_CODER, "Codez", Mode.IND, Personne.PL2);
    public static final VerbeFini MOT_CODENT = getVerbeFini(ENTREE_CODER, "Codent", Mode.IND, Personne.PL3);


    //Ce fournisseur produit des mots pour des entrées fournies par FournisseurEntreeValide
    static Stream<Arguments> fournirArgumentsMotsValides(){
        return fournirVerbesFiniValides()
                .map(verbeFini -> Arguments.of(verbeFini.getLexeme().getIdentite(), verbeFini.getForme(), proprietesEnSet(verbeFini.getMode(), verbeFini.getAccord())));
    }

    static Stream<VerbeFini> fournirVerbesFiniValides(){
        return Stream.of(
                MOT_CODONS_VERBE, MOT_CODEZ, MOT_CODENT
        );
    }

    //Le constructeur de la classe VerbeFini est buggué donc on génère les mots avec une methode statique
    //Dans ce cas là il est conseillé d'écrire un test annexe -> voir classe VerbeFiniTest
    private static VerbeFini getVerbeFini(Entree entree, String forme, Mode mode, Personne personne) {
        VerbeFini codons = new VerbeFini(entree, forme, mode, personne);
        codons.setAccord(personne);
        return codons;
    }

    //Ce fournisseur produit des mots invalides car prop manquante pour des entrées fournies par FournisseurEntreeValide
    static Stream<Arguments> fournirArgumentsMotsPropManquante(){
        return Stream.of(
                Arguments.of(CODER, "Codons", proprietesEnSet(Mode.IND)),
                Arguments.of(CODER, "Codez", proprietesEnSet(Personne.PL2))
        );
    }

    //Ce fournisseur produit des mots invalides car prop en double pour des entrées fournies par FournisseurEntreeValide
    static Stream<Arguments> fournirArgumentsMotsPropDouble(){
        return Stream.of(
                Arguments.of(CODER, "Codons", proprietesEnSet(Mode.IND, Personne.PL1, Personne.PL3)),
                Arguments.of(CODER, "Codez", proprietesEnSet(Mode.IND,Mode.IMP, Personne.PL2))
        );
    }

    //Ce fournisseur produit des mots valide avec prop suruméraires pour des entrées fournies par FournisseurEntreeValide
    static Stream<Arguments> fournirArgumentsMotsValidesPropSurnum(){
        return Stream.of(
                Arguments.of(CODER, "Codons", proprietesEnSet(Mode.IND, Personne.PL1, Genre.TOUS, Genre.M)),
                Arguments.of(CODER, "Codez", proprietesEnSet(Mode.IND, Personne.PL2, Categorie.ADJ, Categorie.ADV))
        );
    }
}
