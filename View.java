package projet;

import java.util.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.JDialog;

import autrevent.AutreEvent;
import autrevent.AutreEventListener;
import autrevent.AutreEventNotifieur;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class View extends JFrame implements AutreEventListener{

	private Modele modele;
	private Controleur controleur;
	private AutreEventNotifieur notifieur = new AutreEventNotifieur();
	private JTextArea afficheTexte;
	private JScrollPane affichage;
	private Dessin zoneDessin;
	private Color couleurChoose = new Color(255,255,255);
	private int modif=-1;
	
	public View(Controleur controleur, Modele modele) {
		super("Dessin");
		Box boxGeneral = new Box(BoxLayout.Y_AXIS);
		this.modele=modele;
		modele.addAutreEventListener(this);
		this.controleur=controleur;
		notifieur.addAutreEventListener(controleur);

		Vector<String> items = new Vector<String>(); //on crée un vector qui va contenir les couleurs 
		items.add("noir");items.add("rouge"); items.add("vert");
		items.add("bleu"); items.add("orange"); items.add("violet"); items.add("blanc");

		JMenuBar barreDeMenu = new JMenuBar();

		JMenu menuFichier = new JMenu("File");
		JMenu menuEditer = new JMenu("Edit");
		JMenu menuSetting = new JMenu("Settings");
		JMenu menuOther = new JMenu("Other...");

		barreDeMenu.add(menuFichier);
			JMenuItem itemSave = new JMenuItem("Save");
			menuFichier.add(itemSave);
			JMenuItem itemLoad = new JMenuItem("Load");
 			menuFichier.add(itemLoad);
 		
		barreDeMenu.add(menuEditer);
			JMenuItem itemUndo = new JMenuItem("Undo");
			menuEditer.add(itemUndo);
			JMenuItem itemRedo = new JMenuItem("Redo");
 			menuEditer.add(itemRedo);	

		barreDeMenu.add(menuSetting);
			JMenuItem itemBack = new JMenuItem("Backgroung Setting");
			menuSetting.add(itemBack);
			JMenuItem itemSaveSet = new JMenuItem("Save Settings");
			menuSetting.add(itemSaveSet);

		barreDeMenu.add(menuOther);
			JMenuItem itemCredit = new JMenuItem("Credits");
			menuOther.add(itemCredit);

		this.setJMenuBar(barreDeMenu);

		itemUndo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {
        			notifieur.diffuserAutreEvent(new AutreEvent(this, "undo"));
            	}
    	});



		JDialog dialogBack = new JDialog(new javax.swing.JFrame(), "Background"); 
		dialogBack.setSize(700,150);

		JMenuBar barreDeReset = new JMenuBar();


		JButton menuReset = new JButton("Reset");

		barreDeReset.add(menuReset);
		dialogBack.setJMenuBar(barreDeReset);

		menuReset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {
        			zoneDessin.setImage(null);
        			zoneDessin.setColor(Color.WHITE);
        			zoneDessin.repaint();
            	}
    	});

		Box background = new Box(BoxLayout.Y_AXIS); //box contenant composant permettant de changer le fond de la zone de dessin
		background.setBorder(BorderFactory.createEtchedBorder());

		JPanel colorBackPanel = new JPanel(); //panel contenant le changement de fond par couleur
		colorBackPanel.setPreferredSize(new Dimension(300,125));
		BoxLayout geometrieManager = new BoxLayout(colorBackPanel, BoxLayout.X_AXIS);
		colorBackPanel.setLayout(geometrieManager);


		JComboBox<String> colorBack = new JComboBox<String>(items); //on crée une jcombobox avec les couleurs 
		colorBack.setPreferredSize(new Dimension(300,10));
		colorBack.setMaximumRowCount(4);

		JPanel radioBackPanel = new JPanel();
		BoxLayout geometrieManagerRBP = new BoxLayout(radioBackPanel, BoxLayout.Y_AXIS);
		radioBackPanel.setLayout(geometrieManagerRBP);

		ButtonGroup selectionColor = new ButtonGroup();
		JRadioButton comboBack = new JRadioButton("Liste");
		selectionColor.add(comboBack);
		comboBack.setSelected(true);
		JRadioButton chooserBack = new JRadioButton("Echantillon");
		selectionColor.add(chooserBack);

		radioBackPanel.add(comboBack);
		radioBackPanel.add(chooserBack);

		colorBackPanel.add(radioBackPanel);


		JButton echantillonButton = new JButton("Echantillon");
		colorBackPanel.add(echantillonButton);
		echantillonButton.setVisible(false);
		JTextField echantillonText = new JTextField();
		colorBackPanel.add(echantillonText);
		echantillonText.setPreferredSize(new Dimension(10,10));
		echantillonText.setVisible(false);
		echantillonText.setEnabled(false);

		
		comboBack.addActionListener((ActionEvent ae) -> { 
			colorBack.setVisible(true);
			echantillonButton.setVisible(false);
			echantillonText.setVisible(false);
		});

		chooserBack.addActionListener((ActionEvent ae) -> { 
			colorBack.setVisible(false);
			echantillonButton.setVisible(true);
			echantillonText.setVisible(true);
		});

		

		colorBackPanel.add(colorBack);

		JButton colorModif = new JButton("Changer"); //bouton permet de changer la couleur de fond
		colorBackPanel.add(colorModif);

		JDialog dialogColor = new JDialog(new javax.swing.JFrame(), "Color Chooser"); 
		dialogColor.setSize(700,400);

		JPanel dialogColorPanel = new JPanel();
			BoxLayout geometrieManagerDCP = new BoxLayout(dialogColorPanel, BoxLayout.Y_AXIS);
			dialogColorPanel.setLayout(geometrieManagerDCP);
		JPanel colorChooserPanel = new JPanel();
			dialogColorPanel.add(colorChooserPanel);

		JColorChooser choix = new JColorChooser();
		choix.setPreviewPanel(new JPanel());

		colorChooserPanel.add(choix);

		JButton colorValider = new JButton("OK");
		dialogColorPanel.add(colorValider);

		dialogColor.add(dialogColorPanel);


		echantillonButton.addActionListener((ActionEvent ae) -> { 
			dialogColor.setVisible(true);
		});


		colorValider.addActionListener((ActionEvent ae) -> { 
			couleurChoose = choix.getColor();
			dialogColor.setVisible(false);
			echantillonText.setBackground(couleurChoose);
		});
		

		colorModif.addActionListener((ActionEvent ae) -> { //si le bouton changer est cliqué
			Color color=new Color(0,0,0);
			if (comboBack.isSelected()){
				int index=colorBack.getSelectedIndex();
				switch (colorBack.getItemAt(index)) 
				{
					case "noir" : 
						color=Color.BLACK;
			    	break;
					case "rouge" :
						color=Color.RED;
			    	break;
					case "vert" :
						color=Color.GREEN;
			    	break;
					case "bleu" :
						color=Color.BLUE;
			    	break;
					case "orange" :
						color=Color.ORANGE;
			    	break;
					case "violet" :
						color=Color.MAGENTA;
			    	break;
			    	case "blanc" :
						color=Color.WHITE;
			    	break;
				}
				zoneDessin.setImage(null); //on met l'image de fond à null
				zoneDessin.setColor(color); //on change la couleur de fond*/
			}
		if (chooserBack.isSelected()) {	
			zoneDessin.setImage(null); //on met l'image de fond à null
			zoneDessin.setColor(couleurChoose); //on change la couleur de fond*/	
		}
				

		});
		background.add(colorBackPanel);

		JPanel picBackPanel = new JPanel(); //panel contenant le changement de fond par image
		BoxLayout geometrieManager1 = new BoxLayout(picBackPanel, BoxLayout.X_AXIS);
		picBackPanel.setLayout(geometrieManager1);

		JButton parcourir = new JButton("Parcourir"); //bouton parcourir permettant de parcourir les fichiers
		picBackPanel.add(parcourir);
		JTextField picPath = new JTextField(10);
		picBackPanel.add(picPath);
		JButton picAdd = new JButton("Valider"); //boutton permettant de valider la selection
		picBackPanel.add(picAdd);
		JFileChooser chooser = new JFileChooser(); 
		
		parcourir.addActionListener((ActionEvent ae) -> { //si parcouri est cliqué
			int result = chooser.showOpenDialog(dialogBack);
			//on ouvre une fenetre contenant JFileChooser
			if (result == JFileChooser.APPROVE_OPTION) {
    			File selectedFile = chooser.getSelectedFile(); //on stocke le fichier choisie
    			picPath.setText(selectedFile.getAbsolutePath()); //on ajoute le chemin absolue de l'image dans le jtextfield
    			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			}
		});

		picAdd.addActionListener((ActionEvent ae) -> { //si le bouton picAdd est cliqué
			zoneDessin.setImage(picPath.getText()); //on utilise la fonction setImage de la classe Dessin avec la valeur presente dans le jtextfiel picPath 
    		zoneDessin.repaint(); //on repaint la zone de dessin
		});

		background.add(picBackPanel);
		dialogBack.add(background);

		itemBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				dialogBack.setVisible(true);

			}
		});

		JDialog dialogCredit = new JDialog(new javax.swing.JFrame(), "Credits"); 
		dialogCredit.setSize(200,200);

		JPanel panelCredit = new JPanel();
		BoxLayout geometrieManagerCredit = new BoxLayout(panelCredit, BoxLayout.Y_AXIS);
		panelCredit.setLayout(geometrieManagerCredit);

		JLabel creditLabel = new JLabel("ABEL Josselin");
		JLabel creditLabel2 = new JLabel("WIMS Jimmy");
		JLabel creditLabel3 = new JLabel("Projet PO2 L3 UPJV");
		panelCredit.add(creditLabel);
		panelCredit.add(creditLabel2);
		panelCredit.add(creditLabel3);
		dialogCredit.add(panelCredit);

		itemCredit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {
            	dialogCredit.setVisible(true);
            }
    	});

		itemSaveSet.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {
            	notifieur.diffuserAutreEvent(new AutreEvent(this, new DataSave(zoneDessin.getPath(), zoneDessin.getColor())));
            	JOptionPane.showMessageDialog(null,"Donn\u00e9es Sauvegard\u00e9es", "Save Settings", JOptionPane.INFORMATION_MESSAGE); 
        	}
    	});



 		itemSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {
            	notifieur.diffuserAutreEvent(new AutreEvent(this, "save"));
            	JOptionPane.showMessageDialog(null,"Donn\u00e9es Sauvegard\u00e9es", "Save", JOptionPane.INFORMATION_MESSAGE); 
        	}
    	});
    	
    	itemLoad.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {

        		try {
        			BufferedReader br = new BufferedReader(new FileReader("save.txt"));
					String line, m_line;
					String m_name, m_valeur;
					Color m_couleur;
					int m_x, m_y, m_width, m_height;
					while ((line = br.readLine()) != null) {

							m_line = line;
							m_name = m_line.substring(0, m_line.indexOf(";"));
							m_line = m_line.substring(m_line.indexOf(";")+1, m_line.length());
							m_x = Integer.parseInt(m_line.substring(0, m_line.indexOf(";")));
							m_line = m_line.substring(m_line.indexOf(";")+1, m_line.length());
							m_y = Integer.parseInt(m_line.substring(0, m_line.indexOf(";")));
							m_line = m_line.substring(m_line.indexOf(";")+1, m_line.length());
							m_width = Integer.parseInt(m_line.substring(0, m_line.indexOf(";")));
							m_line = m_line.substring(m_line.indexOf(";")+1, m_line.length());
							m_height = Integer.parseInt(m_line.substring(0, m_line.indexOf(";")));
							m_line = m_line.substring(m_line.indexOf(";")+1, m_line.length());
							m_couleur = new Color(Integer.parseInt(m_line.substring(0, m_line.indexOf(";"))), true);
							m_line = m_line.substring(m_line.indexOf(";")+1, m_line.length());
							if (!(m_line.equals(""))){
								m_valeur = m_line;
								System.out.println("| "+m_line+" |");
							}else{m_valeur = "";};
							//System.out.println(m_line);
							notifieur.diffuserAutreEvent(new AutreEvent(this, "reset"));
							notifieur.diffuserAutreEvent(new AutreEvent(this, new Figure(m_name,m_valeur,m_x,m_y,m_width,m_height,m_couleur)));
					}
					br.close();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}

            	JOptionPane.showMessageDialog(null,"Donn\u00e9es Charg\u00e9es", "Load", JOptionPane.INFORMATION_MESSAGE); 
        	}
    	});

 		
		
		Box boiteHaut = new Box(BoxLayout.Y_AXIS); //box contenant les zone de saisie et le titre
		boiteHaut.setSize(700,150);
		JPanel panel = new JPanel();
		panel.add(boiteHaut);
		boxGeneral.add(panel);
		JLabel Titre=new JLabel("Barre d'outils"); //ajout d'un titre
		boiteHaut.add(Titre);
		
		Box boxOutils = new Box(BoxLayout.X_AXIS);
		boiteHaut.add(boxOutils);
		
		CustomTextField posX = new CustomTextField(7);//creation d'une zone de texte pour la position x
		posX.setPlaceholder("posX");
		boxOutils.add(posX);
		CustomTextField posY = new CustomTextField(7); //creation d'une zone de texte pour la position y
		posY.setPlaceholder("posY");
		boxOutils.add(posY);
		CustomTextField largeur = new CustomTextField(7); //creation d'une zone de texte pour la largeur
		largeur.setPlaceholder("largeur");
		boxOutils.add(largeur);
		CustomTextField hauteur = new CustomTextField(7);	//creation d'une zone de texte pour la hauteur
		hauteur.setPlaceholder("hauteur");
		boxOutils.add(hauteur);
		CustomTextField contenue = new CustomTextField(7); //creation d'une zone de texte pour le contenue du texte
		contenue.setPlaceholder("contenue");
		boxOutils.add(contenue);
		ButtonGroup selection=new ButtonGroup();
		JRadioButton choixEllipse=new JRadioButton("Ellipse"); //creation du bouton radio ellipse
		selection.add(choixEllipse);
		JRadioButton choixTexte=new JRadioButton("Texte");	 //creation du bouton radio texte
		selection.add(choixTexte);
		boxOutils.add(choixTexte);
		boxOutils.add(choixEllipse);
		choixTexte.setSelected(true); //le bouton radio texte est initialement selectionné
		
		choixEllipse.addActionListener(new ActionListener() {	//si on clique sur le radio bouton ellipse
        public void actionPerformed(ActionEvent ae) {
			contenue.setText("");	//on vide la zone de texte contenue
			contenue.setBackground(Color.GRAY); //on grise la zone de texte contenue
			contenue.setEnabled(false); //on rend inaccessible le jtextfield contenue
        }
		});
		
		choixTexte.addActionListener(new ActionListener() { //si on clique le radio bouton ellipse
        public void actionPerformed(ActionEvent ae) {
			contenue.setPlaceholder("contenue");
			contenue.setBackground(Color.WHITE); //on change la couleur la zone de texte contenue
			contenue.setEnabled(true); //on rend accessible le jtextfield contenue
        }
		});
			
		
		JComboBox<String> choixCouleur = new JComboBox<String>(items); //on crée un jcombobox permettant de choisir les couleurs
		choixCouleur.setMaximumRowCount(4);
		boxOutils.add(choixCouleur);
		
		JButton ajouter=new JButton("ajouter"); // on crée un bouton ajouter
		boxOutils.add(ajouter);
		ajouter.addActionListener((ActionEvent ae) -> { //si on appuie sur le bouton ajouter
			try {
				int x = Integer.parseInt(posX.getText().trim()); //on stocke chaque valeurs contenue dans les jtextfield
				int y = Integer.parseInt(posY.getText().trim());             
				int width = Integer.parseInt(largeur.getText().trim());
				int height = Integer.parseInt(hauteur.getText().trim());
				Color color=new Color(0,0,0);
				int index=choixCouleur.getSelectedIndex(); //on prend l'index selectionné par l'utilisateur
				switch (choixCouleur.getItemAt(index)) //on compare la valeur choisie avec les couleurs
				{
					case "noir" : 
						color=Color.BLACK;
			    	break;
					case "rouge" :
						color=Color.RED;
			    	break;
					case "vert" :
						color=Color.GREEN;
			    	break;
					case "bleu" :
						color=Color.BLUE;
			    	break;
					case "orange" :
						color=Color.ORANGE;
			    	break;
					case "violet" :
						color=Color.MAGENTA;
			    	break;
			    	case "blanc" :
						color=Color.WHITE;
			    	break;
				}
				String nom, valeur;
				if(choixTexte.isSelected()) //le nombre dépend du radio bouton selectionné
				{
					nom="texte";
					valeur=contenue.getText();
				}
				else
				{
					nom="ellipse";
					valeur="";
				}
				
				if (x + width > 400 ||  y + height > 400 || x < 0 || x > 400 || y > 400 || y < 0 || x + valeur.length() * width > 400 || (nom.equals("texte") && y-height<0)) {
					JOptionPane.showMessageDialog(null,"Mauvaises Coordonn\u00e9es", "Erreur", JOptionPane.ERROR_MESSAGE); 
					//si la figure est en dehors de la zone de dessin on affiche une erreur
				}else {
					if(modif==-1) //si c'est un ajout
					{
						notifieur.diffuserAutreEvent(new AutreEvent(this, new Figure(nom,valeur,x,y,width,height,color)));
						//on notifie le controleur
					}
					else //si c'est modification
					{
						notifieur.diffuserAutreEvent(new AutreEvent(this, new Figure(nom,valeur,modif,x,y,width,height,color)));
						//on notifie le controleur
						modif=-1;
						choixEllipse.setEnabled(true); //on remet l'affichage à l'etat initial
						choixTexte.setEnabled(true);
						ajouter.setText("ajouter");
					}
					posX.setPlaceholder("posX");
					posY.setPlaceholder("posY");
					largeur.setPlaceholder("largeur");
					hauteur.setPlaceholder("hauteur");
					contenue.setPlaceholder("contenue");
				}
				
			} catch (NumberFormatException nfe) { //si il y a probleme dans les coordonnée on affiche une erreur
				System.out.println("catch");
				JOptionPane.showMessageDialog(null,"Mauvaises Coordonn\u00e9es", "Erreur", JOptionPane.ERROR_MESSAGE); 
			}
		});

		JButton reset = new JButton("Reset");
		boxOutils.add(reset);
		reset.addActionListener((ActionEvent ae) -> {
			notifieur.diffuserAutreEvent(new AutreEvent(this, "reset"));
		});

		
		Box Graphique = new Box(BoxLayout.X_AXIS); //box qui contient l'affichage graphique et l'affichage objet
		zoneDessin = new Dessin(); //creation de la zone de dessin 
		zoneDessin.setOpaque(true);
		zoneDessin.setBorder(BorderFactory.createEtchedBorder());
		//zoneDessin.setBackground(Color.WHITE);
		Graphique.add(zoneDessin);
		Box boxAffiche = new Box(BoxLayout.Y_AXIS); //box contenant l'affichage objet et les modification
		JScrollPane affichage=new JScrollPane();
		affichage.createVerticalScrollBar();
		afficheTexte = new JTextArea(); //creation d'un jtextarea pour l'affichage objet
		afficheTexte.setPreferredSize(new Dimension(300,200));
		afficheTexte.setLineWrap(true);
		afficheTexte.setEnabled(false);
		affichage.setViewportView(afficheTexte);
		boxAffiche.add(affichage);
		
		JPanel modification = new JPanel(); //box contenant ce qui concer la modification et suppression
		modification.setPreferredSize(new Dimension(300,50));
		CustomTextField posRemove = new CustomTextField(7);//creation d'un Jtextfield pour l'indice de la figure a modifié 
		posRemove.setPlaceholder("indice");
		modification.add(posRemove);
		JButton supprimer=new JButton("supprimer");
		modification.add(supprimer);
		supprimer.addActionListener((ActionEvent ae) -> { //si le bouton supprimer est cliqué
			try{
				int pos =Integer.parseInt(posRemove.getText().trim());;
				if ((pos>=0)&&(pos<(modele.getListe()).size()))
					//si l'indice existe on notifie le controleur
					notifieur.diffuserAutreEvent(new AutreEvent(this, pos));
			} catch (NumberFormatException nfe){ }
			finally
			{
				posRemove.setPlaceholder("indice"); //on remet posRemove a son etat initial
			}
		});
		JButton modifier=new JButton("modifier");
		modification.add(modifier);
		modifier.addActionListener((ActionEvent ae) -> { //si le bouton modifier est cliqué
			try{
				int pos =Integer.parseInt(posRemove.getText().trim());;
				if ((pos>=0)&&(pos<(modele.getListe()).size()))
				{
					posX.setText("" + modele.getListe().get(pos).posx); //on affiche chaque valeurs de la figure selectionné dans les jtextfield
					posY.setText("" + modele.getListe().get(pos).posy);
					largeur.setText("" + modele.getListe().get(pos).largeur);
					hauteur.setText("" + modele.getListe().get(pos).hauteur);
					contenue.setText(modele.getListe().get(pos).valeur);
					modif=pos;
					choixEllipse.setEnabled(false);
					choixTexte.setEnabled(false);
					if (modele.getListe().get(pos).nom.equals("ellipse")) { //on bloque le changement du type de figure 
						choixEllipse.setSelected(true);
						contenue.setEnabled(false);
						contenue.setBackground(Color.GRAY);
					}else {
						choixTexte.setSelected(true);
						contenue.setEnabled(true);
						contenue.setBackground(Color.WHITE);
					}
					ajouter.setText("Modifier"); //on modifie le texte du bouton ajouter 
				}
			} catch (NumberFormatException nfe){ }
			finally
			{
				posRemove.setPlaceholder("indice");
			}
		});
		
		boxAffiche.add(modification);

		

		//boxAffiche.add(background);
		Graphique.add(boxAffiche);
		boxGeneral.add(Graphique);
		this.add(boxGeneral);
	}
	
	@SuppressWarnings("unchecked")
	public void actionADeclancher(AutreEvent event) { //si la vue à été notifié d'un changement
	   if (event.getSource() == modele) { //si cela vient du modele
			List<Figure> listeFigure = (List<Figure>) event.getDonnee();
			String t="";
			int n = listeFigure.size();
			afficheTexte.setText(""); //on vide la jtextarea
			Figure f;
			for (int i=0 ; i<n ; ++i) //pour chaque figure
			{
				f=listeFigure.get(i);
				if(f.nom.equals("ellipse")) //selon le type de figure on affiche certaines informations dans le jtextarea
					t=i + ". " + f.nom + " : " + f.posx + ", " + f.posy + ", " + f.largeur + ", " + f.hauteur + "\n";
				else
					t=i + ". " + f.nom + " : " + f.posx + ", " + f.posy + ", " + f.valeur + "\n";
				afficheTexte.append(t);
			}
			zoneDessin.setListeFigure(listeFigure);
			zoneDessin.repaint(); //on repaint la zone de dessin
		}
	}
}