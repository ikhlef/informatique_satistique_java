package projet1ikhlef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Prediction {
   
	Realisation rea = new Realisation();
	public int nombre_mot=17;
	double nbre_mot_fr=(2/(double)17);
	double nbre_mot_en=(9/(double)17);
	double nbre_mot_du=(1/(double)17);
	double nbre_mot_it=(5/(double)17);
   
// 2.5 fonction permettant de determiner la langue d'un mot.
	
	public String langue_Mot(String word) throws IOException{
		
		double prob_french =0.25 * rea.probabilite_Mot_Langue (word, "french");
		double prob_english = 0.25 * rea.probabilite_Mot_Langue (word, "english");
		double prob_italian = 0.25 * rea.probabilite_Mot_Langue (word, "italian");
		double prob_dutch = 0.25 * rea.probabilite_Mot_Langue (word, "dutch");
  //application de la fonction de decision L = argmax(l/w) 
		double resultat= Math.max(Math.max(prob_french,prob_english),Math.max(prob_italian,prob_dutch));

		if(resultat == prob_french){
			return "french";
		}else if(resultat ==prob_english){
			return "english";
		}else if(resultat == prob_italian){
			return "italian";
		}else if(resultat == prob_dutch){
			return "dutch";
		}else{
			return "Pas de mot de cette langue";
		}
	}
	
// 2.6	fonction de perfermonce avec une p(l) uniforme = 0.25
	public double performance(String fichier) throws IOException{
		String ligne; int Nbre_Repense_fausse=0; int j=1;
		String file ="C:/Users/foufi2012/workspace/projet1ikhlef/"+fichier+".txt";
		BufferedReader buffer = new BufferedReader(new FileReader(file));
			while ((ligne=buffer.readLine())!=null) {
				String tableau_mot[]= ligne.split(" ");
				//supprimer les espaces dans le tableau de mot, d'ou le (tableau_mot.length+1)/2
				//System.out.println((tableau_mot.length+1)/2);
				for(int i=0;i<(tableau_mot.length+1)/2;i++){
     System.out.println((j++) +")" + tableau_mot[2*i]+" "+tableau_mot[(2*i)+1]+".  langue probable est : "+langue_Mot(tableau_mot[2*i]));
                 
                 	if(!langue_Mot(tableau_mot[2*i]).equals(tableau_mot[(2*i)+1])){
                 		Nbre_Repense_fausse++;    
                 	}
				} 
			}		
		buffer.close();
		System.out.println("----------------------------------");
		System.out.println(" Nombres de reponse fausse :"+Nbre_Repense_fausse+" sur "+nombre_mot+" mots");
		double resultat= 1-(((double)Nbre_Repense_fausse) / ((double)nombre_mot));  
		
		return resultat;
	}
// 2.8.a fonction permettant de determiner la langue d'un mot en prenant en compte le nombre de mots par langue.
	
	public String langue_Mot_bis(String word) throws IOException{
		
		double prob_french =nbre_mot_fr * rea.probabilite_Mot_Langue (word, "french");
		double prob_english = nbre_mot_en * rea.probabilite_Mot_Langue (word, "english");
		double prob_italian = nbre_mot_it * rea.probabilite_Mot_Langue (word, "italian");
		double prob_dutch = nbre_mot_du * rea.probabilite_Mot_Langue (word, "dutch");
  //application de la fonction de decision L = argmax(l/w) 
		double resultat= Math.max(Math.max(prob_french,prob_english),Math.max(prob_italian,prob_dutch));

		if(resultat == prob_french){
			return "french";
		}else if(resultat ==prob_english){
			return "english";
		}else if(resultat == prob_italian){
			return "italian";
		}else if(resultat == prob_dutch){
			return "dutch";
		}else{
			return "Pas de mot de cette langue";
		}
	}
// 2.8.b fonction perfermance en prenant en compte la probabilité a priori p(l)
public double performance_bis(String fichier) throws IOException{
	String file ="C:/Users/foufi2012/workspace/projet1ikhlef/"+fichier+".txt";
	int nbre_rep_fausse_bis=0; String ligne; int j=1;
	BufferedReader buff = new BufferedReader(new FileReader(file));;
		while ((ligne=buff.readLine())!=null) {
			String tableau_mot_bis[]= ligne.split(" ");
			for(int i=0;i<(tableau_mot_bis.length+1)/2;i++){
			  System.out.println((j++)+")"+tableau_mot_bis[2*i]+" "+tableau_mot_bis[(2*i)+1]+". langue obtenu probable : "+langue_Mot_bis(tableau_mot_bis[2*i]));
			  if(!langue_Mot_bis(tableau_mot_bis[2*i]).equals(tableau_mot_bis[(2*i)+1])){
				nbre_rep_fausse_bis++;    
			  }
		    } 
		}
	buff.close();
	System.out.println("Nombres de reponse fausse :"+nbre_rep_fausse_bis+" sur "+nombre_mot);
	return 1-(((double)nbre_rep_fausse_bis) / ((double)nombre_mot));  
}
	


	
	public static void main(String[] args) throws IOException {
		Prediction predi = new Prediction();
		System.out.println("2)------------Prediction---------------");
		System.out.println("Question 5");
		System.out.println("----------------------------------");
		System.out.println("la langue du mot est : "+ predi.langue_Mot("statistics"));
		System.out.println("la langue du mot est : "+ predi.langue_Mot("probability"));
		System.out.println("la langue du mot est : "+ predi.langue_Mot("hippiques"));
		System.out.println("la langue du mot est : "+ predi.langue_Mot("constitutionnellement"));
	System.out.println("----------------------------------");
	System.out.println("Question 6 , les performances");
	System.out.println("----------------------------------");
	System.out.println("le resultat de perfermance est : "+predi.performance("table"));
	System.out.println("----------------------------------");
	System.out.println("Question 8 , les performances");
	System.out.println("----------------------------------");
	System.out.println("le resultat de perfermance est : "+predi.performance_bis("table"));
	System.out.println("----------------------------------");
	
	}
	

}
