package projet;

import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import autrevent.AutreEvent;
import autrevent.AutreEventListener;
import autrevent.AutreEventNotifieur;

public class Modele 
{
	List<Figure> listeFigure = new ArrayList<>(); //liste de figure
	List<Figure> listeUndo = new ArrayList<>(); 
	List<Figure> listeRedo = new ArrayList<>(); 
	private AutreEventNotifieur notifieur = new AutreEventNotifieur();
	
	public void addFigure(Figure figure) //fonction qui permet d'ajouter une figure d'une liste de figure
	{
		listeFigure.add(figure); //on ajoute la figure passé en paramètre a la liste
		int index = listeFigure.size()-1;
		listeFigure.get(index).index = index;
		
		listeUndo.add(figure);
		notifieur.diffuserAutreEvent(new AutreEvent(this, listeFigure)); //on notifie la vue de ce changement
	}
	
	public List<Figure> getListe(){return listeFigure;}
	
	public void removeFigure(int position) //fonction qui permet de supprimer une figure d'une liste de figure
	{
		listeUndo.add(listeFigure.get(position));
		listeUndo.get(listeUndo.size()-1).state = "remove";
		listeFigure.remove(position);  //on supprime la figure passé en paramètre a la liste
		notifieur.diffuserAutreEvent(new AutreEvent(this, listeFigure)); //on notifie la vue de ce changement
	}
	
	public void modifFigure(Figure figure)	//fonction qui permet de modifier une figure d'une liste de figure
	{
		/*on prend l'index de la figure passé en paramètre 
		qui correspond à la place de la figure à modifié dans la liste*/
		int index=figure.index;
		listeFigure.remove(index); //on supprime la version actuelle de la figure
		//figure.index = -1;  //n'est pas utile mais reste là au cas où^^
		listeFigure.add(index,figure); //on ajoute la nouvelle à la liste
		listeUndo.add(listeFigure.get(index));
		listeUndo.get(listeUndo.size()-1).state = "modif";
		notifieur.diffuserAutreEvent(new AutreEvent(this, listeFigure)); //on notifie la vue de ce changement
	}

	public void Undo() 
	{
		for(int i=0; i<listeUndo.size(); i++) {
			System.out.println(listeUndo.get(i).nom+";"+listeUndo.get(i).valeur+";"+listeUndo.get(i).posx+";"+listeUndo.get(i).posy+";"+listeUndo.get(i).largeur+";"+listeUndo.get(i).hauteur+";"+listeUndo.get(i).i_couleur+";"+listeUndo.get(i).index+";"+listeUndo.get(i).state);
		}
		System.out.println("____________________________");
		//listeRedo.add(listeUndo.get(listeUndo.size()));
		//Figure fig = listeFigure.get(listeUndo.size());

		//listeUndo.remove(listeUndo.get(listeUndo.size()));
	}

	public void resetList(){
		listeFigure.clear();
		notifieur.diffuserAutreEvent(new AutreEvent(this, listeFigure));
	}

	public void Sauve() {

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("save.txt")));
				//writer.write(ds.couleur+"\n");
 				//writer.write(ds.path+"\n");
 				for(int i=0; i<listeFigure.size(); i++) {
					writer.write(listeFigure.get(i).nom+";"+listeFigure.get(i).posx+";"+listeFigure.get(i).posy+";"+listeFigure.get(i).largeur+";"+listeFigure.get(i).hauteur+";"+listeFigure.get(i).i_couleur+";"+listeFigure.get(i).valeur+"\n");
				}
			writer.close();
		}		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SauveSet(DataSave ds) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("saveSet.txt")));
				writer.write(ds.couleur+"\n");
 				writer.write(ds.path+"\n");
			writer.close();
		}		
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void addAutreEventListener(AutreEventListener listener) {
    	notifieur.addAutreEventListener(listener);
    }
      
    public void removeAutreEventListener(AutreEventListener listener) {
        notifieur.removeAutreEventListener(listener);
    }
}
