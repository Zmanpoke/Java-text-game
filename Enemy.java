import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

// =================== ENEMY ===================
class Enemy {
   String name;
   double strength, vitality, agility;
   double currentHP;
   int expReward;
   double goldReward;
   List<StatusEffect> statusEffects = new ArrayList<>();

   public Enemy(String name, double strength, double vitality, double agility, int expReward,
         double goldReward) {
      this.name = name;
      this.strength = strength;
      this.vitality = vitality;
      this.agility = agility;
      this.expReward = expReward;
      this.currentHP = getMaxHP();
      this.goldReward = goldReward;
   }

   public double getMaxHP() {
      return vitality * 2 + 10;
   }

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
            System.out.println(effect.name + " effect has worn off " + name + "!");
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

   public double getAttackDamage(Player player) {
      double damage = 2 + strength;

      // Check for weakened status
      for (StatusEffect effect : statusEffects) {
         if (effect.name.equals("Weakened")) {
            damage *= 0.7; // Reduce damage by 30%
            break;
         }
      }

      // Enhanced crit mechanics for agility-based enemies
      Random random = new Random();
      double critChance = Math.min(agility * 1.5, 75); // Up to 75% crit chance
      double roll = random.nextDouble() * 100;

      if (roll < critChance && agility >= 50) {
         double critMultiplier = 1.8 + (agility / 200);
         System.out.println(name + " CRITICAL HIT! (+" + String.format("%.0f", (critMultiplier - 1) * 100)
               + "% damage)");
         damage *= critMultiplier;
      }

      // Damage Wall special ability - nullify percentage of damage
      if (name.equals("Damage Wall")) {
         double nullifyPercent = 0.8; // Nullifies 80% of incoming damage
         System.out.println("Damage Wall's defensive aura reduces incoming damage!");
      }

      if (player.charClass.equalsIgnoreCase("bard")) {
         double reduction = player.charisma * 0.25;
         damage -= reduction;
         if (damage < 0)
            damage = 0;
         System.out.println("Bard's music weakens the enemy! Enemy damage reduced by "
               + reduction);
      }
      return damage;
   }

   public void takeDamage(double dmg) {
      currentHP -= dmg;
      if (currentHP < 0)
         currentHP = 0;
   }

   public boolean isAlive() {
      return currentHP > 0;
   }

   public void displayStats() {
      System.out.println("\n--- Enemy Stats ---");
      System.out.println("Enemy: " + name);
      System.out.println("HP: " + String.format("%.1f", currentHP) + "/"
            + String.format("%.1f", getMaxHP()));
      System.out.println("STR: " + strength + " | VIT: " + vitality + " | AGI: " + agility);

      if (!statusEffects.isEmpty()) {
         System.out.print("Status Effects: ");
         for (StatusEffect effect : statusEffects) {
            System.out.print(effect.name + "(" + effect.duration + ") ");
         }
         System.out.println();
      }
   }
}