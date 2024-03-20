package com.belas.ika.web;

import com.belas.ika.service.dto.ProcedureDetail;
import com.belas.ika.service.dto.ProcedureParameter;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedureResource {

    @Inject
    DataSource dataSource;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProcedureDetail> listProcedures() {
        List<ProcedureDetail> procedures = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getProcedures(null, "YOUR_SCHEMA", null)) {
                while (rs.next()) {
                    String procedureCatalog = rs.getString(1);
                    String procedureSchema = rs.getString(2);
                    String procedureName = rs.getString(3);
                    short procedureType = rs.getShort(8);
                    String remarks = rs.getString(7);
                    ProcedureDetail procedure = new ProcedureDetail(procedureName, procedureType, remarks);

                    // Fetching procedure parameters
                    try (ResultSet rsp = metaData.getProcedureColumns(null, "YOUR_SCHEMA", procedureName, null)) {
                        while (rsp.next()) {
                            String columnName = rsp.getString(4);
                            int columnType = rsp.getInt(5);
                            String columnTypeName = rsp.getString(6);
                            int dataType = rsp.getInt(6);
                            String typeName = rsp.getString(7);
                            int columnReturn = rsp.getInt(5); // Column return type for functions
                            ProcedureParameter parameter = new ProcedureParameter(columnName, columnType, columnTypeName, dataType, typeName);
                            procedure.parameters.add(parameter);
                        }
                    }

                    procedures.add(procedure);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }
        return procedures;
    }

}

