import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PolybiusCipher {

    public  char[] polishAlphabet =
            {
                    'A', 'Ą', 'B', 'C', 'Ć', 'D', 'E', 'Ę', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'Ł', 'M', 'N', 'Ń',
                    'O', 'Ó', 'P', 'R', 'S', 'Ś', 'T', 'Q', 'U', 'V' , 'W', 'X', 'Y', 'Z', 'Ź', 'Ż',
            };
    public char[][] publicArray = new char[5][7];

    public static char[][] setting_array(char[][] array, char[] polishAlphabet) {
        List<Character> alphabetList = Arrays.asList(toCharacterArray(polishAlphabet));
        Collections.shuffle(alphabetList); // Przetasuj listę

        int index = 0;
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[0].length; y++) {
                if (index < alphabetList.size()) {
                    array[x][y] = alphabetList.get(index);
                    index++;
                }
            }
        }
        return array; // Zwróć zmodyfikowaną tablicę
    }

    // Pomocnicza metoda do konwersji char[] na Character[]
    private static Character[] toCharacterArray(char[] array) {
        Character[] charArray = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            charArray[i] = array[i];
        }
        return charArray;
    }
    public String encrypt(char[][] array, String input, int jump, JTextArea encrypt_text) {
        StringBuilder result = new StringBuilder();
        char[] inputChars = input.toCharArray();

        for (char c : inputChars) {
            boolean found = false; // Flaga do sprawdzenia, czy znak został znaleziony
            for (int x = 0; x < array.length; x++) {
                for (int y = 0; y < array[0].length; y++) {
                    if (c == array[x][y]) {
                        int newX = (x + jump) % array.length;
                        result.append(newX);
                        result.append(y);
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (!found) {
                JOptionPane.showMessageDialog(null,"Podano jeden lub więcej znaków z poza słownika","Błąd",JOptionPane.ERROR_MESSAGE);
                encrypt_text.setText(""); // Wyczyść pole tekstowe (np. w oknie GUI)
                return ""; // Zwróć pusty wynik, jeśli wystąpił błąd
            }
        }
        return result.toString();
    }


    public String decrypt(char[][] array, String input, int jump, JTextArea decrypt_text) {
        StringBuilder result = new StringBuilder();
        // We assume the input string consists of pairs of digits representing coordinates (x, y)
        for (int z = 0; z < input.length(); z += 2) {
            // Ensure that we have a complete pair
            if (z + 1 >= input.length()) {
                JOptionPane.showMessageDialog(null, "Podano nie parzystą ilość cyfr", "Błąd", JOptionPane.ERROR_MESSAGE);
                decrypt_text.setText(""); // Clear the text area
                return ""; // Return empty result if there's an error
            }

            int x = Character.getNumericValue(input.charAt(z));     // X coordinate
            int y = Character.getNumericValue(input.charAt(z + 1)); // Y coordinate

            // Check if x and y are within valid range
            if (x < 0 || x >= array.length || y < 0 || y >= array[0].length) {
                JOptionPane.showMessageDialog(null, "Podano jeden lub więcej znaków z poza słownika", "Błąd", JOptionPane.ERROR_MESSAGE);
                decrypt_text.setText(""); // Clear the text area
                return ""; // Return empty result if there's an error
            }

            // Calculate the original position
            int originalX = (x - jump) % array.length;
            if(originalX < 0)
            {
                originalX+=array.length;
            }
            result.append(array[originalX][y]); // Append the character from the array
        }

        return result.toString();
    }




    public static void Application() {
        // Główne okno
        JFrame frame = new JFrame("Polybius Cipher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);

        // Panel główny z GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Etykieta "Szyfruj"
        JLabel encrypt_label = new JLabel("Szyfruj:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(encrypt_label, gbc);

        // pola tekstowe do szyfrowania
        JTextArea encrypt_text = new JTextArea();
        encrypt_text.setLineWrap(true);
        encrypt_text.setWrapStyleWord(true);
        JScrollPane encrypt_scroll = new JScrollPane(encrypt_text);
        encrypt_scroll.setPreferredSize(new Dimension(300, 60));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(encrypt_scroll, gbc);

        // Przycisk "Szyfruj"
        JButton encrypt_button = new JButton("Szyfruj");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(encrypt_button, gbc);

        // Etykieta dla liczby skoków
        JLabel jump_label = new JLabel("Liczba skoków:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(jump_label, gbc);

        // Pole tekstowe do liczby skoków
        JTextField jump_field = new JTextField("1", 3); // Domyślnie 1
        jump_field.setPreferredSize(new Dimension(50, 30));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.WEST;
        panel.add(jump_field, gbc);

        // Etykieta "Deszyfruj"
        JLabel decrypt_label = new JLabel("Deszyfruj:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(decrypt_label, gbc);

        // Pole tekstowe do deszyfrowania
        JTextArea decrypt_text = new JTextArea();
        decrypt_text.setLineWrap(true);
        decrypt_text.setWrapStyleWord(true);
        JScrollPane decrypt_scroll = new JScrollPane(decrypt_text);
        decrypt_scroll.setPreferredSize(new Dimension(300, 60));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(decrypt_scroll, gbc);

        //Przycisk "Deszyfruj"
        JButton decrypt_button = new JButton("Deszyfruj");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(decrypt_button, gbc);


        // Inicjalizacja instancji PolybiusCipher
        PolybiusCipher cipher = new PolybiusCipher();
        // Wywołanie funkcji do wypełnienia publicArray
        cipher.publicArray=cipher.setting_array(cipher.publicArray, cipher.polishAlphabet);
        // Ustawienie nasłuchiwacza akcji dla przycisku szyfruj
        encrypt_button.addActionListener(e -> {
            decrypt_text.setText("");
            String inputText = encrypt_text.getText().toUpperCase(); // Pobierz tekst z pola encrypt_text
            int jump = Integer.parseInt(jump_field.getText()); // Określ wartość skoku dla szyfrowania
            String encryptedText = cipher.encrypt(cipher.publicArray, inputText, jump,encrypt_text); // Wywołaj metodę encrypt
            decrypt_text.setText(encryptedText); // Ustaw zaszyfrowany tekst w polu decypher_text
        });
        decrypt_button.addActionListener(e -> {
            encrypt_text.setText("");
            String inputText = decrypt_text.getText().toUpperCase(); // Pobierz tekst z pola encrypt_text
            int jump = Integer.parseInt(jump_field.getText()); // Określ wartość skoku dla szyfrowania
            String decryptedText = cipher.decrypt(cipher.publicArray, inputText, jump,decrypt_text); // Wywołaj metodę encrypt
            encrypt_text.setText(decryptedText); // Ustaw zaszyfrowany tekst w polu decypher_text
        });


        //Panel do okna
        frame.getContentPane().add(panel);

        // Ustawienie okna na środku ekranu
        frame.setLocationRelativeTo(null);

        // Wyświetlenie okna
        frame.setVisible(true);

    }
    public static void main(String[] args) {
        // Uruchomianie aplikacji
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Application();
            }
        });
    }
}