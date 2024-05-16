package edu.sdccd.cisc191.template;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Conversions {
    private Stage stage;
    /**
     * allows the user to import a previously saved schedule in the form of a csv file
     *
     * @return subject array to be imported
     */
    public ArrayList<Subject> convertCSVFileToSubject() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files", "*.TXT"),
                new FileChooser.ExtensionFilter("txt files", "*.txt"));
        File inFile = fc.showOpenDialog(null);
        ArrayList<Subject> subjectSave = new ArrayList<>();
        if (inFile != null) {
            try {
                Scanner inputStream = new Scanner(inFile);
                while (inputStream.hasNextLine()) {
                    ArrayList<Assignment> assignments = new ArrayList<>();
                    String temp = inputStream.nextLine();
                    String[] tokens = temp.split(",");
                    Subject tempSubject = new Subject(tokens[0], Boolean.parseBoolean(tokens[2]), Double.parseDouble(tokens[1]));
                    tempSubject.setColor(Integer.parseInt(tokens[3]));
                    for (int i = 4; i < tokens.length; i++) {
                        assignments.add(new Assignment(tokens[i]));
                    }
                    tempSubject.setAssignmentList(assignments);
                    subjectSave.add(tempSubject);
                }
            } catch (FileNotFoundException e) {
                e.getMessage();
            }
        } else {
            System.out.println("Invalid file");
        }
        return subjectSave;
    }

    public void convertSubjectToCSV(ArrayList<Subject> subjectArrayList, ConcurrentLinkedDeque<Subject> stack) {
        FileChooser.ExtensionFilter availableFiles = new FileChooser.ExtensionFilter("txt files", "*.txt");
        FileChooser fc = new FileChooser();
        try {

            Database database = new Database();

            Schedule schedule = new Schedule(stack);

            database.createSchedule(schedule);

            while (stack.peekFirst() != null)
                System.out.println("running in Thread " + Thread.currentThread().getName());
                database.create(stack.pollFirst(), schedule);
            //grab all subjects out of the stack

            // Commit the transaction `
            database.getConnection().commit();
            database.getConnection().setAutoCommit(true);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        fc.setTitle("Save Schedule");
        fc.setInitialFileName("My_Schedule.txt");
        fc.getExtensionFilters().add(availableFiles);

        File saveLocation = fc.showSaveDialog(stage);

        if (saveLocation != null) {
            try (FileWriter writer = new FileWriter(saveLocation)) {
                for (Subject subject : subjectArrayList) {
                    writer.append(subject.getNameOfSubject())
                            .append(',')
                            .append(String.valueOf(subject.getGradeInClass()))
                            .append(',')
                            .append(String.valueOf(subject.isWeighted()))
                            .append(',')
                            .append(String.valueOf(subject.getColor()))
                            .append(',');

                    ArrayList<Assignment> temp = subject.getAssignmentList();
                    for (Assignment assignment : temp) {
                        writer.append(assignment.getNameOfAssignment()).append(',');
                    }
                    writer.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * used for tomcat activities
     *
     * @param a subject array to convert
     * @return string consisting of all schedule info
     */
    public String convertEverythingToString(ArrayList<Subject> a) {
        //IGNORE METHOD. New implementation of networking on web server itself.
        String finalString = "";
        String temp = "";
        for (Subject subject : a) {
            temp = subject.getNameOfSubject() + ',' + String.valueOf(subject.getGradeInClass()) + ',' +
                    String.valueOf(subject.isWeighted()) + ',' + String.valueOf(subject.getColor()) + ',';
            finalString += temp;
            ArrayList<Assignment> assignments = subject.getAssignmentList();
            for (int j = 0; j < assignments.size(); j++) {
                finalString += assignments.get(j).getNameOfAssignment() + (',');
            }
            finalString += "\n";
        }
        return finalString;
    }
}
