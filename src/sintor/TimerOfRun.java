/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sintor;

/**
 *
 * @author 1
 */
public class TimerOfRun
{
	private static TimerOfRun TOR = new TimerOfRun();;
			
	private static long startTime;
	private static long finishTime;
	private static boolean isStart = true;
	
	public static void press()
	{
		if (isStart) 
		{
			startTime = System.currentTimeMillis();
			isStart = !isStart;
		}
		else
		{
			finishTime = System.currentTimeMillis();
			System.out.println("Time on Timer    " + (finishTime - startTime) + "  ms");
			isStart = !isStart;
		}
	}
}
