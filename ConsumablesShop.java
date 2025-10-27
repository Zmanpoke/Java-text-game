import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// =================== CONSUMABLES SHOP ===================
class ConsumablesShop {
   Consumable[] consumables;

   public ConsumablesShop(Consumable[] consumables) {
      this.consumables = consumables;
   }

   public void openConsumablesShop(Player player, Scanner scanner) {
      boolean shopping = true;
      while (shopping) {
         System.out.println("\n=== CONSUMABLES SHOP ===");
         System.out.println("🧪  Welcome to the Apothecary! 🧪");
         System.out.println("1. Buy Consumables");
         System.out.println("2. Use Consumable");
         System.out.println("3. View Consumables Inventory");
         System.out.println("4. Exit Shop");
         int choice = InputValidator.getIntInput(scanner, "Choose option (1-4): ", 1, 4);

         switch (choice) {
            case 1:
               buyConsumables(player, scanner);
               break;
            case 2:
               useConsumable(player, scanner);
               break;
            case 3:
               viewConsumablesInventory(player);
               break;
            case 4:
               shopping = false;
               break;
            default:
               System.out.println("Invalid choice.");
         }
      }
   }

   private void buyConsumables(Player player, Scanner scanner) {
      System.out.println("\n--- Consumables Stock ---");
      System.out.println("Your Gold: " + (int) player.gold + "g");
      System.out.println();

      for (int i = 0; i < consumables.length; i++) {
         System.out.print((i + 1) + ". ");
         consumables[i].displayConsumable();
      }
      System.out.println((consumables.length + 1) + ". Cancel");
      System.out.print("Choose item to buy: ");

      int choice = InputValidator.getIntInput(scanner,
            "Choose consumable to buy (1-" + (consumables.length + 1) + "): ", 1, consumables.length + 1);
      if (choice > 0 && choice <= consumables.length) {
         Consumable item = consumables[choice - 1];

         System.out.print("How many would you like to buy? ");
         int quantity = InputValidator.getIntInput(scanner, "Enter quantity: ", 1, Integer.MAX_VALUE);

         if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
         }

         int totalCost = item.buyPrice * quantity;
         if (player.gold >= totalCost) {
            player.gold -= totalCost;

            // Check if player already has this consumable
            boolean found = false;
            for (Consumable playerConsumable : player.consumables) {
               if (playerConsumable.name.equals(item.name)) {
                  playerConsumable.quantity += quantity;
                  found = true;
                  break;
               }
            }

            if (!found) {
               // Create new consumable for player
               Consumable newConsumable = new Consumable(item.name, item.effect, item.value, quantity, item.buyPrice,
                     item.sellPrice, item.description);
               player.consumables.add(newConsumable);
            }

            System.out.println("You bought " + quantity + "x " + item.name + " for " + totalCost + " gold!");
         } else {
            System.out.println("Not enough gold. You need " + totalCost + " gold.");
         }
      }
   }

   boolean useConsumable(Player player, Scanner scanner) {
      return useConsumable(player, scanner, null);
   }

   // Overloaded method for combat situations with enemy access
   boolean useConsumable(Player player, Scanner scanner, Enemy enemy) {
      if (player.consumables.isEmpty()) {
         System.out.println("You have no consumables.");
         return false;
      }

      viewConsumablesInventory(player);
      System.out.println((player.consumables.size() + 1) + ". Cancel");
      System.out.print("Choose consumable to use: ");

      try {
         int choice = InputValidator.getIntInput(scanner,
               "Choose consumable to use (1-" + (player.consumables.size() + 1) + "): ", 1,
               player.consumables.size() + 1);
         if (choice == player.consumables.size() + 1) {
            return false; // Player canceled
         }
         if (choice > 0 && choice <= player.consumables.size()) {
            Consumable consumable = player.consumables.get(choice - 1);

            if (consumable.quantity <= 0) {
               System.out.println("You don't have any of this item.");
               return false;
            }

            // Use the consumable with combat context
            boolean actionTaken = false;
            if (enemy != null) {
               actionTaken = useConsumableEffectInCombat(player, consumable, enemy);
            } else {
               useConsumableEffect(player, consumable);
               actionTaken = true;
            }

            if (actionTaken) {
               consumable.quantity--;

               // Remove from inventory if quantity reaches 0
               if (consumable.quantity <= 0) {
                  player.consumables.remove(consumable);
               }
            }

            return actionTaken;
         } else {
            System.out.println("Invalid choice. Try again.");
            return false;
         }
      } catch (Exception e) {
         scanner.nextLine(); // Clear invalid input
         System.out.println("Invalid input. Try again.");
         return false;
      }
   }

   // Combat-specific consumable effects with enemy access
   private boolean useConsumableEffectInCombat(Player player, Consumable consumable, Enemy enemy) {
      switch (consumable.effect.toLowerCase()) {
         case "throwable":
            if (player.currentStamina >= 5) {
               double damage = consumable.value;
               player.currentStamina -= 5;
               enemy.takeDamage(damage);
               System.out.println("You used " + consumable.name + " for " + String.format("%.1f", damage) + " damage!");
               return true;
            } else {
               System.out.println("Not enough stamina!");
               return false;
            }

         case "escape":
            System.out.println("You used " + consumable.name + " and escaped from battle!");
            return true; // This will be handled specially in combat loop

         // Fall back to regular consumable effects for non-combat items
         default:
            useConsumableEffect(player, consumable);
            return true;
      }
   }

   private void useConsumableEffect(Player player, Consumable consumable) {
      switch (consumable.effect.toLowerCase()) {
         case "heal_hp":
            double oldHP = player.currentHP;
            player.currentHP = Math.min(player.currentHP + consumable.value, player.getMaxHP());
            double healed = player.currentHP - oldHP;
            System.out
                  .println("You used " + consumable.name + " and restored " + String.format("%.1f", healed) + " HP!");
            break;

         case "restore_mana":
            double oldMana = player.currentMana;
            player.currentMana = Math.min(player.currentMana + consumable.value, player.getMaxMana());
            double manaRestored = player.currentMana - oldMana;
            System.out.println(
                  "You used " + consumable.name + " and restored " + String.format("%.1f", manaRestored) + " Mana!");
            break;

         case "restore_stamina":
            double oldStamina = player.currentStamina;
            player.currentStamina = Math.min(player.currentStamina + consumable.value, player.getMaxStamina());
            double staminaRestored = player.currentStamina - oldStamina;
            System.out.println("You used " + consumable.name + " and restored " + String.format("%.1f", staminaRestored)
                  + " Stamina!");
            break;

         case "full_restore":
            player.currentHP = player.getMaxHP();
            player.currentMana = player.getMaxMana();
            player.currentStamina = player.getMaxStamina();
            System.out.println("You used " + consumable.name + " and fully restored all your resources!");
            break;

         case "buff_strength":
            player.applyStatusEffect(new StatusEffect("Strength Boost", (int) consumable.value, 0, false));
            // Note: You'd need to modify StatusEffect to handle stat boosts
            System.out.println("You used " + consumable.name + " and feel stronger!");
            break;

         case "cure_status":
            player.statusEffects.clear();
            System.out.println("You used " + consumable.name + " and cured all negative status effects!");
            break;

         case "throwable":
            // Throwable weapons can only be used in combat
            System.out.println("You can only use " + consumable.name + " during combat!");
            break;

         default:
            System.out.println("You used " + consumable.name + " but nothing happened...");
            break;
      }
   }

   private void viewConsumablesInventory(Player player) {
      System.out.println("\n--- Your Consumables ---");
      if (player.consumables.isEmpty()) {
         System.out.println("You have no consumables.");
         return;
      }

      for (int i = 0; i < player.consumables.size(); i++) {
         Consumable consumable = player.consumables.get(i);
         System.out.println(
               (i + 1) + ". " + consumable.name + " (x" + consumable.quantity + ") - " + consumable.description);
      }
   }
}