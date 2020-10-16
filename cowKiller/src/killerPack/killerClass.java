package killerPack;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;

import java.awt.*;


@ScriptManifest(name = "Cow cock", description = "flaming hot garbage probably gona get banned", author = "michoo",
        version = 1.0, category = Category.WOODCUTTING, image = "")

public class killerClass extends AbstractScript {

    Area cowArea = new Area(3253, 3255, 3265, 3293, 0);
    // int howMuchFood = getInventory().count("Cooked meat");



    @Override
    public void onStart(){
        log("Starting");
    }


    @Override
    public int onLoop(){



        getInventory().interact("Burnt meat", "Drop");
        int howMuchFood = getInventory().count("Cooked meat");
        if(howMuchFood>=1) {
            if (cowArea.contains(getLocalPlayer())) {
                pickUpBone();
                attackCow();
            } else if(!cowArea.contains(getLocalPlayer())){
                if (getWalking().walk(cowArea.getRandomTile())) {
                    sleep(Calculations.random(3000, 5500));
                }
            }
        }else {
            log("restocking");
        restock();
        }/*else {
            GameObject gate = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Gate"));
            getWalking().walk(3254, 3267, 0);
            if (gate.hasAction("Open")){
                gate.interact("Open");
                sleepUntil(() -> gate.hasAction("Close"), 10000);
            }
            restock();
            */


        return 600;
    }

    public void attackCow(){
        NPC victim = getNpcs().closest(npc -> npc != null && npc.hasAction("Attack"));
        int health=getSkills().getBoostedLevels(Skill.HITPOINTS);;
        if(health<=6){
            getInventory().interact("Cooked meat","Eat");
        }else {
            if (victim != null & victim.interact("Attack")) {
                sleepUntil(() -> !getLocalPlayer().isInCombat(), 30000);
            }
        }
    }

    public void pickUpBone(){
        GroundItem bones = getGroundItems().closest("Bones");
        if (bones != null) {
            if (!getLocalPlayer().isAnimating()) {
                if (bones.distance(getLocalPlayer()) < 3) {
                    if (bones.interact("Take")) {
                        sleepUntil(() -> !bones.exists(), 15000);
                        getInventory().interact("Bones","Bury");
                    }
                }
            }
        }
    }

    public void restock() {
        GroundItem rawMeat = getGroundItems().closest("Raw beef");
        if (rawMeat != null) {
            if (rawMeat.distance(getLocalPlayer()) < 300) {

                while(getInventory().count("Raw beef")<12) {
                    rawMeat = getGroundItems().closest("Raw beef");
                    rawMeat.interact("Take");
                }

                GameObject gate = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Gate"));

                getWalking().walk(3254, 3267, 0);
sleepUntil(() -> !getInventory().isFull(), 5000);
                if (gate.hasAction("Open")) {
                    gate.interact("Open");
                    sleepUntil(() -> gate.hasAction("Close"), 10000);
                    getWood();
                }else{
                    getWood();
                }
             //   getWood();
                // cookMeat();
            }
        }

        //}
    }

    public void getWood() {
        GameObject tree = getGameObjects().closest(gameObject -> gameObject != null && gameObject.getName().equals("Tree"));

        while (getInventory().count("Logs") < 1) {
            if (tree != null && tree.interact("Chop down")) {
                sleepUntil(() -> getInventory().contains("Logs"), 5000);
            }
        }
        makeFire();
    }


    public void makeFire () {
        if (getInventory().interact("Logs", "Use")) {
            getInventory().interact("Tinderbox", "Use");
            sleepUntil(() -> getInventory().isFull(), 1000);
            sleepUntil(() -> !getLocalPlayer().isAnimating(), 150000);
            sleepUntil(() -> getInventory().isFull(), 1000);
        }
        GameObject fire = getGameObjects().closest("Fire");

        if (getInventory().interact("Raw beef", "Use")) {
            if (fire.interact("Use")) {
                sleepUntil(() -> getInventory().isFull(), 1000);
                if (getWidgets().getWidgetChild(270, 14) != null) {
                    getWidgets().getWidgetChild(270, 14).interact();
                    sleepUntil(() -> !getInventory().contains("Raw beef"), 30000);
                }
            }
        }
        sleepUntil(() -> !getLocalPlayer().isAnimating(), 15000);
    }


    @Override
    public void onExit() {
        log("Bye");
    }

}
