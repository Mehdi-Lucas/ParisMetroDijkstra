package Dijkstra;

/**
 *
 * @author ammar
 */

public class StationMetro {
    
    //Attributs identité de la station
    private int numero;
    private String nom;
    private int ligne;
    //La duree est initialement trés grande
    private int duree = 1000;
    //La station précédente dans un itinéraire optimal
    private StationMetro stationPrecedente = null;

    
    //Constructeur qui fixe les attributs d'identité
    public StationMetro(int numero, String nom, int ligne){
       this.numero = numero;
       this.nom = nom;
       this.ligne = ligne;
    }
    
    //getter du numero de station
    public int getNumero() {
        return numero;
    }
    //getter du nom de station
    public String getNom() {
        return nom;
    }
    //getter du num de ligne
    public int getLigne() {
        return ligne;
    }
    //getter de la duree
    public int getDuree() {
        return duree;
    }
    //setter de la duree
    public void setDuree(int duree) {
        this.duree = duree;
    }
    //getter de la station precedente dans l'itineraire optimal
    public StationMetro getStationPrecedente() {
        return stationPrecedente;
    }
    //setter de la station precedente dans l'itineraire optimal
    public void setStationPrecedente(StationMetro stationPrecedente) {
        this.stationPrecedente = stationPrecedente;
    }

}

