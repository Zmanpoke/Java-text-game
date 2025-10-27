import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// =================== QUEST BOT ===================
class QuestBot {
   Quest[] quests;

   public QuestBot(Quest[] quests) {
      this.quests = quests;
   }

   public void openQuestBot(Player player, Scanner scanner) {
      boolean questing = true;
      while (questing) {
         System.out.println("\n--- Quest Menu ---");
         System.out.println("1. View Available Quests");
         System.out.println("2. Check Active Quests");
         System.out.println("3. Exit Quest Menu");
         int choice = InputValidator.getIntInput(scanner, "Choose option (1-3): ", 1, 3);

         switch (choice) {
            case 1:
               viewAvailableQuests(player, scanner);
               break;
            case 2:
               checkActiveQuests(player);
               break;
            case 3:
               questing = false;
               break;
            default:
               System.out.println("Invalid choice.");
               break;

         }
      }
   }

   private void viewAvailableQuests(Player player, Scanner scanner) {
      System.out.println("\n--- Available Quests ---");

      // Show quests that aren't already active
      boolean hasAvailable = false;
      for (int i = 0; i < quests.length; i++) {
         Quest quest = quests[i];
         boolean isActive = false;

         for (Quest activeQuest : player.activeQuests) {
            if (activeQuest.questName.equals(quest.questName)) {
               isActive = true;
               break;
            }
         }

         if (!isActive) {
            System.out.print((i + 1) + ". ");
            quest.displayQuest();
            hasAvailable = true;
         }
      }

      if (!hasAvailable) {
         System.out.println("No new quests available!");
         return;
      }

      System.out.println("Enter quest number to accept (0 to cancel): ");
      int choice = InputValidator.getIntInput(scanner, "Enter quest number (0-" + quests.length + "): ", 0,
            quests.length);

      if (choice > 0 && choice <= quests.length) {
         Quest selectedQuest = quests[choice - 1];

         // Check if already active
         boolean isAlreadyActive = false;
         for (Quest activeQuest : player.activeQuests) {
            if (activeQuest.questName.equals(selectedQuest.questName)) {
               isAlreadyActive = true;
               break;
            }
         }

         if (!isAlreadyActive) {
            player.activeQuests.add(selectedQuest);
            System.out.println("Quest accepted: " + selectedQuest.questName);
         } else {
            System.out.println("You already have this quest!");
         }
      }
   }

   private void checkActiveQuests(Player player) {
      System.out.println("\n--- Active Quests ---");
      if (player.activeQuests.isEmpty()) {
         System.out.println("You have no active quests.");
         return;
      }

      for (int i = 0; i < player.activeQuests.size(); i++) {
         Quest quest = player.activeQuests.get(i);
         int currentKills = player.getEnemyKillCount(quest.enemyName);
         System.out.println((i + 1) + ". " + quest.questName);
         System.out.println("   Progress: " + currentKills + "/" + quest.quantity + " " + quest.enemyName + " killed");
         System.out.println("   Rewards: " + quest.goldReward + " gold, " + quest.expReward + " exp");
         System.out.println();
      }
   }
}