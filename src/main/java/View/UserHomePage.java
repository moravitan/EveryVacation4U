package View;

import Controller.Controller;
import Model.Flight;
import Model.PendingFlight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;
import java.util.Optional;

public class UserHomePage extends HomePage {

    private UpdateUserDetails updateUserDetailsWindow;
    private HomePage homePage;
    private SaleFlight saleFlight;
    private DisplaySearchedFlights displaySearchedFlights;
    private SearchUser searchUser;
    private MyFlightsHomePage myFlightsHomePage;
    private MyFlightsForSwap myFlightsForSwap;
    private FlightsWaitingForApproval flightsWaitingForApproval;
    private PendingToSwapMessages pendingToSwapMessages;
    private Controller controller;
    private Stage stage;


    public javafx.scene.control.Label lbl_user;
    public javafx.scene.control.TextField tf_origin;
    public javafx.scene.control.TextField tf_destination;
    public javafx.scene.control.DatePicker dp_arrival;
    public javafx.scene.control.DatePicker dp_departure;
    public javafx.scene.control.TextField tf_numOfTickets;

    public static int flightIdForSwap;

    public void setController(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        lbl_user.setText("שלום " + controller.getUserName());
        super.setImage();
        tooltip.setText("\nהכנס מיקום בפורמט:\n"+"עיר,מדינה"+"\n");
        tf_origin.setTooltip(tooltip);
        tf_destination.setTooltip(tooltip);
        displayAvailableFlights();


    }


    public void update(ActionEvent actionEvent) {
        newStage("UpdateUserDetails.fxml", "עדכון פרטים אישיים", updateUserDetailsWindow, 615, 490,controller);
    }

    public void delete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("כן");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("לא");
        alert.setContentText("האם אתה בטוח שברצונך למחוק את שם המשתמש מהמערכת?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            // Close program
                controller.deleteUser(controller.getUserName());
                alert("החשבון נמחק בהצלחה", Alert.AlertType.INFORMATION);
                stage.close();
                newStage("HomePage.fxml", "כניסת משתמש רשום", homePage, 716, 995,controller);
        } else {
            // ... user chose CANCEL or closed the dialog
            //windowEvent.consume();

        }
    }

    public void search(ActionEvent actionEvent) {
        if(tf_origin.getText()==null || tf_destination.getText()==null  || dp_departure.getValue()==null || dp_arrival.getValue() == null ) {
            alert("אופס! אחד או יותר משדות החיפוש ריקים", Alert.AlertType.ERROR);
            return;
        } else {
            int numberOfTickets = 0;
            //valid number (not empty)
            if (!tf_numOfTickets.getText().trim().equals("") && StringUtils.isNumeric(tf_numOfTickets.getText()))
                numberOfTickets = Integer.valueOf(tf_numOfTickets.getText());
            //invalid number (not empty)
            if (!tf_numOfTickets.getText().trim().equals("") && !StringUtils.isNumeric(tf_numOfTickets.getText())) {
                alert("אופס! הערך שהוזן במספר טיסות איננו תקין.", Alert.AlertType.ERROR);
                return;
            }
            if(!isValidChosenDate(dp_departure, dp_arrival) ) {
                alert("אנא הזן טווח תאריכים חוקי", Alert.AlertType.ERROR);
                return;
            }
            //empty, make default 1
            else if (tf_numOfTickets.getText().trim().equals("") || StringUtils.isNumeric(tf_numOfTickets.getText())) {
                //tf_numOfTickets.setText("1");
                numberOfTickets = 1;
                String dateDepart = controller.changeToRightDateFormat(dp_departure.getValue().toString());
                String dateArriv = controller.changeToRightDateFormat(dp_arrival.getValue().toString());
                System.out.println(dateDepart);
                System.out.println(dateArriv);
//                String dateDepart = dp_departure.getValue().toString();
//                String dateArriv = dp_arrival.getValue().toString();
                Flight flight = new Flight(tf_origin.getText(), tf_destination.getText(), dateDepart, dateArriv, numberOfTickets);
                if(!controller.setMatchesFlights(flight))
                    newStage("DisplaySearchedFlights.fxml", "", displaySearchedFlights, 896, 325, controller);
                else
                    alert("מתנצלים אך אין חופשה שתואמת את החיפוש שלך", Alert.AlertType.INFORMATION);
                //tf_numOfTickets.clear();
            }
        }


    }

    public void sellTickets(ActionEvent actionEvent) {
        newStage("SaleFlight.fxml", "יצירת חופשה", saleFlight, 760, 430,controller);
        //stage.close();


    }

    public void logOut(ActionEvent actionEvent){
        stage.close();
        controller.setUserName(null);
        newStage("HomePage.fxml", "כניסת משתמש רשום", homePage, 944, 650,controller);
    }

    public void searchUser(){
        newStage("SearchUser.fxml", "חיפוש משתמש", searchUser,364, 284, controller);
    }


    public void displayAvailableFlights(){
        origin.setCellValueFactory(new PropertyValueFactory<Flight,String>("origin"));
        destination.setCellValueFactory(new PropertyValueFactory<Flight,String>("destination"));
        price.setCellValueFactory(new PropertyValueFactory<Flight,String>("price"));
        DateOfDeparture.setCellValueFactory(new PropertyValueFactory<Flight,String>("dateOfDeparture"));
        DateOfArrival.setCellValueFactory(new PropertyValueFactory<Flight,String>("dateOfArrival"));
        numberOfTickets.setCellValueFactory(new PropertyValueFactory<Flight,String>("numOfTickets"));
        buy.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<Flight, String>, TableCell<Flight, String>> cellFactory
                = //
                new Callback<TableColumn<Flight, String>, TableCell<Flight, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Flight, String> param) {
                        final TableCell<Flight, String> cell = new TableCell<Flight, String>() {

                            final Button btn = new Button("רכש חופשה");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                this.setAlignment(Pos.CENTER);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        if (controller.getUserName() == null)
                                            alert("עלייך להתחבר למערכת תחילה", Alert.AlertType.ERROR);
                                        else {
                                            Flight flight = getTableView().getItems().get(getIndex());
                                            flight = controller.getFlight(flight.getFlightId());
                                            if (!controller.getUserName().equals(flight.getSeller())) {
                                                PendingFlight PF = new PendingFlight(flight.getFlightId(), flight.getSeller(), controller.getUserName());
                                                controller.insertPendingFlight(PF);
                                                controller.deleteAvailableFlight(Integer.valueOf(flight.getFlightId()));
                                                alert("בקשתך נשלחה למוכר", Alert.AlertType.CONFIRMATION);
                                                btn.setDisable(true);
                                                //   stage.close();
                                            }
                                            else {
                                                alert("אתה לא יכול לרכוש חופשה שבבעלותך", Alert.AlertType.ERROR);
                                            }
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        buy.setCellFactory(cellFactory);
        ObservableList<Flight> data = FXCollections.observableArrayList(controller.getAllAvailableFlights());
        flightBoard.setItems(data);

        origin1.setCellValueFactory(new PropertyValueFactory<Flight,String>("origin"));
        destination1.setCellValueFactory(new PropertyValueFactory<Flight,String>("destination"));
        price1.setCellValueFactory(new PropertyValueFactory<Flight,String>("price"));
        DateOfDeparture1.setCellValueFactory(new PropertyValueFactory<Flight,String>("dateOfDeparture"));
        DateOfArrival1.setCellValueFactory(new PropertyValueFactory<Flight,String>("dateOfArrival"));
        numberOfTickets1.setCellValueFactory(new PropertyValueFactory<Flight,String>("numOfTickets"));
        buy.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<Flight, String>, TableCell<Flight, String>> cellFactory1
                = //
                new Callback<TableColumn<Flight, String>, TableCell<Flight, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Flight, String> param) {
                        final TableCell<Flight, String> cell = new TableCell<Flight, String>() {

                            final Button btn = new Button("הגש בקשה להחלפה");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                this.setAlignment(Pos.CENTER);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        if (controller.getUserName() == null)
                                            alert("עלייך להתחבר למערכת תחילה", Alert.AlertType.ERROR);
                                        else {
                                            Flight flight = getTableView().getItems().get(getIndex());
                                            String owner = controller.readPurchasedFlightBuyer(flight.getFlightId());
                                            if (!controller.getUserName().equals(owner)) {
                                                flightIdForSwap = flight.getFlightId();
                                                newStage("MyFlightsForSwap.fxml", "החלפת חופשות", myFlightsForSwap, 896, 325, controller);
                                                //stage.close();
                                                btn.setDisable(true);
                                            }
                                            else{
                                                alert("אתה לא יכול להחליף חופשה שבבעלותך", Alert.AlertType.ERROR);

                                            }
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        buy1.setCellFactory(cellFactory1);
        ObservableList<Flight> data1 = FXCollections.observableArrayList(controller.readPendingToSwapFlights());
        exchangeBoard.setItems(data1);
    }


    public void readFlightsWaitForApproval(){
        newStage("FlightsWaitingForApproval.fxml", "אישור חופשות", flightsWaitingForApproval, 896, 325,controller);


    }

    public void swapFlights(){
        newStage("MyFlightsHomePage.fxml", "החלפת חופשות", myFlightsHomePage, 896, 325,controller);

    }

    public void readOffersRequest(){
        // pending to swap messages
        newStage("PendingToSwapMessages.fxml", "החלפת חופשות", pendingToSwapMessages, 907, 457,controller);

    }


}
