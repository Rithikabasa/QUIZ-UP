import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class DeleteAttempts extends Frame 
{
	Button deleteAttemptsButton;
	List attemptsList;
	TextField sidText, quizidText,noText,dateText;
	TextArea errorText;
	Connection connection;
	Statement statement;
	ResultSet rs;
	
	public DeleteAttempts() 
	{
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} 
		catch (Exception e) 
		{
			System.err.println("Unable to find and load driver");
			System.exit(1);
		}
		connectToDB();
	}

	public void connectToDB() 
    {
		try 
		{
		  connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:SHARATH","maha","2000");
		  statement = connection.createStatement();

		} 
		catch (SQLException connectException) 
		{
		  System.out.println(connectException.getMessage());
		  System.out.println(connectException.getSQLState());
		  System.out.println(connectException.getErrorCode());
		  System.exit(1);
		}
    }
	
	private void loadQuiz() 
	{	   
		try 
		{
		  rs = statement.executeQuery("SELECT * FROM Students_quiz");
		  while (rs.next()) 
		  {
			attemptsList.add(rs.getString("STUDENTID"));
		  }
		} 
		catch (SQLException e) 
		{ 
		  displaySQLErrors(e);
		}
	}
	
	public void buildGUI() 
	{		
	    attemptsList = new List(10);
		loadQuiz();
		add(attemptsList);
		
		//When a list item is selected populate the text fields
		attemptsList.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) 
			{
				try 
				{
					rs = statement.executeQuery("SELECT * FROM students_quiz");
					while (rs.next()) 
					{
						if (rs.getString("STUDENTID").equals(attemptsList.getSelectedItem()))
						break;
					}
					if (!rs.isAfterLast()) 
					{
						sidText.setText(rs.getString("STUDENTID"));
						quizidText.setText(rs.getString("quizID"));
						noText.setText(rs.getString("noofattempts"));
						dateText.setText(rs.getString("DAY"));
					}
				} 
				catch (SQLException selectException) 
				{
					displaySQLErrors(selectException);
				}
			}
		});		
		
	    
		//Handle Delete Sailor Button
		deleteAttemptsButton = new Button("Delete Attempts");
		deleteAttemptsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					Statement statement = connection.createStatement();
					int i = statement.executeUpdate("DELETE FROM students_quiz WHERE STUDENTID ='" + attemptsList.getSelectedItem()+"' and quizid='"+quizidText.getText()+"'and noofattempts="+Integer.parseInt(noText.getText())+" and day='" +dateText.getText()+"'" );
					errorText.append("\nDeleted " + i + " rows successfully");
					sidText.setText(null);
					quizidText.setText(null);
					noText.setText(null);
					dateText.setText(null);
					attemptsList.removeAll();
					loadQuiz();
				} 
				catch (SQLException insertException) 
				{
					displaySQLErrors(insertException);
				}
			}
		});
		
		sidText = new TextField(15);
		quizidText = new TextField(15);
		noText = new TextField(15);
		dateText = new TextField(15);
		
		errorText = new TextArea(10, 40);
		errorText.setEditable(false);

		Panel first = new Panel();
		first.setLayout(new GridLayout(4, 2));
		first.add(new Label("Student ID:"));
		first.add(sidText);
		first.add(new Label("QuizID:"));
		first.add(quizidText);
		first.add(new Label("No of Attempts:"));
		first.add(noText);
		first.add(new Label("Date:"));
		first.add(dateText);
		
		Panel second = new Panel(new GridLayout(4, 1));
		second.add(deleteAttemptsButton);
		
		Panel third = new Panel();
		third.add(errorText);
		
		add(first);
		add(second);
		add(third);
	    
		setTitle("Remove Attempts");
		setSize(450, 600);
		setLayout(new FlowLayout());
		setVisible(true);
		
	}

	

	private void displaySQLErrors(SQLException e) 
	{
		Frame f=new Frame();
		JOptionPane.showMessageDialog(f,"Enter valid data types");  
		errorText.append("\nSQLException: " + e.getMessage() + "\n");
		errorText.append("SQLState:     " + e.getSQLState() + "\n");
		errorText.append("VendorError:  " + e.getErrorCode() + "\n");
	}

	

	
}