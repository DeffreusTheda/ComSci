/* ******************************************************************************
 * Copyright (c) 2024 Deffreus Theda
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/

package org.effie.payroll;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    // Login Menu
    @FXML private TextField tfUsername;
    @FXML private PasswordField pfPassword;
    @FXML private Label lWarning;
    @FXML private TextField tfPassword;
    @FXML private CheckBox cbToggleShowPassword;
    // Payroll Menu
    @FXML private TextField employeeCodeField;
    @FXML private TextField employeeNameField;
    @FXML private ComboBox<String> employeeGradeCombo;
    @FXML private RadioButton marriedRadioButton;
    @FXML private TextField childrenCountField;
    @FXML private TextArea resultArea;

    private final Map<String, Double> gajiGolongan = new HashMap<>();

    private boolean validasiKode(String kode) {
        char[] carr = kode.toCharArray();
        if ( kode.length() != 6 ) {
            return false;
        }
        for ( int i = 0; i < 3; ++i ) {
            if ( carr[i] < 65 || carr[i] > 90 ) {
                return false;
            }
        }
        for ( int i = 3; i < 6; ++i ) {
            if ( carr[i] < 48 || carr[i] > 57 ) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void calculateSalary() {
        gajiGolongan.clear();
        gajiGolongan.put("ia", 1948300.0);
        gajiGolongan.put("ib", 2088700.0);
        gajiGolongan.put("ic", 2177050.0);
        gajiGolongan.put("id", 2269150.0);
        gajiGolongan.put("iia", 2697900.0);
        gajiGolongan.put("iib", 2862350.0);
        gajiGolongan.put("iic", 2983400.0);
        gajiGolongan.put("iid", 3109600.0);
        gajiGolongan.put("iiia", 3407900.0);
        gajiGolongan.put("iiib", 3552050.0);
        gajiGolongan.put("iiic", 3702350.0);
        gajiGolongan.put("iiid", 3858900.0);
        gajiGolongan.put("iva", 4022150.0);
        gajiGolongan.put("ivb", 4192300.0);
        gajiGolongan.put("ivc", 4369600.0);
        gajiGolongan.put("ivd", 4554450.0);
        gajiGolongan.put("ive", 4747150.0);

        try {
            final String namaKaryawan = employeeNameField.getText().toUpperCase();
            final String kode = employeeCodeField.getText();
            final double gajiPokok = gajiGolongan.get(employeeGradeCombo.getValue());
            final double tunjanganIstri = 150000.0 * ((marriedRadioButton.isSelected()) ? 1 : 0);
            final double tunjanganAnak = 75000.0 * Integer.parseInt(childrenCountField.getText());
            final double gajiKotor = gajiPokok + tunjanganIstri + tunjanganAnak;
            final double gajiBersih = gajiKotor * 0.85;

            if ( ! validasiKode(kode) ) {
                resultArea.setText("KODE KARYAWAN TIDAK VALID! PALSU!");
                return;
            }

            resultArea.setText(String.format("NAMA KARYAWAN   : %s (KODE: %s)\n", namaKaryawan, kode));
            resultArea.appendText(String.format("GAJI POKOK      : %.1f\n", gajiPokok));
            resultArea.appendText(String.format("TUNJANGAN ISTRI : %.1f\n", tunjanganIstri));
            resultArea.appendText(String.format("TUNJANGAN ANAK  : %.1f\n", tunjanganAnak));
            resultArea.appendText(String.format("TOTAL TUNJANGAN : %.1f\n", tunjanganAnak + tunjanganIstri));
            resultArea.appendText(String.format("GAJI KOTOR      : %.1f\n", gajiKotor));
            resultArea.appendText(String.format("POTONGAN        : %.1f\n", gajiKotor * 0.15));
            resultArea.appendText(String.format("GAJI BERSIH     : %.1f\n", gajiBersih));
        } catch (Exception e) {
            resultArea.setText("INPUT IS NOT VALID! CHECK BACK! LITERACY!");
        }
    }

    // Login Page
    @FXML
    protected void signIn() {
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        if (username.isEmpty() && password.isEmpty()) {
            lWarning.setText("Login successful!");
            EmployeePayroll.loginUser("Anonymous", "");
            return;
        }
        for (int i = 0; i < EmployeePayroll.account.size(); i++) {
            Pair<String, String> iAccount = EmployeePayroll.account.get(i);
            if (username.equals(iAccount.getKey()) && password.equals(iAccount.getValue())) {
                lWarning.setText("Login successful!");
                EmployeePayroll.loginUser(username, password);
                return;
            }
        }
        lWarning.setText("Incorrect username or password.");
    }

    @FXML
    protected void signUp() {
        lWarning.setText("Register successful!");
        EmployeePayroll.account.add(new Pair<>(tfUsername.getText(), pfPassword.getText()));
        EmployeePayroll.loginUser(tfUsername.getText(), pfPassword.getText());
    }

    @FXML
    protected void toggleShowPassword() {
        if (cbToggleShowPassword.isSelected()) {
            tfPassword.setText(pfPassword.getText());
            pfPassword.setVisible(false);
            tfPassword.setVisible(true);
        } else {
            pfPassword.setText(tfPassword.getText());
            tfPassword.setVisible(false);
            pfPassword.setVisible(true);
        }
    }
}