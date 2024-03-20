package com.belas.ika.service.dto;

import java.util.ArrayList;
import java.util.List;

public class ProcedureDetail {
    public String name;
    public short procedureType;
    public String remarks;
    public List<ProcedureParameter> parameters = new ArrayList<>();

    public ProcedureDetail(String name, short procedureType, String remarks) {
        this.name = name;
        this.procedureType = procedureType; // 1=PROCEDURE, 2=FUNCTION
        this.remarks = remarks;
    }
}
