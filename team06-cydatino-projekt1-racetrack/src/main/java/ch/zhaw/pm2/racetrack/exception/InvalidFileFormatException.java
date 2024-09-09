package ch.zhaw.pm2.racetrack.exception;

import ch.zhaw.pm2.racetrack.ConsoleText;

/**
 * Exception when loaded file is invalid.
 *
 * @author baumgnoa, bergecyr, brunndar, sigritim
 * @version 24.03.2022
 */
public class InvalidFileFormatException extends Exception {
    /**
     * Transforms an exception to an error message
     * @return error message
     */
    @Override
    public String toString() {
        return ConsoleText.INVALIDFILE_EXC.toString();
    }
}
