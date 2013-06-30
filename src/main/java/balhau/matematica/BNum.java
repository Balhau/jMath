/**
 * Este pacote contem um conjunto de classes que auxiliam o computo de calculos matemáticos
 */
package balhau.matematica;

import balhau.utils.ArrayUtils;
import balhau.utils.Mask;

/**
 * Objecto que irá representar um dígito na implementação de aritmética para inteiros de dimensões
 * não suportadas pela arquitectura do computador. BNum é a abreviatura de BigNumber.
 * A implementação da aritmética de precisão múltipla foi inspirada na libraria
 * LibTomMath existente em C. 
 * @author Balhau
 *
 */
public class BNum {
	// Propriedades privadas que são implementadas tendo em conta
	// a estrutura mp_int da libraria LibTomMath
	private static int COMBA_DIM=256;
	private int digitos[];
	public int usados;
	private int alocados;
	public int sinal;
	private static int MIN_DIGITOS=28;
	public static int Z_POS=1;
	public static int Z_NEG=-1;
	public static int Z_MAIOR=1;
	public static int Z_IGUAL=0;
	public static int Z_MENOR=-1;
	//Base aritmética
	public static int N_BASE=28;
	public static int C_BIN2DEC=3;
	public static long MAX_VAL=(long)Math.pow(2, N_BASE);
	public static int base=Mask.MASK28;
	private static String _hex="0123456789ABCDEF";
	//Multiplication cuttofs
	private static int KARATSUBA_MUL_CUTOFF = 128;
	private static int TOOM_MUL_CUTOFF = 129;
	//Squaring cuttofs
//	private static int KARATSUBA_SQR_CUTOFF=128;
//	private static int TOOM_SQR_CUTOFF=186;
	

	/**
	 * construtor do big número a partir de uma {@link String}
	 * @param num
	 */
	public BNum(String num){
		
	}
	/**
	 * Construtor do objecto com especificação dos coeficientes do número
	 * @param dig {@link int[]} Array com os coeficientes
	 */
	public BNum(int[] dig){
		digitos=dig.clone();
		usados=dig.length;
		alocados=usados;
		sinal=Z_POS;
	}
	
	/**
	 * Construtor do objecto. Este construtor permite especificar o número de dígitos que se pretende
	 * alocar para o BNum
	 * @param ndig {@link int} ndig Número de dígitos que irá compor o novo BNum
	 */
	public BNum(int ndig){
		//ajusta o número de dígitos para um múltiplo de MIN_DIGITOS
		if(ndig==0)
			ndig=1;
		int ndigitos=ndig>MIN_DIGITOS?(MIN_DIGITOS*(ndig/MIN_DIGITOS)+MIN_DIGITOS):MIN_DIGITOS;
		digitos=new int[ndigitos];
		for(int i=0;i<ndigitos;i++){
			digitos[i]=0;
		}
		this.usados=1;
		this.sinal=Z_POS;
		this.alocados=ndigitos;
	}
	
	/**
	 * Construtor vazio do objecto.
	 * Devolve um número cujo valor é zero
	 */
	public BNum(){
		digitos=new int[MIN_DIGITOS];
		for(int i=0;i<MIN_DIGITOS;i++){
			digitos[i]=0;
		}
		this.usados=1;
		this.sinal=Z_POS;
		this.alocados=MIN_DIGITOS;
	}
	
	/**
	 * Subtracção "unsigned" de dois {@link BNum} A, B. Assume-se que |A|>=|B| para o algoritmo
	 * funcionar sem problemas
	 * @param a {@link BNum} BigNum A
	 * @param b {@link BNum} BigNum B
	 * @return {@link BNum} C= |A|-|B|
	 */
	public static BNum usub(BNum a,BNum b){
		BNum c=new BNum();
		int min=b.usados;
		int max=a.usados;
		if(c.alocados<max)
			c.expande(max);
		int auxu=c.usados;
		int aux=0;
		c.usados=max;
		//efectua a subtracção 
		for(int i=0;i<min;i++){
			c.digitos[i]=a.digitos[i]-b.digitos[i]-aux;
			aux=MathUtils.MSB(c.digitos[i]);
			c.digitos[i]=MathUtils.UMod(c.digitos[i], base);
		}
		for(int i=min;i<max;i++){
			c.digitos[i]=a.digitos[i]-aux;
			aux=MathUtils.MSB(c.digitos[i]);
			c.digitos[i]=MathUtils.UMod(c.digitos[i], base);
		}
		for(int i=max;i<auxu;i++){
			c.digitos[i]=0;
		}
		c.trim();
		return c;
	}
	
	/**
	 * Método que efectua um clone do número
	 * @return {@link BNum} Número de precisão múltipla
	 */
	public BNum clone(){
		BNum cl=new BNum();
		cl.digitos=this.digitos.clone();
		cl.sinal=this.sinal;
		cl.alocados=this.alocados;
		cl.usados=this.usados;
		return cl;
	}
	
	public void copyFrom(BNum numA){
		if(this.alocados< numA.usados)
			this.expande(numA.usados);
		for(int i=0;i<numA.usados;i++)//copia os digitos do numero
			this.digitos[i]=numA.digitos[i];
		for(int i=numA.usados;i<this.alocados;i++)//coloca a zero os digitos restantes
			this.digitos[i]=0;
		this.usados=numA.usados;
		this.sinal=numA.sinal;
	}
	
	public String toDescString(){
		String str="";
		str+="Hex: "+this.toHexString()+"\n";
		str+="Coef: "+this.toCoefArray()+"\n";
		str+="Sign: "+this.sinal+"\n";
		str+="Used: "+this.usados+"\n";
		return str;
	}
	/**
	 * Método que coloca o número a zero
	 */
	public void Zero(){
		this.digitos=new int[MIN_DIGITOS];
		for(int i=0;i<MIN_DIGITOS;i++){
			this.digitos[i]=0;
		}
		this.sinal=Z_POS;
		this.alocados=MIN_DIGITOS;
		this.usados=1;
	}
	
	/***
	 * Método que devolve o simétrico do BNum considerado
	 * @param numA {@link BNum} BigNumber A
	 * @return {@link BNum} -A
	 */
	public static BNum neg(BNum numA){
		if(numA.usados==0)
			return new BNum();
		BNum out=numA.clone();
		if(numA.sinal==Z_POS)
			out.sinal=Z_NEG;
		else
			out.sinal=Z_POS;
		return out;
	}
	
	/**
	 * Devolve o número de bits usados pelo {@link BNum} 
	 * @return {@link int} representa o número de bits que o número ocupa
	 */
	public int getNumBits(){
		return this.usados*N_BASE;
	}
	
	/**
	 * Este método atribui um valor inteiro ao objecto BNum
	 * @param snum {@link int} Inteiro a
	 */
	public void set(int snum){
		if(snum==0){
			this.usados=0;
			this.sinal=Z_POS;
			this.digitos[0]=0;
		}
		else{
			if(snum>0)
				this.sinal=Z_POS;
			else
				this.sinal=Z_NEG;
			this.usados=1;
			this.digitos[0]=snum;
		}
	}
	
	public int compare(BNum numA){
		if(this.sinal==Z_POS && numA.sinal==Z_NEG)
			return Z_MAIOR;
		if(this.sinal==Z_NEG && numA.sinal==Z_POS)
			return Z_MENOR;
		if(this.sinal==Z_NEG){
			if(this.usados<numA.usados)
				return Z_MAIOR;
			if(this.usados>numA.usados)
				return Z_MENOR;
			for(int i=this.usados-1;i>=0;i--){
				if(this.digitos[i]>numA.digitos[i])
					return Z_MENOR;
				if(this.digitos[i]<numA.digitos[i])
					return Z_MAIOR;
			}
		}
		if(this.sinal==Z_POS){
			if(this.usados>numA.usados)
				return Z_MAIOR;
			if(this.usados<numA.usados)
				return Z_MENOR;
			for(int i=this.usados-1;i>=0;i--){
				if(this.digitos[i]>numA.digitos[i])
					return Z_MAIOR;
				if(this.digitos[i]<numA.digitos[i])
					return Z_MENOR;
			}
		}
		return Z_IGUAL;
	}
	
	/**
	 * Método que expande o número de dígitos alocados no array de inteiros
	 * @param ndig {@link int} número de dígitos que se pretende expandir
	 */
	private void expande(int ndig){
		if(this.alocados>=ndig)
			return;
		this.alocados=ndig+2*MIN_DIGITOS-(ndig%MIN_DIGITOS);
		int[] arrdig=new int[this.alocados];
		for(int i=0;i<this.usados;i++){
			arrdig[i]=this.digitos[i];
		}
		for(int i=this.usados;i<this.alocados;i++){
			arrdig[i]=0;
		}
		this.digitos=arrdig;
	}
	/**
	 * Devolve o valor de {@link BNum} multiplicado por 2 elevado a uma potencia {@link int} b
	 * @param power {@link int} Potência b
	 * @return {@link BNum} 
	 */
	public static BNum MPowerOf2(BNum a,int power){
		BNum out=a.clone();
		int nalloc=a.usados+power/N_BASE+1;
		if(out.alocados<nalloc)
			out.expande(nalloc);
		//caso a potencia seja superior a N_BASE então opta-se por efectuar uma deslocamento de um dígito
		//ao invés de sucessivos shifts binários
		int nfullshifts=power/N_BASE;
		int nbinshifts=power%N_BASE;
		out=MultByPB(a, nfullshifts);
		if(out.alocados<nalloc)
			out.expande(nalloc);
		if(nbinshifts==0)//caso não hajam deslocamentos binários a efectuar retorna-se de imadiato o BNum
			return out;
		//caso contrário deveremos proceder a um deslocamento binário de nbinshifs unidades
		int aux=0;
		int carry=0;
		for(int i=0;i<out.usados;i++){
			carry=(out.digitos[i]>>(N_BASE-nbinshifts));
			out.digitos[i]=((out.digitos[i]<<nbinshifts)|aux)&base;
			aux=carry;
		}
		if(aux>0){
			out.digitos[out.usados]=aux;
			out.usados++;
		}
		out.trim();
		return out;
	}
	
	/**
	 * Método que efectua a divisão entre um {@link BNum} a e uma potência {@link int} power de 2.
	 * @param a {@link BNum} Número a que se pretende dividir
	 * @param power {@link int} Potência de 2 que irá dividir a
	 * @return {@link BNum} Número resultante da divisão
	 */
	public static BNum DPowerOf2(BNum a,int power){
		BNum out=a.clone();
		int nfullshifts=power/N_BASE;
		int bshifts=power%N_BASE;
		out=DivByPB(out, nfullshifts);
		if(bshifts==0)
			return out;
		int mask=(int) Math.pow(2,bshifts)-1;
		int carry=0;
		int aux=0;
		for(int i=out.usados-1;i>=0;i--){
			carry=out.digitos[i]&mask;
			out.digitos[i]=(out.digitos[i]>>bshifts)|((aux<<(N_BASE-bshifts))&base);
			aux=carry;
		}
		out.trim();
		return out;
	}
	
	/**
	 * Este método é responsável por eliminar os zeros à esquerda presentes no {@link BNum}  
	 */
	private void trim(){
		while(this.usados>0 && this.digitos[this.usados-1]==0){
			this.usados--;
		}
		//Se o valor é zero então sinal deve passar a positivo
		if(this.usados==0){
			this.sinal=Z_POS;
		}
	}
	/**
	 * Método que efectua o computo de a mod (2^power)
	 * @param a {@link BNum} Grande número
	 * @param power {@link int} Potência de 2
	 * @return {@link BNum} a mod (2^power)
	 */
	public static BNum mod2Power(BNum a,int power){
		BNum out=a.clone();
		if(power<=0){//caso em que o expoente é negativo
			out.Zero();
			return out;
		}
		//caso o expoente seja positivo 
		if(power>out.usados*N_BASE)//quando o expoente garante que o 2^power > out
			return out;
		int ndig=(power%N_BASE==0)?(power/N_BASE):(power/N_BASE+1);//equivalente ao math.ceil
		for(int i=ndig;i<a.usados;i++){//os digitos superiores a 2^power devem ficar a zero
			out.digitos[i]=0;
		}
		//só deve ser actualizado o dígito que coincide com o  math.floor(power/N_BASE)
		int ix=power/N_BASE;
		out.digitos[ix]&=(1<<(power%N_BASE))-1;//efectua um a AND 2^power-1 <=> a mod 2^power
		out.trim();
		return out;
	}
	
	/**
	 * Efectua a multiplicação de um BNum a pelo valor 2
	 * @param a {@link BNum} Valor BNum a multiplicar por 2
	 * @return Valor {@link BNum} out resultante da multiplicação de a por 2
	 */
	public static BNum MultBy2(BNum a){
		BNum out=a.clone();
		if(out.alocados<a.usados+1){
			out.expande(a.usados+1);
		} 
		int lsv=0;		//último dígito binário do valor representado
		int aux=0;
		for(int i=0;i<a.usados;i++){//multiplica índice por índice
			lsv=(a.digitos[i]>>(N_BASE-1));
			out.digitos[i]=((a.digitos[i]<<1)|aux)&base;
			aux=lsv;
		}
		if(lsv!=0){//caso em que há um novo dígito a acrescentar
			out.digitos[out.usados]=1;
			out.usados++;
		}
		return out;
	}
	
	/**
	 * Método que efectua a multiplicação de {@link BNum} a por uma potência {@link int} power da base.
	 * Este método aplica um deslocamento de dígitos igual ao valor especificado no expoente
	 * @param a {@link BNum} Valor que se pretende multiplicar
	 * @param power {@link int} Valor do expoente ao pelo qual se pretende multiplicar a
	 * @return {@link BNum} out = a x base^power 
	 */
	public static BNum MultByPB(BNum a,int power){
		BNum out=a.clone();
		if(out.alocados<out.usados+power)//caso não hajam dígitos alocados suficientes expande
			out.expande(out.usados+power);
		out.usados=a.usados+power;
		for(int i=out.usados-1;i>=power;i--){//copia os dígitos de a deslocados power posições
			out.digitos[i]=a.digitos[i-power];
		}
		for(int i=power-1;i>=0;i--){//Coloca os restantes dígitos a zero
			out.digitos[i]=0;
		}
		return out;
	}
	
	/**
	 * Método que efectua a divisão de {@link BNum} a por uma potência {@link int} power da base. Este
	 * método aplica um deslocamento de dígitos igual ao valor especificado no expoente.
	 * @param a {@link BNum} Valor que se pretende dividir
	 * @param power {@link int} Valor do expoente pelo qual se pretende elevar o inverso da base
	 * @return {@link BNum} out=a/(base^power)
	 */
	public static BNum DivByPB(BNum a,int power){
		if(power>=a.usados){
			return new BNum();
		}
		BNum out=new BNum(a.usados-power);
		for(int i=0;i<a.usados-power;i++){
			out.digitos[i]=a.digitos[i+power];
		}
		out.usados=a.usados-power;
		return out;
	}
	/**
	 * Devolve um BNUm com n coeficientes, equivale a um número aleatório compreendido entre Base^n-1 e Base^n
	 * @param n {@link int} Numero de coeficientes do BNum
	 * @return {@link BNum} um BNum aleatório com n coeficientes
	 */
	public static BNum rand(int n){
		return new BNum(ArrayUtils.getIntMaskedRand(n, base));
	}
	
	/**
	 * Método que retorna a representação decimal para o {@link BNum} em questão
	 * @return {@link String} Representação decimal, em string, do {@link BNum}
	 */
	public String toDecimalString(){
		String str="";
		return str;
	}
	
	/**
	 * Método que efectua a divisão de um {@link BNum} a pelo valor 2 
	 * @param a {@link BNum} que irá ser dividido por 2
	 * @return {@link BNum} out que representa a divisão de a por 2
	 */
	public static BNum DivBy2(BNum a){
		BNum out=a.clone();
		int resto;
		int restoaux;
		resto=0;
		for(int i=a.usados-1;i>=0;i--){
			restoaux=a.digitos[i] % 2;
			out.digitos[i]=(a.digitos[i]>>1)|(resto<<N_BASE-1)&base;
			resto=restoaux;
		}
		if(out.digitos[out.usados-1]==0)
			out.usados--;
		return out;
	}
	/**
	 * Método estático que devolve o {@link BNum} que representa o valor absoluto de um dado {@link BNum}
	 * @param val {@link BNum} Número de precisão múltipla para o qual se pretende encontrar o valor
	 * absoluto
	 * @return {@link BNum} Número de precisão múltipla que representa o valor absoluto do valor de entrada
	 * do método
	 */
	public static BNum abs(BNum val){
		BNum cl=val.clone();
		cl.sinal=Z_POS;
		return cl;
	}
	
	/**
	 * Efectua a multiplicação modulo Base^digs entre dois grandes números
	 * @param A {@link BNum} Grande número
	 * @param B {@link BNum} Grande número
	 * @param digs {@link int} Número máximo de dígitos possíveis para a multiplicação 
	 * @return {@link BNum} Grande número C=|A| x |B| mod (BASE ^ digs)
	 */
	 public static BNum MulDig(BNum A,BNum B, int digs){
		int carry=0;
		long sum;
		if(Math.min(A.usados, B.usados)<COMBA_DIM)
			return CombMulDig(A, B, digs);
		int[] tdigs=new int[digs];
		for(int i=0;i<tdigs.length;i++){//coloca a zero todos os valores do array
			tdigs[i]=0;
		}
		BNum c=new BNum(digs);
		int pb;
		for(int i=0;i<A.usados;i++){
			sum=carry;
			carry=0;
			pb=Math.min(B.usados, digs-i);
			if(pb>=1){
				for(int j=0;j<pb;j++){
					sum=tdigs[i+j]+A.digitos[i]*B.digitos[j]+carry;
					tdigs[i+j]=(int)(sum%MAX_VAL);
					carry=(int)(sum/MAX_VAL);
				}
				if(i+pb<digs)//adiciona o carry ao último valor da 
					tdigs[i+pb]=carry;
			}else
				break;
		}
		c.usados=digs;
		for(int i=0;i<digs;i++){//coloca os dígitos do array temporário no objecto BNum
			c.digitos[i]=tdigs[i];
		}
		c.trim();
		return c;
	}
	
	private static int[] getCombaArray(){
		int[] carr=new int[COMBA_DIM];
		for(int i=0;i<COMBA_DIM;i++){
			carr[i]=0;
		}
		return carr;
	}
	/**
	 * Método que efectua a multiplicação de dois grandes números a partir do algoritmo
	 * Comba
	 * @param A {@link BNum} Grande número
	 * @param B {@link BNum} Grande número
	 * @param digs {@link int} Número de dígitos
	 * @return {@link BNum} Grande número
	 */
	public static BNum CombMulDig(BNum A,BNum B,int digs){
		BNum c=new BNum(digs);
		//Variável acumuladora
		long W=0;
		int[] carray=getCombaArray();//array de coeficientes com um comprimento máximo de 256 elementos 
		if(c.alocados<digs)
			c.expande(digs);
		int pa=Math.min(digs, A.usados+B.usados);
		int tx,ty,iy;
		for(int ix=0;ix<pa;ix++){
			ty=Math.min(B.usados-1, ix);
			tx=ix-ty;
			iy=Math.min(A.usados-tx, ty+1);
//			System.out.println("---------------------------------COLLUM("+ix+")---------------------------------");
			for(int z=0;z<iy;z++){
				W+=A.digitos[tx+z]*B.digitos[ty-z];
//				System.out.println("Val: ("+(tx+z)+","+(ty-z)+")="+W);
			}
			carray[ix]=(int)(W%MAX_VAL);
//			System.out.println("CARRAY: "+carray[ix]);
			W=W/MAX_VAL;
		}
		
		int oused=c.usados;
		c.usados=digs;
//		System.out.println("OUsados: "+oused);
//		System.out.println(c.toDescString());
		c.usados=digs;
		for(int i=0;i<=pa;i++){
			c.digitos[i]=carray[i];
//			System.out.println("D: "+c.digitos[i]);
		}
		for(int i=pa+1;i<oused;i++)
			c.digitos[i]=0;
		c.trim();
		return c;
	}
	
	/**
	 * Multiplicação pelo algoritmo de Karatsuba. Este algoritmo efectua o computo de
	 * <center>W(x)=f(x)*g(x)=w_2*x^2+w_1*x+w_0</center> 
	 * Onde
	 * <center>f(x)=a*x+b</center>
	 * <center>g(x)=c*x+d</center>
	 * O algoritmo utiliza o facto que 
	 * <center>f(x)*g(x)=acx²+(ad+bc)*x+bd</center>
	 * e ad+bc=(a+b)(c+d)-(ac+bd) para concluir
	 * <center>f(x)*g(x)=acx^2+((a+b)(c+d)-(ac+bd))*x+bd</center>
	 * Analogamente ao algoritmo Toom-Cook são calculados pontos, neste caso três.
	 * Que são utilizados no calculo de W(x)
	 * <ul>
	 * <li>W(0)=f(0)*g(0)=b*d=w_0</li>
	 * <li>W(1)=f(1)*g(1)=(a+b)(c+d)=w_2+w_1+w_0</li>
	 * <li>W(inf)=a*c=w_2</li>
	 * </ul>
	 * <br><br>
	 * Nota: O algoritmo de Karatsuba consiste no algoritmo de Toom Cook para k=2<br><br>
	 * @see MulToomCook
	 * @param A {@link BNum} Grande número
	 * @param B {@link BNum} Grande número
	 * @return {@link BNum} Grande número C=|A| x |B| 
	 */
	public static BNum MulKaratsuba(BNum A,BNum B){
		BNum x0,x1,y0,y1,x0y0,x1y1,t,out;
		int base=Math.min(A.usados,B.usados)/2;
		//Divisão dos números em partes
		x0=new BNum(base);
		y0=new BNum(base);
		x1=new BNum(A.usados-base);
		y1=new BNum(B.usados-base);
		for(int i=0;i<base;i++){
			x0.digitos[i]=A.digitos[i];
			y0.digitos[i]=B.digitos[i];
			x0.usados=base;
			y0.usados=base;
		}
		for(int i=base;i<A.usados;i++){
			x1.digitos[i-base]=A.digitos[i];
			x1.usados=A.usados-base;
		}
		for(int i=base;i<B.usados;i++){
			y1.digitos[i-base]=B.digitos[i];
			y1.usados=B.usados-base;
		}
		
		//Cálculo dos três produtos
		x0y0=BNum.MulDig(x0, y0, x0.usados+y0.usados+1);
		x1y1=BNum.MulDig(x1, y1, x1.usados+y1.usados+1);
		t=BNum.add(x0, x1);
		x0=BNum.add(y1,y0);
		t=BNum.MulDig(t, x0, t.usados+x0.usados+1);
		
		//Cálculo dos termos intermédios
		x0=BNum.add(x0y0, x1y1);
		t=BNum.sub(t, x0);
		
		//Cálculo do produto final
		t=BNum.MultByPB(t, base);
		x1y1=BNum.MultByPB(x1y1, 2*base);
		t=BNum.add(x0y0, t);
		out=BNum.add(t,x1y1);
		return out;
	}
	
	/**
	 * Efectua a multiplicação de dois {@link BNum} a partir do algoritmo de Toom-Cook
	 * com k=3.<br>
	 * O algoritmo computa:<br>
	 * <center>W(x) = f(x)*g(x)</center>
	 * <br><br> 
	 * Onde<br> 
	 * <center>f(x)=a_2*x²+a_1*x¹+a_0*x⁰<br>
	 * g(x)=b_2*x²+b¹*x^1+b_0*x⁰
	 * </center><br> 
	 * Computa<br>
	 * <center>W(x)=w_4*x⁴+w_3*x³+w_2*x²+w_1*x¹+w_0*x⁰</center><br>
	 * utilizando para tal os seguintes pontos:<br><br>
	 * <ul>
	 * <li>w(0)=f(0)*g(0)=a_0*b_0=w_0</li>
	 * <li>16*w(1/2)=16*f(1)*g(1)=16*(a_2/4+a_1/2+a_0)*(b_2/4+b_1/2+b_0)=w_4+w_3*2+w_2*4+w_1*8+w_0*16</li>
	 * <li>w(1)=f(1)*g(1)=(a_2+a_1+a_0)*(b_2+b_1+b_0)=w_4+w_3+w_2+w_1+w_0</li>
	 * <li>w(2)=f(2)*g(2)=()*()=16w_4+8w_3+4w_2+2w_1+w_0 </li>
	 * <li>w(inf)=a_2*b_2=w_4</li>
	 * </ul>
	 * <br><br>
	 * 
	 * @param A {@link BNum} Grande número
	 * @param B {@link BNum} Grande número
	 * @return {@link BNum} Produto dos dois grandes números
	 */
	public static BNum MulToomCook(BNum A, BNum B){
		//A=a_3*x³+a_2*x²+a_1*x+a_0 e B=b_3*x³+b_2*x²+b_1*x+b_0
		//Computo dos coeficientes de A
		int k=(int)Math.floor(Math.min(A.usados, B.usados)/3);
		BNum a_0=BNum.mod2Power(A,MIN_DIGITOS*k);	//a_0=A mod Ba^k
		BNum a_1=BNum.DivByPB(A, k);        		//a_1=A/Ba^k
		a_1=BNum.mod2Power(a_1, MIN_DIGITOS*k);		//a_1=a_1 mod Ba^k
		BNum a_2=BNum.DivByPB(A,k*2);				//a_2=A/Ba^(k*2)
		a_2=BNum.mod2Power(a_2, MIN_DIGITOS*k);		//a_2=a_2 mod Ba^k
		//Computo dos coeficientes de B
		BNum b_0=BNum.mod2Power(B,MIN_DIGITOS*k);	//b_0=B mod Ba^k
		BNum b_1=BNum.DivByPB(B, k);        		//b_1=B/Ba^k
		b_1=BNum.mod2Power(b_1, MIN_DIGITOS*k);		//b_1=b_1 mod Ba^k
		BNum b_2=BNum.DivByPB(B,k*2);				//b_2=B/Ba^(k*2)
		b_2=BNum.mod2Power(b_2, MIN_DIGITOS*k);		//b_2=b_2 mod Ba^k
		
		//Computo de w_0=w(0)
		BNum w_0=BNum.Multiplica(a_0, b_0);
		//Computo de w_4=w(infinito)
		BNum w_4=BNum.Multiplica(a_2, b_2);
		
		//Computo de w_1=16*w(1/2)
		BNum tmp1=BNum.MPowerOf2(a_0, 1);
		tmp1=BNum.add(a_1, tmp1);
		tmp1=BNum.MPowerOf2(tmp1, 1);
		tmp1=BNum.add(tmp1, a_2);
		BNum tmp2=BNum.MPowerOf2(b_0, 1);
		tmp2=BNum.add(b_1, tmp2);
		tmp2=BNum.MPowerOf2(tmp2, 1);
		tmp2=BNum.add(tmp2, b_2);
		BNum w_1=BNum.Multiplica(tmp1,tmp2);
		
		//Computo de w_2=w(1);
		tmp1=BNum.add(a_0, a_1);
		tmp1=BNum.add(a_2, tmp1);
		tmp2=BNum.add(b_0,b_1);
		tmp2=BNum.add(b_2, tmp2);
		BNum w_2=BNum.Multiplica(tmp1, tmp2);
		
		//Computo de w_3=w(2)
		tmp1=BNum.MPowerOf2(a_2, 1);
		tmp1=BNum.add(a_1, tmp1);
		tmp1=BNum.MPowerOf2(tmp1, 1);
		tmp1=BNum.add(a_0, tmp1);
		tmp2=BNum.MPowerOf2(b_2, 1);
		tmp2=BNum.add(b_1, tmp2);
		tmp2=BNum.MPowerOf2(tmp2, 1);
		tmp2=BNum.add(b_0, tmp2);
		BNum w_3=BNum.Multiplica(tmp1, tmp2);
		
		//Resolução das equações para isolar os coeficientes de W(x)
		w_1=BNum.sub(w_4, w_1);w_3=BNum.sub(w_3, w_0);
		w_1=BNum.DivBy2(w_1);w_3=BNum.DivBy2(w_3);
		w_2=BNum.sub(w_2, w_0);w_2=BNum.sub(w_2, w_4);
		w_1=BNum.sub(w_1, w_2);w_3=BNum.sub(w_3, w_2);
		tmp1=BNum.MPowerOf2(w_0, 3);w_1=BNum.sub(w_1, tmp1);
		tmp1=BNum.MPowerOf2(w_4, 3);w_3=BNum.sub(w_3,tmp1);
		w_2=BNum.Multiplica(a_2, 3);w_2=BNum.sub(w_2, w_1);w_2=BNum.sub(w_2,w_3);
		w_1=BNum.sub(w_1, w_2);w_3=BNum.sub(w_3, w_2);
		w_1=BNum.Divide(w_1, 3);w_3=BNum.Divide(w_3, 3);
		
		//Construir um BNum a partir dos quatro coeficientes
		BNum out;
		out=w_0.clone();
		out=BNum.add(BNum.MultByPB(w_1, 1), out);
		out=BNum.add(BNum.MultByPB(w_2, 2),out);
		out=BNum.add(BNum.MultByPB(w_3, 3), out);
		out=BNum.add(BNum.MultByPB(w_4,4),out);
		return out;
	}
	/**
	 * Método que efectua a multiplicação de um número inteiro (com menos de 28 bits) por um {@link BNum}
	 * @param A {@link BNum} A ser multiplicado
	 * @param b {@link int} Inteiro a multiplicar
	 * @return {@link BNum} Produto do BNum pelo inteiro 
	 */
	public static BNum Multiplica(BNum A,int b){
		return new BNum();
	}
	
	public static BNum Divide(BNum A, int b){
		return new BNum();
	}
	/**
	 * Método de multiplicação geral. Invoca os sub procedimentos de multiplicação Karatsuba, Toom Cook, e multiplicação base
	 * @param A {@link BNum} Grande número A
	 * @param B {@link BNum} Grande número B
	 * @return {@link BNum} C=A x B
	 */
	public static BNum Multiplica(BNum A, BNum B){
		BNum out=new BNum();
		int digs;
		int sinal;
		if(A.sinal==B.sinal)
			sinal=Z_MAIOR;
		else
			sinal=Z_MENOR;
		if(Math.min(A.usados, B.usados)>TOOM_MUL_CUTOFF)
			out= MulToomCook(A, B);
		else if (Math.min(A.usados, B.usados)>KARATSUBA_MUL_CUTOFF)
			out=MulKaratsuba(A, B);
		else{
			digs=A.usados+B.usados+1;
			out=MulDig(A, B, digs);
		}
		out.sinal=sinal;
		return out;
	}
	
	
	public int compareMagnitude(BNum C){
		if(this.usados>C.usados)
			return Z_MAIOR;
		if(this.usados<C.usados)
			return Z_MENOR;
		for(int i=this.usados-1;i>=0;i--){
			if(this.digitos[i]>C.digitos[i])
				return Z_MAIOR;
			if(this.digitos[i]<C.digitos[i])
				return Z_MENOR;
		}
		return Z_IGUAL;
	}
	
	/**
	 * Método estático que adiciona dois grandes números
	 * @param B {@link BNum} Grande número B
	 * @param C {@link BNum} Grande número C
	 * @return {@link BNum} Grande número D=B+C 
	 */
	public static BNum uadd(BNum B,BNum C){
		BNum soma=new BNum();
		long sum=0;
		long msi;
		int nd=Math.max(B.usados, C.usados);
		int min;
		int max;
		BNum aux;
		int resto=0;
		if(B.usados>C.usados){
			min=C.usados;
			max=B.usados;
			aux=B;
		}
		else{
			min=B.usados;
			max=C.usados;
			aux=C;
		}
		if(soma.alocados<nd+1)
			soma.expande(nd+1);
		soma.usados=max+1;
		for(int i=0;i<min;i++){//aqui adiciona os valores dos dois BNum
			sum=B.digitos[i]+C.digitos[i]+resto;
			msi=(sum>>N_BASE)&base;
			resto=(int)msi;
			soma.digitos[i]=(int)sum&base;
		}
		for(int i=min;i<max;i++){//aqui adiciona o resto do numero
			sum=aux.digitos[i]+resto;
			msi=(sum>>N_BASE)&base;//most significant integer
			resto=(int)msi;
			soma.digitos[i]=(int)(sum&base);
		}
		if(resto!=0){
			soma.digitos[max]=resto;
		}
		soma.trim();
		return soma;
	}
	
	/**
	 * Método de adição geral
	 * @param a {@link BNum} 
	 * @param b {@link BNum}
	 * @return {@link BNum} a+b 
	 */
	public static BNum add(BNum a,BNum b){
		BNum c;
		if(a.sinal==b.sinal){
			c=uadd(a, b);
			c.sinal=a.sinal;
			return c;
		}
		if(a.compareMagnitude(b)==BNum.Z_MENOR){
			c=usub(b, a);
			c.sinal=b.sinal;
			return c;
		}
		c=usub(a, b);
		c.sinal=a.sinal;
		return c;
	}
	
	/**
	 * Método que subtrai dois grandes números
	 * @param a {@link BNum} Grande número a
	 * @param b {@link BNum} Grande número b
	 * @return {@link BNum} Grande número C=a-b
	 */
	public static BNum sub(BNum a,BNum b){
		BNum c;
		int cmp;
		if(a.sinal!=b.sinal){
			c=uadd(a, b);
			c.sinal=a.sinal;
			return c;
		}
		cmp=a.compareMagnitude(b);
		if(cmp!=Z_MENOR){
			c=usub(a, b);
			c.sinal=a.sinal;
			return c;
		}
		c=usub(b, a);
		c.sinal=a.sinal==Z_NEG?Z_POS:Z_NEG;
		return c;
	}
	/**
	 * Método que efectua a operação A --> A² pelo método elementar. A ideia por trás deste algoritmo consiste no seguinte.
	 * Consideremos o valor A de coeficientes [4,3,2,1]. O quadrado deste valor será dado por
	 * <table>
	 * <tr><td></td><td></td><td></td><td>1</td><td>2</td><td>3</td><td>4</td></tr>
	 * <tr><td></td><td></td><td></td><td>1</td><td>2</td><td>3</td><td>4</td></tr>
	 * <tr><td></td><td></td><td></td><td>1*4</td><td>2*4</td><td>3*4</td><td>4*4</td></tr>
	 * <tr><td></td><td></td><td>1*3</td><td>2*3</td><td>3*3</td><td>4*3</td><td></td></tr>
	 * <tr><td></td><td>1*2</td><td>2*2</td><td>3*2</td><td>4*2</td><td></td></tr>
	 * <tr><td>1*1</td><td>2*1</td><td>3*1</td><td></td><td></td><td></td><td></td></tr>
	 * </table>
	 * 
	 * Da representação anterior seguem duas observações:
	 * <li>Na posição 2*i temos o valor v[i]²</li>
	 * Repare-se que out[0]=A[0]*A[0], out[2]=A[1]*A[1]+r_0,out[4]=A[2]+A[2]+r_1,out[6]=A[3]*A[3]+r_2
	 * <li>As colunas ímpares são constituidas unicamente por duplos produtos</li>
	 * <li>Para cara linha nova de produtos o unico termo novo é constituido pelo item na posição 2*k+1</li>
	 * @param A {@link BNum} Grande número A
	 * @return {@link BNum} Grande número C=A²
	 */
	public static BNum base_square(BNum A){
		BNum out=new BNum();
		out.expande(2*A.usados+1);
		long acum=0;
		long carry=0;
		int j;
		int i;
		for(i=0;i<A.usados;i++){
			acum=out.digitos[2*i]+A.digitos[i]*A.digitos[i];
			out.digitos[2*i]=(int)(acum % MAX_VAL);
			carry=(int)(acum / MAX_VAL);
			for(j=i+1;j<A.usados;j++){
				acum=2*A.digitos[i+j]+out.digitos[i+j]+carry;
				out.digitos[i+j]=(int)(acum % MAX_VAL);
				carry=(int)(acum/MAX_VAL);
			}
			while(out.digitos[j]>0){
				j++;
				acum=out.digitos[i+j]+carry;
				out.digitos[i+j]=(int)(acum % MAX_VAL);
				carry=(int)(acum/MAX_VAL);
			}
		}
		out.trim();
		return out;
	}
	
	
	/**
	 * Método que nos devolve a representação inteira em formato decimal do valor representado
	 * no {@link BNum} 
	 */
	public String toString(){
		return null;
	}
	/**
	 * Método que nos devolve uma string com os valores dos coeficientes do número
	 * @return {@link String} Descrição do array dos coeficientes
	 */
	public String toCoefArray(){
		String str="";
		for(int i=0;i<this.usados;i++){
			if(i!=0)
				str+=","+this.digitos[i];
			else
				str+=this.digitos[i];
		}
		return str;
	}
	/**
	 * Método que nos devolve a representação em formato hexadecimal do valor representado
	 * pelo {@link BNum}
	 * @return
	 */
	public String toHexString(){
		StringBuilder sb=new StringBuilder();
		if(this.usados==0)
			return DigToString(0);
		for(int i=this.usados-1;i>=0;i--){
			if(i!=this.usados-1)
				sb.append(":");
			sb.append(DigToString(this.digitos[i]));
		}
		return sb.toString();
	}
	/**
	 * Método que devolve a representação em string de um dígito
	 * @return String representando o valor de um digito em hexadecimal
	 */
	private String DigToString(int a){
		int groups=N_BASE/4;
		int aux=a;
		int ind;
		String out="";
		for(int i=0;i<groups;i++){
			ind=aux&0xF;
			out=_hex.charAt(ind)+out;
			aux=aux>>4;
		}
		return out;
	}
	
	public static BNum fromString(String val){
		BNum out=new BNum();
//		val=val.toLowerCase();
//		int formato=10; //10- decimal, 16- hexadecimal
//		int sup=(int)Math.pow(2, 28);
//		char dig;
//		int digv;
//		int k=0;
//		int digpos=0;
//		int sum=0;
//		int exp=0;
//		int mod=0;
//		dig=val.charAt(k);
//		if(dig=='h')
//			formato=16;
//		else if(dig=='-'){
//			formato=10;
//			out.sinal=Z_NEG;
//		}
//		k++;		
		return out;
	}
}
