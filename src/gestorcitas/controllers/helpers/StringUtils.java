/*
 * IPC - Entrega 1
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */

package gestorcitas.controllers.helpers;

/*
 * A collection of convenience methods for manipulating strings. 
 */
public class StringUtils {
    
    public static String prettifyEnumeration(String raw) {
        return prettifyEnumeration(raw, "\\s*,\\s*");
    }
    
    /*
     * Formats a delimiter-separated enumeration replacing the delimiters
     * with a comma and ensuring exactly only one space exists between each
     * comma and the next word.
     * @param raw The unformatted string.
     * @param delimiters A regex matching the delimiters separating items in
     *      the unformatted string.
     */
    public static String prettifyEnumeration(String raw, String delimiters) {
        String separator = ", ";
        return String.join(separator, raw.trim().split(delimiters));
    }
    
}
