import dtu.dagprojekt.bankaroo.util.DB;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class NewCustomerTest {

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        try{
            DB.addNewCustomer(110793, "Bjørn", "password123");
        } catch (Exception e){
            System.out.println("Opps!" + e.getMessage());
        }
    }
}
