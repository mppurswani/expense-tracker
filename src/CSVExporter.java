//csvexporter 
import java.io.FileWriter;
import java.sql.*;

public class CSVExporter {
    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    public static void export() {
        String sql = "SELECT * FROM expenses";
        try(Connection c = DriverManager.getConnection(DB_URL);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            FileWriter fw = new FileWriter("expenses.csv")){
            fw.write("ID,Date,Category,Amount,Description\n");
            while(rs.next()){
                fw.write(rs.getInt("id")+","+rs.getString("date")+","+
                        rs.getString("category")+","+rs.getDouble("amount")+","+
                        rs.getString("description")+"\n");
            }
        } catch(Exception e){ e.printStackTrace(); }
    }
}