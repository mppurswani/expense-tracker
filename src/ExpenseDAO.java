//expensedao.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    public ExpenseDAO() {
        try { Class.forName("org.sqlite.JDBC"); createTable(); }
        catch (Exception e){ e.printStackTrace(); }
    }

    private Connection connect() throws SQLException { return DriverManager.getConnection(DB_URL); }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "category TEXT," +
                "amount REAL," +
                "description TEXT)";
        try(Connection c = connect(); Statement s = c.createStatement()){ s.execute(sql); }
        catch(SQLException e){ e.printStackTrace(); }
    }

    public void addExpense(String date, String category, double amount, String description){
        String sql = "INSERT INTO expenses(date,category,amount,description) VALUES(?,?,?,?)";
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, date); ps.setString(2, category); ps.setDouble(3, amount); ps.setString(4, description); ps.executeUpdate();
        } catch(SQLException e){ e.printStackTrace(); }
    }

    public void editExpense(int id, String date, String category, double amount, String description){
        String sql = "UPDATE expenses SET date=?, category=?, amount=?, description=? WHERE id=?";
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, date); ps.setString(2, category); ps.setDouble(3, amount); ps.setString(4, description); ps.setInt(5,id); ps.executeUpdate();
        } catch(SQLException e){ e.printStackTrace(); }
    }

    public void deleteExpense(int id){
        String sql = "DELETE FROM expenses WHERE id=?";
        try(Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)){ ps.setInt(1,id); ps.executeUpdate(); }
        catch(SQLException e){ e.printStackTrace(); }
    }

    public String getAllExpensesAsJson() {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses ORDER BY date ASC";
        try(Connection c = connect(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)){
            while(rs.next()){
                list.add(new Expense(rs.getInt("id"), rs.getString("date"), rs.getString("category"), rs.getDouble("amount"), rs.getString("description")));
            }
        } catch(SQLException e){ e.printStackTrace(); }

        StringBuilder sb = new StringBuilder("[");
        for(int i=0;i<list.size();i++){
            Expense e = list.get(i);
            sb.append("{\"id\":").append(e.id)
              .append(",\"date\":\"").append(e.date)
              .append("\",\"category\":\"").append(e.category)
              .append("\",\"amount\":").append(e.amount)
              .append(",\"description\":\"").append(e.description).append("\"}");
            if(i<list.size()-1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public void clearAllExpenses() {
        try(Connection c = connect(); Statement s = c.createStatement()){
            s.executeUpdate("DELETE FROM expenses");
            s.executeUpdate("DELETE FROM sqlite_sequence WHERE name='expenses'");
        } catch(SQLException e){ e.printStackTrace(); }
    }
}