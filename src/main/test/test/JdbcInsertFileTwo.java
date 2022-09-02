package test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcInsertFileTwo {
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/AssignSecurities";
		String user = "root";
		String password = "root123";

		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement statement = null;
			FileInputStream inputStream = null;

			// String filePath =
			// "E:\\Rash\\gitHome\\quickadmission\\quickadmission\\src\\main\\resources\\templates\\Application_en_US_Template
			// V 1.0.xlsx";
			String filePath = "C:\\Users\\Narendra\\Desktop\\Neerajan\\DataLoad\\Script.xlsx";

//			File resource = new ClassPathResource("templates\\\\Application_en_US_Template V 1.0.xlsx").getFile();

			File resource  = new File(filePath);
			inputStream = new FileInputStream(resource);

			if (resource.getName().contains("Template")) {
				statement = connection.prepareStatement("Update DM_OBJ_TEMPLATE set TEMPLATE_BYTE= ? where OBJ_ID=1");
			} else {
				statement = connection.prepareStatement("Update DM_OBJ_IMPORT set FILE_BYTE =?,STATUS_ID=90796 where id=1");
			}
			statement.setBinaryStream(1, (InputStream) inputStream, (int) (resource.length()));
			int row = statement.executeUpdate();
			if (row > 0) {
				System.out.println("Stored in DB:" + resource.getName());
			}

			System.out.println("<==========================================>");
			filePath = "C:\\Users\\Narendra\\Desktop\\Neerajan\\DataLoad\\RtaData.xlsx";

			resource  = new File(filePath);
			inputStream = new FileInputStream(resource);
			if (resource.getName().contains("Template")) {
				statement = connection.prepareStatement("Update DM_OBJ_TEMPLATE set TEMPLATE_BYTE= ? where OBJ_ID=1");
			} else {
				statement = connection.prepareStatement("Update DM_OBJ_IMPORT set FILE_BYTE =?,STATUS_ID=90796 where id=2");
			}
			statement.setBinaryStream(1, (InputStream) inputStream, (int) (resource.length()));
			row = statement.executeUpdate();
			if (row > 0) {
				System.out.println("Stored in DB:" + resource.getName());
			}
			// connection.commit();
			connection.close();
			// image.delete();
			resource = null;
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
	}
}