from pydantic import BaseModel
from typing import Dict, Optional, List, Union

class Question(BaseModel):
    question_id: str
    question: str
    question_type: str
    options: Dict[str, str]
    year: int
    section: str
    topic: Optional[str] = None
    answer: Union[str, List[str]]