/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dijkstra;

/**
 *
 * @author ammar
 */

//Cette class ReseauMetro va representer notre graphe
public class ReseauMetro {

    //Le tas qui va abaisser la complexité de recherche
    private TasStations tasParis;
    //Le tableau contenant les stations qui sont les sommets du graphe avec ind = numero station (dans l'ordre)
    private StationMetro[] tableauParis;
    //le tableau des listes d'arc du graph, ind = station ind+1, la station représente le depart de chaque arc
    private Liste2[] tableauArcs;

    public ReseauMetro(int nombreStations) {
        //On initialise en créant le tableau des sommets (stations) avec le nombre de stations (388) qu'on récupére dans le fichier
        this.tableauParis = new StationMetro[nombreStations];
        //Le tas est créer par la même occasion
        this.tasParis = new TasStations(nombreStations);
        //Le tableau des arc aura aussi le même nombre de case vu que chaque indice présente l'arc qui commence à chaque station
        this.tableauArcs = new Liste2[nombreStations];
    }

    public int getTempsTrajet(int depart, int fin) {
        //On commence par mettre le temps à -1 par défault
        int tempsTrajet = -1;
        //On va chercher dans le tableau des arc la station de départ
        Liste2 L = tableauArcs[depart - 1];
        //On parcours la chaine en vérifiant si le numéro de station de chaque maillon est égal à la station d'arrivée, dans ce cas on renvoie cette durée
        while (L != null) {
            if (L.getNumero() == fin) {
                tempsTrajet = L.getDuree();
                break;
            }
            L = L.getQueue();
        }
        if (tempsTrajet == -1) {
            System.out.println("Erreur: les deux stations ne sont pas reliée");
        }
        return tempsTrajet;
    }

    //On recupére la station dans le tableau des stations puis son nom avec la méthode getNom() de le class StationMetro
    public String getNomLigne(int station) {
        return tableauParis[station - 1].getNom();
    }

    //On recupére la station dans le tableau des stations puis son num de ligne avec la méthode getLigne() de la class StationMetro
    public int getNumLigne(int station) {
        return tableauParis[station - 1].getLigne();
    }

    //La méthode qu'on appelle quand on parcours la liste des stations dans le fichier texte
    public void ajouterStation(int numero, String nom, int ligne) {
        //Création de la station à partir du string découpé et converti en int qu'on récupére dans le fichier
        StationMetro station = new StationMetro(numero, nom, ligne);
        //Ajout dans le tableau de sommet à l'indice i-1
        tableauParis[numero - 1] = station; //On met dans la case i-1 pour commencer à zero (l'ennoncé demande juste que ca soit dans l'ordre)
        //Insertion dans le tas
        tasParis.inserer(station);
    }

    //La méthode qu'on appelle quand on parcours la liste des arcs dans le fichier texte
    public void ajouterArc(int depart, int fin, int temps) {
        //D'abord il faut récupérer la liste déja stocké dans le tableau (la toute première valeur sera null)
        Liste2 listePrecedent = tableauArcs[depart - 1];
        //On créer ensuite une nouvelle liste avec le nouveau sommet en cours de traitement, tout en pointant vers l'ancienne liste listePrecedent dans la chaîne
        Liste2 successeurs = new Liste2(fin, temps, listePrecedent);
        //Il nous suffit maintenant d'assigner la nouvelle liste créer à la case du tableau avec ind = numeroStation-1
        tableauArcs[depart - 1] = successeurs;

    }
    
    //Fonction qu'on appelle pour avoir litineraire optimal
    public StationMetro[] itineraire(int numFin) {
        //On récupére la station d'arrivé dans le tableau
        StationMetro stationFin = tableauParis[numFin - 1];
        //Si il y'a la même station d'une autre ligne qui a une duree inférieur alors on va commencer notre recherche d'itineraire par cette station plutôt
        for (int i = 0; i < tableauParis.length; i++) {
            if (tableauParis[i].getNom().equals(stationFin.getNom()) && tableauParis[i].getDuree() < stationFin.getDuree()) {
                stationFin = tableauParis[i];
            }
        }
        StationMetro stationFinBis = stationFin;
        
        //Avant de déclarer le tableau il faut déterminer sa taille, pour cela on va chercher le nombre de station précédent la destination
        int r = 1;
        while (stationFin.getStationPrecedente() != null) {
            stationFin = stationFin.getStationPrecedente();
            r++;
        }
        //r a été incrémenté en fonction du nombre d'élément, on l'utilise maintenant pour créer notre tableau
        StationMetro[] itineraire = new StationMetro[r];
        int j = r-1;
        //Tant que la station precendente n'est pas null alors on va ajouter cette station au parcours
        //Si la station precedente est null alors on est arrivé à la station de départ et on peut s'arreter
        while (stationFinBis.getStationPrecedente() != null) {
            itineraire[j] = stationFinBis;
            stationFinBis = stationFinBis.getStationPrecedente();
            j--;
        }
        //A la fin on va ajouter la station de départ dans notre tableau
        itineraire[0] = stationFinBis;
        //On retourne le tableau de l'itineraire
        return itineraire;

    }

    public void parcoursDijkstra(int numDepart) {
        //On récupére la station de départ dans le tableau
        StationMetro stationDepart = tableauParis[numDepart - 1];
        //On diminue la duree dans le tas à 0 ce qui fera remonter la station à la racine
        tasParis.diminuerDuree(stationDepart, 0);
        //On va parcourir notre tableau de station pour mettre la duree à zero de toute les stations portant le même nom
        //Ceci nous permettera de partir avec une ligne différente si c'est plus optimale
        for (int i = 0; i < tableauParis.length; i++) {
            if (tableauParis[i].getNom().equals(stationDepart.getNom())) {
                tasParis.diminuerDuree(tableauParis[i], 0);
            }
        }
        //On va parcourir tout le tableau pour régler la duree de parcours à partir de notre stations de départ
        for (int l = 1; l <= tableauParis.length; l++) {
            //On récupére la station de de départ
            StationMetro i = tasParis.minimum();
            //On la supprime du tas avant de commencer le traitement
            tasParis.supprimerMin();
            //On récupére le premier maillon de la chaine des successeurs
            Liste2 arcIj = tableauArcs[i.getNumero()-1];
            //Tant qu'il y'a ses Successeurs
            while (arcIj != null) {
                //On récupére le successeur 
                StationMetro j = tableauParis[arcIj.getNumero()-1];
                //Si il est encore dans le tas et que sa durée est superieur à la durée de station i en cours de traitement + temps de l'arc ij
                if (tasParis.estDansTas(j.getNumero()) && (j.getDuree() > (i.getDuree() + arcIj.getDuree()))) {
                    //Dans ce cas on lui assigne la duree comme étant la longuer de l'arc ij + le temps de trajet depuis i
                    //il va ainsi remonter dans le tas
                    tasParis.diminuerDuree(j, (i.getDuree() + arcIj.getDuree()));
                    //la station le precedent sera désormais i dans un itineraire optimal
                    j.setStationPrecedente(i);   
                }
                //On passe au successeur suivant
                arcIj = arcIj.getQueue();
            }

        }
    }
}
