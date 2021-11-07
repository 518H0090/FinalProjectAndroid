package tdtu.com.finalprojectby518h0090.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Table {
    private String TKey;
    private String tableName;

    public Table() {

    }

    public Table(String TKey, String tableName) {
        this.TKey = TKey;
        this.tableName = tableName;
    }

    public String getTKey() {
        return TKey;
    }

    public void setTKey(String TKey) {
        this.TKey = TKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("tkey", TKey);
        result.put("tableName", tableName);

        return result;
    }
}
