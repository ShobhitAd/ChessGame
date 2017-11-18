import java.net.*;
import java.io.*;

/** 
* Class used to dictate server and client communication
* @author Shobhit
* @version 1.0
*/
public class ClientServerSocket{
	//Server IP Address
	private String IP;
	//Server port name
	private int PORT;
	//Client and server sockets
	private Socket CLIENT = null;
	private ServerSocket SERVER = null;
	//Data streams used to communicate data
	DataOutputStream OUT;
	DataInputStream IN;
	//Message Dialog
	MessageBox MSG = new MessageBox(200,50);

	/**
	* Constructor to initializ the socket
	* @param ipAddr_ Server IP Address
	* @param port_ Port number
	*/
	public ClientServerSocket(String ipAddr_, int port_){
		//Initialize
		IP = ipAddr_;
		PORT = port_;

	}

	/**
	* Function used to initialize a server socket
	*/
	public void startServer(){

		try{
			//Initialize server socket
			SERVER = new ServerSocket(PORT);
			//Display message to host
			MSG.Open("Waiting for client");
			
			//Wait for client to connect
			CLIENT = SERVER.accept();
			
			//Initialize data streams
			OUT = new DataOutputStream(CLIENT.getOutputStream());
			IN = new DataInputStream(CLIENT.getInputStream());		
			//Display completion message to host
			
			MSG.Close();

		}
		catch(Exception e){System.out.println("Could not start server");}	
					
	}
	
	/**
	* Function used to initialize a client socket
	* @return true if the client successfully connects to the server
	*/
	public boolean startClient(){
		try{
			//Initialize client socket
			CLIENT = new Socket(IP,PORT);
			//Initialize data streams
			OUT = new DataOutputStream(CLIENT.getOutputStream());
			IN = new DataInputStream(CLIENT.getInputStream());
			//Display completion message to client
			MSG.Open("Connected to server");
			Thread.sleep(2000);
			
			MSG.Close();
			//return true
			return true;
		
		}catch (Exception e){
			//Connection failed
			System.out.println("Could not start client");
			return false;		
		}	
	}

		


}

