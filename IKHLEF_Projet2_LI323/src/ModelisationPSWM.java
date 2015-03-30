import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ModelisationPSWM {
		static char [] tableau_Acide={'A','C','D','E','F','G','H','I','K','L','M','N','P','Q','R','S','T','V','W','Y','-'};
		static int M ,N, L, q; // nombre de proteine, longueur, nbre d'acides aminés
	    public ArrayList<String> liste_Dtrain, liste_Test_seq, liste_Distances;
		private Scanner lecteur;
	    
		
	    public ModelisationPSWM() throws FileNotFoundException{
	         M = 5643;
	         q = 21;
	         L = 48;
	         N=114;
	         
	         liste_Dtrain = new ArrayList<String>();
	         liste_Test_seq = new ArrayList<String>(); 
	         liste_Distances=new ArrayList<String>();
	         
     
	    }
  
	    //a) fonction d'ouverture du fichier et qui renvoie le flux 
	    public File open_file(String fichier){
		File file = null;

		if(fichier.equals("Dtrain")){
			file = new File("./donnees/Dtrain.txt");
		}
		else if(fichier.equals("test_seq")){
			file = new File("./donnees/test_seq.txt");
		}
		else if(fichier.equals("distances")){
			file = new File("./donnees/distances.txt");
		
			}	
		return file;
	    }
	    
	    //b) fonction qui initialise les listes (Dtrain, Distances et test_seq). 
	     public void preparation_Liste(String fichier) throws FileNotFoundException{
	        File file = open_file(fichier);
	        String contenu;
	        if(file != null){
	            lecteur = new Scanner(file);
	            while (lecteur.hasNextLine()){
	                contenu = lecteur.nextLine();
	                if(contenu.charAt(0) == '>')
	                    contenu = lecteur.nextLine();
	                if(fichier.equals("Dtrain"))
	                    liste_Dtrain.add(contenu);
	                
	              else if(fichier.equals("test_seq"))
	            	  liste_Test_seq.add(contenu);
	 
	            else if(fichier.equals("distances"))
	            	liste_Distances.add(contenu);
	            
	            }
	         }    
	    }
	    
  // c) initialiser les  lilstes Dtrain, distances et Test_seq 
	 public void initialiser_liste() throws FileNotFoundException{
	    	 this.preparation_Liste("Dtrain");
	         this.preparation_Liste("test_seq");
	         //this.preparation_Liste("PairesMij");
	         this.preparation_Liste("distances");
	         //this.preparation_Liste("ValeurPairesMij");
	    	 
	     }     
	/*------------------------A. Estimer une PSWM------------------------------*/
// QUESTION 1: calculer le nombre d'occurence ni(a) d'un acide aminé a , pour une posisition donnée i=0----L-1 (la colonne), 
//  equation 1, et le poid wi(a) equation 3.	 
	 			
	    
// 1-1 ) calcule de ni(a) 
 public int nombreOccurence(char a, int position, String fileName) throws FileNotFoundException{
        int nbreOccu=0;
        if(fileName.equals("Dtrain"))
            for(int i=0; i<M; i++){
                if(liste_Dtrain.get(i).charAt(position) == a)
                	nbreOccu++;
            }
         if(fileName.equals("test_seq"))
            for(int i = 0; i<N; i++){
                if(liste_Test_seq.get(i).charAt(position) == a)
                	nbreOccu++;
            }
       return nbreOccu;    
     }
// 1-2 calcule du poids wi(a)=(ni(a)+1)/(M+q)  
      
     public double poids_Specifique_Wi(char a, int position, String fileName) throws FileNotFoundException{
    	 return (double)(nombreOccurence(a, position, fileName) +1) / (double)(M + q);
     }
   /*------------------------ B. CONSERVATION--------------------------*/
// QUESTION 2 : calcule de l'entrepie de l'entrepie relative Si equation 4 et equation 5      
     
  // aide :calcule de log2 
     public static double log2(double x){
    	 return Math.log(x)/Math.log(2);
     }
    
  // Calcule de l'entropie Si=log2(q)+la somme des wi(a)* log2[wi(a)] equation 4
      
     public double Entropie(int posistion,String file) throws FileNotFoundException{ 	 
         double entropie=0;
           for(int i=0;i<q;i++){
          	entropie+=(poids_Specifique_Wi(tableau_Acide[i],posistion,file)*log2(poids_Specifique_Wi(tableau_Acide[i],posistion,file)));
           } 
         return log2(q)+entropie;
     }
     
   //calculer le poids specifique maximum d'un acide aminé a. dans le fichier Dtrain
     
     public double calcul_Max_un_w1(char a,String file)throws FileNotFoundException{
    	 double ps1 = poids_Specifique_Wi(a,0,file);
         double ps2=0;
         for(int i=1; i<L-1; i++){
             ps2 = poids_Specifique_Wi(a,i,file);
             if(ps2 > ps1)
                 ps1 = ps2;
         }
         return ps1;
     } 
    // calcul les 3 plus grands poids des acides amines en renvoyant leur lettres
     public ArrayList<Character> poids_Specifique_Max_Wi(String file)throws FileNotFoundException{
    	 ArrayList<Double> liste1=new ArrayList<Double>();  // liste des poids de chaque acides aminés. 
    	ArrayList<Double> liste2=new ArrayList<Double>();  // une copie de la liste des poids
    	ArrayList<Character> liste3=new ArrayList<Character>();  // la listes des 3 caracteres de poids maximum
    	
    	 for(int i=0; i<q; i++){
    		liste1.add(calcul_Max_un_w1(tableau_Acide[i],file)); 
         }
    	 liste2=  (ArrayList<Double>) liste1.clone();
    	 Collections.sort(liste2,Collections.reverseOrder());      // trier la liste 2 par ordre decroissant
    	// System.out.println(t2);
    	 for(int i=0;i<q;i++){
    		 if(liste1.get(i).equals(liste2.get(0)))
    		 liste3.add(tableau_Acide[i]);
    	 } 
    	 for(int i=0;i<q;i++){
    		 if(liste1.get(i).equals(liste2.get(1)))
    		 liste3.add(tableau_Acide[i]);
    		}
    	 for(int i=0;i<q;i++){
    		 if(liste1.get(i).equals(liste2.get(2)))
    	 liste3.add(tableau_Acide[i]);
    		 
    	 }
         return liste3;

     } 
    
	 
	 
	 
  /*-------------------------C. EVALUER une nouvelle Séquence-----------------------*/	 
     //QUESTION 3: determiner les parametres de f°(a) du model nul equation 7 et 8
     
     //Calcule de la frequence f°(b)= (1/L) * la somme des wi(b) pour i=1 jusqu'à L-1
     public double frequence_f0(char b,String fichier) throws FileNotFoundException{
         double nb = 0;
         for(int i=0; i<48; i++){
             nb+=poids_Specifique_Wi(b, i, fichier);
         }
         return (double)(nb/L);
     }

// Calcule de la probabilite P°(b0,....,bL-1) = le produit des f°(bi) pour i=0......L-1
     public double probabilite_P0(int sequence, String fichier) throws FileNotFoundException{
         double PBi = 1;
         if(fichier.equals("Dtrain")){
             for(int i=0; i<L; i++){
                PBi *= frequence_f0(liste_Dtrain.get(sequence).charAt(i), fichier);
             }
         }else if(fichier.equals("test_seq")){
             for(int i=0; i<L; i++){
                PBi *= frequence_f0(liste_Test_seq.get(sequence).charAt(i), fichier);
             }
         }       
      return PBi;
     }

   //Question 4 : determiner l (equation 9)  pour chaque sous sequence de longueur L. et determiner s'il y a des sous sequences de la famille Dtrain     
     //fonction qui permet de mettre dans une liste toutes les sous sequences le longueur L possible.  
     public ArrayList<String> Liste_Sous_Sequences(){
       ArrayList<String>liste_sous_sequence =new ArrayList<String>();
       String sous_sequence_possible="";
  	   int j=0; 	
  	   int r=L;
  	    // mettre dans liste_sous_sequence toutes les sous sequences possible de longueur L
  	    do{
  	    	for(int i=j;i<r;i++){
  	    	sous_sequence_possible+=liste_Test_seq.get(0).charAt(i);
  	    	}
  	    	liste_sous_sequence.add(sous_sequence_possible);
  	    	j+=1;
  	    	r+=1;
  	    	 sous_sequence_possible="";	
  	    }while(r<N);
  	      
  	 return liste_sous_sequence;
     }
     
     
     
    
     // Calcule de log_vraisemblance pour une seule sequence de test_seq
     
     public double log_vraisemblance_une_sequence(int sequence, String fileName) throws IOException{
    	 double log_oddsratio=0;
    	 ArrayList<String>resultat_sequence =new ArrayList<String>();
    	resultat_sequence=Liste_Sous_Sequences();
    	   
    	for(int i=0; i<48;i++){  
    		log_oddsratio+=(double)log2(poids_Specifique_Wi(resultat_sequence.get(sequence).charAt(i),i, "Dtrain")
    		    / (double)frequence_f0(resultat_sequence.get(sequence).charAt(i), "Dtrain"));
    	    }  
    	   
    	    System.out.print(resultat_sequence.get(sequence)+" = ");
           return log_oddsratio;   
     }
  //Calcule de Toutes les sequences du fichier test_seq
     
    public void log_vraisemblance_Totale(String fileName) throws IOException{
    	ArrayList<Double> som= new ArrayList<Double>();
    	int N=114;
    	 int L=48;
             for(int i=0; i<(N-L); i++){
                som.add((double)log_vraisemblance_une_sequence(i, fileName));
                System.out.println(som.get(i));
             }
            
             
            
            
             
     }
     
     
     
     
	 
	public static void main(String[] args) throws IOException {
		ModelisationPSWM model = new ModelisationPSWM();
		model.initialiser_liste();
		//for(String a : model.liste_Dtrain)
			//System.out.println( a);
		int j=0;
		char a= 'k';
		System.out.println("---------------------Exemple question 1---------------");
		System.out.println("le nombre d'occurence de A a la position "+ j + " est : " + model.nombreOccurence(a, j, "Dtrain"));
		System.out.println("le poid de " +a+ " a la position " +j+ " est : " +  model.poids_Specifique_Wi(a, j, "Dtrain"));
		System.out.println("---------------------Exemple question 2---------------");
		System.out.println ("l'entropie pour une position donnée " +j+ " est : " + model.Entropie(j,"Dtrain" ));
		System.out.println ("le poids specifique maximum d'un acide aminé est : " + model.calcul_Max_un_w1(a, "Dtrain"));
		System.out.println ("les 3  plus grand poids specifique maximum sont  : " + model.poids_Specifique_Max_Wi("Dtrain"));
		System.out.println("---------------------Exemple question 3---------------");
		System.out.println("la frequence f° pour l'acide aminé A est : " + model.frequence_f0(a, "Dtrain"));
		System.out.println("la probabilité d'une sequence a la position 7 est : " + model.probabilite_P0(j, "Dtrain"));
		System.out.println("---------------------Exemple question 4---------------");
		System.out.println("la liste des sous sequences possibles de test-seq est :" + model.Liste_Sous_Sequences());
		System.out.println("la log_vraisemblance de la sequence numoero 0 est : " + model.log_vraisemblance_une_sequence(j,"Dtrain"));
		System.out.println("la log_vraisemblance de la sequence numoero 2 est : " + model.log_vraisemblance_une_sequence(2,"Dtrain"));
		System.out.println("la log_vraisemblance de la sequence numoero 7 est : " + model.log_vraisemblance_une_sequence(7,"Dtrain"));
		System.out.println("la log_vraisemblance de la sequence numoero 13 est : " + model.log_vraisemblance_une_sequence(13,"Dtrain"));
		
		
		System.out.println("les sequences plus au moins probable sont comme suivant :");
		model.log_vraisemblance_Totale("Dtrain");
		
		
	}
}
