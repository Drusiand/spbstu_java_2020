import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        if (Validator.isValid(args)) {
            boolean flag = true;
            Logger logger = Logger.getLogger(Main.class.getName());
            Manager manager = new Manager(args[0], logger);
            if (manager.isConfigured()) {
                logger.info("\nInitializing cycle...");
                manager.Run();
            }
            else
                logger.info("ERROR: failed to parametrize manager");
        }
    }
}