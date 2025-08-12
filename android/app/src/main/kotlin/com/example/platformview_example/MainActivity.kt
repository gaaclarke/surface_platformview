package com.example.platformview_example

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.RenderMode
import io.flutter.embedding.engine.FlutterEngine

class MainActivity: FlutterActivity() {
    override fun getRenderMode(): RenderMode {
        return RenderMode.surface
    }
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory("native-view", NativeViewFactory(flutterEngine.dartExecutor.binaryMessenger))
    }
}
