package cn.com.sherhom.reno.common.utils;


import java.util.*;
/**
 * @author Sherhom
 * @date 2020/9/2 20:12
 */
public class StringUtils {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String FOLDER_SEPARATOR = "/";
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static final char EXTENSION_SEPARATOR = '.';
    public static boolean hasLength(  String str) {
        return str != null && !str.isEmpty();
    }
    public static String cleanPath(String path) {
        if (!hasLength(path)) {
            return path;
        } else {
            String pathToUse = replace(path, "\\", "/");
            if (pathToUse.indexOf(46) == -1) {
                return pathToUse;
            } else {
                int prefixIndex = pathToUse.indexOf(58);
                String prefix = "";
                if (prefixIndex != -1) {
                    prefix = pathToUse.substring(0, prefixIndex + 1);
                    if (prefix.contains("/")) {
                        prefix = "";
                    } else {
                        pathToUse = pathToUse.substring(prefixIndex + 1);
                    }
                }

                if (pathToUse.startsWith("/")) {
                    prefix = prefix + "/";
                    pathToUse = pathToUse.substring(1);
                }
                else if(pathToUse.startsWith("./")){
                    prefix = prefix + "./";
                    pathToUse = pathToUse.substring(2);

                }

                String[] pathArray = delimitedListToStringArray(pathToUse, "/");
                LinkedList<String> pathElements = new LinkedList();
                int tops = 0;

                int i;
                for(i = pathArray.length - 1; i >= 0; --i) {
                    String element = pathArray[i];
                    if (!".".equals(element)) {
                        if ("..".equals(element)) {
                            ++tops;
                        } else if (tops > 0) {
                            --tops;
                        } else {
                            pathElements.add(0, element);
                        }
                    }
                }

                for(i = 0; i < tops; ++i) {
                    pathElements.add(0, "..");
                }

                if (pathElements.size() == 1 && "".equals(pathElements.getLast()) && !prefix.endsWith("/")) {
                    pathElements.add(0, ".");
                }

                return cleanRepeatedString(prefix + collectionToDelimitedString(pathElements, "/"),"/");
            }
        }
    }
    public static String replace(String inString, String oldPattern,   String newPattern) {
        if (hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
            int index = inString.indexOf(oldPattern);
            if (index == -1) {
                return inString;
            } else {
                int capacity = inString.length();
                if (newPattern.length() > oldPattern.length()) {
                    capacity += 16;
                }

                StringBuilder sb = new StringBuilder(capacity);
                int pos = 0;

                for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                    sb.append(inString.substring(pos, index));
                    sb.append(newPattern);
                    pos = index + patLen;
                }

                sb.append(inString.substring(pos));
                return sb.toString();
            }
        } else {
            return inString;
        }
    }
    public static String cleanRepeatedString(String oldString,String repeatedStr){
        String doubleString=nCopies(repeatedStr,2);
        String newString=oldString;
        while (newString.contains(doubleString)){
            newString=oldString.replace(doubleString,repeatedStr);
        }
        return newString;
    }
    public static String collectionToDelimitedString(  Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }
    public static String collectionToCommaDelimitedString(  Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }
    public static String collectionToDelimitedString(  Collection<?> coll, String delim, String prefix, String suffix) {
        if (CollectionUtils.isEmpty(coll)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator it = coll.iterator();

            while(it.hasNext()) {
                sb.append(prefix).append(it.next()).append(suffix);
                if (it.hasNext()) {
                    sb.append(delim);
                }
            }

            return sb.toString();
        }
    }
    public static String[] delimitedListToStringArray(  String str,   String delimiter) {
        return delimitedListToStringArray(str, delimiter, (String)null);
    }

    public static String[] delimitedListToStringArray(  String str,   String delimiter,   String charsToDelete) {
        if (str == null) {
            return EMPTY_STRING_ARRAY;
        } else if (delimiter == null) {
            return new String[]{str};
        } else {
            List<String> result = new ArrayList();
            int pos;
            if (delimiter.isEmpty()) {
                for(pos = 0; pos < str.length(); ++pos) {
                    result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
                }
            } else {
                int delPos;
                for(pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
                    result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                }

                if (str.length() > 0 && pos <= str.length()) {
                    result.add(deleteAny(str.substring(pos), charsToDelete));
                }
            }

            return toStringArray((Collection)result);
        }
    }

    public static String[] toStringArray(  Collection<String> collection) {
        return !CollectionUtils.isEmpty(collection) ? (String[])collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY;
    }

    public static String[] toStringArray(  Enumeration<String> enumeration) {
        return enumeration != null ? toStringArray((Collection)Collections.list(enumeration)) : EMPTY_STRING_ARRAY;
    }
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    public static String deleteAny(String inString,   String charsToDelete) {
        if (hasLength(inString) && hasLength(charsToDelete)) {
            StringBuilder sb = new StringBuilder(inString.length());

            for(int i = 0; i < inString.length(); ++i) {
                char c = inString.charAt(i);
                if (charsToDelete.indexOf(c) == -1) {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return inString;
        }
    }
    public static String nCopies(String s,int n){
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<n;i++){
            sb.append(s);
        }
        return sb.toString();
    }
    public static String dirPathEnd(String path){
        if(path==null)
            return null;
        if(path.equals(""))
            return "";
        return path.endsWith("/")||path.endsWith("\\")?path:path+"/";
    }
}
