package com.edd.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {
    public MultiLineTableCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : "");
        setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
        int textLength = this.getText().length();
        int cols = this.getColumns()==0 ? 1 : this.getColumns();
        int lines = textLength / cols ;
        int height = fontHeight * lines;
        table.setRowHeight(row, height);
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }
}
