package de.tu_darmstadt.gdi1.gorillas.utils;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class FakeSound extends Sound{


    public FakeSound(String ref) throws SlickException {
        super(ref);
    }

    // Java why :(
   //  public FakeSound(){
   //     try {
   //         super(null, null);
   //     } catch (Exception e) {}
   // }

    public void play() {}
    public void play(float pitch, float volume) {}
    public void playAt(float x, float y, float z) {}
    public void playAt(float pitch, float volume, float x, float y, float z) {}
    public void loop() {}
    public void loop(float pitch, float volume) {}
    public boolean playing() {return false;}
    public void stop() {}

}
