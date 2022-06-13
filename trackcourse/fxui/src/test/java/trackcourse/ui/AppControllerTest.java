package trackcourse.ui;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.ListViewMatchers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import trackcourse.core.Subject;

public class AppControllerTest extends ApplicationTest {

    @FXML TextField nameInput;
    @FXML Slider diffSlider, timeSlider, happySlider;
    @FXML Button submitButton, saveButton, detailsButton, btnCloseDetails;


    private AppController controller;
    public Collection<Subject> presaved_subs;
    private Subject sub;
    private Collection<Subject> subjects;

    @Override
    public void start(final Stage stage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("App.fxml"));
        final Parent parent = fxmlLoader.load();
        controller = fxmlLoader.getController();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @BeforeEach
    public void init() throws IOException{
        sub = new Subject("IT1901");
        subjects = new ArrayList<>();
        subjects.add(sub);
    }

    @Test
    public void testController() {
        Assertions.assertNotNull(this.controller);
    }

    @Test
    public void testGetterAndSetter() {
        controller.setSubjects(subjects);
        Assertions.assertEquals(controller.getSubjects(), subjects);
    }

    
    //UI tests

    @Test
    public void testAddSubject() {
        clickOn("#nameInput").write("TDT4100");
        clickOn("#submitButton");
        FxAssert.verifyThat("#subjectListView", ListViewMatchers.hasListCell(("TDT4100 // 5")));
        //Default value for sliders are 5, so avg. will always be 5 in this case
    }

    @Test
    public void testOnDetails() {
        openDetailsPane();
        FxAssert.verifyThat("#detailsPane", NodeMatchers.isVisible()); 
    }

    @Test
    public void testOnClose() {
        openDetailsPane();
        clickOn("#btnCloseDetails");
        FxAssert.verifyThat("#sliderPane", NodeMatchers.isVisible());
    }

    @Test 
    public void testSliders(){
        clickOn("#nameInput").write("TDT4100");
        controller.diffSlider.setValue(6);
        controller.timeSlider.setValue(6);
        controller.happySlider.setValue(6);

        clickOn("#submitButton");

        FxAssert.verifyThat("#subjectListView", ListViewMatchers.hasListCell(("TDT4100 // 6")));

        clickOn("#saveButton"); 
    }

    

    @Test 
    public void testUpdate(){
        controller.onReset();
        clickOn("#saveButton");

        clickOn("#nameInput").write("TDT4100");
        controller.diffSlider.setValue(8);
        controller.timeSlider.setValue(8);
        controller.happySlider.setValue(8);
        clickOn("#submitButton");

        FxAssert.verifyThat("#subjectListView", ListViewMatchers.hasListCell(("TDT4100 // 8")));

        controller.diffSlider.setValue(6);
        controller.timeSlider.setValue(6);
        controller.happySlider.setValue(6);
        clickOn("#submitButton");

        FxAssert.verifyThat("#subjectListView", ListViewMatchers.hasListCell(("TDT4100 // 7")));


    }

    @Test
    public void testLoad() throws JsonProcessingException, IOException{
        controller.onReset();
        clickOn("#saveButton");

        clickOn("#nameInput").write("TDT4100");
        controller.diffSlider.setValue(8);
        controller.timeSlider.setValue(8);
        controller.happySlider.setValue(8);
        clickOn("#submitButton");
        clickOn("#saveButton");

        clickOn("#loadButton");
        FxAssert.verifyThat("#subjectListView", ListViewMatchers.hasListCell(("TDT4100 // 8")));

    }


   
    @Test
    public void testIncorrectSub(){
        clickOn("#nameInput").write("ABC1234");
        Button btn = submitButton;
        assertNull(btn);

    }

    @Test
    public void testNonSelectedOnDetails(){
        clickOn("#detailsButton");
        FxAssert.verifyThat("#detailsPane", NodeMatchers.isInvisible()); 
    }

    public void openDetailsPane() {
        clickOn("#nameInput").write("TDT4100");
        clickOn("#submitButton");
        clickOn("#nameInput");

        //Presses TAB-button 8 times to be able to reach the first object in the Subjects Listview
        for (int i=0; i<8; i++) {
            press(KeyCode.TAB).release(KeyCode.TAB);
        }
        press(KeyCode.ENTER);

        clickOn("#detailsButton");
    }
}
