package com.mysql.cj.jdbc.result;

import com.mysql.cj.Messages;
import com.mysql.cj.MysqlType;
import com.mysql.cj.Query;
import com.mysql.cj.Session;
import com.mysql.cj.conf.PropertyDefinitions;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.exceptions.AssertionFailedException;
import com.mysql.cj.exceptions.CJException;
import com.mysql.cj.exceptions.FeatureNotAvailableException;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlSQLXML;
import com.mysql.cj.jdbc.NClob;
import com.mysql.cj.jdbc.StatementImpl;
import com.mysql.cj.jdbc.exceptions.NotUpdatable;
import com.mysql.cj.jdbc.exceptions.SQLError;
import com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;
import com.mysql.cj.protocol.ResultsetRow;
import com.mysql.cj.protocol.ResultsetRows;
import com.mysql.cj.protocol.a.result.ByteArrayRow;
import com.mysql.cj.result.Field;
import com.mysql.cj.result.Row;
import com.mysql.cj.util.StringUtils;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UpdatableResultSet extends ResultSetImpl {
  static final byte[] STREAM_DATA_MARKER = StringUtils.getBytes("** STREAM DATA **");
  
  private String charEncoding;
  
  private byte[][] defaultColumnValue;
  
  private ClientPreparedStatement deleter = null;
  
  private String deleteSQL = null;
  
  protected ClientPreparedStatement inserter = null;
  
  private String insertSQL = null;
  
  private boolean isUpdatable = false;
  
  private String notUpdatableReason = null;
  
  private List<Integer> primaryKeyIndicies = null;
  
  private String qualifiedAndQuotedTableName;
  
  private String quotedIdChar = null;
  
  private ClientPreparedStatement refresher;
  
  private String refreshSQL = null;
  
  private Row savedCurrentRow;
  
  protected ClientPreparedStatement updater = null;
  
  private String updateSQL = null;
  
  private boolean populateInserterWithDefaultValues = false;
  
  private boolean pedantic;
  
  private boolean hasLongColumnInfo = false;
  
  private Map<String, Map<String, Map<String, Integer>>> databasesUsedToTablesUsed = null;
  
  private boolean onInsertRow = false;
  
  protected boolean doingUpdates = false;
  
  public UpdatableResultSet(ResultsetRows tuples, JdbcConnection conn, StatementImpl creatorStmt) throws SQLException {
    super(tuples, conn, creatorStmt);
    checkUpdatability();
    this.charEncoding = (String)this.session.getPropertySet().getStringProperty(PropertyKey.characterEncoding).getValue();
    this
      .populateInserterWithDefaultValues = ((Boolean)getSession().getPropertySet().getBooleanProperty(PropertyKey.populateInsertRowWithDefaultValues).getValue()).booleanValue();
    this.pedantic = ((Boolean)getSession().getPropertySet().getBooleanProperty(PropertyKey.pedantic).getValue()).booleanValue();
    this.hasLongColumnInfo = getSession().getServerSession().hasLongColumnInfo();
  }
  
  public boolean absolute(int row) throws SQLException {
    try {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      return super.absolute(row);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void afterLast() throws SQLException {
    try {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      super.afterLast();
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void beforeFirst() throws SQLException {
    try {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      super.beforeFirst();
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void cancelRowUpdates() throws SQLException {
    try {
      if (this.doingUpdates) {
        this.doingUpdates = false;
        this.updater.clearParameters();
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  protected void checkRowPos() throws SQLException {
    if (!this.onInsertRow)
      super.checkRowPos(); 
  }
  
  public void checkUpdatability() throws SQLException {
    try {
      if (getMetadata() == null)
        return; 
      String singleTableName = null;
      String dbName = null;
      int primaryKeyCount = 0;
      Field[] fields = getMetadata().getFields();
      if (this.db == null || this.db.length() == 0) {
        this.db = fields[0].getDatabaseName();
        if (this.db == null || this.db.length() == 0)
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.43"), "S1009", 
              getExceptionInterceptor()); 
      } 
      if (fields.length > 0) {
        singleTableName = fields[0].getOriginalTableName();
        dbName = fields[0].getDatabaseName();
        if (singleTableName == null) {
          singleTableName = fields[0].getTableName();
          dbName = this.db;
        } 
        if (singleTableName == null) {
          this.isUpdatable = false;
          this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
          return;
        } 
        if (fields[0].isPrimaryKey())
          primaryKeyCount++; 
        for (int i = 1; i < fields.length; i++) {
          String otherTableName = fields[i].getOriginalTableName();
          String otherDbName = fields[i].getDatabaseName();
          if (otherTableName == null) {
            otherTableName = fields[i].getTableName();
            otherDbName = this.db;
          } 
          if (otherTableName == null) {
            this.isUpdatable = false;
            this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
            return;
          } 
          if (!otherTableName.equals(singleTableName)) {
            this.isUpdatable = false;
            this.notUpdatableReason = Messages.getString("NotUpdatableReason.0");
            return;
          } 
          if (dbName == null || !otherDbName.equals(dbName)) {
            this.isUpdatable = false;
            this.notUpdatableReason = Messages.getString("NotUpdatableReason.1");
            return;
          } 
          if (fields[i].isPrimaryKey())
            primaryKeyCount++; 
        } 
      } else {
        this.isUpdatable = false;
        this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
        return;
      } 
      if (((Boolean)getSession().getPropertySet().getBooleanProperty(PropertyKey.strictUpdates).getValue()).booleanValue()) {
        DatabaseMetaData dbmd = getConnection().getMetaData();
        ResultSet rs = null;
        HashMap<String, String> primaryKeyNames = new HashMap<>();
        try {
          rs = (this.session.getPropertySet().getEnumProperty(PropertyKey.databaseTerm).getValue() == PropertyDefinitions.DatabaseTerm.SCHEMA) ? dbmd.getPrimaryKeys(null, dbName, singleTableName) : dbmd.getPrimaryKeys(dbName, null, singleTableName);
          while (rs.next()) {
            String keyName = rs.getString(4);
            keyName = keyName.toUpperCase();
            primaryKeyNames.put(keyName, keyName);
          } 
        } finally {
          if (rs != null) {
            try {
              rs.close();
            } catch (Exception ex) {
              AssertionFailedException.shouldNotHappen(ex);
            } 
            rs = null;
          } 
        } 
        int existingPrimaryKeysCount = primaryKeyNames.size();
        if (existingPrimaryKeysCount == 0) {
          this.isUpdatable = false;
          this.notUpdatableReason = Messages.getString("NotUpdatableReason.5");
          return;
        } 
        for (int i = 0; i < fields.length; i++) {
          if (fields[i].isPrimaryKey()) {
            String columnNameUC = fields[i].getName().toUpperCase();
            if (primaryKeyNames.remove(columnNameUC) == null) {
              String originalName = fields[i].getOriginalName();
              if (originalName != null && 
                primaryKeyNames.remove(originalName.toUpperCase()) == null) {
                this.isUpdatable = false;
                this.notUpdatableReason = Messages.getString("NotUpdatableReason.6", new Object[] { originalName });
                return;
              } 
            } 
          } 
        } 
        this.isUpdatable = primaryKeyNames.isEmpty();
        if (!this.isUpdatable) {
          this
            .notUpdatableReason = (existingPrimaryKeysCount > 1) ? Messages.getString("NotUpdatableReason.7") : Messages.getString("NotUpdatableReason.4");
          return;
        } 
      } 
      if (primaryKeyCount == 0) {
        this.isUpdatable = false;
        this.notUpdatableReason = Messages.getString("NotUpdatableReason.4");
        return;
      } 
      this.isUpdatable = true;
      this.notUpdatableReason = null;
      return;
    } catch (SQLException sqlEx) {
      this.isUpdatable = false;
      this.notUpdatableReason = sqlEx.getMessage();
      return;
    } 
  }
  
  public void deleteRow() throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.isUpdatable)
          throw new NotUpdatable(this.notUpdatableReason); 
        if (this.onInsertRow)
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.1"), getExceptionInterceptor()); 
        if (this.rowData.size() == 0)
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.2"), getExceptionInterceptor()); 
        if (isBeforeFirst())
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.3"), getExceptionInterceptor()); 
        if (isAfterLast())
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.4"), getExceptionInterceptor()); 
        if (this.deleter == null) {
          if (this.deleteSQL == null)
            generateStatements(); 
          this.deleter = (ClientPreparedStatement)this.connection.clientPrepareStatement(this.deleteSQL);
        } 
        this.deleter.clearParameters();
        int numKeys = this.primaryKeyIndicies.size();
        for (int i = 0; i < numKeys; i++) {
          int index = ((Integer)this.primaryKeyIndicies.get(i)).intValue();
          setParamValue(this.deleter, i + 1, this.thisRow, index, getMetadata().getFields()[index]);
        } 
        this.deleter.executeUpdate();
        this.rowData.remove();
        previous();
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  private void setParamValue(ClientPreparedStatement ps, int psIdx, Row row, int rsIdx, Field field) throws SQLException {
    byte[] val = row.getBytes(rsIdx);
    if (val == null) {
      ps.setNull(psIdx, MysqlType.NULL);
      return;
    } 
    switch (field.getMysqlType()) {
      case NULL:
        ps.setNull(psIdx, MysqlType.NULL);
        return;
      case TINYINT:
      case TINYINT_UNSIGNED:
      case SMALLINT:
      case SMALLINT_UNSIGNED:
      case MEDIUMINT:
      case MEDIUMINT_UNSIGNED:
      case INT:
      case INT_UNSIGNED:
      case YEAR:
        ps.setInt(psIdx, getInt(rsIdx + 1));
        return;
      case BIGINT:
        ps.setLong(psIdx, getLong(rsIdx + 1));
        return;
      case BIGINT_UNSIGNED:
        ps.setBigInteger(psIdx, getBigInteger(rsIdx + 1));
        return;
      case CHAR:
      case ENUM:
      case SET:
      case VARCHAR:
      case JSON:
      case TINYTEXT:
      case TEXT:
      case MEDIUMTEXT:
      case LONGTEXT:
      case DECIMAL:
      case DECIMAL_UNSIGNED:
        ps.setString(psIdx, getString(rsIdx + 1));
        return;
      case DATE:
        ps.setDate(psIdx, getDate(rsIdx + 1));
        return;
      case TIMESTAMP:
      case DATETIME:
        ps.setTimestamp(psIdx, getTimestamp(rsIdx + 1), null, field.getDecimals());
        return;
      case TIME:
        ps.setTime(psIdx, getTime(rsIdx + 1));
        return;
      case DOUBLE:
      case DOUBLE_UNSIGNED:
      case FLOAT:
      case FLOAT_UNSIGNED:
      case BOOLEAN:
      case BIT:
        ps.setBytesNoEscapeNoQuotes(psIdx, val);
        return;
    } 
    ps.setBytes(psIdx, val);
  }
  
  private void extractDefaultValues() throws SQLException {
    DatabaseMetaData dbmd = getConnection().getMetaData();
    this.defaultColumnValue = new byte[(getMetadata().getFields()).length][];
    ResultSet columnsResultSet = null;
    for (Map.Entry<String, Map<String, Map<String, Integer>>> dbEntry : this.databasesUsedToTablesUsed.entrySet()) {
      for (Map.Entry<String, Map<String, Integer>> tableEntry : (Iterable<Map.Entry<String, Map<String, Integer>>>)((Map)dbEntry.getValue()).entrySet()) {
        String tableName = tableEntry.getKey();
        Map<String, Integer> columnNamesToIndices = tableEntry.getValue();
        try {
          columnsResultSet = (this.session.getPropertySet().getEnumProperty(PropertyKey.databaseTerm).getValue() == PropertyDefinitions.DatabaseTerm.SCHEMA) ? dbmd.getColumns(null, this.db, tableName, "%") : dbmd.getColumns(this.db, null, tableName, "%");
          while (columnsResultSet.next()) {
            String columnName = columnsResultSet.getString("COLUMN_NAME");
            byte[] defaultValue = columnsResultSet.getBytes("COLUMN_DEF");
            if (columnNamesToIndices.containsKey(columnName)) {
              int localColumnIndex = ((Integer)columnNamesToIndices.get(columnName)).intValue();
              this.defaultColumnValue[localColumnIndex] = defaultValue;
            } 
          } 
        } finally {
          if (columnsResultSet != null) {
            columnsResultSet.close();
            columnsResultSet = null;
          } 
        } 
      } 
    } 
  }
  
  public boolean first() throws SQLException {
    try {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      return super.first();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  protected void generateStatements() throws SQLException {
    try {
      if (!this.isUpdatable) {
        this.doingUpdates = false;
        this.onInsertRow = false;
        throw new NotUpdatable(this.notUpdatableReason);
      } 
      String quotedId = getQuotedIdChar();
      Map<String, String> tableNamesSoFar = null;
      if (this.session.getServerSession().isLowerCaseTableNames()) {
        tableNamesSoFar = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.databasesUsedToTablesUsed = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
      } else {
        tableNamesSoFar = new TreeMap<>();
        this.databasesUsedToTablesUsed = new TreeMap<>();
      } 
      this.primaryKeyIndicies = new ArrayList<>();
      StringBuilder fieldValues = new StringBuilder();
      StringBuilder keyValues = new StringBuilder();
      StringBuilder columnNames = new StringBuilder();
      StringBuilder insertPlaceHolders = new StringBuilder();
      StringBuilder allTablesBuf = new StringBuilder();
      Map<Integer, String> columnIndicesToTable = new HashMap<>();
      Field[] fields = getMetadata().getFields();
      for (int i = 0; i < fields.length; i++) {
        Map<String, Integer> updColumnNameToIndex = null;
        if (fields[i].getOriginalTableName() != null) {
          String str1 = fields[i].getDatabaseName();
          String tableOnlyName = fields[i].getOriginalTableName();
          String fqTableName = StringUtils.getFullyQualifiedName(str1, tableOnlyName, quotedId, this.pedantic);
          if (!tableNamesSoFar.containsKey(fqTableName)) {
            if (!tableNamesSoFar.isEmpty())
              allTablesBuf.append(','); 
            allTablesBuf.append(fqTableName);
            tableNamesSoFar.put(fqTableName, fqTableName);
          } 
          columnIndicesToTable.put(Integer.valueOf(i), fqTableName);
          updColumnNameToIndex = getColumnsToIndexMapForTableAndDB(str1, tableOnlyName);
        } else {
          String tableOnlyName = fields[i].getTableName();
          if (tableOnlyName != null) {
            String fqTableName = StringUtils.quoteIdentifier(tableOnlyName, quotedId, this.pedantic);
            if (!tableNamesSoFar.containsKey(fqTableName)) {
              if (!tableNamesSoFar.isEmpty())
                allTablesBuf.append(','); 
              allTablesBuf.append(fqTableName);
              tableNamesSoFar.put(fqTableName, fqTableName);
            } 
            columnIndicesToTable.put(Integer.valueOf(i), fqTableName);
            updColumnNameToIndex = getColumnsToIndexMapForTableAndDB(this.db, tableOnlyName);
          } 
        } 
        String originalColumnName = fields[i].getOriginalName();
        String columnName = (this.hasLongColumnInfo && originalColumnName != null && originalColumnName.length() > 0) ? originalColumnName : fields[i].getName();
        if (updColumnNameToIndex != null && columnName != null)
          updColumnNameToIndex.put(columnName, Integer.valueOf(i)); 
        String originalTableName = fields[i].getOriginalTableName();
        String tableName = (this.hasLongColumnInfo && originalTableName != null && originalTableName.length() > 0) ? originalTableName : fields[i].getTableName();
        String databaseName = fields[i].getDatabaseName();
        String qualifiedColumnName = StringUtils.getFullyQualifiedName(databaseName, tableName, quotedId, this.pedantic) + '.' + StringUtils.quoteIdentifier(columnName, quotedId, this.pedantic);
        if (fields[i].isPrimaryKey()) {
          this.primaryKeyIndicies.add(Integer.valueOf(i));
          if (keyValues.length() > 0)
            keyValues.append(" AND "); 
          keyValues.append(qualifiedColumnName);
          keyValues.append("<=>");
          keyValues.append("?");
        } 
        if (fieldValues.length() == 0) {
          fieldValues.append("SET ");
        } else {
          fieldValues.append(",");
          columnNames.append(",");
          insertPlaceHolders.append(",");
        } 
        insertPlaceHolders.append("?");
        columnNames.append(qualifiedColumnName);
        fieldValues.append(qualifiedColumnName);
        fieldValues.append("=?");
      } 
      this.qualifiedAndQuotedTableName = allTablesBuf.toString();
      this.updateSQL = "UPDATE " + this.qualifiedAndQuotedTableName + " " + fieldValues.toString() + " WHERE " + keyValues.toString();
      this.insertSQL = "INSERT INTO " + this.qualifiedAndQuotedTableName + " (" + columnNames.toString() + ") VALUES (" + insertPlaceHolders.toString() + ")";
      this.refreshSQL = "SELECT " + columnNames.toString() + " FROM " + this.qualifiedAndQuotedTableName + " WHERE " + keyValues.toString();
      this.deleteSQL = "DELETE FROM " + this.qualifiedAndQuotedTableName + " WHERE " + keyValues.toString();
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  private Map<String, Integer> getColumnsToIndexMapForTableAndDB(String databaseName, String tableName) {
    Map<String, Map<String, Integer>> tablesUsedToColumnsMap = this.databasesUsedToTablesUsed.get(databaseName);
    if (tablesUsedToColumnsMap == null) {
      tablesUsedToColumnsMap = this.session.getServerSession().isLowerCaseTableNames() ? new TreeMap<>(String.CASE_INSENSITIVE_ORDER) : new TreeMap<>();
      this.databasesUsedToTablesUsed.put(databaseName, tablesUsedToColumnsMap);
    } 
    Map<String, Integer> nameToIndex = tablesUsedToColumnsMap.get(tableName);
    if (nameToIndex == null) {
      nameToIndex = new HashMap<>();
      tablesUsedToColumnsMap.put(tableName, nameToIndex);
    } 
    return nameToIndex;
  }
  
  public int getConcurrency() throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        return this.isUpdatable ? 1008 : 1007;
      } 
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  private String getQuotedIdChar() throws SQLException {
    if (this.quotedIdChar == null)
      this.quotedIdChar = this.session.getIdentifierQuoteString(); 
    return this.quotedIdChar;
  }
  
  public void insertRow() throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow)
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.7"), getExceptionInterceptor()); 
        this.inserter.executeUpdate();
        long autoIncrementId = this.inserter.getLastInsertID();
        Field[] fields = getMetadata().getFields();
        byte[][] newRow = new byte[fields.length][];
        for (int i = 0; i < fields.length; i++) {
          newRow[i] = this.inserter.isNull(i + 1) ? null : this.inserter.getBytesRepresentation(i + 1);
          if (fields[i].isAutoIncrement() && autoIncrementId > 0L) {
            newRow[i] = StringUtils.getBytes(String.valueOf(autoIncrementId));
            this.inserter.setBytesNoEscapeNoQuotes(i + 1, newRow[i]);
          } 
        } 
        ByteArrayRow byteArrayRow = new ByteArrayRow(newRow, getExceptionInterceptor());
        refreshRow(this.inserter, (Row)byteArrayRow);
        this.rowData.addRow((Row)byteArrayRow);
        resetInserter();
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean isAfterLast() throws SQLException {
    try {
      return super.isAfterLast();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean isBeforeFirst() throws SQLException {
    try {
      return super.isBeforeFirst();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean isFirst() throws SQLException {
    try {
      return super.isFirst();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean isLast() throws SQLException {
    try {
      return super.isLast();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  boolean isUpdatable() {
    return this.isUpdatable;
  }
  
  public boolean last() throws SQLException {
    try {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      return super.last();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void moveToCurrentRow() throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.isUpdatable)
          throw new NotUpdatable(this.notUpdatableReason); 
        if (this.onInsertRow) {
          this.onInsertRow = false;
          this.thisRow = this.savedCurrentRow;
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void moveToInsertRow() throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.isUpdatable)
          throw new NotUpdatable(this.notUpdatableReason); 
        if (this.inserter == null) {
          if (this.insertSQL == null)
            generateStatements(); 
          this.inserter = (ClientPreparedStatement)getConnection().clientPrepareStatement(this.insertSQL);
          this.inserter.getQueryBindings().setColumnDefinition(getMetadata());
          if (this.populateInserterWithDefaultValues)
            extractDefaultValues(); 
        } 
        resetInserter();
        Field[] fields = getMetadata().getFields();
        int numFields = fields.length;
        this.onInsertRow = true;
        this.doingUpdates = false;
        this.savedCurrentRow = this.thisRow;
        byte[][] newRowData = new byte[numFields][];
        this.thisRow = (Row)new ByteArrayRow(newRowData, getExceptionInterceptor());
        this.thisRow.setMetadata(getMetadata());
        for (int i = 0; i < numFields; i++) {
          if (!this.populateInserterWithDefaultValues) {
            this.inserter.setBytesNoEscapeNoQuotes(i + 1, StringUtils.getBytes("DEFAULT"));
            newRowData = (byte[][])null;
          } else if (this.defaultColumnValue[i] != null) {
            Field f = fields[i];
            switch (f.getMysqlTypeId()) {
              case 7:
              case 10:
              case 11:
              case 12:
                if ((this.defaultColumnValue[i]).length > 7 && this.defaultColumnValue[i][0] == 67 && this.defaultColumnValue[i][1] == 85 && this.defaultColumnValue[i][2] == 82 && this.defaultColumnValue[i][3] == 82 && this.defaultColumnValue[i][4] == 69 && this.defaultColumnValue[i][5] == 78 && this.defaultColumnValue[i][6] == 84 && this.defaultColumnValue[i][7] == 95) {
                  this.inserter.setBytesNoEscapeNoQuotes(i + 1, this.defaultColumnValue[i]);
                  break;
                } 
                this.inserter.setBytes(i + 1, this.defaultColumnValue[i], false, false);
                break;
              default:
                this.inserter.setBytes(i + 1, this.defaultColumnValue[i], false, false);
                break;
            } 
            byte[] defaultValueCopy = new byte[(this.defaultColumnValue[i]).length];
            System.arraycopy(this.defaultColumnValue[i], 0, defaultValueCopy, 0, defaultValueCopy.length);
            newRowData[i] = defaultValueCopy;
          } else {
            this.inserter.setNull(i + 1, MysqlType.NULL);
            newRowData[i] = null;
          } 
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean next() throws SQLException {
    try {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      return super.next();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean prev() throws SQLException {
    return super.prev();
  }
  
  public boolean previous() throws SQLException {
    try {
      if (this.onInsertRow)
        this.onInsertRow = false; 
      if (this.doingUpdates)
        this.doingUpdates = false; 
      return super.previous();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void realClose(boolean calledExplicitly) throws SQLException {
    try {
      if (this.isClosed)
        return; 
      synchronized (checkClosed().getConnectionMutex()) {
        SQLException sqlEx = null;
        if (this.useUsageAdvisor && 
          this.deleter == null && this.inserter == null && this.refresher == null && this.updater == null)
          this.eventSink.processEvent((byte)0, (Session)this.session, (Query)getOwningStatement(), this, 0L, new Throwable(), 
              Messages.getString("UpdatableResultSet.34")); 
        try {
          if (this.deleter != null)
            this.deleter.close(); 
        } catch (SQLException ex) {
          sqlEx = ex;
        } 
        try {
          if (this.inserter != null)
            this.inserter.close(); 
        } catch (SQLException ex) {
          sqlEx = ex;
        } 
        try {
          if (this.refresher != null)
            this.refresher.close(); 
        } catch (SQLException ex) {
          sqlEx = ex;
        } 
        try {
          if (this.updater != null)
            this.updater.close(); 
        } catch (SQLException ex) {
          sqlEx = ex;
        } 
        super.realClose(calledExplicitly);
        if (sqlEx != null)
          throw sqlEx; 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void refreshRow() throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.isUpdatable)
          throw new NotUpdatable(Messages.getString("NotUpdatable.0")); 
        if (this.onInsertRow)
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.8"), getExceptionInterceptor()); 
        if (this.rowData.size() == 0)
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.9"), getExceptionInterceptor()); 
        if (isBeforeFirst())
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.10"), getExceptionInterceptor()); 
        if (isAfterLast())
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.11"), getExceptionInterceptor()); 
        refreshRow(this.updater, this.thisRow);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  private void refreshRow(ClientPreparedStatement updateInsertStmt, Row rowToRefresh) throws SQLException {
    if (this.refresher == null) {
      if (this.refreshSQL == null)
        generateStatements(); 
      this
        
        .refresher = ((ResultsetRow)this.thisRow).isBinaryEncoded() ? (ClientPreparedStatement)getConnection().serverPrepareStatement(this.refreshSQL) : (ClientPreparedStatement)getConnection().clientPrepareStatement(this.refreshSQL);
      this.refresher.getQueryBindings().setColumnDefinition(getMetadata());
    } 
    this.refresher.clearParameters();
    int numKeys = this.primaryKeyIndicies.size();
    for (int i = 0; i < numKeys; i++) {
      byte[] dataFrom = null;
      int index = ((Integer)this.primaryKeyIndicies.get(i)).intValue();
      if (!this.doingUpdates && !this.onInsertRow) {
        setParamValue(this.refresher, i + 1, this.thisRow, index, getMetadata().getFields()[index]);
      } else {
        dataFrom = updateInsertStmt.getBytesRepresentation(index + 1);
        if (updateInsertStmt.isNull(index + 1) || dataFrom.length == 0) {
          setParamValue(this.refresher, i + 1, this.thisRow, index, getMetadata().getFields()[index]);
        } else {
          dataFrom = StringUtils.stripEnclosure(dataFrom, "_binary'", "'");
          byte[] origBytes = updateInsertStmt.getOrigBytes(i + 1);
          if (origBytes != null) {
            if (this.refresher instanceof com.mysql.cj.jdbc.ServerPreparedStatement) {
              this.refresher.setBytesNoEscapeNoQuotes(i + 1, origBytes);
            } else {
              this.refresher.setBytesNoEscapeNoQuotes(i + 1, dataFrom);
            } 
          } else {
            this.refresher.setBytesNoEscape(i + 1, dataFrom);
          } 
        } 
      } 
    } 
    ResultSet rs = null;
    try {
      rs = this.refresher.executeQuery();
      int numCols = rs.getMetaData().getColumnCount();
      if (rs.next()) {
        for (int j = 0; j < numCols; j++) {
          byte[] val = rs.getBytes(j + 1);
          rowToRefresh.setBytes(j, (val == null || rs.wasNull()) ? null : val);
        } 
      } else {
        throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.12"), "S1000", 
            getExceptionInterceptor());
      } 
    } finally {
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException sQLException) {} 
    } 
  }
  
  public boolean relative(int rows) throws SQLException {
    try {
      return super.relative(rows);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  private void resetInserter() throws SQLException {
    this.inserter.clearParameters();
    for (int i = 0; i < (getMetadata().getFields()).length; i++)
      this.inserter.setNull(i + 1, 0); 
  }
  
  public boolean rowDeleted() throws SQLException {
    try {
      throw SQLError.createSQLFeatureNotSupportedException();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean rowInserted() throws SQLException {
    try {
      throw SQLError.createSQLFeatureNotSupportedException();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean rowUpdated() throws SQLException {
    try {
      throw SQLError.createSQLFeatureNotSupportedException();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void setResultSetConcurrency(int concurrencyFlag) {
    super.setResultSetConcurrency(concurrencyFlag);
  }
  
  protected void syncUpdate() throws SQLException {
    if (this.updater == null) {
      if (this.updateSQL == null)
        generateStatements(); 
      this.updater = (ClientPreparedStatement)getConnection().clientPrepareStatement(this.updateSQL);
      this.updater.getQueryBindings().setColumnDefinition(getMetadata());
    } 
    Field[] fields = getMetadata().getFields();
    int numFields = fields.length;
    this.updater.clearParameters();
    for (int i = 0; i < numFields; i++) {
      if (this.thisRow.getBytes(i) != null) {
        switch (fields[i].getMysqlType()) {
          case DATE:
          case TIMESTAMP:
          case DATETIME:
          case TIME:
            this.updater.setString(i + 1, getString(i + 1));
            break;
          default:
            this.updater.setObject(i + 1, getObject(i + 1), (SQLType)fields[i].getMysqlType());
            break;
        } 
      } else {
        this.updater.setNull(i + 1, 0);
      } 
    } 
    int numKeys = this.primaryKeyIndicies.size();
    for (int j = 0; j < numKeys; j++) {
      int idx = ((Integer)this.primaryKeyIndicies.get(j)).intValue();
      setParamValue(this.updater, numFields + j + 1, this.thisRow, idx, fields[idx]);
    } 
  }
  
  public void updateRow() throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.isUpdatable)
          throw new NotUpdatable(this.notUpdatableReason); 
        if (this.doingUpdates) {
          this.updater.executeUpdate();
          refreshRow();
          this.doingUpdates = false;
        } else if (this.onInsertRow) {
          throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.44"), getExceptionInterceptor());
        } 
        syncUpdate();
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public int getHoldability() throws SQLException {
    try {
      throw SQLError.createSQLFeatureNotSupportedException();
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
    try {
      updateAsciiStream(findColumn(columnLabel), x, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setAsciiStream(columnIndex, x, length);
        } else {
          this.inserter.setAsciiStream(columnIndex, x, length);
          this.thisRow.setBytes(columnIndex - 1, STREAM_DATA_MARKER);
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
    try {
      updateBigDecimal(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setBigDecimal(columnIndex, x);
        } else {
          this.inserter.setBigDecimal(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : StringUtils.getBytes(x.toString()));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
    try {
      updateBinaryStream(findColumn(columnLabel), x, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setBinaryStream(columnIndex, x, length);
        } else {
          this.inserter.setBinaryStream(columnIndex, x, length);
          this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : STREAM_DATA_MARKER);
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBlob(String columnLabel, Blob blob) throws SQLException {
    try {
      updateBlob(findColumn(columnLabel), blob);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBlob(int columnIndex, Blob blob) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setBlob(columnIndex, blob);
        } else {
          this.inserter.setBlob(columnIndex, blob);
          this.thisRow.setBytes(columnIndex - 1, (blob == null) ? null : STREAM_DATA_MARKER);
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBoolean(String columnLabel, boolean x) throws SQLException {
    try {
      updateBoolean(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBoolean(int columnIndex, boolean x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setBoolean(columnIndex, x);
        } else {
          this.inserter.setBoolean(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateByte(String columnLabel, byte x) throws SQLException {
    try {
      updateByte(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateByte(int columnIndex, byte x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setByte(columnIndex, x);
        } else {
          this.inserter.setByte(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBytes(String columnLabel, byte[] x) throws SQLException {
    try {
      updateBytes(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBytes(int columnIndex, byte[] x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setBytes(columnIndex, x);
        } else {
          this.inserter.setBytes(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, x);
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
    try {
      updateCharacterStream(findColumn(columnLabel), reader, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setCharacterStream(columnIndex, x, length);
        } else {
          this.inserter.setCharacterStream(columnIndex, x, length);
          this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : STREAM_DATA_MARKER);
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateClob(String columnLabel, Clob clob) throws SQLException {
    try {
      updateClob(findColumn(columnLabel), clob);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateClob(int columnIndex, Clob clob) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (clob == null) {
          updateNull(columnIndex);
        } else {
          updateCharacterStream(columnIndex, clob.getCharacterStream(), (int)clob.length());
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateDate(String columnLabel, Date x) throws SQLException {
    try {
      updateDate(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateDate(int columnIndex, Date x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setDate(columnIndex, x);
        } else {
          this.inserter.setDate(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateDouble(String columnLabel, double x) throws SQLException {
    try {
      updateDouble(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateDouble(int columnIndex, double x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setDouble(columnIndex, x);
        } else {
          this.inserter.setDouble(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateFloat(String columnLabel, float x) throws SQLException {
    try {
      updateFloat(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateFloat(int columnIndex, float x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setFloat(columnIndex, x);
        } else {
          this.inserter.setFloat(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateInt(String columnLabel, int x) throws SQLException {
    try {
      updateInt(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateInt(int columnIndex, int x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setInt(columnIndex, x);
        } else {
          this.inserter.setInt(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateLong(String columnLabel, long x) throws SQLException {
    try {
      updateLong(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateLong(int columnIndex, long x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setLong(columnIndex, x);
        } else {
          this.inserter.setLong(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNull(String columnLabel) throws SQLException {
    try {
      updateNull(findColumn(columnLabel));
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNull(int columnIndex) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setNull(columnIndex, 0);
        } else {
          this.inserter.setNull(columnIndex, 0);
          this.thisRow.setBytes(columnIndex - 1, null);
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateObject(String columnLabel, Object x) throws SQLException {
    try {
      updateObject(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateObject(int columnIndex, Object x) throws SQLException {
    try {
      updateObjectInternal(columnIndex, x, (Integer)null, 0);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateObject(String columnLabel, Object x, int scale) throws SQLException {
    try {
      updateObject(findColumn(columnLabel), x, scale);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
    try {
      updateObjectInternal(columnIndex, x, (Integer)null, scale);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  protected void updateObjectInternal(int columnIndex, Object x, Integer targetType, int scaleOrLength) throws SQLException {
    try {
      MysqlType targetMysqlType = (targetType == null) ? null : MysqlType.getByJdbcType(targetType.intValue());
      updateObjectInternal(columnIndex, x, (SQLType)targetMysqlType, scaleOrLength);
    } catch (FeatureNotAvailableException nae) {
      throw SQLError.createSQLFeatureNotSupportedException(Messages.getString("Statement.UnsupportedSQLType") + JDBCType.valueOf(targetType.intValue()), "S1C00", 
          getExceptionInterceptor());
    } 
  }
  
  protected void updateObjectInternal(int columnIndex, Object x, SQLType targetType, int scaleOrLength) throws SQLException {
    synchronized (checkClosed().getConnectionMutex()) {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        if (targetType == null) {
          this.updater.setObject(columnIndex, x);
        } else {
          this.updater.setObject(columnIndex, x, targetType);
        } 
      } else {
        if (targetType == null) {
          this.inserter.setObject(columnIndex, x);
        } else {
          this.inserter.setObject(columnIndex, x, targetType);
        } 
        this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
      } 
    } 
  }
  
  public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
    try {
      updateObject(findColumn(columnLabel), x, targetSqlType);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
    try {
      updateObjectInternal(columnIndex, x, targetSqlType, 0);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
    try {
      updateObject(findColumn(columnLabel), x, targetSqlType, scaleOrLength);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
    try {
      updateObjectInternal(columnIndex, x, targetSqlType, scaleOrLength);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateShort(String columnLabel, short x) throws SQLException {
    try {
      updateShort(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateShort(int columnIndex, short x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setShort(columnIndex, x);
        } else {
          this.inserter.setShort(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateString(String columnLabel, String x) throws SQLException {
    try {
      updateString(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateString(int columnIndex, String x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setString(columnIndex, x);
        } else {
          this.inserter.setString(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : StringUtils.getBytes(x, this.charEncoding));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateTime(String columnLabel, Time x) throws SQLException {
    try {
      updateTime(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateTime(int columnIndex, Time x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setTime(columnIndex, x);
        } else {
          this.inserter.setTime(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
    try {
      updateTimestamp(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setTimestamp(columnIndex, x);
        } else {
          this.inserter.setTimestamp(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
    try {
      updateAsciiStream(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
    try {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setAsciiStream(columnIndex, x);
      } else {
        this.inserter.setAsciiStream(columnIndex, x);
        this.thisRow.setBytes(columnIndex - 1, STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
    try {
      updateAsciiStream(findColumn(columnLabel), x, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
    try {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setAsciiStream(columnIndex, x, length);
      } else {
        this.inserter.setAsciiStream(columnIndex, x, length);
        this.thisRow.setBytes(columnIndex - 1, STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
    try {
      updateBinaryStream(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
    try {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBinaryStream(columnIndex, x);
      } else {
        this.inserter.setBinaryStream(columnIndex, x);
        this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
    try {
      updateBinaryStream(findColumn(columnLabel), x, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
    try {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBinaryStream(columnIndex, x, length);
      } else {
        this.inserter.setBinaryStream(columnIndex, x, length);
        this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
    try {
      updateBlob(findColumn(columnLabel), inputStream);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
    try {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBlob(columnIndex, inputStream);
      } else {
        this.inserter.setBlob(columnIndex, inputStream);
        this.thisRow.setBytes(columnIndex - 1, (inputStream == null) ? null : STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
    try {
      updateBlob(findColumn(columnLabel), inputStream, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
    try {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setBlob(columnIndex, inputStream, length);
      } else {
        this.inserter.setBlob(columnIndex, inputStream, length);
        this.thisRow.setBytes(columnIndex - 1, (inputStream == null) ? null : STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
    try {
      updateCharacterStream(findColumn(columnLabel), reader);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
    try {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setCharacterStream(columnIndex, x);
      } else {
        this.inserter.setCharacterStream(columnIndex, x);
        this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    try {
      updateCharacterStream(findColumn(columnLabel), reader, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    try {
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setCharacterStream(columnIndex, x, length);
      } else {
        this.inserter.setCharacterStream(columnIndex, x, length);
        this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateClob(String columnLabel, Reader reader) throws SQLException {
    try {
      updateClob(findColumn(columnLabel), reader);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateClob(int columnIndex, Reader reader) throws SQLException {
    try {
      updateCharacterStream(columnIndex, reader);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
    try {
      updateClob(findColumn(columnLabel), reader, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
    try {
      updateCharacterStream(columnIndex, reader, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
    try {
      updateNCharacterStream(findColumn(columnLabel), reader);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
    try {
      String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
      if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
        throw new SQLException(Messages.getString("ResultSet.16")); 
      if (!this.onInsertRow) {
        if (!this.doingUpdates) {
          this.doingUpdates = true;
          syncUpdate();
        } 
        this.updater.setNCharacterStream(columnIndex, x);
      } else {
        this.inserter.setNCharacterStream(columnIndex, x);
        this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : STREAM_DATA_MARKER);
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
    try {
      updateNCharacterStream(findColumn(columnLabel), reader, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
        if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
          throw new SQLException(Messages.getString("ResultSet.16")); 
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setNCharacterStream(columnIndex, x, length);
        } else {
          this.inserter.setNCharacterStream(columnIndex, x, length);
          this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : STREAM_DATA_MARKER);
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNClob(String columnLabel, Reader reader) throws SQLException {
    try {
      updateNClob(findColumn(columnLabel), reader);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNClob(int columnIndex, Reader reader) throws SQLException {
    try {
      String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
      if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
        throw new SQLException(Messages.getString("ResultSet.17")); 
      updateCharacterStream(columnIndex, reader);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
    try {
      updateNClob(findColumn(columnLabel), reader, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
    try {
      String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
      if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
        throw new SQLException(Messages.getString("ResultSet.17")); 
      updateCharacterStream(columnIndex, reader, length);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
    try {
      updateNClob(findColumn(columnLabel), nClob);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
        if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
          throw new SQLException(Messages.getString("ResultSet.17")); 
        if (nClob == null) {
          updateNull(columnIndex);
        } else {
          updateNCharacterStream(columnIndex, nClob.getCharacterStream(), (int)nClob.length());
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
    try {
      updateSQLXML(findColumn(columnLabel), xmlObject);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
    try {
      updateString(columnIndex, ((MysqlSQLXML)xmlObject).getString());
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNString(String columnLabel, String x) throws SQLException {
    try {
      updateNString(findColumn(columnLabel), x);
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public void updateNString(int columnIndex, String x) throws SQLException {
    try {
      synchronized (checkClosed().getConnectionMutex()) {
        String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
        if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
          throw new SQLException(Messages.getString("ResultSet.18")); 
        if (!this.onInsertRow) {
          if (!this.doingUpdates) {
            this.doingUpdates = true;
            syncUpdate();
          } 
          this.updater.setNString(columnIndex, x);
        } else {
          this.inserter.setNString(columnIndex, x);
          this.thisRow.setBytes(columnIndex - 1, (x == null) ? null : StringUtils.getBytes(x, fieldEncoding));
        } 
      } 
      return;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public Reader getNCharacterStream(String columnLabel) throws SQLException {
    try {
      return getNCharacterStream(findColumn(columnLabel));
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public Reader getNCharacterStream(int columnIndex) throws SQLException {
    try {
      String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
      if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
        throw new SQLException(Messages.getString("ResultSet.11")); 
      return getCharacterStream(columnIndex);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public NClob getNClob(String columnLabel) throws SQLException {
    try {
      return getNClob(findColumn(columnLabel));
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public NClob getNClob(int columnIndex) throws SQLException {
    try {
      String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
      if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
        throw new SQLException("Can not call getNClob() when field's charset isn't UTF-8"); 
      String asString = getStringForNClob(columnIndex);
      if (asString == null)
        return null; 
      return (NClob)new NClob(asString, getExceptionInterceptor());
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public String getNString(String columnLabel) throws SQLException {
    try {
      return getNString(findColumn(columnLabel));
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public String getNString(int columnIndex) throws SQLException {
    try {
      String fieldEncoding = getMetadata().getFields()[columnIndex - 1].getEncoding();
      if (fieldEncoding == null || !fieldEncoding.equals("UTF-8"))
        throw new SQLException("Can not call getNString() when field's charset isn't UTF-8"); 
      return getString(columnIndex);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public SQLXML getSQLXML(String columnLabel) throws SQLException {
    try {
      return getSQLXML(findColumn(columnLabel));
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public SQLXML getSQLXML(int columnIndex) throws SQLException {
    try {
      return (SQLXML)new MysqlSQLXML(this, columnIndex, getExceptionInterceptor());
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  private String getStringForNClob(int columnIndex) throws SQLException {
    String asString = null;
    String forcedEncoding = "UTF-8";
    try {
      byte[] asBytes = null;
      asBytes = getBytes(columnIndex);
      if (asBytes != null)
        asString = new String(asBytes, forcedEncoding); 
    } catch (UnsupportedEncodingException uee) {
      throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", 
          getExceptionInterceptor());
    } 
    return asString;
  }
  
  public boolean isClosed() throws SQLException {
    try {
      return this.isClosed;
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    try {
      checkClosed();
      return iface.isInstance(this);
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
  
  public <T> T unwrap(Class<T> iface) throws SQLException {
    try {
      try {
        return iface.cast(this);
      } catch (ClassCastException cce) {
        throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", 
            getExceptionInterceptor());
      } 
    } catch (CJException cJException) {
      throw SQLExceptionsMapping.translateException(cJException, getExceptionInterceptor());
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\result\UpdatableResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */