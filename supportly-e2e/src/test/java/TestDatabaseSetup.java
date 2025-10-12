import org.example.BaseE2ETest;
import org.junit.jupiter.api.BeforeAll;

import java.sql.*;
import java.time.LocalDate;

public class TestDatabaseSetup extends BaseE2ETest {

    protected static final String URL = System.getenv().getOrDefault("BASE_URL_DATABASE", "jdbc:sqlserver://192.168.0.81:1110;databaseName=supportly;encrypt=false");
    protected static final String PROFILE = System.getenv().getOrDefault("PROFILE","postgres"); // "postgres", "mssql", "oracle"

    private static final String USER = "supportly";
    private static final String PASS = "Qwerty.1";

    @BeforeAll
    static void setupDatabase() {
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
                case "postgres", "mssql" -> {
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (1, 'Warszawa', 'Marszałkowska', '00-001', 10)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (2, 'Kraków', 'Floriańska', '31-019', 15)");
                }
                case "oracle" -> {
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (address_seq.NEXTVAL, 'Warszawa', 'Marszałkowska', '00-001', 10)");
                    stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (address_seq.NEXTVAL, 'Kraków', 'Floriańska', '31-019', 15)");
                }
            }

            // --- insert company (analogicznie) ---
            switch (PROFILE.toLowerCase()) {
                case "postgres", "mssql" -> {
                    stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (1, 'Tech Solutions Sp. z o.o.', '1234567890', 1, '123456789', 'kontakt@techsolutions.pl', '987654321')");
                }
                case "oracle" -> {
                    stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (company_seq.NEXTVAL, 'Tech Solutions Sp. z o.o.', '1234567890', 1, '123456789', 'kontakt@techsolutions.pl', '987654321')");
                }
            }

            // --- insert order / task / task_employee ---
            switch (PROFILE.toLowerCase()) {
                case "postgres" -> {
                    stmt.executeUpdate("INSERT INTO \"order\" (CLIENT_ID, DATE_OF_ADMISSION, DATE_OF_EXECUTION, AGREEMENT_NUMBER, MAN_HOUR, DISTANCE, PRIORITY, STATUS, PERIOD, NOTE) " +
                            "VALUES (1, CURRENT_DATE, CURRENT_DATE + INTERVAL '7 day', 'AGR-2025/001', 8.5, 42.0, 'HIGH', 'IN_PROGRESS', 'MONTHLY', 'Test')");
                    stmt.executeUpdate("INSERT INTO TASK (NAME, EXECUTION_TIME, ORDER_ID, DONE) " +
                            "VALUES ('Przygotowanie raportu miesięcznego', CURRENT_DATE + INTERVAL '2 day', (SELECT id FROM \"order\" WHERE AGREEMENT_NUMBER ='AGR-2025/001'), false)");
                }
                case "mssql" -> {
                    stmt.executeUpdate("INSERT INTO [order] (CLIENT_ID, DATE_OF_ADMISSION, DATE_OF_EXECUTION, AGREEMENT_NUMBER, MAN_HOUR, DISTANCE, PRIORITY, STATUS, PERIOD, NOTE) " +
                            "VALUES (1, GETDATE(), DATEADD(day,7,GETDATE()), 'AGR-2025/001', 8.5, 42.0, 'HIGH', 'IN_PROGRESS', 'MONTHLY', 'Test')");
                    stmt.executeUpdate("INSERT INTO TASK (NAME, EXECUTION_TIME, ORDER_ID, DONE) " +
                            "VALUES ('Przygotowanie raportu miesięcznego', DATEADD(day,2,GETDATE()), (SELECT id FROM [order] WHERE AGREEMENT_NUMBER ='AGR-2025/001'), 0)");
                }
                case "oracle" -> {
                    stmt.executeUpdate("INSERT INTO \"ORDER\" (ID, CLIENT_ID, DATE_OF_ADMISSION, DATE_OF_EXECUTION, AGREEMENT_NUMBER, MAN_HOUR, DISTANCE, PRIORITY, STATUS, PERIOD, NOTE) " +
                            "VALUES (order_seq.NEXTVAL, 1, SYSDATE, SYSDATE+7, 'AGR-2025/001', 8.5, 42.0, 'HIGH', 'IN_PROGRESS', 'MONTHLY', 'Test')");
                    stmt.executeUpdate("INSERT INTO TASK (ID, NAME, EXECUTION_TIME, ORDER_ID, DONE) " +
                            "VALUES (task_seq.NEXTVAL, 'Przygotowanie raportu miesięcznego', SYSDATE+2, (SELECT ID FROM \"ORDER\" WHERE AGREEMENT_NUMBER ='AGR-2025/001'), 0)");
                }
            }

            System.out.println("Baza danych została zasiliowa testowymi danymi.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd przy przygotowaniu bazy danych.");
        }
    }
}
