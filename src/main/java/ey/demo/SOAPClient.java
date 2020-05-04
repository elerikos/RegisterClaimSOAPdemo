package ey.demo;

import javax.swing.*;


import com.fasterxml.jackson.core.JsonProcessingException;

import ey.client.rest.InsisRestConsumer;
import ey.client.rest.insis.claims.model.ClaimGroup;
import ey.client.rest.insis.claims.model.Fnol;
import ey.client.soap.InsisClaimsConsumer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.*;
import java.io.IOException;
import java.util.Map;  


public class SOAPClient extends Logger implements ActionListener{  
	
	private final static String INSIS_HLT_V10 = "insis_hlt_v10";
    static SOAPClient  textFieldExample;
	
    JFrame f;
    static JFrame frame;
    JTextField tf1,tf2,tf3,tf4,tf5,tf6,tf7,tf8,tf9,tf11,tf12;  
    JLabel l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12; 
    JButton b1,b2,b3;
    
    
    
    SOAPClient(){ 
    	
    	int leftmargin = 10;
    	int length = 130;
    	int tf_length = 200;
    	int paxos= 20;
    	int frameWidth= 410;
    	int frameHeight= 300;
        f = new JFrame("Register Health Claim Demo"); 
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        l1 = new JLabel("Card ID:");  
        l1.setBounds(leftmargin,paxos, length,paxos);  
        l2 = new JLabel("Claim Started:");  
        l2.setBounds(leftmargin,2*paxos, length,paxos);  
        l5 = new JLabel("Event Type:");  
        l5.setBounds(leftmargin,3*paxos, length,paxos);  
        l6 = new JLabel("Event Date:");  
        l6.setBounds(leftmargin,4*paxos, length,paxos);  
        l7 = new JLabel("Event Country:");  
        l7.setBounds(leftmargin,5*paxos, length,paxos);  
        l9 = new JLabel("Primary Diagnosis ID:");  
        l9.setBounds(leftmargin,6*paxos, length,paxos); 
        l12 = new JLabel("Case ID:");  
        l12.setBounds(leftmargin,7*paxos, length,paxos);
        
        tf1=new JTextField("11400000002891");
        tf1.setBounds(leftmargin+length,paxos,tf_length,paxos); 
        tf2=new JTextField("2015-07-10");
        tf2.setBounds(leftmargin+length,2*paxos,tf_length,paxos); 
        tf5=new JTextField("249");
        tf5.setBounds(leftmargin+length,3*paxos,tf_length,paxos); 
        tf6=new JTextField("2015-07-10");
        tf6.setBounds(leftmargin+length,4*paxos,tf_length,paxos); 
        tf7=new JTextField("ES");
        tf7.setBounds(leftmargin+length,5*paxos,tf_length,paxos); 
        tf9=new JTextField("15420000077306");
        tf9.setBounds(leftmargin+length,6*paxos,tf_length,paxos); 
        tf12=new JTextField();
        tf12.setBounds(leftmargin+length,7*paxos,tf_length,paxos);
        
        f.add(l1);f.add(tf1);
        f.add(l2);f.add(tf2);
        f.add(l5);f.add(tf5);
        f.add(l6);f.add(tf6);
        f.add(l7);f.add(tf7);
        f.add(l9);f.add(tf9);
        f.add(l12);f.add(tf12);
        
  //      l10 = new JLabel("______________________________________________"); 
   //     l10.setBounds(leftmargin,7*paxos, frameSize,paxos); 
   //     f.add(l10);
 
    //    l8 = new JLabel("HOST_PORT:");  
    //    l8.setBounds(leftmargin,9*paxos,tf_length,paxos);
    //    tf8=new JTextField("10.3.4.148:7809");
    //    tf8.setBounds(leftmargin+length,9*paxos,94,paxos);
    //    f.add(l8);f.add(tf8);
        
        b1=new JButton("Register Health Claim");  
        b1.setBounds(leftmargin,8*paxos+12,380,30); 
        b1.addActionListener(this);  
        f.add(b1);
        
        l11 = new JLabel("Response Claim ID:");  
        l11.setBounds(leftmargin,11*paxos,tf_length,paxos);
        tf11=new JTextField();
        tf11.setFont( new Font(null, Font.BOLD,12));
        tf11.setBounds(leftmargin+length,11*paxos,tf_length,paxos);
        f.add(l11);f.add(tf11);
        
        
        b2=new JButton("log");  
        b2.setBounds(leftmargin+2*length+72,11*paxos,55,19);
        b2.setBorderPainted( false );
        b2.setBackground(Color.lightGray);
        b2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createFrame("log");
			}
		});   
        f.add(b2);
    
        
        area=new JTextArea();  
        JScrollPane scrollableTextArea = new JScrollPane(area);
        scrollableTextArea.setBounds(leftmargin,14*paxos, 450,160); 
       // f.add(scrollableTextArea);
        
        f.setSize(frameWidth,frameHeight);  
        f.setLayout(null);
        f.setResizable(false);
        f.setLocationByPlatform(true);
        f.setVisible(true); 
        f.setAlwaysOnTop(true);
    }      
    
    
    public void actionPerformed(ActionEvent e) { 
     	    	    	
    	try {
        	
			InsisClaimsConsumer soapConsumer = new InsisClaimsConsumer();
			//soapConsumer.setHOST_PORT(tf8.getText().trim());
			String response = soapConsumer.registerClaim(tf1.getText().trim(), tf2.getText().trim(), tf5.getText().trim(), tf6.getText().trim(),  tf7.getText().trim(), getTextFieldValue(tf9),getTextFieldValue(tf12));
			tf11.setText(response);
			
		}catch(Exception ex) {
			log("[ERROR] " +ex.getMessage());
			//System.out.println("[ERROR]" + " " + ex.getClass() + " with message: " + ex.getMessage());
		}
    	

    }  
    
	public static void main(String[] args) {  
		textFieldExample = new SOAPClient();  
	}
	
	public static void createFrame(String title)
    {
		if (frame ==null) {
			
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                frame = new JFrame(title);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
               // area=new JTextArea();  
                area.setEditable(false);
                JScrollPane scrollableTextArea = new JScrollPane(area);
                scrollableTextArea.setBounds(0,0, 700,500); 
                frame.add(scrollableTextArea);

                frame.setSize(710,550);  
                //frame.pack();
                frame.setLayout(null);
                frame.setVisible(true);
              // frame.setLocationByPlatform(true);
                frame.setResizable(false);
            }
        });
        
		} else {
			frame.setVisible(true);
		}
    }
	
	private String getTextFieldValue(JTextField jtf) {
		String tmp = jtf.getText().trim(); 
		if (tmp.length() == 0) {
			return null;			
		}
		return tmp;
	}
	

}  