/* Парсер заголовка Wav файла. Должен бы работать со стандартными файлами этого типа. */

package sintor;

import java.io.*;

public class ParserWav
{
	private final String FORMAT = "WAVE";
	
	public FileInformator FI;
	
	public int sizeFile;
	public int subchunk1Size;
	public int audioFormat;
	public int countOfChannels;
	public int sampleRate;
	public int byteRate;
	public int blockAlign;
	public int bitsPerSample;
	public int subchunk2Size;
	public String strData;
	
	public int sampleCount;
	public int ampls[];
	
	public boolean flagWrite;
	public boolean flagWav;
	
	public int countByteOfHead;
	public int bytesPerSample;
	public int countOfSampleOfCannel;
	public double maxTime;
	
	public int maxAmpl;
	
	public ParserWav (FileInformator fi, boolean flag)
	{
		FI = fi;
		flagWrite = flag;
		//System.out.println (fileName);
		if (!this.checkFormat())flagWav = false;
		else
		{
			flagWav = true;
			this.parse();
			maxAmpl = (int) Math.pow(256, bytesPerSample)/2;
		}	
		
	}
	
	public void info ()
	{
		if (!flagWav) System.out.println ("It's no WAV");
		else
		{
			System.out.println ("Size of file: " + sizeFile + " bytes");
			System.out.println ("Size of remaining subchunk: " + subchunk1Size + " bytes");
			System.out.println ("id of WAVE_FORMAT: " + audioFormat);
			System.out.println ("Count of Channels: " + countOfChannels);
			System.out.println ("Sample rate: " + sampleRate + " Hz");
			System.out.println ("Number of bytes on second playback: " + byteRate);
			System.out.println ("Number of bytes for sample of all channels: " + blockAlign);
			System.out.println ("Number of bits in sample: " + bitsPerSample);
			System.out.println ("Size of data: " + subchunk2Size + " bytes");
			System.out.println ("Size of head: " + countByteOfHead + " bytes");
			System.out.println (strData);
		}
	}
	
	private void parse ()
	{
		try (FileInputStreamDecorator file = new FileInputStreamDecorator (FI.nameFile))
		{		
			int s = -1;
			for (int i = 0; i < 4; i++)
			{
				s = file.read();
			}
			
			int inputChars[] = new int[4];
			for (int i = 0; i < 4; i++)
			{
				s = file.read();
				inputChars[i] = s;			
			}
			sizeFile = translator(inputChars) + 8;
			for (int i = 0; i < 8; i++)
			{
				s = file.read();
			}
			for (int i = 0; i < 4; i++)
			{
				s = file.read();
				inputChars[i] = s;
			}
			subchunk1Size = translator(inputChars);
			
			int inputChars2[] = new int[2];
			for (int i = 0; i < 2; i++)
			{
				s = file.read();
				inputChars[i] = s;
			}
			audioFormat = translator(inputChars2);
			
			s = file.read();
			countOfChannels = s;
			s = file.read();
			
			for (int i = 0; i < 4; i++)
			{
				s = file.read();
				inputChars[i] = s;
			}
			sampleRate = translator(inputChars);
			
			for (int i = 0; i < 4; i++)
			{
				s = file.read();
				inputChars[i] = s;
			}
			byteRate = translator(inputChars);
			
			for (int i = 0; i < 2; i++)
			{
				s = file.read();
				inputChars2[i] = s;
			}
			blockAlign = translator(inputChars2);
			
			for (int i = 0; i < 2; i++)
			{
				s = file.read();
				inputChars2[i] = s;
			}
			bitsPerSample = translator(inputChars2);
			bytesPerSample = (int) bitsPerSample/8;
			
			char symb = '1';
			boolean dataFlag = false;
			boolean retFlag = false;
			while (!dataFlag)
			{
				if (!retFlag) 
				{
					symb = (char) file.read();
					retFlag = false;
				}
				if (symb == 'd')
				{
					symb = (char) file.read();
					if (symb == 'a')
					{
						symb = (char) file.read();
						if (symb == 't')
						{
							symb = (char) file.read();
							if (symb == 'a')
							{
								dataFlag = true;
							}
						}
					}
					if (symb == 'd') retFlag = true;
				}
				//System.out.println(strData);
			}
			for (int i = 0; i < 4; i++)
			{
				s = file.read();
				inputChars[i] = s;
			}
			subchunk2Size = translator(inputChars);
			
			sampleCount = (int) subchunk2Size / (blockAlign / countOfChannels);
			
			countByteOfHead = file.getCount();
			countOfSampleOfCannel = (int) subchunk2Size/countOfChannels;
			maxTime = (double) countOfSampleOfCannel/(bytesPerSample*sampleRate);
			
			/* часть отвечающая за запись в файл */
			if (flagWrite)
			{
				//System.out.println(FI.getAPathFileForFolder("inf", "txt"));
				FileOutputStream dataFile = new FileOutputStream (FI.getAPathFileForFolder("inf", "txt"));
				
				ampls = new int[sampleCount];
				
				for (int i = 0; i < sampleCount; i++)
				{
					for (int j = 0; j < 2; j++)
					{
						s = file.read();
						inputChars2[j] = s;
					}
					ampls[i] = translator(inputChars2);
					
				}
				
				s = file.read();
				if (s == -1) System.out.println ("Writing...");
				
				String num;
                boolean f = true;
				for (int i = 0; i < sampleCount; i++)
				{
                    if (f)
                    {
                        num = Integer.toString(ampls[i]);
                        for (int j = 0; j < num.length(); j++)
                        {
                            dataFile.write((int) num.charAt(j));
                        }
                        dataFile.write(13);
                        dataFile.write(10);
                        f = false;
                    }
                    else f = true;
				}
				
				dataFile.close();
			}
			/* ----------------------------------- */
		}
		catch (IOException t) { System.out.println ("error 1"); }
		
	}
	
	private int translator (int mas[]) // перевод 16го числа в little-endian в 10ое
	{
		int res = 0;
		try
		{
			StringBuffer lengthFile = new StringBuffer("");
			for (int i = mas.length - 1; i >= 0; i--)
			{
				lengthFile.append(Integer.toHexString(mas[i]));
			}
			String str = new String (lengthFile);
			
			res = Integer.parseUnsignedInt(str, 16);
		}
		catch (NumberFormatException t) { System.out.println ("error 2"); }
		return res;
	}
	
	private int negativeTranslator (int mas[]) // перевод 16го числа в little-endian в 10ое, включая отрицательные значения
	{
		int res = 0;
		try
		{
			StringBuffer lengthFile = new StringBuffer("");
			for (int i = mas.length - 1; i >= 0; i--)
			{
				StringBuffer helpSB = new StringBuffer(Integer.toBinaryString(mas[i]));
				if (helpSB.length() < 8)
				{
					//System.out.print(lengthFile + "   ");
					while (helpSB.length() != 8)
							helpSB.insert(0, "0");
					//System.out.println(lengthFile);
				}
				lengthFile.append(helpSB);
			}
			try
			{
				//System.out.println(lengthFile);
				if (lengthFile.charAt(0) == '1') 
				{
					//lengthFile.replace(0, 1, "-");
					res = Integer.parseUnsignedInt(new String (lengthFile), 2) - 65536;
				}
				else //lengthFile.replace(0, 1, "+"); 
					res = Integer.parseUnsignedInt(new String (lengthFile), 2);
				//String str = new String (lengthFile);
			
			} catch (NumberFormatException e)
			{
				System.out.println("Error in ParserWav.negativeTranslator()!!!!");
			}
		}
		catch (NumberFormatException t) { System.out.println ("error 2"); }
		return res;
	}
	
	private boolean checkFormat ()
	{
		boolean isWav = false;
		//System.out.println ("!  " + fileName);
		try (FileInputStream file = new FileInputStream (FI.nameFile))
		{
			int s;
			for (int i = 0; i < 8; i++)
			{
				s = file.read();
			}
			char nameFormatChars[] = new char[4];
			for (int i = 0; i < 4; i++)
			{
				s = file.read();
				nameFormatChars[i] = (char) s;
			}
			String nameFormat = new String (nameFormatChars);
			isWav = nameFormat.equals(FORMAT);
		}
		catch (IOException t) { System.out.println ("error 1.1  " + FI.nameFile); }
		return isWav;
	}
	
	public int[] getPart (int pos0, int pos1)	// возвращает часть аудиоданных в 10м виде
	{           								// для одного канала в промежутке (pos0; pos1]
        
        while (pos0%((int) bytesPerSample*countOfChannels) > 0) //выравнивание
        {
            pos0++;
        }
		//System.out.println(pos0);
        while (pos1%((int) bytesPerSample*countOfChannels) > 0)
        {
            pos1++;
        }
        
		int lr = (int) (pos1 - pos0)/4;
		int res[] = new int[lr];
		if (!flagWav) System.out.println ("It's no WAV");
		else
		{
			try (FileInputStream file = new FileInputStream (FI.nameFile))
			{
				int s;
				for (int i = 0; i < pos0; i++)
				{
					s = file.read();
				}
				
				int data16AllC[] = new int[pos1 - pos0]; // байты сэмплов для всех каналов
				for (int i = 0; i < data16AllC.length; i++)
				{
					s = file.read();
					data16AllC[i] = s;
				}
				
				int data16[]; // байты сэмплов для 1го канала
				if (countOfChannels == 1) data16 = data16AllC;
				else
				{
					data16 = new int[(int) (pos1 - pos0)/countOfChannels];
					
                    for (int i = 0, j = 0; (i < data16.length) && (j < data16AllC.length); i++, j++)
                    {
                        data16[i] = data16AllC[j];
                        i++; j++;
                        data16[i] = data16AllC[j];
                        for (int k = 0; k < countOfChannels; k++)
                        {
                            j++;
                        }
                    }
                    
				}	
				
                //System.out.println(pos1 + "  " + data16AllC.length + "  " + data16.length);
				int num[] = new int[bytesPerSample];	// число из байтов
				TimerOfRun.press();
				//int gg = 0;
				for (int i = 0, j = 0; j < data16.length; i++)
				{
					num[0] = data16[j];
					j++;
					num[1] = data16[j];
					j++;
					
					res[i] = negativeTranslator(num);
					//if (res[i] < maxAmpl* (-1)) System.out.println(res[i]);
					/*if (res[i] != 0)
					{
						res[i] = (int) (20*(Math.log10((Math.abs(res[i])/maxAmpl))));
					}*/
					//res[i] = (int) (100*res[i]/maxAmpl);
					//if (res[i] < 0) System.out.println(res[i]);
				}
				//System.out.println(gg);
				TimerOfRun.press();
				//System.out.println(res[0] + "  " + res[1] + "  " + res[2]);
			}
			catch (IOException t) { System.out.println ("error 3"); }
		}
		return res;
	}
}