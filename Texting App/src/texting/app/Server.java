package texting.app;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

public class Server implements ActionListener {
    
    JTextField text1;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    
    Server(){
        f.setLayout(null); 
         //Top Panel
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 435, 70);
        p1.setLayout(null);
        f.add(p1);
        
        //Back Button
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        
        //Back Button Function
        back.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });
        
        //Profile Picture
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/1.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);
        
        //VidCall Button
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        video.setBounds(300, 20, 30, 30);
        p1.add(video);
        
        //VoiceCall Button
        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone= new JLabel(i12);
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);
        
        //Morevert
        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel morevert= new JLabel(i15);
        morevert.setBounds(420, 20, 10, 25);
        p1.add(morevert);
        
        JLabel name = new JLabel("Ojendra");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN SERIF", Font.BOLD, 18));
        p1.add(name);
        
        JLabel status = new JLabel("Active now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);
        
        a1 = new JPanel();
        a1.setBounds(5, 75, 425, 515);
        f.add(a1);
        
        
        text1 = new JTextField();
        text1.setBounds(5, 600, 310, 40);
        text1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text1);
        
        JButton send = new JButton("Send");
        send.setBounds(320, 600, 113, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);
        
        f.setSize(435, 690);
        f.setLocation(200, 30);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        
        
        f.setVisible(true);
    }
   
    @Override
    public void actionPerformed(ActionEvent ae){
        try{
        String out = text1.getText();
        
        
        JPanel p2= formatLabel(out);
        
        
        a1.setLayout(new BorderLayout());
        
        JPanel right= new JPanel(new BorderLayout());
        right.add(p2, BorderLayout.LINE_END);
        vertical.add(right);
        vertical.add(Box.createVerticalStrut(15));
        
        a1.add(vertical, BorderLayout.PAGE_START);
        
        if (dout != null) {
            dout.writeUTF(out); // Move this line inside the check for dout
        } else {
            System.out.println("DataOutputStream is not initialized.");
            // You may want to handle this case appropriately, e.g., display an error message to the user.
        }
        
        
        text1.setText("");
        f.repaint();
        f.invalidate();
        f.validate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static JPanel formatLabel(String out){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel output= new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        
        panel.add(output);
        
        Calendar cal= Calendar.getInstance();
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
     
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        
       panel.add(time);
        return panel;
    }
    
    public static void main(String[] args){
        new Server();
        
        try{
            ServerSocket skt= new ServerSocket(6001);
            while(true){
                Socket s= skt.accept();
                DataInputStream din= new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                
                JScrollPane scrollPane = new JScrollPane(vertical);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

               
                f.add(scrollPane, BorderLayout.CENTER);
                f.validate();
                
                while(true){
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);
                    
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    f.validate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

