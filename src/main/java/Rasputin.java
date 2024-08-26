import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Rasputin {

    private static void printText(String text) {
        System.out.println(lineBreak);
        System.out.println(text);
        System.out.println(lineBreak + "\n");
    }

    private static ArrayList<Task> readFile(File file) {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split("\\|");
                switch (split[0]) {
                    case "T":
                        Todo todo = new Todo(split[2]);
                        if (split[1].equals("1")) {
                            todo.markAsDone();
                        }
                        tasks.add(todo);
                        break;
                    case "D":
                        Deadline deadline = new Deadline(split[2], split[3]);
                        if (split[1].equals("1")) {
                            deadline.markAsDone();
                        }
                        tasks.add(deadline);
                        break;
                    case "E":
                        Event event = new Event(split[2], split[3], split[4]);
                        if (split[1].equals("1")) {
                            event.markAsDone();
                        }
                        tasks.add(event);
                        break;
                }
            }
            fileReader.close();
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Task file not found");
        } catch (IOException e) {
            System.out.println("Error reading file");
        }

        return tasks;
    }

    private static String lineBreak = "____________________________________";

    public static void main(String[] args) {
        String name = "Rasputin";
        // initialize task list
        ArrayList<Task> ls = new ArrayList<>();

        File file = new File("src/main/data/rasputin.txt");
        if (file.exists()) {
            System.out.println("Task file found.");
            ls = readFile(file);

        } else {
            System.out.println("Task file not found, creating task file.");
            try {
                file.createNewFile();
                System.out.println("Task file created");
            } catch (IOException e) {
                System.out.println("Unable to create task file.");
            }
        }


        // scanner to read user input
        Scanner scanner = new Scanner(System.in);

        // greeting
        printText("Hello, I'm " + name + "!\nWhat can I do for you?");

        while (true) {
            String input = scanner.nextLine().trim();
            String command = input.split(" ")[0];
            // if user types "bye", break out of loop and bid farewell
            if (command.equals("bye")) {
                break;
            }

            // display current list of tasks
            if (command.equals("list")) {
                if (ls.isEmpty()) {
                    printText("No tasks in list!");
                    continue;
                }
                int index = 1;
                System.out.println(lineBreak);
                for (Task item: ls) {
                    System.out.println(index + "." + item.toString());
                    index++;
                }
                System.out.println(lineBreak + "\n");
                continue;
            }

            // unmark task
            if (command.equals("unmark")) {
                if (ls.isEmpty()) {
                    printText("No tasks in list!");
                    continue;
                }
                try {
                    int index = (input.charAt(7) - '0' - 1);
                    ls.get(index).markAsNotDone();
                    String output = "Task has been unmarked.\n" +
                            ls.get(index).toString();
                    printText(output);
                }
                catch (StringIndexOutOfBoundsException e) {
                    printText("ERROR! Task to be unmarked not specified.");
                }
                catch (IndexOutOfBoundsException e) {
                    printText("ERROR! Task not found.");
                }
                continue;
            }

            // mark task as done
            if (command.equals("mark")) {
                if (ls.isEmpty()) {
                    printText("No tasks in list!");
                    continue;
                }
                try {
                    int index = (input.charAt(5) - '0' - 1);
                    ls.get(index).markAsDone();
                    String output = "Marked that as done for you.\n" +
                            ls.get(index).toString();
                    printText(output);
                }
                catch (StringIndexOutOfBoundsException e) {
                    printText("ERROR! Task to be marked not specified.");
                }
                catch (IndexOutOfBoundsException e) {
                    printText("ERROR! Task not found.");
                }
                finally {
                    continue;
                }
            }

            // create todo task
            if (command.equals("todo")) {
                try {
                    String desc = input.substring(5);
                    Todo task = new Todo(desc);
                    ls.add(task);
                    String output = "Added Todo task:\n" + task.toString();
                    output += "\nYou currently have " + ls.size() + " task/s in your list.";
                    printText(output);
                }
                catch (StringIndexOutOfBoundsException e) {
                    printText("ERROR! The description of a todo cannot be empty.");
                }
                finally {
                    continue;
                }

            }

            // create deadline task
            if (command.equals("deadline")) {
                try {
                    String str = input.trim().substring(9);
                    String desc = str.split(" /by ")[0];
                    String deadline = str.split(" /by ")[1];
                    Deadline task = new Deadline(desc, deadline);
                    ls.add(task);
                    String output = "Added Deadline task:\n" + task.toString();
                    output += "\nYou currently have " + ls.size() + " task/s in your list.";
                    printText(output);
                    continue;
                }
                catch (StringIndexOutOfBoundsException e) {
                    printText("ERROR! The description of a deadline cannot be empty.");
                }

                catch (ArrayIndexOutOfBoundsException e) {
                    printText("ERROR! Deadline tasks require a deadline to be completed by.");
                }
                finally {
                    continue;
                }
            }

            // create event task
            if (command.equals("event")) {
                try {
                    String str = input.substring(6);
                    String desc = str.split(" /from ")[0];
                    String duration = str.split(" /from ")[1];
                    String from = duration.split(" /to ")[0];
                    String to = duration.split(" /to ")[1];

                    Event task = new Event(desc, from, to);
                    ls.add(task);
                    String output = "Added Event task:\n" + task.toString();
                    output += "\nYou currently have " + ls.size() + " task/s in your list.";
                    printText(output);
                }
                catch (StringIndexOutOfBoundsException e) {
                    printText("ERROR! The description of an event cannot be empty.");
                }

                catch (ArrayIndexOutOfBoundsException e) {
                    printText("ERROR! Event tasks require a duration for the event.");
                }
                finally {
                    continue;
                }
            }

            if (command.equals("delete")) {
                try {
                    int index = (input.charAt(7) - '0' - 1);
                    String output = "Done, removed that task for you.\n" +
                            ls.get(index).toString();
                    ls.remove(index);
                    printText(output);
                }
                catch (StringIndexOutOfBoundsException e) {
                    printText("ERROR! Task to be deleted not specified.");
                }
                catch (IndexOutOfBoundsException e) {
                    printText("ERROR! Task not found.");
                }
                finally {
                    continue;
                }
            }



            printText("I'm sorry, I don't understand that command.");

        }

         try {
             FileWriter fileWriter = new FileWriter(file);
             fileWriter.write("");
             for (Task item: ls) {
                 String type;
                 String isDone;
                 String description = item.getDescription();
                 StringBuilder str = new StringBuilder();

                 if (item.isDone) {
                     isDone = "1";
                 } else {
                     isDone = "0";
                 }

                 if (item instanceof Deadline) {
                     type = "D";
                     String by = ((Deadline) item).getBy();

                     str.append(type);
                     str.append("|");
                     str.append(isDone);
                     str.append("|");
                     str.append(description);
                     str.append("|");
                     str.append(by);
                     str.append("\n");

                     fileWriter.append(str.toString());

                 } else if (item instanceof Event) {
                     type = "E";
                     String from = ((Event) item).getFrom();
                     String to = ((Event) item).getTo();

                     str.append(type);
                     str.append("|");
                     str.append(isDone);
                     str.append("|");
                     str.append(description);
                     str.append("|");
                     str.append(from);
                     str.append("|");
                     str.append(to);
                     str.append("\n");

                     fileWriter.append(str.toString());
                 } else if (item instanceof Todo) {
                     type = "T";

                     str.append(type);
                     str.append("|");
                     str.append(isDone);
                     str.append("|");
                     str.append(description);
                     str.append("\n");

                     fileWriter.append(str.toString());
                 }


             }
             fileWriter.close();
             System.out.println("Written to file successfully");
         } catch (IOException e) {
             System.out.println("ERROR! Could not write to file.");
         }
        printText("Bye. See you later!");
    }


}
