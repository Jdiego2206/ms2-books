import mysql.connector
import random
import uuid

conn = mysql.connector.connect(
    host="localhost",
    port=3307,
    database="ms2db",
    user="root",
    password="secreto123"
)
cur = conn.cursor()

CATEGORIES = ['Fiction', 'Science', 'History', 'Philosophy',
              'Horror', 'Romance', 'Biography', 'Technology', 'Art', 'Poetry']

print("Inserting categories...")
for name in CATEGORIES:
    cur.execute("""
        INSERT INTO categories (name, description, created_at, active)
        VALUES (%s, %s, NOW(), 1)
        ON DUPLICATE KEY UPDATE name=name
    """, (name, "Category " + name))
conn.commit()

cur.execute("SELECT id FROM categories")
category_ids = [row[0] for row in cur.fetchall()]

AUTHORS = ['George Orwell', 'Jane Austen', 'Mark Twain', 'Leo Tolstoy',
           'Gabriel Garcia Marquez', 'Fyodor Dostoevsky', 'Ernest Hemingway']

print("Inserting 20000 books...")
for i in range(20000):
    isbn = str(uuid.uuid4()).replace("-", "")[:13]
    cur.execute("""
        INSERT INTO books (title, author, isbn, description, price, stock,
                          user_id, available, active, category_id, created_at, updated_at)
        VALUES (%s, %s, %s, %s, %s, %s, %s, 1, 1, %s, NOW(), NOW())
    """, (
        "Book Title " + str(i + 1),
        random.choice(AUTHORS),
        isbn,
        "Description for book " + str(i + 1),
        round(random.uniform(5.0, 100.0), 2),
        random.randint(1, 50),
        random.randint(1, 500),
        random.choice(category_ids)
    ))
    if i % 1000 == 0:
        conn.commit()
        print(str(i) + "/20000 books inserted...")

conn.commit()
print("20000 books inserted")

cur.execute("SELECT id FROM books")
book_ids = [row[0] for row in cur.fetchall()]

TYPES = ['LOAN', 'EXCHANGE', 'SALE']
STATUSES = ['REQUESTED', 'ACCEPTED', 'DELIVERED', 'COMPLETED']

print("Inserting transactions...")
for i in range(5000):
    book_id = random.choice(book_ids)
    cur.execute("SELECT user_id FROM books WHERE id = %s", (book_id,))
    seller_id = cur.fetchone()[0]
    buyer_id = random.randint(1, 500)
    price = round(random.uniform(5.0, 100.0), 2)
    qty = random.randint(1, 3)
    cur.execute("""
        INSERT INTO transactions (book_id, buyer_id, seller_id, quantity,
                                  total_price, type, status, active, created_at, updated_at)
        VALUES (%s, %s, %s, %s, %s, %s, %s, 1, NOW(), NOW())
    """, (
        book_id, buyer_id, seller_id, qty,
        round(price * qty, 2),
        random.choice(TYPES),
        random.choice(STATUSES)
    ))
    if i % 1000 == 0:
        conn.commit()
        print(str(i) + "/5000 transactions inserted...")

conn.commit()
print("5000 transactions inserted")
cur.close()
conn.close()
print("Done.")