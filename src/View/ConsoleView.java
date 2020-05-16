package View;

import Controller.Controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.YearMonth;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleView extends View {

    // DO NOT CHANGE
    private static final int WIDTH = 72;

    public final static String KEY_OPTION = "Option";
    private static final String INVALID = "The selected option is invalid.";
    private final Scanner scanner;
    private final Controller controller;


    public ConsoleView(Controller controller, String title, Scanner scanner) {

        super(title);
        this.scanner = scanner;
        this.controller = controller;
    }

    public Controller getController()
    {
        return controller;
    }

    @Override
    public void show(){
        List<String> options = controller.getOptions();

        int option;
        do {
            String doubleLine = buildString('=',WIDTH);
            String spaceLine = buildString(' ', WIDTH);
            String singleLine = buildString('-', WIDTH);

            // length of the title
            double lengthTitle = title.length();

            System.out.printf("|%s|\n", doubleLine);
            System.out.printf("|%s|\n", spaceLine);
            System.out.printf("|%s%s%s|\n",buildString(' ', (int) Math.floor((WIDTH-lengthTitle)/2)),title.toUpperCase(), buildString(' ', (int) Math.ceil((WIDTH-lengthTitle)/2)));
            System.out.printf("|%s|\n", spaceLine);
            System.out.printf("|%s|\n", singleLine);
            // Loop through and display the list of options
            for(int i = 0; i < options.size(); i++) {
                System.out.printf("| " + (i + 1) + ") %-" + (WIDTH-4) + "s|\n", options.get(i));
            }

            System.out.printf("| " + (options.size() + 1) + ") Exit from %-" + (WIDTH-14) + "s|\n", title);
            System.out.printf("|%s|\n", doubleLine);
            System.out.println();

            do {
                System.out.print("Please select an option: ");
                try {
                    option = Integer.parseInt(scanner.nextLine());
                } catch(NumberFormatException e) {
                    option = -1;
                }

                System.out.println();

                if(option < 1 || option > options.size() + 1)
                    System.out.println("Invalid selection, please enter a valid value\n");
                else if(option <= options.size())
                    controller.onOptionSelected(this, option - 1);
            } while(option < 1 || option > options.size() + 1);

        } while(option != options.size() + 1);

    }

    @Override
    public void onSelected() {

    }

    @Override
    public void input(Map<String, String> input) {
        Iterable<String> keyset = input.keySet();

        // Loop through the requested fields and prompt user for input.
        for(String key: keyset) {
            System.out.print("Enter " + key + ": ");
            input.put(key, scanner.nextLine());
        }

        System.out.println();
    }

    @Override
    public void error(List<String> invalidFields) {
        System.out.println("One or all of the following fields are invalid: ");
        for(String field: invalidFields)
            System.out.println("\t- " + Character.toUpperCase(field.charAt(0)) + field.substring(1));

        System.out.println();
    }

    @Override
    public void display(Object object) {
        System.out.println(object);
    }

    @Override
    public void display(List objList) {
        if(objList.size() > 0)
            // Loop through the list of objects and display.
            for(Object obj: objList)
                display(obj);
        else
            System.out.println("There is no data to be displayed.");
    }

    @Override
    public void message(String message) {
        System.out.println(message);
    }

    @Override
    public boolean bailout() {
        // Prompt user
        message("Do you want to retry?");

        return options(Arrays.asList(Options.Yes, Options.No)) == Options.No;
    }

    @Override
    public <T> T options(List<T> options) {
        T selected = null;

        for(int i = 0; i < options.size(); i++)
            System.out.println((i + 1) + ") " + options.get(i).toString());

        do {
            System.out.print("Select an option: ");
            try {
                selected = options.get(Integer.parseInt(scanner.nextLine()) - 1);
            } catch(NumberFormatException | IndexOutOfBoundsException e) {
                selected = null;
                error(Arrays.asList(KEY_OPTION));
            }
        } while(selected == null);
        System.out.println();

        return selected;
    }

    // create a String which is made up of one char being repeated a given amount of times
    private String buildString(char stringChar,int stringLength)
    {
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i=0; i < stringLength; i++){
            sb.append(stringChar);
        }

        return sb.toString();
    }

    public YearMonth getValidDate(String name, String patternDate, String regexDate)
    {
        // declare YearMonth variable
        YearMonth date = null;

        // flag to check whether the entry should be tried again
        boolean repeatEntry;

        // create a formatter for date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternDate);

        // repeat until valid date is plugged in
        do
        {
            // initialize repeat flag to false
            repeatEntry = false;

            // get date
            String stringDate = getInputRegex(name.trim() , regexDate);

            // break out of method
            if(stringDate == null)
            {
                return null;
            }

            // convert date from String to LocalDate
            YearMonth testDate = YearMonth.parse(stringDate, formatter);

            // check whether the date is after today
            if(testDate.isBefore(YearMonth.now()))
            {

                // check whether the entry should be repeated
                repeatEntry = repeatEntry(INVALID);

            }
            else
            {
                // assign valid date to date
                date = testDate;
            }

        }while(date == null && repeatEntry);

        return date;
    }

    public LocalDate getValidDate(String name, String patternDate, String regexDate, LocalDate dateInput)
    {
        // declare LocalDate variable
        LocalDate date = null;

        // flag to check whether the entry should be tried again
        boolean repeatEntry;

        // create a formatter for date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternDate);

        // repeat until valid date is plugged in
        do
        {
            // initialize repeat flag to false
            repeatEntry = false;

            // get date
            String stringDate = getInputRegex(name.trim() , regexDate);

            // break out of method
            if(stringDate == null)
            {
                return null;
            }

            // convert date from String to LocalDate
            LocalDate testDate = LocalDate.parse(stringDate, formatter);

            // check whether the start date is after the input date
            if(!testDate.isAfter(dateInput))
            {

                // check whether the entry should be repeated
                repeatEntry = repeatEntry(INVALID);

            }
            else
            {
                // assign valid date to date
                date = testDate;
            }

        }while(date == null && repeatEntry);

        return date;
    }

    public String getInputRegex(String name, String patternCondition)
    {
        //declare input
        String input = null;

        // flag to check whether the entry should be tried again
        boolean repeatEntry;

        // initialize the Pattern object
        Pattern pattern = Pattern.compile(patternCondition);

        // declare matcher
        Matcher matcher;

        // repeat
        do
        {

            // initialize repeat flag to false
            repeatEntry = false;

            // print
            System.out.printf("Enter " + name.trim() + " : ");

            // get input
            String testInput = scanner.nextLine();

            // initialize matcher
            matcher = pattern.matcher(testInput);

            // check whether the pattern matches
            if(!matcher.matches())
            {

                // check whether the entry should be repeated
                repeatEntry = repeatEntry(INVALID);

            }
            else
            {
                input = testInput;
            }


        }while(input == null && repeatEntry);

        return input;
    }

    public <E extends Enum<?>> Enum<?> getInputEnum(Class<E> e, String name, String patternEnum)
    {

        // declare enum
        Object enumInput = null;

        // flag to check whether the entry should be tried again
        boolean repeatEntry;

        // initialize the Pattern object
        Pattern pattern = Pattern.compile(patternEnum);

        // declare matcher
        Matcher matcher;

        // repeat
        do
        {

            // initialize repeat flag to false
            repeatEntry = false;

            // print
            System.out.print("Enter the " + name.trim() + ": \n");

            // initialize iterator
            int iterator = 1;

            // iterate through enum
            for(Object eIterator : e.getEnumConstants())
            {
                // print
                System.out.println(iterator + ") " + capitalizeFirstLetter(eIterator.toString()));

                // increment iterator
                iterator++;
            }

            // print
            System.out.print("Please select an option: ");

            // get input
            String testInput = scanner.nextLine();

            // initialize matcher
            matcher = pattern.matcher(testInput);

            // declare input
            int input = 0;

            // check whether the pattern matches
            if(!matcher.matches())
            {

                // check whether the entry should be repeated
                repeatEntry = repeatEntry(INVALID);

                continue;

            }
            else
            {
                input = Integer.parseInt(testInput);
            }

            // check whether the number is out of bounds
            if(!((input > 0) && (input < e.getEnumConstants().length+1)))
            {

                // check whether the entry should be repeated
                repeatEntry = repeatEntry(INVALID);

            }
            else
            {
                enumInput = e.getEnumConstants()[input-1];
            }


        }while(enumInput == null && repeatEntry);

        return (E)enumInput;
    }

    public <E> E getInputArray(ArrayList<E> e, String name, String patternEnum)
    {
        // declare output
        E output = null;

        // flag to check whether the entry should be tried again
        boolean repeatEntry;

        // initialize the Pattern object
        Pattern pattern = Pattern.compile(patternEnum);

        // declare matcher
        Matcher matcher;

        // repeat
        do
        {

            // initialize repeat flag to false
            repeatEntry = false;

            // print
            System.out.print("Enter the " + name.trim() + ": \n\n");

            // initialize iterator
            int iterator = 1;

            // iterate through ArrayList
            for(E eIterator : e)
            {

                // print
                System.out.println(iterator + ")\n" + eIterator.toString() + "\n");

                // increment iterator
                iterator++;

            }

            // print
            System.out.print("\nPlease select an option: ");

            // get input
            String testInput = scanner.nextLine();

            // initialize matcher
            matcher = pattern.matcher(testInput);

            // declare input
            int input = 0;

            // check whether the pattern matches
            if(!matcher.matches())
            {

                // check whether the entry should be repeated
                repeatEntry = repeatEntry(INVALID);

                continue;

            }
            else
            {
                input = Integer.parseInt(testInput);
            }

            // check whether the number is out of bounds
            if(!((input > 0) && (input < iterator)))
            {

                // check whether the entry should be repeated
                repeatEntry = repeatEntry(INVALID);

            }
            else
            {
                output = e.get(input-1);
            }

        }while(output == null && repeatEntry);

        return output;

    }

    public boolean repeatEntry(String operation)
    {

            // print
            System.out.print("\n" +  operation.trim() + "\n\nDo you want to try again?\n1) Yes\n0) No\n\nPlease select an option: ");

            // check whether the entry should be tried again
            String stringRepeatEntry = scanner.nextLine();

            System.out.println();


        return stringRepeatEntry.equals("1");
    }

    public void displayText(String OutputMessage)
    {
        System.out.print(OutputMessage);
    }

    public String capitalizeFirstLetter(String str) {
        StringBuffer s = new StringBuffer();

        // Declare a character of space
        // To identify that the next character is the starting
        // of a new word
        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {

            // If previous character is space and current
            // character is not space then it shows that
            // current letter is the starting of the word
            if (ch == ' ' && str.charAt(i) != ' ')
            {
                ch = str.charAt(i);
                s.append(Character.toUpperCase(ch));
            }
            else if(str.charAt(i) == '_')
            {
                ch = ' ';
                s.append(ch);
            }
            else
            {
                ch = str.charAt(i);
                s.append(Character.toLowerCase(ch));
            }
        }

        // Return the string with trimming
        return s.toString().trim();
    }


}
