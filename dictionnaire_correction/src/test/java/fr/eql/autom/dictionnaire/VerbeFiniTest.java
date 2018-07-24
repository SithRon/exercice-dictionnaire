package fr.eql.autom.dictionnaire;

import fr.eql.autom.modele.entrees.Entree;
import fr.eql.autom.modele.mots.VerbeFini;
import fr.eql.autom.modele.proprietes.Categorie;
import fr.eql.autom.modele.proprietes.Mode;
import fr.eql.autom.modele.proprietes.Personne;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static fr.eql.autom.dictionnaire.FournisseurEntrees.CODER;
import static org.junit.jupiter.api.Assertions.assertNotNull;


//Test bonus pour vérifier le débuggage du constructeur de VerbeFini
public class VerbeFiniTest {

    @Test
    @DisplayName("Le contructeur a 4 arguements devrait initialiser correctement le champs accord")
    void constructeurTest (){
        VerbeFini codons = new VerbeFini(new Entree(CODER, Categorie.VERBE), "Codons", Mode.IND, Personne.PL1);
        assertNotNull(codons.getAccord());
    }
}
