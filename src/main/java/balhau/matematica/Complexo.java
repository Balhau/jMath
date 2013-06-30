package balhau.matematica;

/**
 * Classe que representa um número complexo.
 * @author balhau
 *
 */
public class Complexo {
	private double _real;
	private double _imag;
	
	/**
	 * Construtor do número
	 */
	public Complexo(){
		this._real=0;
		this._imag=0;
	}
	/**
	 * Construtor do número complexo
	 * @param real {@link double} valor da parte real 
	 * @param imaginario {@link double} valor da parte imaginária
	 */
	public Complexo(double real,double imaginario){
		this._real=real;
		this._imag=imaginario;
	}
	/**
	 * Cria uma cópia do presente objecto
	 * @return {@link Complexo} clone
	 */
	public Complexo Clone(){
		return new Complexo(this._real,this._imag);
	}
	/**
	 * Passa o presente {@link Complexo} para o conjugado
	 */
	public void Conjugado(){
		this._imag=-this._imag;
	}
	/**
	 * Soma o presente {@link Complexo} com um outro
	 * @param comp {@link Complexo} A
	 */
	public void soma(Complexo comp){
		this._imag+=comp._imag;
		this._real+=comp._real;
	}
	/**
	 * Multiplica o complexo por um outro dado como parametro de entrada
	 * @param comp {@link Complexo} A
	 */
	public void multiplica(Complexo comp){
		this._real=this._real*comp._real-(this._imag*comp._imag);
		this._imag=this._real*comp._imag+this._imag*comp._real;
	}
	/**
	 * Método que devolve uma {@link String} com a descrição do número complexo
	 */
	public String toString(){
		if(this._imag>=0)
			return "["+_real+"+"+_imag+"i]";
		double aux=-_imag;
		return "["+_real+"-"+aux+"i]";
	}
	
	/**
	 * Devolve a parte real do número {@link Complexo}
	 * @return {@link double} parte real
	 */
	public double getReal(){
		return this._real;
	}
	/**
	 * Devolve a parte imaginária do número {@link Complexo}
	 * @return {@link double} parte imaginária
	 */
	public double getImaginaria(){
		return this._imag;
	}
}
