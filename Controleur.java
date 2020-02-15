package projet;

import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import autrevent.AutreEvent;
import autrevent.AutreEventListener;
import autrevent.AutreEventNotifieur;

public class Controleur implements AutreEventListener {

	private Modele modele;

	public Controleur(Modele modele) {
		this.modele = modele;
	}
	
	public void actionADeclancher(AutreEvent event) 
	{
		//si le controleur reçoit un evenement
		 if (event.getDonnee() instanceof Figure)  { //si cette evenement est une figure
			Figure figure = (Figure) event.getDonnee();
			 if(figure.index == -1)	//si figure.index==-1 on ajoute 
			 {
				modele.addFigure(figure); 
			 }
			 else	//sinon on modifie
			 {
				 modele.modifFigure(figure); 
			 }
		  } else if (event.getDonnee() instanceof Integer) { 	// si l'evenement est un entier
			  int pos = (Integer) event.getDonnee();
			  modele.removeFigure(pos);
		  }else if (event.getDonnee() instanceof String) {
		  	String e = (String) event.getDonnee();
		  	if (e.equals("reset")) {
		  		modele.resetList();
		  	}
		  	else if (e.equals("save"))
		  	{
		  		modele.Sauve();
		  	}else if (e.equals("undo")) {
		  		modele.Undo();
		  	}
		  }else if (event.getDonnee() instanceof DataSave) {
		  		DataSave ds = (DataSave) event.getDonnee();
		  		modele.SauveSet(ds);
		  }
	}	
}
