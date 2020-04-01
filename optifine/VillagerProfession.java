package optifine;

public class VillagerProfession {
  private int profession;
  
  private int[] careers;
  
  public VillagerProfession(int p_i100_1_) {
    this(p_i100_1_, (int[])null);
  }
  
  public VillagerProfession(int p_i101_1_, int p_i101_2_) {
    this(p_i101_1_, new int[] { p_i101_2_ });
  }
  
  public VillagerProfession(int p_i102_1_, int[] p_i102_2_) {
    this.profession = p_i102_1_;
    this.careers = p_i102_2_;
  }
  
  public boolean matches(int p_matches_1_, int p_matches_2_) {
    if (this.profession != p_matches_1_)
      return false; 
    return !(this.careers != null && !Config.equalsOne(p_matches_2_, this.careers));
  }
  
  private boolean hasCareer(int p_hasCareer_1_) {
    return (this.careers == null) ? false : Config.equalsOne(p_hasCareer_1_, this.careers);
  }
  
  public boolean addCareer(int p_addCareer_1_) {
    if (this.careers == null) {
      this.careers = new int[] { p_addCareer_1_ };
      return true;
    } 
    if (hasCareer(p_addCareer_1_))
      return false; 
    this.careers = Config.addIntToArray(this.careers, p_addCareer_1_);
    return true;
  }
  
  public int getProfession() {
    return this.profession;
  }
  
  public int[] getCareers() {
    return this.careers;
  }
  
  public String toString() {
    return (this.careers == null) ? this.profession : (this.profession + ":" + Config.arrayToString(this.careers));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\VillagerProfession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */