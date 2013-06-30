package balhau.matematica;

/**
 * Classe que representa um vector 3-Dimensional. 
 * @author balhau
 *
 */
public class Vector3D {
	private double _x;
	private double _y;
	private double _z;
	
	/**
	 * Construtor da classe. Vector nulo
	 */
	public Vector3D(){
		this._x=0;
		this._y=0;
		this._z=0;
	}
	
	/**
	 * Construtor da classe com especificação das coordenadas do vector
	 * @param x {@link double} coordenada em x
	 * @param y {@link double} coordenada em y
	 * @param z {@link double} coordenada em z
	 */
	public Vector3D(double x,double y,double z){
		this._x=x;
		this._y=y;
		this._z=z;
	}
	
	/**
	 * Método que efectua um clone do objecto
	 * @return devolve um novo {@link Vector3D} que representa um clone do presente vector 
	 */
	public Vector3D Clone(){
		return new Vector3D(_x,_y,_z);
	}
	
	/**
	 * Método estático que efectua a soma de dois {@link Vector3D} 
	 * @param A {@link Vector3D}
	 * @param B {@link Vector3D}
	 * @return
	 */
	public static Vector3D Soma(Vector3D A,Vector3D B){
		return new Vector3D(A._x+B._x,A._y+B._y,A._z+B._z);
	}
	
	/**
	 * Método que determina o produto interno entre dois vectores 
	 * @param A {@link Vector3D}
	 * @param B {@link Vector3D}
	 * @return {@link double} com o valor do produto interno entre o {@link Vector3D} A e o {@link Vector3D} B
	 */
	public static double ProdInterno(Vector3D A,Vector3D B){
		return A._x*B._x+A._y*B._y+A._z*B._z;
	}
	/**
	 * Método que devolve a norma do vector
	 * @return {@link Double} norma do vector
	 */
	public double Norma(){
		return Math.sqrt(this._x*this._x+this._y*this._y+this._z*this._z);
	}
	/**
	 * Método que efectua a normalização do vector
	 */
	public void Normaliza(){
		double norma=this.Norma();
		this._x/=norma;
		this._y/=norma;
		this._z/=norma;
	}
	/**
	 * Método que efectua a multiplicação de um vector por um escalar
	 * @param escalar {@link Double} escalar
	 */
	public void MEscalar(double escalar){
		this._x*=escalar;
		this._y*=escalar;
		this._z*=escalar;
	}
	/**
	 * Método que efectua o produto vectorial.
	 * @param A {@link Vector3D} 
	 * @param B {@link Vector3D} 
	 * @return devolve o {@link Vector3D} C resultante do produto entre A e B
	 */
	public static Vector3D ProdVectorial(Vector3D A,Vector3D B){
		return new Vector3D(A._y*B._z-A._z*B._y,A._x*B._z-A._z*B._x,A._x*B._y-B._x*A._y);
	}
	
	/**
	 * Método que define o vector
	 * @param x {@link double} valor de X
	 * @param y {@link double} valor de Y
	 * @param z {@link double} valor de Z
	 */
	public void setVector(double x,double y,double z){
		this._x=x;
		this._y=y;
		this._z=z;
	}
	
	/**
	 * Método que adiciona um {@link Vector3D} multiplicado por um determinado factor
	 * @param vec {@link Vector3D} 
	 * @param magnitude {@link double} magnitude
	 */
	public void addVectorMult(Vector3D vec,double magnitude){
		this._x+=vec._x*magnitude;
		this._y+=vec._y*magnitude;
		this._z+=vec._z*magnitude;
	}
	
	/**
	 * Método que adiciona um vector.
	 * @param vec {@link Vector3D}
	 */
	public void addVector(Vector3D vec){
		this._x+=vec._x;
		this._y+=vec._y;
		this._z+=vec._z;
	}
	/**
	 * Adiciona um vector componente a componente
	 * @param x {@link float} Valor de x
	 * @param y {@link float} Valor de y
	 * @param z {@link float} Valor de z
	 */
	public void addVector(float x, float y, float z){
		this._x+=x;
		this._y+=y;
		this._z+=z;
	}
	
	/**
	 * Subtracção do vector por um {@link Vector3D}
	 * @param vec {@link Vector3D}
	 */
	public void subVector(Vector3D vec){
		this._x-=vec._x;
		this._y-=vec._y;
		this._z-=vec._z;
	}
	
	/**
	 * Subtracção do vector componente a componente
	 * @param x {@link float} Valor de x
	 * @param y {@link float} Valor de y
	 * @param z {@link float} Valor de z
	 */
	public void subVector(float x,float y,float z){
		this._x-=x;
		this._y-=y;
		this._z-=z;
	}
	
	/**
	 * Cria um novo {@link Vector3D} que corresponde à soma dos dois dados como parâmetros de entrada 
	 * @param v1 {@link Vector3D} A
	 * @param v2 {@link Vector3D} B
	 * @return {@link Vector3D} C=A+B
	 */
	public static Vector3D addVector(Vector3D v1,Vector3D v2){
		Vector3D v=v1.Clone();
		v.addVector(v2);
		return v;
	}
	/**
	 * Efectua um clone do {@link Vector3D} dado como parâmetro de entrada e multiplica-o pelo escalar
	 * fornecido, igualmente, como parâmetro de entrada
	 * @param vec {@link Vector3D} vec
	 * @param esc {@link Vector3D} esc
	 * @return clone de vec multipicado por esc
	 */
	public static Vector3D MEscalar(Vector3D vec,double esc){
		Vector3D aux=vec.Clone();
		aux.MEscalar(esc);
		return aux;
	}
	/**
	 * Devolve a o valor de X
	 * @return {@link double} X
	 */
	public double getX(){
		return this._x;
	}
	/**
	 * Devolve o valor de Y
	 * @return {@link double} Y
	 */
	public double getY(){
		return this._y;
	}
	/**
	 * Devolve o valor de Z
	 * @return {@link double} Z
	 */
	public double getZ(){
		return this._z;
	}
	
	/**
	 * Método que converte o {@link Vector3D} para (0,0,0)
	 */
	public void Nulo(){
		this._x=0;
		this._y=0;
		this._z=0;
	}
	
	/**
	 * Método que devolve a descrição do {@link Vector3D} sob a forma de {@link String}
	 * @return {@link String} descrição do {@link Vector3D}
	 */
	public String toString(){
		return "x: "+this._x+",y:"+this._y+",z:"+this._z;
	}
}
