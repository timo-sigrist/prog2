package concurrency2.nested;

import java.time.LocalDateTime;

// Abstract class or interface used as type for anonymous class.
abstract class AnonymousClass {
    public abstract void info();
}

class AnonymousOuterClass {
    private String outerName = AnonymousOuterClass.class.getSimpleName();
    // Anonymous inner classes can be defined on member or on local level
    // define and create a single instance of the AnonymousClass in one step.
    private AnonymousClass anonymousMember = new AnonymousClass() {
        private String innerName = this.getClass().getSimpleName();
        public void info() {
            String info = outerName+"$"+innerName+"@"+LocalDateTime.now().toString();
            System.out.println("Info from member anonymous  class: " + info);
        }
    };

    public void methodUsingMemberAnonymousClass() {
        // execute info method of the member anonymous class instance
        anonymousMember.info();
    }

    public void methodUsingLocalAnonymousClass() {
        // local variable (only visible within method)
        String localVar = LocalDateTime.now().toString();
        // define and create a single instance of the AnonymousClass in one step.
        // notice: the class has no name, but is of type AnonymousClass
        AnonymousClass anonymousLocal = new AnonymousClass() {
            private String innerName = this.getClass().getSimpleName();
            public void info() {
                String info = outerName+"$"+innerName+"@"+localVar;
                System.out.println("Info from local anonymous class: " + info);
            }
        };

        // execute info method of the local anonymous class instance
        anonymousLocal.info();
    }

}

public class AnonymousClassExample {
    public static void main(String[] args) {
        AnonymousOuterClass outer = new AnonymousOuterClass();
        outer.methodUsingMemberAnonymousClass();
        outer.methodUsingLocalAnonymousClass();
    }
}
