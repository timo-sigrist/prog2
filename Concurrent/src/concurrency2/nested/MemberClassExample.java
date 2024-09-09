package concurrency2.nested;

import java.time.LocalDateTime;

class MemberOuterClass {
    // private instance variables (final and not final)
    private final String outerName = MemberOuterClass.class.getSimpleName();
    private int counter = 0;

    // create an instance of the inner class. It is bound to the outer instance.
    private MemberInnerClass inner = new MemberInnerClass();

    /*
     * Member Class definition
     * - private -> only accessible by member methods of outer Class
     */
    private class MemberInnerClass {
        private final String innerName = MemberInnerClass.class.getSimpleName();
        public void info() {
            String timestamp = LocalDateTime.now().toString();
            // Member inner class has access to instance variables / methods of outer class
            System.out.println("Info from member inner class: "+outerName+"$"+ innerName+"@"+timestamp);
            counter++; // update outer instance variable
        }
    } // end of member class definition

    // accessing the member inner class from other member methods
    public void methodAccessingMemberInnerClass() {
        inner.info();
    }

    // get counter
    public int getCounter() {
        return counter;
    }
}

public class MemberClassExample {
    public static void main(String[] args) {
        MemberOuterClass outer = new MemberOuterClass();
        // we can only access the inner class using an outer class method.
        for (int i = 0; i < 3; i++) {
            outer.methodAccessingMemberInnerClass();
        }
        System.out.printf("Number of calls to info: %s%n", outer.getCounter());

        // not possible to access instance of inner class directly
        // - change visibility of inner to public -> inner is accessible, but not method print()
        // - change visibility of MemberInnerClass to pubic -> print() is accessible.
        // outer.inner.info();
    }
}
