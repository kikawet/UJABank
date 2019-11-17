/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.cliente.menu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Simple helper class for processing console input/output
 *
 * @author ajrueda
 */
public class ConsoleUtils {

    /**
     * Print a line message on the console
     *
     * @param string the message to show
     */
    public static void println(String string) {
        System.out.println(string);
    }

    /**
     * Print a message on the console
     *
     * @param string the message to show
     */
    public static void print(String string) {
        System.out.print(string);
    }

    /**
     * Gets a non-empty string from the console
     *
     * @param label the description of the requested data
     * @return the requested string
     */
    public static String getStringField(String label) {
        Scanner inputScanner = new Scanner(System.in);
        String result = null;

        do {
            print(label + "> ");
            result = inputScanner.nextLine();
        } while (result.isBlank());

        return result;
    }

    /**
     * Gets a non-empty string from the console matching a given pattern
     *
     * @param label the description of the requested data
     * @param patternExpr the pattern to match
     * @return the requested string
     */
    public static String getStringFieldWithPattern(String label, String patternExpr) {
        Pattern pattern = Pattern.compile(patternExpr);
        String result = null;
        do {
            result = getStringField(label);
        } while (!pattern.matcher(result).matches());

        return result;
    }

    /**
     * Gets an int from the console
     *
     * @param label the description of the requested data
     * @return the requested int
     */
    public static int getIntField(String label) {
        int result = 0;
        boolean done;

        do {
            try {
                result = Integer.parseInt(getStringField(label));
                done = true;
            } catch (NumberFormatException e) {
                done = false;
            }
        } while (!done);

        return result;
    }

    /**
     * Gets an float from the console
     *
     * @param label the description of the requested data
     * @return the requested int
     */
    public static float getFloatField(String label) {
        float result = 0;
        boolean done;

        do {
            try {
                result = Float.parseFloat(getStringField(label));
                done = true;
            } catch (NumberFormatException e) {
                done = false;
            }
        } while (!done);

        return result;
    }

    /**
     * Gets an int from the console in a given range
     *
     * @param label the description of the requested data
     * @param from the minimum acceptable value
     * @param to the maximum acceptable value
     * @return the requested int
     */
    public static int getIntFieldWithLimits(String label, int from, int to) {
        int result;

        do {
            result = getIntField(label);
        } while (result < from || to < result);

        return result;
    }

    public static Date getDateField(String label) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date result = null;

        do {
            try {
                result = dateFormat.parse(getStringField(label));
            } catch (ParseException e) {
                result = null;
            }
        } while (result == null);

        return result;
    }
}
