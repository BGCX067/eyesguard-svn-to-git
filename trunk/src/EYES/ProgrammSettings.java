package EYES;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Класс форма определяющий интервалы работы программы
 * <p>
 * Задаёт период пока программа не работает и может быть выключенна
 * А также время 1ого упражнения и период выполнения 1ой задачи
 */
public class ProgrammSettings {
	static JFrame psf = new JFrame("Programm Settings"); // форма основных настроек
	
	public int practicePeriod = 1; // период упражнения в милисекундах
	public int waitPeriod = 1;     // переиод ожидания в милисекундах 
	public int practiceCount = 1;  // количество упражнений за один раз
	
	private JEditorPane edWait = new JEditorPane();
	private JEditorPane edPractice = new JEditorPane();
	private JEditorPane edCount = new JEditorPane();
	private JLabel lbWait = new JLabel("work duration in minutes");
	private JLabel lbPractice = new JLabel("practice duration in minutes");
	private JLabel lbPracticeCount = new JLabel("practice count");

	private JButton btSave = new JButton("Save");
	private JButton btCancel = new JButton("Cancel");
	private JPanel panel = new JPanel();
	private String path = "mainConfig.xml";

	public void initProgrammSettingsForm(){
		LoadSettings();
		panel.setLayout(null);
		lbWait.setBounds(5, 5, 200, 20);
		panel.add(lbWait);
		edWait.setBounds(210, 5, 100, 20);
		panel.add(edWait);
		edWait.setText(Integer.toString(waitPeriod/60000));
		lbPractice.setBounds(lbWait.getLocation().x, lbWait.getLocation().y + 25, 200, 20);
		panel.add(lbPractice);
		edPractice.setBounds(edWait.getLocation().x, edWait.getLocation().y + 25, 100, 20);
		panel.add(edPractice);
		edPractice.setText(Integer.toString(practicePeriod/60000));
		lbPracticeCount.setBounds(lbPractice.getLocation().x, lbPractice.getLocation().y + 25, 200, 20);
		panel.add(lbPracticeCount);
		edCount.setBounds(edPractice.getLocation().x, edPractice.getLocation().y + 25, 100, 20);
		panel.add(edCount);
		edCount.setText(Integer.toString(practiceCount));
		btSave.setBounds(lbPracticeCount.getLocation().x, lbPracticeCount.getLocation().y + 25, 100, 25);
		panel.add(btSave);
		btCancel.setBounds(lbPracticeCount.getLocation().x + 105, btSave.getLocation().y, 100, 25);
		panel.add(btCancel);
		
		try{
			   int min = Integer.parseInt(edWait.getText());
			   waitPeriod = (min * 60) * 1000;
			   min = Integer.parseInt(edPractice.getText());
			   practicePeriod = (min * 60) * 1000;
			   practiceCount = Integer.parseInt(edCount.getText()); 
			   
		   }catch (NumberFormatException nfe)
		   {
			   JOptionPane.showMessageDialog(null, nfe.getMessage());
			   System.err.println(nfe.getMessage());
		   }
		
		btCancel.addActionListener(new java.awt.event.ActionListener()
		  {
			   public void actionPerformed(ActionEvent e)
			   {
			     psf.setVisible(false);
			     psf.dispose();
			   }
			  });
		
		btSave.addActionListener(new java.awt.event.ActionListener()
		  {
			   public void actionPerformed(ActionEvent e)
			   {
				 if (edWait.getText().equals("0") & edPractice.getText().equals("0")
						 & edCount.getText().equals("0")) {
					 JOptionPane.showMessageDialog(psf, "Warning: bad params");
				 } else {
					 try {
						 int min = Integer.parseInt(edWait.getText());
						 waitPeriod = (min * 60) * 1000;
						 min = Integer.parseInt(edPractice.getText());
						 practicePeriod = (min * 60) * 1000;
						 practiceCount = Integer.parseInt(edCount.getText()); 
					 } catch (Exception ex) {
						 JOptionPane.showMessageDialog(psf, "Wrong params " + ex.getMessage());
						 ex.printStackTrace();
						 return;
					 }
					 
					 if ((waitPeriod <= 0) || (practicePeriod <= 0) || (practiceCount <= 0)) {
						 JOptionPane.showMessageDialog(psf, "Warning: bad params");
					 } else {
						 
						 SaveSettings();
						 JOptionPane.showMessageDialog(psf, "settings saved");
					     psf.setVisible(false);
					     psf.dispose();
					 }
				 }
			   }
			  });
		
		psf.getContentPane().add(panel);
		psf.setPreferredSize(new Dimension(325,140));
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		psf.setBounds(d.width/2 - 140, d.height/2 - 160, 325, 140);
		psf.pack();
		psf.setVisible(true);
		psf.setAlwaysOnTop(true);
		
	}
	
	public void LoadSettings(){
        Document sets = null;
        try {
            sets = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
            Element EyeSets = sets.getDocumentElement();
            String sWaitPeriod = EyeSets.getElementsByTagName("WaitPeriod").item(0).getFirstChild().getNodeValue();
            String sPracticePeriod = EyeSets.getElementsByTagName("PracticePeriod").item(0).getFirstChild().getNodeValue();
            String sPracticeCount = EyeSets.getElementsByTagName("PracticeCount").item(0).getFirstChild().getNodeValue();
            
            try {
            	waitPeriod = Integer.parseInt(sWaitPeriod);
            	practicePeriod = Integer.parseInt(sPracticePeriod);
            	practiceCount = Integer.parseInt(sPracticeCount);
            } catch (NumberFormatException nfe) {
            	JOptionPane.showMessageDialog(null, nfe.getMessage());
    			System.err.println(nfe.getMessage());
            }	
            }
        catch (Exception e){
            System.err.println(e.getMessage());
            }	
	}
	
	public void SaveSettings(){
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(path);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
			XMLStreamWriter writer;
			try {
				writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fo);
	        try {
	        	 writer.writeStartDocument();
	        	 writer.writeStartElement("setings");
	        	 	writer.writeStartElement("WaitPeriod");
	        	 		writer.writeCharacters(Integer.toString(waitPeriod));
	        	 	writer.writeEndElement();
	        	 	writer.writeStartElement("PracticePeriod");
	        	 		writer.writeCharacters(Integer.toString(practicePeriod));
	        	 	writer.writeEndElement();
	        	 	writer.writeStartElement("PracticeCount");
	        	 		writer.writeCharacters(Integer.toString(practiceCount));
	        	 	writer.writeEndElement();
	        	 writer.writeEndDocument();
	            }
	        finally {
	            writer.close();
	            }
			} catch (XMLStreamException e) { 
				e.printStackTrace();
			}
	}

}
