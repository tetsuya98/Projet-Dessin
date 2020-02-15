package projet;

import java.awt.Color;

public class DataSave
{
	/*Classe qui va nous permettre de stocker les donner d'une figure
	-nom étant le nom de la figure(ellipse,texte)
	-valeur est le texte a afficher si la figure est un texte
	-index permet de stocker l'indice de la figure a modifié si cela est necessaire
	*/
	//public Color couleur;
	public String couleur;
	public String path;
	public DataSave(String path, Color couleur)
	{
		//this.couleur = couleur;
		this.couleur = Integer.toString(couleur.getRGB());
		this.path = path;
	}

}