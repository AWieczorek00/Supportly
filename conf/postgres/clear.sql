-- Krok 1: Wymuszenie zakończenia wszystkich aktywnych połączeń z bazą 'supportly'
-- Jest to konieczne, aby móc ją usunąć.
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'supportly';

-- Krok 2: Usunięcie bazy danych (jeśli istnieje)
-- IF EXISTS zapobiega błędom, jeśli baza jeszcze nie istnieje.
DROP DATABASE IF EXISTS supportly;

-- Krok 3: Usunięcie roli/użytkownika (jeśli istnieje)
-- Opcjonalny krok, jeśli chcesz wyzerować również uprawnienia użytkownika.
DROP ROLE IF EXISTS supportly;

---

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