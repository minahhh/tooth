package game.juan.andenginegame0;

import android.app.Activity;
import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.DynamicSpriteBatch;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 2017. 10. 10..
 */

public class Stage {
    private static final int CAMERA_WIDTH = 1600;
    private static final int CAMERA_HEIGHT = 2560;

    BitmapTextureAtlas mBitmapTextureAtlas;

    ITextureRegion playerTexture;

    ITextureRegion backgroundTexture1, backgroundTexture2;

    ITextureRegion germTexture;

    ITextureRegion toptoothRegion;

    ITextureRegion jointTextureRegion1, jointTextureRegion2,jointTextureRegion3,jointTextureRegion4, jointTextureRegion5;

    ITextureRegion itemTextureRegion, itemTextureRegion2;
    ITextureRegion slowTextureRegion1, slowTextureRegion2,slowTextureRegion3, slideTextureRegion;




    Player player;

    ArrayList<int[]> germs_xs;
    ArrayList<int[]> germs_ys;

    ArrayList<int[]> obstacle_xs;
    ArrayList<int[]> obstacle_ys;
    ArrayList<int[]> obstacle_types;

    ArrayList<int[]> tem_xs;
    ArrayList<int[]> tem_ys;
    ArrayList<int[]> tem_types;

    public void load(int stage, BaseGameActivity activity){
        germs_xs = new ArrayList<>();
        germs_ys = new ArrayList<>();
        obstacle_xs= new ArrayList<>();
        obstacle_ys= new ArrayList<>();
        obstacle_types = new ArrayList<>();
        tem_xs= new ArrayList<>();
        tem_ys= new ArrayList<>();
        tem_types= new ArrayList<>();
        for(int i=0;i<5;i++) {
            loadSectionData(activity,i+1);
            for(int j=0;j<germs_ys.get(i).length;j++){
                germs_ys.get(i)[j]+=i*2560;
            }
            for(int j=0;j<obstacle_ys.get(i).length;j++){
                obstacle_ys.get(i)[j]+=i*2560;
            }
            for(int j=0;j<tem_ys.get(i).length;j++){
                tem_ys.get(i)[j]+=i*2560;
            }

        }






        loadOnHudImage(activity);
        loadBackgroundImage(stage,activity);
        loadPlayerImage(activity);
        loadObstacleImage(activity);
        loadGermImage(activity);
    }

    private void loadSectionData(Context context,int section){
        try{
            int germs_x[];
            int germs_y[];

            int obstacle_x[];
            int obstacle_y[];
            int obstacle_type[];

            int tem_x[];
            int tem_y[];
            int tem_type[];

            JSONArray germData;
            JSONObject stageObject = loadJSONFromAsset(context,"section"+section+".json").getJSONObject("stage");

            germData = stageObject.getJSONArray("germs");
            int germ_size = germData.length();
            germs_x = new int[germ_size];
            germs_y = new int[germ_size];
            for(int i=0;i<germ_size;i++){
                germs_x[i] = germData.getJSONObject(i).getInt("x");
                germs_y[i] = germData.getJSONObject(i).getInt("y");
            }


            JSONArray obstacleData = stageObject.getJSONArray("obstacle");
            int obstacle_size = obstacleData.length();
            obstacle_x = new int[obstacle_size];
            obstacle_y = new int[obstacle_size];
            obstacle_type = new int[obstacle_size];
            for(int i=0;i<obstacle_size;i++){
                obstacle_x[i] = obstacleData.getJSONObject(i).getInt("x");
                obstacle_y[i] = obstacleData.getJSONObject(i).getInt("y");
                obstacle_type[i] = obstacleData.getJSONObject(i).getInt("type");
            }

            JSONArray temData = stageObject.getJSONArray("tem");
            int tem_size = temData.length();
            tem_x = new int[tem_size];
            tem_y = new int[tem_size];
            tem_type = new int[tem_size];
            for(int i=0;i<tem_size;i++){
                tem_x[i] = temData.getJSONObject(i).getInt("x");
                tem_y[i] = temData.getJSONObject(i).getInt("y");
                tem_type[i] = temData.getJSONObject(i).getInt("type");
            }

            germs_xs.add(germs_x);
            germs_ys.add(germs_y);

            obstacle_xs.add(obstacle_x);
            obstacle_ys.add(obstacle_y);
            obstacle_types.add(obstacle_type);

            tem_xs.add(tem_x);
            tem_ys.add(tem_y);
            tem_types.add(tem_x);
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }
    private static JSONObject loadJSONFromAsset(Context context, String filename){
        String json = null;
        JSONObject object = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            object = new JSONObject(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return object;
    }
    private void loadOnHudImage(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/onhud/");
        BitmapTextureAtlas topBTA = new BitmapTextureAtlas(activity.getTextureManager(),1600,2560);
        toptoothRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(topBTA, activity,"t_t_2.png",0,0 );
        topBTA.load();
    }

    BitmapTextureAtlas background_layer1;
    BitmapTextureAtlas background_layer2;

    private void loadBackgroundImage(int stage, BaseGameActivity activity){

       // this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);


        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/background/");

        background_layer1 = new BitmapTextureAtlas(activity.getTextureManager(),1600,2560);
        backgroundTexture1 = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(background_layer1, activity,"bg_"+stage+"_1.jpg",0,0 );
        background_layer1.load();

         background_layer2 = new BitmapTextureAtlas(activity.getTextureManager(),1600,2560);
        backgroundTexture2 = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(background_layer2, activity,"bg_"+stage+"_2.jpg",0,0 );
        background_layer2.load();
    }
    private void loadPlayerImage(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");

        BitmapTextureAtlas playerBTA = new BitmapTextureAtlas(activity.getTextureManager(),256,256);
        playerTexture = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(playerBTA, activity,"tooth.png",0,0 );
        playerBTA.load();
    }
    private void loadObstacleImage(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/obstacle/");


        //img item
        BitmapTextureAtlas itemTexture = new BitmapTextureAtlas(activity.getTextureManager(),256,128);
        itemTextureRegion = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(itemTexture, activity,"tem_1.png",0,0);
        itemTexture.load();
        BitmapTextureAtlas itemTexture2 = new BitmapTextureAtlas(activity.getTextureManager(),256,128);
        itemTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(itemTexture2, activity,"tem_2.png",0,0);
        itemTexture2.load();

        //img joint
        BitmapTextureAtlas jointTexture2 = new BitmapTextureAtlas(activity.getTextureManager(),320,80);
        jointTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(jointTexture2, activity,"spin_2.png",0,0);
        jointTexture2.load();

        BitmapTextureAtlas jointTexture1 = new BitmapTextureAtlas(activity.getTextureManager(),80,80);
        jointTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(jointTexture1, activity,"spin_1.png",0,0);
        jointTexture1.load();

        BitmapTextureAtlas jointTexture4 = new BitmapTextureAtlas(activity.getTextureManager(),512,80);
        jointTextureRegion4 = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(jointTexture4, activity,"spin_4.png",0,0);
        jointTexture4.load();

        BitmapTextureAtlas jointTexture3= new BitmapTextureAtlas(activity.getTextureManager(),80,80);
        jointTextureRegion3 = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(jointTexture3 , activity,"spin_3.png",0,0);
        jointTexture3 .load();

        BitmapTextureAtlas jointTexture5= new BitmapTextureAtlas(activity.getTextureManager(),640,80);
        jointTextureRegion5 = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(jointTexture5 , activity,"spin_5.png",0,0);
        jointTexture5 .load();


        //img slow
        BitmapTextureAtlas slowTexture1 = new BitmapTextureAtlas(activity.getTextureManager(),256,256);
        slowTextureRegion1 = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(slowTexture1, activity,"slow_1.png",0,0 );
        slowTexture1.load();

        BitmapTextureAtlas slowTexture2 = new BitmapTextureAtlas(activity.getTextureManager(),512,256);
        slowTextureRegion2 = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(slowTexture2, activity,"slow_2.png",0,0 );
        slowTexture2.load();

        BitmapTextureAtlas slowTexture3 = new BitmapTextureAtlas(activity.getTextureManager(),1024,256);
        slowTextureRegion3 = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(slowTexture3, activity,"slow_3.png",0,0 );
        slowTexture3.load();

        //img slide
        BitmapTextureAtlas slideTexture = new BitmapTextureAtlas(activity.getTextureManager(),1024,128);
        slideTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(slideTexture, activity,"slide_1.png",0,0 );
        slideTexture.load();


    }
    private void loadGermImage(BaseGameActivity activity){
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/germ/");

        BitmapTextureAtlas germBTA = new BitmapTextureAtlas(activity.getTextureManager(),128,128);
        germTexture = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(germBTA, activity,"germ.png",0,0 );
        germBTA.load();
    }


    public void create(BaseGameActivity activity, PhysicsWorld world, Scene scene, SmoothCamera camera){
        createOnHud(activity,camera);
        createBackground(activity,scene);
        createWalls(activity,world,scene);
        createPlayer(activity,world,scene,camera);
        createObstacle(activity,world,scene);
        createGerm(activity,world,scene,player);

        setupPhysics(world);
    }

    private void createOnHud(BaseGameActivity activity,SmoothCamera camera){
        Sprite toptooth = new Sprite(0, 0, toptoothRegion, activity.getVertexBufferObjectManager());
        HUD hud = new HUD();
        hud.attachChild(toptooth);
        camera.setHUD(hud);
    }
    private void createBackground(BaseGameActivity activity, Scene scene){


        final SpriteBatch staticSpriteBatch = new SpriteBatch(this.background_layer1, 2, activity.getVertexBufferObjectManager());
        staticSpriteBatch.draw(backgroundTexture1, 0, 0, backgroundTexture1.getWidth(), backgroundTexture1.getHeight(), 1, 1, 1, 1, 1, 1);
        staticSpriteBatch.draw(backgroundTexture1, 0, 2560*2, backgroundTexture1.getWidth(), backgroundTexture1.getHeight(), 1, 1, 1, 1, 1);
        staticSpriteBatch.submit();
        scene.attachChild(staticSpriteBatch);

        final SpriteBatch staticSpriteBatch2 = new SpriteBatch(this.background_layer2, 2, activity.getVertexBufferObjectManager());
        staticSpriteBatch2.draw(backgroundTexture2, 0, 2560, backgroundTexture2.getWidth(), backgroundTexture2.getHeight(), 1, 1, 1, 1, 1, 1);
        staticSpriteBatch2.draw(backgroundTexture2, 0, 2560*3, backgroundTexture2.getWidth(), backgroundTexture2.getHeight(), 1, 1, 1, 1, 1);
        staticSpriteBatch2.submit();
        scene.attachChild(staticSpriteBatch2);

    }
    private void createPlayer(BaseGameActivity activity , final PhysicsWorld world, Scene scene, SmoothCamera camera){
        player = new Player(CAMERA_WIDTH/2,CAMERA_WIDTH/2+200, playerTexture, activity.getVertexBufferObjectManager());
        player.createRectUnit(world,scene,1,1);
//        scene.attachChild(player);
        camera.setChaseEntity(player);

        scene.registerUpdateHandler(new IUpdateHandler(){
            @Override
            public void onUpdate(float pSecondsElapsed) {

                player.getBody().applyLinearImpulse(new Vector2(0, 20), player.getBody().getPosition());
               // world.getBodies(
            }

            @Override
            public void reset() {

            }
        });
    }
    private void createObstacle(BaseGameActivity activity , PhysicsWorld world, Scene scene){
        for(int j=0;j<5;j++) {
            for (int i = 0; i < obstacle_types.get(j).length; i++) {
                switch (obstacle_types.get(j)[i]) {
                    case 0:
                        MapBuilder.create_3bar_Obstacle(scene, world, activity, jointTextureRegion1, jointTextureRegion2, obstacle_xs.get(j)[i], obstacle_ys.get(j)[i]);
                        break;
                    case 1:
                        //MapBuilder.create_2bar_Obstacle(scene,world,activity,jointTextureRegion3,jointTextureRegion4,obstacle_x[i],obstacle_y[i]);
                        break;
                }
            }
        }
        for(int j=0;j<5;j++) {
            for (int i = 0; i < tem_types.get(j).length; i++) {
                switch (tem_types.get(j)[i]) {
                    case 0:
                        MapBuilder.create_item1_Obstacle(scene,world,activity,itemTextureRegion,tem_xs.get(j)[i],tem_ys.get(i)[i]);
                        break;
                    case 1:
                        MapBuilder.create_item2_Obstacle(scene,world,activity,itemTextureRegion2,tem_xs.get(j)[i],tem_ys.get(i)[i]);
                        break;
                    case 2:
                        //MapBuilder.create_item3_Obstacle(scene,world,activity,itemTextureRegion3,tem_xs.get(j)[i],tem_ys.get(i)[i]);
                        break;
                }
            }
        }
    }

    private void createGerm(BaseGameActivity activity , PhysicsWorld world, Scene scene,Player p){
        for(int i=0;i<5;i++){
            MapBuilder.create_germs(scene,world,activity,germTexture,germs_xs.get(i),germs_ys.get(i), p);
        }

    }
    private void createWalls(BaseGameActivity activity, PhysicsWorld physicsWorld,Scene scene){

        final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(1.0F ,1.0F, 1F);
        fixtureDef.filter.maskBits=ConstantsSet.OBS_MASK_BITS;
        fixtureDef.filter.categoryBits=ConstantsSet.OBS_CATG_BITS;
        Rectangle ground_right = new Rectangle(CAMERA_WIDTH,0,0,CAMERA_HEIGHT*100,activity.getVertexBufferObjectManager());
        Rectangle ground_left = new Rectangle(0,0,0,CAMERA_HEIGHT*100,activity.getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(physicsWorld,ground_right, BodyDef.BodyType.StaticBody,fixtureDef);
        scene.attachChild(ground_right);
        PhysicsFactory.createBoxBody(physicsWorld,ground_left, BodyDef.BodyType.StaticBody,fixtureDef);
        scene.attachChild(ground_left);
    }
    private void setupPhysics(PhysicsWorld physicsWorld){
        physicsWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Body bodyA = fixtureA.getBody();
                Object oa = bodyA.getUserData();

                Fixture fixtureB = contact.getFixtureB();
                Body bodyB = fixtureB.getBody();
                Object ob = bodyB.getUserData();

                if(oa!=null && ob!=null){
                    int typeA = ((EData)oa).getType();
                    int typeB = ((EData)ob).getType();
                    if(typeA==EData.TYPE_GERM && typeB == EData.TYPE_PLAYER){
                        ((EData)oa).setShould_remove();
                    }else if(typeB==EData.TYPE_GERM && typeA == EData.TYPE_PLAYER){
                        ((EData)ob).setShould_remove();
                    }

                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

}
