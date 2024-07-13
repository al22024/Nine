import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.Timestamp;
//
//import dbtest.ProjectInfo;
//
public class Test {
//public static void main(String[] args) { 
//         UserInfo test = new UserInfo(); 
//         test.userID = 1; 
//         test.name = "テスト"; 
//         test.email = "test@gmail.com"; 
//         test.setUserInfo(); 
//}
	public static void main(String[] args) {
		/*ProjectInfo pre = new ProjectInfo();
		ProjectInfo PInfo = new ProjectInfo();
		PInfo.dateTime = new Timestamp(System.currentTimeMillis());
		PInfo.category = "スポーツ";
		PInfo.destination = "test dest";
		PInfo.managerID = 1;
		PInfo.progressStatus = "test progress";
		PInfo.projectID = 1;
		PInfo.projectName = "let's play sports!";
		PInfo.region = "東京都";*/
		String server = "//172.27.0.15/"; // seserverのIPアドレス
		String dataBase = "g9_db";
		String user = "group09";
		String passWord = "grp9";
		String url = "jdbc:postgresql:" + server + dataBase;
//		String server = "//postgresql:5432/"; 
//		String dataBase = "test1";
//		String user = "oops";
//		String passWord = "pass";
//		String url = "jdbc:postgresql:" + server + dataBase;
		try {//プロジェクトアップデート
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, user, passWord);

            String sql = "CREATE TABLE UsersTableNinth(\r\n"
            		+ "    UserID INTEGER PRIMARY KEY,\r\n"
            		+ "    DisplayName VARCHAR(50),\r\n"
            		+ "    AuthToken VARCHAR(300),\r\n"
            		+ "    Email VARCHAR(50),\r\n"
            		+ "    CalendarInfo TEXT\r\n"
            		+ ");\r\n"
            		+ "\r\n"
            		+ "CREATE TABLE ProjectsTableNinth(\r\n"
            		+ "    ProjectID INTEGER PRIMARY KEY,\r\n"
            		+ "    Name VARCHAR(50),\r\n"
            		+ "    DateTime TIMESTAMP,\r\n"
            		+ "    Category VARCHAR(50),\r\n"
            		+ "    Destination VARCHAR(50),\r\n"
            		+ "    ManagerID INTEGER,\r\n"
            		+ "    Region VARCHAR(50),\r\n"
            		+ "    ProgressStatus VARCHAR(50),\r\n"
            		+ "    FOREIGN KEY (ManagerID) REFERENCES UsersTableNinth(UserID)\r\n"
            		+ ");\r\n"
            		+ "\r\n"
            		+ "CREATE TABLE UserAndProjectsDetailsTableNinth(\r\n"
            		+ "    UserID INTEGER,\r\n"
            		+ "    ProjectID INTEGER,\r\n"
            		+ "    Genre VARCHAR(50),\r\n"
            		+ "    Budget1 VARCHAR(50),\r\n"
            		+ "    Budget2 VARCHAR(50),\r\n"
            		+ "    Review VARCHAR(50),\r\n"
            		+ "    PRIMARY KEY(UserID, ProjectID),\r\n"
            		+ "    FOREIGN KEY (UserID) REFERENCES UsersTableNinth(UserID),\r\n"
            		+ "    FOREIGN KEY (ProjectID) REFERENCES ProjectsTableNinth(ProjectID)\r\n"
            		+ ");";
            Statement stmt = con.createStatement();
            stmt.execute(sql);
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//		PInfo.setProjectInfo();//新規プロジェクト設定
		/*PInfo = pre.getProjectInfo(1);
		System.out.println("dateTime:" + PInfo.dateTime.toString());
		System.out.println("category:" + PInfo.category);
		System.out.println("dest:" + PInfo.destination);
		System.out.println("maneger:" + PInfo.managerID);
		System.out.println("progress:" + PInfo.progressStatus);
		System.out.println("projectID:" + PInfo.projectID);
		System.out.println("projectName:" + PInfo.projectName);
		System.out.println("region:" + PInfo.region);*/
	}
}

