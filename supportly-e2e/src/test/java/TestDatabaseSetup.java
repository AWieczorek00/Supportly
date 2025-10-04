import org.example.BaseE2ETest;
import org.junit.jupiter.api.BeforeAll;

import java.sql.*;
import java.time.LocalDate;

public class TestDatabaseSetup extends BaseE2ETest {

//    private static final String URL = "jdbc:postgresql://192.168.0.81:1010/postgres";
    protected static final String URL = System.getenv().getOrDefault("BASE_URL_DATABASE", "jdbc:postgresql://192.168.0.81:1010/postgres");

    private static final String USER = "supportly";
    private static final String PASS = "Qwerty.1";

    @BeforeAll
    static void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            // czyszczenie tabel
            stmt.executeUpdate("DELETE FROM agreement");

            stmt.executeUpdate("DELETE FROM TASK_EMPLOYEE");
            stmt.executeUpdate("DELETE FROM task");
            stmt.executeUpdate("DELETE FROM \"order\"");
            stmt.executeUpdate("DELETE FROM client");
            stmt.executeUpdate("DELETE FROM company");
            stmt.executeUpdate("DELETE FROM address");
            stmt.executeUpdate("DELETE FROM employee where PHONE_NUMBER = '502254567'");



            stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (1, 'Warszawa', 'Marszałkowska', '00-001', 10)");
            stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (2, 'Kraków', 'Floriańska', '31-019', 15)");
            stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (3, 'Gdańsk', 'Długa', '80-827', 7)");
            stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (4, 'Wrocław', 'Świdnicka', '50-066', 22)");
            stmt.executeUpdate("INSERT INTO address (id, city, street, zip_code, street_number) VALUES (5, 'Poznań', 'Półwiejska', '61-888', 5)");

            stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (1, 'Tech Solutions Sp. z o.o.', '1234567890', 1, '123456789', 'kontakt@techsolutions.pl', '987654321')");
            stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (2, 'InnovaTech S.A.', '9876543210', 2, '987654321', 'biuro@innovatech.pl', '123456789')");
            stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (3, 'SoftCom S.C.', '1112223334', 3, '222333444', 'kontakt@softcom.pl', '567890123')");
            stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (4, 'NextGen IT Sp. z o.o.', '5556667778', 4, '333444555', 'info@nextgenit.pl', '345678901')");
            stmt.executeUpdate("INSERT INTO company (id, name, nip, address_id, phone_number, email, regon) VALUES (5, 'GreenData Sp. z o.o.', '4445556667', 5, '444555666', 'kontakt@greendata.pl', '789012345')");



            stmt.executeUpdate("INSERT INTO client (id, first_name, second_name, phone_number, email, company_id, type) VALUES (1, 'Jan', 'Kowalski', '501234567', 'jan.kowalski@example.com', 1, 'B2B')");
            stmt.executeUpdate("INSERT INTO client (id, first_name, second_name, phone_number, email, company_id, type) VALUES (2, 'Anna', 'Nowak', '502345678', 'anna.nowak@example.com', 2, 'B2C')");
            stmt.executeUpdate("INSERT INTO client (id, first_name, second_name, phone_number, email, company_id, type) VALUES (3, 'Piotr', 'Wiśniewski', '503456789', 'piotr.wisniewski@example.com', 3, 'B2B')");
            stmt.executeUpdate("INSERT INTO client (id, first_name, second_name, phone_number, email, company_id, type) VALUES (4, 'Katarzyna', 'Mazur', '504567890', 'katarzyna.mazur@example.com', 4, 'B2C')");
            stmt.executeUpdate("INSERT INTO client (id, first_name, second_name, phone_number, email, company_id, type) VALUES (5, 'Marek', 'Wójcik', '505678901', 'marek.wojcik@example.com', 5, 'B2B')");

            stmt.executeUpdate("INSERT INTO agreement (id, company_id, agreement_number, period, signed_date, next_service_date, last_service_date) VALUES (3, 1, '321312/3232', 'MONTHLY', '2025-09-06', '2025-10-06', null)");
            stmt.executeUpdate("INSERT INTO agreement (id, company_id, agreement_number, period, signed_date, next_service_date, last_service_date) VALUES (5, 2, 'AGR-2025/004', 'YEARLY', '2025-04-05', '2026-04-05', null)");
            stmt.executeUpdate("INSERT INTO agreement (id, company_id, agreement_number, period, signed_date, next_service_date, last_service_date) VALUES (6, 3, 'AGR-2025/005', 'QUARTERLY', '2025-05-20', '2025-08-20', null)");
            stmt.executeUpdate("INSERT INTO agreement (id, company_id, agreement_number, period, signed_date, next_service_date, last_service_date) VALUES (7, 4, 'AGR-2025/006', 'MONTHLY', '2025-06-01', '2025-07-01', '2025-06-30')");



            stmt.executeUpdate("INSERT INTO \"order\" (CLIENT_ID, DATE_OF_ADMISSION, DATE_OF_EXECUTION, AGREEMENT_NUMBER, MAN_HOUR, DISTANCE, PRIORITY, STATUS, PERIOD, NOTE) VALUES (1, '2025-10-04', '2025-10-11', 'AGR-2025/001', 8.5, 42.0, 'HIGH', 'IN_PROGRESS', 'MONTHLY', 'Zamówienie testowe dla klienta firmowego.')");
            stmt.executeUpdate("INSERT INTO TASK (NAME, EXECUTION_TIME, ORDER_ID, DONE) VALUES ('Przygotowanie raportu miesięcznego', CURRENT_DATE + INTERVAL '2 day', (Select id from \"order\" where AGREEMENT_NUMBER ='AGR-2025/001'), false);");
            stmt.executeUpdate("INSERT INTO TASK_EMPLOYEE (TASK_ID, EMPLOYEE_ID) VALUES ((Select id from Task where NAME ='Przygotowanie raportu miesięcznego') , 2)");




            System.out.println("Baza danych została zasiliowa testowymi danymi.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd przy przygotowaniu bazy danych.");
        }
    }
}
