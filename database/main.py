import mysql.connector
from faker import Faker
import re
import random
from datetime import datetime as dt
from datetime import timedelta
import bcrypt
import tqdm

# Connect to MySQL database
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="12345678PITIATwww$",
    database="uservault"
)
phone_numbers = []
cursor = conn.cursor()

# Clear data in tables
tables = ["calls", "sms", "phonenumbers", "admin_seller", "bills", "client_seller", "clients", "programs", "sellers", "admins"]
for table in tables:
    query = f"DELETE FROM `{table}`"
    cursor.execute(query)
conn.commit()

# Reset auto-increment for each table
for table in tables:
    query = f"ALTER TABLE `{table}` AUTO_INCREMENT = 1"
    cursor.execute(query)
    conn.commit()

# Instantiate Faker
fake = Faker()

# Open the file to store usernames and passwords
with open("user_credentials.txt", "w") as f:
    # Function to generate a valid password
    def generate_password():
        pattern = r"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=.*\S).{8,24}$"
        pw = fake.password(length=random.randint(8, 24), special_chars=True, digits=True, upper_case=True,
                           lower_case=True)
        while not re.match(pattern, pw):
            pw = fake.password(length=random.randint(8, 24), special_chars=True, digits=True, upper_case=True,
                               lower_case=True)
        return pw


    def hash_password(pw):
        hashed = bcrypt.hashpw(pw.encode('utf-8'), bcrypt.gensalt(prefix=b"2a"))
        return hashed.decode('utf-8')


    def check_password_hash(pw, hashed_pw):
        return bcrypt.checkpw(pw.encode('utf-8'), hashed_pw.encode('utf-8'))


    emails = set()


    def check_email(iemail):
        if iemail in emails:
            return False
        if len(iemail) > 255:
            return False
        emails.add(iemail)
        return True


    def random_datetime_previous_month(months):
        current_date = dt.now()

        # Calculate the target month and year
        year = current_date.year
        month = current_date.month - months

        if month <= 0:
            month += 12
            year -= 1

        # Calculate the last day of the target month
        first_day_of_next_month = (dt(year, month, 1) + timedelta(days=31)).replace(day=1)
        last_day_of_target_month = first_day_of_next_month - timedelta(days=1)

        # Generate a random date within the target month
        random_day = random.randint(1, last_day_of_target_month.day)
        random_datetime = dt(year, month, random_day,
                             random.randint(0, 23), random.randint(0, 59), random.randint(0, 59))

        return random_datetime


    def generate_random_timestamp():
        start_date = dt(dt.now().year, 1, 1)
        end_date = dt(dt.now().year, 12, 31)
        delta = end_date - start_date
        random_days = random.randint(0, delta.days)
        random_time = timedelta(seconds=random.randint(0, 24 * 60 * 60))
        random_timestamp = start_date + timedelta(days=random_days) + random_time
        return random_timestamp.strftime('%Y-%m-%d %H:%M:%S')


    # Generate and insert data into admins table
    NUM_ADMINS = 20

    usernames = set()
    # create a progress bar
    for _ in tqdm.tqdm(range(NUM_ADMINS), desc="Inserting admins"):
        username = fake.user_name()[:23]
        while len(username) < 6 or username in usernames:
            username = fake.user_name()[:23]
        usernames.add(username)
        email = fake.email()
        while check_email(email) is False:
            email = fake.email()
        birthday = fake.date_of_birth(minimum_age=18, maximum_age=90)
        while True:
            password = generate_password()
            hashed_password = hash_password(password)
            if check_password_hash(password, hashed_password):
                break
        query = "INSERT INTO admins (username, first_name, last_name, email, birthday, password_hash) VALUES (%s, %s, %s, %s, %s, %s)"
        try:
            cursor.execute(query, (username, fake.first_name(), fake.last_name(), email, birthday, hashed_password))
            # Write the username and password to the file
            f.write(f"admin: {username}, password: [{password}]\n")
        except mysql.connector.IntegrityError as e:
            print(f"Error inserting into admins table: {e}")
    conn.commit()
    print("Admins table populated")

    # Generate and insert data into clients table
    NUM_CLIENTS = 5000
    batch = []
    BATCH_SIZE = 100
    client_vats = set()
    for _ in tqdm.tqdm(range(NUM_CLIENTS), desc="Inserting clients"):
        username = fake.user_name()[:23]
        while len(username) < 6 or username in usernames:
            username = fake.user_name()[:23]
        usernames.add(username)
        email = fake.email()
        while check_email(email) is False:
            email = fake.email()
        birthday = fake.date_of_birth(minimum_age=18, maximum_age=90)
        vat = fake.random_int(100000000, 999999999)
        while vat in client_vats:
            vat = fake.random_int(100000000, 999999999)
        client_vats.add(vat)
        while True:
            password = generate_password()
            hashed_password = hash_password(password)
            if check_password_hash(password, hashed_password):
                break
        query = "INSERT INTO clients (username, first_name, last_name, email, birthday, VAT, password_hash) VALUES (%s, %s, %s, %s, %s, %s, %s)"
        try:
            cursor.execute(query,
                           (username, fake.first_name(), fake.last_name(), email, birthday, vat, hashed_password))
            # Write the username and password to the file
            f.write(f"client: {username}, password: [{password}]\n")
        except mysql.connector.IntegrityError as e:
            print(f"Error inserting into clients table: {e}")
    conn.commit()
    print("Clients table populated")

    # Generate and insert data into sellers table
    NUM_SELLERS = 100
    for _ in tqdm.tqdm(range(NUM_SELLERS), desc="Inserting sellers"):
        username = fake.user_name()[:23]
        while len(username) < 6 or username in usernames:
            username = fake.user_name()[:23]
        usernames.add(username)
        email = fake.email()
        while check_email(email) is False:
            email = fake.email()
        birthday = fake.date_of_birth(minimum_age=18, maximum_age=90)
        while True:
            password = generate_password()
            hashed_password = hash_password(password)
            if check_password_hash(password, hashed_password):
                break
        query = "INSERT INTO sellers (username, first_name, last_name, email, birthday, password_hash) VALUES (%s, %s, %s, %s, %s, %s)"
        try:
            cursor.execute(query, (username, fake.first_name(), fake.last_name(), email, birthday, hashed_password))
            # Write the username and password to the file
            f.write(f"seller: {username}, password: [{password}]\n")
        except mysql.connector.IntegrityError as e:
            print(f"Error inserting into sellers table: {e}")
    conn.commit()
    print("Sellers table populated")
    del usernames

program_names = set()
program_names.add("Program Default")
# Generate and insert data into programs table
NUM_PROGRAMS = 10
for _ in tqdm.tqdm(range(NUM_PROGRAMS), desc="Inserting programs"):
    text = "Program "
    program_name = text + fake.word()
    while program_name in program_names:
        program_name = text + fake.word()
    program_names.add(program_name)
    fixed_cost = fake.pyfloat(left_digits=2, right_digits=2, positive=True, min_value=10, max_value=100)
    cost_per_minute = fake.pyfloat(left_digits=1, right_digits=2, positive=True, min_value=1, max_value=5)
    cost_per_sms = fake.pyfloat(left_digits=1, right_digits=1, positive=True, min_value=1, max_value=3)
    available_minutes = fake.random_int(100, 1000)
    available_sms = fake.random_int(100, 1000)
    query = "INSERT INTO programs (program_name, fixed_cost, cost_per_minute, cost_per_sms, available_minutes, available_sms) VALUES (%s, %s, %s, %s, %s, %s)"
    try:
        cursor.execute(query,
                       (program_name, fixed_cost, cost_per_minute, cost_per_sms, available_minutes, available_sms))
    except mysql.connector.IntegrityError as e:
        print(f"Error inserting into programs table: {e}")
conn.commit()

query = "INSERT INTO programs (program_name, fixed_cost, cost_per_minute, cost_per_sms, available_minutes, available_sms) VALUES (%s, %s, %s, %s, %s, %s)"
try:
    cursor.execute(query, ("Program Default", 0.0, 1.5, 2.0, 0, 0))
except mysql.connector.IntegrityError as e:
    print(f"Error inserting into programs table: {e}")
conn.commit()

print("Programs table populated")
del program_names

# Generate and insert data into phonenumbers table
NUM_PHONE_NUMBERS = random.randint(6000, 10000)
phone_numbers_set = set()
for _ in tqdm.tqdm(range(NUM_PHONE_NUMBERS), desc="Inserting phone numbers"):
    phone_number = fake.numerify(text="693#######")
    while phone_number in phone_numbers_set:
        phone_number = fake.numerify(text="693#######")
    phone_numbers_set.add(phone_number)
    if _ < 5000:
        client_id = _ + 1
    else:
        client_id = fake.random_int(1, NUM_CLIENTS)
    program_id = fake.random_int(1, NUM_PROGRAMS)
    query = "INSERT INTO phonenumbers (phone_number, client_id, program_id) VALUES (%s, %s, %s)"
    try:
        cursor.execute(query, (phone_number, client_id, program_id))
        phone_numbers.append(phone_number)  # Store the phone number
    except mysql.connector.IntegrityError as e:
        print(f"Error inserting into phonenumbers table: {e}")
conn.commit()
print("Phonenumbers table populated")
del phone_numbers_set

calls = set()
# Generate and insert data into calls table for the current month using stored phone numbers
NUM_CALLS = random.randint(500000, 1000000)
for _ in tqdm.tqdm(range(NUM_CALLS), desc="Inserting calls"):
    phone_number = random.choice(phone_numbers)  # Choose a random stored phone number
    timestamp = generate_random_timestamp()
    duration = fake.random_int(1, 60)
    while (phone_number, timestamp) in calls:
        timestamp = generate_random_timestamp()
    calls.add((phone_number, timestamp))
    query = "INSERT INTO calls (phone_number, timestamp, duration) VALUES (%s, %s, %s)"
    cursor.execute(query, (phone_number, timestamp, duration))
conn.commit()
print("Calls table populated")
del calls

sms = set()
# Generate and insert data into sms table for the current month using stored phone numbers
NUM_SMS = random.randint(500000, 1000000)
for _ in tqdm.tqdm(range(NUM_SMS), desc="Inserting sms"):
    phone_number = random.choice(phone_numbers)  # Choose a random stored phone number
    timestamp = generate_random_timestamp()
    message = fake.text()
    while (phone_number, timestamp) in sms:
        timestamp = generate_random_timestamp()
    sms.add((phone_number, timestamp))
    query = "INSERT INTO sms (phone_number, timestamp, message) VALUES (%s, %s, %s)"
    cursor.execute(query, (phone_number, timestamp, message))
conn.commit()
print("SMS table populated")
del sms

datetime0 = random_datetime_previous_month(0)
for client_id in tqdm.tqdm(range(1, NUM_CLIENTS + 1), desc="Calculating bills"):
    try:
        cursor.callproc("CalculateBill", args=(client_id, datetime0))
        conn.commit()
    except mysql.connector.Error as e:
        print(f"Error executing stored procedure for client {client_id}: {e}")
print("Bills calculated for June")
datetime1 = random_datetime_previous_month(1)
for client_id in tqdm.tqdm(range(1, NUM_CLIENTS + 1), desc="Calculating bills"):
    try:
        cursor.callproc("CalculateBill", args=(client_id, datetime1))
        conn.commit()
    except mysql.connector.Error as e:
        print(f"Error executing stored procedure for client {client_id}: {e}")
print("Bills calculated for May")
datetime2 = random_datetime_previous_month(2)
print(datetime2)
for client_id in tqdm.tqdm(range(1, NUM_CLIENTS + 1), desc="Calculating bills"):
    try:
        cursor.callproc("CalculateBill", args=(client_id, datetime2))
        conn.commit()
    except mysql.connector.Error as e:
        print(f"Error executing stored procedure for client {client_id}: {e}")
print("Bills calculated for April")
datetime3 = random_datetime_previous_month(3)
for client_id in tqdm.tqdm(range(1, NUM_CLIENTS + 1), desc="Calculating bills"):
    try:
        cursor.callproc("CalculateBill", args=(client_id, datetime3))
        conn.commit()
    except mysql.connector.Error as e:
        print(f"Error executing stored procedure for client {client_id}: {e}")
print("Bills calculated for March")
datetime4 = random_datetime_previous_month(4)
for client_id in tqdm.tqdm(range(1, NUM_CLIENTS + 1), desc="Calculating bills"):
    try:
        cursor.callproc("CalculateBill", args=(client_id, datetime4))
        conn.commit()
    except mysql.connector.Error as e:
        print(f"Error executing stored procedure for client {client_id}: {e}")
print("Bills calculated for February")
datetime5 = random_datetime_previous_month(5)
for client_id in tqdm.tqdm(range(1, NUM_CLIENTS + 1), desc="Calculating bills"):
    try:
        cursor.callproc("CalculateBill", args=(client_id, datetime5))
        conn.commit()
    except mysql.connector.Error as e:
        print(f"Error executing stored procedure for client {client_id}: {e}")
print("Bills calculated for January")

# Generate and insert data into admin_seller table to establish relationships
for seller_id in tqdm.tqdm(range(1, NUM_SELLERS + 1), desc="Inserting admin_seller"):
    admin_id = fake.random_int(1, NUM_ADMINS)
    query = "INSERT INTO admin_seller (admin_id, seller_id) VALUES (%s, %s)"
    cursor.execute(query, (admin_id, seller_id))
conn.commit()
print("Admin_seller table populated")

# Generate and insert data into client_seller table to establish relationships
for client_id in tqdm.tqdm(range(1, NUM_CLIENTS + 1), desc="Inserting client_seller"):
    seller_id = fake.random_int(1, NUM_SELLERS)  # Assuming there are 100 sellers
    query = "INSERT INTO client_seller (client_id, seller_id) VALUES (%s, %s)"
    cursor.execute(query, (client_id, seller_id))
conn.commit()
print("Client_seller table populated")

# Close connection
cursor.close()
conn.close()
print("Connection closed")
