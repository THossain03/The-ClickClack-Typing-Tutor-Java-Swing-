/* Names: Tameem H. and Shaik K.
Date: 2019-09-20
Course Code: ICS 3U1- 2/4 and 4/3
Teacher: Ms. Lal
Project Name: Typing Tutor
Project Description: An online typing tester that will allow you to get better at typing over time. There is a timer that allows the computer to also measure the time it took for the user to type down the given task.
*/

//imports 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image;
import java.util.*;
import java.lang.*;
import java.time.*;
import java.io.*;
import java.text.DecimalFormat; 

public class Main {

	// the following is a group of all static variables that will be used for many methods in this program.
	//the static timer machine.
	static javax.swing.Timer timer;
	// static decimal format for representing time on the timer.
	static DecimalFormat df = new DecimalFormat("##");
  //the static difficulty selection variable (for accessing all methods).
	static int difficulty = 0;
	// the static difficuly variable during the games (accesible for all methods).
	static int currDifficulty = 0;
	//static variable used for knowing what action to perform for changing difficulty (depending on if the game has started or not).
	static boolean startGameFlag = false;	
	//the static arrays of letters.
	static String [] difficultListofLetters = {"a", "b" , "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", ".", "," ,"1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "+", "=", ">", "<", "/", " "};
	static String [] averageListofLetters = {"a", "b" , "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", ".", ",", " "};
	static String [] easyListofLetters = {"a", "b" , "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " "};
	static String newText = ""; //static variable to show new text for user to enter.
	static double startTime = 0; //static variable representing the start time for timer.
	static double elapsedTime = 0; //static variable reprenting the end time for timer.
	static double elapsedSeconds = 0; //static variable representing tje elapsed seconds.
	static double secondsDisplay = 0; //static variable reprenting the seconds in whole number form.
	static double elapsedMinutes = 0; //static variable showing minutes elapsed.
	static double totTimeRequire = 0; //static variable for counting total time required for all questions in seconds. (using elapsedSeconds as collector var).
	static int numofCharacters = 0; //static variable to count total number of characters entered throughout tutoring process.
	static int totnum_correctin1Ans = 0;  //static variable to calculate total number of answers that were right the first time submitted.
	static int totnum_mistakes = 0; //static variable to count the total amount of mistakes.
	static int num_mistakes = 0; //static variable to count number of mistakes it took to pass one level.
	static int easyDifficultyMaster = 0; //determines whether user has completed and mastered at least 12 easy prompts.
	static boolean masterEasy = false;
	static int averageDifficultyMaster = 0; //determines whether user has completed and mastered at least 12 average prompts.
	static boolean masterAverage = false;
	static int difficultDifficultyMaster = 0; //determines whether user has completed and mastered at least 15 difficult prompts.
	static boolean masterDifficult = false;

	//method for a new difficulty being reached, or when the user starts the typing tutor.
	public static void newDifficulty () {
		if (currDifficulty == 1) {
			JOptionPane.showMessageDialog(null, "In Easy mode, you will learn how to type letters a-z in lower-case form,\nand learn how to use the spacebar");
			newText = newTextGenerator(12); // stores a 12-character text value.
		} else if (currDifficulty == 2) {
			JOptionPane.showMessageDialog(null, "In Average mode, you will learn how to type letters in upper-case,\nand will also be introduced to the period(.) and comma (,)");
			newText = newTextGenerator(20); //stores a 20-character text value.
		} else if (currDifficulty == 3) {
			JOptionPane.showMessageDialog(null, "In Difficult mode, you will learn how to type numbers and various other symbols\non the keyboard (most of which are located to the right of letters)");
			newText = newTextGenerator(40); // stores a 40 character text value.
		}
	}
	//the method for giving the user a different text to type.
	public static String newTextGenerator (int minimumOfLetters) {
		newText = "";
		Random rand = new Random();
		int randLength = rand.nextInt(5) + minimumOfLetters; //selects a rand number of letters between minimum and 5 above minimum.
		numofCharacters += randLength;
		for (int i = 0; i<randLength; i++) {
			if (currDifficulty == 1) {
				newText += easyListofLetters[rand.nextInt(easyListofLetters.length)];
			} 
			if (currDifficulty == 2) {
				newText += averageListofLetters[rand.nextInt(averageListofLetters.length)];
			}
			if (currDifficulty == 3) {
				newText += difficultListofLetters[rand.nextInt(difficultListofLetters.length)];
			}
		}
		return newText;
	}

	//a decimal format that will help format the following doubles used for calculations (in the showResults method).
	static DecimalFormat statShow = new DecimalFormat ("##.##");
	static double accuracy = 0;
	static double grossWPM = 0;
	static double netWPM = 0;
	static double avgtimeBYchar = 0; 
	// the method for calculating the results at the end of a game, or before a user wants to quit.
	public static void calcResults () {
		if ((totnum_correctin1Ans + totnum_mistakes) < 1) {
			accuracy = 0;
		} else {
			accuracy = (easyDifficultyMaster + averageDifficultyMaster + difficultDifficultyMaster)/(totnum_correctin1Ans + totnum_mistakes);
			accuracy = accuracy * 100;
		}
		//accuracy = (easyDifficultyMaster + averageDifficultyMaster + difficultDifficultyMaster)/(totnum_correctin1Ans + totnum_mistakes);
		//accuracy = accuracy * 100;
		grossWPM = (numofCharacters/5)/(totTimeRequire/60);
		netWPM = grossWPM - (totnum_mistakes/(totTimeRequire/60));
		avgtimeBYchar = totTimeRequire/numofCharacters;
	}

	// main code starts here.
	public static void main(String [] args) {
		//site introductory statement.
		JOptionPane.showMessageDialog(null, "Welcome to the typing tutor. Press OK to continue");

		//states temporary username and password in variables.
		String realUsername = "abc";
		String realPassword = "123";

		//login frame design.
		JFrame login = new JFrame("Login Page"); //header or title.
		final JLabel title_login = new JLabel();            
    title_login.setBounds(20,150, 200,50);  
    JLabel l1=new JLabel("Username:");    //username label for users.
    l1.setBounds(20,20, 100,40);    
    JLabel l2=new JLabel("Password:");    // password label for users.
    l2.setBounds(20,75, 100,40);    
		JLabel l3= new JLabel("Temporary Username: abc"); //show temporary username to input for logging in.
		l3.setBounds(60,140, 260,30);
		JLabel l4 = new JLabel("Temporary Password: 123"); //show temporary password to input in order to log in.
		l4.setBounds(60,190, 225, 30);
    JButton login_btn = new JButton("Login");  // login button.
    login_btn.setBounds(100,250, 120,60);
	  final JTextField username = new JTextField();  //username text box input
    username.setBounds(100,25, 175,40);    
		final JPasswordField password = new JPasswordField();   //password text box input
    password.setBounds(100,80, 175,40);  
    login.add(password); login.add(l1); login.add(title_login); login.add(l2); login.add(login_btn); login.add(l3); login.add(l4); login.add(username);  //adds all elements into login frame.  
    login.setSize(400,400);    //sets size of frame.
    login.setLayout(null);    
		login.setVisible(true); // allows frame to show
		
		//requires frame to be declared so login click may lead to new screen (if username and password is correct).
		JFrame type_Screen = new JFrame("The Typing Tutor (by Tameem and Shaik)");
		type_Screen.setSize(750,600);
		type_Screen.setLayout(null);
   	//login actions by event.
		login_btn.addActionListener( new ActionListener() 	{
			public void actionPerformed(ActionEvent e) { 
				if ((realUsername.equals(username.getText())) && (realPassword.equals(new String(password.getPassword())))) {
					login.setVisible(false); // changes screens from login to typing tutor screens.
					type_Screen.setVisible(true);			
				} else {
					JOptionPane.showMessageDialog(login, "Username or Password is Incorrect", "Alert!", JOptionPane.WARNING_MESSAGE);
				}
			}
		});		

		// ****
		//main user Interface (type_Screen frame).
		JButton diff_btn_in_type = new JButton(" Select Difficulty ");
		diff_btn_in_type.setBounds(480,50, 225,35);
		JLabel instruct_Lbl1 = new JLabel("Please select a difficulty by");
		instruct_Lbl1.setBounds(225,120, 250,25);
		JLabel instruct_Lbl2 = new JLabel("clicking the difficulty selection");
		instruct_Lbl2.setBounds(225,150, 250,25);
		JLabel instruct_Lbl3 = new JLabel("button before starting the game.");
		instruct_Lbl3.setBounds(225,180, 250,25);
		JButton hints_btn = new JButton("Hints"); 
		hints_btn.setBounds(40,50, 125,50);
		JButton start_btn = new JButton("Click here to Start");
		start_btn.setBounds(500,500, 180,65);
		JTextArea typeText_area = new JTextArea();
		typeText_area.setBounds(200,300, 325,150);
		JTextArea theText = new JTextArea();
		theText.setBounds(200, 168, 325, 75);
		JLabel theText_userlbl = new JLabel("Your given text:");
		theText_userlbl.setBounds(50,200, 140,15);
		JLabel typeText_userlbl = new JLabel("Write answer here -->");
		typeText_userlbl.setBounds(25,375, 165,15);
		Font f1 = new Font("TimesRoman", Font.BOLD, 15);
		theText_userlbl.setFont(f1); typeText_userlbl.setFont(f1);
		JButton quit_btn = new JButton("Quit");
		quit_btn.setBounds(50,540, 100,40);
		JButton timecheck_btn = new JButton("Check Elapsed time");
		timecheck_btn.setBounds(180,540, 200,40);
		JButton checkAns_btn = new JButton("Submit Answer");
		checkAns_btn.setBounds(550,250, 175,35);
		JLabel instruct_Lbl4 = new JLabel ("Please type out the given text in");
		instruct_Lbl4.setBounds(225,70, 250, 20);
		JLabel instruct_Lbl5 = new JLabel ("the bottom text area, and make sure");
		instruct_Lbl5.setBounds(225,100, 250,20);
		JLabel instruct_Lbl6 = new JLabel ("not to make any errors!");
		instruct_Lbl6.setBounds(225,130, 250,20);  
		type_Screen.add(diff_btn_in_type); type_Screen.add(hints_btn); type_Screen.add(start_btn); type_Screen.add(instruct_Lbl1); type_Screen.add(instruct_Lbl2); type_Screen.add(instruct_Lbl3); type_Screen.add(typeText_area);type_Screen.add(theText);type_Screen.add(theText_userlbl);type_Screen.add(quit_btn);type_Screen.add(timecheck_btn); type_Screen.add(checkAns_btn); type_Screen.add(typeText_userlbl); type_Screen.add(instruct_Lbl4); type_Screen.add(instruct_Lbl5); type_Screen.add(instruct_Lbl6);
		typeText_area.setVisible(false); theText.setVisible(false); theText_userlbl.setVisible(false); quit_btn.setVisible(false); timecheck_btn.setVisible(false);checkAns_btn.setVisible(false); typeText_userlbl.setVisible(false); instruct_Lbl4.setVisible(false); instruct_Lbl5.setVisible(false); instruct_Lbl6.setVisible(false);


		//following are global swing variables reuired for the difficulty selection frame.
		JRadioButton opt1; JRadioButton opt2; JRadioButton opt3; 	JButton diffSelect_btn;	JFrame diffChooser;
    //following is a frame that will prompt (in radio button format) for the difficulty of the upcoming tasks.
		diffChooser = new JFrame("Choose Difficulty");
		opt1 = new JRadioButton ("Easy");
		opt1.setBounds(70,50,100,30);
		opt2 = new JRadioButton ("Average");
		opt2.setBounds(70,100,100,30);
		opt3 = new JRadioButton ("Difficult");
		opt3.setBounds(70,150, 100,30);
		ButtonGroup bg = new ButtonGroup();
		bg.add(opt1);bg.add(opt2); bg.add(opt3);
		diffSelect_btn = new JButton("Select");
		diffSelect_btn.setBounds(70,220, 100, 50);
  	diffChooser.add(opt1);
    diffChooser.add(opt2); 
    diffChooser.add(opt3); 
    diffChooser.add(diffSelect_btn);
		diffChooser.setSize(400,400);
		diffChooser.setLayout(null);
		
		//events for showing and storing the difficulty user wants to start with.
		diff_btn_in_type.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				diffChooser.setVisible(true);
			}
		});
		diffSelect_btn.addActionListener( new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				String diffLabel = "";
				if (opt1.isSelected()) { //stores difficulty based on which option is selected.
					diffLabel = "Easy";
					difficulty = 1;
				} else if (opt2.isSelected()) {
					diffLabel = "Average";
					difficulty = 2;
			  }	else if (opt3.isSelected()) {
					diffLabel = "Difficult";
					difficulty = 3;
				} 
				JOptionPane.showMessageDialog(diffChooser ,"You have selected " + diffLabel + " mode.");
				diff_btn_in_type.setText("Difficulty: " + diffLabel); // displays difficulty chosen on button.
				diffChooser.setVisible(false); //exits the difficulty selection frame, and resumes to typing screen.
				if (startGameFlag == true) {
					currDifficulty = difficulty;
					newDifficulty(); //sets a new text for the new difficulty mode.
					theText.setText(newText);
					startTime = System.currentTimeMillis();
					timer.start();
				}
			}
		});
		
		// event when start button is clicked.

		start_btn.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				if (difficulty == 0) {
					JOptionPane.showMessageDialog(type_Screen, "Please select a difficulty before starting the game", "Alert!", JOptionPane.WARNING_MESSAGE);
				} else {
					currDifficulty = difficulty;
					start_btn.setVisible(false); instruct_Lbl1.setVisible(false); instruct_Lbl2.setVisible(false); instruct_Lbl3.setVisible(false);
					typeText_area.setVisible(true); theText.setVisible(true); theText_userlbl.setVisible(true); quit_btn.setVisible(true); timecheck_btn.setVisible(true); checkAns_btn.setVisible(true); typeText_userlbl.setVisible(true); instruct_Lbl4.setVisible(true); instruct_Lbl5.setVisible(true); instruct_Lbl6.setVisible(true);
					startGameFlag = true;
					newDifficulty();
					theText.setText(newText);
					startTime = System.currentTimeMillis();
					timer.start();
				}
			}
		});

		//frame for showing elapsed time.
		JFrame timecheck_screen = new JFrame("Timer");
		timecheck_screen.setSize(200,200);
		JLabel timeDisplay = new JLabel("00:00");
		timeDisplay.setBounds(0,0, 200, 200);
		Font f2 = new Font("TimesRoman", Font.BOLD, 24);
		timeDisplay.setFont(f2);
		timecheck_screen.add(timeDisplay);
		timer = new javax.swing.Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        elapsedTime = System.currentTimeMillis() - startTime;
				elapsedSeconds = elapsedTime / 1000;
    		secondsDisplay = elapsedSeconds % 60;
   			elapsedMinutes = elapsedSeconds / 60;
				timeDisplay.setText(df.format(elapsedMinutes) + ":" + df.format(secondsDisplay));
      }
    });

		//action listener to display time check screen.
		timecheck_btn.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				timecheck_screen.setVisible(true);
			}
		});

		//frame showing that today's class has come to an end, and that there is still an opportunity to continue practicing. Stats are shown here.
		JFrame endofgame_screen = new JFrame("The End!");
		endofgame_screen.setSize(600,400);
		endofgame_screen.setLayout(null);
		JButton practice_btn = new JButton("Click here to continue practing");
		practice_btn.setBounds(10,350, 580,25);
		JButton officialQuit_btn = new JButton("Quit");
		officialQuit_btn.setBounds(475, 120, 125, 90);
		JLabel accuracy_lbl = new JLabel();
		accuracy_lbl.setBounds(60,25, 400, 20);
		JLabel grossWPM_lbl = new JLabel();
		grossWPM_lbl.setBounds(60,50, 400, 20);
		JLabel netWPM_lbl = new JLabel();
		netWPM_lbl.setBounds(60,75, 400, 20);
		JLabel avgtimeBYchar_lbl = new JLabel();
		avgtimeBYchar_lbl.setBounds(60,100, 400, 20);
		Font f3 = new Font("TimesRoman", Font.BOLD, 12);
		accuracy_lbl.setFont(f3); grossWPM_lbl.setFont(f3); netWPM_lbl.setFont(f3); avgtimeBYchar_lbl.setFont(f3);
		JLabel timePlayed_lbl = new JLabel();
		timePlayed_lbl.setBounds(60, 175, 400, 20);
		String doubleline = "Start Over \nwith new Data";
    JButton startOverBtn = new JButton("<html>" + doubleline.replaceAll("\\n", "<br>") + "</html>");
    startOverBtn.setBounds(475, 240, 125, 90);
		endofgame_screen.add(practice_btn); endofgame_screen.add(officialQuit_btn); endofgame_screen.add(startOverBtn);

	  //action listener to start over
    startOverBtn.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e)  {
        endofgame_screen.setVisible(false);
        type_Screen.setVisible(true);
        difficulty = 0;
        currDifficulty = 0;
        startGameFlag = false;	
        totTimeRequire = 0;
        numofCharacters = 0; 
	      totnum_correctin1Ans = 0;  
	      totnum_mistakes = 0; 
        num_mistakes = 0; 
        easyDifficultyMaster = 0; 
	      masterEasy = false;
        averageDifficultyMaster = 0; 
	      masterAverage = false;
	      difficultDifficultyMaster = 0; 
	      masterDifficult = false;
        start_btn.setVisible(true); instruct_Lbl1.setVisible(true); instruct_Lbl2.setVisible(true); instruct_Lbl3.setVisible(true);
				typeText_area.setVisible(false); theText.setVisible(false); theText_userlbl.setVisible(false); quit_btn.setVisible(false); timecheck_btn.setVisible(false); checkAns_btn.setVisible(false); typeText_userlbl.setVisible(false); instruct_Lbl4.setVisible(false); instruct_Lbl5.setVisible(false); instruct_Lbl6.setVisible(false);
      }
    });

  
   	// action listeners to end game.
		quit_btn.addActionListener(new ActionListener() {
				public void actionPerformed (ActionEvent e) {
					endofgame_screen.setVisible(true);
					type_Screen.setVisible(false);
					calcResults(); //calculates for many of the following variables used in the labels, and decimal format statshow converts variable to string format.
					accuracy_lbl.setText("Accuracy: " + statShow.format(accuracy) +"%"); 
					grossWPM_lbl.setText("Gross WPM: " + statShow.format(grossWPM) + " WPM");
					netWPM_lbl.setText("Net WPM: " + statShow.format(netWPM) + " WPM");
					avgtimeBYchar_lbl.setText("Average Time per 1 character: " + statShow.format(avgtimeBYchar) + " s./char");
					double elapsedMinutesforTable = totTimeRequire/60;
					double secondsDisplayforTable = totTimeRequire%60;
					timePlayed_lbl.setText("Total Time Played: " + df.format(elapsedMinutesforTable) + " m   " + df.format(secondsDisplayforTable) + " s.");
					String difficultyData[][] = {{"LEVEL", "CORRECT ANSWERS", "MASTERED?"},{"Easy", Integer.toString(easyDifficultyMaster), Boolean.toString(masterEasy)}, {"Average", Integer.toString(averageDifficultyMaster), Boolean.toString(masterAverage)}, {"Difficult", Integer.toString(difficultDifficultyMaster), Boolean.toString(masterDifficult)}};
					String columnlbl[] = {"", "", ""};
					JTable results_table = new JTable (difficultyData,columnlbl); //a table that represents and level-based result and performance.
					results_table.setBounds(60, 200, 300, 130);
					endofgame_screen.add(accuracy_lbl); endofgame_screen.add(grossWPM_lbl); endofgame_screen.add(netWPM_lbl); endofgame_screen.add(avgtimeBYchar_lbl); endofgame_screen.add(timePlayed_lbl); endofgame_screen.add(results_table);  
				}
		});
		officialQuit_btn.addActionListener(new ActionListener() { // officially exits the user from program.
			public void actionPerformed (ActionEvent e) {
				int lastCall = JOptionPane.showConfirmDialog(endofgame_screen, "Are you sure?");
				if (lastCall == JOptionPane.YES_OPTION) {
					endofgame_screen.setVisible(false);
				} 
			}
		});

		// action listener for continuing to practice.
		practice_btn.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				type_Screen.setVisible(true);
				type_Screen.setTitle("Typing Tutor >>>(Practice Mode)<<< ");
				endofgame_screen.setVisible(false);
			}
		});

		//event for checking the answer and finding whether it is correct.
		checkAns_btn.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				timer.stop();
				if (typeText_area.getText().equals(theText.getText())) { //asks if answer is correct.
					JOptionPane.showMessageDialog(null, "Great job! You got it all correct. It took you  " + num_mistakes + " mistakes for you to pass this level.");
					JOptionPane.showMessageDialog(null, "The time needed for this level was " + df.format(elapsedMinutes) + " minutes and " + df.format(secondsDisplay) + " seconds. Press OK for next Q.");
					if (num_mistakes>0) { // locates number of previous mistakes on this question.
						totnum_mistakes += num_mistakes;
						num_mistakes = 0;
					} else {
						totnum_correctin1Ans++;
					}
					// following if-statements asks if level has been mastered or not, if yes, it would make user move on to next level. 
					if (currDifficulty == 1) {
						easyDifficultyMaster ++;
						if (easyDifficultyMaster == 12) {
							masterEasy = true;
							currDifficulty ++;
							JOptionPane.showMessageDialog(null, "Congratulations! You have now completely mastered the Easy Level.\nYou will now advance to Average mode -->");
							newDifficulty();
							diff_btn_in_type.setText("Difficulty: Average");
						} else {
							theText.setText(newTextGenerator(12));
						}
					}
					if (currDifficulty == 2) {
						averageDifficultyMaster ++;
						if (averageDifficultyMaster == 12) {
							masterAverage = true;
							currDifficulty ++;
							JOptionPane.showMessageDialog(null, "Congratulations! You have now completely mastered the Average Level.\nYou will now advance to Difficult mode -->");
							newDifficulty();
							diff_btn_in_type.setText("Difficulty: Difficult");
						} else {
							theText.setText(newTextGenerator(20));
						}
					}
					if (currDifficulty == 3) {
						difficultDifficultyMaster ++;
						if (difficultDifficultyMaster == 12) {
							masterDifficult = true;
							if ((masterAverage = true) && (masterEasy = true)) {
								JOptionPane.showMessageDialog (null, "Congratulations! You have officially finished your session.\nTo see you results or to continue practicing, press OK.");
								type_Screen.setVisible(false);
								endofgame_screen.setVisible(true); //ends the game here.
								calcResults(); //calculates for many of the following variables used in the labels, and decimal format statshow converts variable to string format.
								accuracy_lbl.setText("Accuracy: " + statShow.format(accuracy) +"%"); 
								grossWPM_lbl.setText("Gross WPM: " + statShow.format(grossWPM) + " WPM");
								netWPM_lbl.setText("Net WPM: " + statShow.format(netWPM) + " WPM");
								avgtimeBYchar_lbl.setText("Average Time per 1 character: " + statShow.format(avgtimeBYchar) + " s./char");
								double elapsedMinutesforTable = totTimeRequire/60;
								double secondsDisplayforTable = totTimeRequire%60;
								timePlayed_lbl.setText("Total Time Played: " + df.format(elapsedMinutesforTable) + " m   " + df.format(secondsDisplayforTable) + " s.");
								String difficultyData[][] = {{"LEVEL", "CORRECT ANSWERS", "MASTERED?"},{"Easy", Integer.toString(easyDifficultyMaster), Boolean.toString(masterEasy)}, {"Average", Integer.toString(averageDifficultyMaster), Boolean.toString(masterAverage)}, {"Difficult", Integer.toString(difficultDifficultyMaster), Boolean.toString(masterDifficult)}};
								String columnlbl[] = {"", "", ""};
								JTable results_table = new JTable (difficultyData,columnlbl); //a table that represents and level-based result and performance.
								results_table.setBounds(60, 200, 300, 130);
								endofgame_screen.add(accuracy_lbl); endofgame_screen.add(grossWPM_lbl); endofgame_screen.add(netWPM_lbl); endofgame_screen.add(avgtimeBYchar_lbl); endofgame_screen.add(timePlayed_lbl); endofgame_screen.add(results_table); 
							} else {
								JOptionPane.showMessageDialog(null, "Congratulations! You have now completely mastered the Difficult Level.\nPlease complete the other two levels in order to finish the session. -->");
							}
						}
						theText.setText(newTextGenerator(40));
					}
				} else {
					num_mistakes++;
					int index_mistake = 0; // used to determine the index at which the mistake occured. This variable will be used to determine the character at that spot.
					String a = theText.getText();
					String b = typeText_area.getText();
					char letterA = ' '; char letterB = ' ';
					if  (a.length() != b.length()) {
						JOptionPane.showMessageDialog(null, "Please enter the same amount of characters from your given text, as this will cause you 1 mistake!");
					} else {
						for (int j = 0; j < a.length(); j++) {
							letterA = a.charAt(j);
							letterB = b.charAt(j);
							if (letterA != letterB) { // asks if letters at the specific positions match or not.
								index_mistake = j; // if yes, index of mistake is stored.
							}
						}
						JOptionPane.showMessageDialog(null, "That is incorrect. The " + (index_mistake+1) + "th letter is '" + a.charAt(index_mistake) + "', but you instead wrote '" + b.charAt(index_mistake) +"'.");
					}
				}
				totTimeRequire += elapsedSeconds;
				startTime = System.currentTimeMillis();
				timer.start(); 
			} 
		}); 

		//action listener for hints. Will display a JOption message based on the current level the user is on.
		hints_btn.addActionListener(new ActionListener() 	{
			public void actionPerformed(ActionEvent e) { 
				if (currDifficulty == 0) {
					JOptionPane.showMessageDialog(type_Screen, "Sorry, there is no hints available now.\nIn order to enable hints, the game will need to be started.", "Hints", JOptionPane.WARNING_MESSAGE);
				}
				if (currDifficulty == 1) {
					JOptionPane.showMessageDialog(type_Screen, "Letters:\n2nd Row (top): q w e r t y u i o p\n3rd Row (middle): a s d f g h j k l\n 4th Row (bottom): z x c v b n m\n \n- Spacebar is under the letters, and beside the two alternates (Alt)", "Hints", JOptionPane.INFORMATION_MESSAGE);
				}
				if (currDifficulty == 2) {
					JOptionPane.showMessageDialog(type_Screen, "- 4th Row: Shift Key is to the left of Z (pressing and holding creates upper-case letters)\n- 3rd Row: CAPS LOCK is to the left of A (pressing once allows upper-case spelling)\n- 4th Row: period(.) and comma(,) is to the right of M (no shift or CAPS needed).\n", "Hints", JOptionPane.INFORMATION_MESSAGE);
				}                                               
				if (currDifficulty == 3) {
					JOptionPane.showMessageDialog(type_Screen, "- 1st Row (numbers): Numbers are located here, also with (-) located right beside digit 0.\n- 1st Row: Holding onto Shift Key and pressing number will give you the symbol above it.\n- 3rd Row: (;) and (') produced without shift beside L, while (:) and ('') produced with Shift\n- 4th Row: (<) and (>) produced when shift is used upon comma (,) or period (.).\n- 4th Row: (/) located beside period (.), and holding onto shift will produce (?).", "Hints", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});                                                                                 

		                                                                               
  }
}