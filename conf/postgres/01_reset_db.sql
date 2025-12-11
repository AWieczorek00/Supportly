-- Krok 1: Wymuszenie zakończenia wszystkich aktywnych połączeń z bazą 'supportly'
-- (Zostawiamy bez zmian - to jest OK)
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'supportly'
  AND pid <> pg_backend_pid();

-- Krok 2: Usunięcie bazy danych
-- Usuwamy bazę, ale NIE użytkownika (bo na nim pracujemy)
DROP DATABASE IF EXISTS supportly;

-- Krok 3: Utworzenie nowej, pustej bazy danych
-- Użytkownik 'supportly' już istnieje, więc przypisujemy go jako właściciela
CREATE DATABASE supportly OWNER supportly;

-- Krok 4: Nadanie uprawnień (dla pewności)
GRANT ALL PRIVILEGES ON DATABASE supportly TO supportly;