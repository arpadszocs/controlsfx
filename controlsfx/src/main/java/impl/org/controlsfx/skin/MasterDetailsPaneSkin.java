/**
 * Copyright (c) 2014, ControlsFX
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
package impl.org.controlsfx.skin;

import static java.lang.Double.MAX_VALUE;
import static javafx.geometry.Orientation.HORIZONTAL;
import static javafx.geometry.Orientation.VERTICAL;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.layout.Region;

import org.controlsfx.control.MasterDetailsPane;
import org.controlsfx.control.MasterDetailsPane.DetailsPos;

public class MasterDetailsPaneSkin extends SkinBase<MasterDetailsPane> {

    private double lastDividerPosition;
    private boolean requiresDividerPositionUpdate;

    private SplitPane splitPane;
    private Timeline timeline;

    public MasterDetailsPaneSkin(MasterDetailsPane masterDetailsPane) {
        super(masterDetailsPane);

        this.splitPane = new SplitPane();

        switch (masterDetailsPane.getDetailsPos()) {
        case BOTTOM:
        case RIGHT:
            this.splitPane.setDividerPosition(0, .8);
            break;
        case TOP:
        case LEFT:
            this.splitPane.setDividerPosition(0, .2);
            break;
        default:
            break;

        }

        SplitPane
                .setResizableWithParent(getSkinnable().getDetailsNode(), false);

        this.requiresDividerPositionUpdate = !getSkinnable().isExpanded();

        switch (getSkinnable().getDetailsPos()) {
        case BOTTOM:
        case TOP:
            splitPane.setOrientation(VERTICAL);
            break;
        case LEFT:
        case RIGHT:
            splitPane.setOrientation(HORIZONTAL);
            break;
        }

        getSkinnable().masterNodeProperty().addListener(
                new ChangeListener<Node>() {
                    @Override
                    public void changed(ObservableValue<? extends Node> value,
                            Node oldNode, Node newNode) {

                        if (oldNode != null) {
                            splitPane.getItems().remove(oldNode);
                        }

                        if (newNode != null) {

                            updateMinAndMaxSizes();

                            int masterIndex = 0;
                            switch (splitPane.getOrientation()) {
                            case HORIZONTAL:
                                switch (getSkinnable().getDetailsPos()) {
                                case LEFT:
                                    masterIndex = 1;
                                    break;
                                case RIGHT:
                                    masterIndex = 0;
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "illegal details position "
                                                    + getSkinnable()
                                                            .getDetailsPos()
                                                    + " for orientation "
                                                    + splitPane
                                                            .getOrientation());
                                }
                                break;
                            case VERTICAL:
                                switch (getSkinnable().getDetailsPos()) {
                                case TOP:
                                    masterIndex = 1;
                                    break;
                                case BOTTOM:
                                    masterIndex = 0;
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "illegal details position "
                                                    + getSkinnable()
                                                            .getDetailsPos()
                                                    + " for orientation "
                                                    + splitPane
                                                            .getOrientation());
                                }
                                break;
                            }
                            List<Node> items = splitPane.getItems();
                            if (items.isEmpty()) {
                                items.add(newNode);
                            } else {
                                items.add(masterIndex, newNode);
                            }
                        }
                    }
                });

        getSkinnable().detailsNodeProperty().addListener(
                new ChangeListener<Node>() {
                    @Override
                    public void changed(ObservableValue<? extends Node> value,
                            Node oldNode, Node newNode) {

                        if (oldNode != null) {
                            splitPane.getItems().remove(oldNode);
                        }

                        if (newNode != null) {

                            updateMinAndMaxSizes();

                            SplitPane.setResizableWithParent(newNode, false);

                            int detailsIndex = 0;
                            switch (splitPane.getOrientation()) {
                            case HORIZONTAL:
                                switch (getSkinnable().getDetailsPos()) {
                                case LEFT:
                                    detailsIndex = 0;
                                    break;
                                case RIGHT:
                                    detailsIndex = 1;
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "illegal details position "
                                                    + getSkinnable()
                                                            .getDetailsPos()
                                                    + " for orientation "
                                                    + splitPane
                                                            .getOrientation());
                                }
                                break;
                            case VERTICAL:
                                switch (getSkinnable().getDetailsPos()) {
                                case TOP:
                                    detailsIndex = 0;
                                    break;
                                case BOTTOM:
                                    detailsIndex = 1;
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "illegal details position "
                                                    + getSkinnable()
                                                            .getDetailsPos()
                                                    + " for orientation "
                                                    + splitPane
                                                            .getOrientation());
                                }
                                break;
                            }
                            List<Node> items = splitPane.getItems();
                            if (items.isEmpty()) {
                                items.add(newNode);
                            } else {
                                items.add(detailsIndex, newNode);
                            }
                        }
                    }
                });

        getSkinnable().expandedProperty().addListener(
                new ChangeListener<Boolean>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Boolean> value,
                            Boolean oldExpanded, Boolean newExpanded) {
                        if (newExpanded) {
                            open();
                        } else {
                            close();
                        }
                    }
                });

        getSkinnable().detailsPosProperty().addListener(
                new ChangeListener<DetailsPos>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends DetailsPos> value,
                            DetailsPos oldPos, DetailsPos newPos) {
                        if (getSkinnable().isExpanded()) {
                            lastDividerPosition = splitPane.getDividers()
                                    .get(0).getPosition();
                            splitPane.getItems().clear();
                        }
                        switch (newPos) {
                        case TOP:
                        case BOTTOM:
                            splitPane.setOrientation(VERTICAL);
                            break;
                        case LEFT:
                        case RIGHT:
                            splitPane.setOrientation(HORIZONTAL);
                        }
                        switch (newPos) {
                        case TOP:
                        case LEFT:
                            if (getSkinnable().isExpanded()) {
                                splitPane.getItems().add(
                                        getSkinnable().getDetailsNode());
                                splitPane.getItems().add(
                                        getSkinnable().getMasterNode());
                            }
                            switch (oldPos) {
                            case BOTTOM:
                            case RIGHT:
                                lastDividerPosition = 1 - lastDividerPosition;
                                break;
                            default:
                                break;
                            }
                            break;
                        case BOTTOM:
                        case RIGHT:
                            if (getSkinnable().isExpanded()) {
                                splitPane.getItems().add(
                                        getSkinnable().getMasterNode());
                                splitPane.getItems().add(
                                        getSkinnable().getDetailsNode());
                            }
                            switch (oldPos) {
                            case TOP:
                            case LEFT:
                                lastDividerPosition = 1 - lastDividerPosition;
                                break;
                            default:
                                break;
                            }
                            break;
                        }
                        if (getSkinnable().isExpanded()) {
                            splitPane.setDividerPositions(lastDividerPosition);
                        }
                    }
                });

        updateMinAndMaxSizes();

        getChildren().add(splitPane);

        splitPane.getItems().add(getSkinnable().getMasterNode());

        if (getSkinnable().isExpanded()) {
            /*
             * Add details node to the splitpane if it is already expanded.
             */
            switch (getSkinnable().getDetailsPos()) {
            case TOP:
            case LEFT:
                splitPane.getItems().add(0, getSkinnable().getDetailsNode());
                break;
            case BOTTOM:
            case RIGHT:
                splitPane.getItems().add(getSkinnable().getDetailsNode());
                break;
            }

            bindDividerPosition();
        }
    }

    private void bindDividerPosition() {
        final ObservableList<Divider> dividers = splitPane.getDividers();
        if (dividers.size() > 0) {
            Bindings.bindBidirectional(
                    getSkinnable().dividerPositionProperty(), dividers.get(0)
                            .positionProperty());
        }
    }

    private void unbindDividerPosition() {
        ObservableList<Divider> dividers = splitPane.getDividers();
        if (dividers.size() > 0) {
            Bindings.unbindBidirectional(getSkinnable()
                    .dividerPositionProperty(), dividers.get(0)
                    .positionProperty());
        }
    }

    private void updateMinAndMaxSizes() {
        if (getSkinnable().getMasterNode() instanceof Region) {
            ((Region) getSkinnable().getMasterNode()).setMinSize(0, 0);
            ((Region) getSkinnable().getMasterNode()).setMaxSize(MAX_VALUE,
                    MAX_VALUE);
        }

        if (getSkinnable().getDetailsNode() instanceof Region) {
            ((Region) getSkinnable().getDetailsNode()).setMinSize(0, 0);
            ((Region) getSkinnable().getDetailsNode()).setMaxSize(MAX_VALUE,
                    MAX_VALUE);
        }
    }

    private void open() {
        Node node = getSkinnable().getDetailsNode();

        switch (getSkinnable().getDetailsPos()) {
        case TOP:
        case LEFT:
            splitPane.getItems().add(0, node);
            splitPane.setDividerPositions(0);
            break;
        case BOTTOM:
        case RIGHT:
            splitPane.getItems().add(node);
            splitPane.setDividerPositions(1);
            break;
        }

        if (requiresDividerPositionUpdate) {
            /*
             * +10 needed because of width of divider. Might be better to look
             * this up somewhere, but where?
             */
            switch (splitPane.getOrientation()) {
            case HORIZONTAL:
                switch (getSkinnable().getDetailsPos()) {
                case LEFT:
                    lastDividerPosition = (node
                            .prefWidth(splitPane.getHeight()) + 10)
                            / splitPane.getWidth();
                    break;
                case RIGHT:
                    lastDividerPosition = 1
                            - (node.prefWidth(splitPane.getHeight()) + 10)
                            / splitPane.getWidth();
                    break;
                default:
                    break;
                }
                break;
            case VERTICAL:
                switch (getSkinnable().getDetailsPos()) {
                case TOP:
                    lastDividerPosition = (node
                            .prefHeight(splitPane.getWidth()) + 10)
                            / splitPane.getHeight();
                    break;
                case BOTTOM:
                    lastDividerPosition = 1
                            - (node.prefHeight(splitPane.getWidth()) + 10)
                            / splitPane.getHeight();
                    break;
                default:
                    break;
                }
                break;
            }

            requiresDividerPositionUpdate = false;
        }

        maybeAnimatePositionChange(lastDividerPosition, true);
    }

    private void close() {
        if (!splitPane.getDividers().isEmpty()) {

            Divider divider = splitPane.getDividers().get(0);

            lastDividerPosition = divider.getPosition();

            /*
             * Do we collapse by moving the divider to the left/right or
             * top/bottom?
             */
            double targetLocation = 0;
            switch (getSkinnable().getDetailsPos()) {
            case BOTTOM:
            case RIGHT:
                targetLocation = 1;
                break;
            default:
                break;
            }

            maybeAnimatePositionChange(targetLocation, false);
        }
    }

    private void maybeAnimatePositionChange(final double position,
            final boolean newExpandedState) {

        Divider divider = splitPane.getDividers().get(0);

        if (getSkinnable().isAnimated()) {
            if (newExpandedState) {
                unbindDividerPosition();
                bindDividerPosition();
            }

            KeyValue positionKeyValue = new KeyValue(
                    divider.positionProperty(), position);
            KeyValue opacityKeyValue = new KeyValue(getSkinnable()
                    .getDetailsNode().opacityProperty(), newExpandedState ? 1
                    : 0);
            KeyFrame keyFrame = new KeyFrame(getSkinnable()
                    .getAnimationDuration(), positionKeyValue, opacityKeyValue);
            timeline = new Timeline(keyFrame);
            timeline.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent evt) {
                    if (!newExpandedState) {
                        unbindDividerPosition();
                        splitPane.getItems().remove(
                                getSkinnable().getDetailsNode());
                    }
                }
            });
            timeline.play();
        } else {
            if (newExpandedState) {
                unbindDividerPosition();
                bindDividerPosition();
            }
            getSkinnable().getDetailsNode().setOpacity(1);
            divider.setPosition(position);

            if (!newExpandedState) {
                unbindDividerPosition();
                splitPane.getItems().remove(getSkinnable().getDetailsNode());
            }
        }
    }
}
