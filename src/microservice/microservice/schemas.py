from pydantic import BaseModel, RootModel
from typing import List, Dict


class BarrioInfo(BaseModel):
    cantidad: int
    nombres: List[str]


ReporteBarrios = RootModel[Dict[str, BarrioInfo]]
