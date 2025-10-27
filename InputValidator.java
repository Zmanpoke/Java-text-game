import java.util.Scanner;

// =================== INPUT VALIDATION HELPER ===================
class InputValidator {

   public static int getIntInput(Scanner scanner, String prompt, int min, int max) {
      int input = -1;
      boolean validInput = false;

      while (!validInput) {
         try {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
               input = scanner.nextInt();
               if (input >= min && input <= max) {
                  validInput = true;
               } else {
                  System.out.println("Please enter a number between " + min + " and " + max + ".");
               }
            } else {
               System.out.println("Invalid input. Please enter a valid number.");
               scanner.next(); // Clear the invalid input
            }
         } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear any remaining input
         }
      }

      return input;
   }

   public static char getCharInput(Scanner scanner, String prompt, char[] validChars) {
      char input = ' ';
      boolean validInput = false;

      while (!validInput) {
         try {
            System.out.print(prompt);
            String inputStr = scanner.next().toLowerCase();
            if (inputStr.length() > 0) {
               input = inputStr.charAt(0);
               for (char validChar : validChars) {
                  if (input == validChar) {
                     validInput = true;
                     break;
                  }
               }
               if (!validInput) {
                  System.out.print("Invalid choice. Valid options are: ");
                  for (int i = 0; i < validChars.length; i++) {
                     System.out.print(validChars[i]);
                     if (i < validChars.length - 1)
                        System.out.print(", ");
                  }
                  System.out.println();
               }
            } else {
               System.out.println("Please enter a valid character.");
            }
         } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear any remaining input
         }
      }

      return input;
   }

   public static String getStringInput(Scanner scanner, String prompt) {
      String input = "";
      boolean validInput = false;

      while (!validInput) {
         try {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
               validInput = true;
            } else {
               System.out.println("Please enter a valid name (cannot be empty).");
            }
         } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
         }
      }

      return input;
   }
}