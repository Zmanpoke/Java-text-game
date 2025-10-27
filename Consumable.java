// =================== CONSUMABLE ===================
class Consumable {
   String name;
   String effect;
   double value;
   int quantity;
   int buyPrice;
   int sellPrice;
   String description;

   public Consumable(String name, String effect, double value, int quantity, int buyPrice, int sellPrice,
         String description) {
      this.name = name;
      this.effect = effect;
      this.value = value;
      this.quantity = quantity;
      this.buyPrice = buyPrice;
      this.sellPrice = sellPrice;
      this.description = description;
   }

   public void displayConsumable() {
      System.out.println(name + " (x" + quantity + ")");
      System.out.println("  " + description);
      System.out.println("  Buy: " + buyPrice + "g | Sell: " + sellPrice + "g\n");
   }
}