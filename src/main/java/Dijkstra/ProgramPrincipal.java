package Dijkstra;
/**
 *
 * @author ammar
 */

import java.io.*;
import java.util.Scanner;
import java.nio.file.Paths;

public class ProgramPrincipal {

    public static void main(String[] args) {

        //Variable avec le nom du fichier qu'on récupére depuis args (reseau_metro_projet2020.txt)
        String nomFichier = "reseau_metro_projet2020.txt";
        ecritureGraph(nomFichier);
    }
    //Methode pour afficher l'interface et lire les données que l'utilisateur ecrit
    public static void UI(String strLignes, ReseauMetro metroParis) {
        //On imprime le menu
        System.out.println("Choisir la station de départ, puis la destination." + "\n" + "\n");
        System.out.println("---------------------------------------");
        System.out.println(" Liste des lignes disponibles :");
        System.out.println("---------------------------------------" + "\n");
        //On récupére les cases du tableau conenant le nombre de stations par lignes
        String[] tableauLigne = strLignes.split(":");
        int indStation = 1;
        //iteration sur le tableau pour afficher toutes les lignes
        for (int i = 0; i < tableauLigne.length; i++) {
            int numLigne = metroParis.getNumLigne(indStation);
            String nomLigne = String.valueOf(numLigne);
            //Cas uniques  de 33 et 77 seront transformé en la 3bis et 7bis par convention 
            if (numLigne == 33) {
                nomLigne = "3bis";
            } else if (numLigne == 77) {
                nomLigne = "7bis";
            }
            //On imprime le numéro et nom de chaque ligne
            System.out.println((i + 1) + ". Ligne " + nomLigne);
            //On additionant le nombre de stations pour chaque ligne on passe à chaque fois l'indice à la première station de la ligne suivante
            indStation = indStation + Integer.parseInt(tableauLigne[i]);
        }
        //On récupére la ligne de départ que l'utilisateur choisi
        System.out.println(">>> Choisir la ligne de la station de départ : ");
        Scanner sc = new Scanner(System.in);
        String ligneDepart = sc.nextLine();
        //On récupére la ligne de destination que l'utilisateur choisi
        System.out.println(">>> Choisir la ligne de la station de destination : ");
        String ligneDestination = sc.nextLine();
        //On affiche les stations pour la ligne choisi et l'utilisateur peut par la suite choisir le numero de la station de départ
        int numStationDepart = affichageLigne(ligneDepart, metroParis, tableauLigne);
        //On affiche les stations pour la ligne choisi et l'utilisateur peut par la suite choisir le numero de la station de destination
        int numStationDestination = affichageLigne(ligneDestination, metroParis, tableauLigne);

        //On lance l'algorithme de Dijkstra sur notre graph
        metroParis.parcoursDijkstra(numStationDepart);
        //On récupére le tableau de stationMetro constituant notre itinéraire
        StationMetro[] itineraire = metroParis.itineraire(numStationDestination);
        
        //On va maintenant afficher le résultat
        System.out.println("Voici votre itinéraire, pour aller de " + metroParis.getNomLigne(numStationDepart) + " à " + metroParis.getNomLigne(numStationDestination) + " : " + "\n");
        //Compteur pour les numéro de station dans l'ordre
        int ordre = 1;
        //On va itérer sur chaque station dans le tableau itinéraire
        for (int r = 0; r < itineraire.length; r++) {
            //On récupére le num de ligne sous forme d'un String
            String nomLigneR = String.valueOf(itineraire[r].getLigne());
            nomLigneR = correctionNomLigne(nomLigneR); //Si c'est un cas particulier elle sera formaté
            
            //On récupére le nom de la station avec la méthode de StationMetro
            //Pour la station de départ on n'affiche pas temps de trajet
            if (r == 0) {
                System.out.println("Station " + ordre + " : " + itineraire[r].getNom() + " (ligne " + nomLigneR + ")");
                ordre++;
            //Si la station qu'on traite a le même nom que la précédente alors c'est un changement de station    
            } else if (itineraire[r].getNom().equals(itineraire[r - 1].getNom())) {
                //D'abord on récupére les lignes et on les formate si besoin
                String ligneR = String.valueOf(itineraire[r].getLigne());
                String ligneR1 = String.valueOf(itineraire[r - 1].getLigne());
                ligneR = correctionNomLigne(ligneR);
                ligneR1 = correctionNomLigne(ligneR1);
                //Cette fois ci on va afficher qu'il s'agit d'un changement avec sa durée 
                System.out.println(">>>>>> CHANGEMENT [" + metroParis.getTempsTrajet(itineraire[r - 1].getNumero(), itineraire[r].getNumero()) + " minutes] à " + itineraire[r].getNom() + " (ligne " + ligneR1 + " --> ligne " + ligneR + ")");
            } else {
                //Sinon c'est une station et dans ce cas on affiche le temps de trajet entre la station précédente et celle qu'on traite puis les informations de la station
                System.out.println("[Trajet de " + metroParis.getTempsTrajet(itineraire[r - 1].getNumero(), itineraire[r].getNumero()) + " minutes]");
                System.out.println("Station " + ordre + " : " + itineraire[r].getNom() + " (ligne " + nomLigneR + ")");
                ordre++;
            }
        }
        //Finalement on affiche le temps du trajet total qu'on peut récupérer dans la durée de station finale
        System.out.println(" ");
        System.out.println("La durée totale de cet itinéraire est " + itineraire[itineraire.length - 1].getDuree() + " minutes.");

    }

    //La méthode qui va construire le graph à partir des sommets (stations) et arc
    public static void ecritureGraph(String nomFichier) {
        try {
            //Initialisation du lecteur pour lire le fichier reseau_metro texte
            BufferedReader lecteur = new BufferedReader(new FileReader(nomFichier));
            //Lecture de la première ligne
            String ligne = lecteur.readLine();
            //Lecture de la seconde ligne qui représente le nombre de station
            ligne = lecteur.readLine();
            int nombreStation = Integer.parseInt(ligne);
            //Grace au nombre de station récupéré on peut dimensionner et initialiser notre ReseauMetro
            ReseauMetro metroParis = new ReseauMetro(nombreStation);
            ligne = lecteur.readLine();
            ligne = lecteur.readLine();
            //On récupére le nombre de stations par ligne
            String strLignes = ligne;
            ligne = lecteur.readLine();
            int i = 0;
            //On lance une boucle "tant que" qui s'arrête quand le fichier ne contient plus de ligne à lire
            while (!(ligne.isEmpty())) {
                //Selon la convention les lignes représentant les stations une fois  découpé nous donne 3 informations
                if ((ligne.split(":").length == 3)) {
                    //On decoupe la ligne avec les information de la station 
                    String[] tableauInfoStation = ligne.split(":");
                    //L'indice 0 du tableauInfoStation correspond au numero de la station, l'ind 1 au nom et l'ind 2 au numero de la ligne
                    //On peut alors ajouter la station à notre reseau de metro
                    metroParis.ajouterStation(Integer.parseInt(tableauInfoStation[0]), tableauInfoStation[1], Integer.parseInt(tableauInfoStation[2]));
                    //On incrémente i seuleument quand on ajoute des informations au graphe, comme ca on peut ignorer les lignes de commentaire dans le compte
                    i++;

                } //Selon la convention les lignes représentant les arcs une fois  découpé nous donne 4 informations
                else if ((ligne.split(":").length == 4)) {
                    //On decoupe la ligne avec les information de l'arc
                    String[] tableauInfoArc = ligne.split(":");
                    //L'indice 0 du tableauInfoArc correspond au numero de l'arc, l'ind 1 à x, l'ind 2 à y et l'ind 3 au temps
                    //On peut alors ajouter la station à notre reseau de metro
                    metroParis.ajouterArc(Integer.parseInt(tableauInfoArc[1]), Integer.parseInt(tableauInfoArc[2]), Integer.parseInt(tableauInfoArc[3]));
                    i++;
                }
                //Lecture d'une nouvelle ligne
                ligne = lecteur.readLine();
            }
            //Apres la lecture on lance l'interface
            UI(strLignes, metroParis);
            //A la fin de la procedure on ferme le lecteur
            try {
                lecteur.close();
            } catch (IOException r) {
                System.out.println("erreur d'E/S");
            }
            //On renvoie le string représentant le nombre de stations par ligne

            //En cas d'erreur on léve une exception    
        } catch (FileNotFoundException r) {
            System.out.println("Le fichier " + nomFichier + " est inconnu");
        } catch (IOException r) {
            System.out.println("erreur d'E/S");
        }
    }
    //Methode pour afficher les stations des lignes choisi
    public static int affichageLigne(String ligne, ReseauMetro metroParis, String[] tableauLigne) {
        Scanner scanStation = new Scanner(System.in);
        //Correction pour les numéro 15 et 16
        String nomLigne = ligne;
        if (ligne.equals("15")) {
            nomLigne = "3bis";
        } else if (ligne.equals("16")) {
            nomLigne = "7bis";
        }
        
        System.out.println("---------------------------------------");
        System.out.println(" Liste des stations de la ligne " + nomLigne + ":");
        System.out.println("---------------------------------------" + "\n");
        //On additionne le nombre de stations de chaque ligne jusqu'à arriver à la ligne choisi
        //de cette façon indicePremiereStation correspondera au numéro de la première station de la ligne choisi
        int indicePremiereStation = 1;
        for (int i = 0; i < Integer.parseInt(ligne) - 1; i++) {
            indicePremiereStation = indicePremiereStation + Integer.parseInt(tableauLigne[i]);
        }
        //On va itérer dans le tableau des lignes à partir de la station choisi
        //On s'arretera quand r = nombre de stations de cette lignes
        int r = 1;
        for (int i = indicePremiereStation; i < indicePremiereStation + Integer.parseInt(tableauLigne[Integer.parseInt(ligne) - 1]); i++) {
            System.out.println(r + ". " + metroParis.getNomLigne(i));
            r++;
        }
        //On récupére la station que l'utilisateur choisi et on la retourne
        System.out.println(">>> Choisir la station de départ sur cette ligne : ");
        int station = Integer.parseInt(scanStation.nextLine());
        int ligneDansTableau = station + indicePremiereStation - 1;

        return ligneDansTableau;

    }
    //Formatage pour les lignes 3bis et 7bis
    public static String correctionNomLigne(String nomLigne) {
        if (nomLigne.equals("33")) {
            return "3bis";
        } else if (nomLigne.equals("77")) {
            return "7bis";
        }
        return nomLigne;

    }

}
