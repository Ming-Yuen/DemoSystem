package com.database;

import java.util.List;

public interface DatabaseRecordClass {
  String getTableName();
  
  String[] getTablePrivateKey();
  
  void setWhereClauseKey(CharSequence paramCharSequence);
  
  StringBuffer getWhereClauseKey();
  
  void setWhereClauseValues(List<Object> paramList);
  
  List<Object> getWhereClauseValues();
}
