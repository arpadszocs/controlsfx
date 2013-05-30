package org.controlsfx.control.action;

import java.util.ArrayList;
import java.util.Collection;

import javafx.beans.binding.ObjectBinding;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

/**
 * Convenience class for users of the {@link Action} API. Primarily this class
 * is used to conveniently create UI controls from a given Action (this is 
 * necessary for now as there is no built-in support for Action in JavaFX 
 * UI controls at present).
 * 
 * @see Action
 */
public class ActionUtils {

    private ActionUtils() {
        // no-op
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link Button} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link Button} should bind to.
     * @return A {@link Button} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static Button createButton(final Action action) {
        return configure(new Button(), action);
    }
    
    public static MenuButton createMenuButton(final Action action) {
        return configure(new MenuButton(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link Hyperlink} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link Hyperlink} should bind to.
     * @return A {@link Hyperlink} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static Hyperlink createHyperlink(final Action action) {
        return configure(new Hyperlink(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link ToggleButton} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link ToggleButton} should bind to.
     * @return A {@link ToggleButton} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static ToggleButton createToggleButton(final Action action) {
        return configure(new ToggleButton(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link CheckBox} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link CheckBox} should bind to.
     * @return A {@link CheckBox} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static CheckBox createCheckBox(final Action action) {
        return configure(new CheckBox(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link RadioButton} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link RadioButton} should bind to.
     * @return A {@link RadioButton} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static RadioButton createRadioButton(final Action action) {
        return configure(new RadioButton(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link MenuItem} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link MenuItem} should bind to.
     * @return A {@link MenuItem} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static MenuItem createMenuItem(final Action action) {
        return configure(new MenuItem(), action);
    }
    
    public static Menu createMenu(final Action action) {
        return configure(new Menu(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link CheckMenuItem} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link CheckMenuItem} should bind to.
     * @return A {@link CheckMenuItem} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static CheckMenuItem createCheckMenuItem(final Action action) {
        return configure(new CheckMenuItem(), action);
    }
    
    /**
     * Takes the provided {@link Action} and returns a {@link RadioMenuItem} instance
     * with all relevant properties bound to the properties of the Action.
     * 
     * @param action The {@link Action} that the {@link RadioMenuItem} should bind to.
     * @return A {@link RadioMenuItem} that is bound to the state of the provided 
     *      {@link Action}
     */
    public static RadioMenuItem createRadioMenuItem(final Action action) {
        return configure(new RadioMenuItem(action.textProperty().get()), action);
    }
    
    public static ToolBar createToolBar( Collection<? extends Action> actions ) {
        
        ToolBar toolbar = new ToolBar();
        for (Action action : actions) {
           
            if ( action instanceof ActionGroup ) {
                MenuButton menu = createMenuButton( action );
                menu.getItems().addAll( toMenuItems( ((ActionGroup)action).getActions()));
                toolbar.getItems().add(menu);
            } else if ( action == null ) {
                toolbar.getItems().add( new Separator());
            } else {
                toolbar.getItems().add( createButton(action));
            }
            
        }
        
        return toolbar;
        
    }
    
    public static MenuBar createMenuBar( Collection<? extends Action> actions ) {
        
        MenuBar menuBar = new MenuBar();
        for (Action action : actions) {
            
            Menu menu = createMenu( action );
            
            if ( action instanceof ActionGroup ) {
               menu.getItems().addAll( toMenuItems( ((ActionGroup)action).getActions()));
            } 
            
            menuBar.getMenus().add(menu);
            
        }
        
        return menuBar;
        
    }
    
    public static ContextMenu createContextMenu( Collection<? extends Action> actions ) {
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(toMenuItems(actions));
        return menu;
    }
    
    private static Collection<MenuItem> toMenuItems( Collection<? extends Action> actions ) {
        
        Collection<MenuItem> items = new ArrayList<MenuItem>();
        
        for (Action action : actions) {
            
            if ( action instanceof ActionGroup ) {
                
               Menu menu = createMenu( action );
               menu.getItems().addAll( toMenuItems( ((ActionGroup)action).getActions()));
               items.add(menu);
                
            } else if ( action == null ) {
                
                items.add( new SeparatorMenuItem());
                
            } else {
                
                items.add( createMenuItem(action));
                
            }
            
        }
        
        return items;
        
    }
    
    private static <T extends ButtonBase> T configure(final T btn, final Action action) {
        
        if (action == null) {
            throw new NullPointerException("Action can not be null");
        }
        
        // button bind to action properties
        btn.textProperty().bind(action.textProperty());
        btn.disableProperty().bind(action.disabledProperty());
        btn.graphicProperty().bind(action.graphicProperty());
        
        // add all the properties of the action into the button, and set up
        // a listener so they are always copied across
        btn.getProperties().putAll(action.getProperties());
        action.getProperties().addListener(new MapChangeListener<Object, Object>() {
            public void onChanged(MapChangeListener.Change<? extends Object,? extends Object> change) {
                btn.getProperties().clear();
                btn.getProperties().putAll(action.getProperties());
            }
        });
        
        // tooltip requires some special handling (i.e. don't have one when
        // the text property is null
        btn.tooltipProperty().bind(new ObjectBinding<Tooltip>() {
            private Tooltip tooltip = new Tooltip();
            
            { 
                bind(action.longTextProperty()); 
                tooltip.textProperty().bind(action.longTextProperty());
            }
            
            @Override protected Tooltip computeValue() {
                String longText = action.longTextProperty().get();
                return longText == null || longText.isEmpty() ? null : tooltip;
            } 
        });
        
        // TODO handle the selected state of the button if it is of the applicable
        // type
        
        // Just call the execute method on the action itself when the action
        // event occurs on the button
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent ae) {
                if ( !action.disabledProperty().get()) {
                    action.execute(ae);
                }
            }
        });
        
        return btn;
    }
    
    private static <T extends MenuItem> T configure(final T btn, final Action action) {
        
        if (action == null) {
            throw new NullPointerException("Action can not be null");
        }
        
        // button bind to action properties
        btn.textProperty().bind(action.textProperty());
        btn.disableProperty().bind(action.disabledProperty());
        btn.graphicProperty().bind(action.graphicProperty());
        
        // add all the properties of the action into the button, and set up
        // a listener so they are always copied across
        btn.getProperties().putAll(action.getProperties());
        action.getProperties().addListener(new MapChangeListener<Object, Object>() {
            public void onChanged(MapChangeListener.Change<? extends Object,? extends Object> change) {
                btn.getProperties().clear();
                btn.getProperties().putAll(action.getProperties());
            }
        });
        
        // TODO handle the selected state of the menu item if it is a 
        // CheckMenuItem or RadioMenuItem
        
        // Just call the execute method on the action itself when the action
        // event occurs on the button
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent ae) {
                if ( !action.disabledProperty().get()) {
                    action.execute(ae);
                }
            }
        });
        
        return btn;
    }
}
