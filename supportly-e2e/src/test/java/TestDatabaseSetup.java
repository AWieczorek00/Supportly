import org.example.BaseE2ETest;
import org.junit.jupiter.api.BeforeAll;

import java.sql.*;
import java.time.LocalDate;

public class TestDatabaseSetup extends BaseE2ETest {

    protected static final String URL = System.getenv().getOrDefault("BASE_URL_DATABASE", "jdbc:sqlserver://192.168.0.81:1110;databaseName=supportly;encrypt=false");
    protected static final String PROFILE = System.getenv().getOrDefault("PROFILE","mssql"); // "postgres", "mssql", "oracle"

    private static final String USER = "supportly";
    private static final String PASS = "Qwerty.1";

    @BeforeAll
    static void setupDatabase() {
        System.out.println("Url= "+URL);
        System.out.println("Profil= "+URL);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            // --- czyszczenie tabel ---
            stmt.executeUpdate("DELETE FROM agreement");
            stmt.executeUpdate("DELETE FROM TASK_EMPLOYEE");
            stmt.executeUpdate("DELETE FROM task");
            switch (PROFILE.toLowerCase()) {
                case "oracle" -> stmt.executeUpdate("DELETE FROM \"ORDER\"");
                case "postgres" -> stmt.executeUpdate("DELETE FROM \"order\"");
                case "mssql" -> stmt.executeUpdate("DELETE FROM [order]");
            }
            stmt.executeUpdate("DELETE FROM client");
            stmt.executeUpdate("DELETE FROM company");
            stmt.executeUpdate("DELETE FROM address");
            stmt.executeUpdate("DELETE FROM employee WHERE PHONE_NUMBER = '502254567'");

            // --- insert address ---
            switch (PROFILE.toLowerCase()) {
                case "oracle" -> {
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (address_seq.NEXTVAL, 'Warszawa', 'Marszałkowska', '00-001', 10)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (address_seq.NEXTVAL, 'Kraków', 'Floriańska', '31-019', 15)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (address_seq.NEXTVAL, 'Gdańsk', 'Długa', '80-827', 7)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (address_seq.NEXTVAL, 'Wrocław', 'Świdnicka', '50-066', 22)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (address_seq.NEXTVAL, 'Poznań', 'Półwiejska', '61-888', 5)");
                }
                case "postgres" -> {
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (1, 'Warszawa', 'Marszałkowska', '00-001', 10)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (2, 'Kraków', 'Floriańska', '31-019', 15)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (3, 'Gdańsk', 'Długa', '80-827', 7)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (4, 'Wrocław', 'Świdnicka', '50-066', 22)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (5, 'Poznań', 'Półwiejska', '61-888', 5)");
                }
                case "mssql" -> {
                    stmt.executeUpdate("SET IDENTITY_INSERT address ON");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (1, 'Warszawa', 'Marszałkowska', '00-001', 10)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (2, 'Kraków', 'Floriańska', '31-019', 15)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (3, 'Gdańsk', 'Długa', '80-827', 7)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (4, 'Wrocław', 'Świdnicka', '50-066', 22)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (5, 'Poznań', 'Półwiejska', '61-888', 5)");
                    stmt.executeUpdate("SET IDENTITY_INSERT address OFF");
                }
            }

            // --- insert company ---
            switch (PROFILE.toLowerCase()) {
                case "oracle" -> {
                    stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (company_seq.NEXTVAL, 'Tech Solutions Sp. z o.o.', '1234567890', 1, '123456789', 'kontakt@techsolutions.pl', '987654321')");
                    stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (company_seq.NEXTVAL, 'InnovaTech S.A.', '9876543210', 2, '987654321', 'biuro@innovatech.pl', '123456789')");
                }
                case "postgres" -> {
                    stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (1, 'Tech Solutions Sp. z o.o.', '1234567890', 1, '123456789', 'kontakt@techsolutions.pl', '987654321')");
                    stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (2, 'InnovaTech S.A.', '9876543210', 2, '987654321', 'biuro@innovatech.pl', '123456789')");
                }
                case "mssql" -> {
                    stmt.executeUpdate("SET IDENTITY_INSERT company ON");
                    stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (1, 'Tech Solutions Sp. z o.o.', '1234567890', 1, '123456789', 'kontakt@techsolutions.pl', '987654321')");
                    stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (2, 'InnovaTech S.A.', '9876543210', 2, '987654321', 'biuro@innovatech.pl', '123456789')");
                    stmt.executeUpdate("SET IDENTITY_INSERT company OFF");
                }
            }

            // --- insert client ---
            switch (PROFILE.toLowerCase()) {
                case "oracle" -> stmt.executeUpdate("INSERT INTO client (id, first_name, second_name, phone_number, email, company_id, type) VALUES (client_seq.NEXTVAL, 'Jan', 'Kowalski', '501234567', 'jan.kowalski@example.com', 1, 'B2B')");
                case "postgres" -> stmt.executeUpdate("INSERT INTO client (id, first_name, second_name, phone_number, email, company_id, type) VALUES (1, 'Jan', 'Kowalski', '501234567', 'jan.kowalski@example.com', 1, 'B2B')");
                case "mssql" -> {
                    stmt.executeUpdate("SET IDENTITY_INSERT client ON");
                    stmt.executeUpdate("INSERT INTO client (id, first_name, second_name, phone_number, email, company_id, type) VALUES (1, 'Jan', 'Kowalski', '501234567', 'jan.kowalski@example.com', 1, 'B2B')");
                    stmt.executeUpdate("SET IDENTITY_INSERT client OFF");
                }
            }

            // --- insert order ---
            switch (PROFILE.toLowerCase()) {
                case "oracle" -> stmt.executeUpdate("""
                INSERT INTO "ORDER" (ID, CLIENT_ID, DATE_OF_ADMISSION, DATE_OF_EXECUTION, AGREEMENT_NUMBER, MAN_HOUR, DISTANCE, PRIORITY, STATUS, PERIOD, NOTE)
                VALUES (order_seq.NEXTVAL, 1, SYSDATE, SYSDATE+7, 'AGR-2025/001', 8.5, 42.0, 'HIGH', 'IN_PROGRESS', 'MONTHLY', 'Zamówienie testowe dla klienta firmowego.')
            """);
                case "postgres" -> stmt.executeUpdate("""
                INSERT INTO "order" (CLIENT_ID, DATE_OF_ADMISSION, DATE_OF_EXECUTION, AGREEMENT_NUMBER, MAN_HOUR, DISTANCE, PRIORITY, STATUS, PERIOD, NOTE)
                VALUES (1, CURRENT_DATE, CURRENT_DATE + INTERVAL '7 day', 'AGR-2025/001', 8.5, 42.0, 'HIGH', 'IN_PROGRESS', 'MONTHLY', 'Zamówienie testowe dla klienta firmowego.')
            """);
                case "mssql" -> stmt.executeUpdate("""
                INSERT INTO [order] (CLIENT_ID, DATE_OF_ADMISSION, DATE_OF_EXECUTION, AGREEMENT_NUMBER, MAN_HOUR, DISTANCE, PRIORITY, STATUS, PERIOD, NOTE)
                VALUES (1, GETDATE(), DATEADD(day,7,GETDATE()), 'AGR-2025/001', 8.5, 42.0, 'HIGH', 'IN_PROGRESS', 'MONTHLY', 'Zamówienie testowe dla klienta firmowego.')
            """);
            }

            // --- insert task ---
            switch (PROFILE.toLowerCase()) {
                case "oracle" -> stmt.executeUpdate("""
                INSERT INTO TASK (ID, NAME, EXECUTION_TIME, ORDER_ID, DONE)
                VALUES (task_seq.NEXTVAL, 'Przygotowanie raportu miesięcznego', SYSDATE+2, (SELECT ID FROM "ORDER" WHERE AGREEMENT_NUMBER='AGR-2025/001'), 0)
            """);
                case "postgres" -> stmt.executeUpdate("""
                INSERT INTO TASK (NAME, EXECUTION_TIME, ORDER_ID, DONE)
                VALUES ('Przygotowanie raportu miesięcznego', CURRENT_DATE + INTERVAL '2 day', (SELECT id FROM "order" WHERE AGREEMENT_NUMBER='AGR-2025/001'), false)
            """);
                case "mssql" -> stmt.executeUpdate("""
                INSERT INTO TASK (NAME, EXECUTION_TIME, ORDER_ID, DONE)
                VALUES ('Przygotowanie raportu miesięcznego', DATEADD(day,2,GETDATE()), (SELECT id FROM [order] WHERE AGREEMENT_NUMBER='AGR-2025/001'), 0)
            """);
            }

            System.out.println("✅ Baza danych została zasilona testowymi danymi.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Url= "+URL);
            System.out.println("Profil= "+URL);
            throw new RuntimeException("❌ Błąd przy przygotowaniu bazy danych.");
        }
    }


}
