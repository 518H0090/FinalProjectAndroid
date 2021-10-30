package tdtu.com.finalprojectby518h0090.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Table {
    private String TKey;
    private String customerName;
    private String tableName;
    private String tableStatus;

    public Table() {

    }

    public Table(String TKey, String customerName, String tableName, String tableStatus) {
        this.TKey = TKey;
        this.customerName = customerName;
        this.tableName = tableName;
        this.tableStatus = tableStatus;
    }

    public String getTKey() {
        return TKey;
    }

    public void setTKey(String TKey) {
        this.TKey = TKey;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("tkey", TKey);
        result.put("customerName", customerName);
        result.put("tableName", tableName);
        result.put("tableStatus", tableStatus);

        return result;
    }
}
