import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

//Elitle Mob Spawning
class EliteMobSpawner{
   double eliteMultiplier;
   double eliteStrength;
   double eliteVitality;
   double eliteAgility;
   int eliteExpReward;
   double eliteGoldReward;

   //stat multiplier for elite mobs
   public EliteMobSpawner(double eliteMultiplier){
      double rawMultiplier = new Random().nextDouble() * 1.9 + 1.1; // Range from 1.1 to 3.0
      this.eliteMultiplier = Math.round(rawMultiplier * 10.0) / 10.0; // Round to tenths place
   }
   double getEliteStrength(double strength){
      return strength * eliteMultiplier;
   }
   double getEliteVitality(double vitality){
      return vitality * eliteMultiplier;
   }
   double getEliteAgility(double agility){
      return agility * eliteMultiplier;
   }
   int getEliteExpReward(int expReward){
      return (int) (expReward * eliteMultiplier);
   }
   double getEliteGoldReward(double goldReward){
      return goldReward * eliteMultiplier;
   }
   //elite mob spawn check
   public boolean isEliteMob(){
      double spawnRate = new Random().nextDouble() * 100;
      return spawnRate < 1; // 1% chance to spawn an elite mob
   }
}