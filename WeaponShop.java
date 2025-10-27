import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// =================== WEAPON SHOP ===================
class WeaponShop {
   Item[] weapons;

   public WeaponShop(Item[] weapons) {
      this.weapons = weapons;
   }

   public void openWeaponShop(Player player, Scanner scanner) {
      boolean shopping = true;
      while (shopping) {
         System.out.println("\n=== WEAPON SHOP ===");
         System.out.println("⚔️  Welcome to the Armory! ⚔️");
         System.out.println("1. Buy Weapons");
         System.out.println("2. Sell Equipment");
         System.out.println("3. Exit Shop");
         int choice = InputValidator.getIntInput(scanner, "Choose option (1-3): ", 1, 3);

         switch (choice) {
            case 1:
               buyWeapons(player, scanner);
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

   private void buyWeapons(Player player, Scanner scanner) {
      System.out.println("\n--- Weapon Stock ---");
      System.out.println("Your Gold: " + (int) player.gold + "g");
      System.out.println();

      for (int i = 0; i < weapons.length; i++) {
         System.out.print((i + 1) + ". ");
         weapons[i].displayItem();
      }
      System.out.println((weapons.length + 1) + ". Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose weapon to buy (1-" + (weapons.length + 1) + "): ", 1,
            weapons.length + 1);
      if (choice > 0 && choice <= weapons.length) {
         Item weapon = weapons[choice - 1];

         // Check class requirements
         boolean canUse = checkClassRequirement(weapon, player);
         if (!canUse) {
            System.out.println("You cannot use this weapon. It requires class: " + weapon.reqClass);
            return;
         }

         if (player.lvl < weapon.reqlvl) {
            System.out.println("You need to be level " + weapon.reqlvl + " to buy this weapon.");
            return;
         }

         if (player.gold >= weapon.buyPrice) {
            player.gold -= weapon.buyPrice;
            player.addItem(weapon);
            System.out.println("You bought " + weapon.itemName + "!");

            // Ask if player wants to equip the item
            System.out.print("Would you like to equip this item now? (y/n): ");
            String equipChoice = scanner.next().toLowerCase();
            if (equipChoice.equals("y") || equipChoice.equals("yes")) {
               if (player.equipItem(weapon)) {
                  System.out.println("Item equipped successfully!");
               }
            }
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