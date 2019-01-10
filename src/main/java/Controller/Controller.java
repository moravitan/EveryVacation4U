package Controller;


import Model.UserModel;
import Model.FlightModel;
import Model.Flight;
import Model.RegisteredUser;
import Model.PurchasedFlight;
import Model.PendingFlight;
import Model.ConfirmedFlight;
import Model.*;

import java.util.*;

public class Controller extends Observable implements Observer {

    private UserModel userModel;
    private FlightModel flightModel;
    public static String currentUserName;
    private ArrayList<Flight> flightMatchSearches;



    /**
     * Constructor for the class Controller
     * @param userModel
     */
    public Controller(UserModel userModel, FlightModel flightModel) {
        this.userModel = userModel;
        this.flightModel = flightModel;
    }

    /**
     *
     * @param registeredUser
     * This method insertUser a new row to the data base with the given parameters
     */
    public String insertUser(RegisteredUser registeredUser, String confirmPassword) {
        return userModel.createUser(registeredUser,confirmPassword);

    }

    /**
     * This method searchUser and return the row in the database which is equal to the given userName
     * @param userName
     * @return the row
     */
    public RegisteredUser readUsers(String userName, Boolean isInsert){
        return userModel.searchUsers(userName,isInsert);
    }


    public String updatedDB(String oldUserName, RegisteredUser registeredUser, String confirmPassword){
        return userModel.updateUser(oldUserName, registeredUser,confirmPassword);
    }
    /**
     * This method deleteUser a row from the data base where the primary key is equal to @param userName
     * @param userName
     */
    public void deleteUser(String userName){
        userModel.deleteUser(userName);
    }

    public ArrayList<Flight> readPendingFlights(String sellerUserName){
        return flightModel.readPendingFlights(sellerUserName);
    }

    public ArrayList<Flight> readConfirmedFlights(String buyerUserName){
        return flightModel.readConfirmedFlights(buyerUserName);
    }

    public ArrayList<Flight> readConfirmedFlightsSeller(String sellerUserName){
        return flightModel.readConfirmedFlightsSeller(sellerUserName);
    }

    public String readConfirmedFlightBuyer(int flightId){
        return flightModel.readConfirmedFlightBuyer(flightId);
    }

    public void deletePendingFlight(int flightId){
        flightModel.deletePendingFlight(flightId);
    }
    public void insertConfirmedFlight(ConfirmedFlight CFlight){
        flightModel.insertConfirmedFlight(CFlight);
    }

    public void insertPurchasedFlight(PurchasedFlight flight){
        flightModel.insertPurchasedFlight(flight);
    }

    public String readPurchasedFlightBuyer(int flightId){
        return flightModel.readPurchasedFlightBuyer(flightId);
    }

    @Override
    public void update(Observable o, Object arg) {
        /// check if has usage!!!!!!!
        if (o == userModel){
            setChanged();
            notifyObservers(arg);
        }

    }

    public void insertFlight(Flight Data){
        flightModel.insertFlight(Data);
    }

    public Flight getFlight(int flightId){
        return flightModel.getFlight(flightId);
    }

    public int getflightID(){
        return flightModel.getFlightID();
    }

    public String signIn(String userName, String password){
        currentUserName = userName;
        return userModel.signIn(userName,password);
    }

    /**
     * return true if size is 0, else return false
     * @return
     */
    public boolean setMatchesFlights(Flight flight){
        flightMatchSearches = new ArrayList<Flight>();
        flightMatchSearches = flightModel.getMatchesFlights(flight);
        if(flightMatchSearches.size()==0)
            return true;
        return false;
    }

    public ArrayList<Flight> getMatchesFlights(){
        if(flightMatchSearches !=null)
            return flightMatchSearches;
        return null;
    }



    public void deleteAvailableFlight(int vacationId){
        flightModel.deleteAvailableFlight(vacationId);
    }

    public String readPendingFlightBuyer(int VacationId){
        return flightModel.readPendingFlightBuyer(VacationId);
    }

    public String getUserName() {
        return currentUserName;
    }

    public void insertPendingFlight(PendingFlight pFlight){
        flightModel.insertPendingFlight(pFlight);
    }


    public void deleteConfirmedFlight(int vacationID) {
        flightModel.deleteConfirmedFlight(vacationID);
    }

        /**
         * FROM:YYYY-MM-DD  TO:DD/MM/YY
         * @param dataPickerValue YYYY-MM-DD
         * @return DD/MM/YY
         */
    public String changeToRightDateFormat(String dataPickerValue){
        String[] arr = new String[3];
        String str = dataPickerValue.substring(0,4);
        arr[2] = str;
        str = dataPickerValue.substring(5,7);
        arr[1] = str;
        str = dataPickerValue.substring(8);
        arr[0] = str;
        String RightDateFormat = arr[0] + "/" + arr[1] + "/" + arr[2] ;
        return RightDateFormat;
    }

    public void setUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public ArrayList<Flight> getAllAvailableFlights() {
        ArrayList<Flight> availableFlights = new ArrayList<Flight>();
        availableFlights=flightModel.readAllAvailableFlights();
        return availableFlights;
    }


    public ArrayList<Flight> readPendingToSwapFlights(){
        return flightModel.readPendingToSwapFlights();
    }

    public void deletePendingToSwapFlight(int flightId){
        flightModel.deletePendingToSwapFlight(flightId);
    }

    public void insertPendingToSwapFlight(PendingToSwapFlight pendingToSwapFlight){
        flightModel.insertPendingToSwapFlight(pendingToSwapFlight);
    }

    public void insertOfferToSwapFlight(OfferedToSwapFlight offeredToSwapFlight){
        flightModel.insertOfferToSwapFlight(offeredToSwapFlight);
    }

    public TreeMap<Integer, Vector<Flight>> readOfferedToSwapFlights(String userName){
        return flightModel.readOfferToSwapFlights(userName);
    }

    public void deleteOfferToSwapFlight(int flightIdPending, int flightIdChosen){
        flightModel.deleteOfferToSwapFlight(flightIdPending,flightIdChosen);
    }

    public void insertSwappedFlight(SwappedFlight swappedFlight){
        flightModel.insertSwappedFlights(swappedFlight);
    }

    public void readPendingToSwapFlightBuyer(){

    }

    public ArrayList<Flight> getFlightsToSwap(String userName){
        return flightModel.getFlightsToSwap(userName);
    }

    public ArrayList<Flight> getFlightsToSwapHomePage(String userName){
        return flightModel.getFlightsToSwapHomePage(userName);
    }

    public void insertAvailableFlight(Flight flight) {
        flightModel.insertAvailableFlight(flight);
    }
}
