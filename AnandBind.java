import javax.swing.JFrame; 
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField; 
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionListener;  
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;
import java.io.IOException; 

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

import java.lang.Thread;
import java.util.Date;

class AnandBind extends JFrame implements ActionListener , ItemListener
{ 
    JPanel mainPanel;
    JTextField locationTF; 
    JButton selectLocationBTN,startButton,stopButton,pauseButton,resumeButton;
    MySound ms;
    String defaultLocation="D:\\";
    JRadioButton rb1,rb2;
    String fileTypeext=".mp3";

    public AnandBind()
    {   
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){ }

        mainPanel=new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(154, 171, 169));
        this.add(mainPanel);

        JLabel rbl=new JLabel("Select File Type");
        rbl.setBounds(29,10,155,20);
        rbl.setFont(new Font("Arial",Font.BOLD,16));
        rbl.setForeground(Color.white);
        mainPanel.add(rbl);


        ButtonGroup group = new ButtonGroup();
        rb1 = new JRadioButton("WAVE");
        rb2 = new JRadioButton("MP3",true);
        rb1.setBounds(175,10,60,20);
        rb2.setBounds(245,10,60,20);
        rb1.setBackground(null);
        rb2.setBackground(null);
        group.add(rb1);
        group.add(rb2);
        rb1.addItemListener(this);
        rb2.addItemListener(this);
        mainPanel.add(rb1);
        mainPanel.add(rb2);
        
        
        locationTF=new JTextField(defaultLocation);  
        locationTF.setBounds(15,45,200,22); 
        locationTF.setEditable(false);
        mainPanel.add(locationTF);
        
        selectLocationBTN=new JButton("Browse");
        selectLocationBTN.setFont(new Font("Arial",Font.BOLD,13));
        selectLocationBTN.setForeground(new Color(0,0,0));
        selectLocationBTN.setBackground(new Color(79, 97, 110));
        selectLocationBTN.setBounds(220,45,85,22);
        selectLocationBTN.addActionListener(this);
        mainPanel.add(selectLocationBTN);

        startButton=new JButton("Start");
        startButton.setFont(new Font("Arial",Font.BOLD,20));  
        startButton.setBounds(15,80,140,40); 
        mainPanel.add(startButton); 
        startButton.addActionListener(this);
        
        stopButton=new JButton("Stop");  
        stopButton.setEnabled(false);
        stopButton.setBounds(160,80,140,40);
        stopButton.setFont(new Font("Arial",Font.BOLD,20)); 
        mainPanel.add(stopButton); 
        stopButton.addActionListener(this);

        pauseButton=new JButton("pause");  
        pauseButton.setEnabled(false);
        pauseButton.setFont(new Font("Arial",Font.BOLD,20));  
        pauseButton.setBounds(15,130,140,40);
        mainPanel.add(pauseButton); 
        pauseButton.addActionListener(this);
        
        resumeButton=new JButton("Resume");  
        resumeButton.setEnabled(false);
        resumeButton.setBounds(160,130,140,40);
        resumeButton.setFont(new Font("Arial",Font.BOLD,20)); 
        mainPanel.add(resumeButton); 
        resumeButton.addActionListener(this);

        JLabel createrName=new JLabel("Created By-: Anand Bind");
        createrName.setBounds(20,200,250,30);
        createrName.setFont(new Font("Arial",Font.BOLD,20));
        createrName.setForeground(Color.white);
        mainPanel.add(createrName);

        this.setVisible(true);
        this.setTitle("Voice Recorder");
        this.setBounds(100,100,330,270);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }  


    @Override
    public void itemStateChanged(ItemEvent e) {
        System.out.println("itemStateChanged");
        int sel = e.getStateChange();
        if (sel == ItemEvent.SELECTED) {
            var button = (JRadioButton) e.getSource();
            if(button==rb1)
                fileTypeext=".wav";
        }
        ms=new MySound(defaultLocation, mainPanel,fileTypeext);  
    }
  
    public void actionPerformed(ActionEvent e)
    {  
        if(e.getSource()==selectLocationBTN)
        {
            JFileChooser jFileChooser = new JFileChooser(defaultLocation);
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser.setDialogTitle("Folder Select"); 
            int r = jFileChooser.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) 
            {   
                defaultLocation=jFileChooser.getSelectedFile().getPath();
                locationTF.setText(defaultLocation);
            } 
            else
                JOptionPane.showMessageDialog(this,"No File Select");
        }

        else if(e.getSource()==startButton)
        {       ms=new MySound(defaultLocation, mainPanel,fileTypeext); 
                ms.start(); 
                startButton.setEnabled(false); 
                stopButton.setEnabled(true); 
                rb1.setEnabled(false);
                rb2.setEnabled(false);     
        }

        else if(e.getSource()==stopButton)
        {
            ms.stopRec(); 
            stopButton.setEnabled(false);
            rb1.setEnabled(true);
            rb2.setEnabled(true);
            startButton.setEnabled(true);
        }
        else if(e.getSource()==pauseButton){}
        else if(e.getSource()==resumeButton){}
}

    public static void main(String args[])
    {   
        new AnandBind();  
    } 
}

class MySound extends Thread
{
    File fileName;
    AudioFileFormat.Type fileType;
    TargetDataLine line;
    JPanel mainPanel;

    public MySound(String defaultLocation, JPanel mainPanel1 ,String fileTypeext)
    {    
        fileName  = new File(defaultLocation+"\\"+"AnandBind_"+Long.toString(new Date().getTime())+fileTypeext); 
        mainPanel=mainPanel1; 
    }

    public void run()
    {   
        try 
        {
            fileType= AudioFileFormat.Type.WAVE;
            AudioFormat format = new AudioFormat(16000,8,2,true, true); // sampleRate : 16000 // sampleSizeInBits : 8 // channels : 2 // signed : true// bigEndian : true*/
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) 
                JOptionPane.showMessageDialog( mainPanel, "Line Not Supported");

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start(); 
            AudioInputStream ais = new AudioInputStream(line);
            AudioSystem.write(ais, fileType, fileName);
        } 
        catch (LineUnavailableException ex) {} 
        catch (IOException ex){}
    }

    public void pause(){ }

    public void stopRec() 
    {  
        line.stop();   
        line.close(); 
    }

}