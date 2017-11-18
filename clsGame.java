import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
/** 
* The main class that uses all of the provided classes with a GUI
* @author Shobhit
* @version 1.0
*/
public class clsGame extends JFrame implements ActionListener{
	
	//Color arrays
		//White color array
	Color[] colP1 = {Color.WHITE, new Color(255, 255, 204)};
		//Black color array
	Color[] colP2 = {Color.GRAY.darker(), new Color(153, 76, 0)};
	
	//GUI Variables
	JPanel pnlTOP = new JPanel(), pnlLEFT = new JPanel(new BorderLayout()), pnlBOT = new JPanel(), 
		pnlRIGHT = new JPanel(), pnlCENTER = new JPanel(), pnlLEFT_TOP = new JPanel(), pnlLEFT_BOT = new JPanel();
	Timer tmrANIM = new Timer(1, this);
	JLabel lblTITLE = new JLabel("CHESS"), lblSTATS = new JLabel("No text"), 
		lblLEFT_TITLE = new JLabel("Move List", SwingConstants.CENTER);
	JButton btnUNDO = new JButton ("Undo Move");
	JMenuBar MAIN_MENU = new JMenuBar();
	JMenu mnuGAME = new JMenu("Game"), mnuSTYLE = new JMenu("Styles"), mnuSTYLE_TILE = new JMenu("Tiles");
	JMenuItem mnuGAME_NEW = new JMenuItem("New Game"), mnuGAME_SAVE = new JMenuItem("Save Game"),
		mnuGAME_LOAD = new JMenuItem("Load Game"), mnuGAME_EXIT = new JMenuItem("Exit"),
		mnuSTYLE_TILE_LIST[] = new JMenuItem[colP1.length];
	
	//List of all moves made by player
	JList<String> MOVE_LIST = new JList<String>();

	//Instance of the chess game
	ChessGame instGAME;

	/** 
	* Constructor for the main class
	* @param W Width of the GUI window
	* @param H Height of the GUI window
	*/
	public clsGame(int W ,int H, ClientServerSocket socket){
		//Initial Setup
		super("Chess");
		setBounds(this.getToolkit().getScreenSize().width / 2 - W / 2, 
			this.getToolkit().getScreenSize().height / 2 - H / 2, W, H);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add J Components
		this.setLayout(new BorderLayout());
		
			//TOP
		this.add(pnlTOP, BorderLayout.NORTH);
		pnlTOP.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		pnlTOP.add(lblTITLE);
		lblTITLE.setFont(new Font("Ubuntu", 40, 40));

			//BOTTOM
		this.add(pnlBOT, BorderLayout.SOUTH);
		pnlBOT.setBorder(BorderFactory.createEtchedBorder());
		pnlBOT.add(lblSTATS);
		lblSTATS.setFont(new Font("Century Gothic", 20, 20));
		
			//LEFT
		this.add(pnlLEFT,BorderLayout.WEST);
		pnlLEFT.setPreferredSize(new Dimension(getWidth() / 4, getHeight()));
		pnlLEFT.setBorder(BorderFactory.createBevelBorder(0));
		pnlLEFT.add(pnlLEFT_TOP, BorderLayout.NORTH);
		pnlLEFT_TOP.add(lblLEFT_TITLE);
		lblLEFT_TITLE.setFont(new Font("Century Gothic", 20, 20));
		pnlLEFT.add(MOVE_LIST, BorderLayout.CENTER);
		MOVE_LIST.setFont(new Font("URW Chancery L", 20, 20));
		pnlLEFT.add(pnlLEFT_BOT, BorderLayout.SOUTH);
		pnlLEFT_BOT.add(btnUNDO);
		btnUNDO.addActionListener(this);

			//RIGHT
		this.add(pnlRIGHT,BorderLayout.EAST);
	
			//CENTER
		this.add(pnlCENTER,BorderLayout.CENTER);
			//MENU BAR
		this.setJMenuBar(MAIN_MENU);
		MAIN_MENU.add(mnuGAME);
		mnuGAME.add(mnuGAME_NEW);
		mnuGAME_NEW.addActionListener(this);
		mnuGAME.add(mnuGAME_SAVE);
		mnuGAME_SAVE.addActionListener(this);
		mnuGAME.add(mnuGAME_LOAD);
		mnuGAME_LOAD.addActionListener(this);
		mnuGAME.add(mnuGAME_EXIT);
		mnuGAME_EXIT.addActionListener(this);
		MAIN_MENU.add(mnuSTYLE);
		mnuSTYLE.add(mnuSTYLE_TILE);
		for(int i = 0; i < mnuSTYLE_TILE_LIST.length; i++){
			mnuSTYLE_TILE_LIST[i] = new JMenuItem("Style " + i);
			mnuSTYLE_TILE_LIST[i].addActionListener(this);
			mnuSTYLE_TILE.add(mnuSTYLE_TILE_LIST[i]);
		}

		setVisible(true);
		
		//Start game
		instGAME = new ChessGame(pnlCENTER, lblSTATS, MOVE_LIST, true, socket);
		instGAME.colorScheme(colP1[0], colP2[0]);
		pnlCENTER.addMouseListener(instGAME);
		
	}


	public void actionPerformed(ActionEvent event){
		//Menu options
			//STYLE OPTIONS
		for(int i = 0; i < mnuSTYLE_TILE_LIST.length; i++){
			//Check if menu item was clicked
			if(event.getSource() == mnuSTYLE_TILE_LIST[i]){
				//Chnage color scheme
				instGAME.colorScheme(colP1[i], colP2[i]);
			}
		}
			//NEW GAME
		if(event.getSource() == mnuGAME_NEW){
			//JOptionPane.showMessageDialog(null, "New game");
			instGAME.resetGame();
		}
			//SAVE CURRENT GAME
		else if(event.getSource() == mnuGAME_SAVE){
			//If there are no moves to save
			if(instGAME.MOVE_LIST.size() == 0){
				//Cancel game save
				JOptionPane.showMessageDialog(null, "There are no moves to save", "Cannot save an unplayed game", JOptionPane.ERROR_MESSAGE);
				return;		
			}

			//Get Filename from the user
			String FName = JOptionPane.showInputDialog(null, "Enter filename(without the .cg suffix)", "Save Game", JOptionPane.QUESTION_MESSAGE);
			
			//If entered name is invalid, cancel save
			if(FName == null || FName.length() == 0){return;}

			//Create file reference 
			File flGame = new File(FName + ".cg");

			//If the file already exists
			if(flGame.exists()){
				//Ask the user if they wish to overwrite the file
				int ans = JOptionPane.showConfirmDialog(null, "File already exists. Do you wish to overwrite it?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);	
				
				//If yes then delete the current file and create a new one; OTHERWISE cancel save
				if(ans == JOptionPane.YES_OPTION){
					flGame.delete();		
				}else{
					return;
				}
			}

			//Create the save file
			try{
				//Create file
				flGame.createNewFile();
				//Write to the file 
				instGAME.saveGame(flGame);

			}catch(Exception e){}
		
		}
			//LOAD SAVED GAME
		else if(event.getSource() == mnuGAME_LOAD){
			//JOptionPane.showMessageDialog(null, "Load game");
			
			//Get Filename from the user
			String FName = JOptionPane.showInputDialog(null, "Enter filename(without the .cg suffix)", "Load Game", JOptionPane.QUESTION_MESSAGE);

			//Create file reference 
			File flGame = new File(FName + ".cg");

			//If file does not exist
			if(!flGame.exists()){
				//Show error message and exit
				JOptionPane.showMessageDialog(null, "ERROR: File does not exist", "Load error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			//Load game from file
			instGAME.loadGame(flGame);

		}
			//EXIT APPLICATION
		else if(event.getSource() == mnuGAME_EXIT){
			System.exit(0);
		}
		else if(event.getSource() == btnUNDO){
			instGAME.Undo();
		}

	}


}