package com.Util;

public class Sounds {

    public  SoundUtilityJOAL soundJOAL;
    public  String[] soundNames;
    private String BackPath = "Background/", soundPath = "SoundEffects/";
    private Thread thread;
    private boolean stop;

    public Sounds(){
        soundNames = new String[10];
        loadSound(); // load sound
    }

    public void loadSound(){
        soundJOAL = new SoundUtilityJOAL();
        for(int i = 1; i<11; i++){
            String soundName = BackPath + "sound" + i;
            soundNames[i-1] = soundName;
            if(!soundJOAL.load(soundName, 0, 0,0, true )){
                System.out.println(soundName + " not loaded :(");
            }
        }

        String[] soundEffects = {"check", "invalid", "kill", "lose", "move", "win"};
        for(String s : soundEffects){
            if(!soundJOAL.load(soundPath + s, 0, 0,0, true )){
                System.out.println(soundPath + s + " not loaded :(");
            }
        }
    }

    public void playSound(String soundName){
        soundJOAL.play(soundName);
    }

    public void stopSound(String soundName) {
        soundJOAL.pause(soundName);
    }

    public String[] getSoundNames() {
        return soundNames;
    }

    public void validMove(){
        String sound = "kill";
        soundJOAL.play(soundPath + sound);
        try{
            Thread.sleep(150);
        }catch (InterruptedException e){
            System.out.println(e);
        }
        soundJOAL.stop(soundPath + sound);
    }

    public void inValidMove(){
        String sound = "invalid";
        soundJOAL.play(soundPath + sound);
        try{
            Thread.sleep(450);
        }catch (InterruptedException e){
            System.out.println(e);
        }
        soundJOAL.stop(soundPath + sound);
    }

    public void gameWon(){
        String sound = "win";
        soundJOAL.play(soundPath + sound);
        try{
            Thread.sleep(1500);
        }catch (InterruptedException e){
            System.out.println(e);
        }
        soundJOAL.stop(soundPath + sound);
    }

    public void movePiece(){
        String sound = "move";
        soundJOAL.play(soundPath + sound);
        try{
            Thread.sleep(450);
        }catch (InterruptedException e){
            System.out.println(e);
        }
        soundJOAL.stop(soundPath + sound);
    }

}
