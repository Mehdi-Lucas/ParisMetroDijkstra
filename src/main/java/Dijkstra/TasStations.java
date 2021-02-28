package Dijkstra;

/**
 *
 * @author ammar
 */

public class TasStations {

    private StationMetro[] tas;
    private int[] indicesStationsDansTas;
    private int nbElements = 0;

    public TasStations(int taille) {
        tas = new StationMetro[taille];
        indicesStationsDansTas = new int[taille];
        
        //On rempli le tableau indicesStationsDansTas avec -1 car quand on crée le tas il est encore vide et il n'y aucune station
        for (int i = 0; i < indicesStationsDansTas.length; i++) {
            indicesStationsDansTas[i] = -1;
        }

    }

    public boolean estVide() {
        return (nbElements == 0);
    }


    public void diminuerDuree(StationMetro element, int duree) {
        
        //On test si la station n'est pas présente dans le tas alors rien en se passe
        if (!estDansTas(element.getNumero())){
            return;
        }
        //On récupére l'indice dans le tas de la station, cette info est stocké dans le tableau indicesStationsDansTas
        //On calcule le pére de l'indice trouver et par la suite on change la duree avec la méthode setDuree
        int fils = indicesStationsDansTas[element.getNumero()-1];
        int pere = (fils - 1) / 2;
        element.setDuree(duree);
        
        //La même boucle que pour inserer, par echange succesif on fait remonter la station dans le tas
        //On met à jour aussi le tableau indicesStationsDansTas en assignant l'entier pere à la station element qu'on traite et fils à la station qui était dans pére
        while (pere >= 0 && element.getDuree() < tas[pere].getDuree()) {
            indicesStationsDansTas[(element.getNumero()-1)] = pere;
            indicesStationsDansTas[(tas[pere].getNumero()-1)] = fils;
            
            tas[fils] = tas[pere]; //père descend
            tas[pere] = element; //élément remonte en père

            fils = pere;
            pere = (fils - 1) / 2;


        }
    }

    public void inserer(StationMetro element) { 
        //Si le tas est plein on ne fait rien
        if (estPlein()){
            return;
        }
        //même procédure vu en cours, la différence ici c'est les changement dans indicesStationsDansTas
        int fils = nbElements;
        int pere = (fils - 1) / 2;
        tas[fils] = element;
        //On commence par mettre l'indice fils dans le tableau indicesStationsDansTas pour la station element
        indicesStationsDansTas[(element.getNumero()-1)] = fils;
        while (pere >= 0 && element.getDuree() < tas[pere].getDuree()) {
            indicesStationsDansTas[(element.getNumero()-1)] = pere; //La station element qu'on traite remonte à la place du pere donc c'est l'indice à mettre dans indicesStationsDansTas
            indicesStationsDansTas[(tas[pere].getNumero()-1)] = fils; //La station qui était dans pere va descendre dans fils donc c'est l'indice à mettre dans indicesStationsDansTas
            
            tas[fils] = tas[pere]; //père descend
            tas[pere] = element; //élément remonte en père

            fils = pere;
            pere = (fils - 1) / 2;
        }

        nbElements++;
    }

    public void supprimerMin() {
        //Si le tas est vide on ne fait rien
        if (estVide()){
            return;
        }
        //même procédure vu en cours, la différence ici c'est les changement dans indicesStationsDansTas
        nbElements--;
        StationMetro val = tas[nbElements]; //c'est l'élément que nous devons reclasser
        int pere = 0;
        int fils;
        //La valeur min qui se trouve à l'indice 0 du tas va être supprimer donc cette case doit passer à -1 dans indicesStationsDansTas
        indicesStationsDansTas[tas[0].getNumero()-1] = -1;
        indicesStationsDansTas[(val.getNumero()-1)] = 0; //La valeur de la sation qu'on reclasse va être placé à l'indice 0 dans le tas, la case dans indicesStationsDansTas doi être égal à 0
        tas[0] = val;

        do {
            fils = -1;
            //si fils gauche existe
            if ((2 * pere + 1) < nbElements) {
                fils = 2 * pere + 1;
            }
            //si fils droit existe et a 1 valeur + petite que le gauche
            if (fils != -1 && (2 * pere + 2) < nbElements && tas[2 * pere + 2].getDuree() < tas[fils].getDuree()) {
                fils++;
            }
            if (fils != -1 && val.getDuree() > tas[fils].getDuree()) {
                indicesStationsDansTas[(tas[pere].getNumero()-1)] = fils; //La station pere va prendre la place de fils, dans indicesStationsDansTas on doit indiquer que l'indice du pere est celui de fils maintenant
                indicesStationsDansTas[(tas[fils].getNumero()-1)] = pere; //La station fils va prendre la place de pere, dans indicesStationsDansTas on doit indiquer que l'indice du fils est celui de pere maintenant
                
                tas[pere] = tas[fils]; //le min des 2 fils remonte
                tas[fils] = val; //val descend

                pere = fils; //on descend pere
            } else {
                fils = -1;
            }
        } while (fils != -1);
    }

    public StationMetro minimum() {
        //Condition tas non vide
        if (estVide()) {
            return null;
        }
        return tas[0];
    }
    
    public boolean estDansTas(int numeroStation){
        //On renvoie le resultat de l'egalite entre la valeur de la case de la station à -1
        //Si égal à -1 alors la station a été supprimé
        return (indicesStationsDansTas[numeroStation-1] != -1);
    }

    public boolean estPlein() {
        return (nbElements == tas.length);
    }
}

