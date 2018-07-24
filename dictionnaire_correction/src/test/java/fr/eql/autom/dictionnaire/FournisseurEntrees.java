package fr.eql.autom.dictionnaire;

import fr.eql.autom.modele.entrees.Entree;
import fr.eql.autom.modele.entrees.EntreeNominale;
import fr.eql.autom.modele.proprietes.Categorie;
import fr.eql.autom.modele.proprietes.Genre;
import fr.eql.autom.modele.proprietes.IPropriete;
import fr.eql.autom.modele.proprietes.Personne;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FournisseurEntrees {

    static final String CODER = "Coder";
    static final String BEAU = "Beau";
    static final String PROPREMENT = "Proprement";
    static final String CODON = "Codon";

    static final Entree ENTREE_CODER = new Entree(CODER, Categorie.VERBE);
    static final Entree ENTREE_BEAU = new Entree(BEAU, Categorie.ADJ);
    static final Entree ENTREE_PROPREMENT = new Entree(PROPREMENT, Categorie.ADV);
    static final EntreeNominale ENTREE_CODON = new EntreeNominale(CODON, Genre.M);

    static Stream<Arguments> fournirArgumentEntrees(){
        return fournirEntreesNonNominale().map((entree) -> Arguments.of(entree.getIdentite(), entree.getCategorie()));
    }

    static Stream<Arguments> fournirArgumentEntreesAvecGenre(){
        return fournirEntreesNonNominale().map((entree) -> Arguments.of(entree.getIdentite(), entree.getCategorie(), Genre.TOUS));
    }
    
    static Stream<Entree> fournirEntreesNonNominale(){
    	return Stream.of(
                ENTREE_CODER,
                ENTREE_BEAU,
                ENTREE_PROPREMENT
    			);
    }

    static Stream<Arguments> fournirArgumentEntreesNominales(){
        return fournirEntreesNominales().map((entree) -> Arguments.of(entree.getIdentite(), entree.getCategorie(), entree.getGenre()));
    }

    static Stream<EntreeNominale> fournirEntreesNominales(){
        return Stream.of(
                new EntreeNominale("Code", Genre.M),
                new EntreeNominale("Testeur", Genre.M),
                new EntreeNominale("Developpeur", Genre.M),
                ENTREE_CODON
        );
    }

    static Stream<Arguments> fournirEntreesArgumentsAvecPropriete(){
        return Stream.of(
                Arguments.of("Coder", proprietesEnSet(Categorie.VERBE)),
                Arguments.of("Beau", proprietesEnSet(Categorie.ADJ, Genre.M)),
                Arguments.of("Proprement", proprietesEnSet(Categorie.ADV))
        );
    }

    static Stream<Arguments> fournirEntreesArgumentsAvecProprietesSurnumeraires(){
        return Stream.of(
                Arguments.of("Coder", proprietesEnSet(Categorie.VERBE, Genre.M, Genre.TOUS)),
                Arguments.of("Beau", proprietesEnSet(Categorie.ADJ, Genre.M, Personne.PL1, Personne.PL3)),
                Arguments.of("Proprement", proprietesEnSet(Categorie.ADV))
        );
    }

    static Set<IPropriete> proprietesEnSet(IPropriete... iProprietes){
        return Stream.of(iProprietes).collect(Collectors.toSet());
    }

}
