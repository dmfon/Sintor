
package sintor;

import java.io.*;
import javax.sound.sampled.*;

//import javax.media.*;

public class WavPlayer implements Runnable
{
	private FileInformator FI;
	private File audioFile;
	private AudioInputStream ais;
	private Clip clip;

	private FloatControl fc;	//контроллер громкости.
	private boolean isPlaying;	//определяет проигрывается ли файл.
	private boolean isWorking;	//определяет необходимость работы потока.
	private int position;		//текущий фрейм проигрывания.
	public Thread t;
	

	public WavPlayer(FileInformator fi)
	{
		FI = fi;
		isPlaying = false;
		try
		{
			audioFile = new File (FI.nameFile);
			clip = AudioSystem.getClip();	//получение интерфейса Clip аудиосистемы, который позволяет загружать аудиоданные
											//и проигрывать их в реальном времени.

			isWorking = true;
			isPlaying = true;
			position = 0;
			t = new Thread(this, "PLAYER_WAV");
			t.start();
		} 
		catch (LineUnavailableException e) { System.out.println ("ERROR P2"); } //если аудиоданные не доступны.
	}

	public void run()
	{
		try
		{			
			ais = AudioSystem.getAudioInputStream(audioFile);	//задание аудиофайла как потока.
			clip.open(ais);

			fc = (FloatControl) clip.getControl (FloatControl.Type.MASTER_GAIN); //получение контроллера громкости.
			//System.out.println (fc.getMinimum());
			//System.out.println (fc.getMaximum());

			int frameLength = clip.getFrameLength();	//получение кол-ва фреймов в аудиофайле.
			clip.setFramePosition (position);	//задание позиции с которой начнется проигрывание.
			clip.start();

			while ((clip.getFramePosition() != frameLength) && (isWorking))	//ожидание или достигания конца файла или
			{								//или необходимости завершения работы.

			}

			isWorking = false;
			if (clip.getFramePosition() == frameLength) System.out.println ("The record is over");
		}
		catch (IOException e) { System.out.println ("ERROR P3"); }
		catch (LineUnavailableException e) { System.out.println ("ERROR P4"); } //если аудиоданные не доступны.
		catch (UnsupportedAudioFileException e) { System.out.println ("ERROR P5"); }
	}

	public boolean isPlaying()
	{
		return isPlaying;
	}

	public synchronized boolean getIsWorking()
	{
		return isWorking;
	}

	public synchronized void stop()	//остановка проигрывания.
	{
		if (isPlaying) clip.stop();
		clip.close();
		System.out.println ("Playing stopped");
		isWorking = false;
	}

	public synchronized boolean getIsPlaying()
	{
		return isPlaying;
	}

	public synchronized void pause () //пауза.
	{
		isPlaying = !isPlaying;
		position = clip.getFramePosition();
		clip.stop();
		System.out.println ("Pause");
	}

	public synchronized void resume () //продолжение проигрывания.
	{
		isPlaying = !isPlaying;
		clip.setFramePosition (position);
		clip.start();
		System.out.println ("Resume");
	}

	public synchronized void replay()	//проиграть заново.
	{
		if (isPlaying) clip.stop();
		else isPlaying = !isPlaying;
		position = 0;
		clip.setFramePosition (position);
		clip.start();
		System.out.println ("Replay");

	}

	public synchronized void increatingVolume()	//увеличение громкости на 0.5
	{
		float currentVolume = fc.getValue();
		currentVolume += 1;
		if (currentVolume > fc.getMaximum())
		{
			System.out.println ("it's maximum of volume");
			fc.setValue (fc.getMaximum());
		}
		else fc.setValue (currentVolume);
	}

	public synchronized void decreatingVolume()	//уменьшение громкости на 0.5
	{
				float currentVolume = fc.getValue();
		currentVolume -= 1;
		if (currentVolume < fc.getMinimum())
		{
			System.out.println ("it's minimum of volume");
			fc.setValue (fc.getMinimum());
		}
		else fc.setValue (currentVolume);
	}
	
	public synchronized long getTimePosition ()
	{
		return clip.getMicrosecondPosition();
	}
	
	

}