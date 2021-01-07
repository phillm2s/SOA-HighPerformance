package com.DataBaseRestApi.DataBaseRestApi;

import com.DataBaseRestApi.DataBaseRestApi.Model.Evaluation;
import com.DataBaseRestApi.DataBaseRestApi.Model.OrderEvaluation;
import com.DataBaseRestApi.DataBaseRestApi.Model.SocialEvaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnection {
    private static DataBaseConnection instance;

    String url = "jdbc:mysql://localhost:3306/hpevaluations";
    String username = "hpadmin";
    String password = "admin";

    private Connection connection;

    private DataBaseConnection(){ }

    public static DataBaseConnection getInstance(){
        if (DataBaseConnection.instance==null) {
            DataBaseConnection.instance = new DataBaseConnection();
        }
        return DataBaseConnection.instance;
    }

    public void loadDBDriver(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
    }

    public void connect(){
        System.out.println("Connecting database...");
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public void close() {
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private ResultSet executeQuery(String sqlQuery) throws SQLException {
        return connection.createStatement().executeQuery(sqlQuery);
    }

    private int executeManipulation(String sqlQuery) throws SQLException {
        return connection.createStatement().executeUpdate(sqlQuery);
    }



    public Evaluation getFullEvaluation(int employeeID, String year) throws SQLException, NoSuchFieldException {
        //get Evaluation
        String sql = "SELECT `evaluationID`, `employeeID`, `openCrxEmployeeRef` , `employeeName`, `year`, `comment`, `totalBonus`, `department` \n" +
                "FROM `evaluations` \n" +
                "WHERE employeeID="+employeeID+"\n" +
                "AND year=\""+year+"\";";

        ResultSet res = executeQuery(sql);
        if(!res.next())
            throw new NoSuchFieldException("There is no Evaluation for employeeID: "+employeeID+" year: "+ year);

        Evaluation evaluation = new Evaluation();
        evaluation.setEvaluationID(res.getInt("evaluationID"))
                .setEmplyeeID(employeeID)
                .setOpenCRXEmployeeRef(res.getString("openCrxEmployeeRef"))
                .setDepartment(res.getString("department"))
                .setFullName(res.getString("employeeName"))
                .setYear(year)
                .setComment(res.getString("comment"))
                .setBonus(res.getString("totalBonus"));


        //get social Evaluations by reference
        sql = "SELECT `socialEvaluationID`, `evaluationID`, `description`, `targetValue`, `actualValue`, `bonus`, `comment` \n" +
                "FROM `socialevaluations` \n" +
                "WHERE evaluationID="+evaluation.evaluationID+";";
        res = executeQuery(sql);
        ArrayList<SocialEvaluation> socialEvaluation = new ArrayList<SocialEvaluation>();
        while(res.next()) {
            socialEvaluation.add(new SocialEvaluation()
                    .setSocialEvaluationID(res.getInt("socialEvaluationID"))
                    .setDescription(res.getString("description"))
                    .setTargetValue(res.getString("targetValue"))
                    .setActualValue(res.getString("actualValue"))
                    .setBonus(res.getString("bonus"))
                    .setComment(res.getString("comment")));
        }
        evaluation.setSocialEvaluations(socialEvaluation);

        //get order Evaluations by reference
        sql = "SELECT `orderEvaluationID`, `evaluationID`, `productName`, `clientName`, `clientRanking`, `itemAmount`, `price` , `bonus`, `comment` \n" +
                "FROM `orderevaluations` \n" +
                "WHERE evaluationID="+evaluation.evaluationID+";";
        res = executeQuery(sql);
        ArrayList<OrderEvaluation> orderEvaluations = new ArrayList<OrderEvaluation>();

        while(res.next()){
            orderEvaluations.add( new OrderEvaluation()
                    .setOrderEvaluationID(res.getInt("orderEvaluationID"))
                    .setProductName(res.getString("productName"))
                    .setClientName(res.getString("clientName"))
                    .setClientRanking(res.getString("clientRanking"))
                    .setAmount(res.getString("itemAmount"))
                    .setPrice(res.getString("price"))
                    .setBonus(res.getString("bonus"))
                    .setComment(res.getString("comment")));
        }
        evaluation.setOrderEvaluations(orderEvaluations);

        return evaluation;
    }

    public List<Evaluation> getAllMinimalEvaluations(String year) throws SQLException {
        String sql = "SELECT `evaluationID`, `employeeID`, `openCrxEmployeeRef` , `employeeName`, `year`, `comment`, `totalBonus`, `department` \n" +
                "FROM `evaluations`\n" +
                "WHERE year = \""+year+"\";";

        return getAllEvaluationsViaSQL(sql);
    }

    public List<Evaluation> getAllMinimalEvaluations() throws SQLException {
        String sql = "SELECT `evaluationID`, `employeeID`, `openCrxEmployeeRef` , `employeeName`, `year`, `comment`, `totalBonus`, `department` \n" +
                "FROM `evaluations`;";

        return getAllEvaluationsViaSQL(sql);
    }

    public void updateSocialEvaluation(SocialEvaluation socialEvaluation) throws SQLException {
        String sql= "UPDATE `socialevaluations` \n" +
                "SET `description`=\""+socialEvaluation.description +"\",\n" +
                "`targetValue`=\""+socialEvaluation.targetValue+"\",\n" +
                "`actualValue`=\""+socialEvaluation.actualValue+"\",\n" +
                "`bonus`=\""+socialEvaluation.bonus+"\",\n" +
                "`comment`=\""+socialEvaluation.comment+"\"\n" +
                "WHERE socialEvaluationID="+socialEvaluation.socialEvaluationID+";";
        executeManipulation(sql);
        System.out.println("SocialEvaluation ( id: "+socialEvaluation.socialEvaluationID+" ) updated successfully.");
    }

    private void insertOrderEvaluation(OrderEvaluation orderEvaluation, int evaluationID) throws SQLException {
        String sql= "INSERT INTO `orderevaluations`(`evaluationID`, `productName`, `clientName`, `clientRanking`, `itemAmount`, `price`, `bonus`, `comment`) " +
                "VALUES ("+evaluationID+",'"+orderEvaluation.productName+"','"+orderEvaluation.clientName+"','"+orderEvaluation.clientRanking+
                "','"+orderEvaluation.amount+"','"+orderEvaluation.price+"','"+orderEvaluation.bonus+"','"+orderEvaluation.comment+"' );";

        executeManipulation(sql);
        System.out.println("OrderEvaluation with ProductName:"+ orderEvaluation.productName +", clientName:" + orderEvaluation.clientName +" successful.");
    }

    public void updateFullEvaluation(Evaluation evaluation) throws SQLException {
        String sqlEval= "UPDATE `evaluations` SET `comment`=\""+evaluation.comment+"\",`totalBonus`=\""+evaluation.bonus+
                "\",`signatureHR`=\""+evaluation.signatureHR+"\",`signatureCEO`=\""+evaluation.signatureCEO+
                "\",`bonusOrderEvaluation`=\""+evaluation.totalBonusOrderEvaluation+"\",`bonusSocialEvaluation`=\""+evaluation.totalBonusSocialEvaluation+"\" \n" +
                "WHERE `evaluationID`="+evaluation.evaluationID+";";

        executeManipulation(sqlEval);
        System.out.println("Basic Evaluation Infos with ID: "+evaluation.evaluationID+" , Name: "+evaluation.fullName+" successfully updated.");


        //delete all "old" orderevaluations depending to current evaluations
        String sqlDeleteOrderEvaluations = "DELETE \n" +
                "FROM `orderevaluations` \n" +
                "WHERE `evaluationID`="+evaluation.evaluationID+";";

        executeManipulation(sqlDeleteOrderEvaluations);


        //INSERT all new orderEvaluations
        for (OrderEvaluation oe : evaluation.orderEvaluations) {
            insertOrderEvaluation(oe, evaluation.evaluationID);
        }



        //update all socualEvaluaitons
        for (SocialEvaluation se : evaluation.socialEvaluations) {
            updateSocialEvaluation(se);
        }

        System.out.println("Complete Evaluation with ID: "+evaluation.evaluationID+" , Name: "+evaluation.fullName+" successfully updated.\n");

    }

    public void createNewEvaluation(int employeeID, String employeeFullName, String year, String department) throws SQLException {
        //check if there is an existing evaluation
        String sqlEmployeeID ="SELECT evaluationID \n" +
                "FROM evaluations\n" +
                "WHERE employeeID="+employeeID+"\n" +
                "AND year=\""+year+"\";\n";
        ResultSet res = executeQuery(sqlEmployeeID);
        if(res.next())
            throw new IllegalArgumentException("Dataset (employeeID: "+employeeID+", year: "+year+") already exist but should be unique. ");


        //insert into Evaluation
        String sql= "INSERT INTO `evaluations`(employeeID, employeeName, year, department) \n" +
                "VALUES (\""+employeeID+"\", \""+employeeFullName+"\", \""+year+"\", \""+department+"\");";
        executeManipulation(sql);

        //get the evlautionID from above insert
        res = executeQuery(sqlEmployeeID);
        res.next();
        int evaluationID = res.getInt("evaluationID");

        //add an emty socialEvaluation for each existing evaluationType(inside DB) in reference to this Evaluation
        sql = "SELECT description \n" +
                "FROM evaluationtypes;";
        res = executeQuery(sql);
        while(res.next()){
            String description= res.getString("description");
            sql= "INSERT INTO `socialevaluations`(`evaluationID`, `description`) \n" +
                    "VALUES ("+evaluationID+", \""+description+"\");";
            executeManipulation(sql);
        }
        System.out.println("Succsessfully created new Evaluation for "+employeeFullName);
    }

    public void addOrderEvaluation(OrderEvaluation orderEvaluation, int employeeID, String year) throws SQLException {
        //id will be ignored (autoincremented)
        //check if there is an existing evaluation for given employee and year
        String sql ="SELECT evaluationID \n" +
                "FROM evaluations\n" +
                "WHERE employeeID="+employeeID+"\n" +
                "AND year=\""+year+"\";\n";
        ResultSet res = executeQuery(sql);
        if(!res.next())
            throw new IllegalArgumentException("Dataset (employeeID: "+employeeID+", year: "+year+") don´t exist.");
        int evaluationID = res.getInt("evaluationID");
        sql = "INSERT INTO `orderevaluations`\n" +
                "(`evaluationID`, `productName`, `clientName`, `clientRanking`, `itemAmount`, `bonus`, `comment`) \n" +
                "VALUES ("+evaluationID+", \""+orderEvaluation.productName+"\",\""+orderEvaluation.clientName+"\",\""+orderEvaluation.clientRanking+
                "\",\""+orderEvaluation.amount+"\",\""+orderEvaluation.bonus+"\",\""+orderEvaluation.comment+"\");";
        executeManipulation(sql);
        System.out.println("Orderevaluation successfuly added to Evaluation " +
                "(evaluationID: "+evaluationID +", employeeID: "+employeeID+ ", year: "+ year+")");
    }

    public void deleteOrderEvaluation(int orderEvaluationID){

    }



    private List<Evaluation> getAllEvaluationsViaSQL(String fullSQLStatement) throws SQLException {
        ResultSet res = executeQuery(fullSQLStatement);
        List<Evaluation> evaluations = new ArrayList<Evaluation>();
        while(res.next()) {
            evaluations.add(new Evaluation()
                    .setEvaluationID(res.getInt("evaluationID"))
                    .setEmplyeeID(res.getInt("employeeID"))
                    .setOpenCRXEmployeeRef(res.getString("openCrxEmployeeRef"))
                    .setFullName(res.getString("employeeName"))
                    .setYear(res.getString("year"))
                    .setComment(res.getString("comment"))
                    .setBonus(res.getString("totalBonus"))
                    .setDepartment(res.getString("department")));

        }
        return evaluations;
    }
}
