package de.tu_darmstadt.gdi1.gorillas.ui.states;

import de.matthiasmann.twl.Button;

import de.matthiasmann.twl.slick.BasicTWLGameState;
import de.matthiasmann.twl.slick.RootPane;
import de.tu_darmstadt.gdi1.gorillas.assets.Assets;
import de.tu_darmstadt.gdi1.gorillas.main.*;
import de.tu_darmstadt.gdi1.gorillas.main.Game;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class HelpState extends BasicTWLGameState {

    private Image background;
    private Button btnNext;
    private Button btnBack;
    private Button btnMainMenu;
    private int page;
    private String page0;
    private String page1;
    private String page2;
    private String page3;
    private String[] pages;

    private StateBasedGame game;

    @Override
    public int getID() {
        return Game.HELPSTATE;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame game) throws SlickException {
        background = Assets.loadImage(Assets.Images.MAINMENU_BACKGROUND);
        this.createRootPane();
        this.game = game;

        page = 0;
        page0 = "How to play the game:\n" +
                "\n" +
                "Each Player takes control of one gorilla and\n" +
                "throws bananas at the other gorilla.\n" +
                "The Player who hits tree times first wins the game.\n" +
                "You can change the starting \n" +
                "angle and speed to control the flight.\n" +
                "Moreover, you can influence the gravity and \n" +
                "switch the wind on or off in the Option Menu.";
        page1 = "Main menu:\n" +
                " Enter -> New Game\n" +
                " Escape -> Exit Game\n" +
                " M -> Mute\n" +
                " S -> Highscore\n" +
                " H -> Help\n" +
                " O -> Options\n" +
                " \n" +
                "Setup New Game:\n" +
                " Enter -> Start Game\n" +
                " Tap -> Switch between text fields\n" +
                " \n" +
                "In Game:\n" +
                " Up/W -> Increase Speed(Angle)\n" +
                " Down/S -> Decrease Speed(Angle)\n" +
                " Right/D -> Increase Angle(Speed)\n" +
                " Left/A -> Decrease Angle(Speed)\n" +
                " \n" +
                " Enter/Space -> Throw Banana\n" +
                " Escape/P -> Pause\n" +
                " M -> Mute";
        page2 = "Pause:\n" +
                " Escape/P -> Return to Game\n" +
                " Enter -> New Game\n" +
                " E -> Exit Game\n" +
                " S -> Return to Main Menu\n" +
                " M -> Mute\n" +
                " \n" +
                "Victory:\n" +
                " Escape -> Return to Main Menu\n" +
                " Enter -> New Game\n" +
                " \n" +
                "Highscore:\n" +
                " Enter/Escape/S -> Main Menu";
        page3 = " Options:\n" +
                " Escape/O -> Except Options and return to Main Menu\n" +
                " UP -> Increase Gravity\n" +
                " DOWN -> Decrease Gravity\n" +
                " C -> Change Control for Angle/Speed\n" +
                " W -> Turn Wind on/off\n" +
                " M -> Mute\n" +
                " \n" +
                "Help:\n" +
                " Enter/Escape/H -> Return to Main Menu\n" +
                " RIGHT/D -> Next Page\n" +
                " LEFT/A -> Last Page";
        pages = new String[]{page0, page1, page2, page3};
    }

    @Override
    protected RootPane createRootPane() {
        RootPane rp = super.createRootPane();
        btnNext = new Button("Next");
        btnBack = new Button("Back");
        btnMainMenu = new Button("MainMenu");

        btnNext.addCallback(new Runnable() {
            public void run() {
                if(page < pages.length - 1) page++;
                else page = 0;
            }
        });

        btnBack.addCallback(new Runnable(){
            public void run() {
                if(page > 0) page--;
                else page = 3;
            }
        });

        btnMainMenu.addCallback(new Runnable() {
            public void run() {
                game.enterState(de.tu_darmstadt.gdi1.gorillas.main.Game.MAINMENUSTATE);
            }
        });


        rp.add(btnNext);
        rp.add(btnBack);
        rp.add(btnMainMenu);
        return rp;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background, -10, -20);
        graphics.setColor(new Color(50,50,50,150));
        graphics.fillRect(0, 0, Gorillas.FRAME_WIDTH, Gorillas.FRAME_HEIGHT);
        graphics.setColor(Color.yellow);
        graphics.drawString(pages[page],100,50);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input in_key = gameContainer.getInput();
        if (in_key.isKeyPressed(Input.KEY_RETURN) || in_key.isKeyPressed(Input.KEY_ESCAPE) || in_key.isKeyPressed(Input.KEY_H)) { game.enterState(Game.MAINMENUSTATE); }
        if (in_key.isKeyPressed(Input.KEY_RIGHT)  || in_key.isKeyPressed(Input.KEY_D)){
            if(page < pages.length - 1) page++;
            else page = 0;
        }
        if (in_key.isKeyPressed(Input.KEY_LEFT) || in_key.isKeyPressed(Input.KEY_A)){
            if(page > 0) page--;
            else page = 3;
        }
    }

    @Override
    protected void layoutRootPane() {
        int paneHeight = this.getRootPane().getHeight();
        int paneWidth = this.getRootPane().getWidth();

        // Layout subject to change
        btnNext.setSize(128, 32);
        btnBack.setSize(128, 32);
        btnMainMenu.setSize(128, 32);
        // Center the Textfields on the screen. Jetzt wird duch 2 geteilt :)
        int x = (paneWidth - btnNext.getWidth()) >> 1;

        btnNext.setPosition(x, 500);
        btnBack.setPosition(x - 140, 500);
        btnMainMenu.setPosition(x + 140, 500);


    }
}
