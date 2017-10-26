import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static List<String> _enteredText = new ArrayList<>(1);
    private static List<String> _emails = new ArrayList<>(1);
    private static List<String> _phones = new ArrayList<>(1);
    private static List<String> _birthdays = new ArrayList<>(1);
    private static List<String> _reminders = new ArrayList<>(1);
    private static BufferedReader _br = new BufferedReader(new InputStreamReader(System.in));
    private static FileWriter _fw;
    private static String _filename = "shrralis";
    private static String _fileExtension = ".txt";
    private static String _temp;
    private static File _file;

    @FunctionalInterface
    public interface ThrowingConsumer<T, E extends Exception> {
        void accept(T t) throws E;
    }
    private static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(
            ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    System.err.println(
                            "Exception occured : " + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Enter a filename (it will be \"shrralis\" if empty): ");

        if (!(_temp = _br.readLine()).equals("")) {
            _filename = _temp;
        }
        System.out.print("Enter a file extension (it will be \".txt\" if empty): ");

        if (!(_temp = _br.readLine()).equals("")) {
            _fileExtension = _temp;
        }

        _fileExtension = _fileExtension.substring(0, 1).equals(".") ? _fileExtension : "." + _fileExtension;

        System.out.println("Just a little information, here are formats we accept:");
        System.out.println(" — All of emails.");
        System.out.println(" — Phone numbers in next format: +{country}{operator}{else} (e.g. +380999999999).");
        System.out.println(" — Birthdays in next format: {dd or MM}{. or - or /}{dd or MM}{. or - or /}{YYYY}{- or ,}" +
                "{Name Surname} (e.g. 12/11/1997 - bday Shrralis Rrsynth).");
        System.out.println(" — Reminders in next format: {hh}{. or - or :}{mm}{- or ,}{some text}{.} (DOT IN THE END!)" +
                "(e.g. 12/11/1997 - bday Shrralis Rrsynth).");
        System.out.println("—————————— NO ANOTHER FORMATS PLEASE!!! ——————————");
        System.out.print("Enter any text to process. When you finished just enter \"SHRRALIS END\" without quotes: ");

        while (!(_temp = _br.readLine()).equals("SHRRALIS END")) {
            _enteredText.add(_temp);
        }
        System.out.println("Now wait until the text you entered will be processed.\n" +
                "It may take some time, please be patient...");

        for (String text : _enteredText) {
            Pattern pEmail = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}", Pattern.CASE_INSENSITIVE);
            Matcher mEmail = pEmail.matcher(text);

            while (mEmail.find()) {
                _emails.add(mEmail.group(0));
            }

            Pattern pPhone = Pattern.compile("\\+(?:[0-9] ?){6,14}[0-9]");
            Matcher mPhone = pPhone.matcher(text);

            while (mPhone.find()) {
                _phones.add(mPhone.group(0));
            }

            Pattern pBirthday = Pattern.compile("((\\d{1,2}[.-/]){2}\\d{4}(\\s?[-–—]\\s?)|(,\\s?))((b(irth)?day)|(день (народження)|(рождения))|(дн))?(\\s[A-ZА-ЯІЇЄЁ][a-zа-яіїєё'-]+){1,2}");
            Matcher mBirthday = pBirthday.matcher(text);

            while (mBirthday.find()){
                _birthdays.add(mBirthday.group(0));
            }

            Pattern pReminder = Pattern.compile("(.*?)\\d{2}[.-:]\\d{2}((\\s?[-–—]\\s?)|(,\\s?))(.*?)(\\..*?)");
            Matcher mReminder = pReminder.matcher(text);

            while (mReminder.find()) {
                _reminders.add(mReminder.group(0));
            }
        }
        System.out.printf("———————You entered %d text(s)———————\n", _enteredText.size());
        System.out.printf("———————There found %d E-Mail(s)———————\n", _emails.size());
        System.out.printf("———————There found %d phone(s)———————\n", _phones.size());
        System.out.printf("———————There found %d birthday(s)———————\n", _birthdays.size());
        System.out.printf("———————There found %d reminder(s)———————\n", _reminders.size());
        System.out.println("\n\nWriting processed data to file...");
        System.out.println("Writing E-Mails->->->->" + _filename + _fileExtension + "...");

        _file = new File(_filename + _fileExtension);

        if (!_file.exists()) {
            _file.createNewFile();
        } else {
            _file.delete();
            _file.createNewFile();
        }

        _fw = new FileWriter(_file);

        _fw.write("E-Mails (" + _emails.size() + "):\n");
        _emails.forEach(handlingConsumerWrapper(s -> _fw.write(s + "\n"), IOException.class));
        System.out.println("Writing phones->->->->" + _filename + _fileExtension + "...");
        _fw.write("——————————————————————————————————\n\nPhones (" + _phones.size() + "):\n");
        _phones.forEach(handlingConsumerWrapper(s -> _fw.write(s + "\n"), IOException.class));
        System.out.println("Writing birthdays->->->->" + _filename + _fileExtension + "...");
        _fw.write("——————————————————————————————————\n\nBirthdays (" + _birthdays.size() + "):\n");
        _birthdays.forEach(handlingConsumerWrapper(s -> _fw.write(s + "\n"), IOException.class));
        System.out.println("Writing reminders->->->->" + _filename + _fileExtension + "...");
        _fw.write("——————————————————————————————————\n\nReminders (" + _reminders.size() + "):\n");
        _reminders.forEach(handlingConsumerWrapper(s -> _fw.write(s + "\n"), IOException.class));
        _fw.close();
        System.out.println("Finished! Thank you for using!");
        System.out.println("\n\n\n———————————————Created by Yaroslav Zhyravov a.k.a. Shrralis © 2017———————————————");
    }
}
