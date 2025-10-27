// dungeon stats
class Dungeon {
   String name;
   String theme; // "rat", "goblin", "undead", etc.
   int length; // number of encounters
   int difficulty; // 1-10 scale
   int goldReward;
   int expReward;
   String[] possibleEnemies; // enemies that can appear in this dungeon
   String bossEnemy; // final boss name

   public Dungeon(String name, String theme, int length, int difficulty,
         int goldReward, int expReward, String[] possibleEnemies, String bossEnemy) {
      this.name = name;
      this.theme = theme;
      this.length = length;
      this.difficulty = difficulty;
      this.goldReward = goldReward;
      this.expReward = expReward;
      this.possibleEnemies = possibleEnemies;
      this.bossEnemy = bossEnemy;
   }

   public void displayDungeon() {
      System.out.println("=== " + name + " ===");
      System.out.println("Theme: " + theme);
      System.out.println("Length: " + length + " encounters");
      System.out.println("Difficulty: " + getDifficultyText());
      System.out.println("Rewards: " + goldReward + " gold, " + expReward + " exp");
      System.out.println();
   }

   private String getDifficultyText() {
      if (difficulty <= 2)
         return "Easy";
      else if (difficulty <= 4)
         return "Normal";
      else if (difficulty <= 6)
         return "Hard";
      else if (difficulty <= 8)
         return "Very Hard";
      else
         return "Extreme";
   }
}