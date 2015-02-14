package de.tu_darmstadt.gdi1.dropofwater;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import eea.engine.action.Action;
import eea.engine.action.basicactions.ChangeStateAction;
import eea.engine.action.basicactions.DestroyEntityAction;
import eea.engine.action.basicactions.MoveDownAction;
import eea.engine.component.Component;
import eea.engine.component.render.ImageRenderComponent;
import eea.engine.entity.DestructibleImageEntity;
import eea.engine.entity.Entity;
import eea.engine.entity.StateBasedEntityManager;
import eea.engine.event.Event;
import eea.engine.event.basicevents.CollisionEvent;
import eea.engine.event.basicevents.KeyPressedEvent;
import eea.engine.event.basicevents.LoopEvent;
import eea.engine.interfaces.IDestructible;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * @author Timo Baehr, Peter Kl�ckner
 *         <p>
 *         Diese Klasse repraesentiert das Spielfenster, indem ein Wassertropfen
 *         erscheint und nach unten faellt.
 */
public class GameplayState extends BasicTWLGameState {

    private int stateID; // Identifier dieses BasicTWLGameState
    private StateBasedEntityManager entityManager; // zugehoeriger entityManager
    private Label xLabel;
    EditField xInput;
    private Label yLabel;
    EditField yInput;
    private Button dropButton;

    GameplayState(int sid) {
        stateID = sid;
        entityManager = StateBasedEntityManager.getInstance();
    }

    /**
     * Wird vor dem (erstmaligen) Starten dieses States ausgefuehrt
     */
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

        // Hintergrund laden
        Entity background = new Entity("background"); // Entitaet fuer
        // Hintergrund
        background.setPosition(new Vector2f(400, 300)); // Startposition des
        // Hintergrunds
        background.addComponent(new ImageRenderComponent(new Image("/assets/dropofwater/background.png"))); // Bildkomponente

        // Hintergrund-Entitaet an StateBasedEntityManager uebergeben
        StateBasedEntityManager.getInstance().addEntity(stateID, background);

        // Bei Druecken der ESC-Taste zurueck ins Hauptmenue wechseln
        Entity escListener = new Entity("ESC_Listener");
        KeyPressedEvent escPressed = new KeyPressedEvent(Input.KEY_ESCAPE);
        escPressed.addAction(new ChangeStateAction(Launch.MAINMENU_STATE));
        escListener.addComponent(escPressed);
        entityManager.addEntity(stateID, escListener);

        // erstelle ein Bild der Breite 500 und der H�he 200
        BufferedImage image = new BufferedImage(500, 200, BufferedImage.TYPE_INT_ARGB);
        // mit Graphics2D l�sst sich das Bild bemalen
        Graphics2D graphic = image.createGraphics();
        // die folgende Zeile bewirkt, dass sich auch wieder "ausradieren" l�sst
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        // bemale das vollst�ndige Bild wei�
        graphic.setColor(new Color(255, 255, 255));
        graphic.fillRect(0, 0, 500, 200);
        // radiere in der Mitte wieder ein Rechteck aus
        graphic.setColor(new Color(255, 255, 255, 0));
        graphic.fillRect(100, 50, 300, 100);

        // erstelle eine DestructibleImageEntity mit dem gerade gemalten Bild
        // als Image, das durch das Zerst�rungs-Pattern destruction.png zerst�rt
        // werden kann
        DestructibleImageEntity obstacle = new DestructibleImageEntity("obstacle", image, "dropofwater/destruction.png", false);
        obstacle.setPosition(new Vector2f(game.getContainer().getWidth() / 2, game.getContainer().getHeight() - 100));

        entityManager.addEntity(stateID, obstacle);
    }

    /**
     * Wird vor dem Frame ausgefuehrt
     */
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // StatedBasedEntityManager soll alle Entities aktualisieren
        entityManager.updateEntities(container, game, delta);
    }

    /**
     * Wird mit dem Frame ausgefuehrt
     */
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // StatedBasedEntityManager soll alle Entities rendern
        entityManager.renderEntities(container, game, g);
    }

    @Override
    public int getID() {
        return stateID;
    }

    /**
     * In dieser Methode werden in einem BasicTWLGameSate alle GUI-Elemente dem
     * GameState mit Hilfe einer RootPane hinzugef�gt
     */
    @Override
    protected RootPane createRootPane() {

        // erstelle die RootPane
        RootPane rp = super.createRootPane();

        // erstelle ein Label mit der Aufschrift "x:"
        xLabel = new Label("x:");
        // erstelle ein EditField. Es dient der Eingabe von Text
        xInput = new EditField();
        // mit der Methode addCallBack l�sst sich dem EditField ein CallBack
        // hinzuf�gen, in dessen Methode callback(int key) bestimmt wird, was
        // geschehen soll, wenn ein Zeichen eingegeben wird
        xInput.addCallback(new Callback() {
            public void callback(int key) {
                // in unserem Fall wird der Input in der Methode
                // handleEditFieldInput verarbeitet (siehe weiter unten in
                // dieser Klasse, was diese tut, und was es mit ihren Parametern
                // auf sich hat)
                handleEditFieldInput(key, xInput, this, 1000);
            }
        });

        // analog zu einer Eingabem�glichkeit f�r x-Werte wird auch eine f�r
        // y-Werte kreiert
        yLabel = new Label("y:");
        yInput = new EditField();
        yInput.addCallback(new Callback() {
            public void callback(int key) {
                handleEditFieldInput(key, yInput, this, 500);
            }
        });

        // zuletzt wird noch ein Button hinzugef�gt
        dropButton = new Button("drop");
        // �hnlich wie einem EditField kann auch einem Button ein CallBack
        // hinzugef�gt werden
        // Hier ist es jedoch von Typ Runnable, da keine Parameter (z. B. welche
        // Taste wurde gedr�ckt) ben�tigt werden
        dropButton.addCallback(new Runnable() {
            @Override
            public void run() {
                // ein Klick auf den Button wird in unserem Fall in der
                // folgenden Methode verarbeitet
                inputFinished();
            }
        });

        // am Schluss der Methode m�ssen alle GUI-Elemente der Rootpane
        // hinzugef�gt werden
        rp.add(xLabel);
        rp.add(xInput);

        rp.add(yLabel);
        rp.add(yInput);

        rp.add(dropButton);

        // ... und die fertige Rootpane zur�ckgegeben werden
        return rp;
    }

    /**
     * in dieser Methode des BasicTWLGameState werden die erstellten
     * GUI-Elemente platziert
     */
    @Override
    protected void layoutRootPane() {

        int xOffset = 50;
        int yOffset = 50;
        int gap = 5;

        // alle GUI-Elemente m�ssen eine Gr��e zugewiesen bekommen. Soll die
        // Gr��e automatisch �ber die Beschriftung des GUI-Elements bestimmt
        // werden, so muss adjustSize() aufgerufen werden.
        xLabel.adjustSize();
        yLabel.adjustSize();

        // Ansonsten wird die Gr��e manuell mit setSize() gesetzt
        xInput.setSize(50, 25);
        yInput.setSize(50, 25);
        dropButton.setSize(50, 25);

        // Nachdem alle Gr��en adjustiert wurden, muss allen GUI-Elementen eine
        // Position (linke obere Ecke) zugewiesen werden
        xLabel.setPosition(xOffset, yOffset);
        xInput.setPosition(xOffset + xLabel.getWidth() + gap, yOffset);

        yLabel.setPosition(xOffset, yOffset + xLabel.getHeight() + gap);
        yInput.setPosition(xOffset + yLabel.getWidth() + gap, yOffset + xLabel.getHeight() + gap);

        dropButton.setPosition(xOffset + yLabel.getWidth() + gap, yOffset + xLabel.getHeight() + gap + yLabel.getHeight() + gap);
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Zeichen in ein EditField eingegeben wurde.
     *
     * @param key       die gedr�ckte Taste
     * @param editField das EditField, in das ein Zeichen eingef�gt wurde
     * @param callback  der CallBack, der dem EditField hinzugef�gt wurde
     * @param maxValue  die gr��te Zahl, die in das <code>editField</code> eingegeben werden kann
     */
    void handleEditFieldInput(int key, EditField editField, Callback callback, int maxValue) {

        if (key == de.matthiasmann.twl.Event.KEY_NONE) {
            String inputText = editField.getText();

            if (inputText.isEmpty()) {
                return;
            }

            char inputChar = inputText.charAt(inputText.length() - 1);
            if (!Character.isDigit(inputChar) || Integer.parseInt(inputText) > maxValue) {
                // a call of setText on an EditField triggers the callback, so
                // remove callback before and add it again after the call
                editField.removeCallback(callback);
                editField.setText(inputText.substring(0, inputText.length() - 1));
                editField.addCallback(callback);
            }
        }
    }

    /**
     * diese Methode wird bei Klick auf den Button ausgef�hrt
     */
    void inputFinished() {

        // Wassertropfen wird erzeugt
        Entity drop = new Entity("drop of water");
        drop.setPosition(new Vector2f(Integer.parseInt(xInput.getText()), Integer.parseInt(yInput.getText())));

        try {
            // Bild laden und zuweisen
            drop.addComponent(new ImageRenderComponent(new Image("assets/dropofwater/drop.png")));
        } catch (SlickException e) {
            System.err.println("Cannot find file assets/dropofwater/drop.png!");
            e.printStackTrace();
        }

        // Wassertropfen faellt nach unten
        LoopEvent loop = new LoopEvent();
        loop.addAction(new MoveDownAction(0.5f));
        drop.addComponent(loop);

        Event collisionEvent = new CollisionEvent();
        collisionEvent.addAction(new Action() {
            @Override
            public void update(GameContainer gc, StateBasedGame sb, int delta, Component event) {

                // hole die Entity, mit der kollidiert wurde
                CollisionEvent collider = (CollisionEvent) event;
                Entity entity = collider.getCollidedEntity();

                // wenn diese durch ein Pattern zerst�rt werden kann, dann caste
                // zu IDestructible
                // ansonsten passiert bei der Kollision nichts
                IDestructible destructible = null;
                if (entity instanceof IDestructible) {
                    destructible = (IDestructible) entity;
                }
                else {
                    return;
                }

                // zerst�re die Entit�t (dabei wird das der Entit�t
                // zugewiese Zerst�rungs-Pattern benutzt)
                destructible.impactAt(event.getOwnerEntity().getPosition());
            }
        });
        collisionEvent.addAction(new DestroyEntityAction());
        drop.addComponent(collisionEvent);

        entityManager.addEntity(stateID, drop);
    }
}
