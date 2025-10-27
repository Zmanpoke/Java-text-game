import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// =================== ARMOR SHOP ===================
class ArmorShop {
   Item[] armor;

   public ArmorShop(Item[] armor) {
      this.armor = armor;
   }

   public void openArmorShop(Player player, Scanner scanner) {
      boolean shopping = true;
      while (shopping) {
         System.out.println("\n=== ARMOR SHOP ===");
         System.out.println("🛡️  Welcome to the Forge! 🛡️");
         System.out.println("1. Buy Armor");
         System.out.println("2. Sell Equipment");
         System.out.println("3. Exit Shop");
         int choice = InputValidator.getIntInput(scanner, "Choose option (1-3): ", 1, 3);

         switch (choice) {
            case 1:
               buyArmor(player, scanner);
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

   private void buyArmor(Player player, Scanner scanner) {
      System.out.println("\n--- Armor Stock ---");
      System.out.println("Your Gold: " + (int) player.gold + "g");
      System.out.println();

      for (int i = 0; i < armor.length; i++) {
         System.out.print((i + 1) + ". ");
         armor[i].displayItem();
      }
      System.out.println((armor.length + 1) + ". Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose armor to buy (1-" + (armor.length + 1) + "): ", 1,
            armor.length + 1);
      if (choice > 0 && choice <= armor.length) {
         Item armorPiece = armor[choice - 1];

         boolean canUse = checkClassRequirement(armorPiece, player);
         if (!canUse) {
            System.out.println("You cannot use this armor. It requires class: " + armorPiece.reqClass);
            return;
         }

         if (player.lvl < armorPiece.reqlvl) {
            System.out.println("You need to be level " + armorPiece.reqlvl + " to buy this armor.");
            return;
         }

         if (player.gold >= armorPiece.buyPrice) {
            player.gold -= armorPiece.buyPrice;
            player.addItem(armorPiece);
            System.out.println("You bought " + armorPiece.itemName + "!");
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

   private boolean checkClassRequirement(Item item, Player player) {
      if (item.reqClass.equalsIgnoreCase("Any")) {
         return true;
      } else if (item.reqClass.contains("|")) {
         String[] allowedClasses = item.reqClass.split("\\|");
         for (String allowedClass : allowedClasses) {
            if (allowedClass.trim().equalsIgnoreCase(player.charClass)) {
               return true;
            }
         }
      } else {
         return item.reqClass.equalsIgnoreCase(player.charClass);
      }
      return false;
   }
}