public class Expense {
    public int id;
    public String date, category, description;
    public double amount;

    public Expense(int id, String date, String category, double amount, String description) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }
}