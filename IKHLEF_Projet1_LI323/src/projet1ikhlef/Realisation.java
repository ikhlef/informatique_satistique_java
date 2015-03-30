package projet1ikhlef;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Realisation {
	
//1.2.1 compter le nombre d'occurence d'une lettre
	public int occurence_Lettre(String langue, char lettre) throws IOException {						
		int compteur=0;
		String file ="C:/Users/foufi2012/workspace/projet1ikhlef/"+langue+".txt";
		String ligne;
		BufferedReader buffer = new BufferedReader(new FileReader(file));
			while ((ligne=buffer.readLine())!=null){
				for(int i=0; i<ligne.length();i++){
					if(ligne.charAt(i)==lettre){
						compteur++; 
					}
				}   
			}
			buffer.close(); 
		return compteur;        
	}
//1.2.2.a) fonction compter le nombre de lettre d'un document donné.
	public int nombre_lettre(String langue) throws IOException {					
		int compteur=0;
		String file ="C:/Users/foufi2012/workspace/projet1ikhlef/"+langue+".txt";
		String ligne;	
		BufferedReader buffer = new BufferedReader(new FileReader(file));		
				while ((ligne=buffer.readLine())!=null){
					for(int i=0; i<ligne.length();i++){
						if(ligne.charAt(i)!=' '){
							compteur++;    
						}
					}    
				}
			
				buffer.close();
		return compteur;        
	}

//1.2.2.b) fonction pour calculer la probabilité d'une lettre sachant la langue : P(wi/langue)	
	public double probabilite_Lettre_Langue (char lettre, String langue) throws IOException{
		return (double)occurence_Lettre(langue, lettre)/(double)nombre_lettre(langue);
	}
	

// 1.2.3.1 fonction qui renvoie la probabilité d'un mot sachant la langue 
//P(W/langue)=produit des (P(wi/langue))
	public double probabilite_Mot_Langue (String word, String langue) throws IOException{
		double probabilite= probabilite_Lettre_Langue (word.charAt(0), langue);
		for(int i=1; i<word.length();i++){
			probabilite *= probabilite_Lettre_Langue (word.charAt(i), langue);
		}
		return probabilite;
	}
	
//1.2.3.2 fonction qui renvoie la probabilité d'un mot, sachant que la probabilité est uniforme pour chaque langue donnée
// probabilité(langue) = 1/4.
	public double probabilite_word (String word) throws IOException{
	return  0.25*(probabilite_Mot_Langue (word,"french")+probabilite_Mot_Langue (word,"english")+
				probabilite_Mot_Langue (word,"italian")+probabilite_Mot_Langue (word,"dutch"));
	
	}
	
	public static void main(String[] args) throws IOException {
		Realisation r =new Realisation();
		char b ='a';
		System.out.println(" Realisation ");
		System.out.println("--------------------------------- ");
		System.out.println( "nbre de la lettre "+b+" en french est : "+r.occurence_Lettre("french",b));
		System.out.println( "nbre de "+b+" en english est : "+r.occurence_Lettre("english",b));
		System.out.println( "nbre de "+b+" en dutch est : "+r.occurence_Lettre("dutch",b));
		System.out.println( "nbre de "+b+" en italian est : "+r.occurence_Lettre("italian",b));
		System.out.println( "nbre de lettre d 'un document french est : "+r.nombre_lettre("french"));
		System.out.println( "nbre de lettre d 'un document french est : "+r.nombre_lettre("italian"));
		System.out.println( "la probabilite de la lettre "+b+"dans un document en french est : "+r.probabilite_Lettre_Langue (b, "french"));
		System.out.println( "la probabilité du mot statistics en anglais est : "+r.probabilite_Mot_Langue ("statistics", "english"));
		System.out.println( "la probabilité du mot probability : "+r.probabilite_word("probability"));
		System.out.println("-------------------------------------------------------------------------");
	}

}
