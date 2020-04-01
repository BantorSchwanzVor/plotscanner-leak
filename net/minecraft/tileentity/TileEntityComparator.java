package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityComparator extends TileEntity {
  private int outputSignal;
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setInteger("OutputSignal", this.outputSignal);
    return compound;
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.outputSignal = compound.getInteger("OutputSignal");
  }
  
  public int getOutputSignal() {
    return this.outputSignal;
  }
  
  public void setOutputSignal(int outputSignalIn) {
    this.outputSignal = outputSignalIn;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */