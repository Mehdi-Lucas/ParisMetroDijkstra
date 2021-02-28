package Dijkstra;

/**
 *
 * @author ammar
 */
//Class quasiment identique à Liste
public class Liste2 {

    private int numero;
    private int duree;
    private Liste2 suivant;
    //Chaque maillon contient 3 infos : un numéro, une durée et l'adresse du prochain maillon
    public Liste2(int numero, int duree, Liste2 reste) {
        this.numero = numero;
        this.duree = duree;
        this.suivant = reste;
    }
    //Getter du numero
    public int getNumero() {
        return numero;
    }
    //Getter de la duree
    public int getDuree() {
        return duree;
    }
    //Getter de la chaine suivante
    public Liste2 getQueue() {
        return suivant;
    }
    

}
