package balhau.matematica;
/**
 * Classe que implementa uma matriz 4x4 para funcionalidades de programação gráfica.
 * As funcionalidades aqui presentes também se encontram na classe mais genérica {@link Matriz}, no entanto esta encontra-e
 * optimizada para efectuar computações geométricas. 
 * @author balhau
 *
 */
public class Matriz4D {
	float _arrval[];
	static final int DIM=16;
	
	/**
	 * Construtor defeito do objecto. Os valores são todos inicalizados a nulo
	 */
	public Matriz4D(){
		this._arrval=new float[DIM];
		anula();
	}
	
	private void anula(){
		for(int i=0;i<16;i++)
			_arrval[i]=0;
	}
	
	public static Matriz4D Identidade(){
		Matriz4D mat=new Matriz4D();
		int col=0;
		for(int i=0;i<4;i++){
			mat._arrval[i*4]=0;
			mat._arrval[i*4+1]=0;
			mat._arrval[i*4+2]=0;
			mat._arrval[i*4+3]=0;
			mat._arrval[i*4+col]=1;
			col++;
		}
		return mat;
	}
	/**
	 * Multiplica o objecto matriz por um outro objecto matriz. O resultado da operação fica gravado no objecto
	 * que invoca o procedimento de multiplicação
	 * @param b {@link Matriz4D} Objecto matriz a multiplicar
	 */
	public void Multiplica(Matriz4D b){
		float a00=this._arrval[0];float a01=this._arrval[1];float a02=this._arrval[2];float a03=this._arrval[3];
		float a10=this._arrval[4];float a11=this._arrval[5];float a12=this._arrval[6];float a13=this._arrval[7];
		float a20=this._arrval[8];float a21=this._arrval[9];float a22=this._arrval[10];float a23=this._arrval[11];
		float a30=this._arrval[12];float a31=this._arrval[13];float a32=this._arrval[14];float a33=this._arrval[15];
		
		float b00=b._arrval[0];float b01=b._arrval[1];float b02=b._arrval[2];float b03=b._arrval[3];
		float b10=b._arrval[4];float b11=b._arrval[5];float b12=b._arrval[6];float b13=b._arrval[7];
		float b20=b._arrval[8];float b21=b._arrval[9];float b22=b._arrval[10];float b23=b._arrval[11];
		float b30=b._arrval[12];float b31=b._arrval[13];float b32=b._arrval[14];float b33=b._arrval[15];
		
		this._arrval[0]=a00*b00+a01*b10+a02*b20+a03*b30;
		this._arrval[1]=a00*b01+a01*b11+a02*b21+a03*b31;
		this._arrval[2]=a00*b02+a01*b12+a02*b22+a03*b32;
		this._arrval[3]=a00*b03+a01*b13+a02*b23+a03*b33;
		
		this._arrval[4]=a10*b00+a11*b10+a12*b21+a13*b30;
		this._arrval[5]=a10*b01+a11*b11+a12*b21+a13*b31;
		this._arrval[6]=a10*b02+a11*b12+a12*b22+a13*b32;
		this._arrval[7]=a10*b03+a11*b13+a12*b23+a13*b33;
		
		this._arrval[8]=a20*b00+a21*b10+a22*b20+a23*b30;
		this._arrval[9]=a20*b01+a21*b11+a22*b21+a23*b31;
		this._arrval[10]=a20*b02+a21*b12+a22*b22+a23*b32;
		this._arrval[11]=a20*b03+a21*b12+a22*b23+a23*b33;
		
		this._arrval[12]=a30*b00+a31*b10+a32*b20+a33*b30;
		this._arrval[13]=a30*b01+a31*b11+a32*b21+a33*b31;
		this._arrval[14]=a30*b02+a31*b12+a32*b22+a33*b32;
		this._arrval[15]=a30*b03+a31*b13+a32*b23+a33*b33;
	}
	
	public static Matriz4D perspectiva(float fov,float aspecto,float frustrum_near,float frustrum_far){
		Matriz4D mat=new Matriz4D();
		return mat;
	}
	
	public static Matriz4D frustrum(float esq,float dir, float bottom, float top, float near, float far){
		Matriz4D mat=new Matriz4D();
		
		return mat;
	}
		
	/**
	 * Efectua a multiplicação de duas matrizes devolvendo o valor num novo objecto
	 * Note que se a criação de objectos tem custo de performance. Caso seja necessário poupar processamento utilize
	 * antes o método <strong>public void Multiplica(Matriz4D a)</strong>
	 * Aqui utilizou-se a técnica de loop unroling e cache de variaveis para minimizar o número de computações e respectivo
	 * tempo de computação
	 * @param a {@link Matriz4D} Matriz A
	 * @param b {@link Matriz4D} Matriz B
	 * @return c {@link Matriz4D} Matriz C
	 */
	public static Matriz4D Multiplica(Matriz4D a, Matriz4D b){
		Matriz4D mat =new Matriz4D();
		float a00=a._arrval[0];float a01=a._arrval[1];float a02=a._arrval[2];float a03=a._arrval[3];
		float a10=a._arrval[4];float a11=a._arrval[5];float a12=a._arrval[6];float a13=a._arrval[7];
		float a20=a._arrval[8];float a21=a._arrval[9];float a22=a._arrval[10];float a23=a._arrval[11];
		float a30=a._arrval[12];float a31=a._arrval[13];float a32=a._arrval[14];float a33=a._arrval[15];
		
		float b00=b._arrval[0];float b01=b._arrval[1];float b02=b._arrval[2];float b03=b._arrval[3];
		float b10=b._arrval[4];float b11=b._arrval[5];float b12=b._arrval[6];float b13=b._arrval[7];
		float b20=b._arrval[8];float b21=b._arrval[9];float b22=b._arrval[10];float b23=b._arrval[11];
		float b30=b._arrval[12];float b31=b._arrval[13];float b32=b._arrval[14];float b33=b._arrval[15];
		
		mat._arrval[0]=a00*b00+a01*b10+a02*b20+a03*b30;
		mat._arrval[1]=a00*b01+a01*b11+a02*b21+a03*b31;
		mat._arrval[2]=a00*b02+a01*b12+a02*b22+a03*b32;
		mat._arrval[3]=a00*b03+a01*b13+a02*b23+a03*b33;
		
		mat._arrval[4]=a10*b00+a11*b10+a12*b21+a13*b30;
		mat._arrval[5]=a10*b01+a11*b11+a12*b21+a13*b31;
		mat._arrval[6]=a10*b02+a11*b12+a12*b22+a13*b32;
		mat._arrval[7]=a10*b03+a11*b13+a12*b23+a13*b33;
		
		mat._arrval[8]=a20*b00+a21*b10+a22*b20+a23*b30;
		mat._arrval[9]=a20*b01+a21*b11+a22*b21+a23*b31;
		mat._arrval[10]=a20*b02+a21*b12+a22*b22+a23*b32;
		mat._arrval[11]=a20*b03+a21*b12+a22*b23+a23*b33;
		
		mat._arrval[12]=a30*b00+a31*b10+a32*b20+a33*b30;
		mat._arrval[13]=a30*b01+a31*b11+a32*b21+a33*b31;
		mat._arrval[14]=a30*b02+a31*b12+a32*b22+a33*b32;
		mat._arrval[15]=a30*b03+a31*b13+a32*b23+a33*b33;
		
		return mat;
	}
	/**
	 * Especifica um valor na matriz
	 * @param i {@link int} Linha
	 * @param j {@link int} Coluna
	 * @param val {@link float} Valor a assinar
	 */
	public void setVal(int i, int j, float val){
		
	}
	/**
	 * Devolve um valor da matriz
	 * @param i {@link int} Linha
	 * @param j {@link int} Coluna
	 * @return {@link float} Valor da matriz
	 */
	public float getVal(int i,int j){
		return this._arrval[i*4+j];
	}
	
	/**
	 * Construtor da matriz com valores especificados a partir de uma outra matriz
	 * @param mat {@link Matriz4D} Objecto matriz
	 */
	public Matriz4D(Matriz4D mat){
		this._arrval=new float[DIM];
		for(int i=0;i<DIM;i++)
			this._arrval[i]=mat._arrval[i];
	}
	
	/**
	 * Método que efectua a duplicação do objecto
	 * @return {@link Matriz4D} Objecto matriz duplicado
	 */
	public Matriz4D clone(){
		return new Matriz4D(this);
	}
	/**
	 * Construtor com especificação dos valores da matriz a partir de um array de float
	 * @param array {@link float[]} Array de floats
	 */
	public Matriz4D(float[] array){
		_arrval=new float[DIM];
		if(array.length==DIM){
			for(int i=0;i<DIM;i++)
				_arrval[i]=array[i];
		}
	}
	/**
	 * Metodo que efectua a multiplicação entre uma matriz e um escalar, devolvendo o resultado num novo objecto
	 * @param mat {@link Matriz4D} Objecto matriz
	 * @param escalar {@link float} Escalar 
	 * @return {@link Matriz4D} Objecto matriz
	 */
	public static Matriz4D MultiplicaEscalar(Matriz4D mat,float escalar){
		Matriz4D out=new Matriz4D();
		for(int i=0;i<DIM;i++){
			out._arrval[i]=mat._arrval[i]*escalar;
		}
		return out;
	}
	/**
	 * Método que efectua a multiplicação entre o objecto matriz e um escalar, ficando o resultado na operação no
	 * objecto que invoca o método
	 * @param escalar {@link float} Escalar a multiplicar pela matriz
	 */
	public void MultiplicaEscalar(float escalar){
		for(int i=0;i<DIM;i++){
			this._arrval[i]*=escalar;
		}
	}
	/**
	 * Método que devolve os valores da matriz sob a forma de array de floats;
	 * @return {@link float[]} Array de valores do tipo float
	 */
	public float[] toFloatArray(){
		float[] fl=new float[16];
		for(int i=0;i<DIM;i++)
			fl[i]=this._arrval[i];
		return fl;
	}
	
	public String toString(){
		String str="";
		for(int i=0;i<4;i++){
			str+="["+_arrval[i*4]+"\t"+_arrval[i*4+1]+"\t"+_arrval[i*4+2]+"\t"+_arrval[i*4+3]+"]\n";
		}
		return str;
	}
}
