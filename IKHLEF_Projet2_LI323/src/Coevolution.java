import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Coevolution {

	ModelisationPSWM pswm ;
	public ArrayList<Double> resultat;
	public ArrayList<Double> Mij_Trier=new ArrayList<Double>();
    public ArrayList<Double> lis_Distance=new ArrayList<Double>();
    public ArrayList<Double> Fraction_Paires=new ArrayList<Double>();
    public ArrayList<Integer> Valeur_50_Max = new ArrayList<Integer>();
	public Coevolution() throws FileNotFoundException{
        pswm =  new ModelisationPSWM();
        pswm.initialiser_liste();
        resultat=new ArrayList<Double>();
    }
	public ArrayList<Double> getResultat(){
		return resultat;
	}
    
    //QUESTION 1 :  calcule de wi(a)  
     public double poids_Specifique_Wi_bis(char a, int position, String file) throws FileNotFoundException{
        return pswm.poids_Specifique_Wi(a, position, file);
        }
     
     //QUESTION 2 :
     //calculer le nombre d'occurence ni,j(a,b) equation 10   
  public int nombreOccurence_A_B(char a, int positioni, char b, int positionj, String file) throws FileNotFoundException{
   int compteur=0; 
   if(file.equals("Dtrain")){
    for(int i = 0; i<pswm.liste_Dtrain.size(); i++){
     if(pswm.liste_Dtrain.get(i).charAt(positioni) == a && pswm.liste_Dtrain.get(i).charAt(positionj) == b){
         compteur++;
     }
    } 
   } 
  return compteur;
 }
  
 //calculer le poid Wi,j(a,b) equation 11     
 public double poids_Specifique_Wi_A_B(char a, int positioni, char b, int positionj, String fileName ) throws FileNotFoundException{
    double d=0.0476;// ===> d =1/q
    return (double)(nombreOccurence_A_B(a, positioni, b, positionj, fileName) + d)/(double)(pswm.M + pswm.q);    
 }
	
//QUESTION 3:
 
 // Calcul de Mi, fonction de calcule de Wi,j parametre deux entiers positionI,positionJ  return valeur Mi,j
  
 public double Mij(int position_i, int position_j) throws FileNotFoundException{
     double wij, wi, wj, information_mutuelle=0;
	 char [] acide_amine={'A','C','D','E','F','G','H','I','K','L','M','N','P','Q','R','S','T','V','W','Y','-'};
     for(int i= 0; i<pswm.q; i++){
        for(int j = 0; j<pswm.q; j++){
               wij = poids_Specifique_Wi_A_B(acide_amine[i], position_i, acide_amine[j], position_j, "Dtrain");
               wi = poids_Specifique_Wi_bis(acide_amine[i], position_i, "Dtrain");
               wj = poids_Specifique_Wi_bis(acide_amine[j], position_j, "Dtrain");
               information_mutuelle+=wij * pswm.log2(wij / (wi * wj));    
        }
     }
     return information_mutuelle;       
 }
	
 //QUESTION 4
 
  //Initialiser la matrice des Mij et  calcul la fraction des paires Mij selectionnee
 
  public ArrayList<Double>Fraction_Paire_Mij() throws IOException {    
        int cpt1=1, cpt2=0;
        System.out.println("Debut");
    for(int i=0; i<=pswm.L-2; i++){
         	 for(int j=cpt1; j<=pswm.L-1; j++){
            		this.resultat.add(Mij(i, j));
            		System.out.println(i + " " +j + " " +Mij(i, j));      		
         	 }
         	 cpt1++;
      }
    System.out.println("fin");
    System.out.println("taille de la liste resultat  " +resultat.size());
    
    
     	Mij_Trier=(ArrayList<Double>) resultat.clone();
     	//listes des valeurs triées par ordre decroissant pour pouvoir reccuperer les 50 plus grandes paires.
     	Collections.sort(Mij_Trier,Collections.reverseOrder());
     
     	
  //les 50 plus grandes valeurs	 
 	do{
   	 for(int i=0;i<resultat.size();i++){
   		  if(Mij_Trier.get(cpt2)== resultat.get(i)){
   			Valeur_50_Max.add(i);
   		  }
    }
   			 cpt2++; 
   		  }while(cpt2<50);
 	
 
 ///Recuperation des distances depuis le fichier distances 
for(int i=0;i<pswm.liste_Distances.size();i++){
	lis_Distance.add(Double.parseDouble(pswm.liste_Distances.get(i).substring(5,pswm.liste_Distances.get(i).length())));
  } 

 // calculer la fractions des paires selectionnees qui ont une distance < 8
for(int i=0;i<50;i++){
	if(lis_Distance.get(Valeur_50_Max.get(i)) < 8){
		Fraction_Paires.add((resultat.get(Valeur_50_Max.get(i))/lis_Distance.get(Valeur_50_Max.get(i))));
	}
}

return Fraction_Paires;

  }
	

	public static void main(String[] args) throws IOException {
		Coevolution co = new Coevolution();
		System.out.println("---------------COEVULUTION-----------------------");
		System.out.println("---------------Question 2--------------------------------");
		System.out.println("le nombre d'occurence de N et M a la position 4 et 8 est : "+co.nombreOccurence_A_B('N',4,'M',8,"Dtrain"));
		System.out.println("le poids specfique de N et M a la position 4 et 8 est : "+co.poids_Specifique_Wi_A_B('N',4,'M',8,"Dtrain"));
		System.out.println("---------------Question 3--------------------------------");
		System.out.println("l'information mutuelle de N et M a la position 4 et 8 est :" + co.Mij(4,8));
		System.out.println("l'information mutuelle de N et M a la position 0 et 1 est :" + co.Mij(0,1));
		
		System.out.println("--------------------------Question 4----------------------");
		ArrayList<Double> a = new ArrayList<Double>();
		a=co.Fraction_Paire_Mij();
		System.out.println("---listes des 50 valeurs MAX-------------------");
		System.out.println(co.Valeur_50_Max);
		System.out.println("------liste distance----");
		System.out.println(co.lis_Distance);
		System.out.println("------listes des Fractions ----");
		System.out.println(co.Fraction_Paires);
		
	}
}
