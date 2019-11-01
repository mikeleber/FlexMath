import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

/**
 * The type String utils.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * The constant newLine.
     */
    public static final String newLine = System.lineSeparator();

    /**
     * Create a string representation for a map
     *
     * @param <K>               the type parameter
     * @param <V>               the type parameter
     * @param map               the map
     * @param separator         default = ","
     * @param keyValueSeparator default = "="
     * @return string
     */
    public static <K, V> String mapToString(Map<K, V> map, String separator, String keyValueSeparator) {

        if (map == null) {
            return null;
        }
        if (map.size() == 0) {
            return "";
        }
        separator = org.apache.commons.lang3.StringUtils.defaultString(separator, ",");
        keyValueSeparator = org.apache.commons.lang3.StringUtils.defaultString(keyValueSeparator, "=");

        final StringBuilder buffer = new StringBuilder();
        final Iterator<Map.Entry<K, V>> entryIterator = map.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<K, V> entry = entryIterator.next();
            buffer.append(entry.getKey());
            buffer.append(keyValueSeparator);
            buffer.append(entry.getValue());

            if (entryIterator.hasNext()) {
                buffer.append(separator);
            }
        }
        return buffer.toString();
    }

    /**
     * Cut a string to the specified length. This method returns null if given string is null.
     *
     * @param given  the string
     * @param length the length
     * @return the string
     */
    public static final String cut(String given, int length) {
        if (given == null || given.length() < length) {
            return given;
        } else {
            return given.substring(0, length);
        }
    }

    /**
     * Replaces indicated characters with other characters.
     *
     * @param text          string where characters should be replaced
     * @param oldCharacters the character to be replaced by <code>newCharacters</code>.
     * @param newCharacters the character replacing <code>oldCharacters</code>.
     * @return replaced string.
     */
    public static final String replaceIgnoreCase(String text, String oldCharacters, String newCharacters) {
        final int l = oldCharacters.length();
        if (l > 0) {
            int ncL = newCharacters.length() - 1;
            if (ncL < 0) {
                ncL = 0;
            }
            StringBuilder buffer = new StringBuilder(text.length() + 2 * (ncL));
            int i = 0;
            for (int j = 0; ; i = j + l) {
                j = indexOfIgnoreCase(text, oldCharacters, i);
                if (j == -1) {
                    break;
                }
                buffer.append(text.substring(i, j));
                buffer.append(newCharacters);
            }
            buffer.append(text.substring(i));
            text = buffer.toString();
        }
        return text;
    }

    /**
     * Cut a string to the specified length. This method returns null if given string is null.
     *
     * @param given        the string
     * @param startReplace the to be replaced string
     * @return the string
     */
    public static final String replaceIfStartsWith(final String given, final String startReplace, final String with, final boolean ignoreCase) {
        if (given == null || isEmpty(startReplace) || with == null || given.length() < startReplace.length()) {
            return given;
        } else {
            final boolean startsWith = ignoreCase ? startsWithIgnoreCase(given, startReplace) : startsWith(given, startReplace);
            if (startsWith) {
                return with + given.substring(startReplace.length(), given.length());
            } else {
                return given;
            }
        }
    }

    /**
     * Cut a string to the specified length. This method returns null if given string is null.
     *
     * @param given   the string
     * @param replace the to be replaced string
     * @return the string
     */
    public static final String replaceIfEndsWith(final String given, final String replace, final String with, final boolean ignoreCase) {
        if (given == null || isEmpty(replace) || with == null || given.length() < replace.length()) {
            return given;
        } else {
            final boolean startsWith = ignoreCase ? endsWithIgnoreCase(given, replace) : endsWith(given, replace);
            if (startsWith) {
                return given.substring(0, given.length() - replace.length()) + with;
            } else {
                return given;
            }
        }
    }

    /**
     * This method is to convert any object to a long value if possible. If not the default value is returned.
     *
     * @param aValue       the a value
     * @param defaultValue (optional)
     * @return long long
     * @see Long#parseLong(String) java.lang.Long#parseLong(String)
     */
    public static long toLong(Object aValue, long defaultValue) {
        if (aValue != null) {
            return Long.parseLong(aValue.toString());
        } else {
            return defaultValue;
        }
    }

    /**
     * This method is to convert any object to a int value if possible. If not, the default value is returned.
     *
     * @param aValue       the a value
     * @param defaultValue (optional)
     * @return long int
     * @see Integer#parseInt(String) java.lang.Integer#parseInt(String)String)
     */
    public static int toInt(Object aValue, int defaultValue) {
        if (aValue != null) {
            return Integer.parseInt(aValue.toString());
        } else {
            return defaultValue;
        }
    }

    /**
     * Converts a JAXB object instance to a xml-string.
     * usage: String resultAsXML = XmlUtil.JaxbObject2String(resultElement, FindMetadataResultType.class);
     *
     * @param objInstance the object instance.
     * @param clazz       the Class of the object instance.
     * @return a String with the serialized object or null in case of an error.
     */
    public static String toXML(Object objInstance, Class<?> clazz) {

        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            m.marshal(objInstance, sw);
            return sw.toString();
        } catch (JAXBException e) {
            //  log.error("failed to convert object to XML({}): ", clazz.getName(), e);
        }
        return null;
    }

    private String convertPojoToXMLString(Object obj) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        try {
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(obj, sw);
            return sw.toString();
        } catch (MarshalException e) {
            String message;
            Throwable cause = e.getCause();
            if (cause != null) {
                message = cause.getMessage();
            } else {
                message = e.getMessage();
            }
            if (message != null) {
                if (message.contains("missing an @XmlRootElement")) {
                    // fix missing root element
                    //
                    StringWriter sw = new StringWriter();
                    String qNameLocal = obj.getClass().getSimpleName();
                    jaxbMarshaller.marshal(new JAXBElement(new QName("", qNameLocal), obj.getClass(), obj), sw);
                    return sw.toString();
                }
            }
        }
        return "";
    }

    /**
     * This method returns true if val has one of the following values(ignores case): true,y,yes,1,1.0
     *
     * @param val the val
     * @return boolean
     */
    public static final boolean isTrue(Object val) {
        String sVal = null;
        if (val == null || (sVal = val.toString()).equalsIgnoreCase("false")) {
            return false;
        }
        return (sVal.equalsIgnoreCase("true")
                || sVal.equalsIgnoreCase("y")
                || sVal.equalsIgnoreCase("yes")
                || sVal.equalsIgnoreCase("1")
                || sVal.equalsIgnoreCase("1.0"));
    }

    public static int naturalCompare(String a, String b, boolean ignoreCase) {
        if (ignoreCase) {
            a = a.toLowerCase();
            b = b.toLowerCase();
        }
        int aLength = a.length();
        int bLength = b.length();
        int minSize = Math.min(aLength, bLength);
        char aChar, bChar;
        boolean aNumber, bNumber;
        boolean asNumeric = false;
        int lastNumericCompare = 0;
        for (int i = 0; i < minSize; i++) {
            aChar = a.charAt(i);
            bChar = b.charAt(i);
            aNumber = aChar >= '0' && aChar <= '9';
            bNumber = bChar >= '0' && bChar <= '9';
            if (asNumeric) {
                if (aNumber && bNumber) {
                    if (lastNumericCompare == 0) {
                        lastNumericCompare = aChar - bChar;
                    }
                } else if (aNumber) {
                    return 1;
                } else if (bNumber) {
                    return -1;
                } else if (lastNumericCompare == 0) {
                    if (aChar != bChar) {
                        return aChar - bChar;
                    }
                    asNumeric = false;
                } else {
                    return lastNumericCompare;
                }
            } else if (aNumber && bNumber) {
                asNumeric = true;
                if (lastNumericCompare == 0) {
                    lastNumericCompare = aChar - bChar;
                }
            } else if (aChar != bChar) {
                return aChar - bChar;
            }
        }
        if (asNumeric) {
            if (aLength > bLength && a.charAt(bLength) >= '0' && a.charAt(bLength) <= '9') // as number
            {
                return 1;  // a has bigger size, thus b is smaller
            } else if (bLength > aLength && b.charAt(aLength) >= '0' && b.charAt(aLength) <= '9') // as number
            {
                return -1;  // b has bigger size, thus a is smaller
            } else if (lastNumericCompare == 0) {
                return aLength - bLength;
            } else {
                return lastNumericCompare;
            }
        } else {
            return aLength - bLength;
        }
    }

    public static final int getNestedEndPos(CharSequence text, int start, int end, char openBracket, char closeBracket) {
        int endPos = -1;
        int oBCount = 0;
        int cBCount = 0;
        end = (end == -1 ? text.length() : end);
        for (int i = start; i < end; i++) {
            char posChar = text.charAt(i);
            if (posChar == openBracket) {
                oBCount++;
                endPos = i + 1;
            } else if (posChar == closeBracket) {
                cBCount++;
                endPos = i + 1;
                if (oBCount == cBCount || oBCount == 0) {
                    return endPos;
                }
            } else {
                endPos = i + 1;
            }
        }
        return endPos;
    }

}
