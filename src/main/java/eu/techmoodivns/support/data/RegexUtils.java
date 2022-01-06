package eu.techmoodivns.support.data;

import java.util.regex.Pattern;

public class RegexUtils {
    public static Pattern contains(String name) {
        return Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE);
    }
}
