package balhau.matematica;

import balhau.metastruct.Lista;
import balhau.utils.ArrayUtils;

/**
 * Classe com um conjunto de funções matemáticas utilitárias.
 * @author Balhau
 *
 */
public class MathUtils {
	public static double NUMMAGICO=(1+Math.sqrt(5))/2;
	public static long MASK32=0xFFFFFFFFL;
	public static long MAXPOSL=0x7FFFFFFFFFFFFFFFL;
	public static long MAXLONG=0xFFFFFFFFFFFFFFFFL;
	/**
	 * Método para determinação do máximo divisor comum
	 * @param a {@link int} valor a 
	 * @param b {@link int} valor b
	 * @return {@link int} máximo divisor comum entre a e b
	 */
	public static int mdc(int a,int b){
		int max=Math.max(a, b);
		int min=Math.min(a,b);
		int resto;
		if(a==b)
			return a;
		resto=min;
		while(resto!=0){
			min=resto;
			resto=max%min;
			max=min;
		}
		return min;
	}
	
	public static int NDig(int a){
		int i=1;
		while((a=a/10)!=0)
			i++;
		return i;
	}
	/**
	 * Devolve o bit mais significativo. Retorna 0 caso o valor seja positivo ou
	 * 1 caso seja negativo
	 * @param a {@link int} 
	 * @return {@link int} 0 se a é positivo, 1 se a é negativo
	 */
	public static int MSB(int a){
		return a>>>31;
	}
	/**
	 * Devolve o unsigned mod para a mod b
	 * @param a {@link int} 
	 * @param b {@link int} 
	 * @return a mod b
	 */
	public static int UMod(int a,int b){
		return ((a>>>31)==1)?b-((~a)%b):a%b;
	}
	
	public static String IntToBinaryString(int a){
		String out="";
		int aux=a;
		if(a==0)
			return "0";
		while(aux!=0){
			out=(aux&1)+out;
			aux=(aux>>>1);
		}
		return out;
	}
	/**
	 * Extensão do algoritmo de Euclides para inteiros
	 * @param a {@link Integer} inteiro a
	 * @param b {@link Integer} inteiro b
	 * @return {@link EERes} Objecto que representa o resultado para a aplicação da extensão
	 * do algoritmo de Euclides
	 */
	public static EERes EMdc(int a,int b){
		int r1,r2,aux,x0,x1,y0,y1,div;
		r1=Math.max(a, b);
		r2=Math.min(a, b);
		x0=1;x1=0;y0=0;y1=1;
		while(r2!=0){
			//actualização do resto
			div=r1/r2;
			aux=r2;
			r2=r1-div*r2;
			r1=aux;
			//actualização do xi
			aux=x1;
			x1=x0-div*x1;
			x0=aux;
			//actualização do yi
			aux=y1;
			y1=y0-div*y1;
			y0=aux;
//			System.out.println("x0:"+x0);
//			System.out.println("x1:"+x1);
//			System.out.println("y0:"+y0);
//			System.out.println("y1:"+y1);
		}
		return new EERes(x0, y0, r1);
	}
	/**
	 * Tangente hiperbólica de um número
	 * @param a {@link double} valor para o qual se pretende computar a tangente hiperbólica
	 * @return  {@link double} valor da tangente hiperbólica 
	 */
	public static double tanh(double a){
		double n=Math.exp(a);
		double d=Math.exp(-a);
		return ((n-d)/(n+d));
	}
	
	public static String HEX="0123456789ABCDEF";
	
	/**
	 * Método que devolve uma String representando o valor hexadecimal do {@link int} fornecido como
	 * parâmetro de entrada
	 * @param bt {@link int} inteiro para o qual se pretende obter o valor hexadecimal
	 * @return {@link String} com a representação do inteiro em hexadecimal 
	 */
	public static String hexValue(int bt){
		int a=bt;
		String hx="";
		while(a!=0){
			hx=HEX.charAt((int)a&15)+hx;
			a=a>>>4;
		}
		if(bt==0)
			return "0";
		return hx;
	}
	/**
	 * Método que converte um inteiro de 64 bits para a sua representação em {@link String}
	 * @param bt {@link Long} inteiro de 64 bits
	 * @return {@link String} representação em {@link String}
	 */
	public static String hexValue(long bt){
		long a=Math.abs(bt);
		int aux;
		String hx="";
		while(a!=0){
			aux=(int)a&15;
			hx=HEX.charAt(aux)+hx;
			a=a>>4;
		}
		if(bt==0)
			return "0";
		if(bt<0)
			return "-"+hx;
		return hx;
	}
	
	/**
	 * Converte um valor de byte para o seu valor em representação Hexadecimal
	 * @param bt {@link byte} a representar
	 * @return {@link String} com a representação do byte
	 */
	public static String hexValue(byte bt){
		if((int)bt>=0)
			return hexValue((int)bt);
		else
			return hexValue((int)bt+256);
	}
	
	/**
	 * Método que verifica se um número é primo
	 * @param num {@link int} Número para o qual se pretende verificar a primalidade
	 * @return {@link boolean} Verdadeiro caso o número seja primo, falso caso contrário
	 */
	public static boolean isPrime(int num){
		for(int i=0;i<=Math.ceil(Math.sqrt(num));i++){
			if(num%i!=0)
				return false;
		}
		return true;
	}
	
	/**
	 * Método que devolve o mínimo múltiplo comum entre dois números
	 * @param a Valor inteiro {@link int} 
	 * @param b Valor inteiro {@link int}
	 * @return Valor inteiro {@link int} representando o mínimo múltiplo comum entre os dois números anteriores
	 */
	public static int mmc(int a,int b){
		int max=a>b ? a : b;
		int min=b<= a ? b : a;
		int aux=min;
		if(max%min==0)
			return max;
		while(aux%max!=0){
			aux+=min;
		}
		return aux;
	}
	/**
	 * Método que converte um {@link byte} para o seu correspondente valor em {@link int} 
	 * @param bt {@link byte} que se pretende proceder à conversão
	 * @return {@link int} com o valor presente no {@link byte}
	 */
	public static int byteToIntValue(byte bt){
		if((int)bt>=0)
			return (int)bt;
		else
			return (int)bt+256;
	}
	/**
	 * Método que verifica se um dado número inteiro é primo
	 * @param a {@link Integer} valor para o qual se pretende verificar a primalidade
	 * @return {@link Boolean} valor verdadeiro caso o número seja primo, falso caso contrário
	 */
	public static boolean Primo(long a){
		int raiz=(int)Math.ceil(Math.sqrt(a));
		if(a==1 || a==0 || a==2 || a==3)
			return true;
		if(a%2==0 && a!=2)
			return false;
		for(int i=3;i<=raiz;i+=2){
			if((a%i)==0)
				return false;
		}
		return true;
	}
	/**
	 * Método que devolve um array de inteiros com os N primeiros primos
	 * @param n {@link Integer} número máximo de primos
	 * @return {@link long[]} Array de inteiros
	 */
	public static long[] PrimeirosNPrimos(int n){
		long[] lsta=new long[n];
		int k=3;
		boolean primo;
		long iter=3;
		if(n==1){
			lsta[0]=1;
			return lsta;
		}
		if(n==2){
			lsta[0]=1;
			lsta[1]=2;
			return lsta;
		}
		lsta[0]=1;
		lsta[1]=2;
		lsta[2]=3;
		while(k<n){
			iter+=2;
			primo=true;
			for(int i=1;i<k;i++){
				if((iter%lsta[i])==0)
					primo=false;
			}
			if(primo){
				lsta[k]=iter;
				k++;
			}
		}
		return lsta;
	}
	/**
	 * First prime numbers decimal cube root
	 * @param n Número de primos a avaliar
	 * @return
	 */
	public static int[] fpndcr(int n){
		int[] pn=ArrayUtils.ArrayLongToInt(PrimeirosNPrimos(n+1));
		int[] out=new int[n];
		double sqcr,fl;
		for(int i=1;i<=n;i++){
			sqcr=Math.pow(pn[i], 1.0/3);
			fl=sqcr-Math.floor(sqcr);
			fl=fl*Math.pow(2,32);
			
			out[i-1]=(int)(long)fl;
		}
		return out;
	}
	/**
	 * First prime numbers decimal root 64 bit number
	 * @param n {@link int} Inteiro a representar o número de primos 
	 * @return {@link long[]} Array de números de 64 bits.
	 */
	public static long[] fpndrl(int n){
		int[] pn=ArrayUtils.ArrayLongToInt(PrimeirosNPrimos(n+1));
		long[] out=new long[n];
		double sqcr,fl;
		for(int i=1;i<=n;i++){
			sqcr=Math.sqrt(pn[i]);
			fl=sqcr-Math.floor(sqcr);
			fl=fl*Math.pow(2,64);
			if(fl>MAXPOSL)
				fl=fl-(double)MAXPOSL;
			out[i-1]=(long)fl;
		}
		return out;
	}
	/**
	 * Distância de Hamming, número de bits que diferem entre o número a e o número b
	 * @param a {@link int}
	 * @param b {@link int}
	 * @return int Número de bits que diferem nos dois números.
	 */
	public static int HammingDistance(int a,int b){
		int aux,bux;
		aux=a;
		bux=b;
		int ndiff=0;
		while(aux!=0 || bux!=0){
			if((aux&1)!=(bux&1))
				ndiff++;
			aux=aux>>1;
			bux=bux>>1;
		}
		return ndiff;
	}
	/**
	 * Distância de Hamming, número de bits que diferem entre o número a e o número b
	 * @param a {@link long}
	 * @param b {@link long}
	 * @return int Número de bits que diferem nos dois números.
	 */
	public static int HammingDistance(long a,long b){
		long aux,bux;
		aux=a;
		bux=b;
		int ndiff=0;
		while(aux!=0 || bux!=0){
			if((aux&1)!=(bux&1))
				ndiff++;
			aux=aux>>1;
			bux=bux>>1;
		}
		return ndiff;
	}
	/**
	 * Distância de Hamming, número de bits que diferem entre o número a e o número b
	 * @param a {@link short}
	 * @param b {@link short}
	 * @return int Número de bits que diferem nos dois números.
	 */
	public static int HammingDistance(short a,short b){
		short aux,bux;
		aux=a;
		bux=b;
		int ndiff=0;
		while(aux!=0 || bux!=0){
			if((aux&1)!=(bux&1))
				ndiff++;
			aux=(short)(aux>>1);
			bux=(short)(bux>>1);
		}
		return ndiff;
	} 
	/**
	 * Método que nos devolve o número de inteiros relativamente primos com o parâmetro de entrada
	 * @param n {@link int}
	 * @return {@link int}
	 */
	public static int EulerPhi(int n){
		int val;
		int phi=0; 
		if(n<0)
			val=-n;
		else
			val=n;
		for(int i=1;i<val;i++){
			if(mdc(val, i)==1)
				phi++;
		}
		return phi;
	}
	/**
	 * Devolve um array com todos os factores de um determinado inteiro
	 * @param a {@link int} Inteiro
	 * @return {@link int[]} Array de inteiros, factores de a
	 */
	public static int[] Factores(int a){
		Lista<Integer> lf=new Lista<Integer>();
		lf.addValue(1);
		for(int i=2;i<a;i++){
			if(a%i==0)
				lf.addValue(i);
		}
		return ArrayUtils.ArrayObjectToInt(lf.toArray());
	}
	/**
	 * Método que devolve um valor booleano, verdadeiro caso o número inteiro seja
	 * "abundante", falso caso contrário
	 * @param val {@link int} Valor inteiro
	 * @return {@link boolean} valor booleano
	 */
	public static boolean Abundante(int val){
		int[] fact=MathUtils.Factores(val);
		int sum=0;
		for(int i=0;i<fact.length;i++){
			sum+=fact[i];
		}
		if(sum>val)
			return true;
		return false;
	}
	/**
	 * Método que devolve os primeiros inteiros abuntantes
	 * @param n {@link int} o valor dos primeiros inteiros abundantes
	 * @return {@link int[]} array de inteiros contendo os abundantes
	 */
	public static int[] getFirstAbundant(int n){
		Lista<Integer> ls=new Lista<Integer>();
		int k=0;
		int s=1;
		while(k<n){
			if(MathUtils.Abundante(s)){
				ls.addValue(s);
				k++;
			}
			s++;
		}
		return ArrayUtils.ArrayObjectToInt(ls.toArray());
	}
}
