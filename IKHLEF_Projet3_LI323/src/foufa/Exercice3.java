package foufa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Exercice3 {
	
	public static  int K =6; 
	public static  double p = 2.0/5; 
	public static  double r = 1.0/6; 
	public static Exercice1 ex1;
	public static Exercice2 ex2;
	public static double [] resolutionExacte() {
		double VecteurPi[]=new double[K+1];
		double som=0.0;
		VecteurPi[0] =1.0/3;
		
		for(int i=0; i<=K; i++) {
			//On utilise la formule etablie en td PI(n)=2exp(n)/3exp(n-1)
			VecteurPi[i] = Math.pow(2,i)/Math.pow(3,i+1);
		}
		
	 return VecteurPi;
	}

	
public static double [][] MatriceTransitionDrapeau(){
	double [][] P=new double[K+1][K+1];
		P[0][0] = r+(1-r)*(1-p); 
		P[0][1] = p*(1-r);
		P[K][K] = (1-r)*(1-p)+(1-r)*p; 
		P[K][0] = r;

		for(int i=1; i<K; i++) {
			P[i][0] = r;
			P[i][i] = (1-r)*(1-p); 
			P[i][i+1] = p*(1-r); 
		}
		return P;
	}

///Identique a celle de l'exo1 pas de modification
public static double[]resolutionPuissance(double[][] P, int nit, double epsilon ){
	
	double[] VecteurPi =new double[K+1];
	for(int i=0;i<VecteurPi.length;i++){
		VecteurPi[i]=1.0/VecteurPi.length;
	}
	int cpt=0;
	boolean critereConvergence = false;
	double[] VectPiNouv = new double[VecteurPi.length]; 
	

	while (cpt<nit && !critereConvergence){ 
		
		for(int j=0;j<VecteurPi.length;j++){
			 VectPiNouv[j] = 0;
			for(int i=0;i<VecteurPi.length;i++){
				VectPiNouv[j] += VecteurPi[i]*P[i][j];
			}
		}
		if (epsilon >= 0) {
			critereConvergence = true;
			for (int i=0;i<VecteurPi.length;i++){
				if ( Math.abs((double)(VectPiNouv[i]-VecteurPi[i])) > epsilon ) {
					critereConvergence = false;
					break;
				}
			}
		}	
		for(int i=0;i<VecteurPi.length;i++){
			VecteurPi[i] = VectPiNouv[i];
		}
		
		cpt++;
		
	}	
	System.out.println("nombre d'it�rations pour la convergence " +cpt);
	return VecteurPi;
}


////Identique a celle de l'exo2 pas de modification
public static double[] resolutionGaussSeidel(double[][] P, int nit, double epsilon ){
	double[] VectPI =new double[K+1];
	double som=0;
	for(int i=0;i<VectPI.length;i++){
		VectPI[i]=1.0/VectPI.length;
	}
	double som1, som2;
	int cpt=0;
	boolean critereConvergence = false;
	
	double[] CopyVectPi = new double[VectPI.length]; 
	for(int i=0;i<VectPI.length;i++){
		CopyVectPi[i] = VectPI[i];
	}
	
	while (cpt<nit && !critereConvergence){ 
		cpt++;
		som = 0;
	
		for (int j=0;j<VectPI.length;j++){
			som1 = 0;
			som2 = 0;
			for (int i=0;i<VectPI.length;i++) {
				if(i<j) {

				som1 += VectPI[i]*P[i][j];
			}
				if(i>j) {
			
				som2 += VectPI[i]*P[i][j];
			}
			}
			VectPI[j] = (1/(1-P[j][j]))*(som1+som2);
			som+=VectPI[j];
		}	
		for(int i=0;i<VectPI.length;i++){
			VectPI[i]/=som;
		}
		
		if (epsilon >= 0) { 
			critereConvergence = true;
			for (int i=0;i<VectPI.length;i++){
				if ( Math.abs((double)(VectPI[i]-CopyVectPi[i])) > epsilon ) {
					critereConvergence = false;
					break;
				}
		    }
		}
		for(int i=0;i<VectPI.length;i++){
			CopyVectPi[i] = VectPI[i];
		}
		cpt++;
	}
	System.out.println("nombre d'iterations pour la convergence " +cpt);

	return VectPI;
}





public void affi(double[] VectPI2){
	for(int i=0; i<=K; i++){
		System.out.println(VectPI2[i]);
	}
	
}




@SuppressWarnings("static-access")
public static void main(String[] args) throws IOException{
	PrintWriter ecrivain,ecrivain1,ecrivain2;
	String fic="C:/Users/foufi2012/workspace/IKHLEF_Projet3_LI323/chaine_infinie_exacte.txt";
	String fic2="C:/Users/foufi2012/workspace/IKHLEF_Projet3_LI323/infinie_gauss.txt";
	String fic1="C:/Users/foufi2012/workspace/IKHLEF_Projet3_LI323/methode_puissance.txt";
	Exercice1 ex1=new Exercice1();
	Exercice2 ex2=new Exercice2();
	Exercice3 ex3=new Exercice3();
	double[] v1 = new double[K+1];
	double[] v2 = new double[K+1];
	double[] v3 = new double[K+1];
	double[][] matriceP = new double[K+1][K+1];
	matriceP =MatriceTransitionDrapeau();
	ex1.afficherMatrice(matriceP);
	System.out.println();
	
	System.out.println("M�thode resolution exacte:");
	v3=resolutionExacte();
	ex3.affi(v3);
	System.out.println("Methode des Puissances :");
	v1=ex3.resolutionPuissance(matriceP, 70000,10E-5);
	ex3.affi(v1);
	System.out.println("Methode Gauss Seidel :");
	v2=ex3.resolutionGaussSeidel(matriceP, 70000,10E-5);
	ex3.affi(v2);
	ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(fic)));
	ecrivain1 =  new PrintWriter(new BufferedWriter(new FileWriter(fic1)));
	ecrivain2 =  new PrintWriter(new BufferedWriter(new FileWriter(fic2)));
    
	for(int a =0; a<v3.length;a++){
    ecrivain.println(a+" "+v3[a]);
    }
    for(int a =0; a<v1.length;a++){
        ecrivain1.println(a+" "+v1[a]);
        }
    for(int a =0; a<v2.length;a++){
        ecrivain2.println(a+" "+v2[a]);
        }
    ecrivain.close();
    ecrivain1.close();
    ecrivain2.close();
	}
}