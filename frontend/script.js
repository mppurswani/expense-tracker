console.log("🚀 Expense Tracker Loaded");

/* =========================
   JavaConnector Fallback
========================= */
if (!window.javaConnector) {
    window.javaConnector = {
        expenses: JSON.parse(localStorage.getItem('expenses') || '[]'),

        getAllExpenses() {
            return JSON.stringify(this.expenses);
        },

        addExpense(date, category, amount, desc) {
            const expense = {
                id: Date.now(),
                date: date,
                category: category,
                amount: parseFloat(amount),
                description: desc
            };
            this.expenses.push(expense);
            localStorage.setItem('expenses', JSON.stringify(this.expenses));
            return true;
        },

        editExpense(id, date, category, amount, desc) {
            this.expenses = this.expenses.map(e =>
                e.id == id ? { id, date, category, amount: parseFloat(amount), description: desc } : e
            );
            localStorage.setItem('expenses', JSON.stringify(this.expenses));
        },

        deleteExpense(id) {
            this.expenses = this.expenses.filter(e => e.id != id);
            localStorage.setItem('expenses', JSON.stringify(this.expenses));
        },

        clearAllExpenses() {
            this.expenses = [];
            localStorage.removeItem('expenses');
        }
    };
}

/* =========================
   ADD BUTTON FIXED
========================= */
document.getElementById("addGlobalBtn").addEventListener("click", function () {

    const date = document.getElementById("exp-date").value;
    const category = document.getElementById("exp-category").value;
    const amount = document.getElementById("exp-amount").value;
    const desc = document.getElementById("exp-desc").value;

    if (!date || !category || !amount) {
        alert("Please fill Date, Category and Amount.\nFormat: YYYY-MM-DD");
        return;
    }

    window.javaConnector.addExpense(date, category, amount, desc);

    document.getElementById("exp-date").value = "";
    document.getElementById("exp-category").value = "";
    document.getElementById("exp-amount").value = "";
    document.getElementById("exp-desc").value = "";

    loadExpenses();
});

/* =========================
   MONTH DISPLAY
========================= */
const monthNames = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

function loadExpenses() {
    const data = JSON.parse(window.javaConnector.getAllExpenses() || "[]");
    updateMonthsDisplay(data);
}

function getMonthName(dateStr) {
    const d = new Date(dateStr);
    return monthNames[d.getMonth()];
}

function updateMonthsDisplay(data) {

    const container = document.getElementById("monthsContainer");
    container.innerHTML = "";

    monthNames.forEach(month => {

        const monthExpenses = data.filter(e => getMonthName(e.date) === month);
        const total = monthExpenses.reduce((sum, e) => sum + e.amount, 0);

        const monthDiv = document.createElement("div");
        monthDiv.classList.add("month-row");

        monthDiv.innerHTML = `
            <h3>${month} (₹${total.toFixed(2)})</h3>
            <div class="month-content" style="display:none;">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Category</th>
                            <th>Amount</th>
                            <th>Description</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
                <canvas width="250" height="200"></canvas>
            </div>
        `;

        container.appendChild(monthDiv);

        const header = monthDiv.querySelector("h3");
        const content = monthDiv.querySelector(".month-content");
        const tbody = monthDiv.querySelector("tbody");
        const canvas = monthDiv.querySelector("canvas");

        monthExpenses.forEach(e => {

            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${e.date}</td>
                <td>${e.category}</td>
                <td>₹${e.amount.toFixed(2)}</td>
                <td>${e.description || ""}</td>
                <td>
                    <button onclick="editExpense(${e.id})">✏️</button>
                    <button onclick="deleteExpense(${e.id})">🗑️</button>
                </td>
            `;

            tbody.appendChild(row);
        });

        header.onclick = () => {
            const show = content.style.display === "none";
            content.style.display = show ? "block" : "none";

            if (show && monthExpenses.length > 0) {
                drawPieChart(canvas, monthExpenses);
            }
        };
    });
}

/* =========================
   EDIT
========================= */
function editExpense(id) {
    const data = JSON.parse(window.javaConnector.getAllExpenses());
    const exp = data.find(e => e.id == id);

    const newDate = prompt("Date (YYYY-MM-DD):", exp.date);
    const newCat = prompt("Category:", exp.category);
    const newAmt = prompt("Amount:", exp.amount);
    const newDesc = prompt("Description:", exp.description);

    if (!newDate || !newCat || !newAmt) return;

    window.javaConnector.editExpense(id, newDate, newCat, newAmt, newDesc);
    loadExpenses();
}

/* =========================
   DELETE
========================= */
function deleteExpense(id) {
    if (!confirm("Delete this expense?")) return;
    window.javaConnector.deleteExpense(id);
    loadExpenses();
}

/* =========================
   CLEAR ALL
========================= */
document.getElementById("clearAllBtn").addEventListener("click", function () {
    if (!confirm("Clear all expenses?")) return;
    window.javaConnector.clearAllExpenses();
    loadExpenses();
});

/* =========================
   EXPORT
========================= */
document.getElementById("exportCSVBtn").addEventListener("click", function () {

    const data = JSON.parse(window.javaConnector.getAllExpenses() || "[]");

    let csv = "ID,Date,Category,Amount,Description\n";

    data.forEach(e => {
        csv += `${e.id},${e.date},${e.category},${e.amount},${e.description}\n`;
    });

    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = "expenses.csv";
    a.click();
});

/* =========================
   REFRESH
========================= */
document.getElementById("refreshBtn").addEventListener("click", loadExpenses);

/* =========================
   DARK MODE
========================= */
document.getElementById("themeToggle").addEventListener("click", function () {
    document.body.classList.toggle("dark");
});

/* =========================
   INIT
========================= */
window.onload = loadExpenses;