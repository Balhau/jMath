package balhau.matematica;

import java.io.IOException;

import balhau.metastruct.Lista;

/**
 * Classe que implementa a aritmética de Grupos de Galois
 * Uma aplicação particular da aritmética de  Grupos de Galois é o mecanismo
 * de encriptação do actual AES.
 * @author Balhau
 *
 */
public class GGalois {
	private int[] _coeficientes;
	private int _coefordem;
	/**
	 * Construtor do grupo finito, com especificação do array de coeficientes
	 * @param coef Array {@link int[]} de coeficientes
	 * @param mod Ordem {@link int} do grupo finito.
	 */
	public GGalois(int[] coef, int mod){
		this._coeficientes=resizeCoefArray(coef,mod);
		this._coefordem=mod;
	}
	
	public GGalois(int mod){
		this._coeficientes=getNPol(0, 0,mod);
		this._coefordem=mod;
	}
	
	public boolean equals(GGalois gf){
		int[] gfcf=resizeCoefArray(gf.getCoeficientes(),gf.CoefMod());
		this._coeficientes=resizeCoefArray(this._coeficientes,this.CoefMod());
		if(gfcf.length!=this._coeficientes.length)
			return false;
		for(int i=0;i<this._coeficientes.length;i++){
			if(gfcf[i]!=this._coeficientes[i])
				return false;
		}
		return true;
	}
	

	/**
	 * Setter para a propriedade dos coeficientes
	 * @param coef {@link int[]} com os coeficientes do polinómio
	 */
	public void setCoeficientes(int[] coef){
		this._coeficientes=coef;
	}
	/**
	 * Getter para a propriedade dos coeficientes
	 * @return {@link int[]} com os coeficientes do polinómio
	 */
	public int[] getCoeficientes(){
		return this._coeficientes;
	}
	
	/**
	 * Getter para a ordem do polinómio
	 * @return {@link int} com a ordem do polinómio
	 */
	public int Ordem(){
		return this._coeficientes.length;
	}
	/**
	 * Getter para o módulo do polinómio 
	 * @return {@link int} Representando o valor para o módulo do polinómio
	 */
	public int CoefMod(){
		return this._coefordem;
	}
	
	/**
	 * Método estático responsável por efectuar a soma de dois grupos finitos
	 * @param ga Grupo {@link GGalois} A
	 * @param gb Grupo {@link GGalois} B
	 * @return {@link GGalois} C resultante da adição dos dois módulos anteriores
	 */
	public static GGalois soma(GGalois ga, GGalois gb){
		if(ga.CoefMod()!=gb.CoefMod()){
			return null;
		}
		
		GGalois gA;
		GGalois gB;
		//Identifica o polinómio de ordem superior
		if(ga.Ordem()>gb.Ordem()){
			gA=ga;
			gB=gb;
		}
		else{
			gA=gb;
			gB=ga;
		}
		int[] coefaux=new int[gA.Ordem()];
		int[] coefA=gA.getCoeficientes();
		int[] coefB=shiftArray(gB.getCoeficientes(), gA.Ordem()-gB.Ordem(), false);
		int mod=ga.CoefMod();
		for(int i=0;i<gA.Ordem();i++){
			coefaux[i]=(coefB[i]+coefA[i])%mod;
		}
		coefaux=resizeCoefArray(coefaux,ga.CoefMod());
		return new GGalois(coefaux, mod);
	}
	/**
	 * Subtracção de dois {@link GGalois}
	 * @param gfa {@link GGalois} A
	 * @param gfb {@link GGalois} B
	 * @return A diferença de A com B
	 */
	public static GGalois subtrai(GGalois gfa,GGalois gfb){
		if(gfa.CoefMod()!=gfb.CoefMod())
			return null;
		return GGalois.soma(gfa, gfb.simetrico());
	}
	
	public static GGalois getPolNulo(int coeford){
		return new GGalois(getNPol(0, 0,coeford),coeford);
	}
	
	/**
	 * Algoritmo de euclides para Grupos finitos 
	 * @param gfa Polinómio {@link GGalois} A
	 * @param gfb Polinómio {@link GGalois} B
	 * @return Polinómio que corresponde ao máximo divisor comum dos dois polinómios
	 */
	public static GGalois mdc(GGalois gfa,GGalois gfb){
		GGalois resto;
		GGalois aaux=gfa.clone();
		GGalois baux=gfb.clone();
		while(!Nulo(baux))
		{
			resto=GGalois.GFMod(aaux, baux);
			aaux=baux;
			baux=resto;
		}
		return aaux;
	}
	/**
	 * Determina o polinómio resultante da divisão de {@link GGalois} gfa pelo polinómio {@link GGalois} gfb
	 * @param gfa Polinómio {@link GGalois}
	 * @param gfb Polinómio {@link GGalois}
	 * @return {@link GGalois} polinómio resultante da divisão
	 */
	public static GGalois Div(GGalois gfa, GGalois gfb){
		if(gfa.CoefMod()!=gfb.CoefMod())
			return null;
		int caux;
		int i=0;
		int limit=5;
		GGalois aux=gfa.clone();
		//Debug.log(gfa);
		GGalois paux;
		int[] co={0};
		int[] co1={1};
		int coef;
		int coefmA;
		int coefmB;
		int mmcom;
		GGalois daux=new GGalois(co, gfa.CoefMod());
		caux=0;
		boolean orda=true;
		while(aux.Ordem()>=gfb.Ordem() && orda && i<limit){
			if(aux.Ordem()!=gfb.Ordem()){
				caux=CoefMax(aux.getCoeficientes())-CoefMax(gfb.getCoeficientes());
				if(caux!=0){
					coef=1;
					coefmA=aux.getCoeficientes()[0];
					coefmB=gfb.getCoeficientes()[0];
					mmcom=MathUtils.mmc(coefmA, coefmB);
					coef=mmcom/coefmB;
//					Debug.log("__________________________");
//					Debug.log("Coef: "+coef);
//					Debug.log("DauxA: "+daux);
//					Debug.log("PauxA: "+daux);
//					Debug.log("AuxA: "+aux);
					paux=new GGalois(getNPol(caux, gfb.getCoeficientes()[0]*coef,gfa.CoefMod()),gfa.CoefMod());
					daux=GGalois.soma(paux, daux);
					aux=GGalois.subtrai(GGalois.multiplica(paux, gfb), aux);
//					Debug.log("DauxB: "+daux);
//					Debug.log("AuxB: "+aux);
//					Debug.log("PauxB: "+paux);
//					Debug.log("CAUX: "+caux);
					
				}else{
					if(aux.Ordem()==1)
						orda=false;
					aux=GGalois.soma(aux,gfb);
					daux=GGalois.soma(new GGalois(co1, gfa.CoefMod()),daux);
				}
			}
			else{
				if(aux.Ordem()==1)
					orda=false;
				aux=GGalois.soma(aux,gfb);
				daux=GGalois.soma(new GGalois(co1, gfa.CoefMod()),daux);
			}
			i++;
		}
		return daux;
		
	}
	/**
	 * Implementação da extensão do algoritmo de euclides para polinómios
	 * @param gfa {@link GGalois} A(x)
	 * @param gfb {@link GGalois} B(x)
	 * @return {@link GFinito[]}, {@link GGalois} mdc(A,B)(x), {@link GGalois} s(x), {@link GGalois} t(x)
	 * tais que s(x)A(x)+t(x)B(x)=mdc(A,B)(x)
	 */
	public static GGalois[] EAEuclides(GGalois gfa,GGalois gfb){
		if(gfa.CoefMod()!=gfb.CoefMod())
			return null;
		GGalois gfaI;
		GGalois gfaIA1;
		GGalois gfaIA2;
		GGalois gfbI;
		GGalois gfbIA1;
		GGalois gfbIA2;
		GGalois gfaAux=gfa.clone();
		GGalois gfbAux=gfb.clone();
		GGalois gFQ;
		GGalois gResto=null;
		int cmod=gfa.CoefMod();
		int[] coef1;
		int[] coef2;
		GGalois[] out=new GGalois[3];
		if(Nulo(gfb)){
			coef1=new int[1];
			coef2=new int[1];
			coef1[0]=1;
			coef2[0]=0;
			out[0]=gfa.clone();
			out[1]=new GGalois(coef1, cmod);
			out[2]=new GGalois(coef2, cmod);
			return out;
		}
		coef1=new int[1];
		coef1[0]=0;
		coef2=new int[1];
		coef2[0]=1;
		gfaIA1=new GGalois(coef1.clone(), cmod);
		gfaIA2=new GGalois(coef2.clone(), cmod);
		gfbIA1=new GGalois(coef2.clone(), cmod);
		gfbIA2=new GGalois(coef1.clone(), cmod);
		while(!Nulo(gfbAux)){
			gFQ=GGalois.Div(gfaAux, gfbAux);
			gResto=GGalois.subtrai(gfaAux, GGalois.multiplica(gFQ, gfbAux));
			gfaI=GGalois.subtrai(gfaIA2, GGalois.multiplica(gFQ, gfaIA1));
			gfbI=GGalois.subtrai(gfbIA2, GGalois.multiplica(gFQ, gfbIA1));
			gfaAux=gfbAux;
			gfbAux=gResto;
			gfaIA2=gfaIA1;
			gfaIA1=gfaI;
			gfbIA2=gfbIA1;
			gfbIA1=gfbI;
		}
		gfaI=gfaIA2;
		gfbI=gfbIA2;
		out[0]=gfaAux;
		out[1]=gfaI;
		out[2]=gfbI;
		return out;
	}
	
	/**
	 * Método que devolve o polinómio {@link GGalois} simétrico
	 * @return {@link GGalois} simétrico
	 */
	public GGalois simetrico(){
		int[] coeb=this._coeficientes.clone();
		//Passa os coeficientes para o seu valor simétrico
		for(int i=0;i<coeb.length;i++){
			coeb[i]=this.CoefMod()-(coeb[i]%this.CoefMod());
		}
		return new GGalois(coeb, this.CoefMod());
	}
	
	public static boolean Nulo(GGalois gfa){
		for(int i=0;i<gfa.getCoeficientes().length;i++){
			if(gfa.getCoeficientes()[i]!=0)
				return false;
		}
		return true;
	}
	
	/**
	 * Método que efectua a multiplicação de dois polinómios
	 * @param ga {@link GGalois} GA
	 * @param gb {@link GGalois} GB
	 * @return Produto {@link GGalois} dos dois polinómios
	 */
	public static GGalois multiplica(GGalois ga,GGalois gb){
		
		GGalois gA,gB,gaux;
		if(ga.CoefMod()!=gb.CoefMod())
			return null;
		if(ga.Ordem()>gb.Ordem()){
			gA=ga;
			gB=gb;
		}
		else{
			gA=gb;
			gB=ga;
		}
		gB=new GGalois(shiftArray(gB.getCoeficientes(), gA.Ordem()-gB.Ordem(), false),gb.CoefMod());
		int[] coefprod=new int[gA.Ordem()+gB.Ordem()];
		for(int i=0;i<gA.Ordem();i++){
			for(int j=0;j<gB.Ordem();j++){
				coefprod[(gA.Ordem()-(i+1))+(gB.Ordem()-(j+1))+1]+=(gA.getCoeficientes()[gA.Ordem()-(i+1)]*gB.getCoeficientes()[gB.Ordem()-(j+1)])%gA.CoefMod();
				coefprod[(gA.Ordem()-(i+1))+(gB.Ordem()-(j+1))+1]=coefprod[(gA.Ordem()-(i+1))+(gB.Ordem()-(j+1))+1]%gb.CoefMod();
			}
		}
		//System.out.println(ArrayUtils.ArrayIntDesc(coefprod));
		gaux=new GGalois(resizeCoefArray(coefprod,ga.CoefMod()), ga.CoefMod());
		return gaux;
	}
	
	/**
	 * Método que efectua o módulo de dois polinómios
	 * @param gfa {@link GGalois} GA
	 * @param gfb {@link GGalois} GB
	 * @return {@link GGalois} GC
	 * @throws IOException 
	 */
	public static GGalois GFMod(GGalois gfa,GGalois gfb){
		if(gfa.CoefMod()!=gfb.CoefMod())
			return null;
		//Caso um deles seja de ordem unitaria devolver imediatamente o modulo
		GGalois una;
		int[] cuna=new int[1];
		if(gfa.Ordem()==1 && gfb.Ordem()==1)
		{
			cuna[0]=gfa.getCoeficientes()[0]%gfb.getCoeficientes()[0];
			una=new GGalois(cuna, gfb.CoefMod());
			return una;
		}
		else if(gfa.Ordem()==1 || gfb.Ordem()==1){
			if(gfa.Ordem()==1)
				cuna[0]=gfa.getCoeficientes()[0];
			else
				cuna[0]=gfb.getCoeficientes()[0];
			una=new GGalois(cuna, gfb.CoefMod());
			return una;
		}
		int caux;
//		int i=0;
		//int limit=10;
		int coef=0;
		int coefmA=0;
		int coefmB=0;
		GGalois aux=gfa.clone();
		GGalois paux;
		int mmcom;
		caux=0;
//		Debug.log("OA: "+aux.Ordem());
//		Debug.log("OB: "+gfb.Ordem());
		while(aux.Ordem()>=gfb.Ordem()){
			if(aux.Ordem()!=gfb.Ordem()){
				caux=CoefMax(aux.getCoeficientes())-CoefMax(gfb.getCoeficientes());
				if(caux!=0){
					coef=1;
					coefmA=aux.getCoeficientes()[0];
					coefmB=gfb.getCoeficientes()[0];
					mmcom=MathUtils.mmc(coefmA, coefmB);
					coef=mmcom/coefmB;
//					System.out.println("GFBMAx: "+gfb.getCoeficientes()[0]);
//					System.out.println("caux: "+caux);
//					System.out.println("Coef: "+ArrayUtils.ArrayIntDesc(getNPol(caux, gfb.getCoeficientes()[0]*coef,gfa.CoefMod())));
					paux=new GGalois(getNPol(caux, gfb.getCoeficientes()[0]*coef,gfa.CoefMod()),gfa.CoefMod());
//					System.out.println("__________________________");
//					System.out.println("coef: "+coef);
//					System.out.println("auxI: "+aux);
//					System.out.println("Paux: "+paux);
//					System.out.println("Gfb: "+gfb);
//					System.out.println("caux: "+caux);
					aux=GGalois.soma(GGalois.multiplica(paux, gfb), aux);
//					System.out.println("auxF: "+aux);
//					System.out.println("__________________________");
				}else{
//					System.out.println("entrouA");
					aux=GGalois.soma(aux,gfb);
				}
			}
			else{
//				System.out.println("entrouB");
				aux=GGalois.soma(aux,gfb);
			}
//			i++;
//			System.out.println("I"+i);
		}
		return aux;
	}
	
	/**
	 * Override do operador clone
	 * @return {@link GGalois}
	 */
	public GGalois clone(){
		return new GGalois(this._coeficientes.clone(), this._coefordem);
	}
	
	/**
	 * Devolve os coeficientes de um polinómio com 
	 * @param grau Grau do polinómio
	 * @param coef
	 * @return
	 */
	public static int[] getNPol(int grau,int coef,int modpol){
		int[] coefi=new int[grau+1];
		for(int i=0;i<=grau;i++){
			if(i==0)
				coefi[i]=coef%modpol;
			else
				coefi[i]=0;
		}
		return coefi;
	}
	
	/**
	 * Método que devolve o coeficiente máximo diferente de zero de um coeficientes de polinómios
	 * @param arrcoef {@link int[]} coeficientes do polinómio
	 * @return {@link int} coeficiente máximo
	 */
	private static int CoefMax(int[] arrcoef){
		for(int i=0;i<arrcoef.length;i++){
			if(arrcoef[i]!=0)
				return arrcoef.length-i;
		}
		return 0;
	}
	/**
	 * Método que devolve uma descrição do polinómio em forma de {@link String}
	 */
	public String toString(){
		String st="";
		int k=0;
		for(int i=0;i<this._coeficientes.length;i++){
			if(this._coeficientes[i]!=0){
				if(k!=0)
					st+="+";
				if(this._coeficientes[i]==1){
					if(i==this._coeficientes.length-1)
						st+=this._coeficientes[i];
				}
				else
					st+=this._coeficientes[i];
				if(i!=this._coeficientes.length-1){
					if(i==this._coeficientes.length-2)
						st+="x";
					else
						st+="x^"+(this._coeficientes.length-(i+1));
				}
				k++;
			}
			else{
				if(i==this._coeficientes.length-1){
					if(k!=0)
						st+="+";
					st+=this._coeficientes[this._coeficientes.length-1];
				}
			}
		}
		return st;
	}
	/**
	 * Método que efectua o resize do array de coeficientes
	 * @param arr {@link int[]} com os coeficientes
	 * @return {@link int[]} redimensionado
	 */
	public static int[] resizeCoefArray(int[] arr,int mod){
		int[] arrR;
		if(CoefMax(arr)==0){
			arrR=new int[1];
			arrR[0]=0;
			return arrR;
		}
		arrR=new int[CoefMax(arr)];
		for(int i=0;i<CoefMax(arr);i++){
			arrR[i]=arr[i+(arr.length-CoefMax(arr))]%mod;
		}
		return arrR;
	}
	/**
	 * Método que efectua um shift no array para a esquerda ou direita
	 * @param arr Array {@link int[]} que se pretende efectuar o shift
	 * @param shift {@link int} representando o deslocamento
	 * @param left {@link boolean} Representando a direcção do shift
	 */
	public static int[] shiftArray(int[] arr,int shift,boolean left){
		int[] aux=new int[arr.length+shift];
		for(int i=0;i<arr.length+shift;i++){
			if(left)
			{
				if(i>=arr.length)
					aux[i]=0;
				else
					aux[i]=arr[i];
			}
			else
			{
				if(i<shift)
					aux[i]=0;
				else
					aux[i]=arr[i-shift];
			}
		}
		return aux;
	}
	
	/**
	 * Método que converte os coeficientes do polinómio numa número inteiro a partir da seguinte fórmula:
	 * para o polinomio g(x)=ax³+bx²+cx+d com entradas em [0,p[. GF2ToInt devolve o valor a partir da seguinte
	 * fórmula a*p^3+bp^2+cp+d
	 * @return Inteiro dado a partir da fórmula anterior
	 */
	public int GF2ToInt(){
		int out=0;
		for(int i=0;i<this._coeficientes.length;i++){
			out+=this._coeficientes[i]*((int)Math.pow(this._coefordem,this._coeficientes.length-1-i));
		}
		return out;
	}
	/**
	 * Método que converte um valor inteiro para um array que o representa na base existente no presente
	 * {@link GGalois}
	 * @param value Valor {@link int} a converter
	 * @return {@link int[]} array de inteiros com a representação dos coeficientes na base presente no
	 * {@link GGalois} do actual valor inteiro
	 */
	public int[] IntToGF2(int value){
		Lista<Integer> lst=new Lista<Integer>();
		while(value!=0){
			lst.addValue(value%this._coefordem);
			value=(value-(value%this._coefordem))/this._coefordem;
		}
		int[] out=new int[lst.getNumElementos()];
		for(int i=0;i<lst.getNumElementos();i++){
			out[lst.getNumElementos()-i-1]=lst.getValor(i);
		}
		return out;
	}
	
	public void GF2FromInt(int value){
		this._coeficientes=this.IntToGF2(value);
	}
	
	public static GGalois GF2FromInt(int value,int ord){
		GGalois aux=new GGalois(ord);
		aux.GF2FromInt(value);
		return aux;
	}
}
