import javax.swing.JWindow;
import java.awt.*;

/**
* Class used to display messages to the user
* @author Shobhit
* @version 1.0
*/
class MessageBox extends JWindow implements Runnable{
	//Thread used to update the JWindow graphics
	Thread tmrUP = new Thread(this);
	//Message to display
	String strMSG =  null;
	//Used to disable a thread
	boolean iter = true;

	/**
	* Constructor for a messagebox
	* @param W width of the window
	* @param H height of the window
	*/
	public MessageBox(int W, int H){
		//Run parent constructor
		super();
		//Set the position and size of the window
		setBounds(this.getToolkit().getScreenSize().width / 2 - W / 2, 
			this.getToolkit().getScreenSize().height / 2 - H / 2, W, H);
		
	}

	/**
	* Open the message box
	* @param txt Message to be displayed in window
	*/
	public void Open(String txt){
		//Set the message to be displayed
		strMSG = txt;
		//Show the window
		setVisible(true);
		//Set font
		this.getGraphics().setFont(new Font("Arial",Font.PLAIN,30));
		//Start update thread
		tmrUP.start();
	}

	/**
	* Close the message box
	*/
	public void Close(){
		//Stop the update thread
		iter = false;
		//Destroy the screen
		this.dispose();
	} 

	/**
	* Run the update thread
	*/
	public void run(){
		try{
			while(iter){
				update(this.getGraphics());
				Thread.sleep(10);
			}
		}catch(Exception e){}
	}

	/** 
	* Draw things on the screen using double buffering
	* @param g
	*/
	public void update(Graphics g){
		//Double buffering
		Image dbImage = createImage(getWidth(), getHeight());
		Graphics dbg = dbImage.getGraphics();
		
		//What to draw
		dbg.setColor(Color.BLACK);
		dbg.fillRect(0, 0, this.getWidth(), this.getHeight());
		dbg.setColor(Color.WHITE);
		for(int i = 0; i < 6; i++){
			if(i < 2 || i > 4){
				dbg.drawRect(i, i, this.getWidth() - i * 2, this.getHeight() - i * 2);
			}
		}
		dbg.drawString(strMSG, this.getWidth()/2 - 50, 30);
		//Double buffering
		g.drawImage(dbImage, 0, 0, this);
		
	}
}	

