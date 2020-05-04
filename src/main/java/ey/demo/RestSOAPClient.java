package ey.demo;

import javax.swing.*;


import com.fasterxml.jackson.core.JsonProcessingException;

import ey.client.rest.InsisRestConsumer;
import ey.client.rest.insis.claims.model.ClaimGroup;
import ey.client.rest.insis.claims.model.Fnol;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.*;
import java.io.IOException;
import java.util.Map;  


public class RestSOAPClient  extends Logger implements ActionListener{  
	
	private final static String INSIS_HLT_V10 = "insis_hlt_v10";
    static RestSOAPClient  textFieldExample;
	
    JTextField tf1,tf2,tf3,tf4,tf5,tf6,tf7,tf8;  
    JLabel l1,l2,l3,l4,l5,l6,l7,l8;  
    JButton b1,b2;
    JTextArea area;
    
    
    RestSOAPClient(){ 
    	
    	int leftmargin = 10;
    	int length = 100;
    	int tf_length = 200;
    	int paxos= 20;
    	int tabbed_pane_width= 450;
    	int tabbed_pane_height= 224;
    	
        JFrame f= new JFrame(); 
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        JPanel p_soap=new JPanel();
        SpringLayout layout = new SpringLayout();
        p_soap.setLayout(new GridLayout(3,2)); 
        JPanel p_rest=new JPanel(); 
        p_rest.setLayout(layout); 
        

        
        JTabbedPane tp=new JTabbedPane();
       // JTabbedPane tp=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
        
        tp.setBounds(leftmargin,leftmargin,tabbed_pane_width,tabbed_pane_height);
        
        tp.addTab("p_soap", null, p_soap, "Choose Installed Options");
        tp.addTab("p_rest", null, p_rest, "Audio system configuration");
        


        
        
        
        
        
        l1 = new JLabel("policyNo :");  
        l1.setBounds(leftmargin,paxos, length,paxos);  
        l2 = new JLabel("claimStarted :");  
        //l2.setBounds(leftmargin,2*paxos, length,paxos);  
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
            
        tf1=new JTextField("100000041859",15);
    //    tf1.setSize(100, 20);
      //  tf1.setBounds(leftmargin+length,paxos,tf_length,paxos); 
        tf2=new JTextField("2015-07-10T00:00:00+03:00");
        tf2.setBounds(leftmargin+length,paxos,tf_length,paxos); 
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
        
        layout.putConstraint(SpringLayout.WEST, l1,5,SpringLayout.EAST, l2);
        
        p_rest.add(l1);p_rest.add(l1);///p_rest.add(tf1);
      //  p_rest.add(l2);p_rest.add(tf2);
    //    p_rest.add(l3);p_rest.add(tf3);
    //    p_rest.add(l4);p_rest.add(tf4);
     //   p_rest.add(l5);p_rest.add(tf5);
     //   p_rest.add(l6);p_rest.add(tf6);
    //    p_rest.add(l7);p_rest.add(tf7);
  
        b1=new JButton("Create Claim");  
        b1.setBounds(leftmargin,9*paxos,200,40); 
        b1.addActionListener(this);  
       // p_rest.add(b1);
        
        l8 = new JLabel("INSIS_REST_BASE_URI :");  
        l8.setBounds(leftmargin,12*paxos, 150,paxos);
        tf8=new JTextField("http://10.3.4.148:7809/insis");
        tf8.setBounds(leftmargin+150,12*paxos,200,paxos);
      //  p_rest.add(l8);p_rest.add(tf8);
        
        
        f.add(tp);
                
        area=new JTextArea();  
        JScrollPane scrollableTextArea = new JScrollPane(area);
        scrollableTextArea.setBounds(leftmargin,14*paxos, 450,160); 
        f.add(scrollableTextArea);
        
        f.setSize(500,500);  
        f.setLayout(null);  
        f.setVisible(true);  
    }      
    
    
    public void actionPerformed(ActionEvent e) { 
     	    	    	
    	try {
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
			
		}catch(Exception ex) {
			log("[ERROR] " +ex.getMessage());
			//System.out.println("[ERROR]" + " " + ex.getClass() + " with message: " + ex.getMessage());
		}
    	

    }  
    
	public static void main(String[] args) {  
		textFieldExample = new RestSOAPClient();  
	}
	
	
}  