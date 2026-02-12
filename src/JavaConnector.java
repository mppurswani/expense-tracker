public class JavaConnector {
    private final ExpenseDAO dao = new ExpenseDAO();

    public void addExpense(String date, String category, double amount, String description){ 
        dao.addExpense(date,category,amount,description); 
    }
    public void editExpense(int id, String date, String category, double amount, String description){ 
        dao.editExpense(id,date,category,amount,description);
    }
    public void deleteExpense(int id){ 
        dao.deleteExpense(id); 
    }
    public void clearAllExpenses(){ 
        dao.clearAllExpenses(); 
    }
    public void exportCSV(){ 
        CSVExporter.export(); 
    }
    public String getAllExpenses(){ 
        return dao.getAllExpensesAsJson(); 
    }
    
    // FIXED MAIN METHOD
    public static void main(String[] args) {
        System.out.println("🚀 Expense Tracker Ready!");
        System.out.println("✅ Database: expenses.db");
        System.out.println("📱 Use ExpenseTrackerApp.java for WebView");
        new JavaConnector();  // Creates DB table
        try { Thread.sleep(Long.MAX_VALUE); } catch(Exception e){}
    }
}