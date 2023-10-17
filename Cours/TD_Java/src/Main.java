public class Main {
    public static void main(String[] argv) {
        System.out.println("Hello world !");
        for (String element : argv) {
            System.out.println(element);  // This is to print parameters set in entry.
        }
    }
}

// We also have to edit the run configuration to use parameters.