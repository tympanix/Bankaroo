package dtu.dagprojekt.bankaroo.util;

import com.google.gson.stream.JsonWriter;

import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Query {

    public ResultSet getResultSet() {
        return resultSet;
    }

    private Statement statement;
    private ResultSet resultSet;

    public Query(String sql) throws SQLException {
        // Create the Statement
        statement = DB.getConnection().createStatement();

        // Execute a query and generate a ResultSet instance
        resultSet = statement.executeQuery(sql);
    }

    public void close() throws SQLException {
        // Close the ResultSet
        resultSet.close();

        // Close the Statement
        statement.close();

        // Connection must be on a unit-of-work boundary to allow close
        DB.getConnection().commit();
    }

    private void writeJson(Writer out) throws IOException, SQLException {
        JsonWriter writer = new JsonWriter(out);

        writer.beginArray();
        while(resultSet.next()) {
            writer.beginObject();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for(int i = 1; i <= metaData.getColumnCount(); i++) {
                writer.name(metaData.getColumnLabel(i));
                writer.value(resultSet.getString(i));
            }
            writer.endObject();
        }
        writer.endArray();
        writer.flush();
    }

    public StreamingOutput toJson(){
        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) {
                try {
                    Writer out = new BufferedWriter(new OutputStreamWriter(os));
                    writeJson(out);
                    out.flush();
                    close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
