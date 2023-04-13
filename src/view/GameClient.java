package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import model.BoardListener;
import model.GamePhase;
import model.GamePiece;
import model.NineMensMorris;
import model.actions.BoardAction;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import agents.GUIAgent;
import agents.LoggerAgent;
import agents.LoggerOutput;
import agents.MinimaxAgent;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class GameClient extends JFrame implements BoardListener,LoggerOutput{

	private static final long serialVersionUID = -8320637700246905143L;
	// Game engine
	private NineMensMorris game;
	// Label that keep track of game phase
	private JLabel lblGamePhase;
	// Label that keep track of the turn player
	private JLabel lblGameTurn;
	// Canvas
	private NineMensMorrisCanvas canvas;
	// Used to establish the depth of the minimax tree
	private JSpinner spinnerDepth;
	// Log of the moves made in the game
	private JTextArea movesTextArea;
	// Log all the information received by the LoggerAgent
	private JTextArea agentsTextArea;
	private ImageIcon whitePieceIcon,blackPieceIcon;
	
	protected GUIAgent guiAgent;

	public GameClient() {
		super("Nine Men's Morris - Nicholas Gazzo");
		initialize();
	}

	private void initialize() {
		// Icons (from https://www.flaticon.com/), made by: Freepik,Pixel perfect,Smashicon,Kiranshastry, dmitri13
		ImageIcon menuIcon = new ImageIcon(new ImageIcon(GameClient.class.getResource("/icon/menu.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		ImageIcon gameIcon = new ImageIcon(new ImageIcon(GameClient.class.getResource("/icon/game.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		ImageIcon settingsIcon = new ImageIcon(new ImageIcon(GameClient.class.getResource("/icon/settings.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		whitePieceIcon = new ImageIcon(new ImageIcon(GameClient.class.getResource("/icon/white_piece.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		blackPieceIcon = new ImageIcon(new ImageIcon(GameClient.class.getResource("/icon/black_piece.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		// Client Bounds
		setBounds(0, 0, 520, 570);
		getContentPane().setLayout(new BorderLayout(0, 0));
		// Tab Section
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Calibri", Font.BOLD, 14));
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		// Menu Tab
		JPanel menuPanel = new JPanel();
		tabbedPane.addTab("Menu", menuIcon, menuPanel, null);
		GridBagLayout gbl_menuPanel = new GridBagLayout();
		gbl_menuPanel.columnWidths = new int[]{50, 200, 200, 0};
		gbl_menuPanel.rowHeights = new int[]{50, 0, 80, 80, 80, 0};
		gbl_menuPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_menuPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0,0.0};
		menuPanel.setLayout(gbl_menuPanel);
		menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		JLabel menuTitle = new JLabel("Nine Men's Morris");
		menuTitle.setFont(new Font("Calibri", Font.BOLD, 40));
		GridBagConstraints gbc_menuTitle = new GridBagConstraints();
		gbc_menuTitle.anchor = GridBagConstraints.NORTH;
		gbc_menuTitle.insets = new Insets(0, 0, 5, 0);
		gbc_menuTitle.gridwidth = 2;
		gbc_menuTitle.gridx = 1;
		gbc_menuTitle.gridy = 0;
		menuPanel.add(menuTitle, gbc_menuTitle);
		
		JLabel menuSubtitle = new JLabel("Nicholas Gazzo, 4498892");
		menuSubtitle.setFont(new Font("Calibri", Font.PLAIN, 20));
		GridBagConstraints gbc_menuSubtitle = new GridBagConstraints();
		gbc_menuSubtitle.fill = GridBagConstraints.VERTICAL;
		gbc_menuSubtitle.gridwidth = 2;
		gbc_menuSubtitle.insets = new Insets(0, 0, 5, 0);
		gbc_menuSubtitle.gridx = 1;
		gbc_menuSubtitle.gridy = 1;
		menuPanel.add(menuSubtitle, gbc_menuSubtitle);
		
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.setFont(new Font("Calibri", Font.PLAIN, 30));
		GridBagConstraints gbc_btnNewGame = new GridBagConstraints();
		gbc_btnNewGame.gridwidth = 2;
		gbc_btnNewGame.gridx = 1;
		gbc_btnNewGame.gridy = 2;
		menuPanel.add(btnNewGame, gbc_btnNewGame);
		
		JButton btnSaveGame = new JButton("Save Game");
		btnSaveGame.setFont(new Font("Calibri", Font.PLAIN, 30));
		GridBagConstraints gbc_btnSaveGame = new GridBagConstraints();
		gbc_btnSaveGame.gridwidth = 2;
		gbc_btnSaveGame.gridx = 1;
		gbc_btnSaveGame.gridy = 3;
		menuPanel.add(btnSaveGame, gbc_btnSaveGame);
		
		JButton btnLoadGame = new JButton("Load Game");
		btnLoadGame.setFont(new Font("Calibri", Font.PLAIN, 30));
		GridBagConstraints gbc_btnLoadGame = new GridBagConstraints();
		gbc_btnLoadGame.gridwidth = 2;
		gbc_btnLoadGame.gridx = 1;
		gbc_btnLoadGame.gridy = 4;
		menuPanel.add(btnLoadGame, gbc_btnLoadGame);
		
		// Game Tab
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new BorderLayout(0, 0));
		tabbedPane.addTab("Game", gameIcon, gamePanel, null);
		JPanel gameInfo = new JPanel();
		gamePanel.add(gameInfo,BorderLayout.NORTH);
		
		JLabel lblCurrentPhase = new JLabel("Current phase: ");
		lblCurrentPhase.setFont(new Font("Calibri", Font.BOLD, 14));
		gameInfo.add(lblCurrentPhase);
		
		lblGamePhase = new JLabel();
		lblGamePhase.setFont(new Font("Calibri", Font.BOLD, 14));
		gameInfo.add(lblGamePhase);
		
		JLabel lblCurrentTurn = new JLabel("Current player: ");
		lblCurrentTurn.setFont(new Font("Calibri", Font.BOLD, 14));
		gameInfo.add(lblCurrentTurn);
		
		lblGameTurn = new JLabel();
		lblGameTurn.setFont(new Font("Calibri", Font.BOLD, 14));
		gameInfo.add(lblGameTurn);
		
		canvas = new NineMensMorrisCanvas(GamePiece.WHITE_PIECE);
		gamePanel.add(canvas, BorderLayout.CENTER);
		
		btnNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choose = JOptionPane.YES_OPTION;
				if(game != null && game.getPhase() != GamePhase.GAME_ENDED) 
					choose = JOptionPane.showConfirmDialog(GameClient.this, "You already have a game in progress, would you like to start a new game?","New Game",JOptionPane.YES_NO_OPTION);
				if(choose == JOptionPane.YES_OPTION) {
					// Create a new game
					game = new NineMensMorris();
					// 
					startGame(game);
					// Change tab
					tabbedPane.setSelectedComponent(gamePanel);
				}
			}
		});
		
		btnSaveGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(game != null && game.getTurn() == GamePiece.WHITE_PIECE) {
					try {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileFilter(new TxtFileFilter());
						int choose = fileChooser.showSaveDialog(GameClient.this);
						if (choose == JFileChooser.APPROVE_OPTION) {
							File f = fileChooser.getSelectedFile();
							BufferedWriter write = new BufferedWriter(new FileWriter(f));
							write.append(game.toString());
							write.flush();
							write.close();
						}
					}catch(Exception ex) {
						JOptionPane.showMessageDialog(GameClient.this, "There was an error while trying to save the game. Please try again.", "Error Saving Game", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
		});
		
		btnLoadGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NineMensMorris newGame = null;
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new TxtFileFilter());
					int choose = fileChooser.showOpenDialog(GameClient.this);
					if (choose == JFileChooser.APPROVE_OPTION) {
						File f = fileChooser.getSelectedFile();
						BufferedReader read = new BufferedReader(new FileReader(f));
						String line = read.readLine();
						newGame = new NineMensMorris(line);
						read.close();
					}
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(GameClient.this, "Impossible to retrieve the game saved in the selected file. Please choose a valid file.", "Error Loading Game", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(newGame != null) {
					int choose = JOptionPane.YES_OPTION;
					if(game != null && game.getPhase() != GamePhase.GAME_ENDED) 
						choose = JOptionPane.showConfirmDialog(GameClient.this, "You already have a game in progress, would you like to start a new game?","New Game",JOptionPane.YES_NO_OPTION);
					if(choose == JOptionPane.YES_OPTION) {
						startGame(newGame);
						// Change tab
						tabbedPane.setSelectedComponent(gamePanel);
					}
				}
			}
		});
		// Info Tab
		JPanel infoPanel = new JPanel();
		tabbedPane.addTab("Info", settingsIcon, infoPanel, "Change the preferences of the game and the agents.");
		
		GridBagLayout gbl_infoPanel = new GridBagLayout();
		gbl_infoPanel.columnWidths = new int[]{20, 230, 230, 0};
		gbl_infoPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_infoPanel.rowHeights = new int[]{35, 200, 35, 35, 20, 150, 0};
		gbl_infoPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		infoPanel.setLayout(gbl_infoPanel);
		
		JLabel lblLogMoves = new JLabel("Game moves log");
		lblLogMoves.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogMoves.setFont(new Font("Calibri", Font.BOLD, 25));
		GridBagConstraints gbc_lblLogMoves = new GridBagConstraints();
		gbc_lblLogMoves.insets = new Insets(0, 0, 5, 0);
		gbc_lblLogMoves.gridwidth = 2;
		gbc_lblLogMoves.gridx = 1;
		gbc_lblLogMoves.gridy = 0;
		infoPanel.add(lblLogMoves, gbc_lblLogMoves);
		
		movesTextArea = new JTextArea();
		movesTextArea.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		movesTextArea.setFont(new Font("Calibri", Font.BOLD, 14));
		movesTextArea.setLineWrap(true);
		movesTextArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(movesTextArea);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		infoPanel.add(scrollPane, gbc_scrollPane);
		
		JLabel lblAgentSettings = new JLabel("Agent Settings");
		lblAgentSettings.setFont(new Font("Calibri", Font.BOLD, 25));
		lblAgentSettings.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblAgentSettings = new GridBagConstraints();
		gbc_lblAgentSettings.insets = new Insets(0, 0, 5, 0);
		gbc_lblAgentSettings.gridwidth = 2;
		gbc_lblAgentSettings.gridx = 1;
		gbc_lblAgentSettings.gridy = 2;
		infoPanel.add(lblAgentSettings, gbc_lblAgentSettings);
		
		JLabel lblMinMaxDepth = new JLabel("MinMax Tree Depth");
		lblMinMaxDepth.setFont(new Font("Calibri", Font.BOLD, 14));
		GridBagConstraints gbc_lblMinMaxDepth = new GridBagConstraints();
		gbc_lblMinMaxDepth.anchor = GridBagConstraints.WEST;
		gbc_lblMinMaxDepth.insets = new Insets(0, 0, 5, 5);
		gbc_lblMinMaxDepth.gridx = 1;
		gbc_lblMinMaxDepth.gridy = 3;
		infoPanel.add(lblMinMaxDepth, gbc_lblMinMaxDepth);
		
		spinnerDepth = new JSpinner();
		spinnerDepth.setModel(new SpinnerNumberModel(3, 1, 10, 1));
		spinnerDepth.setFont(new Font("Calibri", Font.PLAIN, 18));
		spinnerDepth.setValue(3);
		GridBagConstraints gbc_spinnerDepth = new GridBagConstraints();
		gbc_spinnerDepth.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerDepth.anchor = GridBagConstraints.WEST;
		gbc_spinnerDepth.gridx = 2;
		gbc_spinnerDepth.gridy = 3;
		spinnerDepth.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Runnable run = new Runnable() {
					@Override
					public void run() {
						guiAgent.settingsChanged((Integer)spinnerDepth.getValue());
					}
				};
				SwingUtilities.invokeLater(run);
			}
		});
		infoPanel.add(spinnerDepth, gbc_spinnerDepth);
		
		JLabel lblAgentLog = new JLabel("Agent Log");
		lblAgentLog.setFont(new Font("Calibri", Font.BOLD, 14));
		GridBagConstraints gbc_lblAgentLog = new GridBagConstraints();
		gbc_lblAgentLog.anchor = GridBagConstraints.WEST;
		gbc_lblAgentLog.insets = new Insets(0, 0, 5, 5);
		gbc_lblAgentLog.gridx = 1;
		gbc_lblAgentLog.gridy = 4;
		infoPanel.add(lblAgentLog, gbc_lblAgentLog);
		
		agentsTextArea = new JTextArea();
		agentsTextArea.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		agentsTextArea.setFont(new Font("Calibri", Font.BOLD, 14));
		agentsTextArea.setLineWrap(true);
		agentsTextArea.setEditable(false);
		
		JScrollPane scrollPane_1 = new JScrollPane(agentsTextArea);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 5;
		infoPanel.add(scrollPane_1, gbc_scrollPane_1);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 * Remove the previous game and update the all references with 
	 * the new game.
	 */
	private void startGame(NineMensMorris game) {
		// Set this as the new game
		this.game = game;
		// Attach the game to the canvas
		canvas.setGame(game);
		// Attach this object as a listener
		game.addListener(GameClient.this);
		// Trigger to update labels
		onPhaseChange(null);
		// Reset text area
		movesTextArea.setText("");
	}
	
	
	/*
	 * Submit an action with the use of a swing worker, when the action is performed
	 * repaint the canvas.
	 */
	public void performBoardAction(BoardAction action) {
		if(game != null) {
			(new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground() throws Exception {
					game.performAction(action);
					return null;
				}
				
				@Override
				protected void done() {
					canvas.repaint();
				}
				
			}).execute();
		}
	}
	
	/*
	 * Update the game labels on the game tab with current phase and current turn.
	 */
	private void updateGameLabels() {
		if(game != null) {
			lblGamePhase.setText(GamePhase.toString(game.getPhase()));
			lblGameTurn.setIcon(game.getTurn() == GamePiece.WHITE_PIECE ? whitePieceIcon : blackPieceIcon);
			lblGameTurn.setText(GamePiece.toString(game.getTurn()));
		}
	}

	@Override
	public void onBoardAction(BoardAction action, GamePiece turnPlayer) {
		movesTextArea.append(turnPlayer.toString() + " : " + action + "\n");
	}

	@Override
	public void onChangeTurn(GamePiece newTurnPlayer) {
		updateGameLabels();
		if(newTurnPlayer==GamePiece.BLACK_PIECE) {
			Runnable run = new Runnable() {
				@Override
				public void run() {
					guiAgent.turnChanged(game.toString());
				}
			};
			SwingUtilities.invokeLater(run);
		}
	}

	@Override
	public void onPhaseChange(GamePhase newPhase) {
		updateGameLabels();
		if(newPhase == GamePhase.GAME_ENDED) {
			JOptionPane.showMessageDialog(this, game.getTurn() == GamePiece.WHITE_PIECE ? "You won!" : "You lost!", "Game ended", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	@Override
	public void logMessage(String who,String message) {
		appendAgentsTextArea(who+" : "+message);
	}
	
	@Override
	public void logError(String who,String message) {
		appendAgentsTextArea("<ERROR>\n"+who+" : "+message);
	}
	
	private void appendAgentsTextArea(String message) {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				agentsTextArea.append(message+"\n");
			}
		};
		SwingUtilities.invokeLater(run);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameClient window = new GameClient();
					// Runtime and Container
					jade.core.Runtime rt = jade.core.Runtime.instance();
					AgentContainer container = rt.createMainContainer(new ProfileImpl());
					// Create the agents
					GUIAgent guiAgent = new GUIAgent(window);
					MinimaxAgent minimaxAgent = new MinimaxAgent();
					LoggerAgent loggerAgent = new LoggerAgent(window);
					// Add the agent to the container and retrieve the controllers
					AgentController guiController = container.acceptNewAgent("GUIAgent", guiAgent);
					AgentController minimaxController = container.acceptNewAgent("MinimaxAgent", minimaxAgent);
					AgentController loggerController = container.acceptNewAgent("LoggerAgent", loggerAgent);
					// Start the agents
					loggerController.start();
					guiController.start();
					minimaxController.start();
					window.guiAgent = guiAgent;
					// Set the window as visible
					window.setVisible(true);					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
