/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sintor;

import java.awt.Toolkit;

/* класс, содержащий информацию об экране устройства */
public class ScreenInformator
{	
	public final int DELTA_Y_CONST = 200;
	public int scaleKoef;	// определяет соотношение максимального значения амплитуды к возможному кол-ву пикселей для отображения
	
	public final int SCREEN_WIDTH;
	public final int SCREEN_HEIGHT;
	
	public int x0; // 0 по X
	public int x1;
	public int y0; // 0 по Y
	public int y1;
	
	public int yOf0; // кол-во пикселей для положительных/отрицательных значений по Y

	public ScreenInformator ()
	{
		SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - DELTA_Y_CONST;
		SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - DELTA_Y_CONST;
		
		x0 = 50;
		y1 = 20;
		x1 = SCREEN_WIDTH + x0;
		y0 = SCREEN_HEIGHT + y1;
		
		yOf0 = (y0 - y1)/2;
	}
}
