package balhau.matematica;

import org.junit.Test;

import balhau.utils.ArrayUtils;
import static junit.framework.Assert.*;

/**
 * Unit test for simple App.
 */
public class MatematicaTestSuite 
{
	@Test
	public void BNumAdd(){
		int ME=10;
		int cmp1=(int)(Math.random()*ME);
		int cmp2=(int)(Math.random()*ME);
		BNum a=new BNum(ArrayUtils.getIntMaskedRand(cmp1,BNum.base));
		BNum b=new BNum(ArrayUtils.getIntMaskedRand(cmp2,BNum.base));
		BNum c1;
		BNum c2;
		c1=BNum.add(a,b);
		c2=BNum.add(b,a);
		boolean iguais=(c1.compare(c2)==BNum.Z_IGUAL);
		assertTrue(iguais);
	}
	
	@Test
	public void BNumSub(){
		int ME=10;
		int MIT=1000;
		int cmp1;
		int cmp2;
		BNum c1;
		BNum c2;
		boolean iguais;
		for(int i=0;i<MIT;i++){
			cmp1=(int)(Math.random()*ME);
			cmp2=(int)(Math.random()*ME);
			BNum as=new BNum(ArrayUtils.getIntMaskedRand(cmp1,BNum.base));
			BNum bs=new BNum(ArrayUtils.getIntMaskedRand(cmp2,BNum.base));
			c1=BNum.sub(as,bs);
			c2=BNum.add(as,BNum.neg(bs));
			iguais=(c1.compare(c2)==BNum.Z_IGUAL);
			assertTrue(iguais);
		}
	}
	
	@Test
	public void MultDivBy2(){
		boolean eq;
		int MIT=1000;
		BNum b1,b2;
		for(int i=0;i<MIT;i++){
			b1=BNum.rand(10);
			b2=BNum.MultBy2(b1);
			eq=BNum.DivBy2(b2).compare(b1)==BNum.Z_IGUAL;
			assertTrue(eq);
		}
	}
	
	@Test
	public void MuDiPB(){
		boolean eq;
		int MIT=1000;
		int ex=0;
		BNum b1,b2,b3;
		for(int i=0;i<MIT;i++){
			b1=BNum.rand(10);
			ex=(int)Math.floor(Math.random()*20);
			b2=BNum.MultByPB(b1,ex);
			b3=BNum.DivByPB(b2,ex);
			eq=b3.compare(b1)==BNum.Z_IGUAL;
			assertTrue(eq);
		}
	}
	
	@Test
	public void MDPower2(){
		boolean eq;
		int MIT=100;
		int ex=0;
		BNum b1,b2,b3;
		for(int i=0;i<MIT;i++){
			b1=BNum.rand(10);
			ex=(int)Math.floor(Math.random()*20);
			b2=BNum.MPowerOf2(b1,ex);
			b3=BNum.DPowerOf2(b2,ex);
			eq=b3.compare(b1)==BNum.Z_IGUAL;
			assertTrue(eq);
		}
	}
}
