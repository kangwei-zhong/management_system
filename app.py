from flask import Flask, render_template, request, redirect
import sqlite3

app = Flask(__name__)

def init_db():
    conn = sqlite3.connect('employees.db')
    c = conn.cursor()
    c.execute('''CREATE TABLE IF NOT EXISTS employees
                 (id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL,
                  department TEXT NOT NULL)''')
    conn.commit()
    conn.close()

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/employees', methods=['GET', 'POST'])
def employees():
    conn = sqlite3.connect('employees.db')
    c = conn.cursor()

    if request.method == 'POST':
        name = request.form['name']
        department = request.form['department']
        # SQL Injection Vulnerability
        c.execute(f"INSERT INTO employees (name, department) VALUES ('{name}', '{department}')")
        conn.commit()
        return redirect('/employees')

    c.execute("SELECT * FROM employees")
    employees = c.fetchall()
    conn.close()
    return render_template('employees.html', employees=employees)

if __name__ == '__main__':
    init_db()
    app.run(debug=True)