package server;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class OnlineUserTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -444245379288364831L;
    /** 列名标题 */
    private String title = "username";
    /** 数据行 */
    private List<String> rows = new ArrayList<String>();

    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex);
    }

    public String getName() {
        return title;
    }

    public void add(String value) {
        int row = rows.size();
        rows.add(value);
        fireTableRowsInserted(row, row);
    }

    public void remove(String name) {
        rows.remove(name);
        fireTableRowsDeleted(2, 3);
    }
}
