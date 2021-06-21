import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class DeleteGets extends Frame 
{
	Button deleteGetsButton;
	List getsList;
	TextField sidText, scridText,dateText;
	TextArea errorText;
	Connection connection;
	Statement statement;
	ResultSet rs;
	
	public DeleteGets() 
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
	
	private void loadGets() 
	{	   
		try 
		{
		  rs = statement.executeQuery("SELECT * FROM Students_score");
		  while (rs.next()) 
		  {
			getsList.add(rs.getString("STUDENTID"));
		  }
		} 
		catch (SQLException e) 
		{ 
		  displaySQLErrors(e);
		}
	}
	
	public void buildGUI() 
	{		
	    getsList = new List(10);
		loadGets();
		add(getsList);
		
		//When a list item is selected populate the text fields
		getsList.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e) 
			{
				try 
				{
					rs = statement.executeQuery("SELECT * FROM students_score");
					while (rs.next()) 
					{
						if (rs.getString("STUDENTID").equals(getsList.getSelectedItem()))
						break;
					}
					if (!rs.isAfterLast()) 
					{
						sidText.setText(rs.getString("STUDENTID"));
						scridText.setText(rs.getString("SCRID"));
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
		deleteGetsButton = new Button("Delete Gets");
		deleteGetsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{
					Statement statement = connection.createStatement();
					int i = statement.executeUpdate("DELETE FROM students_score WHERE STUDENTID ='" + getsList.getSelectedItem()+"' and scrid='"+scridText.getText()+"'and day='" +dateText.getText()+"'" );
					errorText.append("\nDeleted " + i + " rows successfully");
					sidText.setText(null);
					scridText.setText(null);
					dateText.setText(null);
					getsList.removeAll();
					loadGets();
				} 
				catch (SQLException insertException) 
				{
					displaySQLErrors(insertException);
				}
			}
		});
		
		sidText = new TextField(15);
		scridText = new TextField(15);
		dateText = new TextField(15);
		
		errorText = new TextArea(10, 40);
		errorText.setEditable(false);

		Panel first = new Panel();
		first.setLayout(new GridLayout(4, 2));
		first.add(new Label("Student ID:"));
		first.add(sidText);
		first.add(new Label("ScrID:"));
		first.add(scridText);
		first.add(new Label("Date:"));
		first.add(dateText);
		
		Panel second = new Panel(new GridLayout(4, 1));
		second.add(deleteGetsButton);
		
		Panel third = new Panel();
		third.add(errorText);
		
		add(first);
		add(second);
		add(third);
	    
		setTitle("Remove Gets");
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