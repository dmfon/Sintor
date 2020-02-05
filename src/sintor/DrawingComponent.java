/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sintor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author 1
 */
public class DrawingComponent extends JPanel // панель, отрисовывающая график
{
	
	private ScreenInformator SI;
	private int dn;
	private int dx[];
	private int dy[];
	
	private JLabel JLx[];
	
	
	public DrawingComponent (ScreenInformator si, int dn, int dx[], int dy[], JLabel JLx[])
	{
		super();
		
		SI = si;
		this.dn = dn;
		this.dx = dx;
		this.dy = dy;
		
		this.JLx = JLx;
	}
	
	@Override
	protected void paintComponent(Graphics gh) 
	{  
		Graphics2D drp = (Graphics2D)gh;
		super.paintComponent(gh);

		drp.drawLine(SI.x0, SI.y0, SI.x0, SI.y1);

		drp.drawLine(SI.x0, SI.y1, SI.x0 - 5, SI.y1 + 10);
		drp.drawLine(SI.x0, SI.y1, SI.x0 + 5, SI.y1 + 10);
		
		drp.drawLine(SI.x0, SI.yOf0, dn + 90, SI.yOf0);
		//JLabel pL = new JLabel("p");
		//pL.setBounds (SI.x0 - 22, SI.y1 - 5, pL.getPreferredSize().width, pL.getPreferredSize().height);

		
		for (int i = 1; i < 70; i++)
		{
			if(i%10 == 0)
			{
				drp.drawLine(SI.x0 - 5, SI.y0 - i*10, SI.x0 + 5, SI.y0 - i*10);

				/*int j = (int) i/10 - 1;
				if (nullFlag) JLx2[j] = new JLabel(Integer.toString(i*1000));
				JLx2[j].setBounds (SI.x0 - 42, SI.y0 - i*10 - 8, JLx2[j].getPreferredSize().width, JLx2[j].getPreferredSize().height);
				this.add(JLx2[j]);*/
			}
			else drp.drawLine(SI.x0 - 2, SI.y0 - i*10, SI.x0 + 2, SI.y0 - i*10);
		}

		/*for (int i = 1; i < 70; i++)
		{
			if(i == 36)
			{
				drp.drawLine(SI.x0 - 5, SI.y0 - i*10, SI.x0 + 5, SI.y0 - i*10);
				JLx2[0].setBounds (SI.x0 - 42, SI.y0 - i*10 - 8, JLx2[0].getPreferredSize().width, JLx2[0].getPreferredSize().height);
				this.add(JLx2[0]);
			}
			else drp.drawLine(SI.x0 - 2, SI.y0 - i*10, SI.x0 + 2, SI.y0 - i*10);
		}*/

		//this.add(pL);
		/*for (int i = 0; i < JLx2.length; i++)
		{
			this.add(JLx2[i]);
		}*/
		//System.out.println(Thread.currentThread().getStackTrace()[2].getClassName());

		if (dn != -1)
		{
			drp.drawLine(SI.x0, SI.y0, dn + 100, SI.y0);
			drp.drawLine(dn + 100, SI.y0, dn + 90, SI.y0 - 5);
			drp.drawLine(dn + 100, SI.y0, dn + 90, SI.y0 + 5);

			JLabel tL = new JLabel("t");
			tL.setBounds (dn + 100, SI.y0 + 12, tL.getPreferredSize().width, tL.getPreferredSize().height);
			this.add(tL);

			drp.drawPolyline(dx, dy, dn);

			int lengthData = (int) dn/10;
			for (int i = 1; i < lengthData; i++)
			{
				if(i%10 == 0)
				{
				   drp.drawLine(SI.x0 + i*10, SI.y0 - 5, SI.x0 + i*10, SI.y0 + 5);

				}
				else drp.drawLine(SI.x0 + i*10, SI.y0 - 2, SI.x0 + i*10, SI.y0 + 2);

			}
			if (JLx != null)
			{
				
				for (int i = 0; i < JLx.length; i++)
				{
					JLx[i].setBounds (SI.x0 + i*(100) - 8, SI.y0 + 12, JLx[i].getPreferredSize().width, JLx[i].getPreferredSize().height);
					//System.out.println(JLx[i].getText());
					this.add(JLx[i]);
				}
			}

		}

		//drp.setColor(Color.red);
		//drp.drawLine (xRed, y0, xRed, y1);


	}

}
