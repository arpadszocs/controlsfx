/**
 * Copyright (c) 2013, ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.controlsfx.samples;

import java.text.NumberFormat;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import org.controlsfx.ControlsFXSample;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

public class HelloPopOver extends ControlsFXSample {

    private PopOver popOver = new PopOver();

    private DoubleProperty masterArrowSize;
    private DoubleProperty masterArrowIndent;
    private DoubleProperty masterCornerRadius;
    private ObjectProperty<ArrowLocation> masterArrowLocation;

    private double targetX;
    private double targetY;

    @Override
    public Node getPanel(Stage stage) {
        Group group = new Group();

        final Rectangle rect = new Rectangle();
        rect.setStroke(Color.BLACK);
        rect.setFill(Color.CORAL);
        rect.setWidth(220);
        rect.setHeight(220);
        group.getChildren().add(rect);

        final Circle circle = new Circle();
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        group.getChildren().add(circle);

        final Line line1 = new Line();
        line1.setFill(Color.BLACK);
        group.getChildren().add(line1);

        final Line line2 = new Line();
        line2.setFill(Color.BLACK);
        group.getChildren().add(line2);

        /*
         * These master properties are only needed for this demo as we want to
         * make sure that the settings done by the user via the demo controls
         * will be applied to all popovers that are currently visible (this
         * includes the detached ones).
         */
        masterArrowSize = new SimpleDoubleProperty(popOver.getArrowSize());
        masterArrowIndent = new SimpleDoubleProperty(popOver.getArrowIndent());
        masterCornerRadius = new SimpleDoubleProperty(popOver.getCornerRadius());
        masterArrowLocation = new SimpleObjectProperty<>(
                popOver.getArrowLocation());

        popOver = createPopOver();

        rect.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent evt) {
                double delta = evt.getDeltaY();
                rect.setWidth(Math.max(100,
                        Math.min(500, rect.getWidth() + delta)));
                rect.setHeight(Math.max(100,
                        Math.min(500, rect.getHeight() + delta)));
            }
        });

        rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent evt) {
                if (!popOver.isDetached()) {
                    popOver.hide();
                }

                if (evt.getClickCount() == 2) {
                    targetX = evt.getScreenX();
                    targetY = evt.getScreenY();

                    if (popOver.isDetached()) {
                        popOver = createPopOver();
                    }

                    double size = 3;
                    line1.setStartX(evt.getX() - size);
                    line1.setStartY(evt.getY() - size);
                    line1.setEndX(evt.getX() + size);
                    line1.setEndY(evt.getY() + size);

                    line2.setStartX(evt.getX() + size);
                    line2.setStartY(evt.getY() - size);
                    line2.setEndX(evt.getX() - size);
                    line2.setEndY(evt.getY() + size);

                    circle.setCenterX(evt.getX());
                    circle.setCenterY(evt.getY());
                    circle.setRadius(size * 3);

                    popOver.show(rect, targetX, targetY);
                }
            }
        });

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(group);
        Label label = new Label("Double Click for PopOver. Scroll for resize.");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.requestFocus();
        label.maxWidthProperty().bind(rect.widthProperty());

        stackPane.getChildren().add(label);
        BorderPane.setMargin(stackPane, new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(stackPane);

        return borderPane;
    }
    
    @Override public Node getControlPanel() {
        Slider arrowSize = new Slider(0, 50, masterArrowSize.getValue());
        masterArrowSize.bind(arrowSize.valueProperty());
        GridPane.setFillWidth(arrowSize, true);

        Slider arrowIndent = new Slider(0, 30, masterArrowIndent.getValue());
        masterArrowIndent.bind(arrowIndent.valueProperty());
        GridPane.setFillWidth(arrowIndent, true);

        Slider cornerRadius = new Slider(0, 32, masterCornerRadius.getValue());
        masterCornerRadius.bind(cornerRadius.valueProperty());
        GridPane.setFillWidth(cornerRadius, true);

        GridPane controls = new GridPane();
        controls.setHgap(10);
        controls.setVgap(10);

        BorderPane.setMargin(controls, new Insets(10));
        BorderPane.setAlignment(controls, Pos.BOTTOM_CENTER);

        Label arrowSizeLabel = new Label("Arrow Size:");
        GridPane.setHalignment(arrowSizeLabel, HPos.RIGHT);
        controls.add(arrowSizeLabel, 0, 0);
        controls.add(arrowSize, 1, 0);

        Label arrowIndentLabel = new Label("Arrow Indent:");
        GridPane.setHalignment(arrowIndentLabel, HPos.RIGHT);
        controls.add(arrowIndentLabel, 0, 1);
        controls.add(arrowIndent, 1, 1);

        Label cornerRadiusLabel = new Label("Corner Radius:");
        GridPane.setHalignment(cornerRadiusLabel, HPos.RIGHT);
        controls.add(cornerRadiusLabel, 0, 2);
        controls.add(cornerRadius, 1, 2);

        final Label arrowSizeValue = new Label();
        GridPane.setHalignment(arrowSizeValue, HPos.RIGHT);
        controls.add(arrowSizeValue, 2, 0);

        final Label arrowIndentValue = new Label();
        GridPane.setHalignment(arrowIndentValue, HPos.RIGHT);
        controls.add(arrowIndentValue, 2, 1);

        final Label cornerRadiusValue = new Label();
        GridPane.setHalignment(cornerRadiusValue, HPos.RIGHT);
        controls.add(cornerRadiusValue, 2, 2);

        arrowSize.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value,
                    Number oldSize, Number newSize) {
                arrowSizeValue.setText(NumberFormat.getIntegerInstance()
                        .format(newSize));
            }
        });

        arrowIndent.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value,
                    Number oldSize, Number newSize) {
                arrowIndentValue.setText(NumberFormat.getIntegerInstance()
                        .format(newSize));
            }
        });

        cornerRadius.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value,
                    Number oldSize, Number newSize) {
                cornerRadiusValue.setText(NumberFormat.getIntegerInstance()
                        .format(newSize));
            }
        });

        Label arrowLocationLabel = new Label("Arrow Location:");
        GridPane.setHalignment(arrowLocationLabel, HPos.RIGHT);
        controls.add(arrowLocationLabel, 0, 3);

        ComboBox<ArrowLocation> locationBox = new ComboBox<>();
        locationBox.getItems().addAll(ArrowLocation.values());
        locationBox.setValue(popOver.getArrowLocation());
        Bindings.bindBidirectional(masterArrowLocation,
                locationBox.valueProperty());
        controls.add(locationBox, 1, 3);
        
        return controls;
    }

    private PopOver createPopOver() {
        PopOver popOver = new PopOver();
        popOver.arrowSizeProperty().bind(masterArrowSize);
        popOver.arrowIndentProperty().bind(masterArrowIndent);
        popOver.arrowLocationProperty().bind(masterArrowLocation);
        popOver.cornerRadiusProperty().bind(masterCornerRadius);
        return popOver;
    }

    public PopOver getPopOver() {
        return popOver;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public String getSampleName() {
        return "PopOver";
    }

    @Override
    public String getJavaDocURL() {
        return Utils.JAVADOC_BASE + "org/controlsfx/control/PopOver.html";
    }

    @Override
    public String getSampleDescription() {
        return "An implementation of a pop over control as used by Apple for its iCal application. A pop over allows"
                + " the user to see and edit an objects properties. The pop over gets displayed in its own popup window and"
                + " can be torn off in order to create several instances of it.";
    }
}
