package com.belas.ika.service.dto;

public class ProcedureParameter {
    public String name;
    public int columnType;
    public String columnTypeName;
    public int dataType;
    public String typeName;

    public ProcedureParameter(String name, int columnType, String columnTypeName, int dataType, String typeName) {
        this.name = name;
        this.columnType = columnType; // 1=IN, 2=INOUT, 3=OUT, 4=RETURN, etc.
        this.columnTypeName = columnTypeName;
        this.dataType = dataType;
        this.typeName = typeName;
    }
}
