
package sintor;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

class WavWriter implements Runnable
{
	private FileInformator FI;
	
	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private AudioFileFormat.Type targetType;
	private String outFileName;
	private AudioInputStream ais;
	
	private Thread t;
	
	public WavWriter (FileInformator fi, int sampleRate)
	{
		float freq = sampleRate + 0.0F;
		FI = fi;
		//File outfile = new File (OUTFILE_NAME);
		/*audioFormat = new AudioFormat (AudioFormat.Encoding.PCM_SIGNED,			//задание параметров формата: кодировка, частота дискретизации, размер подцепочки,
													(float) sampleRate, 16, 2, 4, (float) sampleRate, false);	//кол-во каналов, кол-во байтов для сэмпла всех каналов, кол-во фреймов в секунду,
																							//лог. параметр bigEndian, определяющий порядок чтения байтов.
		*/		
		
		audioFormat = new AudioFormat(freq, 16, 2, true, false);
		DataLine.Info info = new DataLine.Info (TargetDataLine.class, audioFormat); //конструктор Info из инт-са DataLine, в котором не задается размер буффера. Этот объект
																					//позволяет определить, каким инт-ом и какого формата будут считываться аудиоданные.
																					
		targetDataLine = null; //TargetDataLine -- интефейс, позволяющий работать с входными аудиоданными.
		try
		{
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info); //определение инт-са для считывания аудиоданных.
			targetType = AudioFileFormat.Type.WAVE; //определение формата записи файла.
		}
		catch (LineUnavailableException e) { System.out.println ("ERROR W2"); }
		
	}
	
	public void setFile (String name)
	{
		outFileName = FI.getAPathFileForFolder("audiofiles", name, "wav");
	}
	
	public static String[][] getInfoOfMixers () //ввывод информации о всех аудиоустройствах системы.
	{
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		
		String res[][] = new String[mixerInfos.length][4];
		System.out.println ("Number of mixers: " + mixerInfos.length);
		System.out.println ();
		for (int i = 0; i < mixerInfos.length; i++)
		{
			byte bs[];
			bs = mixerInfos[i].getName().getBytes();
			res[i][0] = new String (bs);
			System.out.println(res[i][0]);
			res[i][1] = new String (mixerInfos[i].getVendor());
			res[i][2] = new String (mixerInfos[i].getDescription());
			res[i][3] = new String (mixerInfos[i].getVersion());
			
			//System.out.println (mixerInfos[i].getName());
			//System.out.println (mixerInfos[i].getVendor());
			//System.out.println (mixerInfos[i].getDescription());
			//System.out.println (mixerInfos[i].getVersion());
			//System.out.println ();
		}
		
		return res;
	}
	
	public void startWrite()
	{
		System.out.println ("Ricording started");
		t = new Thread (this, "WRITER_WAV");
		t.start();
	}
	
	public void run()
	{
		try
		{
			ais = new AudioInputStream(targetDataLine); //поток входных аудиоданных.
			//System.out.println(outFileName);
			File outFile = new File (outFileName);
			targetDataLine.open (audioFormat);
			targetDataLine.start();
			AudioSystem.write(ais, targetType, outFile); //запись аудиоданных в файл.
		}
		catch (IOException e) { System.out.println ("ERROR W3"); }
		catch (LineUnavailableException e) { System.out.println ("ERROR W4"); }
	}
	
	public synchronized void stopWrite()
	{
		targetDataLine.stop();
		targetDataLine.close();
		System.out.println ("Recording stopped");
    }
}
