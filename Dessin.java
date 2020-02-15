package projet;

import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileReader;
import java.io.BufferedReader;

@SuppressWarnings("serial")  
	public class Dessin extends JPanel  {
		private List<Figure> listeFigure = new ArrayList<>(); //liste de figure
		private BufferedImage image; //image de fond
		private String path;
		private Color m_color = null;

		public Dessin() {
			this.setPreferredSize(new Dimension(400,400)); //dimension de la zone de dessin
			try {
				BufferedReader br = new BufferedReader(new FileReader("saveSet.txt"));
				int tour = 0;
				String line;
				while (tour<2) {
						line = br.readLine();
						if(tour == 0) {
							Color c = new Color(Integer.parseInt(line), true);
							this.m_color = c;
							System.out.println(c);
						}
						if(tour == 1) {
							try {
								this.image = ImageIO.read(new File(path)); //si le chemin path mene a une image on l'attribut a la variable image
								this.path = path;
							}catch(Exception e) {
								this.image = null; //sinon on met image a null et on affiche sur le terminal une erreur
								this.path = "";
							}	
						}
						tour++;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}

		public void setListeFigure(List<Figure> listeFigure) {
		  this.listeFigure = listeFigure; //modifie la liste de figure
		}
		
		public List<Figure>  getListeFigure() {
		  return this.listeFigure; //retourne la liste figure
		}

		public void setImage(String path) { //permet de modifier l'image de fond
			try {
				this.image = ImageIO.read(new File(path)); //si le chemin path mene a une image on l'attribut a la variable image
				this.path = path;
			}catch(Exception e) {
				this.image = null; //sinon on met image a null et on affiche sur le terminal une erreur
				this.path = "";
				System.err.println("C'est pas une image !");
			}	
		}

		public String getPath() {
			return this.path;
		}

		public Color getColor() {
			return this.getBackground();
		}

		public void setColor(Color c) {
			this.m_color = c;
			this.setBackground(m_color);
		}

		public void paintComponent(Graphics g) {
		    super.paintComponent(g);

		    if (this.image != null) { //si image n'est pas null on met cette image en fond
		    	g.drawImage(image, 0, 0, null);
		    	this.setBackground(Color.WHITE);
		    } 
		    if (this.m_color != null) {
		    	this.setBackground(m_color);
		    }else {
		    	this.setBackground(Color.WHITE);
		    }
			int nombre = listeFigure.size();
			Figure figure;
			for (int i=0; i<nombre; i++) //pour chaque figure
			{
				figure=listeFigure.get(i);
				g.setColor(figure.couleur); //on met la couleur correspondant a la figure
				if((figure.nom).equals("ellipse")) //si c'est une ellipse on dessine une ellipse
				{
					g.drawOval(figure.posx,figure.posy,figure.largeur,figure.hauteur);
				}
				else
				{	//sinon on calcule la police et on affiche un texte avec celle-ci
					int taille=(int)Math.sqrt((figure.largeur*figure.largeur)+(figure.hauteur*figure.hauteur));
					Font font = new Font ("Arial", 1,taille);
					g.setFont(font);
					g.drawString(figure.valeur,figure.posx,figure.posy);
				}
			}
		}
	}