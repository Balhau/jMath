package balhau.matematica;

/**
 * Classe que representa um quaternião. Um quaternião é um número cuja dimensão é 4
 * (x,y,z,w) x,y,z --> coordenadas imaginárias, w parte real.
 * Podemos ver um quaternião como uma extensão dos números complexos
 * @author balhau
 *
 */
public class Quaterniao {
	private double xi;
	private double yj;
	private double zk;
	private double w;
	/**
	 * Construtor do quaternião nulo, x=y=z=w=0
	 */
	public Quaterniao(){
		xi=0;
		yj=0;
		zk=0;
		w=0;
	}
	/**
	 * Construtor do quaternião com especificação de parâmetros
	 * @param x {@link double} valor imaginário i
	 * @param y {@link double} valor imaginário j
	 * @param z {@link double} valor imaginário k
	 * @param w {@link double} valor real w
	 */
	public Quaterniao(double x,double y,double z,double w){
		xi=x;
		yj=y;
		zk=z;
		this.w=w;
	}
	/**
	 * Getter para x
	 * @return {@link double} valor de i
	 */
	public double getX() {
		return xi;
	}
	/**
	 * Setter para x
	 * @param xi {@link double} valor de i
	 */
	public void setX(double xi) {
		this.xi = xi;
	}
	/**
	 * Getter para y
	 * @return {@link double} valor de j
	 */
	public double getY() {
		return yj;
	}
	
	/**
	 * Setter para y  
	 * @param yj {@link double} valor de j
	 */
	public void setY(double yj) {
		this.yj = yj;
	}
	
	/**
	 * Getter para z
	 * @return {@link double} valor de k
	 */
	public double getZ() {
		return zk;
	}
	/**
	 * Setter para z
	 * @param zk {@link double} valor de k
	 */
	public void setZ(double zk) {
		this.zk = zk;
	}
	/**
	 * Getter para w
	 * @return {@link double} valor de w
	 */
	public double getW() {
		return w;
	}
	
	/**
	 * Setter para w
	 * @param w {@link double} valor de w
	 */
	public void setW(double w) {
		this.w = w;
	}
	
	/**
	 * Método de instância que adiciona um quaternião ao objecto existente
	 * @param q1 {@link Quaterniao} elemento a adicionar
	 */
	public void Add(Quaterniao q1){
		xi+=q1.xi;
		yj+=q1.yj;
		zk+=q1.zk;
		w+=q1.w;
	}
	/**
	 * Método estático que efectua a adição de dois objectos {@link Quaterniao}
	 * @param q1 {@link Quaterniao} elemento para adição
	 * @param q2 {@link Quaterniao} elemento para adição
	 * @return {@link Quaterniao} soma de q1 com q2
	 */
	public static Quaterniao Add(Quaterniao q1,Quaterniao q2){
		return new Quaterniao(q1.getX()+q2.getX(),q1.getY()+q2.getY(),q1.getZ()+q2.getZ(),q1.getW()+q2.getW());
	}
	/**
	 * Método estático que efectua a multiplicação de dois objectos {@link Quaterniao}
	 * @param q1 {@link Quaterniao} elemento para multiplicação
	 * @param q2 {@link Quaterniao} elemento para multiplicação
	 * @return {@link Quaterniao} multiplicação de q1 por q2
	 */
	public static Quaterniao Mul(Quaterniao q1,Quaterniao q2){
		double x,y,z,w;
		w=q1.w*q2.w-q1.xi*q2.xi-q1.yj*q2.yj-q1.zk*q2.zk;
		x=q1.w*q2.xi+q1.xi*q2.w+q1.yj*q2.zk-q1.zk*q2.yj;
		y=q1.w*q2.yj-q1.xi*q2.zk+q1.yj*q2.w+q1.zk*q2.zk;
		z=q1.w*q2.zk+q1.xi*q2.yj-q1.yj*q2.xi+q1.zk*q2.w;
		return new Quaterniao(x,y,z,w);
	}
	/**
	 * Devolve a norma do {@link Quaterniao}
	 * @return {@link double} norma
	 */
	public double Norma(){
		return Math.sqrt(w*w+xi*xi+yj*yj+zk*zk);
	}
	/**
	 * Devolve o conjugado do quaternião
	 * @return {@link Quaterniao} conjugado
	 */
	public Quaterniao Conjugado(){
		return new Quaterniao(-xi,-yj,-zk,w);
	}
	/**
	 * Devolve o inverso de um {@link Quaterniao}
	 * @return {@link Quaterniao} quaternião inverso
	 */
	public Quaterniao Inverso(){
		double norm=Norma();
		Quaterniao conj=Conjugado();
		return new Quaterniao(conj.getX()/norm,conj.getY()/norm,conj.getZ()/norm,conj.getW()/norm);
	}
}
