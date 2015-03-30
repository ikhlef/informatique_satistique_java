package foufa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Exercice1 {
	public static  int K =6; 
	public static  double p = 0.02;
	public static  double q = 0.01; 
	//public static  double p = 0.99;
	//public static  double q = 0.001; 
	
	
		
	//pour afficher le r�sultat
	public static void afficherVectPI(double[] VectPI){
		
		//for(int i=0; i<K+1; i++){
		for(int i=0; i<VectPI.length; i++){
				
		System.out.println(VectPI[i]);
		}
	}
	
	// remplie la matrice de transition 
	public static double[][] matriceTransition(){
		double[][] P=new double[K+1][K+1];
		for (int i=1; i<K; i++){
			P[i][i]=  (1-p-q+(2*p*q));
			P[i][i-1]=  (q*(1-p));
			P[i-1][i]=  (p*(1-q));
		}
		P[0][0]=  (1 - p);
		P[1][0]= (q*(1-p));
		P[0][1]= p;
		P[K][K]= (1-q);
		P[K-1][K]= (p*(1-q));
		P[K][K-1]= q;
		return P;
		
	}
	
	// Affichage Matrice P
	public static void afficherMatrice(double[][] P){
		System.out.println("Affichage de la matrice P :");
		for (int i=0; i<P.length; i++){
			for (int j=0; j<P.length; j++)
				System.out.print(" "+P[i][j]+" ");
			System.out.println("");
		}
	}
 
	//Question 1: la methode exacte 
		public static double [] resolutionExacte(){		
			double[] tab= new double[K+1];
			double som=0 ;
			//double=  sommbis=0;
			tab[0]=1.0;
			tab[1]=(p*tab[0]/(q*(1-p)));
			     for(int i=2;i<K;i++){
			        tab[i]=(p*(1-q)*tab[i-1]/(q*(1-p)));
			        if(i==K-1){
			            tab[K]=((p*(1-q)*tab[i]/q));    
			        }
			    } 
					for(int i=0; i<K+1;i++){	
						som+=tab[i];	
					}
					//normalisation 
					for(int i=0; i<K+1;i++){
						tab[i]/=som;	
						//sommbis+=tab[i];
					}
					//System.out.println("la somme total de pi est "+ sommbis);
			     return tab;
			}
	
		//Question 2 : methode des puissances   pi(n)= pi(n-1)* la matrice = pi0 * matrice^n
	public static double[]resolutionPuissance(double[][] P, int nit, double epsilon ){
		double[] VecteurPi =new double[K+1];
		//On initialise   PI : PI=1/|E| <=> PI=1/PI.length .On aura besoin de ce vecteur pour pouvoir effectuer
		//la formule des puissance d'une part et pour pouvoir garder les valeurs  Pi(n-1) car lors de l'application de 
		//la formule des puissances on copiera les nouvelles valeurs de vecteurPi dans le vecteur vectPiNouv, comme un clone
		for(int i=0;i<VecteurPi.length;i++){
			VecteurPi[i]=1.0/VecteurPi.length;
		}
		 // compteur permettant de savoir le nombre d'iteration avant d'atteindre la convergence 
		int cpt=0; boolean critereConvergence = false;
		// VectPiNouv : stock le r�sultat du produit vecteur matrice (VecteurPi * P)
		//ex : VectPiNouv[0] =sum( VecteurPi[0..K]*P[0..K][0] )
		double[] VectPiNouv = new double[VecteurPi.length]; 
		while (cpt<nit && !critereConvergence){ 
			for(int j=0;j<VecteurPi.length;j++){     // multiplication du vecteur ligne par veteur colonne de la matrice
				//on initialise chaque tab[j] = 0 a chaque tour de la boucle ci dessous pour pouvoir effectuer la somme 
				//ex :1er tour de boucle  tab[0]==> 0+=Pi[0..K]*Pi[0..K][0] 
				 VectPiNouv[j] = 0;
				for(int i=0;i<VecteurPi.length;i++){
					VectPiNouv[j] += VecteurPi[i]*P[i][j];
				}
			}
			if (epsilon >= 0) {//epsilon ne peut etre negatif sinon pas d'utilite a l'utiliser
				//critere de convergence vrai tant que la distance entre PI(n) ( =tab[n])  et PI(n-1) (=PI(n))> epsilon
				critereConvergence = true;
				for (int i=0;i<VecteurPi.length;i++){
					if ( Math.abs((double)(VectPiNouv[i]-VecteurPi[i])) > epsilon ) {
						critereConvergence = false;
						break;
					}
				}
			}
 // recopier les valeurs, car les valeurs du VecteurPi change a chaque fois que l'on termine de parcourir une colonne de la matrice
//P. On recopie donc ces nouvelles valeurs dans VectPi pour pouvoir au prochain tour de boucle recomparer les distances		
			for(int i=0;i<VecteurPi.length;i++){
				VecteurPi[i] = VectPiNouv[i];
			}
			cpt++;
			
		}	
		System.out.println("nombre d'iterations pour la convergence " +cpt);
		return VecteurPi;
	}
			
public static void main(String[] args) throws IOException{
		
	PrintWriter ecrivain, ecrivain1;
		double[] v1 = new double[K+1];
		double[] v2 = new double[K+1];
		double[][] matriceP = new double[K+1][K+1];
		String fic="C:/Users/foufi2012/workspace/IKHLEF_Projet3_LI323/methode_exacte.txt";
		String fic1="C:/Users/foufi2012/workspace/IKHLEF_Projet3_LI323/methode_puissance.txt";
		
		// Calcul et affichage de la matrice P
		matriceP =matriceTransition();
		afficherMatrice(matriceP);
		System.out.println();
		
		System.out.println(" Methode resolution exacte :");
		v1=resolutionExacte();
		afficherVectPI(v1);
		ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(fic)));
	     for(int a =0; a<v1.length;a++)
	     ecrivain.println(a+" "+v1[a]);
	     ecrivain.close();
		
		System.out.println();
	
		System.out.println("M�thode des puissances :");
		v2=resolutionPuissance(matriceP, 7000, 10E-5);
		//v2=resolutionPuissance(matriceP, 7000, -1);
		
		afficherVectPI(v2);
		ecrivain1 =  new PrintWriter(new BufferedWriter(new FileWriter(fic1)));
	     for(int a =0; a<v2.length;a++)
	     ecrivain1.println(a+" "+v2[a]);
	     ecrivain1.close();
	}

}
