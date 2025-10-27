// =================== STATUS EFFECT ===================
class StatusEffect {
   String name;
   int duration;
   double damagePerTurn;
   boolean preventsAction;

   public StatusEffect(String name, int duration, double damagePerTurn, boolean preventsAction) {
      this.name = name;
      this.duration = duration;
      this.damagePerTurn = damagePerTurn;
      this.preventsAction = preventsAction;
   }
}