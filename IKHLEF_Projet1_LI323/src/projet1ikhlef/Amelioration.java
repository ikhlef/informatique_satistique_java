package projet1ikhlef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Amelioration {
	
		Realisation rea=new Realisation();
		Prediction pred=new Prediction();
		
		public int nombre_mot=17;
		double nbre_mot_fr=(2/(double)17);
		double nbre_mot_en=(9/(double)17);
		double nbre_mot_du=(1/(double)17);
		double nbre_mot_it=(5/(double)17);
		
		
		
//programmer les differentes fonctions neccessaire:
// fonction qui renvoie le nombre d occurence de la lettre en sachant la lettre precedente
		public int nombre_Occurence_lettre_preced_lettre(String langue, char lettreprec,char lettre) throws IOException {						
			int compteur=0;
			String file ="C:/Users/foufi2012/workspace/projet1ikhlef/"+langue+".txt";
				BufferedReader buffer = new BufferedReader(new FileReader(file));;
				String ligne;
				while ((ligne=buffer.readLine())!=null){
					for(int i=1; i<ligne.length();i++){
						if(ligne.charAt(i-1)==lettreprec && ligne.charAt(i)==lettre){
							compteur++; 
						}
					}   
				}
				buffer.close(); 
			return compteur;        
		}

//fonction qui renvoie le nombre d'occurence d'une lettre suivi par n'importe qu'elle lettre		
	public int nombre_Occurence_Lettres_Nimporte_Lettre(String langue, char lettre) throws IOException {					
			int compteur=0;
			String file="C:/Users/foufi2012/workspace/projet1ikhlef/"+langue+".txt";
				BufferedReader buffer = new BufferedReader(new FileReader(file));;
				String ligne;
				while ((ligne=buffer.readLine())!=null){
					for(int i=1; i<ligne.length();i++){
						if(ligne.charAt(i-1)==lettre && ligne.charAt(i)!=' '){
							compteur++;   
						}
					}    
				}
				buffer.close(); 
			return compteur;        
		}
// fonction qui renvoi le nbre d'occurence de wi suivi de w(i-1) sachant le nombre d'occurence de w(i-1)  
		////p(wi|wi-1))=(nbre_Occurence_de Wi precedé de Wi/(nbre_Occurence_Wi-1 suivi_lettre))

		public double probabilite_Letrre_Wi_Sachant_Wi_precedent_Langue(String langue,char lettreprec,char lettre) throws IOException{
				return ((double)nombre_Occurence_lettre_preced_lettre(langue, lettreprec,lettre)) /
						((double)nombre_Occurence_Lettres_Nimporte_Lettre(langue,lettreprec));		
		}
//fonction qui renvoie la probabilité d'un mot sachant la langue améliorée
		public double probabilite_Mot_Connaissant_LaLangue_amelioree (String word, String langue) throws IOException{
			double proba= rea.probabilite_Lettre_Langue(word.charAt(0), langue);
			for(int i=1; i<word.length();i++){
				proba *= probabilite_Letrre_Wi_Sachant_Wi_precedent_Langue(langue,word.charAt(i-1), word.charAt(i));
				}
			return proba;
		}
		

// fonction qui renvoie la probabilté de la langue sachant le mot améliorée.
  public String langue_Mot_ameliore(String word) throws IOException{		
			double prob_french = nbre_mot_fr * probabilite_Mot_Connaissant_LaLangue_amelioree (word, "french");
			double prob_english =nbre_mot_en * probabilite_Mot_Connaissant_LaLangue_amelioree (word, "english");
			double prob_italian =nbre_mot_it * probabilite_Mot_Connaissant_LaLangue_amelioree (word, "italian");
			double prob_dutch =nbre_mot_du * probabilite_Mot_Connaissant_LaLangue_amelioree (word, "dutch");
	//application de la fonction de decision L = argmax(l/w) 		
	double resultat= Math.max( Math.max(prob_french,prob_english), Math.max( prob_italian,prob_dutch));
			if(resultat ==prob_french){
				return "french";
			}else if(resultat == prob_english){
				return "english";
			}else if(resultat == prob_italian){
				return "italian";
  			}else if(resultat == prob_dutch){
				return "dutch";
			}else{
				return "pas de mot de cette langue";
			}
		}

  //fonction qui calcule la perfermonce ameliorée
	public double performance_ameliore(String fichier) throws IOException{
		String file ="C:/Users/foufi2012/workspace/projet1ikhlef/"+fichier+".txt";
		int nbre_rep_fausse=0; String ligne; int j=1;
	    BufferedReader buffer = new BufferedReader(new FileReader(file));;		
			while ((ligne=buffer.readLine())!=null) {
			  String tableau_mot[]= ligne.split(" ");
				for(int i=0;i<(tableau_mot.length+1)/2;i++){
	   System.out.println((j++)+")"+tableau_mot[2*i]+" "+tableau_mot[(2*i)+1]+"langue obtenu probable : "
				+langue_Mot_ameliore(tableau_mot[2*i]));
					if(!langue_Mot_ameliore(tableau_mot[2*i]).equals(tableau_mot[(2*i)+1])){
						nbre_rep_fausse++;    
					}
				} 
		}			
			buffer.close();
			System.out.println("Nombre de reponse fausse :"+nbre_rep_fausse+" sur "+nombre_mot);
			return 1-(((double)nbre_rep_fausse) / ((double)nombre_mot));  	
}
	

	
	
	
	
	public static void main(String[] args) throws IOException {
		Amelioration a= new Amelioration();
	  	
		System.out.println("3) ---------------Amelioration------------");
		System.out.println("la langue du mot est : "+ a.langue_Mot_ameliore("statistics"));
		System.out.println("la langue du mot est : "+ a.langue_Mot_ameliore("probability"));
		System.out.println("la langue du mot est : "+ a.langue_Mot_ameliore("hippiques"));
		System.out.println("la langue du mot est : "+ a.langue_Mot_ameliore("constitutionnellement"));
	System.out.println("----------------------------------");
	System.out.println("Question 3.8 , les performances");
	System.out.println("----------------------------------");
	System.out.println("le resultat de perfermance est : "+a.performance_ameliore("table"));
	System.out.println("----------------------------------");
	System.out.println("Question 3.8 , les performances");
	System.out.println("----------------------------------");
	System.out.println("le resultat de perfermance amelioré est : "+a.performance_ameliore("table"));
	System.out.println("----------------------------------");
	
	}

}
