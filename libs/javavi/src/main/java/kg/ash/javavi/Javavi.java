package kg.ash.javavi;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kg.ash.javavi.actions.Action;
import kg.ash.javavi.clazz.SourceClass;
import kg.ash.javavi.searchers.ClassMap;
import kg.ash.javavi.actions.ActionFactory;

public class Javavi {

    static final String VERSION	= "2.3.0";

    public static String NEWLINE = "";

    public static HashMap<String,SourceClass> cachedClasses = new HashMap<>();
    public static HashMap<String,StringBuilder[]> cachedPackages = new HashMap<>();
    public static HashMap<String,ClassMap> cachedClassPackages = new HashMap<>();

    static boolean debugMode = false;

    public static void debug(Object s) {
        if (debugMode)
            System.out.println(s);
    }

    static void output(String s) {
        if (!debugMode)
            System.out.print(s);
    }

    private static void usage() {
        version();
        System.out.println("  java [-classpath] kg.ash.javavi.Javavi [-sources sourceDirs] [-h] [-v] [-d] [-D port] [action]");
        System.out.println("Options:");
        System.out.println("  -h	        help");
        System.out.println("  -v	        version");
        System.out.println("  -sources      sources directory");
        System.out.println("  -d            enable debug mode");
        System.out.println("  -D port       start daemon on specified port");
    }

    private static void version() {
        System.out.println("Reflection and parsing for javavi " + 
                "vim plugin (" + VERSION + ")");
    }

    public static HashMap<String,String> system = new HashMap<>();
    public static Daemon daemon = null;

    public static void main( String[] args ) throws Exception {
        String response = makeResponse(args);

        debug(response);
        output(response);
    }

    public static String makeResponse(String[] args) {

        Action action = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            debug(arg);
            switch (arg) {
                case "-h":
                    usage();
                    return "";
                case "-v":
                    version();
                    return "";
                case "-sources": 
                    system.put("sources", args[++i]);
                    break;
                case "-d":
                    Javavi.debugMode = true;
                    break;
                default:
                    if (action == null) {
                        action = ActionFactory.get(arg);
                    }
            }

            if (action != null) {
                break;
            }
        }

        if (debugMode) NEWLINE = "\n";

        String result = "";
        if (action != null) {
            result = action.perform(args);
        }

        return result;
    }

}
