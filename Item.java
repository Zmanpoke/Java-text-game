// =================== ITEM ===================
class Item {
   String itemName;
   String reqClass;
   int reqlvl;
   double strBonus, vitBonus, intBonus, agiBonus, charBonus;
   double hpBonus;
   int buyPrice, sellPrice;
   String slotType; // "weapon", "armor", "shield", "accessory"

   public Item(String itemName, String reqClass, int reqlvl, double strBonus, double vitBonus,
         double intBonus, double agiBonus, double charBonus, double hpBonus,
         int sellPrice, int buyPrice, String slotType) {
      this.itemName = itemName;
      this.reqClass = reqClass;
      this.reqlvl = reqlvl;
      this.strBonus = strBonus;
      this.vitBonus = vitBonus;
      this.intBonus = intBonus;
      this.agiBonus = agiBonus;
      this.charBonus = charBonus;
      this.hpBonus = hpBonus;
      this.sellPrice = sellPrice;
      this.buyPrice = buyPrice;
      this.slotType = slotType;
   }

   public void displayItem() {
      System.out.println(itemName + " (Lvl " + reqlvl + " " + reqClass + ")");
      System.out.println("  STR+" + strBonus + " VIT+" + vitBonus + " INT+" + intBonus + " AGI+"
            + agiBonus + " CHA+"
            + charBonus + " HP+" + hpBonus);
      System.out.println("  Buy: " + buyPrice + "g | Sell: " + sellPrice + "\n");
   }
}