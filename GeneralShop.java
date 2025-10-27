import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// =================== GENERAL SHOP ===================
class GeneralShop {
   Item[] generalItems;

   public GeneralShop(Item[] generalItems) {
      this.generalItems = generalItems;
   }

   public void openGeneralShop(Player player, Scanner scanner) {
      boolean shopping = true;
      while (shopping) {
         System.out.println("\n=== GENERAL STORE ===");
         System.out.println("💍  Welcome to the Emporium! 💍");
         System.out.println("1. Buy General Items");
         System.out.println("2. Sell Equipment");
         System.out.println("3. Exit Shop");
         int choice = InputValidator.getIntInput(scanner, "Choose option (1-3): ", 1, 3);

         switch (choice) {
            case 1:
               buyGeneralItems(player, scanner);
               break;
            case 2:
               sellItems(player, scanner);
               break;
            case 3:
               shopping = false;
               break;
            default:
               System.out.println("Invalid choice.");
         }
      }
   }

   private void buyGeneralItems(Player player, Scanner scanner) {
      System.out.println("\n--- General Items Stock ---");
      System.out.println("Your Gold: " + (int) player.gold + "g");
      System.out.println();

      for (int i = 0; i < generalItems.length; i++) {
         System.out.print((i + 1) + ". ");
         generalItems[i].displayItem();
      }
      System.out.println((generalItems.length + 1) + ". Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose item to buy (1-" + (generalItems.length + 1) + "): ", 1,
            generalItems.length + 1);
      if (choice > 0 && choice <= generalItems.length) {
         Item item = generalItems[choice - 1];

         if (player.lvl < item.reqlvl) {
            System.out.println("You need to be level " + item.reqlvl + " to buy this item.");
            return;
         }

         if (player.gold >= item.buyPrice) {
            player.gold -= item.buyPrice;
            player.addItem(item);
            System.out.println("You bought " + item.itemName + "!");
         } else {
            System.out.println("Not enough gold.");
         }
      }
   }

   private void sellItems(Player player, Scanner scanner) {
      if (player.inventory.isEmpty()) {
         System.out.println("You have nothing to sell.");
         return;
      }
      System.out.println("\n--- Your Equipment ---");
      player.displayInventory();
      System.out.println((player.inventory.size() + 1) + ". Cancel");

      int choice = InputValidator.getIntInput(scanner,
            "Choose item to sell (1-" + (player.inventory.size() + 1) + "): ", 1, player.inventory.size() + 1);
      if (choice > 0 && choice <= player.inventory.size()) {
         Item item = player.inventory.get(choice - 1);
         player.gold += item.sellPrice;
         player.removeItem(item);
         System.out.println("You sold " + item.itemName + " for " + item.sellPrice + " gold.");
      }
   }
}