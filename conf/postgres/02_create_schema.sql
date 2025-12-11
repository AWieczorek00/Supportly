-- Krok 4: Utworzenie roli/użytkownika 'supportly'
-- Zastąp 'Twoje_Haslo' rzeczywistym hasłem.
CREATE ROLE supportly WITH LOGIN PASSWORD 'Qwerty.1';

-- Krok 5: Utworzenie nowej, pustej bazy danych 'supportly'
CREATE DATABASE supportly OWNER supportly;

---

-- Krok 6: Nadanie wszystkich uprawnień dla roli 'supportly' na nowo utworzonej bazie
GRANT ALL PRIVILEGES ON DATABASE supportly TO supportly;

-- Krok 7: Połączenie z nową bazą danych 'supportly'
\c supportly

-- Krok 8: Utworzenie schematu 'supportly' z autoryzacją dla roli 'supportly'
CREATE SCHEMA IF NOT EXISTS supportly AUTHORIZATION supportly;