package cn.jater.learn.meta_char;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LimitCharLength {
    private static void checkMatches(String regex, String context) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        if (matcher.matches()) {
            System.out.println(context + "\tmatches: " + regex);
        } else {
            System.out.println(context + "\tnot matches: " + regex);
        }
    }

    public static void main(String[] args) {
        checkMatches("ap{1}", "a"); // a	    not matches: ap{1}
        checkMatches("ap{1}", "ap"); // ap    matches: ap{1}
        checkMatches("ap{1}", "app"); // app	not matches: ap{1}
        checkMatches("ap{1}", "appppppppp"); // appppppppp	not matches: ap{1}

        checkMatches("ap{1,}", "a"); // a	    not matches: ap{1,}
        checkMatches("ap{1,}", "ap"); // ap	matches: ap{1,}
        checkMatches("ap{1,}", "app"); // app	matches: ap{1,}
        checkMatches("ap{1,}", "appppppppp"); // appppppppp	matches: ap{1,}

        checkMatches("ap{2,5}", "a"); // a      not matches: ap{2,5}
        checkMatches("ap{2,5}", "ap"); // ap    not matches: ap{2,5}
        checkMatches("ap{2,5}", "app"); // app  matches: ap{2,5}
        checkMatches("ap{2,5}", "appppppppp"); // appppppppp	not matches: ap{2,5}
    }
}
