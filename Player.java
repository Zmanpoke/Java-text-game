import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// =================== PLAYER ===================
class Player {
   String name;
   String charClass;
   int lvl = 1;
   double strength, vitality, intelligence, agility, charisma;
   int exp = 0;
   double currentHP;
   double gold = 0;
   List<Item> inventory = new ArrayList<>();
   double currentMana;
   double currentStamina;
   List<StatusEffect> statusEffects = new ArrayList<>();
   List<Quest> activeQuests = new ArrayList<>();
   List<String> enemyKillCounts = new ArrayList<>(); // Track kills: "EnemyName:Count"
   List<Consumable> consumables = new ArrayList<>();

   // Equipment slots
   Item mainWeapon = null;
   Item armor = null;
   Item shield = null;
   Item accessory1 = null;
   Item accessory2 = null;

   public Player(String name, String charClass, double strength, double vitality, double intelligence,
         double agility,
         double charisma, double gold) {
      this.name = name;
      this.charClass = charClass;
      this.strength = strength;
      this.vitality = vitality;
      this.intelligence = intelligence;
      this.agility = agility;
      this.charisma = charisma;
      this.currentHP = getMaxHP();
      this.gold = gold;
      this.currentMana = getMaxMana();
      this.currentStamina = getMaxStamina();
   }

   // ----- Inventory handling -----
   public void addItem(Item item) {
      inventory.add(item);
   }

   public void removeItem(Item item) {
      inventory.remove(item);
   }

   // ----- Equipment Management -----
   public boolean equipItem(Item item) {
      if (!canEquipItem(item)) {
         return false;
      }

      switch (item.slotType.toLowerCase()) {
         case "weapon":
            if (mainWeapon != null) {
               unequipItem(mainWeapon);
            }
            mainWeapon = item;
            break;
         case "armor":
            if (armor != null) {
               unequipItem(armor);
            }
            armor = item;
            break;
         case "shield":
            if (shield != null) {
               unequipItem(shield);
            }
            shield = item;
            break;
         case "accessory":
            if (accessory1 == null) {
               accessory1 = item;
            } else if (accessory2 == null) {
               accessory2 = item;
            } else {
               // Both accessory slots full, replace first one
               unequipItem(accessory1);
               accessory1 = item;
            }
            break;
         default:
            return false;
      }

      // Apply stat bonuses
      strength += item.strBonus;
      vitality += item.vitBonus;
      intelligence += item.intBonus;
      agility += item.agiBonus;
      charisma += item.charBonus;

      inventory.remove(item);
      System.out.println("Equipped " + item.itemName + "!");
      return true;
   }

   public void unequipItem(Item item) {
      // Remove stat bonuses
      strength -= item.strBonus;
      vitality -= item.vitBonus;
      intelligence -= item.intBonus;
      agility -= item.agiBonus;
      charisma -= item.charBonus;

      // Remove from slot
      if (mainWeapon == item) {
         mainWeapon = null;
      } else if (armor == item) {
         armor = null;
      } else if (shield == item) {
         shield = null;
      } else if (accessory1 == item) {
         accessory1 = null;
      } else if (accessory2 == item) {
         accessory2 = null;
      }

      // Add back to inventory
      inventory.add(item);
      System.out.println("Unequipped " + item.itemName + "!");
   }

   private boolean canEquipItem(Item item) {
      // Check class requirements
      if (!item.reqClass.equalsIgnoreCase("Any")) {
         if (item.reqClass.contains("|")) {
            String[] allowedClasses = item.reqClass.split("\\|");
            boolean canUse = false;
            for (String allowedClass : allowedClasses) {
               if (allowedClass.trim().equalsIgnoreCase(charClass)) {
                  canUse = true;
                  break;
               }
            }
            if (!canUse) {
               System.out.println("You cannot use this item. It requires class: " + item.reqClass);
               return false;
            }
         } else if (!item.reqClass.equalsIgnoreCase(charClass)) {
            System.out.println("You cannot use this item. It requires class: " + item.reqClass);
            return false;
         }
      }

      // Check level requirements
      if (lvl < item.reqlvl) {
         System.out.println("You need to be level " + item.reqlvl + " to equip this item.");
         return false;
      }

      return true;
   }

   // ----- Derived stats -----
   public double getMaxHP() {
      double bonusHP = 0;
      if (mainWeapon != null)
         bonusHP += mainWeapon.hpBonus;
      if (armor != null)
         bonusHP += armor.hpBonus;
      if (shield != null)
         bonusHP += shield.hpBonus;
      if (accessory1 != null)
         bonusHP += accessory1.hpBonus;
      if (accessory2 != null)
         bonusHP += accessory2.hpBonus;
      return (vitality * 2 + 20) + bonusHP;
   }

   public double getMaxMana() {
      return intelligence * 5;
   }

   public double getMaxStamina() {
      return (agility + strength + vitality) * 3;
   }

   // ----- Status Effects -----
   public void applyStatusEffect(StatusEffect effect) {
      statusEffects.add(effect);
      System.out.println(name + " is now affected by " + effect.name + " for " + effect.duration
            + " turns!");
   }

   public void processStatusEffects() {
      for (int i = statusEffects.size() - 1; i >= 0; i--) {
         StatusEffect effect = statusEffects.get(i);
         if (effect.damagePerTurn > 0) {
            takeDamage(effect.damagePerTurn);
            System.out.println(name + " takes " + effect.damagePerTurn + " damage from "
                  + effect.name + "!");
         }
         effect.duration--;
         if (effect.duration <= 0) {
            System.out.println(effect.name + " effect has worn off!");
            statusEffects.remove(i);
         }
      }
   }

   public boolean isStunned() {
      for (StatusEffect effect : statusEffects) {
         if (effect.preventsAction)
            return true;
      }
      return false;
   }

   // ----- Combat Methods -----
   public double getAttackDamage() {
      double baseDamage;
      switch (charClass.toLowerCase()) {
         case "tank":
            baseDamage = 2 + strength + (vitality / 2);
            break;
         case "warrior":
            baseDamage = 2 + strength;
            double hpPercent = currentHP / getMaxHP();
            if (hpPercent < 0.25)
               baseDamage += 5;
            else if (hpPercent < 0.5)
               baseDamage += 2;
            break;
         case "healer":
            baseDamage = 2 + intelligence + (charisma / 2);
            break;
         case "mage":
            baseDamage = intelligence * 1.25;
            break;
         case "archer":
            baseDamage = 2 + agility;
            break;
         case "bard":
            baseDamage = 2 + agility;
            break;
         case "developer":
            baseDamage = 9999999999.0;
            break;
         default:
            baseDamage = 2;
            break;
      }

      // Critical hit calculation
      Random random = new Random();
      double critChance = Math.min(agility * 2, 50);
      double roll = random.nextDouble() * 100;

      if (roll < critChance) {
         double critMultiplier = 1.5 + (agility / 100);
         System.out.println("CRITICAL HIT! (+" + String.format("%.0f", (critMultiplier - 1) * 100)
               + "% damage)");
         return baseDamage * critMultiplier;
      }

      return baseDamage;
   }

   // ----- Skill/Spell Methods -----
   public boolean useSkillOrSpell(Enemy enemy, Scanner scanner) {
      boolean actionTaken = false;
      switch (charClass.toLowerCase()) {
         case "healer":
            actionTaken = useHealerSpell(scanner);
            break;
         case "warrior":
            actionTaken = useWarriorSkill(enemy, scanner);
            break;
         case "archer":
            actionTaken = useArcherSkill(enemy, scanner);
            break;
         case "mage":
            actionTaken = useMageSpell(enemy, scanner);
            break;
         case "bard":
            actionTaken = useBardSkill(enemy, scanner);
            break;
         case "tank":
            actionTaken = useTankSkill(enemy, scanner);
            break;
         default:
            System.out.println("Your class has no special abilities!");
      }
      return actionTaken;
   }

   private boolean useHealerSpell(Scanner scanner) {
      System.out.println("\n--- Healer Spells ---");
      System.out.println("1. Minor Heal (10 Mana, 5 Stamina) - Heal " + (charisma + intelligence / 2)
            + " HP");
      System.out.println("2. Major Heal (25 Mana, 10 Stamina) - Heal " + (charisma * 2 + intelligence)
            + " HP");
      System.out.println("3. Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose spell (1-3): ", 1, 3);
      switch (choice) {
         case 1:
            if (currentMana >= 10 && currentStamina >= 5) {
               double heal = charisma + (intelligence / 2);
               currentMana -= 10;
               currentStamina -= 5;
               double oldHP = currentHP;
               currentHP = Math.min(currentHP + heal, getMaxHP());
               System.out.println("You cast Minor Heal and restore "
                     + (currentHP - oldHP) + " HP!");
               return true;
            } else {
               System.out.println("Not enough mana or stamina!");
               return false;
            }
         case 2:
            if (currentMana >= 25 && currentStamina >= 10) {
               double heal = charisma * 2 + intelligence;
               currentMana -= 25;
               currentStamina -= 10;
               double oldHP = currentHP;
               currentHP = Math.min(currentHP + heal, getMaxHP());
               System.out.println("You cast Major Heal and restore "
                     + (currentHP - oldHP) + " HP!");
               return true;
            } else {
               System.out.println("Not enough mana or stamina!");
               return false;
            }
         default: // Cancel
            return false;
      }
   }

   private boolean useWarriorSkill(Enemy enemy, Scanner scanner) {
      System.out.println("\n--- Warrior Skills ---");
      System.out.println("1. Power Strike (15 Stamina) - Deal " + (strength * 1.5) + " damage");
      System.out.println("2. Berserker Rage (20 Stamina) - Deal " + (strength * 2)
            + " damage, 30% bleed chance");
      System.out.println("3. Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose skill (1-3): ", 1, 3);
      Random random = new Random();

      switch (choice) {
         case 1:
            if (currentStamina >= 15) {
               double damage = strength * 1.5;
               currentStamina -= 15;
               enemy.takeDamage(damage);
               System.out.println("You use Power Strike for " + damage + " damage!");
               return true;
            } else {
               System.out.println("Not enough stamina!");
               return false;
            }
         case 2:
            if (currentStamina >= 20) {
               double damage = strength * 2;
               currentStamina -= 20;
               enemy.takeDamage(damage);
               System.out.println("You enter a berserker rage for " + damage
                     + " damage!");

               if (random.nextDouble() * 100 < 30) {
                  enemy.applyStatusEffect(
                        new StatusEffect("Bleed", 3, 5, false));
               }
               return true;
            } else {
               System.out.println("Not enough stamina!");
               return false;
            }
         default: // Cancel
            return false;
      }
   }

   private boolean useArcherSkill(Enemy enemy, Scanner scanner) {
      System.out.println("\n--- Archer Skills ---");
      System.out.println("1. Aimed Shot (10 Stamina) - Deal " + (agility * 1.5)
            + " damage, high crit chance");
      System.out.println("2. Poison Arrow (15 Stamina) - Deal " + agility + " damage, 50% poison chance");
      System.out.println("3. Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose skill (1-3): ", 1, 3);
      Random random = new Random();

      switch (choice) {
         case 1:
            if (currentStamina >= 10) {
               double damage = agility * 1.5;
               currentStamina -= 10;

               // Higher crit chance for aimed shot
               if (random.nextDouble() * 100 < 50) {
                  damage *= 1.8;
                  System.out.println("CRITICAL AIMED SHOT!");
               }

               enemy.takeDamage(damage);
               System.out.println("You fire an aimed shot for " + damage
                     + " damage!");
               return true;
            } else {
               System.out.println("Not enough stamina!");
               return false;
            }
         case 2:
            if (currentStamina >= 15) {
               double damage = agility;
               currentStamina -= 15;
               enemy.takeDamage(damage);
               System.out.println("You fire a poison arrow for " + damage
                     + " damage!");

               if (random.nextDouble() * 100 < 50) {
                  enemy.applyStatusEffect(new StatusEffect("Poison", 4, 3,
                        false));
               }
               return true;
            } else {
               System.out.println("Not enough stamina!");
               return false;
            }
         default: // Cancel
            return false;
      }
   }

   private boolean useMageSpell(Enemy enemy, Scanner scanner) {
      System.out.println("\n--- Mage Spells ---");
      System.out.println("1. Fireball (20 Mana) - Deal " + (intelligence * 1.5)
            + " damage, 25% burn chance");
      System.out.println("2. Lightning Bolt (25 Mana) - Deal " + (intelligence * 1.8)
            + " damage, 20% stun chance");
      System.out.println("3. Ice Shard (15 Mana) - Deal " + intelligence + " damage, always slows enemy");
      System.out.println("4. Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose spell (1-4): ", 1, 4);
      Random random = new Random();

      switch (choice) {
         case 1:
            if (currentMana >= 20) {
               double damage = intelligence * 1.5;
               currentMana -= 20;
               enemy.takeDamage(damage);
               System.out.println("You cast Fireball for " + damage + " damage!");

               if (random.nextDouble() * 100 < 25) {
                  enemy.applyStatusEffect(
                        new StatusEffect("Burn", 3, 4, false));
               }
               return true;
            } else {
               System.out.println("Not enough mana!");
               return false;
            }
         case 2:
            if (currentMana >= 25) {
               double damage = intelligence * 1.8;
               currentMana -= 25;
               enemy.takeDamage(damage);
               System.out.println("You cast Lightning Bolt for " + damage
                     + " damage!");

               if (random.nextDouble() * 100 < 20) {
                  enemy.applyStatusEffect(
                        new StatusEffect("Stun", 1, 0, true));
               }
               return true;
            } else {
               System.out.println("Not enough mana!");
               return false;
            }
         case 3:
            if (currentMana >= 15) {
               double damage = intelligence;
               currentMana -= 15;
               enemy.takeDamage(damage);
               System.out.println("You cast Ice Shard for " + damage + " damage!");
               enemy.applyStatusEffect(new StatusEffect("Slow", 2, 0, false));
               return true;
            } else {
               System.out.println("Not enough mana!");
               return false;
            }
         default: // Cancel
            return false;
      }
   }

   private boolean useBardSkill(Enemy enemy, Scanner scanner) {
      System.out.println("\n--- Bard Skills ---");
      System.out.println("1. Inspiring Song (10 Mana) - Restore stamina and boost next attack");
      System.out.println("2. Discordant Note (15 Mana) - Deal " + (charisma * 1.2)
            + " damage, confuse enemy");
      System.out.println("3. Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose skill (1-3): ", 1, 3);

      switch (choice) {
         case 1:
            if (currentMana >= 10) {
               currentMana -= 10;
               currentStamina = Math.min(currentStamina + 20, getMaxStamina());
               System.out.println("You play an inspiring song! Stamina restored and next attack boosted!");
               // Could add temporary buff here
               return true;
            } else {
               System.out.println("Not enough mana!");
               return false;
            }
         case 2:
            if (currentMana >= 15) {
               double damage = charisma * 1.2;
               currentMana -= 15;
               enemy.takeDamage(damage);
               System.out.println("You play a discordant note for " + damage
                     + " damage!");
               enemy.applyStatusEffect(new StatusEffect("Confusion", 2, 0, false));
               return true;
            } else {
               System.out.println("Not enough mana!");
               return false;
            }
         default: // Cancel
            return false;
      }
   }

   private boolean useTankSkill(Enemy enemy, Scanner scanner) {
      System.out.println("\n--- Tank Skills ---");
      System.out.println(
            "1. Shield Bash (12 Stamina) - Deal " + (vitality + strength / 2)
                  + " damage, 40% stun chance");
      System.out.println("2. Taunt (8 Stamina) - Force enemy to focus on you, reduce their damage");
      System.out.println("3. Cancel");

      int choice = InputValidator.getIntInput(scanner, "Choose skill (1-3): ", 1, 3);
      Random random = new Random();

      switch (choice) {
         case 1:
            if (currentStamina >= 12) {
               double damage = vitality + (strength / 2);
               currentStamina -= 12;
               enemy.takeDamage(damage);
               System.out.println("You shield bash for " + damage + " damage!");

               if (random.nextDouble() * 100 < 40) {
                  enemy.applyStatusEffect(
                        new StatusEffect("Stun", 1, 0, true));
               }
               return true;
            } else {
               System.out.println("Not enough stamina!");
               return false;
            }
         case 2:
            if (currentStamina >= 8) {
               currentStamina -= 8;
               System.out.println("You taunt the enemy, reducing their damage!");
               enemy.applyStatusEffect(new StatusEffect("Weakened", 3, 0, false));
               return true;
            } else {
               System.out.println("Not enough stamina!");
               return false;
            }
         default: // Cancel
            return false;
      }
   }

   public void takeDamage(double dmg) {
      currentHP -= dmg;
      if (currentHP < 0)
         currentHP = 0;
   }

   public boolean isAlive() {
      return currentHP > 0;
   }

   public void restoreResources() {
      // Restore some mana and stamina after combat
      currentMana = Math.min(currentMana + (intelligence / 2), getMaxMana());
      currentStamina = Math.min(currentStamina + ((strength + agility) / 2), getMaxStamina());
   }

   // ----- Quest Methods -----
   public void addEnemyKill(String enemyName) {
      // Find existing kill count or create new one
      for (int i = 0; i < enemyKillCounts.size(); i++) {
         try {
            String[] parts = enemyKillCounts.get(i).split(":");
            if (parts.length >= 2 && parts[0].equals(enemyName)) {
               int count = Integer.parseInt(parts[1]) + 1;
               enemyKillCounts.set(i, enemyName + ":" + count);
               return;
            }
         } catch (NumberFormatException e) {
            // Malformed data, remove it
            enemyKillCounts.remove(i);
            i--;
         }
      }
      // If not found, add new entry
      enemyKillCounts.add(enemyName + ":1");
   }

   public int getEnemyKillCount(String enemyName) {
      for (String killCount : enemyKillCounts) {
         try {
            String[] parts = killCount.split(":");
            if (parts.length >= 2 && parts[0].equals(enemyName)) {
               return Integer.parseInt(parts[1]);
            }
         } catch (NumberFormatException e) {
            // Malformed data, skip it
            continue;
         }
      }
      return 0;
   }

   public void checkQuestCompletion(Scanner scanner) {
      for (int i = activeQuests.size() - 1; i >= 0; i--) {
         Quest quest = activeQuests.get(i);
         if (getEnemyKillCount(quest.enemyName) >= quest.quantity) {
            System.out.println("\nQuest Completed: " + quest.questName + "!");
            System.out.println("You earned " + quest.goldReward + " gold and " + quest.expReward + " experience!");
            gold += quest.goldReward;
            gainExp(quest.expReward, scanner);

            // Reset the kill count for this enemy type to prevent quest auto-completion
            for (int j = 0; j < enemyKillCounts.size(); j++) {
               try {
                  String[] parts = enemyKillCounts.get(j).split(":");
                  if (parts.length >= 2 && parts[0].equals(quest.enemyName)) {
                     int remainingKills = Integer.parseInt(parts[1]) - quest.quantity;
                     if (remainingKills <= 0) {
                        enemyKillCounts.remove(j);
                     } else {
                        enemyKillCounts.set(j, quest.enemyName + ":" + remainingKills);
                     }
                     break;
                  }
               } catch (NumberFormatException e) {
                  // Malformed data, remove it
                  enemyKillCounts.remove(j);
                  j--;
               }
            }

            activeQuests.remove(i);
         }
      }
   }

   public void gainExp(int amount, Scanner scanner) {
      exp += amount;
      int requiredExp = 50 + (lvl - 1) * 25;
      System.out.println("You gained " + amount + " EXP! Total EXP: " + exp + "/" + requiredExp);

      while (exp >= requiredExp) {
         exp -= requiredExp;
         lvl++;
         requiredExp = 50 + (lvl - 1) * 25;

         System.out.println("Level Up! You are now level " + lvl + "! Choose a stat to increase:");
         System.out.println("s = Strength, v = Vitality, i = Intelligence, a = Agility, c = Charisma");

         char[] validStats = { 's', 'v', 'i', 'a', 'c' };
         char choice = InputValidator.getCharInput(scanner, "Enter your choice: ", validStats);

         switch (choice) {
            case 's':
               strength++;
               System.out.println("Strength increased! (" + (strength - 1) + " -> " + strength + ")");
               break;
            case 'v':
               vitality++;
               System.out.println("Vitality increased! (" + (vitality - 1) + " -> " + vitality + ")");
               break;
            case 'i':
               intelligence++;
               System.out.println("Intelligence increased! (" + (intelligence - 1) + " -> " + intelligence + ")");
               break;
            case 'a':
               agility++;
               System.out.println("Agility increased! (" + (agility - 1) + " -> " + agility + ")");
               break;
            case 'c':
               charisma++;
               System.out.println("Charisma increased! (" + (charisma - 1) + " -> " + charisma + ")");
               break;
         }

         currentHP = getMaxHP();
         currentMana = getMaxMana();
         currentStamina = getMaxStamina();
         System.out.println("You feel stronger! All resources fully restored.");
      }
   }

   // ----- Display -----
   public void displayStats() {
      System.out.println("\n--- Player Stats ---");
      System.out.println("Name: " + name + " | Class: " + charClass + " | Level: " + lvl);
      System.out.println("HP: " + String.format("%.1f", currentHP) + "/"
            + String.format("%.1f", getMaxHP()));
      System.out.println("Mana: " + String.format("%.1f", currentMana) + "/"
            + String.format("%.1f", getMaxMana()));
      System.out.println(
            "Stamina: " + String.format("%.1f", currentStamina) + "/"
                  + String.format("%.1f", getMaxStamina()));
      System.out.println("Strength: " + strength + " | Vitality: " + vitality);
      System.out.println("Intelligence: " + intelligence + " | Agility: " + agility + " | Charisma: "
            + charisma);
      System.out.println("EXP: " + exp + " | Gold: " + gold);

      if (!statusEffects.isEmpty()) {
         System.out.print("Status Effects: ");
         for (StatusEffect effect : statusEffects) {
            System.out.print(effect.name + "(" + effect.duration + ") ");
         }
         System.out.println();
      }
   }

   public void displayInventory() {
      System.out.println("\n--- Equipment & Inventory ---");

      // Display equipped items
      System.out.println("Currently Equipped:");
      System.out.println("  Main Weapon: " + (mainWeapon != null ? mainWeapon.itemName : "None"));
      System.out.println("  Armor: " + (armor != null ? armor.itemName : "None"));
      System.out.println("  Shield: " + (shield != null ? shield.itemName : "None"));
      System.out.println("  Accessory 1: " + (accessory1 != null ? accessory1.itemName : "None"));
      System.out.println("  Accessory 2: " + (accessory2 != null ? accessory2.itemName : "None"));

      // Display unequipped items
      System.out.println("\nInventory:");
      if (inventory.isEmpty()) {
         System.out.println("  Empty");
      } else {
         for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item != null) {
               System.out
                     .println("  " + (i + 1) + ". " + item.itemName + " (" + item.slotType + ")");
            } else {
               System.out.println("  " + (i + 1) + ". [Corrupted Item Data]");
            }
         }
      }

      // Display consumables
      System.out.println("\nConsumables:");
      if (consumables.isEmpty()) {
         System.out.println("  None");
      } else {
         for (int i = 0; i < consumables.size(); i++) {
            Consumable consumable = consumables.get(i);
            if (consumable != null) {
               System.out.println("  " + (i + 1) + ". " + consumable.name + " (x" + consumable.quantity + ")");
            } else {
               System.out.println("  " + (i + 1) + ". [Corrupted Consumable Data]");
            }
         }
      }
   }
}