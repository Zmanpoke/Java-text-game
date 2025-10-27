import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// Special encounter types
enum EncounterType {
   COMBAT, TREASURE_CHEST, TRAP_CHEST, MERCHANT, REST_AREA, BOSS
}

class DungeonBot {
   Dungeon[] dungeons;
   Random random = new Random();

   public DungeonBot(Dungeon[] dungeons) {
      this.dungeons = dungeons;
   }

   public void openDungeonBot(Player player, Scanner scanner, Enemy[] allEnemies,
         Item[] allItems, Consumable[] allConsumables,
         ConsumablesShop consumablesShop) {
      boolean exploring = true;
      while (exploring) {
         System.out.println("\n========== DUNGEON EXPLORER ==========");
         System.out.println("🏰  Choose your adventure!  🏰");
         System.out.println("======================================");
         System.out.println("1. View Available Dungeons");
         System.out.println("2. Enter Dungeon");
         System.out.println("3. Exit Explorer");
         System.out.println("======================================");

         int choice = InputValidator.getIntInput(scanner, "Choose option (1-3): ", 1, 3);

         switch (choice) {
            case 1:
               viewAvailableDungeons(player);
               break;
            case 2:
               enterDungeon(player, scanner, allEnemies, allItems, allConsumables, consumablesShop);
               // If player died in dungeon, exit the dungeon bot
               if (!player.isAlive()) {
                  exploring = false;
               }
               break;
            case 3:
               exploring = false;
               break;
         }
      }
   }

   private void viewAvailableDungeons(Player player) {
      System.out.println("\n=== Available Dungeons ===");
      System.out.println("Your Level: " + player.lvl);
      System.out.println();

      for (int i = 0; i < dungeons.length; i++) {
         System.out.print((i + 1) + ". ");
         dungeons[i].displayDungeon();
      }
   }

   private void enterDungeon(Player player, Scanner scanner, Enemy[] allEnemies,
         Item[] allItems, Consumable[] allConsumables,
         ConsumablesShop consumablesShop) {
      viewAvailableDungeons(player);
      System.out.println((dungeons.length + 1) + ". Cancel");

      int choice = InputValidator.getIntInput(scanner,
            "Choose dungeon to enter (1-" + (dungeons.length + 1) + "): ",
            1, dungeons.length + 1);

      if (choice > dungeons.length)
         return; // Cancel

      Dungeon selectedDungeon = dungeons[choice - 1];

      // Difficulty warning
      if (selectedDungeon.difficulty > player.lvl + 2) {
         System.out.println("⚠️  WARNING: This dungeon is very dangerous for your current level!");
         System.out.print("Are you sure you want to enter? (y/n): ");
         String confirm = scanner.next().toLowerCase();
         if (!confirm.equals("y") && !confirm.equals("yes")) {
            System.out.println("You decide to wait until you're stronger...");
            return;
         }
      }

      System.out.println("\n🚪 Entering " + selectedDungeon.name + "...");
      System.out.println("The air grows cold as you step into the darkness...");

      exploreDungeon(player, selectedDungeon, scanner, allEnemies, allItems, allConsumables, consumablesShop);
   }

   private void exploreDungeon(Player player, Dungeon dungeon, Scanner scanner,
         Enemy[] allEnemies, Item[] allItems, Consumable[] allConsumables,
         ConsumablesShop consumablesShop) {
      int currentEncounter = 1;
      boolean dungeonCompleted = false;
      boolean playerEscaped = false;

      // Store initial HP for rest area calculations
      double initialHP = player.currentHP;

      while (currentEncounter <= dungeon.length && player.isAlive() && !playerEscaped) {
         System.out.println("\n" + "=".repeat(50));
         System.out.println("🏰 " + dungeon.name + " - Encounter " + currentEncounter + "/" + dungeon.length);
         System.out.println("=".repeat(50));

         DungeonEncounter encounter;

         // Last encounter is always the boss
         if (currentEncounter == dungeon.length) {
            encounter = createBossEncounter(dungeon, allEnemies);
         } else {
            encounter = generateRandomEncounter(dungeon, allEnemies, allItems, allConsumables);
         }

         System.out.println(encounter.description);

         boolean encounterResult = handleEncounter(player, encounter, scanner, consumablesShop);

         if (!encounterResult) {
            // Player escaped or died
            if (player.isAlive()) {
               System.out.println("You flee from the dungeon!");
               playerEscaped = true;
            } else {
               System.out.println("You have fallen in the depths of " + dungeon.name + "...");
               return; // Exit dungeon exploration immediately
            }
            break;
         }

         currentEncounter++;

         // Small rest between encounters (except before boss)
         if (currentEncounter <= dungeon.length && player.isAlive()) {
            System.out.println("\nYou take a moment to catch your breath...");
            player.currentMana = Math.min(player.currentMana + 5, player.getMaxMana());
            player.currentStamina = Math.min(player.currentStamina + 10, player.getMaxStamina());
         }
      }

      // Check if dungeon was completed successfully
      if (currentEncounter > dungeon.length && player.isAlive()) {
         dungeonCompleted = true;
         System.out.println("DUNGEON CLEARED: " + dungeon.name);

         // Award dungeon completion rewards
         System.out.println("Dungeon completion rewards:");
         System.out.println("💰 Gold: " + dungeon.goldReward);
         System.out.println("⭐ Experience: " + dungeon.expReward);

         player.gold += dungeon.goldReward;
         player.gainExp(dungeon.expReward, scanner);

         // Bonus reward based on remaining HP
         double hpPercent = player.currentHP / player.getMaxHP();
         if (hpPercent > 0.8) {
            int bonusGold = dungeon.goldReward / 4;
            System.out.println("🏆 Flawless Victory Bonus: " + bonusGold + " gold!");
            player.gold += bonusGold;
         }
      }

   }

   private DungeonEncounter generateRandomEncounter(Dungeon dungeon, Enemy[] allEnemies,
         Item[] allItems, Consumable[] allConsumables) {
      double rand = random.nextDouble() * 100;

      // 70% combat, 10% treasure, 10% trap, 5% merchant, 5% rest
      if (rand < 70) {
         return createCombatEncounter(dungeon, allEnemies);
      } else if (rand < 80) {
         return createTreasureEncounter(allItems, allConsumables);
      } else if (rand < 90) {
         return createTrapEncounter(allItems, allConsumables);
      } else if (rand < 95) {
         return createMerchantEncounter(allConsumables);
      } else {
         return createRestEncounter();
      }
   }

   private DungeonEncounter createCombatEncounter(Dungeon dungeon, Enemy[] allEnemies) {
      // Find enemies that match this dungeon's theme
      java.util.List<Enemy> validEnemies = new java.util.ArrayList<>();
      for (String enemyName : dungeon.possibleEnemies) {
         for (Enemy enemy : allEnemies) {
            if (enemy.name.equals(enemyName)) {
               validEnemies.add(enemy);
               break;
            }
         }
      }

      if (validEnemies.isEmpty()) {
         // Fallback to any enemy
         validEnemies.add(allEnemies[random.nextInt(allEnemies.length)]);
      }

      Enemy chosenEnemy = validEnemies.get(random.nextInt(validEnemies.size()));
      String description = getThemeDescription(dungeon.theme) + " A " + chosenEnemy.name + " blocks your path!";

      return new DungeonEncounter(EncounterType.COMBAT, description, chosenEnemy);
   }

   private DungeonEncounter createBossEncounter(Dungeon dungeon, Enemy[] allEnemies) {
      // Find the boss enemy
      Enemy bossEnemy = null;
      for (Enemy enemy : allEnemies) {
         if (enemy.name.equals(dungeon.bossEnemy)) {
            bossEnemy = enemy;
            break;
         }
      }

      if (bossEnemy == null) {
         // Fallback to a strong enemy
         bossEnemy = allEnemies[Math.min(10, allEnemies.length - 1)];
      }

      String description = "🐉 BOSS ENCOUNTER 🐉\n" +
            "The chamber ahead grows ominous. You can sense a powerful presence...\n" +
            "The " + bossEnemy.name + " emerges from the shadows!";

      return new DungeonEncounter(EncounterType.BOSS, description, bossEnemy);
   }

   private DungeonEncounter createTreasureEncounter(Item[] allItems, Consumable[] allConsumables) {
      String description = "💎 You discover a glittering treasure chest!";

      // Create a small reward - either items or consumables
      Object reward;
      if (random.nextBoolean()) {
         // Give a random item
         Item randomItem = allItems[random.nextInt(allItems.length)];
         reward = new Item[] { randomItem };
      } else {
         // Give random consumables
         Consumable randomConsumable = allConsumables[random.nextInt(allConsumables.length)];
         reward = new Consumable[] { randomConsumable };
      }

      return new DungeonEncounter(EncounterType.TREASURE_CHEST, description, reward);
   }

   private DungeonEncounter createTrapEncounter(Item[] allItems, Consumable[] allConsumables) {
      String description = "⚠️ You find a suspicious chest... it might be trapped!";

      // 50% chance it's trapped, 50% chance it contains treasure
      Object data = random.nextBoolean() ? "TRAPPED" : createTreasureEncounter(allItems, allConsumables).data;

      return new DungeonEncounter(EncounterType.TRAP_CHEST, description, data);
   }

   private DungeonEncounter createMerchantEncounter(Consumable[] allConsumables) {
      String description = "🛒 A mysterious merchant appears from the shadows!\n" +
            "\"Adventurer! I have wares that might interest you...\"";

      // Select a few random consumables for the merchant
      java.util.List<Consumable> merchantStock = new java.util.ArrayList<>();
      for (int i = 0; i < 3; i++) {
         Consumable item = allConsumables[random.nextInt(allConsumables.length)];
         merchantStock.add(new Consumable(item.name, item.effect, item.value, 1,
               item.buyPrice, item.sellPrice, item.description));
      }

      return new DungeonEncounter(EncounterType.MERCHANT, description,
            merchantStock.toArray(new Consumable[0]));
   }

   private DungeonEncounter createRestEncounter() {
      String description = "🛡️ You find a peaceful alcove with a small shrine.\n" +
            "The area radiates a calming energy...";

      return new DungeonEncounter(EncounterType.REST_AREA, description, null);
   }

   private boolean handleEncounter(Player player, DungeonEncounter encounter,
         Scanner scanner, ConsumablesShop consumablesShop) {
      switch (encounter.type) {
         case COMBAT, BOSS:
            return handleCombatEncounter(player, encounter, scanner, consumablesShop);

         case TREASURE_CHEST:
            return handleTreasureEncounter(player, encounter, scanner);

         case TRAP_CHEST:
            return handleTrapEncounter(player, encounter, scanner);

         case MERCHANT:
            return handleMerchantEncounter(player, encounter, scanner);

         case REST_AREA:
            return handleRestEncounter(player, encounter, scanner);

         default:
            return true;
      }
   }

   private boolean handleCombatEncounter(Player player, DungeonEncounter encounter,
         Scanner scanner, ConsumablesShop consumablesShop) {
      if (!(encounter.data instanceof Enemy)) {
         System.out.println("ERROR: Invalid combat encounter data!");
         return true;
      }
      Enemy template = (Enemy) encounter.data;
      Enemy enemy = new Enemy(template.name, template.strength, template.vitality,
            template.agility, template.expReward, template.goldReward);

      // Boost boss stats
      if (encounter.type == EncounterType.BOSS) {
         enemy.strength *= 1.25;
         enemy.vitality *= 1.5;
         enemy.currentHP = enemy.getMaxHP();
         enemy.expReward *= 1.5;
         enemy.goldReward *= 1.5;
      }

      boolean isDefending = false;
      boolean battleEscaped = false;

      while (player.isAlive() && enemy.isAlive() && !battleEscaped) {
         // Process status effects
         player.processStatusEffects();
         enemy.processStatusEffects();

         if (!enemy.isAlive()) {
            System.out.println("The " + enemy.name + " succumbs to status effects!");
            break;
         }

         // Player's turn
         if (!player.isStunned()) {
            boolean turnTaken = false;
            while (!turnTaken) {
               System.out.println("\n--- Your Turn ---");
               player.displayStats();
               enemy.displayStats();

               System.out.println("\nChoose your action:");
               System.out.println("1. Attack (" + String.format("%.1f", player.getAttackDamage()) + " damage)");
               System.out.println("2. Defend (reduce incoming damage)");
               System.out.println("3. Use Skill/Spell");
               System.out.println("4. Use Consumable");
               if (player.charClass.equalsIgnoreCase("healer")) {
                  System.out.println((encounter.type == EncounterType.BOSS ? "5" : "6") + ". Basic Heal");
               }

               int maxActions = encounter.type == EncounterType.BOSS
                     ? (player.charClass.equalsIgnoreCase("healer") ? 5 : 4)
                     : (player.charClass.equalsIgnoreCase("healer") ? 6 : 5);

               int playerAction = InputValidator.getIntInput(scanner,
                     "Choose action (1-" + maxActions + "): ", 1, maxActions);

               switch (playerAction) {
                  case 1: // Attack
                     double playerDmg = player.getAttackDamage();
                     enemy.takeDamage(playerDmg);
                     System.out.println("You hit " + enemy.name + " for " +
                           String.format("%.1f", playerDmg) + " damage!");
                     turnTaken = true;
                     break;

                  case 2: // Defend
                     System.out.println("You take a defensive stance!");
                     isDefending = true;
                     turnTaken = true;
                     break;

                  case 3: // Skill/Spell
                     boolean skillUsed = player.useSkillOrSpell(enemy, scanner);
                     if (skillUsed) {
                        turnTaken = true;
                     }
                     break;

                  case 4: // Consumable
                     boolean consumableUsed = consumablesShop.useConsumable(player, scanner, enemy);
                     if (consumableUsed) {
                        turnTaken = true;
                     }
                     break;

                  case 5: // Flee or Heal
                     if (encounter.type == EncounterType.BOSS) {
                        // This is heal for boss fights
                        if (player.charClass.equalsIgnoreCase("healer")) {
                           double heal = player.charisma + (player.intelligence / 2);
                           double oldHP = player.currentHP;
                           player.currentHP = Math.min(player.currentHP + heal, player.getMaxHP());
                           double actualHeal = player.currentHP - oldHP;
                           System.out.println("You heal yourself for " +
                                 String.format("%.1f", actualHeal) + " HP!");
                           turnTaken = true;
                        }
                     } else {
                        // This is flee for regular fights
                        double fleeChance = (player.agility * 10) + 30;
                        if (random.nextDouble() * 100 < fleeChance) {
                           System.out.println("You successfully flee from the " + enemy.name + "!");
                           battleEscaped = true;
                           turnTaken = true;
                        } else {
                           System.out.println("You couldn't escape!");
                           turnTaken = true;
                        }
                     }
                     break;

                  case 6: // Heal (only for non-boss fights with healer)
                     if (encounter.type != EncounterType.BOSS && player.charClass.equalsIgnoreCase("healer")) {
                        double heal = player.charisma + (player.intelligence / 2);
                        double oldHP = player.currentHP;
                        player.currentHP = Math.min(player.currentHP + heal, player.getMaxHP());
                        double actualHeal = player.currentHP - oldHP;
                        System.out.println("You heal yourself for " +
                              String.format("%.1f", actualHeal) + " HP!");
                        turnTaken = true;
                     }
                     break;
               }
            }
         } else {
            System.out.println("You are stunned and cannot act this turn!");
         }

         if (battleEscaped) {
            return false; // Player fled
         }

         if (!enemy.isAlive()) {
            System.out.println("You defeated the " + enemy.name + "!");
            if (encounter.type == EncounterType.BOSS) {
               System.out.println("🏆 BOSS DEFEATED! 🏆");
            }
            player.addEnemyKill(enemy.name);
            player.checkQuestCompletion(scanner);
            player.gainExp(enemy.expReward, scanner);
            player.gold += enemy.goldReward;
            player.restoreResources();
            return true;
         }

         // Enemy's turn
         if (!enemy.isStunned()) {
            System.out.println("\n--- " + enemy.name + "'s Turn ---");
            double enemyDmg = enemy.getAttackDamage(player);

            if (isDefending) {
               double defense = (player.vitality / 100);
               if (defense >= 1)
                  defense = 0.75;
               enemyDmg *= (1 - defense);
               System.out.println("Your defensive stance reduces the damage by " +
                     String.format("%.1f", defense * 100) + "%");
            }

            player.takeDamage(enemyDmg);
            System.out.println(enemy.name + " hits you for " +
                  String.format("%.1f", enemyDmg) + " damage!");
         } else {
            System.out.println(enemy.name + " is stunned and cannot attack!");
         }

         isDefending = false;
      }

      // If player died, return false to exit dungeon
      if (!player.isAlive()) {
         return false;
      }

      return player.isAlive();
   }

   private boolean handleTreasureEncounter(Player player, DungeonEncounter encounter, Scanner scanner) {
      System.out.println("You open the treasure chest...");

      if (encounter.data instanceof Item[]) {
         Item[] items = (Item[]) encounter.data;
         for (Item item : items) {
            player.addItem(item);
            System.out.println("Found: " + item.itemName + "!");
         }
      } else if (encounter.data instanceof Consumable[]) {
         Consumable[] consumables = (Consumable[]) encounter.data;
         for (Consumable consumable : consumables) {
            // Add to player's consumables
            boolean found = false;
            for (Consumable playerConsumable : player.consumables) {
               if (playerConsumable.name.equals(consumable.name)) {
                  playerConsumable.quantity += consumable.quantity;
                  found = true;
                  break;
               }
            }
            if (!found) {
               player.consumables.add(new Consumable(consumable.name, consumable.effect,
                     consumable.value, consumable.quantity, consumable.buyPrice,
                     consumable.sellPrice, consumable.description));
            }
            System.out.println("Found: " + consumable.name + " x" + consumable.quantity + "!");
         }
      }

      // Small gold bonus
      int goldFound = random.nextInt(20) + 10;
      player.gold += goldFound;
      System.out.println("Found: " + goldFound + " gold!");

      return true;
   }

   private boolean handleTrapEncounter(Player player, DungeonEncounter encounter, Scanner scanner) {
      System.out.println("Do you want to:");
      System.out.println("1. Open the chest carefully");
      System.out.println("2. Try to disarm any traps first");
      System.out.println("3. Leave it alone");

      int choice = InputValidator.getIntInput(scanner, "Choose option (1-3): ", 1, 3);

      switch (choice) {
         case 1: // Open carefully
            if (encounter.data instanceof String && encounter.data.equals("TRAPPED")) {
               double trapDamage = 15 + random.nextInt(20);
               player.takeDamage(trapDamage);
               System.out.println("💥 TRAP! You take " + String.format("%.1f", trapDamage) + " damage!");
               if (player.isAlive()) {
                  System.out.println("The chest was empty anyway...");
               }
            } else {
               System.out.println("Lucky! The chest wasn't trapped.");
               return handleTreasureEncounter(player,
                     new DungeonEncounter(EncounterType.TREASURE_CHEST, "", encounter.data), scanner);
            }
            break;

         case 2: // Disarm traps
            double disarmChance = player.agility * 5 + 25;
            if (random.nextDouble() * 100 < disarmChance) {
               System.out.println("You successfully disarm the trap mechanism!");
               if (!(encounter.data instanceof String && encounter.data.equals("TRAPPED"))) {
                  return handleTreasureEncounter(player,
                        new DungeonEncounter(EncounterType.TREASURE_CHEST, "", encounter.data), scanner);
               } else {
                  System.out.println("The chest was empty, but at least you're safe!");
               }
            } else {
               double trapDamage = 20 + random.nextInt(15);
               player.takeDamage(trapDamage);
               System.out.println("💥 You trigger the trap while trying to disarm it! " +
                     String.format("%.1f", trapDamage) + " damage!");
            }
            break;

         case 3: // Leave it
            System.out.println("You decide it's not worth the risk and move on.");
            break;
      }

      return player.isAlive();
   }

   private boolean handleMerchantEncounter(Player player, DungeonEncounter encounter, Scanner scanner) {
      if (!(encounter.data instanceof Consumable[])) {
         System.out.println("ERROR: Invalid merchant encounter data!");
         return true;
      }
      Consumable[] merchantStock = (Consumable[]) encounter.data;

      System.out.println("The merchant's eyes gleam as he shows his wares:");
      System.out.println("Your Gold: " + (int) player.gold + "g");
      System.out.println();

      for (int i = 0; i < merchantStock.length; i++) {
         System.out.print((i + 1) + ". ");
         merchantStock[i].displayConsumable();
      }
      System.out.println((merchantStock.length + 1) + ". \"No thanks, I'll be going.\"");

      int choice = InputValidator.getIntInput(scanner,
            "Choose item to buy (1-" + (merchantStock.length + 1) + "): ", 1, merchantStock.length + 1);

      if (choice <= merchantStock.length) {
         Consumable item = merchantStock[choice - 1];

         if (player.gold >= item.buyPrice) {
            player.gold -= item.buyPrice;

            // Add to player's consumables
            boolean found = false;
            for (Consumable playerConsumable : player.consumables) {
               if (playerConsumable.name.equals(item.name)) {
                  playerConsumable.quantity += item.quantity;
                  found = true;
                  break;
               }
            }
            if (!found) {
               player.consumables.add(new Consumable(item.name, item.effect, item.value,
                     item.quantity, item.buyPrice, item.sellPrice, item.description));
            }

            System.out.println("\"Pleasure doing business!\" You bought " + item.name + "!");
         } else {
            System.out.println("\"Sorry adventurer, you don't have enough gold for that.\"");
         }
      } else {
         System.out.println("\"Safe travels, adventurer!\"");
      }

      return true;
   }

   private boolean handleRestEncounter(Player player, DungeonEncounter encounter, Scanner scanner) {
      System.out.println("Do you want to:");
      System.out.println("1. Rest here (restore HP, Mana, and Stamina)");
      System.out.println("2. Pray at the shrine (small permanent stat boost)");
      System.out.println("3. Continue without stopping");

      int choice = InputValidator.getIntInput(scanner, "Choose option (1-3): ", 1, 3);

      switch (choice) {
         case 1: // Rest
            double hpHealed = player.getMaxHP() * 0.6;
            double manaRestored = player.getMaxMana() * 0.8;
            double staminaRestored = player.getMaxStamina() * 0.8;

            player.currentHP = Math.min(player.currentHP + hpHealed, player.getMaxHP());
            player.currentMana = Math.min(player.currentMana + manaRestored, player.getMaxMana());
            player.currentStamina = Math.min(player.currentStamina + staminaRestored, player.getMaxStamina());

            System.out.println("You rest peacefully and feel your strength returning.");
            System.out.println("Restored " + String.format("%.1f", hpHealed) + " HP, " +
                  String.format("%.1f", manaRestored) + " Mana, " +
                  String.format("%.1f", staminaRestored) + " Stamina!");
            break;

         case 2: // Pray
            System.out.println("You kneel before the shrine and offer a prayer...");

            // Small random stat boost
            double rand = random.nextDouble();
            if (rand < 0.2) {
               player.strength += 0.5;
               System.out.println("Your body feels stronger! Strength +0.5");
            } else if (rand < 0.4) {
               player.vitality += 0.5;
               System.out.println("Your constitution improves! Vitality +0.5");
            } else if (rand < 0.6) {
               player.intelligence += 0.5;
               System.out.println("Your mind sharpens! Intelligence +0.5");
            } else if (rand < 0.8) {
               player.agility += 0.5;
               System.out.println("You feel more nimble! Agility +0.5");
            } else {
               player.charisma += 0.5;
               System.out.println("Your presence becomes more commanding! Charisma +0.5");
            }
            break;

         case 3: // Continue
            System.out.println("You decide to press on without delay.");
            break;
      }

      return true;
   }

   private String getThemeDescription(String theme) {
      switch (theme.toLowerCase()) {
         case "Test":
            return "You enter an endless training hall where the only way to go is forward";
         case "rat":
            return "You hear scurrying sounds echoing through the damp corridors.";
         case "goblin":
            return "Crude drawings and symbols cover the walls. You smell smoke and hear distant chatter.";
         case "undead":
            return "The air grows cold. You hear moaning and rattling echoing from the shadows.";
         case "orc":
            return "Battle cries echo through the halls. Weapons clatter against stone.";
         case "spider":
            return "Thick webs hang from the ceiling. You hear clicking sounds in the darkness.";
         case "elemental":
            return "The air crackles with magical energy. Elements dance around you.";
         case "testing":
            return "You... \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n How did you get here? \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n This is not a real dungeon. \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n leave before I have to stop you myself";
         default:
            return "You venture deeper into the unknown.";
      }
   }
}