package ch.zhaw.pm2.racetrack;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class ArchitectureTest {

    /**
     * Tests Architecture Class Names
     */
    @Test
    void testClassNames() {
        JavaClasses importedClasses = new ClassFileImporter().
            withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS).
            importPackages("ch.zhaw.pm2.racetrack");

        ArchRule rule = classes().should().
            haveNameNotMatching("ch.zhaw.pm2.racetrack.[a-zA-Z]+Test([$]1)?");

        rule.check(importedClasses);
    }

    /**
     * Tests Architecture Test Class Names
     */
    @Test
    void testTestClassNames() {
        JavaClasses importedClasses = new ClassFileImporter().
            withImportOption(ImportOption.Predefined.ONLY_INCLUDE_TESTS).
            importPackages("ch.zhaw.pm2.racetrack");

        ArchRule rule = classes().should().
            haveNameMatching("ch.zhaw.pm2.racetrack.([A-Za-z])+Test([$]1)?").
            orShould().haveNameMatching("ch.zhaw.pm2.racetrack.strategy.[A-Za-z]+Test([$]1)?").
            orShould().haveNameMatching("ch.zhaw.pm2.racetrack.[A-Za-z]+Setup([$]1)?");

        rule.check(importedClasses);
    }
}
