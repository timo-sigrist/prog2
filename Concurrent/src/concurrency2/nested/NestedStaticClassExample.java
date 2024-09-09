package concurrency2.nested;

import java.time.LocalDateTime;

class NestedOuterClass {
    // private instance variables (not accessible by static nested class)
    private final String outerName = MemberOuterClass.class.getSimpleName();
    // private static counter (accessible by static nested class)
    private static int counter = 0;

    // create an instance of the static nested class.
    // It is not bound to the outer instance (could exist without the outer instance)
    private StaticNestedClass nested = new StaticNestedClass();

    /*
     * Static nested class definition
     * - private -> only accessible by member or static methods of outer Class
     * - public -> also accessible from external classes (see main)
     */
    public static class StaticNestedClass {
        private final String typeName = StaticNestedClass.class.getTypeName();
        public void info() {
            String timestamp = LocalDateTime.now().toString();
            // Static nested class has only access to static variables/methods of outer class
            // -> instance variable outerName (not accessible), static variable counter (accessible)
            System.out.println("Info from nested class: "+ typeName +"@"+timestamp);
            counter++; // update outer static variable
        }
    } // end of static nested class definition

    // accessing the private nested instance from other member methods
    public void methodAccessingPrivateStaticNestedClass() {
        nested.info();
    }

    // get counter
    public int getCounter() {
        return counter;
    }
}

public class NestedStaticClassExample {
    public static void main(String[] args) {
        NestedOuterClass outer = new NestedOuterClass();
        // we can access the private nested class using an public outer class method.
        for (int i = 0; i < 5; i++) {
            outer.methodAccessingPrivateStaticNestedClass();
        }

        //  possible to access instance of nested class directly if declared public
        NestedOuterClass.StaticNestedClass nested = new NestedOuterClass.StaticNestedClass();
        nested.info();

        System.out.printf("Number of calls to info: %s%n", outer.getCounter());
    }
}
