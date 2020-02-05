/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sintor;

public class WavFFTData
{
	public WavData WD;
	
	public double t0 = 0;
	public double t1 = 1;
	
	public int dsy[];
	public int dny[];
	public double dfy[];
	public int dx[];
	public int dn;

	public WavFFTData (WavData wd)
	{
		WD = wd;
	}
	
	public void initializaFFTData (int y0, int x0)
	{
		/*double d0 = PW.sampleRate * PW.countOfChannels * PW.bytesPerSample * t0 + PW.countByteOfHead; // позиция в файле
		double d1 = PW.sampleRate * PW.countOfChannels * PW.bytesPerSample * t1 + PW.countByteOfHead;
		int pos0 = (int) d0;
		int pos1 = (int) d1;
		
		int values[] = PW.getPart(pos0, pos1);
		dn = (int) (pos1 - pos0)/(PW.countOfChannels*PW.bytesPerSample);// кол-во элементов
		
		int maxValue = 0;
		for (int i = 0; i < dn; i++)
		{
			if (values[i] > maxValue) maxValue = values[i];
		}
		maxValue = maxValue/2;
		dny = new int[dn];
		for (int i = 0; i < dn; i++)
		{
			dny[i] = values[i] - maxValue;
		}	*/
		dny = WD.dy;
		
		int resSize = WD.PW.sampleRate/2;
		double dfy2[] = fft(dny, WD.PW.sampleRate, resSize);//new double[dn/2];//
		dfy = new double[resSize];
		//dsy = new int[dfy.length];
		/*for (int i = 0; i < dsy.length; i++)
		{
			//double res = windowHam(dsy2[i], i);
			//dfy[i] = res;
			dsy[i] = y02 - (int)(dfy[i]);//(int) (res/10000);
			System.out.println(i + " / " + (int) dfy[i]);
		}*/

		dsy = new int[resSize];
		//System.out.println(dsy.length + "  " + dny.length);
		for (int i = 0; i < resSize; i++) 
		{
			dfy[i] = Math.sqrt(dfy2[2*i]*dfy2[2*i] + dfy2[2*i+1]*dfy2[2*i+1]);
			dsy[i] = y0 - (int) (100*Math.log10(dfy[i]));
			//System.out.println(dsy[i]);
		}
		
		dx = new int[dsy.length];
		for (int i = 0; i < dsy.length; i++)
		{
			dx[i] = x0 + i + 1;
		}	
	}
	
	private double[] fft(int[] frame, int sampleRate, int resultSize) 
	{
		double[] result = new double[sampleRate];
		for (int i = 0; i < resultSize; i++) 
		{
			int frequency = i;
			for (int j = 0; j < frame.length; j++) 
			{
				result[2*i] += frame[j] * Math.cos((2 * Math.PI * frequency * j) / sampleRate);
				result[2*i + 1] += frame[j] * Math.sin((2 * Math.PI * frequency * j) / sampleRate);
			}
			result[2*i] = windowHam(result[2*i] / resultSize, 2*i);
			result[2*i + 1] = windowHam(result[2*i + 1] / resultSize, 2*i + 1);
		}

		return result;
	}
	
	public double windowHam (double w, int i)
	{
		return w*(0.53836 - 0.46164*Math.cos(2*Math.PI*i/(dn/2)));
	}
}
