public class Validator {

    public static boolean isValid(String[] args) {
        if (args == null || args.length == 0)  {
            System.out.println("No argument is chosen");
            return false;
        }
        return true;
    }

}

