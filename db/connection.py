import psycopg2

conn = psycopg2.connect(
    host = "localhost",
    database = "gate_cs",
    user = "postgres",
    password = "D@i0s99933"
)

print("Connected")

conn.close()