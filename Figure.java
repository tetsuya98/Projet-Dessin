package projet;

import java.awt.Color;

public class Figure
{
	/*Classe qui va nous permettre de stocker les donner d'une figure
	-nom étant le nom de la figure(ellipse,texte)
	-valeur est le texte a afficher si la figure est un texte
	-index permet de stocker l'indice de la figure a modifié si cela est necessaire
	*/
	public String nom,valeur,i_couleur, state = "";
	public int posx,posy,largeur,hauteur, index, modif;
	public Color couleur;
	public Figure(String nom,String valeur,int posx,int posy,int largeur,int hauteur,Color couleur)
	{
		this.nom=nom;
		this.valeur=valeur;
		this.posx=posx;
		this.posy=posy;
		this.largeur=largeur;
		this.hauteur=hauteur;
		this.couleur=couleur;
		this.i_couleur = Integer.toString(couleur.getRGB());
		this.index = -1;
	}

	public Figure(String nom,String valeur, int index,int posx,int posy,int largeur,int hauteur,Color couleur)
	{
		this.nom=nom;
		this.valeur=valeur;
		this.posx=posx;
		this.posy=posy;
		this.largeur=largeur;
		this.hauteur=hauteur;
		this.couleur=couleur;
		this.i_couleur = Integer.toString(couleur.getRGB());
		this.index = index;
		//this.modif = -1;
	}
}