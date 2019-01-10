package View;

import Controller.Controller;
import Model.RegisteredUser;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class UpdateUserDetails extends HomePage implements Observer {

    private Controller controller;
    private Stage stage;
    private RegisteredUser registeredUser;

    public javafx.scene.control.TextField txtfld_userName;
    public javafx.scene.control.PasswordField txtfld_password;
    public javafx.scene.control.PasswordField txtfld_confirmPassword;
    public javafx.scene.control.TextField txtfld_firstName;
    public javafx.scene.control.TextField txtfld_lastName;
    public javafx.scene.control.TextField txtfld_Address;
    public javafx.scene.control.TextField txtfld_email;
    public javafx.scene.control.ComboBox combo_box_day;
    public javafx.scene.control.ComboBox combo_box_month;
    public javafx.scene.control.ComboBox combo_box_year;


    /**
     *
     * @param controller
     * @param stage
     */
    public void setController(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }


    /**
     * This method set the userDetails field
     * @param registeredUser
     */
    public void setUserDetails(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
        splitToFields();
    }


    /**
     * This method split the data from the database into fields and initials the Text Fields in the
     * updateUser window
     */
    private void splitToFields(){
        txtfld_userName.setText(registeredUser.getUserName());
        txtfld_firstName.setText(registeredUser.getFirstName());
        txtfld_lastName.setText(registeredUser.getLastName());
        txtfld_password.setText(registeredUser.getPassword());
        txtfld_confirmPassword.setText(registeredUser.getPassword());
        txtfld_Address.setText(registeredUser.getAddress());
        txtfld_email.setText(registeredUser.getEmail());
        String [] date = registeredUser.getBirthday().split("/");
        combo_box_day.setValue(date[0]);
        combo_box_month.setValue(date[1]);
        combo_box_year.setValue(date[2]);

    }

    /**
     *
     */
    public void submit (){
        if (!validation()){
            alert("שדה אחד או יותר ריקים", Alert.AlertType.INFORMATION);
            return;
        }
        else {
            String newUserName = txtfld_userName.getText().trim();
            String newPassword = txtfld_password.getText().trim();
            String newPasswordReplay = txtfld_confirmPassword.getText().trim();
            String newFirstName = txtfld_firstName.getText().trim();
            String newLastName = txtfld_lastName.getText().trim();
            String newBirthday = getBirthday().trim();
            String newAddress = txtfld_Address.getText().trim();
            String newEmail = txtfld_email.getText().trim();
            RegisteredUser updateRegisteredUser = new RegisteredUser(newUserName,newPassword,newFirstName,newLastName,newBirthday,newAddress,newEmail);
            String ans = controller.updatedDB(registeredUser.getUserName(), updateRegisteredUser,newPasswordReplay);
            if (!ans.equals("פרטי החשבון עודכנו בהצלחה")) {
                alert(ans, Alert.AlertType.ERROR);
                return;
            }else{
                alert(ans, Alert.AlertType.INFORMATION);
                stage.close();
            }

        }
    }


    /**
     *
     * @return
     */
    private String getBirthday(){
        String newDay = (String) combo_box_day.getValue();
        String newMonth = (String) combo_box_month.getValue();
        String newYear = (String) combo_box_year.getValue();
        return newDay  + "/" + newMonth + "/" + newYear;
    }

    /**
     * if the registeredUser clicked on cancel, close the program
     * @param actionEvent
     */
    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }

    /**
     * This method checks if the registeredUser filled all the Text Fields
     * @return true if the registeredUser filled all the Text Fields, otherwise return false
     */
    private boolean validation() {
        if (txtfld_userName.getText() == null || txtfld_userName.getText().trim().isEmpty() || txtfld_userName.getText().trim().equals(""))
            return false;
        if (txtfld_password.getText() == null || txtfld_password.getText().trim().isEmpty() || txtfld_password.getText().trim().equals(""))
            return false;
        if (txtfld_confirmPassword.getText() == null || txtfld_confirmPassword.getText().trim().isEmpty() || txtfld_confirmPassword.getText().trim().equals(""))
            return false;
        if (txtfld_firstName.getText() == null || txtfld_firstName.getText().trim().isEmpty() || txtfld_firstName.getText().trim().equals(""))
            return false;
        if (txtfld_lastName.getText() == null || txtfld_lastName.getText().trim().isEmpty() ||  txtfld_lastName.getText().trim().equals(""))
            return false;
        if (txtfld_Address.getText() == null || txtfld_Address.getText().trim().isEmpty() || txtfld_Address.getText().trim().equals(""))
            return false;
        if (txtfld_email.getText() == null || txtfld_email.getText().trim().isEmpty() || txtfld_email.getText().trim().equals(""))
            return false;
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {

    }


}
