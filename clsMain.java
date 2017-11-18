import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.*;
/** 
* The class with the main menu giving access to the different game modes
* @author Shobhit
* @version 1.0
*/

public class clsMain extends JFrame implements ActionListener{
	//GUI variables
	JPanel pnlTOP = new JPanel(), pnlCENTER = new JPanel(new FlowLayout()), pnlLEFT = new JPanel()
		, pnlRIGHT = new JPanel(), pnlBOT = new JPanel();
	JLabel lblTITLE = new JLabel("CHESS"); 
	JButton btnPVP = new JButton("Player VS Player"), btnPVC = new JButton("Player VS Computer"), 
		btnOn = new JButton("Online"), btnExit =  new JButton("Quit");

	/**
	* Constructor to intialize the main menu
	* @param W Game window width
	* @param H Game window height
	*/
	public clsMain(int W ,int H){
		//Set window size and postion
		setBounds(this.getToolkit().getScreenSize().width / 2 - W / 2, 
			this.getToolkit().getScreenSize().height / 2 - H / 2, W, H);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Remove border
		setUndecorated(true);

		//Set Layout manager
		this.setLayout(new BorderLayout());
		//TOP
		this.add(pnlTOP, BorderLayout.NORTH);
			//Title		
		pnlTOP.add(lblTITLE);
		pnlTOP.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		lblTITLE.setFont(new Font("Ubuntu", Font.BOLD, 60));
		//CENTER
		this.add(pnlCENTER, BorderLayout.CENTER);
		pnlCENTER.setLayout(new GridLayout(4,1));
			//Player Vs Player
		pnlCENTER.add(btnPVP);
		btnPVP.setFont(new Font("Ubuntu", Font.PLAIN, 20));		
		btnPVP.addActionListener(this);
			//Player Vs Computer
		pnlCENTER.add(btnPVC);
		btnPVC.setFont(new Font("Ubuntu", Font.PLAIN, 20));
		btnPVC.addActionListener(this);
			//Online
		pnlCENTER.add(btnOn);
		btnOn.setFont(new Font("Ubuntu", Font.PLAIN, 20));
		btnOn.addActionListener(this);
			//Quit the game
		pnlCENTER.add(btnExit);
		btnExit.setFont(new Font("Ubuntu", Font.PLAIN, 20));
		btnExit.addActionListener(this);
		//LEFT
		this.add(pnlLEFT, BorderLayout.WEST);
		pnlLEFT.setPreferredSize(new Dimension(50,pnlLEFT.getHeight()));
		//RIGHT
		this.add(pnlRIGHT, BorderLayout.EAST);
		pnlRIGHT.setPreferredSize(new Dimension(50,pnlLEFT.getHeight()));

		//Make the window visible
		setVisible(true);

	}

	/**
	* Used to handle action events (button clicks)
	* @param event ActionEvent
	*/
	public void actionPerformed(ActionEvent event){
		//Player vs Player
		if(event.getSource() == btnPVP){
			//Start game normally
			setVisible(false);
			clsGame ABC = new clsGame(790, 730, null);
			
		}
		//Player vs Computer
		else if(event.getSource() == btnPVC){
			System.out.println("PVC");
		}
		//Online
		else if(event.getSource() == btnOn){
			System.out.println("Online");
			//Get server IP address
			String ip = JOptionPane.showInputDialog(null, "Enter the IP address", "Server IP", JOptionPane.QUESTION_MESSAGE);
			//Create the socket
			ClientServerSocket socket = new ClientServerSocket(ip, 9999);
			//Check if socket is client or server
			boolean isClient = socket.startClient();
			if(isClient){
				System.out.println("Client here");
				clsGame ABC = new clsGame(790, 730, socket);
			}else{
				socket.startServer();
				System.out.println("Server here");
				clsGame ABC = new clsGame(790, 730, socket);
			}
		}
		//Quit button
		else if(event.getSource() == btnExit){
			System.exit(0);
		}
	}

	public static void main(String[] args){
		clsMain ABC = new clsMain(500, 500);

		//clsGame ABC = new clsGame(790, 730);
	}
}