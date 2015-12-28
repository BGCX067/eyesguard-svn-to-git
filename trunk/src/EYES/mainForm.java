package EYES;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.sound.sampled.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import EYES.SetsSettings.EyesSet;
import EYES.SetsSettings.Property;



public class mainForm extends JFrame {
	
	private static final long serialVersionUID = 1L;
	static JFrame fr = new JFrame("Get the rest of your eyes");
	private static JMenuItem itemMainSettings = new JMenuItem("Program Settings");
	private static JMenuItem itemSetsSettings = new JMenuItem("Eyes Sets Settings");
	private static JMenuItem itemStart = new JMenuItem("Start");
	private static JMenuItem itemMinim = new JMenuItem("Minimize");
	private static JMenuItem itemExit = new JMenuItem("Exit");
	private static JMenu Settings = new JMenu("Settings");
	
	private static SystemTray systemTray;
	private static TrayIcon trayIcon;
	
	private Timer timer;
	
	ProgrammSettings pr = new ProgrammSettings();
	
	private void PlaySound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		// процедура проигрывающая указанный файл
		AudioInputStream stream = AudioSystem.getAudioInputStream(new File("womp.wav"));
		DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(stream);
		clip.start();
	}
	/**
	 * функция выполняет отрисовку сферы на переданном интерфейсе формы
	 * Сфера перемещается по заданной траектории в упражнении
	 * <p>
	 * @param 
	 * Graphics gr - интерфейс от формы на которой требуется отрисовка
	 * <p>
	 * EyesSet sets - упражнение на основании которого будет строиться движение
	 */
	private void PaintTrac(Graphics gr, EyesSet sets){
		Graphics2D g = (Graphics2D)gr;		
		int timeLine = (pr.practicePeriod/4)/sets.GetPointsCount();
		Property last = null;
		for (int it = 0; it < 4; it++){
			for (Iterator<Property> iter = sets.GetPoints().iterator(); iter.hasNext();){
				if (last == null) {
					last = iter.next();
				} else {
					Property now = iter.next();
					int step = Math.max(Math.round(Math.abs(last.getXCoord() - now.getXCoord())), 
							Math.round(Math.abs(last.getYCoord() - now.getYCoord())));
					
					for (int i = 0; i < step; i++){
						g.setColor(Color.black);
						g.fillRect(0, 0, fr.getWidth(), fr.getHeight());					
						float x = ((float)(now.getXCoord() - last.getXCoord())/step)*(i+1);
						float y = ((float)(now.getYCoord() - last.getYCoord())/step)*(i+1);
						g.setColor(Color.red);
						g.fillOval(last.getXCoord() + Math.round(x), last.getYCoord() + Math.round(y), 25, 25);
						try {
							Thread.sleep(timeLine/step);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					last = now;
				}
			}
		}
		
	}
	
	private void DoExercise() {
		if (pr.waitPeriod < 0) {
			if (JOptionPane.showConfirmDialog(fr, "WaitPeriod is to short you may stuck", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;
		}
		timer = new Timer(pr.waitPeriod, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fr.setState(JFrame.NORMAL);
				JPanel temp = new JPanel();
				temp.setSize(fr.getSize());				
				fr.add(temp);
				temp.setBackground(Color.black);
				temp.setForeground(Color.black);
				temp.repaint();
				Settings.setEnabled(false);
				timer.stop(); 			
				for (int i=0; i < pr.practiceCount; i++){
					SetsSettings set = new SetsSettings();
					
					try {
						Random r = new Random();
						int ind = r.nextInt(set.readSets().size());
						PaintTrac(temp.getGraphics(), set.readSets().get(ind));
					} catch (Exception e1) {
						try {
							e1.printStackTrace();
							Thread.sleep(pr.practicePeriod);
						} catch (InterruptedException e2) {
							e2.printStackTrace();
						}
					}
					
					try{
						PlaySound();
					} catch (Exception ex)
					{
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(fr, "event happen");
						ex.printStackTrace();
					}
				};
				timer.start();
				Settings.setEnabled(true);
				fr.remove(temp);
				fr.setState(JFrame.ICONIFIED);
			}
		});
		timer.start();
	};
	
	private void SetMenuItems() {
		/**
		 * Создаём меню для доступа к настройкам
		 */
		JMenuBar jb = new JMenuBar();
		itemExit.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
			    System.exit(0);//Выход из системы
			}
		});
		
		itemMainSettings.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				pr.initProgrammSettingsForm();
			}
		});
				
		itemStart.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				fr.setState(JFrame.ICONIFIED);
				itemStart.setEnabled(false);
				DoExercise();
			}
		});
		
		itemMinim.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fr.setState(JFrame.ICONIFIED);
			}
		});
		
		itemSetsSettings.addActionListener(new java.awt.event.ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				SetsSettings ss = new SetsSettings();
				ss.initSetsSettings();
			}
		});

		Settings.add(itemMainSettings);
		Settings.add(itemSetsSettings);		
		Settings.add(itemStart);
		Settings.add(itemMinim);
		Settings.add(itemExit);
		jb.add(Settings);
		fr.setJMenuBar(jb);
	}
	@SuppressWarnings("static-access")
	public void initForm() {
		/**
		 * Иницилизируем максимальный размер онка
		 * отключаем все кнопки 
		 */
		pr.LoadSettings();
		fr.setDefaultCloseOperation(fr.DO_NOTHING_ON_CLOSE);
		fr.setAlwaysOnTop(true);
		fr.setUndecorated(true);
		fr.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		SetMenuItems();
		fr.setVisible(true);
	}
	
	  @SuppressWarnings("unused")
	private void removeTrayIcon()
	  {
	    systemTray.remove(trayIcon);
	  }
	 
	  @SuppressWarnings("unused")
	private void addTrayIcon()
	  {
	    try
	    {
	      systemTray.add(trayIcon);
	      trayIcon.displayMessage("Tray test", "Window minimised to tray, double click to show", TrayIcon.MessageType.INFO);
	    }
	    catch(AWTException ex)
	    {
	      ex.printStackTrace();
	    }
	  }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		mainForm mf = new mainForm();
		mf.initForm();
	}

}
