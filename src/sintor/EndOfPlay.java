
package sintor;

@FunctionalInterface
public interface EndOfPlay
{
	public void EndOfPlay();
}

/*if (countFreq >= WIDTH_1)
		{
			dn = WIDTH_1;
			
			int count1 = (int) countFreq/dn;
			int l1 = dn - countFreq%dn;
			
			dx = new int[dn];
			for (int i = 0; i < dn; i++)
			{
				dx[i] = x0 + i + 1;
			}
			
			dy = new int[dn];
			for (int i = 0; i < dn; i++)
			{
				int n = 0;
				int count2 = count1;
				if (i >= l1) count2++;
				for (int j = 0; j < count2; j++)
				{
					n += values[count1*i + j];
				}
				int res = (int) n/count2;
				if (n%count2 > count2/2) res++;
				
				dy[i] = (int) y0 - res/KOEF;
				if (res/KOEF > KOEF/2) dy[i]--;
			}
            
            JLx = new JLabel[dn/100];
        }   
        else
        {*/