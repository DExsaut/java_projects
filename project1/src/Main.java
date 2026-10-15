import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static ComplexMatrix[] matrices = new ComplexMatrix[10];
    private static int count = 0;

    public static void main(String[] args) {
        while (true) {
            System.out.println();
            System.out.println("1. Создать матрицу");
            System.out.println("2. Выполнить операцию");
            System.out.println("3. Список матриц");
            System.out.println("0. Выход");
            System.out.print("Выбор: ");
            try {
                int choice = readInt();
                if (choice == 0) {
                    return;
                } else if (choice == 1) {
                    createMatrix();
                } else if (choice == 2) {
                    operation();
                } else if (choice == 3) {
                    list();
                } else {
                    System.out.println("Неизвестный пункт меню");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static int readInt() {
        String s = in.next();
        try {
            return Integer.parseInt(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ожидалось целое число: " + s);
        }
    }

    private static void createMatrix() {
        System.out.print("Число строк: ");
        int r = readInt();
        System.out.print("Число столбцов: ");
        int c = readInt();
        if (r <= 0 || c <= 0) {
            throw new IllegalArgumentException("Размеры матрицы должны быть положительными");
        }
        Complex[][] d = new Complex[r][c];
        System.out.println("Введите элементы построчно");
        System.out.println("Формат ввода (a, b - действ. числа): a, ai, a+bi, a-bi):");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                d[i][j] = parse(in.next());
            }
        }
        store(new ComplexMatrix(d));
        System.out.println("Матрица сохранена под номером " + count);
    }

    private static void operation() {
        System.out.println("1. Сложение");
        System.out.println("2. Умножение");
        System.out.println("3. Деление");
        System.out.println("4. Транспонирование");
        System.out.println("5. Определитель");
        System.out.print("Выбор: ");
        int op = readInt();
        if (op < 1 || op > 5) {
            throw new IllegalArgumentException("Неизвестная операция");
        }
        ComplexMatrix a = pick("Номер первой матрицы: ");
        if (op == 4) {
            save(a.transpose());
        } else if (op == 5) {
            System.out.println("Определитель: " + a.determinant());
        } else {
            ComplexMatrix b = pick("Номер второй матрицы: ");
            if (op == 1) {
                save(a.add(b));
            } else if (op == 2) {
                save(a.multiply(b));
            } else {
                save(a.divide(b));
            }
        }
    }

    private static ComplexMatrix pick(String prompt) {
        System.out.print(prompt);
        int n = readInt();
        if (n < 1 || n > count) {
            throw new IllegalArgumentException("Матрицы с номером " + n + " нет");
        }
        return matrices[n - 1];
    }

    private static void save(ComplexMatrix m) {
        store(m);
        System.out.println("Результат (матрица " + count + "):");
        System.out.println(m);
    }

    private static void store(ComplexMatrix m) {
        if (count == matrices.length) {
            ComplexMatrix[] bigger = new ComplexMatrix[count * 2];
            for (int i = 0; i < count; i++) {
                bigger[i] = matrices[i];
            }
            matrices = bigger;
        }
        matrices[count] = m;
        count++;
    }

    private static void list() {
        if (count == 0) {
            System.out.println("Нет сохранённых матриц");
            return;
        }
        for (int i = 0; i < count; i++) {
            System.out.println("Матрица " + (i + 1) + ":");
            System.out.println(matrices[i]);
        }
    }

    private static Complex parse(String text) {
        String s = text;
        try {
            if (s.endsWith("i")) {
                String body = s.substring(0, s.length() - 1);
                int idx = -1;
                for (int k = body.length() - 1; k > 0; k--) {
                    char ch = body.charAt(k);
                    char prev = body.charAt(k - 1);
                    if ((ch == '+' || ch == '-') && prev != 'e' && prev != 'E') {
                        idx = k;
                        break;
                    }
                }
                if (idx < 0) {
                    return new Complex(0, coef(body));
                }
                return new Complex(Double.parseDouble(body.substring(0, idx)), coef(body.substring(idx)));
            }
            return new Complex(Double.parseDouble(s), 0);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Не удалось разобрать число: " + text);
        }
    }

    private static double coef(String s) {
        if (s.length() == 0 || s.equals("+")) {
            return 1;
        }
        if (s.equals("-")) {
            return -1;
        }
        return Double.parseDouble(s);
    }
}