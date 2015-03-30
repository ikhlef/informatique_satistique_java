package foufa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Exercice2 {
	public static  int K = 6; 
	public static  double p = 0.02; 
	public static  double q = 0.01; 
	
	public static double[] resolutionGaussSeidel(double[][] P, int nit, double epsilon ){
		double[] VectPI =new double[K+1];
		double som=0;
		//On initialise tab PI comme pour la resolution des puissances a l'etape initiale n=0 : "VectPI[i]=1.0/|E|". 
		for(int i=0;i<VectPI.length;i++){
			VectPI[i]=1.0/VectPI.length;
		}
		// som1 le cas  i<j , et som2 le cas i>j 
		double som1, som2;   
		int cpt=0; // nombre d'iterations
		boolean critereConvergence = false;
		
		// On a besoin de garder les precedentes valeurs de vectPI pour comparer les distances entre VectPi(n) et VectPi(n-1)
		//avec epsilon .Les pr�c�dentes valeurs sont copiees dans CopyVect dans la boucle ci dessous.
		double[] CopyVectPi = new double[VectPI.length]; 
		for(int i=0;i<VectPI.length;i++){
			CopyVectPi[i] = VectPI[i];
		}
//critere de Convergence qui dépend de la distance entre Pi(n) et Pi(n-1) par rapport a epsilon et de nit
		while (cpt<nit && !critereConvergence){ 
			cpt++;
			som = 0; 
			for (int j=0;j<VectPI.length;j++){
				som1 = 0;
				som2 = 0;
				
				for (int i=0;i<VectPI.length;i++) {
					//CAS DE la 1ere somme i < j.
					if(i<j) {
					som1 += VectPI[i]*P[i][j];
				}
					//CAS de la 2eme somme i > j
					if(i>j) {
					som2 += VectPI[i]*P[i][j];
				   }
				}
				// equation finale :1/(1-P[j][j]) *(som1+som2)
				VectPI[j] = (1/(1-P[j][j]))*(som1+som2);
				// Et on rajoute PI[j] a la somme des PI pour pouvoir normaliser par la suite
				som+=VectPI[j];
			}	
			// Normalisation de PI a chaque iteration demander dans l'ennonce
			for(int i=0;i<VectPI.length;i++){
				VectPI[i]/=som;
			}
			// Convergence par similarite
			if (epsilon >= 0) { 
				critereConvergence = true;
				for (int i=0;i<VectPI.length;i++){
					if ( Math.abs((double)(VectPI[i]-CopyVectPi[i])) > epsilon ) {
						critereConvergence = false;
						break;
					}
			    }
			}
	// On doit recopier les valeurs a chaque tour de boucle car les valeurs du VecteurPi change a chaque fois que
	//l'on termine de parcourir une colonne de la matrice P. On recopie ces nouvelles valeurs dans CopyVect pour
		// pouvoir au prochain tour de boucle recomparer les distances
			for(int i=0;i<VectPI.length;i++){
				CopyVectPi[i] = VectPI[i];
			}
			cpt++;
		}
		System.out.println("nombre d'iterations pour la convergence " +cpt);
		return VectPI;
	}
	
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException{
		PrintWriter ecrivain;
		
		Exercice1 ex1=new Exercice1();
		double[] v1 = new double[K+1];
		double[][] matriceP = new double[K+1][K+1];
		String fic1="C:/Users/foufi2012/workspace/IKHLEF_Projet3_LI323/methode_puissance.txt";
		matriceP =ex1.matriceTransition();
		
				System.out.println("Methode Gauss Seidel :");
				v1=resolutionGaussSeidel(matriceP, 7000,10E-5);
				ex1.afficherVectPI(v1);
				ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(fic1)));
			     for(int a =0; a<v1.length;a++)
			     ecrivain.println(a+" "+v1[a]);
			     ecrivain.close();
	}
}
