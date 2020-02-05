
package sintor;


public class WavData
{
	public FileInformator FI;
	public ParserWav PW;
	
	public double t0 = 0;
	public double t1 = 1;
	
	public int dx[];
    public int dy[];
    public int dn = -1;
	
	public WavData (String fileName)
	{
		FI = new FileInformator (fileName);
		PW = new ParserWav (FI, false);
	}
	
	public void initializeData (int scale, int numberOfPixelsY, int offsetX, int correlation) // заполнение данных из wav
	{
		double d0 = PW.sampleRate * PW.countOfChannels * PW.bytesPerSample * t0 + PW.countByteOfHead; // позиция в файле
		double d1 = PW.sampleRate * PW.countOfChannels * PW.bytesPerSample * t1 + PW.countByteOfHead;
		int pos0 = (int) d0;
		int pos1 = (int) d1;
		
		int countFreq = (int) (pos1 - pos0)/(PW.countOfChannels*PW.bytesPerSample);// кол-во элементов
		int values[] = PW.getPart(pos0, pos1);
		
		if (scale == 1)
		{
			dn = countFreq;
			if (dn%2 != 0) dn--;
			dx = new int[dn];
			for (int i = 0; i < dn; i++)
			{
				dx[i] = offsetX + i + 1;
			}
			dy = new int[dn];
			for (int i = 0; i < dn; i++)
			{
				dy[i] = (int) numberOfPixelsY - values[i]/correlation;
				if (values[i]/correlation > correlation/2) dy[i]--;
			}
		}
		else
		{
			
			boolean flagReal = (values.length%scale == 0);
			dn = flagReal ? (int)values.length/scale : (int)values.length/scale + 1;
			if (dn%2 != 0) dn--;
			
			int l1 = dn - countFreq%dn;

			dx = new int[dn];
			for (int i = 0; i < dn; i++)
			{
				dx[i] = offsetX + i + 1;
			}

			dy = new int[dn];
			for (int i = 0; i < dn; i++)
			{
				int n = 0;
				if ((!flagReal) && (i == dn - 1))
				{
					for (int j = 0; j < values.length%scale; j++)
					{
						n += values[scale*i + j];
					}
				}
				else
				{
					for (int j = 0; j < scale; j++)
					{
						n += values[scale*i + j];
					}
				}
				int res = (int) n/scale;
				if (n%scale > scale/2) res++;

				dy[i] = (int)numberOfPixelsY - res/correlation;
				if (res/correlation > correlation/2) dy[i]++;
			}
		}
	}
	
}

