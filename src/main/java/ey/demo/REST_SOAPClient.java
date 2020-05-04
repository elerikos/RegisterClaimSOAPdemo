package ey.demo;

import javax.swing.*;


import com.fasterxml.jackson.core.JsonProcessingException;

import ey.client.rest.InsisRestConsumer;
import ey.client.rest.insis.claims.model.ClaimGroup;
import ey.client.rest.insis.claims.model.Fnol;
import ey.client.soap.InsisClaimsConsumer;

import java.awt.Event;
import java.awt.event.*;
import java.io.IOException;
import java.util.Map;  


public class REST_SOAPClient extends Logger implements ActionListener{  
	
	private final static String INSIS_HLT_V10 = "insis_hlt_v10";
    static REST_SOAPClient  textFieldExample;
	
    JFrame f;
    JTextField tf1,tf2,tf3,tf4,tf5,tf6,tf7,tf8;  
    JLabel l1,l2,l3,l4,l5,l6,l7,l8; 
    JRadioButton rb_soap,rb_rest;
    JButton b1,b2;
   
    
    REST_SOAPClient(){ 
    	
    	int leftmargin = 10;
    	int length = 100;
    	int tf_length = 200;
    	int paxos= 20;
        f= new JFrame(); 
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        rb_soap=new JRadioButton("SOAP");    
        rb_soap.setBounds(3*leftmargin+length+tf_length,40,100,30);
        rb_soap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	l1.setText("cardId :");
            	b1.setText("Register Health Claim");
            	f.remove(l3);f.remove(l4);
            	f.remove(tf3);f.remove(tf4);
            		f.pack();
                    f.setSize(500,500);  
                    f.setLayout(null);  
                    f.setVisible(true);  
            }
        });

        rb_rest=new JRadioButton("REST");    
        rb_rest.setBounds(3*leftmargin+length+tf_length,80,100,30); 
        rb_rest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	l1.setText("policyNo :");
            	b1.setText("Register Claim");
            	f.add(l3);f.add(l4);
            	f.add(tf3);f.add(tf4);
            		f.pack();
                    f.setSize(500,500);  
                    f.setLayout(null);  
                    f.setVisible(true);  
            }
        });
        
        ButtonGroup group = new ButtonGroup();
        group.add(rb_soap);
        group.add(rb_rest);
        
        f.add(rb_soap);f.add(rb_rest);
        
        l1 = new JLabel();  
        l1.setBounds(leftmargin,paxos, length,paxos);  
        l2 = new JLabel("claimStarted :");  
        l2.setBounds(leftmargin,2*paxos, length,paxos); 
	        l3 = new JLabel("causeId :");  
	        l3.setBounds(leftmargin,3*paxos, length,paxos);  
	        l4 = new JLabel("claimCategory :");  
	        l4.setBounds(leftmargin,4*paxos, length,paxos);  
        l5 = new JLabel("eventType :");  
        l5.setBounds(leftmargin,5*paxos, length,paxos);  
        l6 = new JLabel("eventDate :");  
        l6.setBounds(leftmargin,6*paxos, length,paxos);  
        l7 = new JLabel("eventCountry :");  
        l7.setBounds(leftmargin,7*paxos, length,paxos);  
            
        tf1=new JTextField("100000041859");
        tf1.setBounds(leftmargin+length,paxos,tf_length,paxos); 
        tf2=new JTextField("2015-07-10T00:00:00+03:00");
        tf2.setBounds(leftmargin+length,2*paxos,tf_length,paxos); 
	        tf3=new JTextField("1");
	        tf3.setBounds(leftmargin+length,3*paxos,tf_length,paxos); 
	        tf4=new JTextField("DAMAGES");
	        tf4.setBounds(leftmargin+length,4*paxos,tf_length,paxos); 
        tf5=new JTextField("249");
        tf5.setBounds(leftmargin+length,5*paxos,tf_length,paxos); 
        tf6=new JTextField("2015-07-10T00:00:00+03:00");
        tf6.setBounds(leftmargin+length,6*paxos,tf_length,paxos); 
        tf7=new JTextField("ES");
        tf7.setBounds(leftmargin+length,7*paxos,tf_length,paxos); 
        
        f.add(l1);f.add(l2);
        f.add(l5);f.add(l6);f.add(l7);
        f.add(tf1);f.add(tf2);
        f.add(tf5);f.add(tf6);f.add(tf7);

  
        b1=new JButton();  
        b1.setBounds(leftmargin,9*paxos,200,40); 
        b1.addActionListener(this);  
        f.add(b1);
        
        l8 = new JLabel("HOST_PORT:");  
        l8.setBounds(leftmargin,12*paxos,tf_length,paxos);
        tf8=new JTextField("10.3.4.148:7809");
        tf8.setBounds(leftmargin+length,12*paxos,94,paxos);
        f.add(l8);f.add(tf8);
                
        area=new JTextArea();  
        JScrollPane scrollableTextArea = new JScrollPane(area);
        scrollableTextArea.setBounds(leftmargin,14*paxos, 460,170); 
        f.add(scrollableTextArea);
        
		//
        f.setSize(500,500);  
        f.setLayout(null);  
        f.setVisible(true);  
        rb_soap.doClick();
    }      
    
    
    public void actionPerformed(ActionEvent e) { 
     	    	    	
    	try {
    		if (rb_rest.isSelected()) {
    		
        	ClaimGroup claimGroup = new ClaimGroup();
        	claimGroup.setPolicyNo(tf1.getText().trim());
        	claimGroup.setClaimStarted(tf2.getText().trim());
        	claimGroup.setCauseId(Integer.parseInt(tf3.getText().trim()));
        	claimGroup.setClaimCategory(tf4.getText().trim());
        	claimGroup.setEventType(Integer.parseInt(tf5.getText().trim()));
        	claimGroup.setEventDate(tf6.getText().trim());
        	claimGroup.setEventCountry(tf7.getText().trim());
        	    	
        	Fnol fnol = new Fnol();
        	fnol.setClaimGroup(claimGroup);
        	
        	InsisRestConsumer restConsumer = new InsisRestConsumer();
        	restConsumer.setHOST_PORT(tf8.getText().trim());
        	
    		Map<String,String> cookie = restConsumer.getCookie(INSIS_HLT_V10, INSIS_HLT_V10);
			restConsumer.registerFnol(fnol, cookie);
        	
    		}else{			
			
			InsisClaimsConsumer soapConsumer = new InsisClaimsConsumer();
			soapConsumer.registerClaim(tf1.getText().trim(), tf2.getText().trim(), tf5.getText().trim(), tf6.getText().trim(), tf7.getText().trim(),null,null);
			soapConsumer.setHOST_PORT(tf8.getText().trim());
			
    		}
			
		}catch(Exception ex) {
			log("[ERROR] " +ex.getMessage());
			//System.out.println("[ERROR]" + " " + ex.getClass() + " with message: " + ex.getMessage());
		}
    	

    }  
    
	public static void main(String[] args) {  
		textFieldExample = new REST_SOAPClient();  
	}
	
	
}  