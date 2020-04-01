package com.mysql.cj.protocol.x;

public enum XpluginStatementCommand {
  XPLUGIN_STMT_CREATE_COLLECTION("create_collection"),
  XPLUGIN_STMT_CREATE_COLLECTION_INDEX("create_collection_index"),
  XPLUGIN_STMT_DROP_COLLECTION("drop_collection"),
  XPLUGIN_STMT_DROP_COLLECTION_INDEX("drop_collection_index"),
  XPLUGIN_STMT_PING("ping"),
  XPLUGIN_STMT_LIST_OBJECTS("list_objects"),
  XPLUGIN_STMT_ENABLE_NOTICES("enable_notices"),
  XPLUGIN_STMT_DISABLE_NOTICES("disable_notices"),
  XPLUGIN_STMT_LIST_NOTICES("list_notices");
  
  public String commandName;
  
  XpluginStatementCommand(String commandName) {
    this.commandName = commandName;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\XpluginStatementCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */