package balhau.matematica;

import balhau.utils.Sys;

/**
 * Objecto que representa uma matriz
 * @author balhau
 *
 */
public class Matriz {
	private int _ncol;
	private int _nlin;
	private double[] _matriz;
	
	/**
	 * Construtor da classe {@link Matriz}. Responsável por inicalizar uma {@link Matriz} cujas dimensões são 3x3 com entradas
	 * nulas
	 */
	public Matriz()
	{
		this._ncol=3;
		this._nlin=3;
		buildMatriz(0);
	}
	/**
	 * Construtor da matriz com especificação das dimensões da {@link Matriz}. Numero de linhas e colunas, 
	 * respectivamente
	 * @param i {@link int} inteiro identificando o número de linhas
	 * @param j {@link int} inteiro identificando o número de colunas
	 */
	public Matriz(int i,int j){
		this._nlin=i;
		this._ncol=j;
		buildMatriz(0);
	}
	/**
	 * Método que devolve o número de linhas da {@link Matriz}
	 * @return {@link int} número de linhas da {@link Matriz}
	 */
	public int NLinhas(){
		return this._nlin;
	}
	/**
	 * Método que devolve o número de colunas da {@link Matriz}
	 * @return {@link int} número de colunas da {@link Matriz}
	 */
	public int NColunas(){
		return this._ncol;
	}
	
	/**
	 * Método privado responsável por inicializar o array de entradas
	 * @param defval
	 */
	private void buildMatriz(double defval){
		this._matriz=new double[_nlin*_ncol];
		int comp=_ncol*_nlin;
		for(int i=0;i<comp;i++){
			this._matriz[i]=defval;
		}
	}
	/**
	 * Método que efectua a construção de uma {@link Matriz} identidade
	 */
	public void Identidade(){
		int k=0;
		for(int i=0;i<_ncol*_nlin;i++){
			if((i%_nlin)==0 && i!=0)
				k++;
			if((i%_ncol)==k)
				this._matriz[i]=1;
			else
				this._matriz[i]=0;
		}
	}
	/**
	 * Método que efectua a soma entre duas Matrizes
	 * @param A {@link Matriz} 
	 * @param B {@link Matriz}
	 * @return {@link Matriz} matriz representando a soma de A com B
	 */
	public static Matriz Soma(Matriz A,Matriz B){
		if(A._nlin!=B._nlin || A._ncol!=B._ncol)
			return null;
		Matriz soma=new Matriz(A._nlin, B._ncol);
		int comp=A._nlin*A._ncol;
		for(int i=0;i<comp;i++){
			soma._matriz[i]=A._matriz[i]+B._matriz[i];
		}
		return soma;
	}
	/**
	 * Método que efectua a multiplicação de duas {@link Matriz}
	 * @param A {@link Matriz}
	 * @param B {@link Matriz}
	 * @return {@link Matriz} resultado de A*B
	 */
	public static Matriz Multiplica(Matriz A,Matriz B){
		return null;
	}
	/**
	 * Método que especifica um valor na matriz
	 * @param linha {@link int} Número da linha
	 * @param coluna {@link int} Número da coluna
	 * @param valor {@link double} Valor da entrada
	 */
	public void setValor(int linha,int coluna,double valor){
		this._matriz[linha*this._ncol+coluna]=valor;
	}
	/**
	 * Método que devolve o valor contido numa célula da {@link Matriz}
	 * @param linha Posição da {@link int} linha 
	 * @param coluna Posição da {@link int} coluna
	 * @return {@link double} valor da célula na {@link Matriz}
	 */
	public double getValor(int linha,int coluna){
		return this._matriz[linha*this._ncol+coluna];
	}
	/**
	 * Norma de Forbenius
	 * @return {@link Double} valor da norma de frobenius
	 */
	public double FNorma(){
		double vl=0;
		for(int i=0;i<_matriz.length;i++){
			vl+=_matriz[i]*_matriz[i];
		}
		return Math.sqrt(vl);
	}
	
	/**
	 * Método que devolve uma {@link String} com a descrição da {@link Matriz}
	 * @return {@link String} com a descrição da {@link Matriz}
	 */
	public String toString(){
		String out="|";
		int comp=_ncol*_nlin;
		for(int i=0;i<comp;i++){
			if((i%_nlin)==0 && i!=0)
				out+="|"+Sys.EOL+"|";
			if((i%_nlin)!=0)
				out+=",";
			out+=this._matriz[i];
		}
		out+="|";
		return out;
	}
}
