import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MatrixEditor extends JFrame {
    // Создаем таблицы для матриц X и Y
    private JTable tableX, tableY;
    // Создаем текстовое поле для ввода значения
    private JTextField textField;
    // Создаем модели таблиц для матриц X и Y
    private DefaultTableModel modelX, modelY;
    // Определяем размер матриц (5x5)
    private final int n = 5;
    // Определяем значение x для инициализации матрицы X
    private final double x = 5;
    // Создаем двумерные массивы для матриц X и Y
    private double[][] matrixX, matrixY;

    public MatrixEditor() {
        // Инициализируем матрицы X и Y
        matrixX = new double[n][n];
        matrixY = new double[n][n];

        // Создаем массив строк для заголовков столбцов
        String[] columnNames = new String[n];
        for (int i = 0; i < n; i++) {
            // Инициализируем заголовки столбцов
            columnNames[i] = String.valueOf(i + 1);
        }

        // Создаем модель таблицы для матрицы X
        modelX = new DefaultTableModel(columnNames, n);
        // Создаем таблицу для матрицы X
        tableX = new JTable(modelX);
        // Включаем возможность выбора ячейки в таблице X
        tableX.setCellSelectionEnabled(true);
        // Устанавливаем редактор ячейки по умолчанию для таблицы X
        tableX.setDefaultEditor(Object.class, null);

        // Создаем модель таблицы для матрицы Y
        modelY = new DefaultTableModel(columnNames, n);
        // Создаем таблицу для матрицы Y
        tableY = new JTable(modelY);
        // Отключаем редактирование таблицы Y
        tableY.setEnabled(false);

        // Инициализируем матрицу X
        initMatrixX();

        // Создаем текстовое поле для ввода значения
        textField = new JTextField(10);

        // Добавляем слушатель мыши для таблицы X
        tableX.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {
                // Получаем координаты ячейки, на которую нажали
                int row = tableX.rowAtPoint(e.getPoint());
                int col = tableX.columnAtPoint(e.getPoint());
                if (row != -1 && col != -1) {
                    // Выводим значение ячейки в текстовое поле
                    textField.setText(String.valueOf(matrixX[row][col]));
                }
            }

            // Методы для обработки событий мыши
            public void mouseReleased(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        // Создаем панель для верхней части окна
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Создаем панель для верхней части окна
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Матрица X:"));
        // Добавляем таблицу X на панель
        topPanel.add(new JScrollPane(tableX));
        // Добавляем метку "Value:" на панель
        topPanel.add(new JLabel("Value:"));
        // Добавляем текстовое поле на панель
        topPanel.add(textField);

        // Создаем кнопку "Save"
        JButton saveButton = new JButton("Save");
        // Добавляем слушатель события для кнопки "Save"
        saveButton.addActionListener(e -> {
            // Получаем координаты ячейки, на которую нажали
            int row = tableX.getSelectedRow();
            int col = tableX.getSelectedColumn();
            if (row != -1 && col != -1) {
                try {
                    // Парсим значение из текстового поля
                    double value = Double.parseDouble(textField.getText());
                    // Обновляем значение ячейки в матрице X
                    matrixX[row][col] = value;
                    // Обновляем значение ячейки в модели таблицы X
                    modelX.setValueAt(value, row, col);
                } catch (NumberFormatException ex) {
                    // Выводим сообщение об ошибке, если ввод не является числом
                    JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Добавляем кнопку "Save" на панель
        topPanel.add(saveButton);

        // Создаем кнопку "Calculate"
        JButton calculateButton = new JButton("Calculate");
        // Добавляем слушатель события для кнопки "Calculate"
        calculateButton.addActionListener(e -> calculateMatrixY());
        // Добавляем кнопку "Calculate" на панель
        topPanel.add(calculateButton);

        // Добавляем панель на основную панель
        panel.add(topPanel);
        
        topPanel.add(new JLabel("Матрица Y:"));
        // Добавляем таблицу Y на основную панель
        topPanel.add(new JScrollPane(tableY));

        // Добавляем основную панель на окно
        add(panel);
        // Выводим окно на экран
        setVisible(true);
    }

    // Метод для инициализации матрицы X
    private void initMatrixX() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Инициализируем матрицу X с помощью функции Math.pow
                matrixX[i][j] = Math.pow(x, i + 1);
                // Устанавливаем значение ячейки в модели таблицы X
                modelX.setValueAt(matrixX[i][j], i, j);
            }
        }

        // Устанавливаем модель таблицы X для таблицы X
        tableX.setModel(modelX);
    }

    // Метод для расчета матрицы Y
    private void calculateMatrixY() {
        for (int i = 0; i < modelX.getRowCount(); i++) {
            for (int j = 0; j < modelX.getColumnCount(); j++) {
                // Вычисляем новый индекс столбца для матрицы Y
                int newJ = (j + 1) % modelX.getColumnCount();
                // Получаем значение ячейки из модели таблицы X
                Object item = modelX.getValueAt(i, j);
                // Устанавливаем значение ячейки в модели таблицы Y
                modelY.setValueAt(item, i, newJ);
            }
        }

        tableY.setModel(modelY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MatrixEditor());
    }
}