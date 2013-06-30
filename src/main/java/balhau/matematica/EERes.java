package balhau.matematica;

/**
 * Classe que representa o resultado da extensão ao algoritmo de Euclides
 * @author balhau
 *
 */
public class EERes {
	public int x;
	public int y;
	public int mdc;
	/**
	 * Construtor do objecto
	 * @param x {@link Integer} valor do resultado de x na extensão do algoritmo de Euclides
	 * @param y {@link Integer} valor do resultado de y na extensão do algoritmo de Euclides
	 * @param mdc {@link Integer} Máximo divisor comum
	 */
	public EERes(int x,int y,int mdc){
		this.x=x;
		this.y=y;
		this.mdc=mdc;
	}
}
