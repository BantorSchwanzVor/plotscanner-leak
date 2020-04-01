package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.server.MinecraftServer;

public class ServerScoreboard extends Scoreboard {
  private final MinecraftServer scoreboardMCServer;
  
  private final Set<ScoreObjective> addedObjectives = Sets.newHashSet();
  
  private Runnable[] dirtyRunnables = new Runnable[0];
  
  public ServerScoreboard(MinecraftServer mcServer) {
    this.scoreboardMCServer = mcServer;
  }
  
  public void onScoreUpdated(Score scoreIn) {
    super.onScoreUpdated(scoreIn);
    if (this.addedObjectives.contains(scoreIn.getObjective()))
      this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketUpdateScore(scoreIn)); 
    markSaveDataDirty();
  }
  
  public void broadcastScoreUpdate(String scoreName) {
    super.broadcastScoreUpdate(scoreName);
    this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketUpdateScore(scoreName));
    markSaveDataDirty();
  }
  
  public void broadcastScoreUpdate(String scoreName, ScoreObjective objective) {
    super.broadcastScoreUpdate(scoreName, objective);
    this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketUpdateScore(scoreName, objective));
    markSaveDataDirty();
  }
  
  public void setObjectiveInDisplaySlot(int objectiveSlot, ScoreObjective objective) {
    ScoreObjective scoreobjective = getObjectiveInDisplaySlot(objectiveSlot);
    super.setObjectiveInDisplaySlot(objectiveSlot, objective);
    if (scoreobjective != objective && scoreobjective != null)
      if (getObjectiveDisplaySlotCount(scoreobjective) > 0) {
        this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketDisplayObjective(objectiveSlot, objective));
      } else {
        sendDisplaySlotRemovalPackets(scoreobjective);
      }  
    if (objective != null)
      if (this.addedObjectives.contains(objective)) {
        this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketDisplayObjective(objectiveSlot, objective));
      } else {
        addObjective(objective);
      }  
    markSaveDataDirty();
  }
  
  public boolean addPlayerToTeam(String player, String newTeam) {
    if (super.addPlayerToTeam(player, newTeam)) {
      ScorePlayerTeam scoreplayerteam = getTeam(newTeam);
      this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketTeams(scoreplayerteam, Arrays.asList(new String[] { player }, ), 3));
      markSaveDataDirty();
      return true;
    } 
    return false;
  }
  
  public void removePlayerFromTeam(String username, ScorePlayerTeam playerTeam) {
    super.removePlayerFromTeam(username, playerTeam);
    this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketTeams(playerTeam, Arrays.asList(new String[] { username }, ), 4));
    markSaveDataDirty();
  }
  
  public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {
    super.onScoreObjectiveAdded(scoreObjectiveIn);
    markSaveDataDirty();
  }
  
  public void onObjectiveDisplayNameChanged(ScoreObjective objective) {
    super.onObjectiveDisplayNameChanged(objective);
    if (this.addedObjectives.contains(objective))
      this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketScoreboardObjective(objective, 2)); 
    markSaveDataDirty();
  }
  
  public void onScoreObjectiveRemoved(ScoreObjective objective) {
    super.onScoreObjectiveRemoved(objective);
    if (this.addedObjectives.contains(objective))
      sendDisplaySlotRemovalPackets(objective); 
    markSaveDataDirty();
  }
  
  public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {
    super.broadcastTeamCreated(playerTeam);
    this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketTeams(playerTeam, 0));
    markSaveDataDirty();
  }
  
  public void broadcastTeamInfoUpdate(ScorePlayerTeam playerTeam) {
    super.broadcastTeamInfoUpdate(playerTeam);
    this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketTeams(playerTeam, 2));
    markSaveDataDirty();
  }
  
  public void broadcastTeamRemove(ScorePlayerTeam playerTeam) {
    super.broadcastTeamRemove(playerTeam);
    this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketTeams(playerTeam, 1));
    markSaveDataDirty();
  }
  
  public void addDirtyRunnable(Runnable runnable) {
    this.dirtyRunnables = Arrays.<Runnable>copyOf(this.dirtyRunnables, this.dirtyRunnables.length + 1);
    this.dirtyRunnables[this.dirtyRunnables.length - 1] = runnable;
  }
  
  protected void markSaveDataDirty() {
    byte b;
    int i;
    Runnable[] arrayOfRunnable;
    for (i = (arrayOfRunnable = this.dirtyRunnables).length, b = 0; b < i; ) {
      Runnable runnable = arrayOfRunnable[b];
      runnable.run();
      b++;
    } 
  }
  
  public List<Packet<?>> getCreatePackets(ScoreObjective objective) {
    List<Packet<?>> list = Lists.newArrayList();
    list.add(new SPacketScoreboardObjective(objective, 0));
    for (int i = 0; i < 19; i++) {
      if (getObjectiveInDisplaySlot(i) == objective)
        list.add(new SPacketDisplayObjective(i, objective)); 
    } 
    for (Score score : getSortedScores(objective))
      list.add(new SPacketUpdateScore(score)); 
    return list;
  }
  
  public void addObjective(ScoreObjective objective) {
    List<Packet<?>> list = getCreatePackets(objective);
    for (EntityPlayerMP entityplayermp : this.scoreboardMCServer.getPlayerList().getPlayerList()) {
      for (Packet<?> packet : list)
        entityplayermp.connection.sendPacket(packet); 
    } 
    this.addedObjectives.add(objective);
  }
  
  public List<Packet<?>> getDestroyPackets(ScoreObjective p_96548_1_) {
    List<Packet<?>> list = Lists.newArrayList();
    list.add(new SPacketScoreboardObjective(p_96548_1_, 1));
    for (int i = 0; i < 19; i++) {
      if (getObjectiveInDisplaySlot(i) == p_96548_1_)
        list.add(new SPacketDisplayObjective(i, p_96548_1_)); 
    } 
    return list;
  }
  
  public void sendDisplaySlotRemovalPackets(ScoreObjective p_96546_1_) {
    List<Packet<?>> list = getDestroyPackets(p_96546_1_);
    for (EntityPlayerMP entityplayermp : this.scoreboardMCServer.getPlayerList().getPlayerList()) {
      for (Packet<?> packet : list)
        entityplayermp.connection.sendPacket(packet); 
    } 
    this.addedObjectives.remove(p_96546_1_);
  }
  
  public int getObjectiveDisplaySlotCount(ScoreObjective p_96552_1_) {
    int i = 0;
    for (int j = 0; j < 19; j++) {
      if (getObjectiveInDisplaySlot(j) == p_96552_1_)
        i++; 
    } 
    return i;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\ServerScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */