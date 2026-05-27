import java.util.Scanner;

public class LoginManager {
    private Admin admin;

    public LoginManager(DatabaseManager dbManager) {
        this.admin = new Admin(dbManager);
    }

    public boolean startLogin(Scanner scanner) {
        System.out.println("\n====== USER LOGIN ======");
        return admin.login(scanner);
    }
}
