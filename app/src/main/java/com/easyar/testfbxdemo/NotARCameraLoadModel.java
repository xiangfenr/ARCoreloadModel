package com.easyar.testfbxdemo;

import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

/**
 * 没有AR相机
 * 通过 SceneView 加载模型
 */
public class NotARCameraLoadModel extends AppCompatActivity {

    private Scene scene;
    private Node cupCakeNode;
    private SceneView sceneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notarcamera_loadmodel_layout);
        sceneView = findViewById(R.id.sceneView);
        scene = sceneView.getScene();
        renderObject(Uri.parse("andy.sfb"));
    }

    /**
     * load the 3D model in the space
     *
     * @param parse - URI of the model, imported using Sceneform plugin
     */
    private void renderObject(Uri parse) {

        ModelRenderable.builder()
                .setSource(this, parse)
                .build()
                .thenAccept(modelRenderable -> {
                    addNodeToScene(modelRenderable);
                })
                .exceptionally(throwable -> {
                    Toast toast = Toast.makeText(this, "报错==" + throwable.getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
    }

    /**
     * Adds a node to the current scene
     *
     * @param model - rendered model
     */
    private void addNodeToScene(ModelRenderable model) {
        cupCakeNode = new Node();
        cupCakeNode.setParent(scene);
        Vector3 localPosition = new Vector3(0f, 0f, -1f);
        Vector3 localScale = new Vector3(3f, 3f, 3f);
        cupCakeNode.setLocalPosition(localPosition);
        cupCakeNode.setLocalScale(localScale);
        cupCakeNode.setRenderable(model);
        scene.addChild(cupCakeNode);
    }

    protected void onPause() {
        super.onPause();
        sceneView.pause();
    }

    protected void onResume() {
        super.onResume();
        try {
            sceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
