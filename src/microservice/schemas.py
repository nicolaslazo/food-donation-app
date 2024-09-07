from pydantic import BaseModel
from typing import List, Dict


class BarrioInfo(BaseModel):
    cantidad: int
    nombres: List[str]


class ReporteBarrios(BaseModel):
    __root__: Dict[str, BarrioInfo]
