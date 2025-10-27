import java.util.Scanner;

class DungeonEncounter {
   EncounterType type;
   String description;
   Object data; // Can be Enemy, Item[], Consumable[], etc.

   public DungeonEncounter(EncounterType type, String description, Object data) {
      this.type = type;
      this.description = description;
      this.data = data;
   }
}

// =================== MAIN ===================
public class Main {
   public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);

      // --- PLAYER CREATION WITH VALIDATION ---
      String name = InputValidator.getStringInput(scanner, "Enter your character's name: ");

      System.out.println("\nChoose Your Class:");
      System.out.println("h = Healer\nw = Warrior\na = Archer\nm = Mage\nb = Bard\nt = Tank");

      char[] validClasses = { 'h', 'w', 'a', 'm', 'b', 't', ',' };
      char choice = InputValidator.getCharInput(scanner, "Enter your class choice: ", validClasses);

      Player player = null;

      switch (choice) {
         // Strength, Vitality, Intelligence, Agility, Charisma, Gold
         case 'h':
            player = new Player(name, "Healer", 2, 3, 4, 2, 5, 0);
            break;
         case 'w':
            player = new Player(name, "Warrior", 6, 5, 1, 5, 2, 0);
            break;
         case 'a':
            player = new Player(name, "Archer", 4, 4, 3, 6, 2, 0);
            break;
         case 'm':
            player = new Player(name, "Mage", 1, 1, 8, 4, 4, 0);
            break;
         case 'b':
            player = new Player(name, "Bard", 3, 3, 5, 5, 6, 0);
            break;
         case 't':
            player = new Player(name, "Tank", 4, 6, 2, 3, 2, 0);
            break;
         case ',':
            player = new Player(name, "Developer", 10.0, 200000.0, 10.0, 10000.0, 10.0, 10000);
            break;
      }

      // --- ENEMIES ---
      Enemy[] enemies = {
            // name, stregnth, vitality, Agility, gold, exp
            new Enemy("Rat", 1.0, 1.0, 3.0, 5, 2),
            new Enemy("Slime", 2.0, 2.0, 1.0, 10, 4),
            new Enemy("Goblin", 5.0, 4.0, 5.0, 15, 7),
            new Enemy("Kobold", 6.0, 6.0, 4.0, 20, 9),
            new Enemy("Skeleton", 10.0, 4.0, 3.0, 25, 13),
            new Enemy("Giant Spider", 8, 8, 10, 30, 15),
            new Enemy("Zombie", 5, 15, 5, 40, 17),
            new Enemy("Orc", 12.0, 9.0, 2.0, 50, 18),
            new Enemy("Web Spinner", 10, 12, 15, 60, 20),
            new Enemy("Troll", 10.0, 15.0, 2.0, 65, 25),
            new Enemy("Minotaur", 20.0, 15.0, 4.0, 75, 50),
            new Enemy("Giant", 30.0, 17.0, 1.0, 100, 75),
            new Enemy("Dragon", 100.0, 200.0, 50.0, 1000, 100),
            new Enemy("Kraken", 150.0, 500.0, 50.0, 1500, 200),
            new Enemy("Leviathan", 200.0, 1000.0, 100.0, 2000, 300),
            new Enemy("Behemoth", 300.0, 1500.0, 150.0, 2500, 400),
            new Enemy("Archdemon", 500.0, 2000.0, 200.0, 3000, 500),
            new Enemy("Glass Cannon", 9999999999.0, 50.0, 1000.0, 1000, 1000),
            new Enemy("Demon King", 1000.0, 1000.0, 1000.0, 10000, 1000),
            new Enemy("Dummy Bag", -2.0, 99999999999.0, 0.0, 0, 0),
      };
      // =================== SHOP INITIALIZATION IN MAIN ===================

      // Separate weapons into categories
      Item[] swords = {
            // Warrior weapons
            new Item("Rusty Sword", "Warrior", 1, 1, 0, 0, 0, 0, 0, 5, 5, "weapon"),
            new Item("Wooden sword", "Warrior", 2, 2, 0, 0, 0, 0, 0, 8, 15, "weapon"),
            new Item("Iron Sword", "Warrior", 5, 4, 0, 0, 0, 0, 0, 13, 25, "weapon"),
            new Item("Steel Sword", "Warrior", 8, 7, 0, 0, 0, 0, 0, 25, 50, "weapon"),
            new Item("Mithril Sword", "Warrior", 10, 10, 0, 0, 0, 0, 0, 50, 100, "weapon"),
            new Item("Adamantite Sword", "Warrior", 15, 20, 5, 0, 0, 0, 0, 100, 200, "weapon"),
            new Item("Dragon Slayer", "Warrior", 20, 30, 10, 0, 0, 0, 0, 200, 400, "weapon"),
      };
      Item[] healers = {
            // Healer items
            new Item("Healing Orb", "Healer", 1, 0, 0, 0, 0, 0, 10, 5, 5, "weapon"),
            new Item("Staff of Healing", "Healer", 3, 0, 0, 2, 0, 0, 0, 8, 15, "weapon"),
            new Item("Syringe", "Healer", 5, 0, 0, 0, 0, 0, 5, 13, 25, "weapon"),
            new Item("Holy Text", "Healer", 8, 0, 0, 0, 0, 0, 0, 25, 50, "weapon"),
            new Item("Star of Healing", "Healer", 10, 0, 0, 0, 0, 0, 0, 50, 100, "weapon"),
            new Item("Staff of Life", "Healer", 15, 0, 0, 0, 0, 0, 0, 100, 200, "weapon"),
      };
      Item[] instruments = {
            new Item("Lute", "Bard", 1, 0, 0, 0, 0, 2, 0, 5, 5, "weapon"),
            new Item("Harp", "Bard", 3, 0, 0, 0, 0, 3, 0, 8, 15, "weapon"),
            new Item("Flute", "Bard", 5, 0, 0, 0, 0, 4, 0, 13, 25, "weapon"),
            new Item("Violin", "Bard", 8, 0, 0, 0, 0, 5, 0, 25, 50, "weapon"),
            new Item("Trumpet", "Bard", 10, 0, 0, 0, 0, 6, 0, 50, 100, "weapon"),
            new Item("Drum", "Bard", 15, 0, 0, 0, 0, 7, 0, 100, 200, "weapon"),
      };
      Item[] bows = {
            // Archer items
            new Item("Shortbow", "Archer", 1, 0, 0, 0, 2, 0, 0, 5, 5, "weapon"),
            new Item("Crossbow", "Archer", 3, 0, 0, 0, 2, 0, 0, 8, 15, "weapon"),
            new Item("Longbow", "Archer", 5, 0, 0, 0, 2, 0, 0, 13, 25, "weapon"),
            new Item("Compound Bow", "Archer", 8, 0, 0, 0, 2, 0, 0, 25, 50, "weapon"),
            new Item("Recurve Bow", "Archer", 10, 0, 0, 0, 2, 0, 0, 50, 100, "weapon"),
            new Item("Yumi", "Archer", 15, 0, 0, 0, 2, 0, 0, 100, 200, "weapon"),
      };
      Item[] spellbooks = {
            // Mage items
            new Item("Spellbook", "Mage", 1, 0, 0, 2, 0, 0, 0, 5, 5, "weapon"),
            new Item("Spellbook of Fire", "Mage", 3, 0, 0, 2, 0, 0, 0, 8, 15, "weapon"),
            new Item("Spellbook of Ice", "Mage", 5, 0, 0, 2, 0, 0, 0, 13, 25, "weapon"),
            new Item("Spellbook of Lightning", "Mage", 8, 0, 0, 2, 0, 0, 0, 25, 50, "weapon"),
            new Item("Spellbook of Explosion", "Mage", 10, 0, 0, 2, 0, 0, 0, 50, 100, "weapon"),
            new Item("Spellbook of Death", "Mage", 15, 0, 0, 2, 0, 0, 0, 100, 200, "weapon"),
      };
      Item[] offhanded = {
            new Item("Dagger", "Archer|Bard", 1, 0, 0, 0, 1, 0, 0, 5, 5, "weapon"),
            new Item("Short Sword", "Archer|Bard", 2, 0, 0, 0, 2, 0, 0, 8, 15, "weapon"),
            new Item("Rapier", "Archer|Bard", 4, 0, 0, 0, 3, 0, 0, 13, 25, "weapon"),
            new Item("Saber", "Archer|Bard", 9, 0, 0, 0, 4, 0, 0, 25, 50, "weapon"),
            new Item("Scimitar", "Archer|Bard", 13, 0, 0, 0, 5, 0, 0, 50, 100, "weapon"),
            new Item("Katana", "Archer|Bard", 17, 0, 0, 0, 6, 0, 0, 100, 200, "weapon"),
            new Item("Wakizashi", "Archer|Bard", 25, 0, 0, 0, 7, 0, 0, 200, 400, "weapon"),
      };

      Item[] heavyArmor = {
            // Tank armor
            new Item("Leather Armor", "Tank", 1, 0, 2, 0, 0, 0, 0, 5, 5, "armor"),
            new Item("Chainmail Armor", "Tank", 3, 0, 4, 0, -2, 0, 0, 8, 15, "armor"),
            new Item("Plate Armor", "Tank", 6, 0, 7, 0, 0, -3, 0, 13, 25, "armor"),
            new Item("Mithril Armor", "Tank", 8, 0, 8, 0, 0, -1, 0, 25, 50, "armor"),
            new Item("Adamantite Armor", "Tank", 10, 0, 13, 0, 0, -4, 0, 50, 100, "armor"),
            new Item("Dragon Scale Armor", "Tank", 15, 0, 17, 0, 0, -5, 0, 100, 200, "armor"),
            new Item("Titanium Armor", "Tank", 20, 0, 25, 0, 0, -6, 0, 200, 400, "armor"),
      };
      Item[] lightArmor = {
            new Item("Light Armor", "Warrior|Archer|Bard", 4, 1, 2, 0, 1, 0, 0, 15, 30, "armor"),
            new Item("Padded Clothes", "Warrior|Archer|Bard", 8, 2, 3, 0, 2, 0, 0, 30, 60, "armor"),
            new Item("Studded Leather", "Warrior|Archer|Bard", 12, 3, 4, 0, 3, 0, 0, 60, 120, "armor"),
            new Item("Gambeson", "Warrior|Archer|Bard", 16, 4, 5, 0, 4, 0, 0, 120, 240, "armor"),
            new Item("Brigandine", "Warrior|Archer|Bard", 20, 5, 6, 0, 5, 0, 0, 240, 480, "armor"),
            new Item("Mail Hauberk", "Warrior|Archer|Bard", 24, 6, 7, 0, 6, 0, 0, 480, 960, "armor"),
      };
      Item[] robes = {
            new Item("Cloth Robes", "Mage|Healer", 1, 0, 0, 1, 0, 0, 0, 5, 5, "armor"),
            new Item("Mystic Robes", "Mage|Healer", 2, 0, 0, 2, 0, 0, 3, 12, 25, "armor"),
            new Item("Arcane Robes", "Mage|Healer", 4, 0, 0, 4, 0, 0, 5, 25, 50, "armor"),
            new Item("Enchanted Robes", "Mage|Healer", 9, 0, 0, 5, 0, 8, 0, 50, 100, "armor"),
            new Item("Divine Robes", "Mage|Healer", 13, 0, 0, 7, 0, 0, 10, 100, 200, "armor"),
            new Item("Celestial Robes", "Mage|Healer", 17, 0, 0, 8, 0, 0, 13, 200, 400, "armor"),
            new Item("Astral Robes", "Mage|Healer", 25, 0, 6, 10, 0, 0, 20, 400, 800, "armor"),
      };
      Item[] shields = {
            new Item("Buckler", "Tank|Warrior", 1, 0, 1, 0, 0, 0, 0, 5, 5, "shield"),
            new Item("Kite Shield", "Tank|Warrior", 2, 0, 2, 0, 0, 0, 0, 8, 15, "shield"),
            new Item("Tower Shield", "Tank|Warrior", 6, 0, 3, 0, 0, 0, 0, 13, 25, "shield"),
            new Item("Pavise", "Tank|Warrior", 9, 0, 4, 0, 0, 0, 0, 25, 50, "shield"),
            new Item("Heater Shield", "Tank|Warrior", 12, 0, 5, 0, 0, 0, 0, 50, 100, "shield"),
            new Item("Scutum", "Tank|Warrior", 17, 0, 6, 0, 0, 0, 0, 100, 200, "shield"),
            new Item("Aegis", "Tank|Warrior", 25, 0, 7, 0, 0, 0, 0, 200, 400, "shield"),
      };
      Item[] generalItems = {
            // Rings and accessories
            new Item("Ring of Strength", "Any", 1, 5, 0, 0, 0, 0, 0, 10, 50, "accessory"),
            new Item("Ring of Vitality", "Any", 1, 0, 5, 0, 0, 0, 0, 10, 50, "accessory"),
            new Item("Ring of Intelligence", "Any", 1, 0, 0, 5, 0, 0, 0, 10, 50, "accessory"),
            new Item("Ring of Agility", "Any", 1, 0, 0, 0, 5, 0, 0, 10, 50, "accessory"),
            new Item("Ring of Charisma", "Any", 1, 0, 0, 0, 0, 5, 0, 10, 50, "accessory"),
            new Item("Talisman of Health", "Any", 1, 0, 0, 0, 0, 0, 50, 10, 50, "accessory"),
            new Item("Talisman of Power", "Any", 1, 5, 5, 5, 5, 5, 0, 25, 100, "accessory"),
            new Item("Holy Symbol", "Healer|Bard", 1, 0, 0, 0, 0, 1, 0, 5, 5, "accessory"),
            new Item("Illusory Mark", "Healer|Bard", 2, 0, 0, 0, 0, 2, 0, 8, 15, "accessory"),
            new Item("Top Hat", "Healer|Bard", 4, 0, 0, 0, 0, 3, 0, 13, 25, "accessory"),
            new Item("Crown of Healing", "Healer|Bard", 9, 0, 0, 0, 0, 4, 0, 25, 50, "accessory"),
            new Item("Charismatic Crown", "Healer|Bard", 13, 0, 0, 0, 0, 5, 0, 50, 100, "accessory"),
            new Item("Necklace of the Silver Tongue", "Healer|Bard", 17, 0, 0, 0, 0, 6, 0, 100, 200, "accessory"),
            new Item("Cloak of Charisma", "Healer|Bard", 25, 0, 0, 0, 0, 7, 0, 200, 400, "accessory"),
            // Add more general items...
      };

      Consumable[] consumables = {
            // Name, Effect, Value, Quantity, Buy Price, Sell Price, Description
            new Consumable("Health Potion", "heal_hp", 50, 1, 25, 12, "Restores 50 HP"),
            new Consumable("Greater Health Potion", "heal_hp", 100, 1, 60, 30, "Restores 100 HP"),
            new Consumable("Mana Potion", "restore_mana", 30, 1, 20, 10, "Restores 30 Mana"),
            new Consumable("Stamina Potion", "restore_stamina", 40, 1, 15, 7, "Restores 40 Stamina"),
            new Consumable("Full Restore Elixir", "full_restore", 0, 1, 200, 100,
                  "Fully restores HP, Mana, and Stamina"),
            new Consumable("Antidote", "cure_status", 0, 1, 30, 15, "Cures all negative status effects"),
            new Consumable("Throwing Knife", "throwable", 20, 1, 15, 5, "Single-use throwing weapon (25 damage)"),
            new Consumable("Bomb", "throwable", 60, 1, 75, 25, "Single-use explosive (60 damage to all enemies)"),
            new Consumable("Smoke Bomb", "escape", 0, 1, 30, 15, "Allows escape from any battle"),
      };

      // Create individual weapon shops
      WeaponShop swordShop = new WeaponShop(swords);
      WeaponShop healerShop = new WeaponShop(healers);
      WeaponShop instrumentShop = new WeaponShop(instruments);
      WeaponShop bowShop = new WeaponShop(bows);
      WeaponShop spellbookShop = new WeaponShop(spellbooks);
      WeaponShop offhandWeaponShop = new WeaponShop(offhanded);

      // Create individual armor shops
      ArmorShop heavyArmorShop = new ArmorShop(heavyArmor);
      ArmorShop lightArmorShop = new ArmorShop(lightArmor);
      ArmorShop robeShop = new ArmorShop(robes);
      ArmorShop shieldShop = new ArmorShop(shields);

      // Create other shops
      GeneralShop generalShop = new GeneralShop(generalItems);
      ConsumablesShop consumablesShop = new ConsumablesShop(consumables);

      // Create shop hub with organized categories
      ShopHub shopHub = new ShopHub(swordShop, healerShop, instrumentShop, bowShop, spellbookShop, offhandWeaponShop,
            heavyArmorShop, lightArmorShop, robeShop, shieldShop, generalShop, consumablesShop);

      // =================== QUESTS ===================
      Quest[] quests = {
            // Quest Name, Enemy Name, Quantity, Gold Reward, Exp Reward
            new Quest("Getting started", "Rat", 3, 5, 10),
            new Quest("Exterminator", "Rat", 10, 50, 50),
            new Quest("Slime Slayer", "Slime", 10, 100, 100),
            new Quest("Goblin Lair", "Goblin", 20, 150, 200),
            new Quest("Kobold Killer", "Kobold", 10, 175, 200),
            new Quest("Skeleton Dungeon", "Skeleton", 15, 200, 250),
            new Quest("Orc Outpost", "Orc", 10, 300, 300),
            new Quest("Troll Terminator", "Troll", 10, 350, 350),
            new Quest("Minotaur Mutilator", "Minotaur", 10, 400, 400),
            new Quest("Giant Grinder", "Giant", 10, 450, 450),
            new Quest("Dragon Destroyer", "Dragon", 10, 500, 500),
            new Quest("Kraken Killer", "Kraken", 10, 550, 550),
            new Quest("Leviathan Lacerator", "Leviathan", 10, 600, 600),
      };

      QuestBot questBot = new QuestBot(quests);
      // --- SAFE GAME LOOP ---
      boolean playing = true;

      try {
         while (playing && player.isAlive()) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. View Stats");
            System.out.println("2. View Inventory");
            System.out.println("3. Equipment Management");
            System.out.println("4. Fight");
            System.out.println("5. Visit Shop");
            System.out.println("6. Quest Bot");
            System.out.println("7. Dungeon Bot");
            System.out.println("8. Exit");

            int action = InputValidator.getIntInput(scanner, "Choose an option (1-8): ", 1, 8);

            switch (action) {
               case 1:
                  player.displayStats();
                  break;
               case 2:
                  player.displayInventory();
                  break;
               case 3:
                  // Equipment Management
                  boolean managing = true;
                  while (managing) {
                     System.out.println("\n--- Equipment Management ---");
                     player.displayInventory();
                     System.out.println("\n1. Equip Item");
                     System.out.println("2. Unequip Item");
                     System.out.println("3. Back to Main Menu");

                     int equipAction = InputValidator.getIntInput(scanner, "Choose action (1-3): ", 1, 3);

                     switch (equipAction) {
                        case 1:
                           if (player.inventory.isEmpty()) {
                              System.out.println("No items to equip!");
                              break;
                           }
                           int equipChoice = InputValidator.getIntInput(scanner,
                                 "Choose item to equip (1-" + player.inventory.size() + "): ", 1, player.inventory.size());
                           Item toEquip = player.inventory.get(equipChoice - 1);
                           player.equipItem(toEquip);
                           break;
                        case 2:
                           System.out.println("Choose equipment slot to unequip:");
                           System.out.println("1. Main Weapon"
                                 + (player.mainWeapon != null ? " (" + player.mainWeapon.itemName + ")" : " (Empty)"));
                           System.out.println(
                                 "2. Armor" + (player.armor != null ? " (" + player.armor.itemName + ")" : " (Empty)"));
                           System.out.println(
                                 "3. Shield" + (player.shield != null ? " (" + player.shield.itemName + ")" : " (Empty)"));
                           System.out.println("4. Accessory 1"
                                 + (player.accessory1 != null ? " (" + player.accessory1.itemName + ")" : " (Empty)"));
                           System.out.println("5. Accessory 2"
                                 + (player.accessory2 != null ? " (" + player.accessory2.itemName + ")" : " (Empty)"));
                           System.out.println("6. Cancel");

                           int unequipChoice = InputValidator.getIntInput(scanner, "Choose slot (1-6): ", 1, 6);
                           switch (unequipChoice) {
                              case 1:
                                 if (player.mainWeapon != null)
                                    player.unequipItem(player.mainWeapon);
                                 else
                                    System.out.println("No weapon equipped!");
                                 break;
                              case 2:
                                 if (player.armor != null)
                                    player.unequipItem(player.armor);
                                 else
                                    System.out.println("No armor equipped!");
                                 break;
                              case 3:
                                 if (player.shield != null)
                                    player.unequipItem(player.shield);
                                 else
                                    System.out.println("No shield equipped!");
                                 break;
                              case 4:
                                 if (player.accessory1 != null)
                                    player.unequipItem(player.accessory1);
                                 else
                                    System.out.println("No accessory in slot 1!");
                                 break;
                              case 5:
                                 if (player.accessory2 != null)
                                    player.unequipItem(player.accessory2);
                                 else
                                    System.out.println("No accessory in slot 2!");
                                 break;
                           }
                           break;
                        case 3:
                           managing = false;
                           break;
                     }
                  }
                  break;
               case 4: {
                  System.out.println("\nChoose an enemy to fight:");
                  for (int i = 0; i < enemies.length; i++) {
                     System.out.println((i + 1) + ". " + enemies[i].name);
                  }

                  int enemyChoice = InputValidator.getIntInput(scanner,
                        "Choose enemy (1-" + enemies.length + "): ", 1, enemies.length) - 1;

                  Enemy chosen = enemies[enemyChoice];

                  // Check for elite mob spawn
                  EliteMobSpawner eliteSpawner = new EliteMobSpawner(0);
                  boolean isElite = eliteSpawner.isEliteMob();

                  Enemy enemy;
                  if (isElite) {
                     // Create elite version with boosted stats
                     enemy = new Enemy("Elite " + chosen.name, 
                           eliteSpawner.getEliteStrength(chosen.strength), 
                           eliteSpawner.getEliteVitality(chosen.vitality),
                           eliteSpawner.getEliteAgility(chosen.agility), 
                           eliteSpawner.getEliteExpReward(chosen.expReward), 
                           eliteSpawner.getEliteGoldReward(chosen.goldReward));
                     System.out.println("\n⭐ AN ELITE ENEMY HAS APPEARED! ⭐");
                  } else {
                     enemy = new Enemy(chosen.name, chosen.strength, chosen.vitality,
                           chosen.agility, chosen.expReward, chosen.goldReward);
                  }

                  System.out.println("\nBattle Start: " + player.name + " vs " + enemy.name);
                  boolean isDefending = false;
                  boolean battleEscaped = false;

                  // Determine turn order based on agility
                  boolean playerGoesFirst = player.agility >= enemy.agility;
                  int playerTurns = 0;
                  int enemyTurns = 0;

                  // Calculate extra turns based on agility difference
                  double agilityDiff = Math.abs(player.agility - enemy.agility);
                  if (agilityDiff >= 20) {
                     if (player.agility > enemy.agility) {
                        playerTurns = 1;
                     } else {
                        enemyTurns = 1;
                     }
                  }

                  while (player.isAlive() && enemy.isAlive() && !battleEscaped) {
                     // Process status effects at start of turn
                     player.processStatusEffects();
                     enemy.processStatusEffects();

                     if (!enemy.isAlive()) {
                        System.out.println("The " + enemy.name + " succumbs to status effects!");
                        break;
                     }

                     // Agility-based turn order
                     if (playerGoesFirst) {
                        // Player turn(s)
                        for (int i = 0; i <= playerTurns && player.isAlive() && enemy.isAlive() && !battleEscaped; i++) {
                           if (!player.isStunned()) {
                              boolean turnTaken = false;
                              while (!turnTaken) { // Keep looping until player takes a valid action
                                 System.out.println("\n--- Your Turn " + (i > 0 ? "(Extra)" : "") + " ---");
                                 player.displayStats();
                                 enemy.displayStats();

                                 System.out.println("\nChoose your action:");
                                 System.out
                                       .println(
                                             "1. Attack (" + String.format("%.1f", player.getAttackDamage()) + " damage)");
                                 System.out.println("2. Defend (reduce incoming damage)");
                                 System.out.println("3. Use Skill/Spell");
                                 System.out.println("4. Use Consumable");
                                 if (player.charClass.equalsIgnoreCase("healer")) {
                                    System.out.println("5. Basic Heal (restore HP based on charisma)");
                                 }

                                 int maxActions = player.charClass.equalsIgnoreCase("healer") ? 5 : 4;
                                 int playerAction = InputValidator.getIntInput(scanner,
                                       "Choose action (1-" + maxActions + "): ", 1, maxActions);

                                 switch (playerAction) {
                                    case 1: // Attack
                                       double playerDmg = player.getAttackDamage();

                                       // Apply Damage Wall's damage nullification
                                       if (enemy.name.equals("Damage Wall")) {
                                          double originalDmg = playerDmg;
                                          playerDmg *= 0.2; // Nullifies 80% of damage
                                          System.out.println("Damage Wall nullifies "
                                                + String.format("%.1f", originalDmg - playerDmg) + " damage!");
                                       }

                                       enemy.takeDamage(playerDmg);
                                       System.out.println("You hit " + enemy.name + " for " +
                                             String.format("%.1f", playerDmg) + " damage!");
                                       isDefending = false;
                                       turnTaken = true;
                                       break;

                                    case 2: // Defend
                                       System.out.println("You take a defensive stance!");
                                       isDefending = true;
                                       turnTaken = true;
                                       break;

                                    case 3: // Use Skill/Spell
                                       boolean skillUsed = player.useSkillOrSpell(enemy, scanner);
                                       if (skillUsed) {
                                          isDefending = false;
                                          turnTaken = true;
                                       }
                                       break;

                                    case 4: // Use Consumable
                                       // Store consumables before use to check for escape items
                                       java.util.List<String> consumableNames = new java.util.ArrayList<>();
                                       for (Consumable c : player.consumables) {
                                          consumableNames.add(c.name);
                                       }

                                       boolean consumableUsed = consumablesShop.useConsumable(player, scanner, enemy);
                                       if (consumableUsed) {
                                          // Check if an escape consumable was used by comparing before/after
                                          boolean escapeUsed = false;
                                          for (String consumableName : consumableNames) {
                                             if (consumableName.equals("Smoke Bomb")) {
                                                // Check if the smoke bomb count decreased
                                                boolean stillHasSmokeBomd = false;
                                                for (Consumable c : player.consumables) {
                                                   if (c.name.equals("Smoke Bomb")) {
                                                      stillHasSmokeBomd = true;
                                                      break;
                                                   }
                                                }
                                                if (!stillHasSmokeBomd
                                                      || consumableNames.size() > player.consumables.size()) {
                                                   escapeUsed = true;
                                                   break;
                                                }
                                             }
                                          }

                                          if (escapeUsed) {
                                             battleEscaped = true;
                                             turnTaken = true;
                                             break; // Break out of the action selection loop
                                          }

                                          isDefending = false;
                                          turnTaken = true;
                                       }
                                       // If consumable wasn't used, don't set turnTaken to true
                                       break;

                                    case 5: // Basic Heal (only for healers)
                                       if (player.charClass.equalsIgnoreCase("healer")) {
                                          double heal = player.charisma + (player.intelligence / 2);
                                          double oldHP = player.currentHP;
                                          player.currentHP = Math.min(player.currentHP + heal, player.getMaxHP());
                                          double actualHeal = player.currentHP - oldHP;
                                          System.out.println("You heal yourself for " +
                                                String.format("%.1f", actualHeal) + " HP!");
                                          isDefending = false;
                                          turnTaken = true;
                                       }
                                       break;
                                 }
                              }
                           } else {
                              System.out.println("\nYou are stunned and cannot act this turn!");
                              isDefending = false;
                           }

                           if (battleEscaped || !enemy.isAlive())
                              break;
                        }

                        if (battleEscaped) {
                           System.out.println("You successfully escaped from the battle!");
                           break;
                        }

                        if (!enemy.isAlive()) {
                           System.out.println("You defeated the " + enemy.name + "!");
                           player.addEnemyKill(enemy.name);
                           player.checkQuestCompletion(scanner);
                           player.gainExp(enemy.expReward, scanner);
                           player.gold += enemy.goldReward;
                           player.restoreResources();
                           break;
                        }

                        // Enemy turn(s)
                        for (int i = 0; i <= enemyTurns && player.isAlive() && enemy.isAlive(); i++) {
                           if (!enemy.isStunned()) {
                              System.out.println("\n--- " + enemy.name + "'s Turn " + (i > 0 ? "(Extra)" : "") + " ---");
                              double enemyDmg = enemy.getAttackDamage(player);

                              if (isDefending) {
                                 double defense = (player.vitality / 100);
                                 if (defense >= 1)
                                    defense = 0.75; // Cap at 75% reduction
                                 enemyDmg *= (1 - defense);
                                 System.out.println("Your defensive stance reduces the damage by " +
                                       String.format("%.1f", defense * 100) + "%");
                              }

                              player.takeDamage(enemyDmg);
                              System.out.println(enemy.name + " hits you for " +
                                    String.format("%.1f", enemyDmg) + " damage!");
                           } else {
                              System.out.println("\n" + enemy.name + " is stunned and cannot attack!");
                           }
                        }
                     } else {
                        // Enemy goes first
                        // Enemy turn(s)
                        for (int i = 0; i <= enemyTurns && player.isAlive() && enemy.isAlive(); i++) {
                           if (!enemy.isStunned()) {
                              System.out.println("\n--- " + enemy.name + "'s Turn " + (i > 0 ? "(Extra)" : "") + " ---");
                              double enemyDmg = enemy.getAttackDamage(player);

                              if (isDefending) {
                                 double defense = (player.vitality / 100);
                                 if (defense >= 1)
                                    defense = 0.75; // Cap at 75% reduction
                                 enemyDmg *= (1 - defense);
                                 System.out.println("Your defensive stance reduces the damage by " +
                                       String.format("%.1f", defense * 100) + "%");
                              }

                              player.takeDamage(enemyDmg);
                              System.out.println(enemy.name + " hits you for " +
                                    String.format("%.1f", enemyDmg) + " damage!");
                           } else {
                              System.out.println("\n" + enemy.name + " is stunned and cannot attack!");
                           }
                        }

                        if (!player.isAlive())
                           break;

                        // Player turn(s)
                        for (int i = 0; i <= playerTurns && player.isAlive() && enemy.isAlive() && !battleEscaped; i++) {
                           if (!player.isStunned()) {
                              boolean turnTaken = false;
                              while (!turnTaken) {
                                 System.out.println("\n--- Your Turn " + (i > 0 ? "(Extra)" : "") + " ---");
                                 player.displayStats();
                                 enemy.displayStats();

                                 System.out.println("\nChoose your action:");
                                 System.out
                                       .println(
                                             "1. Attack (" + String.format("%.1f", player.getAttackDamage()) + " damage)");
                                 System.out.println("2. Defend (reduce incoming damage)");
                                 System.out.println("3. Use Skill/Spell");
                                 System.out.println("4. Use Consumable");
                                 if (player.charClass.equalsIgnoreCase("healer")) {
                                    System.out.println("5. Basic Heal (restore HP based on charisma)");
                                 }

                                 int maxActions = player.charClass.equalsIgnoreCase("healer") ? 5 : 4;
                                 int playerAction = InputValidator.getIntInput(scanner,
                                       "Choose action (1-" + maxActions + "): ", 1, maxActions);

                                 switch (playerAction) {
                                    case 1: // Attack
                                       double playerDmg = player.getAttackDamage();

                                       // Apply Damage Wall's damage nullification
                                       if (enemy.name.equals("Damage Wall")) {
                                          double originalDmg = playerDmg;
                                          playerDmg *= 0.2; // Nullifies 80% of damage
                                          System.out.println("Damage Wall nullifies "
                                                + String.format("%.1f", originalDmg - playerDmg) + " damage!");
                                       }

                                       enemy.takeDamage(playerDmg);
                                       System.out.println("You hit " + enemy.name + " for " +
                                             String.format("%.1f", playerDmg) + " damage!");
                                       isDefending = false;
                                       turnTaken = true;
                                       break;

                                    case 2: // Defend
                                       System.out.println("You take a defensive stance!");
                                       isDefending = true;
                                       turnTaken = true;
                                       break;

                                    case 3: // Use Skill/Spell
                                       boolean skillUsed = player.useSkillOrSpell(enemy, scanner);
                                       if (skillUsed) {
                                          isDefending = false;
                                          turnTaken = true;
                                       }
                                       break;

                                    case 4: // Use Consumable
                                       boolean consumableUsed = consumablesShop.useConsumable(player, scanner, enemy);
                                       if (consumableUsed) {
                                          isDefending = false;
                                          turnTaken = true;
                                       }
                                       break;

                                    case 5: // Basic Heal (only for healers)
                                       if (player.charClass.equalsIgnoreCase("healer")) {
                                          double heal = player.charisma + (player.intelligence / 2);
                                          double oldHP = player.currentHP;
                                          player.currentHP = Math.min(player.currentHP + heal, player.getMaxHP());
                                          double actualHeal = player.currentHP - oldHP;
                                          System.out.println("You heal yourself for " +
                                                String.format("%.1f", actualHeal) + " HP!");
                                          isDefending = false;
                                          turnTaken = true;
                                       }
                                       break;
                                 }
                              }
                           } else {
                              System.out.println("\nYou are stunned and cannot act this turn!");
                              isDefending = false;
                           }
                        }
                     }

                     // Reset defending status after turn cycle
                     isDefending = false;
                  }

                  if (!player.isAlive()) {
                     System.out.println("\nYou have been defeated. Game Over.");
                     playing = false;
                  } else if (player.charClass.equalsIgnoreCase("healer")) {
                     double heal = player.charisma / 2;
                     player.currentHP = Math.min(player.currentHP + heal, player.getMaxHP());
                     System.out.println("Your healing power restores " +
                           String.format("%.1f", heal) + " HP after battle.");
                  }
                  break;
               }
               case 5:
                  shopHub.openShopHub(player, scanner);
                  break;
               case 6:
                  System.out.println("  {'-'}");
                  System.out.println("  __|__");
                  System.out.println(" |  |  |");
                  System.out.println(" |  |  |");
                  System.out.println("   _|_ ");
                  System.out.println("  |   |");
                  System.out.println("  |   |");
                  System.out.println("  |   |");
                  questBot.openQuestBot(player, scanner);
                  break;
               case 7:
                  System.out.println("  [|+']");
                  System.out.println("  __|__");
                  System.out.println(" |  |  |");
                  System.out.println(" |  |  |");
                  System.out.println("   _|_ ");
                  System.out.println("  |   |");
                  System.out.println("  |   |");
                  System.out.println("  |   |");
                  // Dungeons
                  // Create dungeon array
                  Dungeon[] dungeons = {
                        // name, theme, length, difficulty, goldReward, expReward, possibleEnemies,
                        // bossEnemy
                        new Dungeon("Survival Training", "Test", 100, 1, 10, 10,
                              new String[] { "Training Dummy", "Rat", "Slime", "Goblin", "Kobold", "Skeleton", "Zombie",
                                    "Orc", "Troll", "Giant", "Minotaur", "Dragon", "Kraken", "Leviathan", "Behemoth",
                                    "Archdemon", "Glass Cannon", "Demon King", "Rat King", "Goblin Chief", "Lich",
                                    "Orc Warlord", "Spider Queen", "Arcane Guardian", "Ancient Dragon", "Demon God" },
                              "Player"),
                        new Dungeon("Rat Infested Sewers", "rat", 5, 2, 100, 75,
                              new String[] { "Rat", "Slime" }, "Rat King"),
                        new Dungeon("Goblin Hideout", "goblin", 6, 3, 150, 100,
                              new String[] { "Goblin", "Kobold" }, "Goblin Chief"),
                        new Dungeon("Abandoned Crypt", "undead", 7, 4, 200, 150,
                              new String[] { "Skeleton", "Zombie" }, "Lich"),
                        new Dungeon("Orc Stronghold", "orc", 8, 5, 300, 200,
                              new String[] { "Orc", "Troll" }, "Orc Warlord"),
                        new Dungeon("Spider's Den", "spider", 6, 4, 250, 175,
                              new String[] { "Giant Spider", "Web Spinner" }, "Spider Queen"),
                        new Dungeon("Crystal Caverns", "elemental", 9, 6, 400, 250,
                              new String[] { "Earth Elemental", "Crystal Golem" }, "Arcane Guardian"),
                        new Dungeon("Dragon's Lair", "dragon", 10, 8, 1000, 500,
                              new String[] { "Drake", "Wyvern", "Dragon" }, "Ancient Dragon"),
                        new Dungeon("Abyssal Depths", "demon", 12, 10, 2000, 1000,
                              new String[] { "Demon", "Archdemon", "Behemoth" }, "Demon God")
                  };

                  // Hidden dungeons array for secret access
                  Dungeon[] hiddenDungeons = {
                        new Dungeon("Void", "testing", 8, 2147483647, 0, 0,
                              new String[] { "Dummy Bag", "Glass Cannon", "Training Dummy", "Agility Monster",
                                    "Damage Wall", "Crit Beast", "Player" },
                              "Zmanpoke")
                  };

                  // Check for secret access to void dungeon
                  if (player.name.toLowerCase().equals("void") || player.charClass.equals("Developer")) {
                        // Combine regular and hidden dungeons
                        Dungeon[] allDungeons = new Dungeon[dungeons.length + hiddenDungeons.length];
                        System.arraycopy(dungeons, 0, allDungeons, 0, dungeons.length);
                        System.arraycopy(hiddenDungeons, 0, allDungeons, dungeons.length, hiddenDungeons.length);
                        dungeons = allDungeons;
                  }

                  // Add some boss enemies to your enemies array if they don't exist:
                  Enemy[] bossEnemies = {
                        // name, stregnth, vitality, Agility, gold, exp
                        new Enemy("Rat King", 4.0, 10.0, 6.0, 50, 75),
                        new Enemy("Goblin Chief", 15.0, 18.0, 8.0, 75, 100),
                        new Enemy("Lich", 25.0, 20.0, 15.0, 100, 150),
                        new Enemy("Orc Warlord", 30.0, 25.0, 10.0, 150, 200),
                        new Enemy("Spider Queen", 20.0, 30.0, 25.0, 125, 175),
                        new Enemy("Arcane Guardian", 35.0, 40.0, 20.0, 200, 250),
                        new Enemy("Ancient Dragon", 80.0, 150.0, 40.0, 400, 500),
                        new Enemy("Demon God", 120.0, 200.0, 60.0, 800, 1000),

                        new Enemy("Player", player.strength, player.vitality, player.agility, 2000, 1500),

                        new Enemy("Zmanpoke", 100000.0, 100000.0, 100000.0, 1000000, 100000),

                        // Testing enemies for void dungeon
                        // name, stregnth, vitality, Agility, gold, exp
                        new Enemy("Training Dummy", -2.0, 1.0, 0.0, 0, 0),
                        new Enemy("Agility Monster", 5.0, 10.0, 200.0, 0, 0),
                        new Enemy("Damage Wall", 1.0, 1000.0, 1.0, 0, 0),
                        new Enemy("Crit Beast", 15.0, 15.0, 100.0, 50, 40),
                  };

                  // Combine original enemies with boss enemies
                  Enemy[] allEnemies = new Enemy[enemies.length + bossEnemies.length];
                  System.arraycopy(enemies, 0, allEnemies, 0, enemies.length);
                  System.arraycopy(bossEnemies, 0, allEnemies, enemies.length, bossEnemies.length);

                  DungeonBot dungeonBot = new DungeonBot(dungeons);
                  dungeonBot.openDungeonBot(player, scanner, allEnemies, generalItems, consumables, consumablesShop);
                  break;

               case 8:
                  playing = false;
                  break;
            }
         }
      } catch (Exception e) {
         System.err.println("\n========================================");
         System.err.println("An unexpected error occurred!");
         System.err.println("Error: " + e.getMessage());
         System.err.println("========================================");
         System.err.println("\nThe game will now exit. Your progress may not have been saved.");
         e.printStackTrace();
      } finally {
         System.out.println("\nThanks for playing!");
         scanner.close();
      }
   }
}