import psycopg2
import json
import os

from models.db_schema import Question
from dotenv import load_dotenv

load_dotenv()

host = os.getenv("DB_HOST")
database = os.getenv("DB_NAME")
user = os.getenv("DB_USER")
password = os.getenv("DB_PASSWORD")

q = Question(
    question_id="GATE2026_Q1",
    question="What is 2+2?",
    question_type="MCQ",
    options={
        "A":"3",
        "B":"4",
        "C":"5",
        "D":"4.5"
    },
    year=2026,
    section="GA",
    answer="B"
)

conn = psycopg2.connect(
    host=host,
    database=database,
    user=user,
    password=password
)

cur = conn.cursor()

cur.execute("""
Insert into gate_pyq
            (question_id, question, question_type, options, year, section, answer)
            values(%s, %s, %s, %s, %s, %s, %s)""",(
                q.question_id,
                q.question,
                q.question_type,
                json.dumps(q.options),
                q.year,
                q.section,
                json.dumps(q.answer)
            )
)

conn.commit()

cur.close()
conn.close()

print("Values are inserted")