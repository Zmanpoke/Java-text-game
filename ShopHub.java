import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// =================== SHOP HUB ===================
class ShopHub {
   // Weapon shops
   WeaponShop swordShop;
   WeaponShop healerShop;
   WeaponShop instrumentShop;
   WeaponShop bowShop;
   WeaponShop spellbookShop;
   WeaponShop offhandWeaponShop;

   // Armor shops
   ArmorShop heavyArmorShop;
   ArmorShop lightArmorShop;
   ArmorShop robeShop;
   ArmorShop shieldShop;

   // Other shops
   GeneralShop generalShop;
   ConsumablesShop consumablesShop;

   public ShopHub(WeaponShop swordShop, WeaponShop healerShop, WeaponShop instrumentShop,
         WeaponShop bowShop, WeaponShop spellbookShop, WeaponShop offhandWeaponShop,
         ArmorShop heavyArmorShop, ArmorShop lightArmorShop, ArmorShop robeShop,
         ArmorShop shieldShop, GeneralShop generalShop, ConsumablesShop consumablesShop) {
      this.swordShop = swordShop;
      this.healerShop = healerShop;
      this.instrumentShop = instrumentShop;
      this.bowShop = bowShop;
      this.spellbookShop = spellbookShop;
      this.offhandWeaponShop = offhandWeaponShop;
      this.heavyArmorShop = heavyArmorShop;
      this.lightArmorShop = lightArmorShop;
      this.robeShop = robeShop;
      this.shieldShop = shieldShop;
      this.generalShop = generalShop;
      this.consumablesShop = consumablesShop;
   }

   public void openShopHub(Player player, Scanner scanner) {
      boolean browsing = true;
      while (browsing) {
         System.out.println("\n========== MARKET DISTRICT ==========");
         System.out.println("🏪  Welcome to the Trading Hub!  🏪");
         System.out.println("Your Gold: " + (int) player.gold + "g");
         System.out.println("=====================================");
         System.out.println("1. ⚔️  Weapon Shops");
         System.out.println("2. 🛡️  Armor Shops");
         System.out.println("3. 💍  General Store (Emporium)");
         System.out.println("4. 🧪  Consumables Shop (Apothecary)");
         System.out.println("5. 🚪  Exit Market");
         System.out.println("=====================================");

         int choice = InputValidator.getIntInput(scanner, "Choose a shop category (1-5): ", 1, 5);

         switch (choice) {
            case 1:
               openWeaponShops(player, scanner);
               break;
            case 2:
               openArmorShops(player, scanner);
               break;
            case 3:
               generalShop.openGeneralShop(player, scanner);
               break;
            case 4:
               consumablesShop.openConsumablesShop(player, scanner);
               break;
            case 5:
               browsing = false;
               System.out.println("Thanks for visiting the Market District!");
               break;
            default:
               System.out.println("Invalid choice.");
         }
      }
   }

   private void openWeaponShops(Player player, Scanner scanner) {
      boolean browsing = true;
      while (browsing) {
         System.out.println("\n========== WEAPON SHOPS ==========");
         System.out.println("⚔️  Choose your weapon type!  ⚔️");
         System.out.println("Your Gold: " + (int) player.gold + "g");
         System.out.println("==================================");
         System.out.println("1. 🗡️  Swords (Warrior)");
         System.out.println("2. 🏥  Healer Equipment");
         System.out.println("3. 🎵  Musical Instruments (Bard)");
         System.out.println("4. 🏹  Bows & Arrows (Archer)");
         System.out.println("5. 📚  Spellbooks (Mage)");
         System.out.println("6. 🗡️  Light Weapons (Archer/Bard)");
         System.out.println("7. 🔙  Back to Main Hub");
         System.out.println("==================================");
         System.out.print("Choose weapon type: ");

         int choice = InputValidator.getIntInput(scanner, "Choose weapon type (1-7): ", 1, 7);

         switch (choice) {
            case 1:
               swordShop.openWeaponShop(player, scanner);
               break;
            case 2:
               healerShop.openWeaponShop(player, scanner);
               break;
            case 3:
               instrumentShop.openWeaponShop(player, scanner);
               break;
            case 4:
               bowShop.openWeaponShop(player, scanner);
               break;
            case 5:
               spellbookShop.openWeaponShop(player, scanner);
               break;
            case 6:
               offhandWeaponShop.openWeaponShop(player, scanner);
               break;
            case 7:
               browsing = false;
               break;
            default:
               System.out.println("Invalid choice.");
         }
      }
   }

   private void openArmorShops(Player player, Scanner scanner) {
      boolean browsing = true;
      while (browsing) {
         System.out.println("\n========== ARMOR SHOPS ==========");
         System.out.println("🛡️  Choose your armor type!  🛡️");
         System.out.println("Your Gold: " + (int) player.gold + "g");
         System.out.println("=================================");
         System.out.println("1. 🛡️  Heavy Armor (Tank)");
         System.out.println("2. 🥼  Light Armor (Warrior/Archer/Bard)");
         System.out.println("3. 👘  Robes (Mage/Healer)");
         System.out.println("4. 🛡️  Shields (Tank/Warrior)");
         System.out.println("5. 🔙  Back to Main Hub");
         System.out.println("=================================");
         System.out.print("Choose armor type: ");

         int choice = InputValidator.getIntInput(scanner, "Choose armor type (1-5): ", 1, 5);

         switch (choice) {
            case 1:
               heavyArmorShop.openArmorShop(player, scanner);
               break;
            case 2:
               lightArmorShop.openArmorShop(player, scanner);
               break;
            case 3:
               robeShop.openArmorShop(player, scanner);
               break;
            case 4:
               shieldShop.openArmorShop(player, scanner);
               break;
            case 5:
               browsing = false;
               break;
            default:
               System.out.println("Invalid choice.");
         }
      }
   }
}