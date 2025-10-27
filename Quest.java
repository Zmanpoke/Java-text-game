// =================== QUEST Stats ===================
class Quest {
   String questName;
   String enemyName;
   int quantity;
   int goldReward;
   int expReward;

   public Quest(String questName, String enemyName, int quantity, int goldReward, int expReward) {
      this.questName = questName;
      this.enemyName = enemyName;
      this.quantity = quantity;
      this.goldReward = goldReward;
      this.expReward = expReward;
   }

   public void displayQuest() {
      System.out.println(questName + " (Kill " + quantity + " " + enemyName + ")");
      System.out.println("  Gold Reward: " + goldReward + "g | Exp Reward: " + expReward + "\n");
   }
}