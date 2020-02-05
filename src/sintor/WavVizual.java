
package sintor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.math.*;

class WavVizual extends JFrame
{
	public ScreenInformator SI;
	public WavData WD;
	
	private String fileName = "1.wav";
	private JTextField fileNameField;
	
	private String audioFileName;
	private int countOfCharsInValue = 6;
	
	private JTextField tfT0;
	private JTextField tfT1;
	private DrawingComponent DC;
	private JScrollPane JSP;
	private JLayeredPane LP;
	private JFrame visualFrame;
    
    private JLabel JLx[];
	
	private WavPlayer WP;
	private WavWriter WW;
	private boolean flagWriting = false;
	private JButton buttonPlayPause;
	private JButton buttonStartStopWrite;
	private JLabel extraWriteLabel;
	private JTextField nameNewWavField;
	private JCheckBox inFileCheckBox;
	private JSlider JS;
	
	private int xRed;
	private TimeShower timeThread;
	private JLabel minLabel;
	private JLabel secLabel;
	private JLabel milLabel;
	
	private RedLine RL;
	
	private int scale = 1;
	private int writeFrequency;
	//private JComboBox scaleBox;
	
	public WavVizual () 
	{  
		super("Amplitude/Time");
		SI = new ScreenInformator();
		xRed = SI.x0;
		
		//System.out.println(WavVizual.class.getResource("audiofiles/out.wav"));	
		//System.out.println(sampleRate + "  " + countOfChannels + "  " + bytesInSample + "  " + countOfSampleOfCannel + "  " + maxTime);
        visualFrame = new JFrame("WavVizual");
        visualFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		visualFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, 
							Toolkit.getDefaultToolkit().getScreenSize().height);
		visualFrame.setExtendedState(MAXIMIZED_BOTH);  // развернуть окно на весь дисплей
		visualFrame.setBackground(Color.gray);
		
		setValues();
		
		/*DC = new DrawingComponent();
		JSP = new JScrollPane (DC);
		visualFrame.add(JSP, BorderLayout.CENTER);*/
        
		
		JButton ButtonEnter = new JButton ("Enter");
		BEntor bEnter = new BEntor ();
		ButtonEnter.addActionListener (bEnter);
		
		tfT0 = new JTextField ("", 5);
		tfT0.setToolTipText("start time");
		tfT1 = new JTextField ("", 5);
		tfT1.setToolTipText("finish time");
		
		JPanel timeSetPanel = new JPanel();
		timeSetPanel.add(tfT0);
		timeSetPanel.add(tfT1);
		timeSetPanel.add(ButtonEnter);
		timeSetPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//visualFrame.add(Panel, BorderLayout.SOUTH);
		
		/* --- графический интерфейс проигрывателя --- */
		
		JLabel FildFileName = new JLabel(audioFileName);
		JPanel panelNameFile = new JPanel();
		panelNameFile.add(FildFileName);
		
		buttonPlayPause = new JButton();
		setImage(buttonPlayPause, "button_play.png");
		buttonPlayPause.addActionListener(new PlayPauseListener());
		
		JButton buttonStop = new JButton();
		setImage(buttonStop, "button_stop.png");
		buttonStop.addActionListener(new StopListener());
		
		JButton buttonReplay = new JButton();
		setImage(buttonReplay, "button_replay.png");
		buttonReplay.addActionListener(new ReplayListener());
		
		JButton buttonVolumePlus = new JButton ();
		setImage(buttonVolumePlus, "button_volume_plus.png");
		buttonVolumePlus.addActionListener(new VolumePlusListener());
		
		JButton buttonVolumeMinus = new JButton ();
		setImage(buttonVolumeMinus, "button_volume_minus.png");
		buttonVolumeMinus.addActionListener(new VolumeMinusListener());
		
		JButton buttonGraph = new JButton ();
		setImage(buttonGraph, "button_graph.png");
		buttonGraph.addActionListener(new GraphListener());
		
		JButton buttonInf = new JButton ();
		setImage(buttonInf, "button_inf.png");
		buttonInf.addActionListener(new InfListener());	
		inFileCheckBox = new JCheckBox ("in file");
		JButton buttonSp = new JButton ("spectrum");
		//buttonSp.setSize(buttonInf.getWidth(), buttonInf.getHeight());
		buttonSp.addActionListener(new SpListener());	
		
		JPanel infPanel = new JPanel();
		infPanel.setLayout(new FlowLayout(0, 5, 0));
		infPanel.add(buttonInf);
		infPanel.add(inFileCheckBox);
		infPanel.add(buttonSp);
		
		// отображение текущего времени
		minLabel = new JLabel ("00");
		secLabel = new JLabel ("00");
		milLabel = new JLabel ("00");
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new FlowLayout(0, 30, 0));
		timePanel.add(minLabel);
		timePanel.add(secLabel);
		timePanel.add(milLabel);
		
		// ползунок управления произведением
		JS = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
		
		JPanel timeAndLinePanel = new JPanel();
		timeAndLinePanel.setLayout(new BorderLayout());
		timeAndLinePanel.add(JS, BorderLayout.NORTH);
		timeAndLinePanel.add(timePanel, BorderLayout.CENTER);
		
		JButton buttonNewWav = new JButton ();
		setImage(buttonNewWav, "button_new_wav.png");
		buttonNewWav.addActionListener(new NewWavListener());
		
		fileNameField = new JTextField ("", 10);
		JButton openButton = new JButton ("open");
		openButton.addActionListener(new OpenListener());
		JPanel openPanel = new JPanel();
		openPanel.setLayout(new FlowLayout(0, 5, 0));
		openPanel.add (fileNameField);
		openPanel.add (openButton);
		JLabel openLabel = new JLabel ("open wav file: ");
		JPanel openPanel2 = new JPanel();
		openPanel2.setLayout(new GridLayout (2, 1));
		openPanel2.add (openLabel);
		openPanel2.add(openPanel);
		openPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JButton delButton = new JButton();
		setImage(delButton, "button_del.png");
		delButton.addActionListener(new DelListener());
		
		int scales[] = {1, 2, 5, 10, 15, 20, 50, 100, 200, 500};
		JComboBox scaleBox = new JComboBox();
		for (int i = 0; i < scales.length; i++)
		{
			scaleBox.addItem(scales[i]);
		}
		scaleBox.addActionListener(new BoxListener());
		
		JLabel boxLabel = new JLabel("Scales: ");
		JPanel boxPanel = new JPanel();
		boxPanel.add(boxLabel);
		boxPanel.add(scaleBox);
		
		JPanel pPanel = new JPanel();
		pPanel.setLayout(new FlowLayout(0, 20, 0));
		pPanel.add(buttonPlayPause);
		pPanel.add(buttonStop);
		pPanel.add(buttonReplay);
		pPanel.add(buttonVolumePlus);
		pPanel.add(buttonVolumeMinus);
		pPanel.add(buttonGraph);
		pPanel.add(delButton);
		pPanel.add(infPanel);
		pPanel.add(openPanel2);
		pPanel.add(timeAndLinePanel);
		pPanel.add(boxPanel);
		
		pPanel.add(timeSetPanel, BorderLayout.EAST);
		pPanel.add(buttonNewWav);
		visualFrame.add(panelNameFile, BorderLayout.NORTH);
		visualFrame.add(pPanel, BorderLayout.SOUTH);
		
		visualFrame.setVisible(true);
    }

	public void setValues ()
	{
		if (fileName != null)
		{
			WD = new WavData(fileName);
			SI.scaleKoef = (int) WD.PW.maxAmpl/(SI.yOf0 - SI.y1);
			if (visualFrame != null) visualFrame.setTitle(fileName);

		}
		else visualFrame.setTitle("WavVizual");
	}
	
	public void setImage (JButton JB, String nameImage)
	{
		//String path = WavVizual.class.getResource("").toString();
		URL iconUrl = getClass().getResource("res/" + nameImage);
		//System.out.println(getClass().getResource(""));
		/*char c1[];
		if (path.charAt(0) == 'j')
		{
			c1 = new char[path.length() - 11];
			for (int i = 0; i < path.length() - 11; i++)
			{
				c1[i] = path.charAt(i + 10);
				if (c1[i] == '/') c1[i] = '\\';
			}
		}
		else
		{
			c1 = new char[path.length() - 7];
			for (int i = 0; i < path.length() - 7; i++)
			{
				c1[i] = path.charAt(i + 6);
				if (c1[i] == '/') c1[i] = '\\';
			}
		}
		path = new String (c1);		
		String pathButton = "\\res\\" + nameImage;
		//String path = new File("").getAbsolutePath();*/
		ImageIcon icon = new ImageIcon (iconUrl);
		//System.out.println(path + pathButton);
		JB.setIcon(icon);
		JB.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
	}
	
	public void setFileName (String name)
	{
		fileName = name;
	}
	
	public void paintData (int[] dd, int ddn, boolean findFile)
	{
		
		if (JSP != null) visualFrame.remove(JSP);
		if (LP != null) visualFrame.remove(LP);
		
		String fileOutName = WD.FI.getAPathFileForFolder("graph", findNamePng(), "png");
		if (findFile && (new File (fileOutName).exists()))
		{
			JLabel pngLabel = new JLabel();
			ImageIcon imageIcon = new ImageIcon(fileOutName);
			pngLabel.setIcon(imageIcon);
			LP = new JLayeredPane();
			LP.setLayout(null);
			LP.setBackground(Color.WHITE);

			pngLabel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
			pngLabel.setOpaque(false);
			LP.setPreferredSize(pngLabel.getSize());
			
			LP.add(pngLabel, JLayeredPane.DEFAULT_LAYER, 0);
			if (RL != null) 
			{
				RL.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
				RL.setOpaque(false);
						
				LP.add(RL, JLayeredPane.DEFAULT_LAYER + 50, -1);	
			}
			JSP = new JScrollPane (LP);
			JSP.getHorizontalScrollBar().setUnitIncrement(250);
			if (RL != null) JSP.getViewport().setViewPosition(new Point(xRed < 700? 0: xRed - 700, 0));
		}
		else
		{
			DC = new DrawingComponent(SI, ddn, WD.dx, dd, this.JLx);
			DC.setLayout(null);
			DC.setSize(WD.dn + 200, SI.SCREEN_HEIGHT + 200);
			LP = new JLayeredPane();
			
			//LP.setLayout(new BorderLayout());
			LP.setBackground(Color.WHITE);
			LP.setPreferredSize(new Dimension(WD.dn + 200, SI.SCREEN_HEIGHT + 50));
			LP.add(DC, JLayeredPane.DEFAULT_LAYER, 0);
			if (RL != null) 
			{
				RL.setSize(WD.dn + 200, SI.SCREEN_HEIGHT + 200);
				RL.setOpaque(false);
						
				LP.add(RL, JLayeredPane.DEFAULT_LAYER + 50, -1);	
			}
			
			JSP = new JScrollPane (LP);
			JSP.getHorizontalScrollBar().setUnitIncrement(250);
			if (RL != null) JSP.getViewport().setViewPosition(new Point(xRed < 700? 0: xRed - 700, 0));

		}
		visualFrame.setVisible(false);
		visualFrame.add(JSP, BorderLayout.CENTER);
		visualFrame.setVisible(true);
	}
	
	public void getInfoOfFile (boolean flagWrite)
	{
		//ParserWav PW = new ParserWav (WD.FI, flagWrite);
		WD.PW.info();
		
		JFrame jfInfo = new JFrame ("Info");
		jfInfo.setBackground(Color.WHITE);
		
		JLabel jl[] = 
		{
			new JLabel (WD.FI.nameFile),
			new JLabel ("Size of file:______________________________" + WD.PW.sizeFile + "   bytes"),
			new JLabel ("Size of remaining subchunk:________________" + WD.PW.subchunk1Size + "   bytes"),
			new JLabel ("id of WAVE_FORMAT:______________________" + WD.PW.audioFormat),
			new JLabel ("Count of Channels:________________________" + WD.PW.countOfChannels),
			new JLabel ("Sample rate:_____________________________" + WD.PW.sampleRate + "   Hz"),
			new JLabel ("Number of bytes on second:________________" + WD.PW.byteRate),
			new JLabel ("Number of bytes for sample of all channels:____" + WD.PW.blockAlign),
			new JLabel ("Number of bits in sample:__________________" + WD.PW.bitsPerSample),
			new JLabel ("Size of data:_____________________________" + WD.PW.subchunk2Size + "   bytes")
		};
		
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout (10, 1, 0, 20));
		int jlHeight = 0;
		for (int i = 0; i < jl.length; i++)
		{
			infoPanel.add(jl[i]);
			jlHeight += 40;
		}
		jfInfo.add(infoPanel);
		jfInfo.setSize(550, jlHeight);
		jfInfo.setVisible(true);
		
	}
	
	public void createWav ()
	{
		JFrame WriteFrame = new JFrame ("Create WAV");
		WriteFrame.setBackground(Color.WHITE);
		
		JLabel nameLabel = new JLabel("file name:  ");
		nameLabel.setPreferredSize(new Dimension(80, 20));
		nameNewWavField = new JTextField("", 25);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new OkWriteListener());
		okButton.addKeyListener(new SpaceListener());
		
		int freqs[] = {1000, 8000, 11025, 16000, 22050, 24000, 32000, 44100};
		JComboBox freqsBox = new JComboBox();
		for (int i = 0; i < freqs.length; i++)
		{
			freqsBox.addItem(freqs[i]);
		}
		freqsBox.addActionListener(new FreqBoxListener());
		freqsBox.setSelectedIndex(1);
		JLabel boxLabel = new JLabel("Frequency of write: ");
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(0, 5, 0));
		namePanel.add(nameLabel);
		namePanel.add(nameNewWavField);
		namePanel.add(okButton);
		namePanel.add(boxLabel);
		namePanel.add(freqsBox);
		
		buttonStartStopWrite = new JButton("Start");
		buttonStartStopWrite.setPreferredSize(new Dimension(200, 40));
		buttonStartStopWrite.setEnabled(false);
		buttonStartStopWrite.addActionListener(new StartStopWriteListener());
		
		JButton buttonInfWrite = new JButton("Inf");
		buttonInfWrite.setSize(20, 40);
		buttonInfWrite.addActionListener(new InfMixListener());
		
		extraWriteLabel = new JLabel("Input file name!");
		extraWriteLabel.setPreferredSize(new Dimension(100, 20));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(0, 20, 0));
		buttonPanel.add(buttonStartStopWrite);
		buttonPanel.add(buttonInfWrite);
		buttonPanel.add(extraWriteLabel);
		
		WriteFrame.setLayout(new GridLayout(2, 1, 15, 0));
		WriteFrame.setSize(450, 160);
		WriteFrame.add(namePanel);
		WriteFrame.add(buttonPanel);
		
		//WriteFrame.addKeyListener(new SpaceListener());
		WriteFrame.setVisible(true);
		
		
		/*	public void createWav ()
	{
		JFrame WriteFrame = new JFrame ("Speech to text system");
		WriteFrame.setBackground(Color.WHITE);
		
		JPanel namePanel = new JPanel();	
		JButton jbb = new JButton("Start");
		jbb.setPreferredSize(new Dimension(200, 40));
		namePanel.add(jbb);
		namePanel.setPreferredSize(new Dimension(jbb.getWidth(), jbb.getHeight()));
		
		JButton b1 = new JButton(" _ ");
		b1.setSize(20, 40);
		JButton b2 = new JButton(" C ");
		b2.setSize(20, 40);
		JButton b3 = new JButton(" , ");
		b3.setSize(20, 40);
		JButton b4 = new JButton("<--");
		b4.setSize(20, 40);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(0, 40, 0));
		buttonPanel.add(b1);
		buttonPanel.add(b2);
		buttonPanel.add(b3);
		buttonPanel.add(b4);
		buttonPanel.setPreferredSize(new Dimension((b1.getWidth() + 40)*4, b1.getHeight()));
		
		JTextField ta = new JTextField("EFEFE");
		ta.setPreferredSize(new Dimension(480, 150));
		JPanel aPanel = new JPanel();
		aPanel.add(ta);
		
		WriteFrame.setLayout(new GridLayout(3, 1, 10, 10));
		WriteFrame.setSize(500, 200);
		WriteFrame.add(namePanel);
		WriteFrame.add(buttonPanel);
		WriteFrame.add(aPanel);
		
		//WriteFrame.addKeyListener(new SpaceListener());
		WriteFrame.setVisible(true);
	}
	*/
	}
	

	public void infMixers ()
	{
		String mixers[][] = WavWriter.getInfoOfMixers();

		JPanel mixPanel[] = new JPanel[mixers.length];
		JPanel resPanel = new JPanel();
		resPanel.setLayout(new GridLayout(mixers.length, 1, 0, 30));
		for (int i = 0; i < mixPanel.length; i++)
		{
			mixPanel[i] = new JPanel();
			mixPanel[i].setLayout(new GridLayout(4, 1, 0, 10));
			JLabel jl[] = 
			{
				new JLabel(mixers[i][0]),
				new JLabel(mixers[i][1]),
				new JLabel(mixers[i][2]),
				new JLabel(mixers[i][3])
			};
			for (int j = 0; j < 4; j++)
			{
				mixPanel[i].add (jl[j]);
			}
			resPanel.add(mixPanel[i]);
		}
		
		JFrame jfInfoMix = new JFrame ("InfoMixers");
		jfInfoMix.setBackground(Color.WHITE);
		jfInfoMix.add(resPanel);
		jfInfoMix.setSize(800, mixers.length*100);
		jfInfoMix.setVisible(true);
	}
	
	
	
	public JLabel[] createJL(JLabel[] jlabs, double a, double b, double n)
	{
		double dt0 = (b - a)/(n/100);
		for (int i = 0; i < jlabs.length; i++)
		{
			double dt = dt0*(i) + a;
			String sdt = new String (Double.toString(dt));
			String sdtr = sdt;
			if (sdt.length() >= countOfCharsInValue)
			{
				char csdt[] = new char[sdt.length()];
				for (int k = 0; k < csdt.length; k++)
				{
					csdt[k] = sdt.charAt(k);
				}
				int lengthNewSdt = 0;
				int charsOf0 = 0;
				for (int j = countOfCharsInValue - 1 ; j >lengthNewSdt; j--)
				{
					if (csdt[j] == '0') charsOf0++;
					else break;
				}
				lengthNewSdt = countOfCharsInValue - charsOf0;
				char csdtr[] = new char [lengthNewSdt];
				for (int j = 0; j < lengthNewSdt; j++)
				{
					csdtr[j] = csdt[j];
				}
				sdtr = new String (csdtr);
			}
			jlabs[i] = new JLabel (sdtr);
		}

		return jlabs;
	}
	
	public String findNamePng()
	{
		if (scale == 1)
		{
			if ((WD.t1 == WD.PW.maxTime) && (WD.t0 == 0)) return WD.FI.nameFileWithoutPath;
			else 
			{
				return WD.FI.nameFileWithoutPath + "_" + WD.t0 + "-" + WD.t1;
			}
		}
		else
		{
			if ((WD.t1 == WD.PW.maxTime) && (WD.t0 == 0)) return WD.FI.nameFileWithoutPath + "_all_" + scale;
			else 
			{
				return WD.FI.nameFileWithoutPath + "_" + WD.t0 + "-" + WD.t1 + "_" + scale;
			}
		}
	}
	
	public void startWP ()
	{
		WD.t0 = 0;
		WD.t1 = WD.PW.maxTime;
		WD.initializeData(scale, SI.yOf0, SI.x0, SI.scaleKoef);
		JLx = new JLabel[WD.dn/100 + 1];
		JLx = createJL(JLx, WD.t0, WD.t1, WD.dn);
		paintData(WD.dy, WD.dn, true);
		WP = new WavPlayer (WD.FI);
		timeThread = new TimeShower();
	}
	
	private void createSx()
	{
		/*if (dny.length < sampleRate)
		{
			int dny2[] = new int [sampleRate];
			for (int i = 0; i < sampleRate; i++)
			{
				if (i < dny.length) dny2[i] = dny2[i];
				else dny2[i] = 0;
			}
			dny = dny2;
		}*/
		//dsy = new int[dn/2];
		
		/*double dsy2[] = new double[(int)dn/2];
		double rex[] = new double[(int)dn/2];
		double imx[] = new double[(int)dn/2];
		
		//double magx[] = new double[(int)dn/2];
		//double phasex[] = new double[(int)dn/2];
		
		for (int i = 0; i < (int) dn/2; i++)
		{
			rex[i] = 0;
			imx[i] = 0;
			for (int j = 0; j < dn - 1; j++)
			{
				rex[i] += dny[j]*Math.cos(2*Math.PI*i*j/dn);
				imx[i] -= dny[j]*Math.sin(2*Math.PI*i*j/dn);
			}
			//magx[i] = Math.sqrt(rex[i]*rex[i] + imx[i]*imx[i]);
			//phasex[i] = Math.atan(imx[i]/rex[i]);
			
			//dsy2[i] = rex[i] + imx[i];
			dsy2[i] = 10 * Math.log10(rex[i]) + imx[i];
			//System.out.println(i + " / " + dn/2);
		}*/
		
		//int y02 = y0/2;
		long start = System.currentTimeMillis();
		WavFFTData WDFFT = new WavFFTData(WD);
		WDFFT.initializaFFTData(SI.y0, SI.x0);
		
		System.out.println();
		System.out.println("FFT was  " + (System.currentTimeMillis() - start) + "  ms");
		
		JFrame jfDTF = new JFrame ("FFT");
		jfDTF.setBackground(Color.WHITE);
		
		JLabel jl2[] = new JLabel[WDFFT.dsy.length/100 + 1];//{new JLabel("0")};
		jl2 = createJL(jl2, 0, WDFFT.dsy.length, WDFFT.dsy.length);
		//JLabel jl3[] = {new JLabel ("0")};
		DrawingComponent DFTdc = new DrawingComponent(SI, WDFFT.dsy.length, WDFFT.dx, WDFFT.dsy, jl2);
		
		//System.out.println(DFTdc.getWidth() + "  " + DFTdc.getHeight());
		DFTdc.setLayout(null);
		DFTdc.setSize(WDFFT.dsy.length + 200, SI.SCREEN_HEIGHT + 150);
		
		JLayeredPane LP2 = new JLayeredPane();
		LP2.setBackground(Color.WHITE);
		LP2.setPreferredSize(new Dimension(WDFFT.dsy.length + 200, SI.SCREEN_HEIGHT + 150));
		LP2.add(DFTdc, JLayeredPane.DEFAULT_LAYER, 0);
		
		JScrollPane JSP2 = new JScrollPane (LP2);
		//JSP2.setPreferredSize(new Dimension(dn/2 + 200, HEIGHT_2 + 150));
		JSP2.getHorizontalScrollBar().setUnitIncrement(250);
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int widthDevice = gd.getDisplayMode().getWidth();
		
		jfDTF.add(JSP2);
		if (WDFFT.dsy.length + 200 > widthDevice) jfDTF.setSize(new Dimension(widthDevice, SI.SCREEN_HEIGHT + 150));
		else jfDTF.setSize(new Dimension(WDFFT.dsy.length + 200, SI.SCREEN_HEIGHT + 150));
		jfDTF.setVisible(true);
	}
	
	public static void main (String args[])
	{
		WavVizual WV = new WavVizual();
	}
	
	private class BEntor implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if ((tfT0.getText().equals("")) || (tfT1.getText().equals("")))
			{
				System.out.println ("Input values of time!");
				WD.dn = -1;
			}
			else
			{
				try
				{
					WD.t0 = Double.parseDouble(tfT0.getText());
					WD.t1 = Double.parseDouble(tfT1.getText());
					if (WD.t1 <= WD.t0) System.out.println ("values is equals or t1 < t0!");
                    else if (WD.t1 > WD.PW.maxTime) 
					{
						System.out.println ("length of audiofile < " + WD.t1);
						System.out.println ("length of audiofile == " + WD.PW.maxTime + " c");
					}
                        else
                        {
                            WD.initializeData (scale, SI.yOf0, SI.x0, SI.scaleKoef);
							JLx = new JLabel[WD.dn/100 + 1];
							JLx = createJL(JLx, WD.t0, WD.t1, WD.dn);
							paintData(WD.dy, WD.dn, true);
                        }
					
				}
				catch (NumberFormatException t) { System.out.println ("error 1"); }
			}
		}
	}
	
	private class PlayPauseListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if (WP == null)
			{
				if(fileName != null)
				{
					startWP();
					setImage(buttonPlayPause, "button_pause.png");
				}
			}
			else
				if (WP.isPlaying())
				{
					WP.pause();
					timeThread.pause();
					setImage(buttonPlayPause, "button_play.png");
					
					xRed = SI.x0 + (int) ((WD.PW.sampleRate * WP.getTimePosition()) / (1000000 * scale));
					//System.out.println(WP.getTimePosition());
					RL = new RedLine();
					paintData(WD.dy, WD.dn, true);
				}
				else
				{
					WP.resume();
					timeThread.resume();
					setImage(buttonPlayPause, "button_pause.png");
				}
		}
	}
	
	private class InfListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if (inFileCheckBox.isSelected()) getInfoOfFile(true);
			else getInfoOfFile(false);
		}
	}
	
	private class StopListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if(WP != null) 
			{
				boolean isP = WP.isPlaying();
				WP.stop();
				WP = null;
				if (isP) setImage(buttonPlayPause, "button_play.png");
			}
		}
	}
	
	private class ReplayListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if(WP != null)
			{
				boolean isP = WP.isPlaying();
				//WP.replay();
				if (!isP) setImage(buttonPlayPause, "button_pause.png");
				WP = null;
				startWP();
				setImage(buttonPlayPause, "button_pause.png");
			}
		}
	}
	
	private class VolumePlusListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if(WP != null)
			{
				WP.increatingVolume();
			}
		}
	}
	
	private class VolumeMinusListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if(WP != null)
			{
				WP.decreatingVolume();
			}
		}
	}
	
	private class GraphListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if(DC != null)
			{
				String fileOutName = WD.FI.getAPathFileForFolder("graph", findNamePng(), "png");
				if (new File (fileOutName).exists()) System.out.println("File is exists!");
				else
				{
					try
					{
						BufferedImage bi = new BufferedImage(DC.getWidth(), DC.getHeight(), BufferedImage.TYPE_INT_ARGB);
						DC.paint(bi.getGraphics());

						File graphFile = new File (fileOutName);
						ImageIO.write(bi, "png", graphFile);

					} catch (IOException e) { }
				}
			}
		}
	}
	
	private class NewWavListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			createWav();
		}
	}
	
	private class FreqBoxListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			JComboBox jComboBox = (JComboBox) click.getSource();
			writeFrequency = (int) jComboBox.getSelectedItem();
			//System.out.println(scale);
		}
	}
	
	private class OkWriteListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if (!nameNewWavField.getText().equals(""))
			{
				extraWriteLabel.setText("");
				buttonStartStopWrite.setEnabled(true);
				
				//System.out.println(writeFrequency);
				if (WW == null) WW = new WavWriter(WD.FI, writeFrequency);
				WW.setFile(nameNewWavField.getText());
			}
			else
			{
				extraWriteLabel.setText("Input file name!");
				buttonStartStopWrite.setEnabled(false);
			}
		}
	}
	
	private class StartStopWriteListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if (WW != null)
			{
				if (!flagWriting)
				{
					WW.startWrite();
					buttonStartStopWrite.setText("Stop");
					extraWriteLabel.setText("Writing start");
					flagWriting = true;
				}
				else
				{
					WW.stopWrite();
					buttonStartStopWrite.setText("Start");
					extraWriteLabel.setText("Writed");
					flagWriting = false;
				}
			}
		}
	}
	
	private class SpaceListener extends KeyAdapter
	{
		@Override
		public void keyPressed (KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				if (WW != null)
				{
					if (!flagWriting)
					{
						WW.startWrite();
						buttonStartStopWrite.setText("Stop");
						extraWriteLabel.setText("Writing start");
						flagWriting = true;
					}
				}
			}
		}
		
		@Override
		public void keyReleased (KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				if (WW != null)
				{
					if (flagWriting)
					{
						WW.stopWrite();
						buttonStartStopWrite.setText("Start");
						extraWriteLabel.setText("Writed");
						flagWriting = false;
					}
				}
			}
		}
	}
	
	private class InfMixListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			infMixers();
		}
	}
	
	private class OpenListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if (!fileNameField.getText().equals(""))
			{
				String name = WD.FI.getAPathFileForFolder("audiofiles", fileNameField.getText(), "wav");
				if (new File(name).exists())
				{
					if(WP != null) 
					{
						boolean isP = WP.isPlaying();
						WP.stop();
						WP = null;
						if (isP) setImage(buttonPlayPause, "button_play.png");
					}
					fileName = new String (fileNameField.getText() + ".wav");
					setValues();
				}
			}
		}
	}
	
	private class DelListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			if (fileName != null)
			{
				JFrame confirmFrame = new JFrame();			
				JLabel sureLabel = new JLabel("Are you sure?");
				
				JButton noButton = new JButton("No");
				noButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent click)
					{
						confirmFrame.setVisible(false);
					}
				});
				
				JButton yesButton = new JButton("Yes");
				yesButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent click)
					{
						if(WP != null) 
						{
							boolean isP = WP.isPlaying();
							WP.stop();
							WP = null;
							if (isP) setImage(buttonPlayPause, "button_play.png");
						}
						
						File file = new File (WD.FI.nameFile);
						if (file.delete()) System.out.println("file deleted");
						else System.out.println("file not deleted " + fileName);
						
						fileName = null;
						setValues();
						confirmFrame.setVisible(false);
					}
				});
				
				JPanel confPanel = new JPanel();
				confPanel.setLayout(new FlowLayout(10));
				confPanel.add(yesButton);
				confPanel.add(noButton);
				
				confirmFrame.add(sureLabel, BorderLayout.NORTH);
				confirmFrame.add(confPanel, BorderLayout.CENTER);
				confirmFrame.setBounds((int) ((JButton) click.getSource()).getLocationOnScreen().getX(),
										(int) ((JButton) click.getSource()).getLocationOnScreen().getY() - 100,
										150, 100);
				
				confirmFrame.setVisible(true);
			}
		}
	}
	
	private class BoxListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			JComboBox jComboBox = (JComboBox) click.getSource();
			scale = (int) jComboBox.getSelectedItem();
			WD.initializeData(scale, SI.yOf0, SI.x0, SI.scaleKoef);
			JLx = new JLabel[WD.dn/100 + 1];
			JLx = createJL(JLx, WD.t0, WD.t1, WD.dn);
			paintData(WD.dy, WD.dn, true);
		}
	}
	
	private class SpListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent click)
		{
			createSx();
			//paintData(dsy, dn, false);
			//System.out.println(scale);
		}
	}
	
	private class RedLine extends JPanel
	{
		@Override
		protected void paintComponent(Graphics gh) 
		{  
			//Graphics2D drp = (Graphics2D)gh;
			gh.setColor(Color.red);
			gh.drawLine(xRed, SI.y0, xRed, SI.y1);
		}
	}
	
	public class TimeShower implements Runnable
	{
		private Thread t;
		private boolean suspendFlag;
		
		public TimeShower()
		{
			t = new Thread(this, "TimeShower");
			suspendFlag = false;
			t.start();
		}
		
		public void run()
		{
			long timePos;
			int mil = 0;
			int sec = 0;
			int min = 0;
			try
			{
				while ((WP != null) && (WP.t.isAlive()))
				{
					timePos = WP.getTimePosition();
					synchronized(this)
					{
						while (suspendFlag)
						{
							wait();
						}
					}
					Thread.sleep(5);
					
					mil = (int) timePos / 1000;
					sec = mil / 1000;
					if (mil > 999) mil = mil % 1000;
					min = sec / 60;
					if (sec > 59) sec = sec % 60;
					
					minLabel.setText(Integer.toString(min));
					secLabel.setText(Integer.toString(sec));
					milLabel.setText(Integer.toString(mil));
					
					//System.out.println(xRed + "  " + dn);
				}
				//System.out.println("!!");
			} catch (InterruptedException ex)
			{
				System.out.println("ERROR in TimeShower!");
			}
			catch (Exception ex)
			{
				System.out.println("ERROR! ");
			}
		}
		
		public synchronized void pause()
		{
			suspendFlag = true;
		}
		
		public synchronized void resume()
		{
			suspendFlag = false;
			notify();
		}
		
	}
	
}
