from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# Banco de dados SQLite em memória
DATABASE_URL = "sqlite:///./test.db"


# Criar engine para banco em memória
engine = create_engine(DATABASE_URL, echo=True, connect_args={"check_same_thread": False})

# Criar sessão do SQLAlchemy
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Criar base de modelos
Base = declarative_base()
