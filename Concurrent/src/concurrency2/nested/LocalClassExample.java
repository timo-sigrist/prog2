package concurrency2.nested;

import java.time.LocalDateTime;
import java.util.List;

class LocalOuterClass {
    private String outerName = LocalClassExample.class.getSimpleName();
    // local inner classes are not visible in outer class or from other members
    // private LocalInnerClass inner = new LocalInnerClass();  //does not work

    public void methodUsingLocalInnerClass() {
        // local variable (only visible within method)
        String localVar = LocalDateTime.now().toString();

        /*
         * Method Local inner class definition, within Method body
         * -> only visible within the same method (private not required)
         */
         class LocalInnerClass {
             // Local inner class has access to local and instance variables / methods
             private String innerName = LocalInnerClass.class.getSimpleName();
             public void info() {
                 String info = outerName+"$"+innerName+"@"+localVar;
                System.out.println("Info from local inner class: " + info);
            }
         } // end of local class definition

        // create multiple instances of the local inner class.
        List<LocalInnerClass> instances = List.of(new LocalInnerClass(), new LocalInnerClass());
        // execute info method of the instances
        instances.forEach(LocalInnerClass::info);
    }

}

public class LocalClassExample {
    public static void main(String[] args) {
        LocalOuterClass outer = new LocalOuterClass();
        // the local class can only be access through the specific method
        outer.methodUsingLocalInnerClass();
    }
}
